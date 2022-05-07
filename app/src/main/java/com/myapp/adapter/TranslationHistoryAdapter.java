package com.myapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.MainViewModel;
import com.myapp.R;
import com.myapp.dialog.TranslationEditorDialog;
import com.myapp.model.TranslationHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TranslationHistoryAdapter extends RecyclerView.Adapter<TranslationHistoryAdapter.TranslationHistoryViewHolder> {
    List<TranslationHistory> list;
    Context context;
    TextToSpeech textToSpeech;
    MainViewModel mainViewModel;
    boolean isEnable = false;
    boolean isSelectedAll = false;
    List<TranslationHistory> selectedList = new ArrayList<>();

    public TranslationHistoryAdapter(List<TranslationHistory> list, Context context) {
        this.list = list;
        this.context = context;
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    //Voice voice = new Voice(textToSpeech.getDefaultEngine(), Locale.US, Voice.QUALITY_VERY_HIGH, Voice.LATENCY_NORMAL, true, null);
                    //textToSpeech.setVoice(voice);
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
    }

    @NonNull
    @Override
    public TranslationHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.translation_history_item, parent, false);

        mainViewModel = ViewModelProviders.of((AppCompatActivity) context).get(MainViewModel.class);
        return new TranslationHistoryViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull TranslationHistoryViewHolder holder, int position) {
        holder.tvText.setText(list.get(position).getText());
        holder.tvTranslatedText.setText(list.get(position).getTranslatedText());
//        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "Item clicked", Toast.LENGTH_SHORT).show();
//                String originalText = holder.tvText.getText().toString();
//                String translatedText = holder.tvTranslatedText.getText().toString();
//                TranslationEditorDialog translationEditorDialog = new TranslationEditorDialog(originalText, translatedText);
//                translationEditorDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "");
//            }
//        });
        holder.btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "btn speak", Toast.LENGTH_SHORT).show();
                textToSpeech.speak(holder.tvText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "btn favorite", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!isEnable) {
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

                            mainViewModel.getTranslationHistory().observe((AppCompatActivity) context, new Observer<TranslationHistory>() {
                                @Override
                                public void onChanged(TranslationHistory translationHistory) {
                                    actionMode.setTitle(String.format("%S selected", translationHistory.getText()));
                                }
                            });
                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            int id = menuItem.getItemId();
                            switch (id) {
                                case R.id.btnDelete:
                                    for(TranslationHistory history: selectedList) {
                                        selectedList.remove(history);
                                    }
                                    if(list.size() == 0) {
                                        //tvEmplty
                                    }

                                    actionMode.finish();
                                    break;
                                case R.id.btnSelectAll:
                                    if(selectedList.size() == list.size()) {
                                        isSelectedAll = false;
                                        selectedList.clear();
                                    }
                                    else {
                                        isSelectedAll = true;
                                        selectedList.clear();
                                        selectedList.addAll(list);
                                    }
                                    mainViewModel.setTranslationHistory(selectedList.get(0));
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
                }
                else {
                    ClickItem(holder);
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEnable) {
                    ClickItem(holder);
                }
                else {
                    Toast.makeText(context, "You clicked" + list.get(holder.getAbsoluteAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(isSelectedAll) {
            holder.ivCheckBox.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor((Color.LTGRAY));
        }else {
            holder.ivCheckBox.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void ClickItem(TranslationHistoryViewHolder holder) {
        TranslationHistory translationHistory = list.get(holder.getAbsoluteAdapterPosition());
        if(holder.ivCheckBox.getVisibility() == View.GONE) {
            holder.ivCheckBox.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            selectedList.add(translationHistory);
        }
        else {
            holder.ivCheckBox.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            selectedList.remove(translationHistory);
        }
        mainViewModel.setTranslationHistory(translationHistory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TranslationHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvText, tvTranslatedText;
        ImageButton btnSpeak, btnFavorite;
        LinearLayout parentLayout;
        ImageView ivCheckBox;

        public TranslationHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvText = itemView.findViewById(R.id.tvText);
            tvTranslatedText = itemView.findViewById(R.id.tvTranslatedText);
            btnSpeak = itemView.findViewById(R.id.btnSpeak);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            ivCheckBox = itemView.findViewById(R.id.ivCheckBox);
            parentLayout = itemView.findViewById(R.id.lyTranslationItem);
        }
    }
}
