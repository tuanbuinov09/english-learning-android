package com.myapp.dictionary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.R;
import com.myapp.adapter.MeaningRecyclerAdapter;
import com.myapp.model.EnWord;
import com.myapp.utils.TTS;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnWordDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnWordDetailFragment extends Fragment {
    public EnWord savedWord;
    public int enWordId;
    public ImageButton buttonSpeak;
    public TextView textViewTitle;
    public TextView textViewWord;
    //    public ImageButton btnSave_UnsaveWord;
    public TextView textViewPronunciation;
    LinearLayoutManager manager;
    public RecyclerView meaningRecyclerView;
    private TTS tts;

    public boolean unsave = true;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context mContext;

    public EnWordDetailFragment() {
        // Required empty public constructor
    }

    public EnWordDetailFragment(EnWord enWord) {
        // Required empty public constructor
        this.savedWord = enWord;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WordDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnWordDetailFragment newInstance(String param1, String param2) {
        EnWordDetailFragment fragment = new EnWordDetailFragment();
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
//        enWordId = getArguments().getInt("enWordId");
//        savedWord = (EnWord) getArguments().getSerializable("enWord");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_word_detail, container, false);
        final FragmentActivity c = getActivity();
        meaningRecyclerView = view.findViewById(R.id.recyclerView);

        buttonSpeak = view.findViewById(R.id.buttonSpeak);
//        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewWord = view.findViewById(R.id.textViewWord);
//        btnSave_UnsaveWord = view.findViewById(R.id.btnSave_UnsaveWord);
        textViewPronunciation = view.findViewById(R.id.textViewPronunciation);

//        textViewTitle.setText(savedWord.getWord().trim());
        textViewWord.setText(savedWord.getWord().trim());
        textViewPronunciation.setText(savedWord.getPronunciation().trim());

        MeaningRecyclerAdapter meaningRecyclerAdapter = new MeaningRecyclerAdapter(c, savedWord.getListMeaning());
        meaningRecyclerView.setAdapter(meaningRecyclerAdapter);
        manager = new LinearLayoutManager(c);
        meaningRecyclerView.setLayoutManager(manager);

        tts = new TTS(getContext());
        buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(savedWord.getWord());
                //Main.ttobj.speak(savedWord.getWord(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
//        btnSave_UnsaveWord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (unsave == true) {
//                    //---run unsave code
//                    btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_bookmark_outline_32px);
//                    unsave = !unsave;
//                } else {
//                    //---run save code
//                    btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
//                    unsave = !unsave;
//                }
//            }
//        });


        return view;
//        return inflater.inflate(R.layout.fragment_word_detail, container, false);
    }
}