package com.myapp.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;

import java.util.Locale;

public class TTS {
    Context context;
    TextToSpeech textToSpeech;

    public TTS(Context context) {
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

    public void speak(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
