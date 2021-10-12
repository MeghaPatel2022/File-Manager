package com.calintat.explorer.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calintat.explorer.Activity.ChildAppActivity;
import com.calintat.explorer.Activity.ChildDocActivityActivity;
import com.calintat.explorer.Activity.ChildInternActivity;
import com.calintat.explorer.Activity.ChildInternAppActivity;
import com.calintat.explorer.Activity.ChildInternDocActivity;
import com.calintat.explorer.Activity.ChildInternMusicActivity;
import com.calintat.explorer.Activity.ChildInternVidActivity;
import com.calintat.explorer.Activity.ChildVideoActivity;
import com.calintat.explorer.Activity.ChildZipActivity;
import com.calintat.explorer.Activity.ChildZipInternActivity;
import com.calintat.explorer.Model.DocumentModel;
import com.calintat.explorer.R;
import com.calintat.explorer.Utils.DownloadData;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class AllDocumentAdapter extends RecyclerView.Adapter<AllDocumentAdapter.MyClassView> {

    ArrayList<DocumentModel> fileList;
    Activity activity;
    String from = "";

    public AllDocumentAdapter(ArrayList<DocumentModel> fileList, Activity activity, String from) {
        this.fileList = fileList;
        this.activity = activity;
        this.from = from;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_documents, null, false);
        return new MyClassView(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        DocumentModel documentModel = fileList.get(position);
        Log.e("LLLL_Name: ", documentModel.getFilePath());

        File file = new File(documentModel.getFilePath());

        if (from.equals("doc")) {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.document));
        } else if (from.equals("zip")) {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.zip));
        } else if (from.equals("zipIntern")) {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.zip));
        } else if (from.equals("InternMusic")) {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.music_cd_black));
        } else if (from.equals("documents")) {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.document));
        } else if (from.equals("appData")) {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.app_icon));
        } else if (from.equals("app")) {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.app_icon));
        } else if (from.equals("video")) {
            holder.list_item_image.setBackgroundColor(activity.getResources().getColor(R.color.black));
            Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(DownloadData.getVideoData(documentModel.getFilePath()).get(0).getFilePath(), MediaStore.Video.Thumbnails.MINI_KIND);
            holder.img_play.setVisibility(View.VISIBLE);
            holder.list_item_image.setImageBitmap(bmThumbnail);
        } else if (from.equals("videos")) {
            holder.list_item_image.setBackgroundColor(activity.getResources().getColor(R.color.black));
            Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(DownloadData.getInternVidData(file).get(0).getFilePath(), MediaStore.Video.Thumbnails.MINI_KIND);
            holder.img_play.setVisibility(View.VISIBLE);
            holder.list_item_image.setImageBitmap(bmThumbnail);
        } else if (from.equals("InternImg")) {
            if (DownloadData.getInternData(file).size() > 0) {
                Picasso
                        .with(activity)
                        .load(new File(DownloadData.getInternData(file).get(0).getFilePath()))
                        .resize(1600, 2048)
                        .centerCrop()
                        .onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
                        .into(holder.list_item_image);
            }

        } else {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.app_item));
        }

        holder.tv_filename.setText(documentModel.getFolderName());
        if (from.equals("doc")) {
            holder.tv_filesize.setText(String.valueOf(DownloadData.getDocumentsData(documentModel.getFilePath()).size()));
        } else if (from.equals("zip")) {
            holder.tv_filesize.setText(String.valueOf(DownloadData.getZipData(documentModel.getFilePath()).size()));
        } else if (from.equals("zipIntern")) {
            holder.tv_filesize.setText(String.valueOf(DownloadData.getInternZipData(file).size()));
        } else if (from.equals("InternMusic")) {
            holder.tv_filesize.setText(String.valueOf(DownloadData.getInternMusicData(file).size()));
        } else if (from.equals("documents")) {
            holder.tv_filesize.setText(String.valueOf(DownloadData.getInternDocData(file).size()));
        } else if (from.equals("app")) {
            holder.tv_filesize.setText(String.valueOf(DownloadData.getAppData(documentModel.getFilePath()).size()));
        } else if (from.equals("appData")) {
            holder.tv_filesize.setText(String.valueOf(DownloadData.getInternAppData(file).size()));
        } else if (from.equals("videos")) {
            holder.tv_filesize.setText(String.valueOf(DownloadData.getInternVidData(file).size()));
        } else if (from.equals("video")) {
            holder.tv_filesize.setText(String.valueOf(DownloadData.getVideoData(documentModel.getFilePath()).size()));
        } else if (from.equals("InternImg")) {
            holder.tv_filesize.setText(String.valueOf(DownloadData.getInternData(file).size()));
        } else {
            holder.tv_filesize.setText(String.valueOf(DownloadData.getAppData(documentModel.getFilePath()).size()));
        }

        if (from.equals("doc")) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ChildDocActivityActivity.class);
                intent.putExtra("folderPath", documentModel.getFilePath());
                activity.startActivity(intent);
                activity.finish();
            });
        } else if (from.equals("zip")) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ChildZipActivity.class);
                intent.putExtra("folderPath", documentModel.getFilePath());
                activity.startActivity(intent);
                activity.finish();
            });
        } else if (from.equals("zipIntern")) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ChildZipInternActivity.class);
                intent.putExtra("folderPath", documentModel.getFilePath());
                activity.startActivity(intent);
                activity.finish();
            });
        } else if (from.equals("InternMusic")) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ChildInternMusicActivity.class);
                intent.putExtra("folderPath", documentModel.getFilePath());
                activity.startActivity(intent);
                activity.finish();
            });
        } else if (from.equals("documents")) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ChildInternDocActivity.class);
                intent.putExtra("folderPath", documentModel.getFilePath());
                activity.startActivity(intent);
                activity.finish();
            });
        } else if (from.equals("app")) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ChildAppActivity.class);
                intent.putExtra("folderPath", documentModel.getFilePath());
                activity.startActivity(intent);
                activity.finish();
            });
        } else if (from.equals("appData")) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ChildInternAppActivity.class);
                intent.putExtra("folderPath", documentModel.getFilePath());
                activity.startActivity(intent);
                activity.finish();
            });
        } else if (from.equals("videos")) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ChildInternVidActivity.class);
                intent.putExtra("folderPath", documentModel.getFilePath());
                activity.startActivity(intent);
                activity.finish();
            });
        } else if (from.equals("video")) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ChildVideoActivity.class);
                intent.putExtra("folderPath", documentModel.getFilePath());
                activity.startActivity(intent);
                activity.finish();
            });
        } else if (from.equals("InternImg")) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ChildInternActivity.class);
                intent.putExtra("folderPath", documentModel.getFilePath());
                activity.startActivity(intent);
                activity.finish();
            });
        } else {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ChildAppActivity.class);
                intent.putExtra("folderPath", documentModel.getFilePath());
                activity.startActivity(intent);
                activity.finish();
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.e("LLLL_Adapter_size: ", String.valueOf(fileList.size()));
        return fileList.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {
        TextView tv_filename, tv_filesize;
        ImageView list_item_image, img_play;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            tv_filename = itemView.findViewById(R.id.tv_filename);
            tv_filesize = itemView.findViewById(R.id.tv_filesize);
            list_item_image = itemView.findViewById(R.id.list_item_image);
            img_play = itemView.findViewById(R.id.img_play);
        }
    }
}
