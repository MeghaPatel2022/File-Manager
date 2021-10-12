package com.calintat.explorer.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.calintat.explorer.R;
import com.calintat.explorer.fragment.GDrive.AllGFragment;
import com.calintat.explorer.fragment.GDrive.AudioGFragment;
import com.calintat.explorer.fragment.GDrive.DocumentGFragment;
import com.calintat.explorer.fragment.GDrive.ImageGFragment;
import com.calintat.explorer.fragment.GDrive.OtherGFragment;
import com.calintat.explorer.fragment.GDrive.VideoGFragment;

public class GDriveTabsPagerAdapter extends FragmentPagerAdapter {

    private final String[] tabTitles = new String[]{"All", "Images", "Videos", "Audios", "Documents", "Other"};
    Context context;

    public GDriveTabsPagerAdapter(FragmentManager fm, Context context) {
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
                return new AllGFragment(); //ChildFragment1 at position 0
            case 1:
                return new ImageGFragment(); //ChildFragment2 at position 1
            case 2:
                return new VideoGFragment();
            case 3:
                return new AudioGFragment();
            case 4:
                return new DocumentGFragment();
            case 5:
                return new OtherGFragment();
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
