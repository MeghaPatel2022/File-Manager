package com.calintat.explorer.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.calintat.explorer.Model.DataModel;
import com.calintat.explorer.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.MyClassView> {

    private final musicActionListerner actionListerner;
    ArrayList<DataModel> fileList;
    Activity activity;

    public RecentAdapter(ArrayList<DataModel> fileList, Activity activity, musicActionListerner actionListerner) {
        this.fileList = fileList;
        this.activity = activity;
        this.actionListerner = actionListerner;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_recent, null, false);
        return new MyClassView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {

        DataModel dataModel = fileList.get(position);
        String pathName = dataModel.getFilePath();
        holder.tv_filename.setText(dataModel.getFilename());
        Bitmap bitmap = null;
        if (!dataModel.isDirectory()) {
            if (dataModel.getFilePath().contains(".mp4") ||
                    dataModel.getFilePath().contains(".mkv")) {
                holder.img_play.setVisibility(View.VISIBLE);
                bitmap = ThumbnailUtils.createVideoThumbnail(dataModel.getFilePath(), MediaStore.Images.Thumbnails.MINI_KIND);
                Glide
                        .with(activity)
                        .load(bitmap)
                        .asBitmap()
                        .into(holder.list_item_image);
            } else if (dataModel.getFilePath().contains(".mp3") ||
                    dataModel.getFilePath().contains(".wav")) {
                holder.img_play.setVisibility(View.GONE);
                holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.music_cd));
            } else if (dataModel.getFilePath().contains(".png") ||
                    dataModel.getFilePath().contains(".jpeg") ||
                    dataModel.getFilePath().contains(".webp") ||
                    dataModel.getFilePath().contains(".JPG") ||
                    dataModel.getFilePath().contains(".CR2") ||
                    dataModel.getFilePath().contains(".jpg")) {
                holder.img_play.setVisibility(View.GONE);

                Picasso
                        .with(activity)
                        .load(new File(dataModel.getFilePath()))
                        .resize(1600, 2048)
                        .onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
                        .into(holder.list_item_image);

            } else if (dataModel.getFilePath().contains(".docx") ||
                    dataModel.getFilePath().contains(".pdf") ||
                    dataModel.getFilePath().contains(".txt") ||
                    dataModel.getFilePath().contains(".xml") ||
                    dataModel.getFilePath().contains(".ppt") ||
                    dataModel.getFilePath().contains(".pptx") ||
                    dataModel.getFilePath().contains(".html")) {
                holder.img_play.setVisibility(View.GONE);
                holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.document));
            } else if (dataModel.getFilePath().contains(".apk")) {
                holder.img_play.setVisibility(View.GONE);
                holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.app_icon));
            } else if (dataModel.getFilePath().contains(".zip")) {
                holder.img_play.setVisibility(View.GONE);
                holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.zip));
            }

        } else {
            holder.img_play.setVisibility(View.GONE);
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.file));
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
        ImageView list_item_image, img_play;
        TextView tv_filename;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            list_item_image = itemView.findViewById(R.id.list_item_image);
            img_play = itemView.findViewById(R.id.img_play);
            tv_filename = itemView.findViewById(R.id.tv_filename);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            RecentAdapter.this.actionListerner.onMusicItemClicked(v, getAdapterPosition());
        }
    }
}
