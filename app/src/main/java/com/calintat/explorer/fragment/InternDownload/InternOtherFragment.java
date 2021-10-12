package com.calintat.explorer.fragment.InternDownload;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calintat.explorer.Adapter.Download.OtherDownloadAdapter;
import com.calintat.explorer.Adapter.Download.OtherDownloadListAdapter;
import com.calintat.explorer.Model.DataModel;
import com.calintat.explorer.R;
import com.calintat.explorer.Utils.DownloadData;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InternOtherFragment extends Fragment {

    @BindView(R.id.rv_other_download)
    RecyclerView rv_other_download;
    @BindView(R.id.no_found)
    TextView no_found;
    @BindView(R.id.img_grid_list)
    ImageView img_grid_list;
    @BindView(R.id.rl_List)
    RelativeLayout rl_List;
    @BindView(R.id.rv_doc_download_list)
    RecyclerView rv_doc_download_list;
//    @BindView(R.id.img_filter)
//    ImageView img_filter;

    OtherDownloadAdapter otherDownloadAdapter;
    OtherDownloadListAdapter otherDownloadListAdapter;

    ArrayList<DataModel> imgDownloadList = new ArrayList<>();
    ArrayList<DataModel> imgMainDownloadList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_others, container, false);
        ButterKnife.bind(this, view);

        rv_other_download.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv_other_download.setNestedScrollingEnabled(false);
        otherDownloadAdapter = new OtherDownloadAdapter(imgMainDownloadList, getActivity());
        rv_other_download.setAdapter(otherDownloadAdapter);

        rv_doc_download_list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_doc_download_list.setNestedScrollingEnabled(false);
        otherDownloadListAdapter = new OtherDownloadListAdapter(imgMainDownloadList, getActivity());
        rv_doc_download_list.setAdapter(otherDownloadListAdapter);

        new LongOperation().execute();

        rv_other_download.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1))
                    onScrolledToBottom();
            }
        });

        img_grid_list.setOnClickListener(v -> {
            if (rv_other_download.getVisibility() == View.VISIBLE) {
                img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.list_view));
                rl_List.setVisibility(View.VISIBLE);
                rv_other_download.setVisibility(View.GONE);
            } else {
                img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.gird_view));
                rl_List.setVisibility(View.GONE);
                rv_other_download.setVisibility(View.VISIBLE);
            }
        });

//        img_filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Collections.reverse(imgMainDownloadList);
//
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        otherDownloadListAdapter.notifyDataSetChanged();
//                        otherDownloadAdapter.notifyDataSetChanged();
//                    }
//                });
//
//            }
//        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void onScrolledToBottom() {
        ArrayList<DataModel> documentModels = imgDownloadList;

        if (imgDownloadList.size() == 0) {
            getActivity().runOnUiThread(() -> {
                no_found.setVisibility(View.VISIBLE);
                rv_other_download.setVisibility(View.GONE);
            });
        } else {

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
                getActivity().runOnUiThread(() -> {
                    otherDownloadAdapter.notifyDataSetChanged();
                    otherDownloadListAdapter.notifyDataSetChanged();
                });
            }
        }

    }

    public void load_image_files(File dir) {

        File[] listFile = dir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    load_image_files(listFile[i]);
                } else if (listFile[i].isFile()) {
                    File imagePath = listFile[i];

                    if (!(listFile[i].getAbsolutePath().contains(".mp3") ||
                            listFile[i].getAbsolutePath().contains(".wav") ||
                            listFile[i].getAbsolutePath().contains(".ogg") ||
                            listFile[i].getAbsolutePath().contains(".docx") ||
                            listFile[i].getAbsolutePath().contains(".pdf") ||
                            listFile[i].getAbsolutePath().contains(".html") ||
                            listFile[i].getAbsolutePath().contains(".txt") ||
                            listFile[i].getAbsolutePath().contains(".xml") ||
                            listFile[i].getAbsolutePath().contains(".ppt") ||
                            listFile[i].getAbsolutePath().contains(".pptx") ||
                            listFile[i].getAbsolutePath().contains(".mp4") ||
                            listFile[i].getAbsolutePath().contains(".mkv") ||
                            listFile[i].getAbsolutePath().contains(".png") ||
                            listFile[i].getAbsolutePath().contains(".jpeg") ||
                            listFile[i].getAbsolutePath().contains(".JPG") ||
                            listFile[i].getAbsolutePath().contains(".CR2") ||
                            listFile[i].getAbsolutePath().contains(".webp") ||
                            listFile[i].getAbsolutePath().contains(".jpg"))) {
                        DataModel dataModel = new DataModel();
                        dataModel.setFilename(imagePath.getName());
                        dataModel.setSize(imagePath.length());
                        dataModel.setFilePath(imagePath.getAbsolutePath());
                        if (imagePath.getParentFile().getName().equalsIgnoreCase("download")) {
                            imgDownloadList.add(dataModel);
                        }
                    }

                    System.gc();
                }
            }
        }
    }

    private final class LongOperation extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            imgMainDownloadList.clear();
            imgDownloadList.clear();
            load_image_files(new File(DownloadData.InternDownDirNamedir));
            return "Execute";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Execute")) {
                onScrolledToBottom();
            }
        }
    }
}

