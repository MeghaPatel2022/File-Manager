package com.calintat.explorer.fragment.GDrive;

import android.os.Bundle;
import android.util.Log;
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

import com.calintat.explorer.Activity.DriveActivity;
import com.calintat.explorer.Adapter.Gdrive.AllGdriveAdapter;
import com.calintat.explorer.Adapter.Gdrive.AllGridGdriveAdapter;
import com.calintat.explorer.Model.GDrive;
import com.calintat.explorer.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoGFragment extends Fragment {

    @BindView(R.id.rv_img_download)
    RecyclerView rv_img_download;
    @BindView(R.id.no_found)
    TextView no_found;
    @BindView(R.id.img_grid_list)
    ImageView img_grid_list;
    @BindView(R.id.rl_List)
    RelativeLayout rl_List;
    @BindView(R.id.rv_doc_download_list)
    RecyclerView rv_doc_download_list;


    AllGdriveAdapter imgDownloadAdapter;
    AllGridGdriveAdapter imgDownloadListAdapter;

    ArrayList<GDrive> imgDownloadList = new ArrayList<>();
    ArrayList<GDrive> imgMainDownloadList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        ButterKnife.bind(this, view);

        getData();

        rv_img_download.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv_img_download.setNestedScrollingEnabled(false);

        rv_doc_download_list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_doc_download_list.setNestedScrollingEnabled(false);

        imgDownloadListAdapter = new AllGridGdriveAdapter(imgMainDownloadList, getActivity(), "video");
        rv_img_download.setAdapter(imgDownloadListAdapter);

        imgDownloadAdapter = new AllGdriveAdapter(imgMainDownloadList, getActivity(), "video");
        rv_doc_download_list.setAdapter(imgDownloadAdapter);

        onScrolledToBottom();

        rv_img_download.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1))
                    onScrolledToBottom();
            }
        });

        rv_doc_download_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1))
                    onScrolledToBottom();
            }
        });

        img_grid_list.setOnClickListener(v -> {
            if (rv_img_download.getVisibility() == View.VISIBLE) {
                img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.list_view));
                rl_List.setVisibility(View.VISIBLE);
                rv_img_download.setVisibility(View.GONE);
            } else {
                img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.gird_view));
                rl_List.setVisibility(View.GONE);
                rv_img_download.setVisibility(View.VISIBLE);
            }
        });



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void getData() {
        imgDownloadList.clear();


        for (int i = 0; i < DriveActivity.gDriveArrayList.size(); i++) {
            String path = DriveActivity.gDriveArrayList.get(i).getName();
            Log.e("LLLLL_Log: ", path);
            if (path.contains(".mp4") ||
                    path.contains(".mkv")) {

                imgDownloadList.add(DriveActivity.gDriveArrayList.get(i));
            }
        }

    }

    private void onScrolledToBottom() {

        if (imgMainDownloadList.size() < imgDownloadList.size()) {
            int x, y;
            if ((imgDownloadList.size() - imgMainDownloadList.size()) >= 40) {
                x = imgMainDownloadList.size();
                y = x + 40;
            } else {
                x = imgMainDownloadList.size();
                y = x + imgDownloadList.size() - imgMainDownloadList.size();
            }
            for (int i = x; i < y; i++) {
                imgMainDownloadList.add(imgDownloadList.get(i));
            }
            getActivity().runOnUiThread(() -> {
                imgDownloadAdapter.notifyDataSetChanged();
                imgDownloadListAdapter.notifyDataSetChanged();
            });
        }
    }
}
