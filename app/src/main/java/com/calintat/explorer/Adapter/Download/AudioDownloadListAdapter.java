package com.calintat.explorer.Adapter.Download;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calintat.explorer.Model.DataModel;
import com.calintat.explorer.R;

import java.util.ArrayList;

public class AudioDownloadListAdapter extends RecyclerView.Adapter<AudioDownloadListAdapter.MyClassView> {

    private final musicActionListerner actionListerner;
    ArrayList<DataModel> fileList = new ArrayList<>();
    Activity activity;

    public AudioDownloadListAdapter(ArrayList<DataModel> fileList, Activity activity, musicActionListerner actionListerner) {
        this.fileList = fileList;
        this.activity = activity;
        this.actionListerner = actionListerner;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_list_doc, null, false);
        return new MyClassView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        DataModel dataModel = fileList.get(position);

        holder.tv_filename.setText(dataModel.getFilename());
        holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.music_cd));
        // Get file from file name

        // Get length of file in bytes
        long fileSizeInBytes = dataModel.getSize();
        long fileSizeInKB = fileSizeInBytes / 1024;
        long fileSizeInMB = fileSizeInKB / 1024;

        if (dataModel.getSize() > 1024 * 1024) {
            holder.tv_filesize.setText(fileSizeInMB + "MB");
        } else if (dataModel.getSize() > 1024) {
            holder.tv_filesize.setText(fileSizeInKB + "KB");
        } else {
            holder.tv_filesize.setText(fileSizeInBytes + "B");
        }

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public interface musicActionListerner {
        void onMusicItemClicked(View view, int position);
    }

    public class MyClassView extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_filename, tv_filesize;
        ImageView list_item_image;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            tv_filesize = itemView.findViewById(R.id.tv_filesize);
            tv_filename = itemView.findViewById(R.id.tv_filename);
            list_item_image = itemView.findViewById(R.id.list_item_image);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AudioDownloadListAdapter.this.actionListerner.onMusicItemClicked(v, getAdapterPosition());
        }
    }
}
