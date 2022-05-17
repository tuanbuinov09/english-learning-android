package com.myapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.GlobalVariables;
import com.myapp.R;
import com.myapp.dictionary.EnWordDetailActivity2;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.model.EnWord;
import com.myapp.utils.FileIO2;
import com.myapp.utils.TTS;

import java.util.ArrayList;
import java.util.List;

public class EnWordRecyclerAdapter extends
        RecyclerView.Adapter<EnWordRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<EnWord> enWordArrayList;
    private TTS tts;

    public EnWordRecyclerAdapter(Context mContext, ArrayList<EnWord> enWordArrayList) {
        this.mContext = mContext;
        this.enWordArrayList = enWordArrayList;
        this.tts = new TTS(mContext);
    }

    public EnWordRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
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
        try {
            viewHolder.textViewMeaning.setText(enWord.getListMeaning().get(0).getMeaning().trim());

        } catch (Exception ex) {
        }

//        viewHolder.buttonWordMenu.setVisibility(View.GONE);
        viewHolder.buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(enWordArrayList.get(viewHolder.getAbsoluteAdapterPosition()).getWord());
            }
        });
        // neu trong danh sach da luu thi to mau vang
        if (GlobalVariables.listSavedWordId.contains(enWord.getId())) {
            viewHolder.unsave = true;
            viewHolder.btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
        } else {
            viewHolder.unsave = false;
            viewHolder.btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_bookmark_outline_32px);
        }
        if (GlobalVariables.userId.equalsIgnoreCase("") || GlobalVariables.userId == null) {
            viewHolder.btnSave_UnsaveWord.setVisibility(View.GONE);
        } else {
            viewHolder.btnSave_UnsaveWord.setVisibility(View.VISIBLE);
        }
        viewHolder.btnSave_UnsaveWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sau này check trong saved word
                if (viewHolder.unsave == true) {
                    //---run unsave code
//                    GlobalVariables.db.collection("saved_word").document(GlobalVariables.userId + enWord.getId() + "")
//                            .delete()
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Toast.makeText(mContext, "Xoa từ khoi danh sach thanh cong", Toast.LENGTH_LONG).show();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(mContext, "Xoa từ khoi danh sach that bai", Toast.LENGTH_LONG).show();
//
//                                }
//                            });
                    GlobalVariables.listSavedWordId.remove(GlobalVariables.listSavedWordId.indexOf(enWord.getId()));

                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(mContext);
                    databaseAccess.open();
                    databaseAccess.unSaveOneWord(GlobalVariables.userId, enWord.getId());
                    databaseAccess.close();

                    viewHolder.btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_bookmark_outline_32px);
                    viewHolder.unsave = !viewHolder.unsave;
                } else {
                    //---run save code
//                    HashMap<String, Object> map = new HashMap<>();
//                    map.put("user_id", GlobalVariables.userId);
//                    map.put("word_id", enWord.getId());
//                    GlobalVariables.db.collection("saved_word")
//                            .document(GlobalVariables.userId + enWord.getId() + "").set(map, SetOptions.merge())
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    Toast.makeText(mContext, "Lưu từ thành công", Toast.LENGTH_LONG).show();
//                                }
//
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(mContext, "Lưu từ khong thành công", Toast.LENGTH_LONG).show();
//                    }
//                });

                    //them ca vao trong nay cho de dung
                    GlobalVariables.listSavedWordId.add((enWord.getId()));

                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(mContext);
                    databaseAccess.open();
                    databaseAccess.saveOneWord(GlobalVariables.userId, enWord.getId());
                    databaseAccess.close();

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
                Integer enWordId = enWordArrayList.get(viewHolder.getAbsoluteAdapterPosition()).getId();
                intent.putExtra("enWordId", enWordId);
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
        if (filterllist.isEmpty()) {
            System.out.println("ket qua tim kiem = 0" + filterllist.size());
            return;
        }
        if (enWordArrayList.isEmpty()) {
            return;
        }

        enWordArrayList = filterllist;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewWord;
        private TextView textViewPronunciation;
        private TextView textViewMeaning;
        private ImageButton buttonSpeak;
        private ImageButton buttonWordMenu;
        private ImageButton btnSave_UnsaveWord;
        private boolean unsave;
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
