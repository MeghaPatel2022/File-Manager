package com.calintat.explorer.Adapter.Download;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calintat.explorer.Model.DataModel;
import com.calintat.explorer.R;

import java.util.ArrayList;

import static com.calintat.explorer.Adapter.ChildDocAdapter.openDocument;

public class DocDownloadListAdapter extends RecyclerView.Adapter<DocDownloadListAdapter.MyClassView> {

    ArrayList<DataModel> fileList = new ArrayList<>();
    Activity activity;

    public DocDownloadListAdapter(ArrayList<DataModel> fileList, Activity activity) {
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDocument(activity, dataModel.getFilePath());
            }
        });

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        TextView tv_filename, tv_filesize;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            tv_filename = itemView.findViewById(R.id.tv_filename);
            tv_filesize = itemView.findViewById(R.id.tv_filesize);
        }
    }
}
