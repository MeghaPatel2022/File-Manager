package com.calintat.explorer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CodeBoy.MediaFacer.mediaHolders.audioArtistContent;
import com.bumptech.glide.Glide;
import com.calintat.explorer.R;

import java.util.ArrayList;

public class ListAudioArtistsAdapter extends RecyclerView.Adapter<ListAudioArtistsAdapter.artistViewHolder> {

    private final Context musicActivity;
    private final ArrayList<audioArtistContent> artistlist;
    private final AudioArtistsAdapter.artistListener actionListerner;

    public ListAudioArtistsAdapter(Context contx, ArrayList<audioArtistContent> allArtists, AudioArtistsAdapter.artistListener Listerner) {
        this.musicActivity = contx;
        this.artistlist = allArtists;
        this.actionListerner = Listerner;
    }

    @NonNull
    @Override
    public artistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(musicActivity);
        View itemView = inflater.inflate(R.layout.item_list_doc, null, false);
        return new artistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull artistViewHolder holder, int position) {
        holder.setItemPosition(position);
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return artistlist.size();
    }

    public interface artistListener {
        void onArtistCliced(audioArtistContent artistContent);
    }

    class artistViewHolder extends RecyclerView.ViewHolder {
        //define items
        TextView artistName;
        TextView num_of_songs;
        ImageView artist_art;
        int itemPosition;

        artistViewHolder(@NonNull View itemView) {
            super(itemView);
            //instantiate items
            artistName = itemView.findViewById(R.id.tv_filename);
            num_of_songs = itemView.findViewById(R.id.tv_filesize);
            artist_art = itemView.findViewById(R.id.list_item_image);
        }

        void setItemPosition(int position) {
            itemPosition = position;
        }

        void bind() {
            artistName.setText(artistlist.get(itemPosition).getArtistName());
            int num_songs = artistlist.get(itemPosition).getNumOfSongs();
            String songs = "";
            songs = String.valueOf(num_songs);
            num_of_songs.setText(songs);

            Glide.with(musicActivity)
                    .load(artistlist.get(itemPosition).getAlbums().get(0).getAlbumArtUri())
                    .placeholder(R.drawable.music_cd)
                    .into(artist_art);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionListerner.onArtistCliced(artistlist.get(itemPosition));
                }
            });

        }

    }
}
