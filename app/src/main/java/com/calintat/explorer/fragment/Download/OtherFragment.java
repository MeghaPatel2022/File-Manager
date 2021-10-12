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
import com.calintat.explorer.Adapter.Download.OtherDownloadAdapter;
import com.calintat.explorer.Adapter.Download.OtherDownloadListAdapter;
import com.calintat.explorer.Model.DataModel;
import com.calintat.explorer.R;
import com.calintat.explorer.Utils.DownloadData;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtherFragment extends Fragment implements IReverse {

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

    ArrayList<DataModel> otherDownloadList = new ArrayList<>();
    ArrayList<DataModel> otherMainDownloadList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_others, container, false);
        ButterKnife.bind(this, view);

        otherDownloadList.clear();
        otherDownloadList = DownloadData.getOtherDownloadData(DownloadData.dirNamedir, otherDownloadList);

        rv_other_download.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv_other_download.setNestedScrollingEnabled(false);
        otherDownloadAdapter = new OtherDownloadAdapter(otherMainDownloadList, getActivity());
        rv_other_download.setAdapter(otherDownloadAdapter);

        rv_doc_download_list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_doc_download_list.setNestedScrollingEnabled(false);
        otherDownloadListAdapter = new OtherDownloadListAdapter(otherMainDownloadList, getActivity());
        rv_doc_download_list.setAdapter(otherDownloadListAdapter);
        onScrolledToBottom();

        rv_other_download.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


        ((DownloadActivity) getActivity()).setOnWidgetClickListener(this);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void onScrolledToBottom() {

        if (otherDownloadList.size() == 0) {
            no_found.setVisibility(View.VISIBLE);
            rv_doc_download_list.setVisibility(View.GONE);
            rv_other_download.setVisibility(View.GONE);
        }

        if (otherMainDownloadList.size() < otherDownloadList.size()) {
            int x, y;
            if ((otherDownloadList.size() - otherMainDownloadList.size()) >= 40) {
                x = otherMainDownloadList.size();
                y = x + 40;
            } else {
                x = otherMainDownloadList.size();
                y = x + otherDownloadList.size() - otherMainDownloadList.size();
            }
            for (int i = x; i < y; i++) {
                otherMainDownloadList.add(otherDownloadList.get(i));
            }
            getActivity().runOnUiThread(() -> {
                otherDownloadAdapter.notifyDataSetChanged();
                otherDownloadListAdapter.notifyDataSetChanged();
            });
        }

    }

    @Override
    public void onReverseAll() {
        Collections.reverse(otherMainDownloadList);
        otherDownloadListAdapter.notifyDataSetChanged();
        otherDownloadAdapter.notifyDataSetChanged();
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                otherDownloadListAdapter.notifyDataSetChanged();
//                otherDownloadAdapter.notifyDataSetChanged();
//            }
//        });
//
    }
}
