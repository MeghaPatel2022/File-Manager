package com.calintat.explorer.Adapter.Download;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.calintat.explorer.Model.DataModel;
import com.calintat.explorer.R;

import java.io.File;
import java.util.ArrayList;

public class OtherDownloadListAdapter extends RecyclerView.Adapter<OtherDownloadListAdapter.MyClassView> {

    ArrayList<DataModel> fileList = new ArrayList<>();
    Activity activity;

    public OtherDownloadListAdapter(ArrayList<DataModel> fileList, Activity activity) {
        this.fileList = fileList;
        this.activity = activity;
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

        if (!dataModel.isDirectory()) {
            if (dataModel.getFilePath().contains(".apk")) {
                holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.app_icon));
            } else if (dataModel.getFilePath().contains(".zip")) {
                holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.zip));
            }
        } else {
            holder.list_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.file));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataModel.getFilePath().contains(".apk")) {
                    File file = new File(dataModel.getFilePath());
                    Uri fileUri = FileProvider.getUriForFile(activity, "com.calintat.explorer.provider", file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
                    activity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        ImageView list_item_image;
        TextView tv_filename;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            list_item_image = itemView.findViewById(R.id.list_item_image);
            tv_filename = itemView.findViewById(R.id.tv_filename);
        }
    }
}
