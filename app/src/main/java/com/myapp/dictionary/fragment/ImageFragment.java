package com.myapp.dictionary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.R;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.model.EnWord;
import com.myapp.utils.CustomSearch;

import java.io.IOException;

public class ImageFragment extends Fragment {

    View convertView;
    RecyclerView recyclerView;
    int enWordId;
    EnWord enWord;
    CustomSearch customSearch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enWordId = getArguments().getInt("enWordId");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_image, container, false);
        recyclerView = convertView.findViewById(R.id.recyclerView);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext().getApplicationContext());
        databaseAccess.open();
        enWord = databaseAccess.getOneEnWord(enWordId);
        databaseAccess.close();


        customSearch = new CustomSearch(getContext(), enWord.getWord());
        try {
            customSearch.fetchImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertView;
    }


}
