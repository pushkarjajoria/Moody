package com.example.moody;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.moody.text_watcher.QueueFactory;
import com.example.moody.text_watcher.TextChangeQueue;
import com.example.moody.text_watcher.TypingEvent;

import org.joda.time.DateTime;

import java.util.EventListener;
import java.util.UUID;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordEditText = (EditText) findViewById(R.id.editText);
        passwordEditText.addTextChangedListener(passwordWatcher);

        // TODO: 2 New Thread to
    }

    private final TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            TextChangeQueue queue = QueueFactory.getBeforeChangeInstance();
            long time = DateTime.now().getMillis();
            TypingEvent typingEvent = new TypingEvent();
            typingEvent.setTime(time);
            typingEvent.setText(s.toString());
            uuid = UUID.randomUUID().toString();
            typingEvent.setId(uuid);
            queue.addActivity(typingEvent);
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            TextChangeQueue queue = QueueFactory.getAfterChangeInstance();
            long time = DateTime.now().getMillis();
            TypingEvent typingEvent = new TypingEvent();
            typingEvent.setTime(time);
            typingEvent.setText(s.toString());
            typingEvent.setId(uuid);
            uuid = "";
            queue.addActivity(typingEvent);
        }

        @Override
        public void afterTextChanged(Editable s) {
            return;
        }
    };
}
