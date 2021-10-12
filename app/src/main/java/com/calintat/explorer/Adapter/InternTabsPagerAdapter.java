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
import com.calintat.explorer.fragment.InternDownload.InternAllFragment;
import com.calintat.explorer.fragment.InternDownload.InternAudiosFrament;
import com.calintat.explorer.fragment.InternDownload.InternDocumentsFragment;
import com.calintat.explorer.fragment.InternDownload.InternImagesFragment;
import com.calintat.explorer.fragment.InternDownload.InternOtherFragment;
import com.calintat.explorer.fragment.InternDownload.InternVideosFragment;

public class InternTabsPagerAdapter extends FragmentPagerAdapter {

    private final String[] tabTitles = new String[]{"All", "Images", "Videos", "Audios", "Documents", "Other"};
    Context context;

    public InternTabsPagerAdapter(FragmentManager fm, Context context) {
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
                return new InternAllFragment(); //ChildFragment1 at position 0
            case 1:
                return new InternImagesFragment(); //ChildFragment2 at position 1
            case 2:
                return new InternVideosFragment();
            case 3:
                return new InternAudiosFrament();
            case 4:
                return new InternDocumentsFragment();
            case 5:
                return new InternOtherFragment();
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
