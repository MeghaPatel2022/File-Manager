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
import com.calintat.explorer.Adapter.Download.DocDownloadAdapter;
import com.calintat.explorer.Adapter.Download.DocDownloadListAdapter;
import com.calintat.explorer.Model.DataModel;
import com.calintat.explorer.R;
import com.calintat.explorer.Utils.DownloadData;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocumentsFragment extends Fragment implements IReverse {

    @BindView(R.id.rv_doc_download)
    RecyclerView rv_doc_download;
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

    DocDownloadAdapter docDownloadAdapter;
    DocDownloadListAdapter docDownloadListAdapter;

    ArrayList<DataModel> allDocumentsList = new ArrayList<>();
    ArrayList<DataModel> getAllDocumentsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documents, container, false);
        ButterKnife.bind(this, view);

        getAllDocumentsList.clear();
        getAllDocumentsList.addAll(DownloadData.getAllDocData(DownloadData.dirNamedir, allDocumentsList));

        docDownloadAdapter = new DocDownloadAdapter(getAllDocumentsList, getActivity());
        rv_doc_download.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv_doc_download.setAdapter(docDownloadAdapter);

        docDownloadListAdapter = new DocDownloadListAdapter(getAllDocumentsList, getActivity());
        rv_doc_download_list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_doc_download_list.setAdapter(docDownloadListAdapter);

        if (getAllDocumentsList.size() == 0) {
            no_found.setVisibility(View.VISIBLE);
            rv_doc_download.setVisibility(View.GONE);
            rl_List.setVisibility(View.GONE);
        }

        img_grid_list.setOnClickListener(v -> {
            if (rv_doc_download.getVisibility() == View.VISIBLE) {
                img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.list_view));
                rl_List.setVisibility(View.VISIBLE);
                rv_doc_download.setVisibility(View.GONE);
            } else {
                img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.gird_view));
                rl_List.setVisibility(View.GONE);
                rv_doc_download.setVisibility(View.VISIBLE);
            }
        });


        ((DownloadActivity) getActivity()).setOnWidgetClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onReverseAll() {
        Collections.reverse(getAllDocumentsList);
//
        docDownloadListAdapter.notifyDataSetChanged();
        docDownloadAdapter.notifyDataSetChanged();
//        getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        docDownloadListAdapter.notifyDataSetChanged();
//                        docDownloadAdapter.notifyDataSetChanged();
//                    }
//                });
//
    }
}
