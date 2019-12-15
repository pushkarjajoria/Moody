package com.example.moody;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.moody.Titanic.Titanic;
import com.example.moody.Titanic.TitanicTextView;
import com.example.moody.Titanic.Typefaces;
import com.example.moody.text_watcher.QueueFactory;
import com.example.moody.text_watcher.TextChangeQueue;
import com.example.moody.text_watcher.TypingEvent;
import com.github.anastr.speedviewlib.ProgressiveGauge;
import com.spark.submitbutton.SubmitButton;

import org.joda.time.DateTime;

import java.util.UUID;

public class Typing_Test extends AppCompatActivity {
    private EditText passwordEditText;
    private String uuid;

    private ProgressiveGauge speedometer;

    private String first_text = "";
    private long first_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typing__test);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        passwordEditText = (EditText) findViewById(R.id.editText);
        passwordEditText.addTextChangedListener(passwordWatcher);

        ActionProcessButton submit = findViewById(R.id.submit);
//        SubmitButton submit = findViewById(R.id.submit);
        submit.setMode(ActionProcessButton.Mode.ENDLESS);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit.setEnabled(false);
                showRadioDialog();
            }
        });

        speedometer = findViewById(R.id.gauge);
        speedometer.setMinMaxSpeed(0, 150);
        speedometer.speedTo(0);
    }

    private void showRadioDialog(){
        String s1 = new String(Character.toChars(Integer.parseInt("1F601", 16)));
        String s2 = new String(Character.toChars(Integer.parseInt("1F642", 16)));
        String s3 = new String(Character.toChars(Integer.parseInt("1F643", 16)));
        final String[] radioItems = new String[]{s1,s2,s3};


        AlertDialog.Builder radioDialog = new AlertDialog.Builder(this);
        radioDialog.setTitle("Choose your mood:");
        radioDialog.setIcon(R.mipmap.ic_launcher_round);

        radioDialog.setSingleChoiceItems(radioItems, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Typing_Test.this,radioItems[which],Toast.LENGTH_SHORT).show();
            }
        });

        radioDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showFinishDialog(which);
            }
        });

        radioDialog.create().show();
    }

    private void showFinishDialog(int which){
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_layout,null);
        SubmitButton dialogBtnConfirm = (SubmitButton) dialogView.findViewById(R.id.submit);

        TitanicTextView tv = (TitanicTextView) dialogView.findViewById(R.id.titanic_tv);
        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
        new Titanic().start(tv);

        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(this);


        layoutDialog.setView(dialogView);

        //设置组件
        dialogBtnConfirm .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = passwordEditText.getText().toString();

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(Typing_Test.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, 1600);

            }
        });

        layoutDialog.create().show();

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

            float speed = (float)(s.toString().length()-first_text.length())/(float)(time- first_time)*60000;
            speedometer.speedTo(speed);
            System.out.printf("%f\n",speed);
            first_text = s.toString();
            first_time = time;
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
