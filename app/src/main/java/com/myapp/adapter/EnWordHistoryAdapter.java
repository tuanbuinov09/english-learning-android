package com.myapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.GlobalVariables;
import com.myapp.MainViewModel;
import com.myapp.R;
import com.myapp.dictionary.EnWordDetailActivity2;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.model.EnWord;
import com.myapp.utils.FileIO2;
import com.myapp.utils.TTS;

import java.util.ArrayList;
import java.util.List;

public class EnWordHistoryAdapter extends
        RecyclerView.Adapter<EnWordHistoryAdapter.ViewHolder> {
    private Context context;
    private List<Integer> wordList;
    TextView tvEmpty;
    private TTS tts;
    boolean isEnable = false;
    boolean isSelectedAll = false;
    List<Integer> selectedList = new ArrayList<>();
    MainViewModel mainViewModel;

    public EnWordHistoryAdapter(Context context, List<Integer> wordList, TextView tvEmpty) {
        this.context = context;
        this.wordList = wordList;
        this.tvEmpty = tvEmpty;
        tts = new TTS(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View heroView = inflater.inflate(R.layout.en_word_item_for_recyclerview_2, parent, false);
        mainViewModel = ViewModelProviders.of((AppCompatActivity) context).get(MainViewModel.class);
        return new ViewHolder(heroView);
    }

    public EnWord getWord(Integer enWordId) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context.getApplicationContext());
        databaseAccess.open();
        EnWord enWord = databaseAccess.getOneEnWord(enWordId);
        databaseAccess.close();
        return enWord;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Integer enWordId = wordList.get(holder.getAbsoluteAdapterPosition());
        EnWord enWord = getWord(enWordId);

        holder.textViewWord.setText(enWord.getWord().trim());
        holder.textViewPronunciation.setText(enWord.getPronunciation().trim());
        try {
            holder.textViewMeaning.setText(enWord.getListMeaning().get(0).getMeaning().trim());

        } catch (Exception ex) {

        }

        holder.buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(enWord.getWord());
            }
        });

        // neu trong danh sach da luu thi to mau vang
        if (GlobalVariables.listSavedWordId.contains(enWord.getId())) {
            holder.unsave = true;
            //.btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
        } else {
            holder.unsave = false;
            //holder.btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_bookmark_outline_32px);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!isEnable) {
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            MenuInflater menuInflater = actionMode.getMenuInflater();
                            menuInflater.inflate(R.menu.translation_history_menu, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            isEnable = true;
                            ClickItem(holder);

                            mainViewModel.getText().observe((LifecycleOwner) context, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    actionMode.setTitle(String.format("%S selected", s));
                                }
                            });
                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            int id = menuItem.getItemId();
                            switch (id) {
                                case R.id.btnDelete:
                                    for (Integer enWordId : selectedList) {
                                        wordList.remove(enWordId);
                                        //translationHistoryDao.deleteOne(enWordId);
                                        FileIO2.writeToFile(wordList, context);
                                    }
                                    if (wordList.size() == 0) {
                                        tvEmpty.setVisibility(View.VISIBLE);
                                    }
                                    actionMode.finish();
                                    break;
                                case R.id.btnSelectAll:
                                    if (selectedList.size() == wordList.size()) {
                                        isSelectedAll = false;
                                        selectedList.clear();
                                    } else {
                                        isSelectedAll = true;
                                        selectedList.clear();
                                        selectedList.addAll(wordList);
                                    }
                                    mainViewModel.setText(String.valueOf(selectedList.size()));
                                    notifyDataSetChanged();
                                    break;
                            }
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode actionMode) {
                            isEnable = false;
                            isSelectedAll = false;
                            selectedList.clear();
                            notifyDataSetChanged();
                        }
                    };

                    ((AppCompatActivity) view.getContext()).startActionMode(callback);
                } else {
                    ClickItem(holder);
                }
                return true;
            }
        });

        holder.btnSave_UnsaveWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sau này check trong saved word
                if (holder.unsave == true) {
                    GlobalVariables.listSavedWordId.remove(GlobalVariables.listSavedWordId.indexOf(enWord.getId()));

                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
                    databaseAccess.open();
                    databaseAccess.unSaveOneWord(GlobalVariables.userId, enWord.getId());
                    databaseAccess.close();

                    holder.btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_bookmark_outline_32px);
                    holder.unsave = !holder.unsave;
                } else {
                    //them ca vao trong nay cho de dung
                    GlobalVariables.listSavedWordId.add((enWord.getId()));

                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
                    databaseAccess.open();
                    databaseAccess.saveOneWord(GlobalVariables.userId, enWord.getId());
                    databaseAccess.close();

                    holder.btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
                    holder.unsave = !holder.unsave;
                }

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnable) {
                    ClickItem(holder);
                } else {
                    Intent intent = new Intent(view.getContext(), EnWordDetailActivity2.class);
                    intent.putExtra("enWordId", wordList.get(enWordId));
                    view.getContext().startActivity(intent);
                }
            }
        });

        if (isSelectedAll) {
            holder.ivCheckBox.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        } else {
            holder.ivCheckBox.setVisibility(View.GONE);
            holder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        }
    }

    private void ClickItem(EnWordHistoryAdapter.ViewHolder holder) {
        Integer enWordId = wordList.get(holder.getAbsoluteAdapterPosition());
        if (holder.ivCheckBox.getVisibility() == View.GONE) {
            holder.ivCheckBox.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
            holder.buttonSpeak.setVisibility(View.GONE);
            holder.btnSave_UnsaveWord.setVisibility(View.GONE);
            selectedList.add(enWordId);
        } else {
            holder.ivCheckBox.setVisibility(View.GONE);
            holder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            holder.buttonSpeak.setVisibility(View.VISIBLE);
            holder.btnSave_UnsaveWord.setVisibility(View.VISIBLE);
            selectedList.remove(enWordId);
        }
        mainViewModel.setText(String.valueOf(selectedList.size()));
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewWord;
        TextView textViewPronunciation;
        TextView textViewMeaning;
        ImageButton buttonSpeak;
        ImageButton buttonWordMenu;
        ImageButton btnSave_UnsaveWord;
        ImageView ivCheckBox;
        private boolean unsave;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWord = (TextView) itemView.findViewById(R.id.textViewWord);
            textViewPronunciation = (TextView) itemView.findViewById(R.id.textViewPronunciation);
            textViewMeaning = (TextView) itemView.findViewById(R.id.textViewMeaning);
            buttonSpeak = (ImageButton) itemView.findViewById(R.id.buttonSpeak);
            buttonWordMenu = (ImageButton) itemView.findViewById(R.id.buttonWordMenu);
            btnSave_UnsaveWord = (ImageButton) itemView.findViewById(R.id.btnSave_UnsaveWord);
            ivCheckBox = itemView.findViewById(R.id.ivCheckBox);
            unsave = true;
        }
    }
}
