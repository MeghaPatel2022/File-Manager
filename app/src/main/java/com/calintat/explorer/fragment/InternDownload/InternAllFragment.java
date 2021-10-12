package com.calintat.explorer.fragment.InternDownload;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import com.bumptech.glide.Glide;
import com.calintat.explorer.Activity.InternDownloadActivity;
import com.calintat.explorer.Adapter.Download.AllDownloadAdapter;
import com.calintat.explorer.Adapter.Download.AllDownloadListAdapter;
import com.calintat.explorer.Model.DataModel;
import com.calintat.explorer.R;
import com.calintat.explorer.Utils.DownloadData;
import com.calintat.explorer.fragment.Download.IReverse;
import com.wang.avi.AVLoadingIndicatorView;
import com.xw.repo.BubbleSeekBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InternAllFragment extends Fragment implements IReverse {

    private final Handler mHandler = new Handler();
    @BindView(R.id.rv_all_download)
    RecyclerView rv_all_download;
    @BindView(R.id.no_found)
    TextView no_found;
    @BindView(R.id.img_grid_list)
    ImageView img_grid_list;
    @BindView(R.id.rl_List)
    RelativeLayout rl_List;
    @BindView(R.id.rv_doc_download_list)
    RecyclerView rv_doc_download_list;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    AllDownloadAdapter allDownloadAdapter;
    AllDownloadListAdapter allDownloadListAdapter;

    TextView tv_track_name;
    BubbleSeekBar seekbar;
    ImageView img_prev, img_pause, img_next, img_close, artist_art;
    String folderPath = "";
    Dialog dial;
    int posi = 0;
    ArrayList<DataModel> imgDownloadList = new ArrayList<>();
    ArrayList<DataModel> imgMainDownloadList = new ArrayList<>();
    ArrayList<DataModel> imgMainDownloadList1 = new ArrayList<>();
    private MediaPlayer player;

    void startAnim() {
        rl_progress.setVisibility(View.VISIBLE);
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        rl_progress.setVisibility(View.GONE);
        avi.hide();
        // or avi.smoothToHide();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        ButterKnife.bind(this, view);

        rv_all_download.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv_all_download.setNestedScrollingEnabled(false);
        allDownloadAdapter = new AllDownloadAdapter(imgMainDownloadList, getActivity(), (view1, position) -> {
            DataModel dataModel = imgMainDownloadList.get(position);
            if (!dataModel.isDirectory()) {
                if (dataModel.getFilePath().contains(".mp3") ||
                        dataModel.getFilePath().contains(".wav")) {
                    playAudio(imgMainDownloadList.get(position), position);
                }
            }
        });
        rv_all_download.setAdapter(allDownloadAdapter);

        rv_doc_download_list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_doc_download_list.setNestedScrollingEnabled(false);

        allDownloadListAdapter = new AllDownloadListAdapter(imgMainDownloadList, getActivity(), (view1, position) -> {
            DataModel dataModel = imgMainDownloadList.get(position);
            if (!dataModel.isDirectory()) {
                if (dataModel.getFilePath().contains(".mp3") ||
                        dataModel.getFilePath().contains(".wav")) {
                    playAudio(imgMainDownloadList.get(position), position);
                }
            }

        });
        rv_doc_download_list.setAdapter(allDownloadListAdapter);

        img_grid_list.setOnClickListener(v -> {
            if (rv_all_download.getVisibility() == View.VISIBLE) {
                img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.list_view));
                rl_List.setVisibility(View.VISIBLE);
                rv_all_download.setVisibility(View.GONE);
            } else {
                img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.gird_view));
                rl_List.setVisibility(View.GONE);
                rv_all_download.setVisibility(View.VISIBLE);
            }
        });

        rv_all_download.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1))
                    onScrolledToBottom();
            }
        });

        dial = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dial.requestWindowFeature(1);
        dial.setContentView(R.layout.dialoge_media_player);
        dial.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dial.setCanceledOnTouchOutside(true);

//        img_filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Collections.reverse(imgMainDownloadList);
//
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        allDownloadAdapter.notifyDataSetChanged();
//                        allDownloadListAdapter.notifyDataSetChanged();
//                    }
//                });
//
//            }
//        });

        ((InternDownloadActivity) getActivity()).setOnWidgetClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new LongOperation().execute();
    }

    private void playAudio(DataModel documentModel, int position) {

        if (player == null) {
            player = new MediaPlayer();
            try {
                if (!documentModel.isDirectory()) {
                    if (!documentModel.getFilePath().contains(".mp3") ||
                            documentModel.getFilePath().contains(".wav")) {
                        posi++;
                        playAudio(imgMainDownloadList.get(posi), posi);
                    }
                }

                player.setDataSource(documentModel.getFilePath());
                player.setLooping(false);
                player.prepare();
                player.start();

                tv_track_name = dial.findViewById(R.id.tv_track_name);
                artist_art = dial.findViewById(R.id.artist_art);
                img_close = dial.findViewById(R.id.img_close);
                img_prev = dial.findViewById(R.id.img_prev);
                img_next = dial.findViewById(R.id.img_next);
                img_pause = dial.findViewById(R.id.img_pause);
                seekbar = dial.findViewById(R.id.seekbar);
                seekbar.getConfigBuilder().max(player.getDuration() / 1000);

                tv_track_name.setText(documentModel.getFilename());
                Glide.with(getActivity())
                        .load(R.drawable.music_cd_black)
                        .placeholder(R.drawable.music_cd_black).error(R.drawable.music_cd_black)
                        .into(artist_art);

                img_pause.setOnClickListener(v -> {
                    if (player != null) {
                        if (player.isPlaying()) {
                            player.pause();
                            img_pause.setImageDrawable(getResources().getDrawable(R.drawable.play));
                        } else {
                            player.start();
                            img_pause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                        }
                    }
                });

                img_next.setOnClickListener(v -> {
                    if (posi < imgMainDownloadList.size() - 1) {
                        posi++;
                        playAudio(imgMainDownloadList.get(posi), posi);
                    }
                });

                img_prev.setOnClickListener(v -> {
                    if (posi > 0) {
                        posi--;
                        playAudio(imgMainDownloadList.get(posi), posi);
                    }
                });

                img_close.setOnClickListener(v -> {
                    player.stop();
                    dial.dismiss();
                });

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (player != null) {
                            if (player.isPlaying()) {
                                int mCurrentPosition = player.getCurrentPosition() / 1000;
                                seekbar.setProgress(mCurrentPosition);
                                mHandler.postDelayed(this, 1000);
                            }
                        }
                    }
                });

                Log.e("LLLLL_Duration: ", String.valueOf(player.getDuration()));
                seekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                    @Override
                    public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                        if (player != null && fromUser) {
                            player.seekTo(progress * 1000);
                        }
                    }

                    @Override
                    public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

                    }

                    @Override
                    public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                    }
                });

                dial.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            player.release();
            player = new MediaPlayer();
            try {
                if (!documentModel.isDirectory()) {
                    if (!documentModel.getFilePath().contains(".mp3") ||
                            documentModel.getFilePath().contains(".wav")) {
                        posi++;
                        playAudio(imgMainDownloadList.get(posi), posi);
                    }
                }

                player.setDataSource(documentModel.getFilePath());

                player.setLooping(false);
                player.prepare();
                player.start();

                tv_track_name = dial.findViewById(R.id.tv_track_name);
                artist_art = dial.findViewById(R.id.artist_art);
                img_close = dial.findViewById(R.id.img_close);
                img_prev = dial.findViewById(R.id.img_prev);
                img_next = dial.findViewById(R.id.img_next);
                img_pause = dial.findViewById(R.id.img_pause);
                seekbar = dial.findViewById(R.id.seekbar);
                seekbar.getConfigBuilder().max(player.getDuration() / 1000);

                tv_track_name.setText(documentModel.getFilename());
                Glide.with(getActivity())
                        .load(R.drawable.music_cd_black)
                        .placeholder(R.drawable.music_cd_black).error(R.drawable.music_cd_black)
                        .into(artist_art);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (player != null) {
                            if (player.isPlaying()) {
                                int mCurrentPosition = player.getCurrentPosition() / 1000;
                                seekbar.setProgress(mCurrentPosition);
                                mHandler.postDelayed(this, 1000);
                            }
                        }
                    }
                });

                img_pause.setOnClickListener(v -> {
                    if (player != null) {
                        if (player.isPlaying()) {
                            player.pause();
                            img_pause.setImageDrawable(getResources().getDrawable(R.drawable.play));
                        } else {
                            player.start();
                            img_pause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                        }
                    }
                });

                img_next.setOnClickListener(v -> {
                    if (posi < imgMainDownloadList.size() - 1) {
                        posi++;
                        playAudio(imgMainDownloadList.get(posi), posi);
                    }
                });

                img_prev.setOnClickListener(v -> {
                    if (posi > 1) {
                        posi--;
                        playAudio(imgMainDownloadList.get(posi), posi);
                    }
                });

                img_close.setOnClickListener(v -> {
                    player.stop();
                    dial.dismiss();
                });

                Log.e("LLLLL_Duration: ", String.valueOf(player.getDuration()));
                seekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                    @Override
                    public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                        if (player != null && fromUser) {
                            player.seekTo(progress * 1000);
                        }
                    }

                    @Override
                    public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

                    }

                    @Override
                    public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                    }
                });

                dial.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getListFolder() {

        imgDownloadList.addAll(imgMainDownloadList1);

        onScrolledToBottom();
    }

    private void onScrolledToBottom() {
        ArrayList<DataModel> documentModels = imgDownloadList;

        if (imgDownloadList.size() == 0) {
            getActivity().runOnUiThread(() -> {
                no_found.setVisibility(View.VISIBLE);
                rv_all_download.setVisibility(View.GONE);
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
                if (getActivity() != null)
                    getActivity().runOnUiThread(() -> {
                        allDownloadAdapter.notifyDataSetChanged();
                        allDownloadListAdapter.notifyDataSetChanged();
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

                    if (imagePath.getParentFile().getName().equalsIgnoreCase("download")) {
                        DataModel dataModel = new DataModel();
                        dataModel.setFilename(imagePath.getName());
                        dataModel.setSize(imagePath.length());
                        dataModel.setFilePath(imagePath.getAbsolutePath());

                        imgMainDownloadList1.add(dataModel);

                    }

                }
            }
        }

    }

    @Override
    public void onReverseAll() {
        Collections.reverse(imgMainDownloadList);

        new LongReverseOperation().execute();
    }

    private final class LongOperation extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            load_image_files(new File(DownloadData.InternDirNamedir));
            return "Execute";
        }

        @Override
        protected void onPostExecute(String result) {
            getListFolder();
        }
    }

    private final class LongReverseOperation extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startAnim();
        }

        @Override
        protected String doInBackground(Void... params) {
            Collections.reverse(imgMainDownloadList);

            return "Execute";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Execute")) {
                stopAnim();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        allDownloadAdapter.notifyDataSetChanged();
                        allDownloadListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }
}