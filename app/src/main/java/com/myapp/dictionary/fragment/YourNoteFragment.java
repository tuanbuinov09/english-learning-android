package com.myapp.dictionary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.myapp.GlobalVariables;
import com.myapp.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YourNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourNoteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText etYourNote;
    private Button btnSaveNote;
    private Button btnClearNote;
    private int enWordId;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public YourNoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YourNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YourNoteFragment newInstance(String param1, String param2) {
        YourNoteFragment fragment = new YourNoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        enWordId = getArguments().getInt("enWordId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_your_note, container, false);
        final FragmentActivity c = getActivity();
        etYourNote = view.findViewById(R.id.etYourNote);
        btnSaveNote = view.findViewById(R.id.btnSaveNote);
        btnClearNote = view.findViewById(R.id.btnClearNote);
        getUserNote();
//        etYourNote.setText(GlobalVariables.userNote);
        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserNote(etYourNote.getText().toString().trim());
            }
        });
        btnClearNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserNote("");
                etYourNote.setText("");
            }
        });
//        return inflater.inflate(R.layout.fragment_your_note, container, false);
        return view;
    }

    public void getUserNote() {
        GlobalVariables.userNote="";
        GlobalVariables.db.collection("user_note").whereEqualTo("user_id", GlobalVariables.userId)
                .whereEqualTo("word_id", enWordId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            GlobalVariables.userNote = snapshot.getString("note");
                            etYourNote.setText(GlobalVariables.userNote);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), "Oops ... something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
        );
    }

    public void saveUserNote(String content) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", GlobalVariables.userId);
        map.put("word_id", enWordId);
        map.put("note", content);

        GlobalVariables.db.collection("user_note").document(GlobalVariables.userId + enWordId + "").set(map, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        GlobalVariables.userNote = content;

                        if (content.equalsIgnoreCase("")) {
                            Toast.makeText(getActivity(), "Clear ghi chú thành công", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(getActivity(), "Lưu ghi chú thành công", Toast.LENGTH_LONG).show();
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (content.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Clear ghi chú thất bại", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getActivity(), "Clear ghi chú thành công", Toast.LENGTH_LONG).show();
            }
        });
    }
}