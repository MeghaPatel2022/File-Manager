package com.calintat.explorer.fragment;

import android.app.Dialog;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CodeBoy.MediaFacer.mediaHolders.audioContent;
import com.bumptech.glide.Glide;
import com.calintat.explorer.Adapter.AudioRecycleAdapter;
import com.calintat.explorer.Adapter.ListAudioRecycleAdapter;
import com.calintat.explorer.R;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentAudio extends Fragment {

    private final Handler mHandler = new Handler();
    TextView tv_track_name;
    BubbleSeekBar seekbar;
    ImageView img_prev, img_pause, img_next, img_close, artist_art;
    int posi = 0;
    Dialog dial;
    private TextView title;
    ImageView img_grid_list;
    private MediaPlayer player;
    private String Title;
    private ArrayList<audioContent> files;
    private RecyclerView data_recycler, rv_img_download_list;

    @BindView(R.id.img_filter)
    ImageView img_filter;

    ListAudioRecycleAdapter listallaudio;
    AudioRecycleAdapter allaudio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressBar progressBar = view.findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        title = view.findViewById(R.id.title);
        title.setText(Title);
        title.setVisibility(View.VISIBLE);

        img_grid_list = view.findViewById(R.id.img_grid_list);

        dial = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dial.requestWindowFeature(1);
        dial.setContentView(R.layout.dialoge_media_player);
        dial.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dial.setCanceledOnTouchOutside(true);

        data_recycler = view.findViewById(R.id.data_recycler);
        data_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        rv_img_download_list = view.findViewById(R.id.rv_img_download_list);
        rv_img_download_list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        img_grid_list.setOnClickListener(v -> {
            if (data_recycler.getVisibility() == View.VISIBLE) {
                img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.list_view));
                rv_img_download_list.setVisibility(View.VISIBLE);
                data_recycler.setVisibility(View.GONE);
            } else {
                img_grid_list.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.gird_view));
                rv_img_download_list.setVisibility(View.GONE);
                data_recycler.setVisibility(View.VISIBLE);
            }
        });

        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.reverse(files);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listallaudio.notifyDataSetChanged();
                        allaudio.notifyDataSetChanged();
                    }
                });

            }
        });

        showData(files);
    }

    void setType(String type, String name, ArrayList<audioContent> files) {
        Title = type + " : " + name;
        this.files = files;
    }

    private void showData(ArrayList<audioContent> files) {

        AudioRecycleAdapter.musicActionListerner listerner = new AudioRecycleAdapter.musicActionListerner() {
            @Override
            public void onMusicItemClicked(int position, audioContent audio) {
                posi = position;
                playAudio(audio, position);
            }

            @Override
            public void onMusicItemLongClicked(int position) {
//                showAudioInfo(position);
            }
        };

        ListAudioRecycleAdapter.musicActionListerner listerner1 = new ListAudioRecycleAdapter.musicActionListerner() {
            @Override
            public void onMusicItemClicked(int position, audioContent audio) {
                posi = position;
                playAudio(audio, position);
            }

            @Override
            public void onMusicItemLongClicked(int position) {
//                showAudioInfo(position);
            }
        };

        allaudio = new AudioRecycleAdapter(getActivity(), files, listerner);
        data_recycler.setAdapter(allaudio);

        listallaudio = new ListAudioRecycleAdapter(getActivity(), files, listerner1);
        rv_img_download_list.setAdapter(listallaudio);

    }

    private void playAudio(audioContent audio, int position) {

        Uri content = Uri.parse(audio.getAssetFileStringUri());
        if (player == null) {
            player = new MediaPlayer();
            try {
                AssetFileDescriptor file = getActivity().getContentResolver().openAssetFileDescriptor(content, "r");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    player.setDataSource(file);

                } else {
                    player.setDataSource(audio.getFilePath());
                }
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
                seekbar.getConfigBuilder().max(audio.getDuration() * 1000);

                tv_track_name.setText(audio.getTitle());
                Log.e("LLLLL_Data: ", String.valueOf(audio.getArt_uri()));
                Glide.with(getContext())
                        .load(audio.getArt_uri())
                        .placeholder(R.drawable.music_cd_black).error(R.drawable.music_cd_black)
                        .into(artist_art);

                img_pause.setOnClickListener(v -> {
                    if (player != null) {
                        if (player.isPlaying()) {
                            player.pause();
                            img_pause.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.play));
                        } else {
                            player.start();
                            img_pause.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pause));
                        }
                    }
                });

                img_next.setOnClickListener(v -> {
                    if (posi < files.size() - 1) {
                        posi++;
                        playAudio(files.get(posi), posi);
                    }
                });

                img_prev.setOnClickListener(v -> {
                    if (posi > 0) {
                        posi--;
                        playAudio(files.get(posi), posi);
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

                Log.e("LLLLL_Duration: ", String.valueOf(audio.getDuration()));
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
                AssetFileDescriptor file = getActivity().getContentResolver().openAssetFileDescriptor(content, "r");
                assert file != null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    player.setDataSource(file);
                } else {
                    player.setDataSource(audio.getFilePath());
                }
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
                seekbar.getConfigBuilder().max(audio.getDuration() * 1000);


                tv_track_name.setText(audio.getTitle());
                Log.e("LLLLL_Data: ", String.valueOf(audio.getArt_uri()));
                Glide.with(getActivity())
                        .load(audio.getArt_uri())
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
                            img_pause.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.play));
                        } else {
                            player.start();
                            img_pause.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pause));
                        }
                    }
                });

                img_next.setOnClickListener(v -> {
                    if (posi < files.size() - 1) {
                        posi++;
                        playAudio(files.get(posi), posi);
                    }
                });

                img_prev.setOnClickListener(v -> {
                    if (posi > 1) {
                        posi--;
                        playAudio(files.get(posi), posi);
                    }
                });

                img_close.setOnClickListener(v -> {
                    player.stop();
                    dial.dismiss();
                });


                Log.e("LLLLL_Duration: ", String.valueOf(audio.getDuration()));
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

//    private void showAudioInfo(int position) {
//        audioInfo audio = new audioInfo();
//        audio.setAudio(files.get(position));
//        audio.show(getActivity().getSupportFragmentManager(), "audio_info");
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
