package com.myapp.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.Main;
import com.myapp.R;
import com.myapp.model.EnWord;

import java.util.ArrayList;

public class YourWordAdapter extends ArrayAdapter {
    Context context;
    ArrayList<EnWord> enWordList;
    int layoutID;
    TextToSpeech ttobj;

    public YourWordAdapter(Context context, ArrayList<EnWord> enWordList, int layoutID/*, TextToSpeech ttobj*/) {
        super(context,layoutID);
        this.context = context;
        this.enWordList = enWordList;
        this.layoutID = layoutID;
//        this.ttobj = ttobj;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return enWordList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflter = (LayoutInflater.from(context));
        convertView = inflter.inflate(layoutID, null);
        TextView textViewWord = (TextView) convertView.findViewById(R.id.textViewWord);
        TextView textViewPronunciation = (TextView) convertView.findViewById(R.id.textViewPronunciation);
        TextView textViewMeaning = (TextView) convertView.findViewById(R.id.textViewMeaning);
        ImageButton buttonSpeak = (ImageButton) convertView.findViewById(R.id.buttonSpeak);

        textViewWord.setText(enWordList.get(position).getWord());
        textViewPronunciation.setText(enWordList.get(position).getPronunciation());
        textViewMeaning.setText(enWordList.get(position).getListMeaning().get(0).getMeaning());

        buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main.ttobj.speak(enWordList.get(position).getWord(), TextToSpeech.QUEUE_FLUSH, null);
                Toast.makeText(context, enWordList.get(position).getWord(),Toast.LENGTH_SHORT).show();
            }
        });

//        layoutClickToSeeDetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, EnWordDetail.class);
//                intent.putExtra("enWord", enWordList.get(position));
//                context.startActivity(intent);
////                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//            }
//        });

        return convertView;
    }
}

