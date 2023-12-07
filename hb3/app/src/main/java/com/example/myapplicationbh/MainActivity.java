package com.example.myapplicationbh;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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


import java.io.File;

@RequiresApi(api = Build.VERSION_CODES.R)
public class MainActivity<i> extends AppCompatActivity {
    private static boolean officialDirAuthed = false;
    private static boolean bilibiliDirAuthed = false;




    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
    };

    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    public static final String OFFICIAL_NAME = "com.miHoYo.enterprise.NGHSoD";
    public static final String BILIBILI_NAME = "com.miHoYo.bh3.bilibili";
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
        TextView textView6 = findViewById(R.id.textView6);

        // 设置版本号到 TextView4 中
        textView6.setText("版本号: " + appVersion);
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

    //删除

    public void delete(View view) {
        Toast.makeText(this, "以防点错，需要进行两次才可删除数据！", Toast.LENGTH_SHORT).show();
        //创建AlertDialog构造器Builder对象，AlertDialog建议使用android.support.v7.app包下的。
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框标题
        builder.setTitle("请选择要删除的服务器");
        //设置对话框图标
        builder.setIcon(R.drawable.bh3b);
        final String[] sexs = new String[]{"官服", "b服","米服","国际服"};
        //设置单选选项
        builder.setSingleChoiceItems(sexs, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "您确定要删除："+sexs[which], Toast.LENGTH_SHORT).show();
                if(sexs[which]=="官服"){
                    deleteofficial();
                }else if(sexs[which]=="b服"){
                    deletebilibili();
                }

            }
        });
        //添加确定按钮
        builder.setPositiveButton("确定", null);

        //创建并显示对话框
        builder.show();

    }
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
        builder.setIcon(R.drawable.bh3guan);
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

int i;


    //保存数据
    public void save(View view) {
        x=1;
        //创建AlertDialog构造器Builder对象，AlertDialog建议使用android.support.v7.app包下的。
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框标题
        builder.setTitle("请选择你现在有数据的服务器");
        //设置对话框图标
        builder.setIcon(R.drawable.bh3guan);
        final String[] sexs = new String[]{"官服", "b服"};
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
            if (i == 1) {
                Toast.makeText(this, "正在进行直接启动", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClassName(OFFICIAL_NAME, "com.miHoYo.overridenativeactivity.OverrideNativeActivity");
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
                intent.setClassName(OFFICIAL_NAME, "com.miHoYo.overridenativeactivity.OverrideNativeActivity");
                startActivity(intent);
    //            exit();
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
                intent.setClassName(BILIBILI_NAME, "com.miHoYo.overridenativeactivity.OverrideNativeActivity");
                startActivity(intent);
               // exit();
            }
            if (i == 2) {
                Toast.makeText(this, "正在进行直接启动", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClassName(BILIBILI_NAME, "com.miHoYo.overridenativeactivity.OverrideNativeActivity");
                startActivity(intent);
              //  exit();

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
        }

    }
}
