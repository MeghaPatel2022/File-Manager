package com.xiaopo.flying.poiphoto.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.xiaopo.flying.poiphoto.Define;
import com.xiaopo.flying.poiphoto.R;
import com.xiaopo.flying.poiphoto.datatype.Photo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListPhotoAdapter extends RecyclerView.Adapter<ListPhotoAdapter.ListPhotoViewHolder> {
    private final String TAG = ListPhotoAdapter.class.getSimpleName();
    private final ArrayList<Photo> mSelectedPhotos;
    private final Set<Integer> mSelectedPhotoPositions;
    private List<Photo> mData;
    private OnPhotoSelectedListener mOnPhotoSelectedListener;
    private OnPhotoUnSelectedListener mOnPhotoUnSelectedListener;
    private OnSelectedMaxListener mOnSelectedMaxListener;

    private int mMaxCount = Define.DEFAULT_MAX_COUNT;

    private int mSelectedResId = R.color.photo_selected_shadow;

    public ListPhotoAdapter() {
        mSelectedPhotos = new ArrayList<>();
        mSelectedPhotoPositions = new HashSet<>();
    }

    public List<Photo> getData() {
        return mData;
    }

    public void setData(List<Photo> data) {
        mData = data;
    }

    public int getMaxCount() {
        return mMaxCount;
    }

    public void setMaxCount(int maxCount) {
        mMaxCount = maxCount;
    }

    public void setOnSelectedMaxListener(OnSelectedMaxListener onSelectedMaxListener) {
        mOnSelectedMaxListener = onSelectedMaxListener;
    }

    public void setOnPhotoSelectedListener(OnPhotoSelectedListener onPhotoSelectedListener) {
        mOnPhotoSelectedListener = onPhotoSelectedListener;
    }

    public void setOnPhotoUnSelectedListener(OnPhotoUnSelectedListener onPhotoUnSelectedListener) {
        mOnPhotoUnSelectedListener = onPhotoUnSelectedListener;
    }

    public ArrayList<Photo> getSelectedPhotos() {
        return mSelectedPhotos;
    }

    public ArrayList<String> getSelectedPhotoPaths() {
        ArrayList<String> paths = new ArrayList<>();
        for (Photo photo : mSelectedPhotos) {
            paths.add(photo.getPath());
        }

        return paths;
    }

    public void refreshData(List<Photo> dataNew) {
        mData = dataNew;
        mSelectedPhotos.clear();
        notifyDataSetChanged();
    }

    public void reset() {
        mSelectedPhotos.clear();
        mSelectedPhotoPositions.clear();

        notifyDataSetChanged();
    }

    @Override
    public ListPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_doc_lib, parent, false);
        return new ListPhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListPhotoViewHolder holder, final int position) {

        holder.shadow_view.setBackgroundResource(mSelectedResId);

        if (!mSelectedPhotoPositions.contains(position) && holder.shadow_view.getVisibility() == View.VISIBLE) {
            holder.shadow_view.setVisibility(View.GONE);
        } else if (mSelectedPhotoPositions.contains(position) && holder.shadow_view.getVisibility() != View.VISIBLE) {
            holder.shadow_view.setVisibility(View.VISIBLE);
        }

        final String path = mData.get(position).getPath();

        holder.tv_filename.setText(new File(mData.get(position).getPath()).getName());

        // Get length of file in bytes
        long fileSizeInBytes = new File(mData.get(position).getPath()).length();
        long fileSizeInKB = fileSizeInBytes / 1024;
        long fileSizeInMB = fileSizeInKB / 1024;

        if (new File(mData.get(position).getPath()).length() > 1024 * 1024) {
            holder.tv_filesize.setText(fileSizeInMB + "MB");
        } else if (new File(mData.get(position).getPath()).length() > 1024) {
            holder.tv_filesize.setText(fileSizeInKB + "KB");
        } else {
            holder.tv_filesize.setText(fileSizeInBytes + "B");
        }

        Picasso.with(holder.itemView.getContext())
                .load("file:///" + path)
                .into(holder.img_List_image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Log.e(TAG, "Picasso failed load photo -> " + path);
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (mSelectedPhotoPositions.contains(pos)) {

                    mSelectedPhotoPositions.remove(pos);
                    mSelectedPhotos.remove(mData.get(pos));
                    if (mOnPhotoUnSelectedListener != null) {
                        mOnPhotoUnSelectedListener.onPhotoUnSelected(mData.get(pos), pos);
                    }

                    holder.shadow_view.setVisibility(View.GONE);

                } else {
                    if (mSelectedPhotoPositions.size() >= mMaxCount) {
                        if (mOnSelectedMaxListener != null) mOnSelectedMaxListener.onSelectedMax();
                    } else {
                        mSelectedPhotoPositions.add(pos);
                        mSelectedPhotos.add(mData.get(pos));
                        if (mOnPhotoSelectedListener != null) {
                            mOnPhotoSelectedListener.onPhotoSelected(mData.get(pos), pos);
                        }
                        holder.shadow_view.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setSelectedResId(int selectedResId) {
        mSelectedResId = selectedResId;
    }

    public interface OnPhotoSelectedListener {
        void onPhotoSelected(Photo photo, int position);
    }

    public interface OnPhotoUnSelectedListener {
        void onPhotoUnSelected(Photo photo, int position);
    }

    public interface OnSelectedMaxListener {
        void onSelectedMax();
    }

    public static class ListPhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView img_List_image;
        View shadow_view;
        TextView tv_filename, tv_filesize;

        public ListPhotoViewHolder(View itemView) {
            super(itemView);

            img_List_image = (ImageView) itemView.findViewById(R.id.img_List_image);
            shadow_view = itemView.findViewById(R.id.shadow_view);
            tv_filename = itemView.findViewById(R.id.tv_filename);
            tv_filesize = itemView.findViewById(R.id.tv_filesize);
        }
    }
}
