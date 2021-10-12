package com.calintat.explorer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.calintat.explorer.R;
import com.calintat.explorer.fragment.Download.IReverse;
import com.calintat.explorer.fragment.InternDownload.InternDownloadMainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InternDownloadActivity extends AppCompatActivity {

    @BindView(R.id.rl_fram)
    RelativeLayout rl_fram;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_filter)
    ImageView img_filter;

    IReverse iReverse;

    public void setOnWidgetClickListener(IReverse onWidgetClickListener) {
        this.iReverse = null;
        this.iReverse = onWidgetClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intern_download);

        ButterKnife.bind(InternDownloadActivity.this);
        tv_title.setText(getString(R.string.download));

        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iReverse.onReverseAll();
            }
        });

        if (savedInstanceState == null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.rl_fram, new InternDownloadMainFragment()).commit();
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InternDownloadActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}