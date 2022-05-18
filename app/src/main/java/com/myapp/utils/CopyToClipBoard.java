package com.myapp.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

public class CopyToClipBoard {
    public static void doCopy(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Đã sao chép văn bản", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Đã sao chép văn bản vào bộ nhớ", Toast.LENGTH_SHORT).show();
    }
}
