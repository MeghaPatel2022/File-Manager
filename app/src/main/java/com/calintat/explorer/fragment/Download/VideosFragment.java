package com.calintat.explorer.fragment.Download;

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

import com.calintat.explorer.Activity.DownloadActivity;
import com.calintat.explorer.Adapter.Download.VidDownloadAdapter;
import com.calintat.explorer.Adapter.Download.VidDownloadListAdapter;
import com.calintat.explorer.Model.DataModel;
import com.calintat.explorer.R;
import com.calintat.explorer.Utils.DownloadData;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideosFragment extends Fragment implements IReverse {

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

    ArrayList<DataModel> vidDownloadList = new ArrayList<>();
    ArrayList<DataModel> vidMainDownloadList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos, container, false);
        ButterKnife.bind(this, view);

        vidDownloadList.clear();
        vidDownloadList = DownloadData.getAllVideoData(DownloadData.dirNamedir, vidDownloadList);

        vidDownloadAdapter = new VidDownloadAdapter(vidMainDownloadList, getActivity());
        rv_video_download.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv_video_download.setAdapter(vidDownloadAdapter);

        vidDownloadListAdapter = new VidDownloadListAdapter(vidMainDownloadList, getActivity());
        rv_doc_download_list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_doc_download_list.setAdapter(vidDownloadListAdapter);

        onScrolledToBottom();
        rv_video_download.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        ((DownloadActivity) getActivity()).setOnWidgetClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void onScrolledToBottom() {
        if (vidMainDownloadList.size() < vidDownloadList.size()) {
            int x, y;
            if ((vidDownloadList.size() - vidMainDownloadList.size()) >= 40) {
                x = vidMainDownloadList.size();
                y = x + 40;
            } else {
                x = vidMainDownloadList.size();
                y = x + vidDownloadList.size() - vidMainDownloadList.size();
            }
            for (int i = x; i < y; i++) {
                vidMainDownloadList.add(vidDownloadList.get(i));
            }
            getActivity().runOnUiThread(() -> {
                vidDownloadAdapter.notifyDataSetChanged();
                vidDownloadListAdapter.notifyDataSetChanged();
            });

        }

    }

    @Override
    public void onReverseAll() {
        Collections.reverse(vidMainDownloadList);
//
        vidDownloadListAdapter.notifyDataSetChanged();
        vidDownloadAdapter.notifyDataSetChanged();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        vidDownloadListAdapter.notifyDataSetChanged();
//                        vidDownloadAdapter.notifyDataSetChanged();
//                    }
//                });
    }
}
