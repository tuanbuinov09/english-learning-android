package com.myapp.learnenglish.fragment.ranking;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.myapp.Main;
import com.myapp.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //RecyclerView recyclerView;
    AdapterRank adapterRank;
    TextView tvRank, tvTest;
    TextView tvPointrank1,tvPointrank2,tvPointrank3;
    ImageView imgback;
    Query database;
    ArrayList<UserRanking> list;
    public RankingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankingFragment newInstance(String param1, String param2) {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        // = (RecyclerView) view.findViewById(R.id.userranklist);
        RecyclerView recyclerView = view.findViewById(R.id.userranklist);
        AnhXa(view);
        database = FirebaseDatabase.getInstance().getReference("User").orderByChild("point");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list= new ArrayList<>();
        adapterRank = new AdapterRank(getActivity(),list);
        recyclerView.setAdapter(adapterRank);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    UserRanking userRanking = dataSnapshot.getValue(UserRanking.class);
                    list.add(userRanking);
                }
                //dao nguoc list
                Collections.reverse(list);
                Top3point();
                adapterRank.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //Back
//        imgback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent
//                        = new Intent(RankingFragment.this.getActivity(),
//                        Main.class);
//                startActivity(intent);
//            }
//        });
        return view;
    }
    private void AnhXa(View view)
    {
        //recyclerView = view.findViewById(R.id.userranklist);
        tvPointrank1 = (TextView) view.findViewById(R.id.tvpointrank1);
        tvPointrank2 = (TextView) view.findViewById(R.id.tvpointrank2);
        tvPointrank3 = (TextView) view.findViewById(R.id.tvpointrank3);
        //imgback = (ImageView) view.findViewById(R.id.imgVBackRank);

    }
    private void Top3point()
    {
        tvPointrank1.setText(String.valueOf(list.get(0).getPoint()));
        tvPointrank2.setText(String.valueOf(list.get(1).getPoint()));
        tvPointrank3.setText(String.valueOf(list.get(2).getPoint()));


    }
}