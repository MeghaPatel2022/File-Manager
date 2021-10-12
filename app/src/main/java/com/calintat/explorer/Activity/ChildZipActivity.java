package com.calintat.explorer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calintat.explorer.Adapter.ChildDocAdapter;
import com.calintat.explorer.Adapter.ChildDocListAdapter;
import com.calintat.explorer.Model.DocumentModel;
import com.calintat.explorer.R;
import com.calintat.explorer.Utils.DownloadData;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildZipActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rv_zip_download)
    RecyclerView rv_zip_download;
    @BindView(R.id.rv_app_download_list)
    RecyclerView rv_app_download_list;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.img_grid_list)
    ImageView img_grid_list;
    @BindView(R.id.img_filter)
    ImageView img_filter;


    ChildDocAdapter allDocumentAdapter;
    ChildDocListAdapter childDocListAdapter;
    ArrayList<DocumentModel> documentsList = new ArrayList<>();
    ArrayList<DocumentModel> documentsList1 = new ArrayList<>();

    String folderPath = "";

    ArrayList<DocumentModel> oldlist = new ArrayList<DocumentModel>();
    ArrayList<DocumentModel> newList = new ArrayList<DocumentModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_zip);

        ButterKnife.bind(ChildZipActivity.this);
        tv_title.setText("Zip Select");

        folderPath = getIntent().getStringExtra("folderPath");

        rv_zip_download.setLayoutManager(new GridLayoutManager(ChildZipActivity.this, 3));
        rv_zip_download.setNestedScrollingEnabled(false);
        allDocumentAdapter = new ChildDocAdapter(getListFolder(), ChildZipActivity.this, "zip");
        rv_zip_download.setAdapter(allDocumentAdapter);

        rv_app_download_list.setLayoutManager(new LinearLayoutManager(ChildZipActivity.this, RecyclerView.VERTICAL, false));
        rv_app_download_list.setNestedScrollingEnabled(false);
        childDocListAdapter = new ChildDocListAdapter(getListFolder(), ChildZipActivity.this, "zip");
        rv_app_download_list.setAdapter(childDocListAdapter);

        img_grid_list.setOnClickListener(v -> {
            if (rv_zip_download.getVisibility() == View.VISIBLE) {
                img_grid_list.setImageDrawable(ChildZipActivity.this.getResources().getDrawable(R.drawable.list_view));
                rv_app_download_list.setVisibility(View.VISIBLE);
                rv_zip_download.setVisibility(View.GONE);
            } else {
                img_grid_list.setImageDrawable(ChildZipActivity.this.getResources().getDrawable(R.drawable.gird_view));
                rv_app_download_list.setVisibility(View.GONE);
                rv_zip_download.setVisibility(View.VISIBLE);
            }
        });

        img_back.setOnClickListener(v -> onBackPressed());

        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.reverse(documentsList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        allDocumentAdapter.notifyDataSetChanged();
                        childDocListAdapter.notifyDataSetChanged();
                    }
                });

            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChildZipActivity.this, ZipActivity.class);
        startActivity(intent);
        finish();
    }

    private ArrayList<DocumentModel> getListFolder() {
        documentsList.clear();
        documentsList.addAll(DownloadData.getZipData(folderPath));

        return documentsList;
    }
}