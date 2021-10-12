package com.calintat.explorer.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.calintat.explorer.Activity.ImgeViewActivity;
import com.calintat.explorer.Model.DocumentModel;
import com.calintat.explorer.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ChildDocAdapter extends RecyclerView.Adapter<ChildDocAdapter.MyClassView> {

    ArrayList<DocumentModel> fileList = new ArrayList<>();
    Activity activity;
    String isFrom = "";
    private musicActionListerner actionListerner;

    public ChildDocAdapter(ArrayList<DocumentModel> fileList, Activity activity, String isFrom) {
        this.fileList = fileList;
        this.activity = activity;
        this.isFrom = isFrom;
    }

    public ChildDocAdapter(ArrayList<DocumentModel> fileList, Activity activity, String isFrom, musicActionListerner actionListerner) {
        this.fileList = fileList;
        this.activity = activity;
        this.isFrom = isFrom;
        this.actionListerner = actionListerner;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_documents, null, false);
        return new MyClassView(itemView);
    }

    public static void openDocument(Activity activity, String name) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        File file = new File(name);
        Uri fileUri = FileProvider.getUriForFile(activity, "com.calintat.explorer.provider", file);
        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (extension.equalsIgnoreCase("") || mimetype == null) {
            // if there is no extension or there is no definite mimetype, still try to open the file
            intent.setDataAndType(fileUri, "text/*");
        } else {
            intent.setDataAndType(fileUri, mimetype);
        }
        // custom message for the intent
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//DO NOT FORGET THIS EVER
        activity.startActivity(Intent.createChooser(intent, "Choose an Application:"));
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        DocumentModel documentModel = fileList.get(position);

        if (isFrom.equals("doc")) {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.document));
        } else if (isFrom.equals("zip")) {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.zip));
        } else if (isFrom.equals("InternMusic")) {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.music_cd_black));
        } else if (isFrom.equals("documents")) {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.document));
        } else if (isFrom.equals("app")) {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.app_icon));
        } else if (isFrom.equals("appData")) {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.app_icon));
        } else if (isFrom.equals("InternVid")) {
            holder.list_item_image.setBackgroundColor(activity.getResources().getColor(R.color.black));
            Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(documentModel.getFilePath(), MediaStore.Video.Thumbnails.MINI_KIND);
            holder.img_play.setVisibility(View.VISIBLE);
            holder.list_item_image.setImageBitmap(bmThumbnail);
        } else if (isFrom.equals("video")) {
            holder.list_item_image.setBackgroundColor(activity.getResources().getColor(R.color.black));
            Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(documentModel.getFilePath(), MediaStore.Video.Thumbnails.MINI_KIND);
            holder.img_play.setVisibility(View.VISIBLE);
            holder.list_item_image.setImageBitmap(bmThumbnail);
        } else if (isFrom.equals("InternImg")) {
            Picasso
                    .with(activity)
                    .load(new File(documentModel.getFilePath()))
                    .resize(1600, 2048)
                    .centerCrop()
                    .onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
                    .into(holder.list_item_image);
        } else {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.app_icon));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFrom.equals("video")) {
                    File file = new File(documentModel.getFilePath());
                    Uri fileUri = FileProvider.getUriForFile(activity, "com.calintat.explorer.provider", file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(fileUri, "video/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//DO NOT FORGET THIS EVER
                    activity.startActivity(intent);
                } else if (documentModel.getFilePath().contains(".jpg") || documentModel.getFilePath().contains(".JPG")
                        || documentModel.getFilePath().contains(".jpeg") || documentModel.getFilePath().contains(".JPEG")
                        || documentModel.getFilePath().contains(".png") || documentModel.getFilePath().contains(".PNG")
                        || documentModel.getFilePath().contains(".gif") || documentModel.getFilePath().contains(".GIF")
                        || documentModel.getFilePath().contains(".bmp") || documentModel.getFilePath().contains(".BMP")) {
                    Intent intent = new Intent(activity, ImgeViewActivity.class);
                    intent.putExtra("Filepath", documentModel.getFilePath());
                    activity.startActivity(intent);
                } else if (documentModel.getFilePath().contains(".apk")) {
                    File file = new File(documentModel.getFilePath());
                    Uri fileUri = FileProvider.getUriForFile(activity, "com.calintat.explorer.provider", file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
                    activity.startActivity(intent);
                } else {
                    openDocument(activity, documentModel.getFilePath());
                }
            }
        });


        holder.tv_filename.setText(documentModel.getFileName());

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public interface musicActionListerner {
        void onMusicItemClicked(View view, int position);
    }

    public class MyClassView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_filename;
        ImageView list_item_image, img_play;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            tv_filename = itemView.findViewById(R.id.tv_filename);
            list_item_image = itemView.findViewById(R.id.list_item_image);
            img_play = itemView.findViewById(R.id.img_play);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            ChildDocAdapter.this.actionListerner.onMusicItemClicked(v, getAdapterPosition());
        }
    }
}
