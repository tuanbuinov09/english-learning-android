package com.myapp.dictionary.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.myapp.R;
import com.myapp.adapter.ImageAdapter;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.model.EnWord;
import com.myapp.model.Image;
import com.myapp.utils.CustomSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageFragment extends Fragment {

    View convertView;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager layoutManager;
    int enWordId;
    EnWord enWord;
    CustomSearch customSearch;
    List<String> linkList = new ArrayList<>();

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
            linkList = customSearch.getLinkList();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Get link error", e.getLocalizedMessage());
        }
//        try {
//            customSearch.fetchImage();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerViewAdapter = new ImageAdapter(linkList, getContext());
//        recyclerView.setAdapter(recyclerViewAdapter);
        return convertView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            runRequest(enWord.getWord());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ImageFragment ERROR", e.getLocalizedMessage());
        }
    }

    private List<String> getLinkList(String keyword) throws IOException {

        String link = null;
        List<String> linkList = new ArrayList<String>();

        //Make a request to the Google Custom Search API

        return linkList;
    }

    private void runRequest(String keyword) throws IOException {
        String qry = keyword;
        URL url = new URL(
                "https://www.googleapis.com/customsearch/v1?key=" + CustomSearch.key + "&cx=" + CustomSearch.cx + "&q=" + qry
                        + "&searchType=image");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    getData(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void getData(JSONArray array) {
        List<Image> imageList = new ArrayList<>();

        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);

                Image image = new Image();
                image.setLink(object.getString("link"));
                image.setTitle(object.getString("title"));
                image.setFileFormat(object.getString("fileFormat"));

                JSONObject object1 = object.getJSONObject("image");
                image.setContextLink(object1.getString("contextLink"));

                imageList.add(image);
            }

            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerViewAdapter = new ImageAdapter(imageList, getContext());
            recyclerView.setAdapter(recyclerViewAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
