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
import com.calintat.explorer.Adapter.Download.ImgDownloadAdapter;
import com.calintat.explorer.Adapter.Download.ImgDownloadListAdapter;
import com.calintat.explorer.Model.DataModel;
import com.calintat.explorer.R;
import com.calintat.explorer.Utils.DownloadData;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagesFragment extends Fragment implements IReverse {

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
//    @BindView(R.id.img_filter)
//    ImageView img_filter;

    ImgDownloadAdapter imgDownloadAdapter;
    ImgDownloadListAdapter imgDownloadListAdapter;

    ArrayList<DataModel> imgDownloadList = new ArrayList<>();
    ArrayList<DataModel> imgMainDownloadList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        ButterKnife.bind(this, view);

        imgDownloadList.clear();
        imgDownloadList = DownloadData.getAllImageData(DownloadData.dirNamedir, imgDownloadList);

        rv_img_download.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv_img_download.setNestedScrollingEnabled(false);

        rv_doc_download_list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_doc_download_list.setNestedScrollingEnabled(false);

        imgDownloadAdapter = new ImgDownloadAdapter(imgMainDownloadList, getActivity());
        rv_img_download.setAdapter(imgDownloadAdapter);

        imgDownloadListAdapter = new ImgDownloadListAdapter(imgMainDownloadList, getActivity());
        rv_doc_download_list.setAdapter(imgDownloadListAdapter);

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

        ((DownloadActivity) getActivity()).setOnWidgetClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void onScrolledToBottom() {
        if (imgDownloadList.size() == 0) {
            getActivity().runOnUiThread(() -> {
                no_found.setVisibility(View.VISIBLE);
                rv_img_download.setVisibility(View.GONE);
            });
        } else {
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

    @Override
    public void onResume() {
        super.onResume();
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
    }

    @Override
    public void onReverseAll() {
        Collections.reverse(imgMainDownloadList);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imgDownloadListAdapter.notifyDataSetChanged();
                imgDownloadAdapter.notifyDataSetChanged();
            }
        });
    }
}
