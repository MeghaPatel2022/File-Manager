package com.calintat.explorer.holder;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.calintat.explorer.R;
import com.calintat.explorer.RecyclerOnItemClickListener;
import com.calintat.explorer.Utils.FileUtils;
import com.calintat.explorer.Utils.PreferenceUtils;

import java.io.File;

import static com.calintat.explorer.Utils.FileUtils.*;
import static com.calintat.explorer.Utils.PreferenceUtils.getBoolean;

public final class RecyclerViewHolder0 extends RecyclerViewHolder
{
    private TextView name;
    private TextView date;
    private TextView size;

    public RecyclerViewHolder0(Context context, RecyclerOnItemClickListener listener, View view)
    {
        super(context,listener,view);
    }

    @Override
    protected void loadIcon()
    {
        image=(ImageView)itemView.findViewById(R.id.list_item_image);
    }

    @Override
    protected void loadName()
    {
        name=(TextView)itemView.findViewById(R.id.list_item_name);
    }

    @Override
    protected void loadInfo()
    {
        date=(TextView)itemView.findViewById(R.id.list_item_date);
        size=(TextView)itemView.findViewById(R.id.list_item_size);
    }

    @Override
    protected void bindIcon(File file,Boolean selected)
    {
        if(getBoolean(context,"pref_icon",true))
        {
            image.setOnClickListener(onActionClickListener);
            image.setOnLongClickListener(onActionLongClickListener);

            if(selected)
            {
                int color=ContextCompat.getColor(context,R.color.misc_file);
//                image.setBackground(getBackground(color));
                Drawable drawable=ContextCompat.getDrawable(context,R.drawable.ic_selected);
                DrawableCompat.setTint(drawable,Color.rgb(255,255,255));
                image.setImageDrawable(drawable);
            }
            else
            {
                //TODO: thumbnail

                int color=ContextCompat.getColor(context,getColorResource(file));
//                image.setBackground(getBackground(color));
                Drawable drawable=ContextCompat.getDrawable(context,getImageResource(file));
                DrawableCompat.setTint(drawable,Color.rgb(255,255,255));
                image.setImageDrawable(drawable);
            }
        }
        else
        {
            int color=ContextCompat.getColor(context,getColorResource(file));
            image.setBackground(null);
            Drawable drawable=ContextCompat.getDrawable(context,getImageResource(file));
            DrawableCompat.setTint(drawable,color);
            image.setImageDrawable(drawable);
        }
    }

    @Override
    protected void bindName(File file)
    {
        boolean extension= PreferenceUtils.getBoolean(context,"pref_extension",true);
        name.setText(extension ? getName(file) : file.getName());
    }

    @Override
    protected void bindInfo(File file)
    {
        date.setText(FileUtils.getLastModified(file));
        size.setText(FileUtils.getSize(context,file));
        setVisibility(date,PreferenceUtils.getBoolean(context,"pref_date",true));
        setVisibility(size,PreferenceUtils.getBoolean(context,"pref_size",false));
    }

//    private ShapeDrawable getBackground(int color)
//    {
//        ShapeDrawable shapeDrawable=new ShapeDrawable(new OvalShape());
//        int size=(int)context.getResources().getDimension(R.dimen.avatar_size);
//        shapeDrawable.setIntrinsicWidth(size);
//        shapeDrawable.setIntrinsicHeight(size);
//        shapeDrawable.getPaint().setColor(color);
//
//        return shapeDrawable;
//    }
}