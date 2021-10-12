package com.calintat.explorer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.calintat.explorer.R;
import com.calintat.explorer.fragment.FragmentAudioArtist;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AudioActivity extends AppCompatActivity {

    @BindView(R.id.fragment_holder)
    RelativeLayout fragment_holder;
    FragmentAudioArtist fragmentAudioArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        ButterKnife.bind(AudioActivity.this);
        loadFragment();
    }

    private void loadFragment(){
        fragmentAudioArtist = new FragmentAudioArtist();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, fragmentAudioArtist);
        fragmentTransaction.commit();
    }
}