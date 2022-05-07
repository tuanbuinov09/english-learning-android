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

public class TranslationEditorDialog extends BottomSheetDialogFragment {
    public enum Type {
        NOTIFICATION, CONFIRM
    }

    public enum Result {
        OK, CANCEL
    }

    public interface Listener {
        //        void sendDialogResult(CustomDialog.Result result, String request);
        void sendDialogResult(String originalText, String translatedText);
    }

    View convertView = null;
    private TranslationEditorDialog.Listener listener;
    private String originalText, translatedText;

    public TranslationEditorDialog(String originalText, String translatedText) {
        this.originalText = originalText;
        this.translatedText = translatedText;
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

        tvOriginalText.setText(originalText);
        tvTranslatedText.setText(translatedText);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newOriginalText = tvOriginalText.getText().toString();
                String newTranslatedText = tvTranslatedText.getText().toString();
                listener.sendDialogResult(newOriginalText, newTranslatedText);
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
