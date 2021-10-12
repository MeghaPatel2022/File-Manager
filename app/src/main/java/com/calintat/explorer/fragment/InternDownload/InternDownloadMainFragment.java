package com.calintat.explorer.fragment.InternDownload;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.calintat.explorer.Adapter.InternTabsPagerAdapter;
import com.calintat.explorer.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InternDownloadMainFragment extends Fragment {

    @BindView(R.id.tab_download)
    TabLayout tab_download;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    InternTabsPagerAdapter tabsPagerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        ButterKnife.bind(this, view);

        tabsPagerAdapter = new InternTabsPagerAdapter(getActivity().getSupportFragmentManager(), getActivity());
        viewPager.setAdapter(tabsPagerAdapter);

        tab_download.setupWithViewPager(viewPager);

        for (int i = 0; i < tab_download.getTabCount(); i++) {
            TabLayout.Tab tab = tab_download.getTabAt(i);
            Objects.requireNonNull(tab).setCustomView(tabsPagerAdapter.getTabView(i));
        }

        Objects.requireNonNull(tab_download.getTabAt(0)).select();
        tab_download.setTabIndicatorFullWidth(false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && viewPager.getCurrentItem() == 1) {
                Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();

                return true;
            } else
                return false;

        });

        tab_download.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView tv = Objects.requireNonNull(tab.getCustomView()).findViewById(R.id.tv_tab);
                tv.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                tv.setTextColor(Color.parseColor("#FFFFFF"));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tv = Objects.requireNonNull(tab.getCustomView()).findViewById(R.id.tv_tab);
                tv.setBackground(getResources().getDrawable(R.drawable.tab_unselect));
                tv.setTextColor(Color.parseColor("#000000"));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}

