package com.myapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.EnWordDetailActivity;
import com.myapp.Main;
import com.myapp.R;
import com.myapp.model.EnWord;

import java.util.ArrayList;

public class EnWordRecyclerAdapter extends
        RecyclerView.Adapter<EnWordRecyclerAdapter.ViewHolder> {

    private Context context;
    private Context mContext;
    private ArrayList<EnWord> enWordArrayList;
    public EnWordRecyclerAdapter(Context mContext, ArrayList<EnWord> enWordArrayList){
            this.mContext = mContext;
            this.enWordArrayList = enWordArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View heroView = inflater.inflate(R.layout.en_word_item_for_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EnWord enWord = enWordArrayList.get(position);

        holder.textViewWord.setText(enWord.getWord());
        holder.textViewPronunciation.setText(enWord.getPronunciation());

        holder.buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main.ttobj.speak(enWordArrayList.get(holder.getAbsoluteAdapterPosition()).getWord(), TextToSpeech.QUEUE_FLUSH, null);
//                Toast.makeText(context, enWordArrayList.get(holder.getAbsoluteAdapterPosition()).getWord(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        EnWord enWord = enWordArrayList.get(viewHolder.getAbsoluteAdapterPosition());

        viewHolder.textViewWord.setText(enWord.getWord());
        viewHolder.textViewPronunciation.setText(enWord.getPronunciation());
        viewHolder.textViewMeaning.setText(enWord.getListMeaning().get(0).getMeaning());

//        viewHolder.buttonWordMenu.setVisibility(View.GONE);
        viewHolder.buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main.ttobj.speak(enWordArrayList.get(viewHolder.getAbsoluteAdapterPosition()).getWord(), TextToSpeech.QUEUE_FLUSH, null, null);
                Toast.makeText(context, enWordArrayList.get(viewHolder.getAbsoluteAdapterPosition()).getWord(),Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EnWordDetailActivity.class);
                intent.putExtra("enWordId", enWordArrayList.get(viewHolder.getAbsoluteAdapterPosition()).getId());
                view.getContext().startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public int getItemCount() {
        return enWordArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewWord;
        private TextView textViewPronunciation;
        private TextView textViewMeaning;
        private ImageButton buttonSpeak;
        private ImageButton buttonWordMenu;
//        private LinearLayout enWordItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWord = (TextView) itemView.findViewById(R.id.textViewWord);
            textViewPronunciation = (TextView) itemView.findViewById(R.id.textViewPronunciation);
            textViewMeaning = (TextView) itemView.findViewById(R.id.textViewMeaning);
            buttonSpeak = (ImageButton) itemView.findViewById(R.id.buttonSpeak);
            buttonWordMenu = (ImageButton) itemView.findViewById(R.id.buttonWordMenu);
//            enWordItemLayout = (LinearLayout) itemView.findViewById(R.id.enWordItemLayout);
        }
    }
}
