package com.myapp.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.myapp.R;
import com.myapp.model.TranslationHistory;

import java.time.LocalDate;

public class TranslationEditorDialog extends BottomSheetDialogFragment {
    public interface Listener {
        //        void sendDialogResult(CustomDialog.Result result, String request);
        void sendDialogResult(TranslationHistory updatedTranslationHistory);
    }

    View convertView = null;
    private TranslationEditorDialog.Listener listener;
    private TranslationHistory translationHistory;

    public TranslationEditorDialog(TranslationHistory translationHistory) {
        this.translationHistory = translationHistory;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (TranslationEditorDialog.Listener) getActivity();
        } catch (ClassCastException e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.dialog_translation_editor, null);

        TextView tvOriginalText = convertView.findViewById(R.id.tvOriginalText);
        TextView tvTranslatedText = convertView.findViewById(R.id.tvTranslatedText);
        Button btnOK = convertView.findViewById(R.id.btnOK);
        Button btnCancel = convertView.findViewById(R.id.btnCancel);

        tvOriginalText.setText(translationHistory.getOriginalText());
        tvTranslatedText.setText(translationHistory.getTranslatedText());

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newOriginalText = tvOriginalText.getText().toString();
                String newTranslatedText = tvTranslatedText.getText().toString();

                TranslationHistory updatedTranslationHistory = new TranslationHistory(translationHistory.getId(), newOriginalText, newTranslatedText, LocalDate.now());
                listener.sendDialogResult(updatedTranslationHistory);
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return convertView;
    }
}
