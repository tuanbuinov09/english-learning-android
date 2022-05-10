package com.myapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.Main;
import com.myapp.R;
import com.myapp.dictionary.EnWordDetailActivity2;
import com.myapp.model.EnWord;

import java.util.ArrayList;

public class EnWordRecyclerAdapter extends
        RecyclerView.Adapter<EnWordRecyclerAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<EnWord> enWordArrayList;
    private ArrayList<EnWord> filteredEnWordArrayList;
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        EnWord enWord = enWordArrayList.get(viewHolder.getAbsoluteAdapterPosition());

        viewHolder.textViewWord.setText(enWord.getWord().trim());

        viewHolder.textViewPronunciation.setText(enWord.getPronunciation().trim());
        viewHolder.textViewMeaning.setText(enWord.getListMeaning().get(0).getMeaning().trim());
//        viewHolder.buttonWordMenu.setVisibility(View.GONE);
        viewHolder.buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main.ttobj.speak(enWordArrayList.get(viewHolder.getAbsoluteAdapterPosition()).getWord(), TextToSpeech.QUEUE_FLUSH, null, null);
                Toast.makeText(mContext, enWordArrayList.get(viewHolder.getAbsoluteAdapterPosition()).getWord(),Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.btnSave_UnsaveWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sau này check trong saved word
                if(viewHolder.unsave==true){
                    //---run unsave code
                    viewHolder.btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_bookmark_outline_32px);
                    viewHolder.unsave = !viewHolder.unsave;
                }else{
                    //---run save code
                    viewHolder.btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
                    viewHolder.unsave = !viewHolder.unsave;
                }

            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), EnWordDetailActivity.class);
                Intent intent = new Intent(view.getContext(), EnWordDetailActivity2.class);
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
    public void filterList(ArrayList<EnWord> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        enWordArrayList = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewWord;
        private TextView textViewPronunciation;
        private TextView textViewMeaning;
        private ImageButton buttonSpeak;
        private ImageButton buttonWordMenu;
        private ImageButton btnSave_UnsaveWord;
        private  boolean unsave;
//        private LinearLayout enWordItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWord = (TextView) itemView.findViewById(R.id.textViewWord);
            textViewPronunciation = (TextView) itemView.findViewById(R.id.textViewPronunciation);
            textViewMeaning = (TextView) itemView.findViewById(R.id.textViewMeaning);
            buttonSpeak = (ImageButton) itemView.findViewById(R.id.buttonSpeak);
            buttonWordMenu = (ImageButton) itemView.findViewById(R.id.buttonWordMenu);
            btnSave_UnsaveWord = (ImageButton) itemView.findViewById(R.id.btnSave_UnsaveWord);
//            enWordItemLayout = (LinearLayout) itemView.findViewById(R.id.enWordItemLayout);
            //lấy dữ liệu thật sau
            unsave = true;
        }
    }
}
