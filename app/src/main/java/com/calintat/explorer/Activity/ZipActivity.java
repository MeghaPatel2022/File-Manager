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

import com.calintat.explorer.Adapter.AllDocListAdapter;
import com.calintat.explorer.Adapter.AllDocumentAdapter;
import com.calintat.explorer.Model.DocumentModel;
import com.calintat.explorer.R;
import com.calintat.explorer.Utils.DownloadData;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZipActivity extends AppCompatActivity {

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
    @BindView(R.id.rl_fram)
    RelativeLayout rl_fram;
    @BindView(R.id.rl_List)
    RelativeLayout rl_List;
    @BindView(R.id.img_filter)
    ImageView img_filter;

    AllDocumentAdapter allDocumentAdapter;
    AllDocListAdapter allDocListAdapter;
    ArrayList<DocumentModel> documentsList = new ArrayList<>();
    ArrayList<DocumentModel> documentsList1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip);

        ButterKnife.bind(ZipActivity.this);
        tv_title.setText("Zip Files");

        rv_zip_download.setLayoutManager(new GridLayoutManager(ZipActivity.this, 3));
        rv_zip_download.setNestedScrollingEnabled(false);
        allDocumentAdapter = new AllDocumentAdapter(getListFolder(), ZipActivity.this, "zip");
        rv_zip_download.setAdapter(allDocumentAdapter);

        rv_app_download_list.setLayoutManager(new LinearLayoutManager(ZipActivity.this, RecyclerView.VERTICAL, false));
        rv_app_download_list.setNestedScrollingEnabled(false);
        allDocListAdapter = new AllDocListAdapter(getListFolder(), ZipActivity.this, "zip");
        rv_app_download_list.setAdapter(allDocListAdapter);

        Log.e("LLLLL_final Size: ", String.valueOf(getListFolder()));
        img_back.setOnClickListener(v -> onBackPressed());

        img_grid_list.setOnClickListener(v -> {
            if (rv_zip_download.getVisibility() == View.VISIBLE) {
                img_grid_list.setImageDrawable(ZipActivity.this.getResources().getDrawable(R.drawable.list_view));
                rl_List.setVisibility(View.VISIBLE);
                rv_zip_download.setVisibility(View.GONE);
            } else {
                img_grid_list.setImageDrawable(ZipActivity.this.getResources().getDrawable(R.drawable.gird_view));
                rl_List.setVisibility(View.GONE);
                rv_zip_download.setVisibility(View.VISIBLE);
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
                        allDocListAdapter.notifyDataSetChanged();
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ZipActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private ArrayList<DocumentModel> getListFolder() {
        documentsList.clear();
        documentsList.addAll(DownloadData.getZipFolder(DownloadData.docDirNamedir, documentsList));
        Log.e("LLLLL_DocList: ", String.valueOf(documentsList.size()));

        for (int i = 0; i < documentsList.size(); i++) {
            DocumentModel documentModel = documentsList.get(i);
            if (i > 0) {
                for (int j = 0; j < documentsList.size(); j++) {
                    if (documentModel.getFilePath().equals(documentsList.get(j).getFilePath())) {
                        documentsList.remove(i);
                    }
                }
            }
        }

        return documentsList;
    }
}