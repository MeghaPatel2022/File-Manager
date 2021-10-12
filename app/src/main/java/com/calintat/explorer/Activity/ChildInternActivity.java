package com.calintat.explorer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildInternActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rv_intern_download)
    RecyclerView rv_intern_download;
    @BindView(R.id.rv_intern_download_list)
    RecyclerView rv_intern_download_list;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.img_grid_list)
    ImageView img_grid_list;
    @BindView(R.id.rl_fram)
    RelativeLayout rl_fram;
    @BindView(R.id.rl_List)
    RelativeLayout rl_List;
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
        setContentView(R.layout.activity_child_intern);

        ButterKnife.bind(ChildInternActivity.this);
        tv_title.setText("Image Select");

        folderPath = getIntent().getStringExtra("folderPath");

        rv_intern_download.setLayoutManager(new GridLayoutManager(ChildInternActivity.this, 3));
        rv_intern_download.setNestedScrollingEnabled(false);
        allDocumentAdapter = new ChildDocAdapter(getListFolder(), ChildInternActivity.this, "InternImg");
        rv_intern_download.setAdapter(allDocumentAdapter);

        rv_intern_download_list.setLayoutManager(new LinearLayoutManager(ChildInternActivity.this, RecyclerView.VERTICAL, false));
        rv_intern_download_list.setNestedScrollingEnabled(false);
        childDocListAdapter = new ChildDocListAdapter(getListFolder(), ChildInternActivity.this, "InternImg");
        rv_intern_download_list.setAdapter(childDocListAdapter);

        Log.e("LLLLL_final Size: ", String.valueOf(getListFolder()));
        img_back.setOnClickListener(v -> onBackPressed());

        img_grid_list.setOnClickListener(v -> {
            if (rl_fram.getVisibility() == View.VISIBLE) {
                img_grid_list.setImageDrawable(ChildInternActivity.this.getResources().getDrawable(R.drawable.list_view));
                rl_List.setVisibility(View.VISIBLE);
                rl_fram.setVisibility(View.GONE);
            } else {
                img_grid_list.setImageDrawable(ChildInternActivity.this.getResources().getDrawable(R.drawable.gird_view));
                rl_List.setVisibility(View.GONE);
                rl_fram.setVisibility(View.VISIBLE);
            }
        });

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
        Intent intent = new Intent(ChildInternActivity.this, InternImgActivity.class);
        startActivity(intent);
        finish();
    }

    private ArrayList<DocumentModel> getListFolder() {
        documentsList.clear();
        documentsList.addAll(DownloadData.getInternData(new File(folderPath)));

        return documentsList;
    }
}