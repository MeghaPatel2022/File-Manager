package com.calintat.explorer.Activity;

import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.calintat.explorer.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.xiaopo.flying.poiphoto.Define;
import com.xiaopo.flying.poiphoto.PhotoPicker;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImgeViewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final DecimalFormat format = new DecimalFormat("#.##");
    private static final long MiB = 1024 * 1024;
    private static final long KiB = 1024;

    @BindView(R.id.imgMain)
    ImageView imgMain;
    @BindView(R.id.ll_wallpaper)
    LinearLayout ll_wallpaper;
    @BindView(R.id.ll_share)
    LinearLayout ll_share;
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;
    @BindView(R.id.ll_information)
    LinearLayout ll_information;
    @BindView(R.id.rl_image_info)
    RelativeLayout rl_image_info;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_filename)
    TextView tv_filename;
    @BindView(R.id.tv_path)
    TextView tv_path;
    @BindView(R.id.tv_size)
    TextView tv_size;
    @BindView(R.id.tv_resolution)
    TextView tv_resolution;
    String FilePath = "";
    BottomSheetBehavior behavior1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imge_view);

        ButterKnife.bind(ImgeViewActivity.this);
        if (getIntent().getStringExtra("Filepath") != null) {
            if (!getIntent().getStringExtra("Filepath").equals("")) {
                FilePath = getIntent().getStringExtra("Filepath");

                Bitmap bitmap = BitmapFactory.decodeFile(FilePath);
                imgMain.setImageBitmap(bitmap);

                File file = new File(FilePath);
                tv_filename.setText(file.getName());
                tv_path.setText(file.getAbsolutePath());


                tv_date.setText(String.valueOf(DateFormat.format("dd MMMM yyyy hh:mm a", new Date(file.lastModified()))));

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                int imageHeight = options.outHeight;
                int imageWidth = options.outWidth;

                tv_resolution.setText(imageWidth + " x " + imageHeight);
                tv_size.setText(getFileSize(file.length()));
            }
        } else {
            setImage();
        }

        ll_wallpaper.setOnClickListener(this);
        ll_share.setOnClickListener(this);
        ll_delete.setOnClickListener(this);
        ll_information.setOnClickListener(this);

        behavior1 = BottomSheetBehavior.from(rl_image_info);
        behavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (behavior1.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    private void setImage() {

        PhotoPicker.newInstance()
                .setAlbumTitle("Album")
                .setPhotoTitle("Photo")
                .setMaxCount(1)
                .pick(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Define.DEFAULT_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> paths = data.getStringArrayListExtra(Define.PATHS);
            String path = paths.get(0);
            FilePath = path;

            Bitmap bitmap = BitmapFactory.decodeFile(FilePath);
            imgMain.setImageBitmap(bitmap);

            File file = new File(FilePath);
            tv_filename.setText(file.getName());
            tv_path.setText(file.getAbsolutePath());

            tv_date.setText(String.valueOf(DateFormat.format("dd MMMM yyyy hh:mm a", new Date(file.lastModified()))));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;

            tv_resolution.setText(imageWidth + " x " + imageHeight);
            tv_size.setText(getFileSize(file.length()));
        }
    }


    public String getFileSize(long file) {

        final double length = Double.parseDouble(String.valueOf(file));

        if (length > MiB) {
            return format.format(length / MiB) + " MB";
        }
        if (length > KiB) {
            return format.format(length / KiB) + " KB";
        }
        return format.format(length) + " B";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_wallpaper:
                setWallpaper(FilePath);
                break;
            case R.id.ll_share:
                shareImage(new File(FilePath));
                break;
            case R.id.ll_delete:
                deleteFile(new File(FilePath));
                break;
            case R.id.ll_information:
                behavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
        }
    }

    private void setWallpaper(String s) {
        Bitmap bitmap = BitmapFactory.decodeFile(s);
        WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
        try {
            manager.setBitmap(bitmap);
            Toast.makeText(this, "Wallpaper set!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException e) {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareImage(File file) {
        Uri uri = FileProvider.getUriForFile(ImgeViewActivity.this, "com.calintat.explorer.provider", file);
        Intent shareIntent = new Intent("android.intent.action.SEND");
        shareIntent.setType("image/*");
        shareIntent.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.app_name) + " Create By : https://play.google.com/store/apps/details?id=" + getPackageName());
        shareIntent.putExtra("android.intent.extra.STREAM", uri);
        startActivity(Intent.createChooser(shareIntent, "Share Image using"));
    }

    private void deleteFile(File file) {
        final Dialog dial = new Dialog(ImgeViewActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dial.requestWindowFeature(1);
        dial.setContentView(R.layout.item_album_delete_confirmation);
        dial.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dial.setCanceledOnTouchOutside(true);
        dial.findViewById(R.id.delete_yes).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.e("LLLL_File: ", file.getAbsolutePath());
                File fD = file;
                if (fD.exists()) {
                    fD.delete();
                }

                sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(String.valueOf(fD)))));
                dial.dismiss();
                finish();
            }
        });
        dial.findViewById(R.id.delete_no).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dial.dismiss();
            }
        });
        dial.show();
    }
}