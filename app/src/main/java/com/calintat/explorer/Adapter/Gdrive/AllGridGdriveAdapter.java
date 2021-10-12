package com.calintat.explorer.Adapter.Gdrive;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calintat.explorer.Model.GDrive;
import com.calintat.explorer.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AllGridGdriveAdapter extends RecyclerView.Adapter<AllGridGdriveAdapter.MyClassView> {

    private static final DecimalFormat format = new DecimalFormat("#.##");
    private static final long MiB = 1024 * 1024;
    private static final long KiB = 1024;
    ArrayList<GDrive> gDriveArrayList;
    Activity activity;
    String name;

    public AllGridGdriveAdapter(ArrayList<GDrive> gDriveArrayList, Activity activity, String name) {
        this.gDriveArrayList = gDriveArrayList;
        this.activity = activity;
        this.name = name;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_drive, null, false);
        return new MyClassView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        GDrive gDrive = gDriveArrayList.get(position);

        Log.e("LLLLL_ID_URL: ", "https://drive.google.com/file/d/" + gDrive.getId() + "/view?usp=drivesdk");

//        Glide.with(activity)
////                .load("https://docs.google.com/uc?id=" + gDrive.getId())
////                .load("https://drive.google.com/file/d/" + gDrive.getId() + "/view?usp=drivesdk")
//                .load("https://drive.google.com/uc?export=download&id=" + gDrive.getId())
////                .load("https://drive.google.com/open?id=" + gDrive.getId())
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        Log.e("LLLL_Failed", "Error loading image", e);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        holder.list_item_image.setImageDrawable(resource);
//                        return false;
//                    }
//
//                })
//                .into(holder.list_item_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/a/google.com/uc?id=" + gDrive.getId() + "&export=download"));
                activity.startActivity(browserIntent);
            }
        });

        if (name.equals("audio")) {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.music_cd));
        } else if (name.equals("document")) {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.document));
        } else if (name.equals("other")) {
            if (gDrive.getName().contains(".apk") ||
                    gDrive.getName().contains(".aab")) {
                holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.app_icon));
            }
        } else if (name.equals("all")) {
            if (gDrive.getName().contains(".apk") ||
                    gDrive.getName().contains(".aab")) {
                holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.app_icon));
            } else if (gDrive.getName().contains(".docx") ||
                    gDrive.getName().contains(".pdf") ||
                    gDrive.getName().contains(".txt") ||
                    gDrive.getName().contains(".xml") ||
                    gDrive.getName().contains(".ppt") ||
                    gDrive.getName().contains(".pptx") ||
                    gDrive.getName().contains(".html")) {
                holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.document));
            } else if (gDrive.getName().contains(".mp3") ||
                    gDrive.getName().contains(".wav") ||
                    gDrive.getName().contains(".ogg")) {
                holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.music_cd));
            } else if (gDrive.getName().contains(".mp4") ||
                    gDrive.getName().contains(".mkv")) {
//                holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.music_cd));
            }
        }

        holder.tv_filesize.setText(getFileSize(gDrive.getSize()));

//        Picasso
//                .with(activity)
//                .load()
//                .error(R.drawable.ic_image)
//                .into(holder.list_item_image);

        holder.tv_filename.setText(gDrive.getName());
    }

    @Override
    public int getItemCount() {
        return gDriveArrayList.size();
    }

    public String getFileSize(long file) {


        final double length = Double.parseDouble(String.valueOf(file));

        if (length > MiB) {
            return format.format(length / MiB) + " MB";
        }
        if (length > KiB) {
            return format.format(length / KiB) + " KB";
        }
        return format.format(length) + " B";
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        ImageView list_item_image;
        TextView tv_filename, tv_filesize;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            list_item_image = itemView.findViewById(R.id.list_item_image);
            tv_filename = itemView.findViewById(R.id.tv_filename);
            tv_filesize = itemView.findViewById(R.id.tv_filesize);
        }
    }
}

