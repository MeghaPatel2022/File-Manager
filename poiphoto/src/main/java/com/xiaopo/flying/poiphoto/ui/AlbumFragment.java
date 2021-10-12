package com.xiaopo.flying.poiphoto.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaopo.flying.poiphoto.Configure;
import com.xiaopo.flying.poiphoto.Define;
import com.xiaopo.flying.poiphoto.PhotoManager;
import com.xiaopo.flying.poiphoto.R;
import com.xiaopo.flying.poiphoto.datatype.Album;
import com.xiaopo.flying.poiphoto.ui.adapter.AlbumAdapter;
import com.xiaopo.flying.poiphoto.ui.adapter.ListAlbumAdapter;
import com.xiaopo.flying.poiphoto.ui.custom.DividerLine;

import java.util.List;

/**
 * the fragment to display album
 */
public class AlbumFragment extends Fragment {

    RecyclerView mAlbumList, rv_img_download_list;

    private AlbumAdapter mAlbumAdapter;
    private ListAlbumAdapter mListAlbumAdapter;
    private PhotoManager mPhotoManager;
    private ImageView img_back, img_grid_list;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.poiphoto_fragment_album, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, Define.DEFAULT_REQUEST_PERMISSION_CODE);
        } else {
            startLoad();
        }
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        new AlbumTask().execute();
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Define.DEFAULT_REQUEST_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            startLoad();
        }
    }

    private void startLoad() {
        new AlbumTask().execute();
    }

    private void init(View view) {
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            initToolbar(toolbar);
//        }

        mAlbumList = (RecyclerView) view.findViewById(R.id.album_list);
        rv_img_download_list = (RecyclerView) view.findViewById(R.id.rv_img_download_list);
        img_back = (ImageView) view.findViewById(R.id.img_back);
        img_grid_list = (ImageView) view.findViewById(R.id.img_grid_list);

        mPhotoManager = new PhotoManager(getContext());

        mAlbumList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv_img_download_list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        mAlbumAdapter = new AlbumAdapter();
        mAlbumAdapter.setOnItemClickListener(new AlbumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("Album")
                        .replace(R.id.container, PhotoFragment.newInstance(mAlbumAdapter.getBuckedId(position), mAlbumAdapter.getBuckedName(position)))
                        .commit();
            }
        });

        mListAlbumAdapter = new ListAlbumAdapter();
        mListAlbumAdapter.setOnItemClickListener(new ListAlbumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("Album")
                        .replace(R.id.container, PhotoFragment.newInstance(mListAlbumAdapter.getBuckedId(position), mListAlbumAdapter.getBuckedName(position)))
                        .commit();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mAlbumList.setAdapter(mAlbumAdapter);
        mAlbumList.addItemDecoration(new DividerLine());

        rv_img_download_list.setAdapter(mListAlbumAdapter);
        rv_img_download_list.addItemDecoration(new DividerLine());

        img_grid_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlbumList.getVisibility() == View.VISIBLE) {
                    img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.list_view));
                    rv_img_download_list.setVisibility(View.VISIBLE);
                    mAlbumList.setVisibility(View.GONE);
                } else {
                    img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.gird_view));
                    rv_img_download_list.setVisibility(View.GONE);
                    mAlbumList.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initToolbar(Toolbar toolbar) {
        Configure configure = ((PickActivity) getActivity()).getConfigure();
        toolbar.setTitleTextColor(configure.getToolbarTitleColor());
        toolbar.setTitle(configure.getAlbumTitle());
        toolbar.setBackgroundColor(configure.getToolbarColor());
    }


    private void refreshAlbumList(List<Album> alba) {
        mAlbumAdapter.refreshData(alba);
        mListAlbumAdapter.refreshData(alba);
    }


    private class AlbumTask extends AsyncTask<Void, Integer, List<Album>> {

        @Override
        protected List<Album> doInBackground(Void... params) {
            return mPhotoManager.getAlbum();
        }

        @Override
        protected void onPostExecute(List<Album> alba) {
            super.onPostExecute(alba);
            refreshAlbumList(alba);
        }
    }

}
