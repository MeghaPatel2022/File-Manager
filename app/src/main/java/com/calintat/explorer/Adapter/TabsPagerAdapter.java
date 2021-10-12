package com.calintat.explorer.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.calintat.explorer.R;
import com.calintat.explorer.fragment.Download.AllFragment;
import com.calintat.explorer.fragment.Download.AudiosFrament;
import com.calintat.explorer.fragment.Download.DocumentsFragment;
import com.calintat.explorer.fragment.Download.ImagesFragment;
import com.calintat.explorer.fragment.Download.OtherFragment;
import com.calintat.explorer.fragment.Download.VideosFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    Context context;
    private String[] tabTitles = new String[]{"All", "Images","Videos","Audios","Documents","Other"};

    public TabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = v.findViewById(R.id.tv_tab);
        tv.setText(tabTitles[position]);

        if (position == 0) {
            tv.setBackground(context.getResources().getDrawable(R.drawable.tab_selected));
            tv.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            tv.setBackground(context.getResources().getDrawable(R.drawable.tab_unselect));
            tv.setTextColor(Color.parseColor("#000000"));
        }


        return v;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new AllFragment(); //ChildFragment1 at position 0
            case 1:
                return new ImagesFragment(); //ChildFragment2 at position 1
            case 2:
                return new VideosFragment();
            case 3:
                return new AudiosFrament();
            case 4:
                return new DocumentsFragment();
            case 5:
                return new OtherFragment();
        }

        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 6;
    }
}
