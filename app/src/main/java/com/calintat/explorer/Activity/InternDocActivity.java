package com.calintat.explorer.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InternDocActivity extends AppCompatActivity {

    ArrayList<DocumentModel> imgDownloadList = new ArrayList<>();
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rv_img_download)
    RecyclerView rv_img_download;
    @BindView(R.id.rv_img_download_list)
    RecyclerView rv_img_download_list;
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

    ArrayList<DocumentModel> imgMainDownloadList = new ArrayList<>();
    ArrayList<DocumentModel> imgMainDownloadList1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intern_doc);

        ButterKnife.bind(InternDocActivity.this);
        tv_title.setText("Documents");

        imgMainDownloadList.clear();
        imgMainDownloadList1.clear();
        imgDownloadList.clear();
        new LongOperation().execute();


        rv_img_download.setLayoutManager(new GridLayoutManager(InternDocActivity.this, 3));
        rv_img_download.setNestedScrollingEnabled(false);
        allDocumentAdapter = new AllDocumentAdapter(imgMainDownloadList, InternDocActivity.this, "documents");
        rv_img_download.setAdapter(allDocumentAdapter);

        rv_img_download_list.setLayoutManager(new LinearLayoutManager(InternDocActivity.this, RecyclerView.VERTICAL, false));
        rv_img_download_list.setNestedScrollingEnabled(false);
        allDocListAdapter = new AllDocListAdapter(imgMainDownloadList, InternDocActivity.this, "documents");
        rv_img_download_list.setAdapter(allDocListAdapter);

        img_grid_list.setOnClickListener(v -> {
            if (rl_fram.getVisibility() == View.VISIBLE) {
                img_grid_list.setImageDrawable(InternDocActivity.this.getResources().getDrawable(R.drawable.list_view));
                rl_List.setVisibility(View.VISIBLE);
                rl_fram.setVisibility(View.GONE);
            } else {
                img_grid_list.setImageDrawable(InternDocActivity.this.getResources().getDrawable(R.drawable.gird_view));
                rl_List.setVisibility(View.GONE);
                rl_fram.setVisibility(View.VISIBLE);
            }
        });

        rv_img_download.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1))
                    onScrolledToBottom();
            }
        });

        img_back.setOnClickListener(v -> onBackPressed());

        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.reverse(imgMainDownloadList);

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
        Intent intent = new Intent(InternDocActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getListFolder() {

        if (imgMainDownloadList1.size() > 1) {
            for (int j = 0; j < imgMainDownloadList1.size(); j++) {
                if (j > 0) {
                    if (!imgMainDownloadList1.get(j).getFolderName().equals(imgMainDownloadList1.get(j - 1).getFolderName())) {
                        imgDownloadList.add(imgMainDownloadList1.get(j));
                    }
                } else {
                    imgDownloadList.add(imgMainDownloadList1.get(j));
                }
            }
        }

        onScrolledToBottom();
    }

    private void onScrolledToBottom() {
        ArrayList<DocumentModel> documentModels = imgDownloadList;

        if (imgMainDownloadList.size() < documentModels.size()) {
            int x, y;
            if ((documentModels.size() - imgMainDownloadList.size()) >= 50) {
                x = imgMainDownloadList.size();
                y = x + 50;
            } else {
                x = imgMainDownloadList.size();
                y = x + documentModels.size() - imgMainDownloadList.size();
            }
            for (int i = x; i < y; i++) {
                imgMainDownloadList.add(documentModels.get(i));
            }
            runOnUiThread(() -> {
                allDocumentAdapter.notifyDataSetChanged();
                allDocListAdapter.notifyDataSetChanged();
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void load_image_files(File dir) {

        File[] listFile = dir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    load_image_files(listFile[i]);
                } else if (listFile[i].isFile()) {
                    File imagePath = listFile[i];
                    String path = imagePath.getAbsolutePath();

                    if (listFile[i].getAbsolutePath().contains(".docx") ||
                            listFile[i].getAbsolutePath().contains(".pdf") ||
                            listFile[i].getAbsolutePath().contains(".txt") ||
                            listFile[i].getAbsolutePath().contains(".xml") ||
                            listFile[i].getAbsolutePath().contains(".ppt") ||
                            listFile[i].getAbsolutePath().contains(".pptx") ||
                            listFile[i].getAbsolutePath().contains(".html")) {
//                        Log.e("LLLLL_Filename_img: ", imagePath.getParentFile().getName());

                        DocumentModel documentModel = new DocumentModel();
                        documentModel.setFileName(imagePath.getName());
                        documentModel.setFolderName(imagePath.getParentFile().getName());
                        documentModel.setFilePath(imagePath.getParentFile().getAbsolutePath());
                        documentModel.setLength(imagePath.length());


                        imgMainDownloadList1.add(documentModel);

                    }

                    System.gc();
                }
            }
        }
    }

    private final class LongOperation extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            load_image_files(new File(DownloadData.InternDirNamedir));
            return "Execute";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Execute")) {
                getListFolder();
            }

        }
    }
}