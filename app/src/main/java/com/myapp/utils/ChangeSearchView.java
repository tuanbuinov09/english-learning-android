package com.myapp.utils;

import android.content.Context;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import com.myapp.R;

public class ChangeSearchView {
    public static void change(SearchView searchInput, Context context) {
        TextView textView = (TextView) searchInput.findViewById(androidx.appcompat.R.id.search_src_text);
        textView.setTextColor(context.getColor(R.color.space_cadet));
        textView.setHintTextColor(context.getColor(R.color.azureish_white));
    }

    public static void change(android.widget.SearchView searchInput, Context context) {
        TextView textView = (TextView) searchInput.findViewById(androidx.appcompat.R.id.search_src_text);
        textView.setTextColor(context.getColor(R.color.space_cadet));
        textView.setHintTextColor(context.getColor(R.color.azureish_white));

    }
}
