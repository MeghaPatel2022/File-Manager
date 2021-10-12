package com.calintat.explorer.fragment.InternDownload;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import com.calintat.explorer.Adapter.Download.AudioDownloadAdapter;
import com.calintat.explorer.Adapter.Download.AudioDownloadListAdapter;
import com.calintat.explorer.Model.DataModel;
import com.calintat.explorer.R;
import com.wang.avi.AVLoadingIndicatorView;
import com.xw.repo.BubbleSeekBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InternAudiosFrament extends Fragment {

    private final Handler mHandler = new Handler();
    @BindView(R.id.rv_audio_download)
    RecyclerView rv_audio_download;
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
//    @BindView(R.id.img_filter)
//    ImageView img_filter;

    AudioDownloadAdapter audDownloadAdapter;
    AudioDownloadListAdapter audioDownloadListAdapter;

    ArrayList<DataModel> audDownloadList = new ArrayList<>();
    ArrayList<DataModel> audMainDownloadList = new ArrayList<>();

    TextView tv_track_name;
    BubbleSeekBar seekbar;
    ImageView img_prev, img_pause, img_next, img_close, artist_art;
    String folderPath = "";
    Dialog dial;
    int posi = 0;
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
        View view = inflater.inflate(R.layout.fragment_audios, container, false);
        ButterKnife.bind(this, view);

        rv_audio_download.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv_audio_download.setNestedScrollingEnabled(false);

        rv_doc_download_list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_doc_download_list.setNestedScrollingEnabled(false);

        audDownloadAdapter = new AudioDownloadAdapter(audMainDownloadList, getActivity(), new AudioDownloadAdapter.musicActionListerner() {
            @Override
            public void onMusicItemClicked(View view, int position) {
                playAudio(audMainDownloadList.get(position), position);
            }
        });
        rv_audio_download.setAdapter(audDownloadAdapter);

        audioDownloadListAdapter = new AudioDownloadListAdapter(audMainDownloadList, getActivity(), new AudioDownloadListAdapter.musicActionListerner() {
            @Override
            public void onMusicItemClicked(View view, int position) {
                playAudio(audMainDownloadList.get(position), position);
            }
        });

        rv_doc_download_list.setAdapter(audioDownloadListAdapter);

        new LongOperation().execute();

        img_grid_list.setOnClickListener(v -> {
            if (rv_audio_download.getVisibility() == View.VISIBLE) {
                img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.list_view));
                rl_List.setVisibility(View.VISIBLE);
                rv_audio_download.setVisibility(View.GONE);
            } else {
                img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.gird_view));
                rl_List.setVisibility(View.GONE);
                rv_audio_download.setVisibility(View.VISIBLE);
            }
        });


        rv_audio_download.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1))
                    onScrolledToBottom();
            }
        });

//        img_filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Collections.reverse(audMainDownloadList);
//
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        audDownloadAdapter.notifyDataSetChanged();
//                        audioDownloadListAdapter.notifyDataSetChanged();
//                    }
//                });
//
//            }
//        });

        dial = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dial.requestWindowFeature(1);
        dial.setContentView(R.layout.dialoge_media_player);
        dial.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dial.setCanceledOnTouchOutside(true);

//        ((DownloadActivity) getActivity()).setOnWidgetClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void onScrolledToBottom() {

        if (audDownloadList.size() == 0) {
            getActivity().runOnUiThread(() -> {
                no_found.setVisibility(View.VISIBLE);
                rv_audio_download.setVisibility(View.GONE);
            });
        } else {


            if (audMainDownloadList.size() < audDownloadList.size()) {
                int x, y;
                if ((audDownloadList.size() - audMainDownloadList.size()) >= 40) {
                    x = audMainDownloadList.size();
                    y = x + 40;
                } else {
                    x = audMainDownloadList.size();
                    y = x + audDownloadList.size() - audMainDownloadList.size();
                }
                for (int i = x; i < y; i++) {
                    audMainDownloadList.add(audDownloadList.get(i));
                }
                getActivity().runOnUiThread(() -> {
                    audDownloadAdapter.notifyDataSetChanged();
                    audioDownloadListAdapter.notifyDataSetChanged();
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
                    String path = imagePath.getAbsolutePath();

                    if (listFile[i].getAbsolutePath().contains(".mp3") ||
                            listFile[i].getAbsolutePath().contains(".wav") ||
                            listFile[i].getAbsolutePath().contains(".ogg")) {
//                        Log.e("LLLLL_Filename_img: ", imagePath.getParentFile().getName());

                        if (imagePath.getParentFile().getName().equalsIgnoreCase("download")) {
                            DataModel dataModel = new DataModel();
                            dataModel.setFilename(imagePath.getName());
                            dataModel.setSize(imagePath.length());
                            dataModel.setFilePath(imagePath.getAbsolutePath());

                            audDownloadList.add(dataModel);
                        }

                    }

                    System.gc();
                }
            }
        }
    }

    private void playAudio(DataModel documentModel, int position) {

        if (player == null) {
            player = new MediaPlayer();
            try {
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
                    if (posi < audMainDownloadList.size() - 1) {
                        posi++;
                        playAudio(audMainDownloadList.get(posi), posi);
                    }
                });

                img_prev.setOnClickListener(v -> {
                    if (posi > 0) {
                        posi--;
                        playAudio(audMainDownloadList.get(posi), posi);
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
                    if (posi < audMainDownloadList.size() - 1) {
                        posi++;
                        playAudio(audMainDownloadList.get(posi), posi);
                    }
                });

                img_prev.setOnClickListener(v -> {
                    if (posi > 1) {
                        posi--;
                        playAudio(audMainDownloadList.get(posi), posi);
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

//    @Override
//    public void onReverseAll() {
//        new LongReverseOperation().execute();
//    }

    private final class LongOperation extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            audMainDownloadList.clear();
            audDownloadList.clear();
            load_image_files(new File(Environment.getStorageDirectory().getAbsolutePath()));
            return "Execute";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Execute")) {
                onScrolledToBottom();
            }
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
            Collections.reverse(audMainDownloadList);

            return "Execute";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Execute")) {
                stopAnim();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        audDownloadAdapter.notifyDataSetChanged();
                        audioDownloadListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

}
