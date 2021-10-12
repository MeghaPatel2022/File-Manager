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

import com.calintat.explorer.Adapter.Download.VidDownloadAdapter;
import com.calintat.explorer.Adapter.Download.VidDownloadListAdapter;
import com.calintat.explorer.Model.DataModel;
import com.calintat.explorer.R;
import com.calintat.explorer.Utils.DownloadData;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InternVideosFragment extends Fragment {

    @BindView(R.id.rv_video_download)
    RecyclerView rv_video_download;
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

    VidDownloadAdapter vidDownloadAdapter;
    VidDownloadListAdapter vidDownloadListAdapter;

    ArrayList<DataModel> imgDownloadList = new ArrayList<>();
    ArrayList<DataModel> imgMainDownloadList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos, container, false);
        ButterKnife.bind(this, view);

        vidDownloadAdapter = new VidDownloadAdapter(imgMainDownloadList, getActivity());
        rv_video_download.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv_video_download.setAdapter(vidDownloadAdapter);

        vidDownloadListAdapter = new VidDownloadListAdapter(imgMainDownloadList, getActivity());
        rv_doc_download_list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_doc_download_list.setAdapter(vidDownloadListAdapter);

        rv_video_download.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1))
                    onScrolledToBottom();
            }
        });
        new LongOperation().execute();

        img_grid_list.setOnClickListener(v -> {
            if (rv_video_download.getVisibility() == View.VISIBLE) {
                img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.list_view));
                rl_List.setVisibility(View.VISIBLE);
                rv_video_download.setVisibility(View.GONE);
            } else {
                img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.gird_view));
                rl_List.setVisibility(View.GONE);
                rv_video_download.setVisibility(View.VISIBLE);
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
//                        vidDownloadListAdapter.notifyDataSetChanged();
//                        vidDownloadAdapter.notifyDataSetChanged();
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
                rv_video_download.setVisibility(View.GONE);
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
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        vidDownloadAdapter.notifyDataSetChanged();
                        vidDownloadListAdapter.notifyDataSetChanged();
                    });
                }
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
                    String path = imagePath.getAbsolutePath();

                    if (listFile[i].getAbsolutePath().contains(".mp4") ||
                            listFile[i].getAbsolutePath().contains(".mkv")) {
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
