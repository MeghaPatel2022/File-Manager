package com.calintat.explorer.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.calintat.explorer.Adapter.ChildDocAdapter;
import com.calintat.explorer.Adapter.ChildDocListAdapter;
import com.calintat.explorer.Model.DocumentModel;
import com.calintat.explorer.R;
import com.calintat.explorer.Utils.DownloadData;
import com.xw.repo.BubbleSeekBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildInternMusicActivity extends AppCompatActivity {

    private final Handler mHandler = new Handler();
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rv_intern_download)
    RecyclerView rv_intern_download;
    @BindView(R.id.rv_intern_download_list)
    RecyclerView rv_intern_download_list;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.img_grid_list)
    ImageView img_grid_list;
    @BindView(R.id.rl_fram)
    RelativeLayout rl_fram;
    @BindView(R.id.rl_List)
    RelativeLayout rl_List;
    TextView tv_track_name;
    BubbleSeekBar seekbar;

    @BindView(R.id.img_filter)
    ImageView img_filter;

    ImageView img_prev, img_pause, img_next, img_close, artist_art;
    ChildDocAdapter allDocumentAdapter;
    ChildDocListAdapter childDocListAdapter;
    ArrayList<DocumentModel> documentsList = new ArrayList<>();
    ArrayList<DocumentModel> documentsList1 = new ArrayList<>();
    String folderPath = "";
    Dialog dial;
    int posi = 0;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_intern_music);

        ButterKnife.bind(ChildInternMusicActivity.this);
        tv_title.setText("Audio Select");

        folderPath = getIntent().getStringExtra("folderPath");

        rv_intern_download.setLayoutManager(new GridLayoutManager(ChildInternMusicActivity.this, 3));
        rv_intern_download.setNestedScrollingEnabled(false);
        allDocumentAdapter = new ChildDocAdapter(getListFolder(), ChildInternMusicActivity.this, "InternMusic", new ChildDocAdapter.musicActionListerner() {
            @Override
            public void onMusicItemClicked(View view, int position) {
                playAudio(documentsList.get(position), position);
            }
        });
        rv_intern_download.setAdapter(allDocumentAdapter);

        rv_intern_download_list.setLayoutManager(new LinearLayoutManager(ChildInternMusicActivity.this, RecyclerView.VERTICAL, false));
        rv_intern_download_list.setNestedScrollingEnabled(false);
        childDocListAdapter = new ChildDocListAdapter(getListFolder(), ChildInternMusicActivity.this, "InternMusic", new ChildDocListAdapter.musicActionListerner() {
            @Override
            public void onMusicItemClicked(View view, int position) {
                playAudio(documentsList.get(position), position);
            }
        });
        rv_intern_download_list.setAdapter(childDocListAdapter);

        Log.e("LLLLL_final Size: ", String.valueOf(getListFolder()));
        img_back.setOnClickListener(v -> onBackPressed());

        dial = new Dialog(ChildInternMusicActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dial.requestWindowFeature(1);
        dial.setContentView(R.layout.dialoge_media_player);
        dial.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dial.setCanceledOnTouchOutside(true);

        img_grid_list.setOnClickListener(v -> {
            if (rl_fram.getVisibility() == View.VISIBLE) {
                img_grid_list.setImageDrawable(ChildInternMusicActivity.this.getResources().getDrawable(R.drawable.list_view));
                rl_List.setVisibility(View.VISIBLE);
                rl_fram.setVisibility(View.GONE);
            } else {
                img_grid_list.setImageDrawable(ChildInternMusicActivity.this.getResources().getDrawable(R.drawable.gird_view));
                rl_List.setVisibility(View.GONE);
                rl_fram.setVisibility(View.VISIBLE);
            }
        });

        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.reverse(documentsList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        allDocumentAdapter.notifyDataSetChanged();
                        childDocListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChildInternMusicActivity.this, InternMusicActivity.class);
        startActivity(intent);
        finish();
    }

    private ArrayList<DocumentModel> getListFolder() {
        documentsList.clear();
        documentsList.addAll(DownloadData.getInternMusicData(new File(folderPath)));

        return documentsList;
    }

    private void playAudio(DocumentModel documentModel, int position) {

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

                tv_track_name.setText(documentModel.getFileName());
                Glide.with(ChildInternMusicActivity.this)
                        .load(R.drawable.music_cd_black)
                        .placeholder(R.drawable.music_cd_black)
                        .error(R.drawable.music_cd_black)
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
                    if (posi < documentsList.size() - 1) {
                        posi++;
                        playAudio(documentsList.get(posi), posi);
                    }
                });

                img_prev.setOnClickListener(v -> {
                    if (posi > 0) {
                        posi--;
                        playAudio(documentsList.get(posi), posi);
                    }
                });

                img_close.setOnClickListener(v -> {
                    player.stop();
                    dial.dismiss();
                });

                runOnUiThread(new Runnable() {
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

                tv_track_name.setText(documentModel.getFileName());
                Glide.with(ChildInternMusicActivity.this)
                        .load(R.drawable.music_cd_black)
                        .placeholder(R.drawable.music_cd_black)
                        .error(R.drawable.music_cd_black)
                        .into(artist_art);

                runOnUiThread(new Runnable() {
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
                    if (posi < documentsList.size() - 1) {
                        posi++;
                        playAudio(documentsList.get(posi), posi);
                    }
                });

                img_prev.setOnClickListener(v -> {
                    if (posi > 1) {
                        posi--;
                        playAudio(documentsList.get(posi), posi);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}