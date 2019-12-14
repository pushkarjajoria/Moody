package com.example.moody.metadata;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.moody.ButtonType;
import com.example.moody.text_watcher.TypingEvent;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class KeyboardActivityUtils {

    private static final KeyboardActivityUtils singleton = new KeyboardActivityUtils();

    public static KeyboardActivityUtils getInstance() {
        return singleton;
    }

    private KeyboardActivityUtils() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Optional<KeyboardActivity> generateKeyboardActivityFrom(TypingEvent beforeEvent, TypingEvent afterEvent) {
        long time = afterEvent.getTime();
        int beforeLength = beforeEvent.getText().length();
        int afterLength = beforeEvent.getText().length();
        if(beforeLength - afterLength == 1) {
            return Optional.of(new KeyboardActivity(ButtonType.BACKSPACE, time));
        }
        else if(beforeLength == afterLength || Math.abs(beforeLength - afterLength) > 1){
            return Optional.empty();
        }
        else {
            try {
                String addedString = StringUtils.difference(afterEvent.getText(), beforeEvent.getText());
                char addedCharacter = addedString.toCharArray()[0];
                if(CharUtils.isAsciiAlpha(addedCharacter) || CharUtils.isAsciiNumeric(addedCharacter)) {
                    return Optional.of(new KeyboardActivity(ButtonType.TEXT_CHARACTER, time));
                }
                else {
                    return Optional.of(new KeyboardActivity(ButtonType.SPECIAL_CHARACTER, time));
                }
            }
            catch (IndexOutOfBoundsException e) {
                return Optional.empty();
            }
        }
    }

}
