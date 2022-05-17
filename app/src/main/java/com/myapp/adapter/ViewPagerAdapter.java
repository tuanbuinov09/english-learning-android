package com.myapp.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.myapp.dictionary.fragment.EnWordDetailFragment;
import com.myapp.dictionary.fragment.ImageFragment;
import com.myapp.dictionary.fragment.YourNoteFragment;
import com.myapp.model.EnWord;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private EnWord savedWord;
    private int enWordId;

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, @NonNull Integer enWordId, @NonNull EnWord savedWord) {
        super(fragmentActivity);
        this.enWordId = enWordId;
        this.savedWord = savedWord;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 1: {
                Bundle bundle = new Bundle();
                bundle.putInt("enWordId", enWordId);
                ImageFragment imageFragment = new ImageFragment();
                imageFragment.setArguments(bundle);
                return imageFragment;
            }
            case 2: {
                Bundle bundle = new Bundle();
                bundle.putInt("enWordId", enWordId);
                YourNoteFragment yourNoteFragment = new YourNoteFragment();
                yourNoteFragment.setArguments(bundle);
                return yourNoteFragment;
            }
            default: {
                Bundle bundle = new Bundle();
                bundle.putInt("enWordId", enWordId);

                bundle.putSerializable("enWord", savedWord);
                EnWordDetailFragment enWordDetailFragment = new EnWordDetailFragment(savedWord);
                enWordDetailFragment.setArguments(bundle);
                return enWordDetailFragment;
            }
        }

//        Fragment fragment = new DemoFragment();
//        Bundle args = new Bundle();
//        args.putString(DemoFragment.TITLE, "Tab " + (position + 1));
//        fragment.setArguments(args);
//        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
