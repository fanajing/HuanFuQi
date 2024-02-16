package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import java.io.File;
import java.io.IOException;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
////////////////////////////////////////////////////////////////////
//                          _ooOoo_                               //
//                         o8888888o                              //
//                         88" . "88                              //
//                         (| ^_^ |)                              //
//                         O\  =  /O                              //
//                      ____/`---'\____                           //
//                    .'  \\|     |//  `.                         //
//                   /  \\|||  :  |||//  \                        //
//                  /  _||||| -:- |||||-  \                       //
//                  |   | \\\  -  /// |   |                       //
//                  | \_|  ''\---/''  |   |                       //
//                  \  .-\__  `-`  ___/-. /                       //
//                ___`. .'  /--.--\  `. . ___                     //
//              ."" '<  `.___\_<|>_/___.'  >'"".                  //
//            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
//            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
//      ========`-.____`-.___\_____/___.-`____.-'========         //
//                           `=---='                              //
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//             佛祖保佑       永不宕机      永无BUG               //
////////////////////////////////////////////////////////////////////
@RequiresApi(api = Build.VERSION_CODES.R)
public class MainActivity<i> extends AppCompatActivity {
    private static boolean officialDirAuthed = false;
    private static boolean bilibiliDirAuthed = false;
    private static boolean xiaomiDirAuthed = false;
    private static boolean guojiDirAuthed = false;
    private static boolean jianceDirAuthed = false;
    // 这里添加你的App Key、Secret Key和Sign Key
    private String appKey = "MVfWdMiAkestxgh3ohzjESmb6uuUbhTw";
    private String secretKey = "5smtisTQ7z0p6O4h9hyH3PbxnCGoG8Ch";
    private String signKey = "65#atFnd5qfAgKu35V4@hY1kBzo-00om";


    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
    };

    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;
    private boolean demoButtonClicked = true;
    public static final String OFFICIAL_NAME = "com.miHoYo.Yuanshen";
    public static final String BILIBILI_NAME = "com.miHoYo.ys.bilibili";
    public static final String GUOJI_NAME = "com.miHoYo.GenshinImpact";
    public static final String XIAOMI_NAME = "com.miHoYo.ys.mi";
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); //隐藏标题栏
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        // android 11 申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (!Environment.isExternalStorageManager()) {
                Toast.makeText(this, "请通过权限！", Toast.LENGTH_SHORT);
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1024);
            }
        }
        // 获取应用的版本号
        String appVersion = getAppVersion();

        // 找到 TextView4 控件
        TextView textView4 = findViewById(R.id.textView4);

        // 设置版本号到 TextView4 中
        textView4.setText("版本号: " + appVersion);
    }

    private String getAppVersion() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "未知";
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }
    public static boolean checkApkExist(Context context, String packageName){
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(packageName,PackageManager.MATCH_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    //检测文件位置
    public void jiance(View view) {
        demoButtonClicked=false;
        // 检查是否具有读写外部存储的权限
        String externalStoragePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/";
        String officialDirName = externalStoragePath + OFFICIAL_NAME;
        String bilibiliDirName = externalStoragePath + BILIBILI_NAME;
        String xiaomiDirName = externalStoragePath + XIAOMI_NAME;
        String guojiDirName = externalStoragePath + GUOJI_NAME;
        if(!officialDirAuthed) {
            startFor(officialDirName, 10);
        }
        if(!bilibiliDirAuthed) {
            startFor(bilibiliDirName, 11);
        }
        if(!xiaomiDirAuthed) {
            startFor(xiaomiDirName, 12);
        }
        if(!guojiDirAuthed) {
            startFor(guojiDirName, 13);
        }
        // 获取官服文件
        DocumentFile officialDir = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(officialDirName)));
        // 获取b服文件
        DocumentFile bilibiliDir = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(bilibiliDirName)));
        // 获取米服文件
        DocumentFile xiaomiDir = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(xiaomiDirName)));
        // 获取国际服文件
        DocumentFile guojiDir = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(guojiDirName)));
        // 获取每个文件夹中文件数量
        int count1 = countFiles(officialDir);
        int count2 = countFiles(bilibiliDir);
        int count3 = countFiles(xiaomiDir);
        int count4 = countFiles(guojiDir);
        // 用于记录有文件的文件夹数量
        int foldersWithFiles = 0;

        // 检查官服文件夹
        if (officialDir != null && officialDir.exists() && officialDir.isDirectory()) {
            int count = countFiles(officialDir);
            if (count > 0) {
                foldersWithFiles++;
                Toast.makeText(MainActivity.this, "文件在官服", Toast.LENGTH_SHORT).show();
            }
        }

        // 检查b服文件夹
        else if (bilibiliDir != null && bilibiliDir.exists() && bilibiliDir.isDirectory()) {
            int count = countFiles(bilibiliDir);
            if (count > 0) {
                foldersWithFiles++;
                Toast.makeText(MainActivity.this, "文件在b服", Toast.LENGTH_SHORT).show();
            }
        }

        // 检查米服文件夹
       else if (xiaomiDir != null && xiaomiDir.exists() && xiaomiDir.isDirectory()) {
            int count = countFiles(xiaomiDir);
            if (count > 0) {
                foldersWithFiles++;
                Toast.makeText(MainActivity.this, "文件在米服", Toast.LENGTH_SHORT).show();
            }
        }

        // 检查国际服文件夹
       else if (guojiDir != null && guojiDir.exists() && guojiDir.isDirectory()) {
            int count = countFiles(guojiDir);
            if (count > 0) {
                foldersWithFiles++;
                Toast.makeText(MainActivity.this, "文件在国际服", Toast.LENGTH_SHORT).show();
            }
        }

        // 根据有文件的文件夹数量进行提示
        if (foldersWithFiles == 0) {
            Toast.makeText(MainActivity.this, "无文件", Toast.LENGTH_SHORT).show();
        } else if (foldersWithFiles > 1) {
            int maxCount = Math.max(Math.max(count1, count2), Math.max(count3, count4));

            if (maxCount == count1 && count1 != 0) {
                String message = "删除";
                if (count2 != 0) {
                    message += "b服";
                }
                if (count3 != 0) {
                    message += "米服";
                }
                if (count4 != 0) {
                    message += "国际服";
                }
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            } else if (maxCount == count2 && count2 != 0) {
                String message = "删除";
                if (count1 != 0) {
                    message += "官服";
                }
                if (count3 != 0) {
                    message += "米服";
                }
                if (count4 != 0) {
                    message += "国际服";
                }
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            } else if (maxCount == count3 && count3 != 0) {
                String message = "删除";
                if (count1 != 0) {
                    message += "官服";
                }
                if (count2 != 0) {
                    message += "b服";
                }
                if (count4 != 0) {
                    message += "国际服";
                }
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            } else if (maxCount == count4 && count4 != 0) {
                String message = "删除";
                if (count1 != 0) {
                    message += "官服";
                }
                if (count2 != 0) {
                    message += "b服";
                }
                if (count3 != 0) {
                    message += "米服";
                }
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

        }
    }
    // 计算文件夹中文件数量的方法
    private int countFiles(DocumentFile folder) {
        int count = 0;
        DocumentFile[] files = folder.listFiles();

        if (files != null) {
            for (DocumentFile file : files) {
                if (file.isFile()) {
                    count++;
                } else if (file.isDirectory()) {
                    count += countFiles(file); // 递归调用以计算子文件夹中的文件数量
                }
            }
        }

        return count;
    }

    //删除

    public void delete(View view) {
        if (!demoButtonClicked) {
            // 创建AlertDialog构造器Builder对象，AlertDialog建议使用android.support.v7.app包下的。
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // 设置对话框标题
            builder.setTitle("请选择要删除的服务器");
            // 设置对话框图标
            builder.setIcon(R.drawable.ic_launcher);
            final String[] servers = new String[]{"官服", "b服", "米服", "国际服"};
            // 记录用户选择的服务器
            int[] selectedServer = {-1};
            // 设置单选选项
            builder.setSingleChoiceItems(servers, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 记录用户选择的服务器
                    selectedServer[0] = which;
                }
            });
            // 添加确定按钮
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 根据用户的选择执行删除操作
                    if (selectedServer[0] != -1) {
                        handleServerDeletion(servers[selectedServer[0]]);
                    }
                }
            });

            // 创建并显示对话框
            builder.show();
        } else {
            Toast.makeText(MainActivity.this, "请先点击 检测(授权) 按钮", Toast.LENGTH_SHORT).show();
        }
        demoButtonClicked=true;
    }
    private void handleServerDeletion(String selectedServer) {
        // 根据用户选择的服务器执行相应的删除操作
        if ("官服".equals(selectedServer)) {
            deleteofficial();
        } else if ("b服".equals(selectedServer)) {
            deletebilibili();
        } else if ("米服".equals(selectedServer)) {
            deleteMI();
        } else if ("国际服".equals(selectedServer)) {
            deleteGUOJI();
        }
    }
    //删除il2cpp
    public void deleteil2cpp(View view) {
        Toast.makeText(this, "正在执行，请根据提示点击！", Toast.LENGTH_SHORT).show();
        //创建AlertDialog构造器Builder对象，AlertDialog建议使用android.support.v7.app包下的。
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框标题
        builder.setTitle("请选择要修复的服务器");
        //设置对话框图标
        builder.setIcon(R.drawable.ic_launcher);
        final String[] sexs = new String[]{"官服", "b服","米服","国际服"};
        //设置单选选项
        builder.setSingleChoiceItems(sexs, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "准备修复："+sexs[which], Toast.LENGTH_SHORT).show();
                if(sexs[which]=="官服"){
                    deleteil2cppguan();
                }else if(sexs[which]=="b服"){
                    deleteil2cppB();
                }else if(sexs[which]=="米服"){
                    deleteil2cppMI();
                }else if(sexs[which]=="国际服"){
                    deleteil2cppGUO();
                }

            }
        });
        //创建并显示对话框
        builder.show();

    }
    //新的更新检测
//    public void demo(View view) {
//        // 获取 GitHub 上 version.txt 文件内容
//        String versionUrl = "https://raw.githubusercontent.com/<your_username>/<your_repo>/master/version.txt";
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(versionUrl)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String versionString = response.body().string();
//                    int latestVersion = Integer.parseInt(versionString);
//                    int currentVersion = BuildConfig.VERSION_CODE;
//
//                    if (latestVersion > currentVersion) {
//                        // 显示更新对话框
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                showUpdateDialog();
//                            }
//                        });
//                    } else {
//                        // 显示无更新消息
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(MainActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }
//            }
//
//            // ... 异常处理等代码
//        });
//    }
    //教程
    public void jc(View view) {
        Toast.makeText(this, "打开2p即可", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "正在跳转视频", Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse("https://www.bilibili.com/video/BV1dV4y1Y76W");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        return;

    }

    //更新
    public void gx(View view) {
        Toast.makeText(this, "密码已经复制到剪贴板了", Toast.LENGTH_SHORT).show();
        copyToClipboard("1234");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框标题
        builder.setTitle("点击下方跳转下载链接");
        //设置对话框图标
        builder.setIcon(R.drawable.ic_launcher);
        final String[] sexs = new String[]{"下载"};
        //设置单选选项
        builder.setSingleChoiceItems(sexs, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gxx();
            }
        });
        //创建并显示对话框
        builder.show();
        return;
    }
    public void gxx() {
        Uri uri = Uri.parse("https://www.123pan.com/s/b6L1jv-WJTE.html");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        // 显示复制成功的提示
    }
    //复制文本
    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(text);
    }
    //官服修复
    public boolean deleteil2cppguan() {
        String externalStoragePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/";
        String officialDirName = externalStoragePath + OFFICIAL_NAME+"/files/il2cpp";
        if(!officialDirAuthed) {
            startFor(officialDirName, 10);
            return false;
        }
        DocumentFile officialDir = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(officialDirName)));
        Toast.makeText(this, "官服资源文件修复成功！", Toast.LENGTH_SHORT).show();
        officialDir.delete();
        return false;
    }
    //b服修复
    public boolean deleteil2cppB() {
        String externalStoragePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/";
        String bilibiliDirName = externalStoragePath + BILIBILI_NAME+"/files/il2cpp";
        if(!bilibiliDirAuthed) {
            startFor(bilibiliDirName, 11);
            return false;
        }
        DocumentFile bilibiliDir = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(bilibiliDirName)));
        Toast.makeText(this, "B服资源文件修复成功！", Toast.LENGTH_SHORT).show();
        bilibiliDir.delete();
        return false;
    }
    //米服修复
    public boolean deleteil2cppMI() {
        String externalStoragePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/";
        String xiaomiDirName = externalStoragePath + XIAOMI_NAME+"/files/il2cpp";
        if(!xiaomiDirAuthed) {
            startFor(xiaomiDirName, 12);
            return false;
        }
        DocumentFile xiaomiDir = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(xiaomiDirName)));
        Toast.makeText(this, "米服资源文件修复成功！", Toast.LENGTH_SHORT).show();
        xiaomiDir.delete();
        return false;
    }
    //国际服修复
    public boolean deleteil2cppGUO() {
        String externalStoragePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/";
        String guojiDirName = externalStoragePath + GUOJI_NAME+"/files/il2cpp";
        if(!guojiDirAuthed) {
            startFor(guojiDirName, 13);
            return false;
        }
        DocumentFile guojiDir = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(guojiDirName)));
        Toast.makeText(this, "国际服资源文件修复成功！", Toast.LENGTH_SHORT).show();
        guojiDir.delete();
        return false;
    }

    //删除
    public boolean deleteofficial() {
        String externalStoragePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/";
        String officialDirName = externalStoragePath + OFFICIAL_NAME;
        if(!officialDirAuthed) {
            startFor(officialDirName, 10);
            return false;
        }
        DocumentFile officialDir = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(officialDirName)));
        Toast.makeText(this, "官服资源文件删除成功！", Toast.LENGTH_SHORT).show();
        officialDir.delete();
        return false;
    }
    public boolean deletebilibili() {
        String externalStoragePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/";
        String bilibiliDirName = externalStoragePath + BILIBILI_NAME;
        if(!bilibiliDirAuthed) {
            startFor(bilibiliDirName, 11);
            return false;
        }
        DocumentFile bilibiliDir = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(bilibiliDirName)));
        Toast.makeText(this, "B服资源文件删除成功！", Toast.LENGTH_SHORT).show();
        bilibiliDir.delete();
        return false;
    }
    public boolean deleteMI() {
        String externalStoragePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/";
        String xiaomiDirName = externalStoragePath + XIAOMI_NAME;
        if(!xiaomiDirAuthed) {
            startFor(xiaomiDirName, 12);
            return false;
        }
        DocumentFile xiaomiDir = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(xiaomiDirName)));
        Toast.makeText(this, "米服资源文件删除成功！", Toast.LENGTH_SHORT).show();
        xiaomiDir.delete();
        return false;
    }
    public boolean deleteGUOJI() {
        String externalStoragePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/";
        String guojiDirName = externalStoragePath + GUOJI_NAME;
        if(!guojiDirAuthed) {
            startFor(guojiDirName, 13);
            return false;
        }
        DocumentFile guojiDir = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(guojiDirName)));
        Toast.makeText(this, "国服资源文件删除成功！", Toast.LENGTH_SHORT).show();
        guojiDir.delete();
        return false;
    }
int i;


    //保存数据
    public void save(View view) {
        x=1;
        //创建AlertDialog构造器Builder对象，AlertDialog建议使用android.support.v7.app包下的。
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框标题
        builder.setTitle("请选择你现在有数据的服务器");
        //设置对话框图标
        builder.setIcon(R.drawable.ic_launcher);
        final String[] sexs = new String[]{"官服", "b服", "米服", "国际服"};
        //设置单选选项
        builder.setSingleChoiceItems(sexs, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击“确认”后的操作

                switch (sexs[which]) {
                    case "官服":
                        i = 1;
                        break;
                    case "b服":
                        i = 2;
                        break;
                    case "米服":
                        i = 3;
                        break;
                    case "国际服":
                        i = 4;
                        break;
                }
            }
        });

        builder.setPositiveButton("确定", null);
        //创建并显示对话框
        builder.show();

    }


    public void startOfficial(View view) {
        if(x == 1) {
            String externalStoragePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/";
            String officialDirName = externalStoragePath + OFFICIAL_NAME;
            String bilibiliDirName = externalStoragePath + BILIBILI_NAME;
            String xiaomiDirName = externalStoragePath + XIAOMI_NAME;
            String guojiDirName = externalStoragePath + GUOJI_NAME;
            if (i == 1) {
                Toast.makeText(this, "正在进行直接启动", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClassName(OFFICIAL_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
//                exit();
            }
            if (i == 2) {
                Toast.makeText(this, "正在进行B服转官服", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", externalStoragePath);
                File bilibiliDir = new File(bilibiliDirName);
                File officialDir = new File(officialDirName);
                if (!officialDir.exists() && bilibiliDir.exists()) {
                    Log.i("MainActivity", "exists");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (bilibiliDir.exists() && !bilibiliDirAuthed) {
                            Toast.makeText(this, "请通过权限后再次点击启动按钮！", Toast.LENGTH_SHORT).show();
                            startFor(bilibiliDirName, 11);
                            return;
                        }
                        DocumentFile documentFile = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(bilibiliDirName)));
                        System.out.println(documentFile.renameTo(OFFICIAL_NAME));
                    } else {
                        System.out.println(bilibiliDir.renameTo(officialDir));
                    }
                }

                Intent intent = new Intent();
                intent.setClassName(OFFICIAL_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
    //            exit();
            }
            if (i == 3) {
                Toast.makeText(this, "正在进行米服转官服", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", externalStoragePath);
                File xiaomiDir = new File(xiaomiDirName);
                File officialDir = new File(officialDirName);
                if (!officialDir.exists() && xiaomiDir.exists()) {
                    Log.i("MainActivity", "exists");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (xiaomiDir.exists() && !xiaomiDirAuthed) {
                            Toast.makeText(this, "请通过权限后再次点击启动按钮！", Toast.LENGTH_SHORT).show();
                            startFor(xiaomiDirName, 12);
                            return;
                        }
                        DocumentFile documentFile = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(xiaomiDirName)));
                        System.out.println(documentFile.renameTo(OFFICIAL_NAME));
                    } else {
                        System.out.println(xiaomiDir.renameTo(officialDir));
                    }
                }
                Intent intent = new Intent();
                intent.setClassName(OFFICIAL_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
              //  exit();
            }
            if (i == 4) {
                Toast.makeText(this, "正在进行国服转官服", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", externalStoragePath);
                File guojiDir = new File(guojiDirName);
                File officialDir = new File(officialDirName);
                if (!officialDir.exists() && guojiDir.exists()) {
                    Log.i("MainActivity", "exists");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (guojiDir.exists() && !guojiDirAuthed) {
                            Toast.makeText(this, "请通过权限后再次点击启动按钮！", Toast.LENGTH_SHORT).show();
                            startFor(guojiDirName, 13);
                            return;
                        }
                        DocumentFile documentFile = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(guojiDirName)));
                        System.out.println(documentFile.renameTo(OFFICIAL_NAME));
                    } else {
                        System.out.println(guojiDir.renameTo(officialDir));
                    }
                }
                Intent intent = new Intent();
                intent.setClassName(OFFICIAL_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
               // exit();
            }
        }else{
            Toast.makeText(this, "错误：请先选择已有服务器", Toast.LENGTH_SHORT).show();
        }
    }
    public void startBilibili(View view) {
        if (x == 1) {
            String externalStoragePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/";
            String officialDirName = externalStoragePath + OFFICIAL_NAME;
            String bilibiliDirName = externalStoragePath + BILIBILI_NAME;
            String xiaomiDirName = externalStoragePath + XIAOMI_NAME;
            String guojiDirName = externalStoragePath + GUOJI_NAME;
            if (i == 1) {
                Toast.makeText(this, "正在进行官服转B服", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", externalStoragePath);
                File officialDir = new File(officialDirName);
                File bilibiliDir = new File(bilibiliDirName);
                if (!bilibiliDir.exists() && officialDir.exists()) {
                    Log.i("MainActivity", "exists");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (officialDir.exists() && !officialDirAuthed) {
                            Toast.makeText(this, "请通过权限后再次点击启动按钮！", Toast.LENGTH_SHORT).show();
                            startFor(officialDirName, 10);
                            return;
                        }
                        DocumentFile documentFile = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(officialDirName)));
                        System.out.println(documentFile.renameTo(BILIBILI_NAME));
                    } else {
                        System.out.println(officialDir.renameTo(bilibiliDir));
                    }
                }

                Intent intent = new Intent();
                intent.setClassName(BILIBILI_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
               // exit();
            }
            if (i == 2) {
                Toast.makeText(this, "正在进行直接启动", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClassName(BILIBILI_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
              //  exit();

            }
            if (i == 3) {
                Toast.makeText(this, "正在进行米服转B服", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", externalStoragePath);
                File xiaomiDir = new File(xiaomiDirName);
                File bilibiliDir = new File(bilibiliDirName);
                if (!bilibiliDir.exists() && xiaomiDir.exists()) {
                    Log.i("MainActivity", "exists");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (xiaomiDir.exists() && !xiaomiDirAuthed) {
                            Toast.makeText(this, "请通过权限后再次点击启动按钮！", Toast.LENGTH_SHORT).show();
                            startFor(xiaomiDirName, 12);
                            return;
                        }
                        DocumentFile documentFile = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(xiaomiDirName)));
                        System.out.println(documentFile.renameTo(BILIBILI_NAME));
                    } else {
                        System.out.println(xiaomiDir.renameTo(bilibiliDir));
                    }
                }
                Intent intent = new Intent();
                intent.setClassName(BILIBILI_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
             //   exit();
            }
            if (i == 4) {
                Toast.makeText(this, "正在进行国服转B服", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", externalStoragePath);
                File guojiDir = new File(guojiDirName);
                File bilibiliDir = new File(bilibiliDirName);
                if (!bilibiliDir.exists() && guojiDir.exists()) {
                    Log.i("MainActivity", "exists");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (guojiDir.exists() && !guojiDirAuthed) {
                            Toast.makeText(this, "请通过权限后再次点击启动按钮！", Toast.LENGTH_SHORT).show();
                            startFor(guojiDirName, 13);
                            return;
                        }
                        DocumentFile documentFile = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(guojiDirName)));
                        System.out.println(documentFile.renameTo(BILIBILI_NAME));
                    } else {
                        System.out.println(guojiDir.renameTo(bilibiliDir));
                    }
                }
                Intent intent = new Intent();
                intent.setClassName(BILIBILI_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
             //   exit();
            }
        }else{
            Toast.makeText(this, "错误：请先选择已有服务器", Toast.LENGTH_SHORT).show();
        }
    }
    public void startMi(View view) {
        if(x==1) {
            String externalStoragePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/";
            String officialDirName = externalStoragePath + OFFICIAL_NAME;
            String bilibiliDirName = externalStoragePath + BILIBILI_NAME;
            String xiaomiDirName = externalStoragePath + XIAOMI_NAME;
            String guojiDirName = externalStoragePath + GUOJI_NAME;
            if (i == 1) {
                Toast.makeText(this, "正在进行官服转米服", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", externalStoragePath);
                File officialDir = new File(officialDirName);
                File xiaomiDir = new File(xiaomiDirName);
                if (!xiaomiDir.exists() && officialDir.exists()) {
                    Log.i("MainActivity", "exists");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (officialDir.exists() && !officialDirAuthed) {
                            Toast.makeText(this, "请通过权限后再次点击启动按钮！", Toast.LENGTH_SHORT).show();
                            startFor(officialDirName, 10);
                            return;
                        }
                        DocumentFile documentFile = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(officialDirName)));
                        System.out.println(documentFile.renameTo(XIAOMI_NAME));
                    } else {
                        System.out.println(officialDir.renameTo(xiaomiDir));
                    }
                }

                Intent intent = new Intent();
                intent.setClassName(XIAOMI_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
            //    exit();
            }
            if (i == 2) {
                Toast.makeText(this, "正在进行B服转米服", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", externalStoragePath);
                File bilibiliDir = new File(bilibiliDirName);
                File xiaomiDir = new File(xiaomiDirName);
                if (!xiaomiDir.exists() && bilibiliDir.exists()) {
                    Log.i("MainActivity", "exists");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (bilibiliDir.exists() && !bilibiliDirAuthed) {
                            Toast.makeText(this, "请通过权限后再次点击启动按钮！", Toast.LENGTH_SHORT).show();
                            startFor(bilibiliDirName, 14);
                            return;
                        }
                        DocumentFile documentFile = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(bilibiliDirName)));
                        System.out.println(documentFile.renameTo(XIAOMI_NAME));
                    } else {
                        System.out.println(bilibiliDir.renameTo(xiaomiDir));
                    }
                }
                Intent intent = new Intent();
                intent.setClassName(XIAOMI_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
               // exit();
            }


            if (i == 3) {
                Toast.makeText(this, "正在进行直接启动", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClassName(XIAOMI_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
            //    exit();

            }
            if (i == 4) {
                Toast.makeText(this, "正在进行国服转米服", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", externalStoragePath);
                File guojiDir = new File(guojiDirName);
                File xiaomiDir = new File(xiaomiDirName);
                if (!xiaomiDir.exists() && guojiDir.exists()) {
                    Log.i("MainActivity", "exists");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (guojiDir.exists() && !guojiDirAuthed) {
                            Toast.makeText(this, "请通过权限后再次点击启动按钮！", Toast.LENGTH_SHORT).show();
                            startFor(guojiDirName, 13);
                            return;
                        }
                        DocumentFile documentFile = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(guojiDirName)));
                        System.out.println(documentFile.renameTo(XIAOMI_NAME));
                    } else {
                        System.out.println(guojiDir.renameTo(xiaomiDir));
                    }
                }
                Intent intent = new Intent();
                intent.setClassName(XIAOMI_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
            //    exit();
            }
        }else{
            Toast.makeText(this, "错误：请先选择已有服务器", Toast.LENGTH_SHORT).show();
        }
    }
    public void startInternational(View view) {
        if(x==1) {
            String externalStoragePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/";
            String officialDirName = externalStoragePath + OFFICIAL_NAME;
            String bilibiliDirName = externalStoragePath + BILIBILI_NAME;
            String xiaomiDirName = externalStoragePath + XIAOMI_NAME;
            String guojiDirName = externalStoragePath + GUOJI_NAME;
            if (i == 1) {
                Toast.makeText(this, "正在进行官服转国服", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", externalStoragePath);
                File officialDir = new File(officialDirName);
                File guojiDir = new File(guojiDirName);
                if (!guojiDir.exists() && officialDir.exists()) {
                    Log.i("MainActivity", "exists");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (officialDir.exists() && !officialDirAuthed) {
                            Toast.makeText(this, "请通过权限后再次点击启动按钮！", Toast.LENGTH_SHORT).show();
                            startFor(officialDirName, 10);
                            return;
                        }
                        DocumentFile documentFile = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(officialDirName)));
                        System.out.println(documentFile.renameTo(GUOJI_NAME));
                    } else {
                        System.out.println(officialDir.renameTo(guojiDir));
                    }
                }

                Intent intent = new Intent();
                intent.setClassName(GUOJI_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
           //     exit();
            }
            if (i == 2) {
                Toast.makeText(this, "正在进行B服转国服", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", externalStoragePath);
                File bilibiliDir = new File(bilibiliDirName);
                File guojiDir = new File(guojiDirName);
                if (!guojiDir.exists() && bilibiliDir.exists()) {
                    Log.i("MainActivity", "exists");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (bilibiliDir.exists() && !bilibiliDirAuthed) {
                            Toast.makeText(this, "请通过权限后再次点击启动按钮！", Toast.LENGTH_SHORT).show();
                            startFor(bilibiliDirName, 14);
                            return;
                        }
                        DocumentFile documentFile = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(bilibiliDirName)));
                        System.out.println(documentFile.renameTo(GUOJI_NAME));
                    } else {
                        System.out.println(bilibiliDir.renameTo(guojiDir));
                    }
                }
                Intent intent = new Intent();
                intent.setClassName(GUOJI_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
           //     exit();
            }


            if (i == 3) {
                Toast.makeText(this, "正在进行米服转国服", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", externalStoragePath);
                File xiaomiDir = new File(xiaomiDirName);
                File guojiDir = new File(guojiDirName);
                if (!guojiDir.exists() && xiaomiDir.exists()) {
                    Log.i("MainActivity", "exists");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (xiaomiDir.exists() && !xiaomiDirAuthed) {
                            Toast.makeText(this, "请通过权限后再次点击启动按钮！", Toast.LENGTH_SHORT).show();
                            startFor(xiaomiDirName, 14);
                            return;
                        }
                        DocumentFile documentFile = DocumentFile.fromTreeUri(this, Uri.parse(changeToUri3(xiaomiDirName)));
                        System.out.println(documentFile.renameTo(GUOJI_NAME));
                    } else {
                        System.out.println(xiaomiDir.renameTo(guojiDir));
                    }
                }
                Intent intent = new Intent();
                intent.setClassName(GUOJI_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
            //    exit();
            }



            if (i == 4) {
                Toast.makeText(this, "正在进行直接启动", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClassName(GUOJI_NAME, "com.miHoYo.GetMobileInfo.MainActivity");
                startActivity(intent);
          //      exit();
            }
        }else{
            Toast.makeText(this, "错误：请先选择已有服务器", Toast.LENGTH_SHORT).show();
        }
    }


    @TargetApi(26)
    public void startFor(String path, int code) {
        String uri = changeToUri(path);
        Uri parse = Uri.parse(uri);
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, parse);
        }
        this.startActivityForResult(intent, code);

    }


    public static String changeToUri(String path) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        String path2 = path.replace("/storage/emulated/0/", "").replace("/", "%2F");
        return "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A" + path2;
    }

    //转换至uriTree的路径
    public static String changeToUri3(String path) {
        path = path.replace("/storage/emulated/0/", "").replace("/", "%2F");
        return ("content://com.android.externalstorage.documents/tree/primary%3A" + path);

    }

    //返回授权状态
    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;

        if (data == null) {
            return;
        }

        if ((uri = data.getData()) != null) {
            getContentResolver().takePersistableUriPermission(uri, data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));//关键是这里，这个就是保存这个目录的访问权限
            if(requestCode == 10) {
                Toast.makeText(this, "官服授权成功！", Toast.LENGTH_SHORT).show();
                officialDirAuthed = true;
            }
            if(requestCode == 11) {
                Toast.makeText(this, "bilibili授权成功！", Toast.LENGTH_SHORT).show();
                bilibiliDirAuthed = true;
            }
            if(requestCode == 12) {
                Toast.makeText(this, "小米版授权成功！", Toast.LENGTH_SHORT).show();
                xiaomiDirAuthed = true;
            }
            if(requestCode == 13) {
                Toast.makeText(this, "国际服授权成功！", Toast.LENGTH_SHORT).show();
                guojiDirAuthed = true;
            }
            if(requestCode == 14) {
                Toast.makeText(this, "检测授权！", Toast.LENGTH_SHORT).show();
                jianceDirAuthed = true;
            }
        }

    }
}
