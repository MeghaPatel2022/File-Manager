package com.xiaopo.flying.poiphoto.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.xiaopo.flying.poiphoto.Configure;
import com.xiaopo.flying.poiphoto.Define;
import com.xiaopo.flying.poiphoto.PhotoManager;
import com.xiaopo.flying.poiphoto.R;
import com.xiaopo.flying.poiphoto.datatype.Photo;
import com.xiaopo.flying.poiphoto.ui.adapter.ListPhotoAdapter;
import com.xiaopo.flying.poiphoto.ui.adapter.PhotoAdapter;

import java.util.List;

/**
 * the fragment to display photo
 */
public class PhotoFragment extends Fragment {

    private static final String TAG = PhotoFragment.class.getSimpleName();
    RecyclerView mPhotoList, rv_img_download_list;
    ImageView img_grid_list;
    String buckedName;
    private PhotoAdapter mAdapter;
    private ListPhotoAdapter mListAdapter;
    private PhotoManager mPhotoManager;
    private ImageView img_back;
    private TextView folderName;

    public static PhotoFragment newInstance(String bucketId, String Name) {
        Bundle bundle = new Bundle();
        bundle.putString("bucketId", bucketId);
        bundle.putString("bucketName", Name);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(bundle);

        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhotoManager = new PhotoManager(getContext());
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.poiphoto_fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String buckedId = getArguments().getString("bucketId");
        buckedName = getArguments().getString("bucketName");
        init(view);

        new PhotoTask().execute(buckedId);

    }

    private void init(final View view) {
//        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        final Configure configure = ((PickActivity) getActivity()).getConfigure();
//        if (toolbar != null) {
//            initToolbar(toolbar, configure);
//
//        }

        img_back = view.findViewById(R.id.img_back);
//        tv_done = view.findViewById(R.id.tv_done);
        folderName = view.findViewById(R.id.folderName);

        folderName.setText(buckedName);

        img_grid_list = view.findViewById(R.id.img_grid_list);

        mPhotoList = (RecyclerView) view.findViewById(R.id.photo_list);

        mPhotoList.setLayoutManager(new GridLayoutManager(getContext(), 3));

        rv_img_download_list = (RecyclerView) view.findViewById(R.id.rv_img_download_list);

        rv_img_download_list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        mAdapter = new PhotoAdapter();

        mAdapter.setMaxCount(configure.getMaxCount());

        mAdapter.setOnSelectedMaxListener(new PhotoAdapter.OnSelectedMaxListener() {
            @Override
            public void onSelectedMax() {
                Snackbar.make(view, configure.getMaxNotice(), Snackbar.LENGTH_SHORT).show();
            }
        });

        mAdapter.setOnPhotoSelectedListener(new PhotoAdapter.OnPhotoSelectedListener() {
            @Override
            public void onPhotoSelected(Photo photo, int position) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(Define.PATHS, mAdapter.getSelectedPhotoPaths());
                intent.putParcelableArrayListExtra(Define.PHOTOS, mAdapter.getSelectedPhotos());
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });

        mAdapter.setOnPhotoUnSelectedListener(new PhotoAdapter.OnPhotoUnSelectedListener() {
            @Override
            public void onPhotoUnSelected(Photo photo, int position) {

            }
        });

        mListAdapter = new ListPhotoAdapter();

        mListAdapter.setMaxCount(configure.getMaxCount());

        mListAdapter.setOnSelectedMaxListener(new ListPhotoAdapter.OnSelectedMaxListener() {
            @Override
            public void onSelectedMax() {
                Snackbar.make(view, configure.getMaxNotice(), Snackbar.LENGTH_SHORT).show();
            }
        });

        mListAdapter.setOnPhotoSelectedListener(new ListPhotoAdapter.OnPhotoSelectedListener() {
            @Override
            public void onPhotoSelected(Photo photo, int position) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(Define.PATHS, mListAdapter.getSelectedPhotoPaths());
                intent.putParcelableArrayListExtra(Define.PHOTOS, mListAdapter.getSelectedPhotos());
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });

        mListAdapter.setOnPhotoUnSelectedListener(new ListPhotoAdapter.OnPhotoUnSelectedListener() {
            @Override
            public void onPhotoUnSelected(Photo photo, int position) {

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });

//        tv_done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putStringArrayListExtra(Define.PATHS, mAdapter.getSelectedPhotoPaths());
//                intent.putParcelableArrayListExtra(Define.PHOTOS, mAdapter.getSelectedPhotos());
//                getActivity().setResult(Activity.RESULT_OK, intent);
//                getActivity().finish();
//            }
//        });

        mPhotoList.setHasFixedSize(true);
        mPhotoList.setAdapter(mAdapter);

        rv_img_download_list.setHasFixedSize(true);
        rv_img_download_list.setAdapter(mListAdapter);

        img_grid_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhotoList.getVisibility() == View.VISIBLE) {
                    img_grid_list.setImageDrawable(PhotoFragment.this.getActivity().getResources().getDrawable(R.drawable.list_view));
                    rv_img_download_list.setVisibility(View.VISIBLE);
                    mPhotoList.setVisibility(View.GONE);
                } else {
                    img_grid_list.setImageDrawable(PhotoFragment.this.getActivity().getResources().getDrawable(R.drawable.gird_view));
                    rv_img_download_list.setVisibility(View.GONE);
                    mPhotoList.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initToolbar(Toolbar toolbar, Configure configure) {

        if (configure != null) {

            toolbar.setTitle(configure.getPhotoTitle());
            toolbar.setBackgroundColor(configure.getToolbarColor());
            toolbar.setTitleTextColor(configure.getToolbarTitleColor());

            toolbar.inflateMenu(R.menu.menu_pick);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_done) {
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra(Define.PATHS, mAdapter.getSelectedPhotoPaths());
                        intent.putParcelableArrayListExtra(Define.PHOTOS, mAdapter.getSelectedPhotos());
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
                        return true;
                    }
                    return false;
                }
            });

            toolbar.setNavigationIcon(configure.getNavIcon());
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();

                }
            });
        }
    }


    private void refreshPhotoList(List<Photo> photos) {
        mAdapter.refreshData(photos);
        mListAdapter.refreshData(photos);
    }

    private class PhotoTask extends AsyncTask<String, Integer, List<Photo>> {

        @Override
        protected List<Photo> doInBackground(String... params) {
            return mPhotoManager.getPhoto(params[0]);
        }

        @Override
        protected void onPostExecute(List<Photo> photos) {
            super.onPostExecute(photos);
            refreshPhotoList(photos);
        }
    }

}
