package com.xiaopo.flying.poiphoto.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.xiaopo.flying.poiphoto.R;
import com.xiaopo.flying.poiphoto.datatype.Album;

import java.io.File;
import java.util.List;

public class ListAlbumAdapter extends RecyclerView.Adapter<ListAlbumAdapter.AlbumViewHolder> {
    private final String TAG = ListAlbumAdapter.class.getSimpleName();

    private List<Album> mData;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public List<Album> getData() {
        return mData;
    }

    public void refreshData(List<Album> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_doc_lib, parent, false);
        return new AlbumViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AlbumViewHolder holder, final int position) {
        holder.mTvTitle.setText(mData.get(position).getName());
        final String path = mData.get(position).getCoverPath();


        holder.folderSize.setText(String.valueOf(mData.get(position).getNumberOfPics()));

        Picasso.with(holder.itemView.getContext())
                .load(new File(path))
                .fit()
                .centerInside()
                .into(holder.mIvPhoto, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Log.e(TAG, "Picasso failed load photo -> " + path);
                    }
                });

        holder.mAlbumContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
    }


    public String getBuckedId(int position) {
        if (mData != null && mData.size() >= position) {
            return mData.get(position).getId();
        } else {
            return "null";
        }
    }

    public String getBuckedName(int position) {
        if (mData != null && mData.size() >= position) {
            return mData.get(position).getName();
        } else {
            return "null";
        }
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvPhoto;
        TextView mTvTitle, folderSize;
        RelativeLayout mAlbumContainer;

        public AlbumViewHolder(View itemView) {
            super(itemView);

            mIvPhoto = (ImageView) itemView.findViewById(R.id.img_List_image);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_filename);
            folderSize = (TextView) itemView.findViewById(R.id.tv_filesize);
            mAlbumContainer = (RelativeLayout) itemView.findViewById(R.id.album_container);

        }
    }
}
