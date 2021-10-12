package com.calintat.explorer.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.calintat.explorer.R;
import com.calintat.explorer.RecyclerOnItemClickListener;
import com.calintat.explorer.Utils.FileUtils;
import com.calintat.explorer.Utils.PreferenceUtils;

import java.io.File;

import static com.calintat.explorer.Utils.FileUtils.getColorResource;
import static com.calintat.explorer.Utils.FileUtils.getName;

public final class RecyclerViewHolder2 extends RecyclerViewHolder {
    private TextView name;

    private TextView date;

    public RecyclerViewHolder2(Context context, RecyclerOnItemClickListener listener, View view) {
        super(context, listener, view);
    }

    @Override
    protected void loadIcon() {
        image = (ImageView) itemView.findViewById(R.id.list_item_image);
    }

    @Override
    protected void loadName() {
        name = (TextView) itemView.findViewById(R.id.list_item_name);
    }

    @Override
    protected void loadInfo() {
        date = (TextView) itemView.findViewById(R.id.list_item_date);
    }

    @Override
    protected void bindIcon(File file, Boolean selected) {
        final int color = ContextCompat.getColor(context, getColorResource(file));

        Glide.with(context)
                .load(file)
                .asBitmap()
                .fitCenter()
                .into(image);
    }

    @Override
    protected void bindName(File file) {
        boolean extension = PreferenceUtils.getBoolean(context, "pref_extension", true);
        name.setText(extension ? getName(file) : file.getName());
    }

    @Override
    protected void bindInfo(File file) {
        if (date == null) return;
        date.setText(FileUtils.getLastModified(file));
    }

}
