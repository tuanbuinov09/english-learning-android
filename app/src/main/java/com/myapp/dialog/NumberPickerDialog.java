package com.myapp.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.myapp.R;
import com.shawnlin.numberpicker.NumberPicker;

public class NumberPickerDialog extends BottomSheetDialogFragment {
    NumberPicker numberPicker;

    public interface Listener {
        void sendDialogResult(int selectNumber);
    }

    View convertView = null;

    private NumberPickerDialog.Listener listener;
    private String title;
    private int value;

    public NumberPickerDialog(String title, int value) {
        this.title = title;
        this.value = value;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (Listener) getActivity();
        } catch (ClassCastException e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.dialog_number_picker, null);
        numberPicker = (NumberPicker) convertView.findViewById(R.id.number_picker);
        Button button = convertView.findViewById(R.id.btnOK);

        numberPicker.setValue(value);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = numberPicker.getValue();
                listener.sendDialogResult(value);
                dismiss();
            }
        });
        return convertView;
    }
}
