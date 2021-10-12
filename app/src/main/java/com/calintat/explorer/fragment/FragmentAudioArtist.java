package com.calintat.explorer.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
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

import com.CodeBoy.MediaFacer.AudioGet;
import com.CodeBoy.MediaFacer.MediaFacer;
import com.CodeBoy.MediaFacer.mediaHolders.audioArtistContent;
import com.CodeBoy.MediaFacer.mediaHolders.audioContent;
import com.calintat.explorer.Adapter.AudioArtistsAdapter;
import com.calintat.explorer.Adapter.ListAudioArtistsAdapter;
import com.calintat.explorer.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentAudioArtist extends Fragment {

    AudioArtistsAdapter adapter;
    ListAudioArtistsAdapter audioArtistsAdapter;
    ImageView img_grid_list;
    private RecyclerView data_recycler, rv_img_download_list;
    private ArrayList<audioArtistContent> allArtists;
    private ProgressBar progressBar;
    @BindView(R.id.img_filter)
    ImageView img_filter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_artist, container, false);

        ButterKnife.bind(this, view);
        progressBar = view.findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        TextView title = view.findViewById(R.id.title);
        title.setVisibility(View.GONE);

        data_recycler = view.findViewById(R.id.data_recycler);
        img_grid_list = view.findViewById(R.id.img_grid_list);
        data_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        rv_img_download_list = view.findViewById(R.id.rv_img_download_list);
        rv_img_download_list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        firstLoad();

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
                Collections.reverse(allArtists);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        audioArtistsAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void showArtistMusic(audioArtistContent artistContent) {
        FragmentAudio audioDataDisplay = new FragmentAudio();
        ArrayList<audioContent> audioContents = new ArrayList<>();
        for (int i = 0; i < artistContent.getAlbums().size(); i++) {
            audioContents.addAll(artistContent.getAlbums().get(i).getAudioContents());
        }

        audioDataDisplay.setType("Artist", artistContent.getArtistName(), audioContents);

        Transition transition = TransitionInflater.from(getContext()).
                inflateTransition(android.R.transition.explode);

        setEnterTransition(transition);
        setExitTransition(transition);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, audioDataDisplay)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("LLLLL_resume: ", "called: ");
        firstLoad();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adapter != null && audioArtistsAdapter != null) {

                    data_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    rv_img_download_list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

                    AsyncTask<Object, Integer, ArrayList<audioArtistContent>> asyncTask = new getAudioArtistTask();
                    asyncTask.execute();

                }
            }
        });

    }

    public void firstLoad() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (allArtists == null) {
                    AsyncTask<Object, Integer, ArrayList<audioArtistContent>> asyncTask = new getAudioArtistTask();
                    asyncTask.execute();
                }
            }
        });

    }

    private class getAudioArtistTask extends AsyncTask<Object, Integer, ArrayList<audioArtistContent>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected ArrayList<audioArtistContent> doInBackground(Object... objects) {
            ArrayList<String> ids = MediaFacer.withAudioContex(getActivity()).getAllArtistIds(AudioGet.externalContentUri);
            allArtists = MediaFacer.withAudioContex(getActivity()).getAllArtists(ids, AudioGet.externalContentUri);
            return allArtists;
        }

        @Override
        protected void onPostExecute(ArrayList<audioArtistContent> audioArtistContents) {
            super.onPostExecute(audioArtistContents);
            AudioArtistsAdapter.artistListener listener = new AudioArtistsAdapter.artistListener() {
                @Override
                public void onArtistCliced(audioArtistContent artistContent) {
                    showArtistMusic(artistContent);
                }
            };

            adapter = new AudioArtistsAdapter(getActivity(), allArtists, listener);
            data_recycler.setAdapter(adapter);

            audioArtistsAdapter = new ListAudioArtistsAdapter(getActivity(), allArtists, listener);
            rv_img_download_list.setAdapter(audioArtistsAdapter);

            progressBar.setVisibility(View.GONE);
        }

    }
}
