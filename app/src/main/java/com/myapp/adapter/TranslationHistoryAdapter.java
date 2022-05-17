package com.myapp.adapter;

import android.content.Context;
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

import com.myapp.MainViewModel;
import com.myapp.R;
import com.myapp.dialog.TranslationEditorDialog;
import com.myapp.model.TranslationHistory;
import com.myapp.sqlite.DatabaseHelper;
import com.myapp.sqlite.dao.TranslationHistoryDao;
import com.myapp.utils.TTS;

import java.util.ArrayList;
import java.util.List;

public class TranslationHistoryAdapter extends RecyclerView.Adapter<TranslationHistoryAdapter.TranslationHistoryViewHolder> {
    List<TranslationHistory> list;
    Context context;
    TTS tts;
    MainViewModel mainViewModel;
    boolean isEnable = false;
    boolean isSelectedAll = false;
    List<TranslationHistory> selectedList = new ArrayList<>();
    TextView tvEmpty;
    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
    TranslationHistoryDao translationHistoryDao = new TranslationHistoryDao(databaseHelper);

    public TranslationHistoryAdapter(List<TranslationHistory> list, Context context, TextView tvEmpty) {
        this.list = list;
        this.context = context;
        this.tvEmpty = tvEmpty;
        this.tts = new TTS(context);
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
        holder.tvText.setText(list.get(position).getOriginalText());
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
                //Toast.makeText(context, "btn speak", Toast.LENGTH_SHORT).show();
                tts.speak(holder.tvText.getText().toString());
            }
        });
        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "btn favorite", Toast.LENGTH_SHORT).show();
            }
        });
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

//                            mainViewModel.getTranslationHistory().observe((AppCompatActivity) context, new Observer<TranslationHistory>() {
//                                @Override
//                                public void onChanged(TranslationHistory translationHistory) {
//                                    actionMode.setTitle(String.format("%S selected", translationHistory.getText()));
//                                }
//                            });

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
                                    for (TranslationHistory history : selectedList) {
                                        list.remove(history);
                                        translationHistoryDao.deleteOne(history);
                                    }
                                    if (list.size() == 0) {
                                        tvEmpty.setVisibility(View.VISIBLE);
                                    }
                                    actionMode.finish();
                                    break;
                                case R.id.btnSelectAll:
                                    if (selectedList.size() == list.size()) {
                                        isSelectedAll = false;
                                        selectedList.clear();
                                    } else {
                                        isSelectedAll = true;
                                        selectedList.clear();
                                        selectedList.addAll(list);
                                    }
                                    //mainViewModel.setTranslationHistory(selectedList.get(0));
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnable) {
                    ClickItem(holder);
                } else {
                    //Toast.makeText(context, "You clicked" + list.get(holder.getAbsoluteAdapterPosition()), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, "Item clicked", Toast.LENGTH_SHORT).show();
                    String originalText = holder.tvText.getText().toString();
                    String translatedText = holder.tvTranslatedText.getText().toString();
                    TranslationHistory history = list.get(holder.getAbsoluteAdapterPosition());
                    TranslationEditorDialog translationEditorDialog = new TranslationEditorDialog(history);
                    translationEditorDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "");
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

    private void ClickItem(TranslationHistoryViewHolder holder) {
        TranslationHistory translationHistory = list.get(holder.getAbsoluteAdapterPosition());
        if (holder.ivCheckBox.getVisibility() == View.GONE) {
            holder.ivCheckBox.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
            holder.btnSpeak.setVisibility(View.GONE);
            holder.btnFavorite.setVisibility(View.GONE);
            selectedList.add(translationHistory);
        } else {
            holder.ivCheckBox.setVisibility(View.GONE);
            holder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            holder.btnSpeak.setVisibility(View.VISIBLE);
            holder.btnFavorite.setVisibility(View.GONE);
            selectedList.remove(translationHistory);
        }
        mainViewModel.setText(String.valueOf(selectedList.size()));
        //mainViewModel.setTranslationHistory(translationHistory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TranslationHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvText, tvTranslatedText;
        ImageButton btnSpeak, btnFavorite;
        //LinearLayout parentLayout;
        ImageView ivCheckBox;

        public TranslationHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvText = itemView.findViewById(R.id.tvText);
            tvTranslatedText = itemView.findViewById(R.id.tvTranslatedText);
            btnSpeak = itemView.findViewById(R.id.btnSpeak);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            ivCheckBox = itemView.findViewById(R.id.ivCheckBox);
            //parentLayout = itemView.findViewById(R.id.lyTranslationItem);
        }
    }
}
