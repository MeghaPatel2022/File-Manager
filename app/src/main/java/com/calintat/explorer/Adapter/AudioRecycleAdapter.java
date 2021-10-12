package com.calintat.explorer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CodeBoy.MediaFacer.mediaHolders.audioContent;
import com.bumptech.glide.Glide;
import com.calintat.explorer.R;

import java.util.ArrayList;

public class AudioRecycleAdapter extends RecyclerView.Adapter<AudioRecycleAdapter.musicViewHolder> {

    private final Context musicActivity;
    private final ArrayList<audioContent> musiclist;
    private final musicActionListerner actionListerner;
    boolean playing;
    //    private View playView;
    private int playPosition = 0;

    public AudioRecycleAdapter(Context context, ArrayList<audioContent> musiclist, musicActionListerner actionListerner) {
        this.musicActivity = context;
        this.musiclist = musiclist;
        this.actionListerner = actionListerner;
    }

    @NonNull
    @Override
    public musicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(musicActivity);
        View itemView = inflater.inflate(R.layout.audio_item, null, false);
        return new musicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull musicViewHolder holder, int position) {
        holder.setItemPosition(position);
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return musiclist.size();
    }

    public interface musicActionListerner {
        void onMusicItemClicked(int position, audioContent audio);

        void onMusicItemLongClicked(int position);
    }

    class musicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        //define views
        ImageView art;
        TextView title;
        TextView artist;
        //        ImageButton play;
        int itemPosition;

        musicViewHolder(@NonNull View itemView) {
            super(itemView);
            //instantiate views
            art = itemView.findViewById(R.id.art);
            title = itemView.findViewById(R.id.title);
            artist = itemView.findViewById(R.id.artist);
//             play = itemView.findViewById(R.id.play);
        }

        void setItemPosition(int position) {
            itemPosition = position;
        }

        void bind() {
            title.setText(musiclist.get(itemPosition).getTitle());
            artist.setText(musiclist.get(itemPosition).getArtist());

            if (itemPosition == 0) {
//                 playView = play;
//                 playView .setVisibility(View.GONE);
            } else if (playPosition == itemPosition && playing) {
//                 playView = play;
//                 playView .setVisibility(View.VISIBLE);
            } else {
//                 play.setVisibility(View.GONE);
            }

            Glide.with(musicActivity)
                    .load(musiclist.get(itemPosition).getArt_uri())
                    .placeholder(R.drawable.music_cd)
                    .into(art);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            actionListerner.onMusicItemClicked(itemPosition, musiclist.get(itemPosition));
            playing = true;
            playPosition = itemPosition;
        }

        @Override
        public boolean onLongClick(View v) {
            actionListerner.onMusicItemLongClicked(itemPosition);
            return false;
        }
    }

}