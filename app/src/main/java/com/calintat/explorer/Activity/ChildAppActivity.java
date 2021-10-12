package com.calintat.explorer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class ChildAppActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rv_app_download)
    RecyclerView rv_app_download;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_app);

        ButterKnife.bind(ChildAppActivity.this);
        tv_title.setText("App Select");

        folderPath = getIntent().getStringExtra("folderPath");

        rv_app_download.setLayoutManager(new GridLayoutManager(ChildAppActivity.this, 3));
        rv_app_download.setNestedScrollingEnabled(false);
        allDocumentAdapter = new ChildDocAdapter(getListFolder(), ChildAppActivity.this, "appData");
        rv_app_download.setAdapter(allDocumentAdapter);

        rv_app_download_list.setLayoutManager(new LinearLayoutManager(ChildAppActivity.this, RecyclerView.VERTICAL, false));
        rv_app_download_list.setNestedScrollingEnabled(false);
        childDocListAdapter = new ChildDocListAdapter(getListFolder(), ChildAppActivity.this, "appData");
        rv_app_download_list.setAdapter(childDocListAdapter);

        img_grid_list.setOnClickListener(v -> {
            if (rv_app_download.getVisibility() == View.VISIBLE) {
                img_grid_list.setImageDrawable(ChildAppActivity.this.getResources().getDrawable(R.drawable.list_view));
                rv_app_download_list.setVisibility(View.VISIBLE);
                rv_app_download.setVisibility(View.GONE);
            } else {
                img_grid_list.setImageDrawable(ChildAppActivity.this.getResources().getDrawable(R.drawable.gird_view));
                rv_app_download_list.setVisibility(View.GONE);
                rv_app_download.setVisibility(View.VISIBLE);
            }
        });

        Log.e("LLLLL_final Size: ", String.valueOf(getListFolder()));
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
        Intent intent = new Intent(ChildAppActivity.this, AppActivity.class);
        startActivity(intent);
        finish();
    }

    private ArrayList<DocumentModel> getListFolder() {
        documentsList.clear();
        documentsList.addAll(DownloadData.getAppData(folderPath));

        return documentsList;
    }
    
}