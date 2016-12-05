package com.chon.runtimepermissiondemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chon.runtimepermissiondemo.camera.CameraPreviewFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OriginalActivity extends AppCompatActivity {

    @BindView(R.id.btn_camera)
    Button btnCamera;

    private static final int REQUEST_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamera();
            }
        });
    }

    private void showCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 应用没有授予相机权限，请求权限
            requestCameraPermission();
        } else {
            // 应用被授予相机权限 PackageManager.PERMISSION_GRANTED
            showCameraPreview();
        }
    }

    private void requestCameraPermission() {
        // 如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            // 向用户详细解释申请该权限的原因
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("拍照需要使用相机权限,如果不授予权限会导致该功能无法正常使用")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(OriginalActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .setNegativeButton("不给", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else {
            // 申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
    }


    /* 请求授权的回调 */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 授予权限，打开相机
                showCameraPreview();
            } else {
                Toast.makeText(this, "请求权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showCameraPreview() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, CameraPreviewFragment.newInstance())
                .addToBackStack("camera")
                .commit();
    }

}
