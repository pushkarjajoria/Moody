package com.example.moody;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.moody.Titanic.Titanic;
import com.example.moody.Titanic.TitanicTextView;
import com.example.moody.Titanic.Typefaces;
import com.example.moody.metadata.ActivityQueue;
import com.example.moody.metadata.JavaActivityQueue;
import com.example.moody.metadata.QueueIngester;
import com.example.moody.persistence.Database;
import com.example.moody.persistence.SQLService;
import com.example.moody.text_watcher.QueueFactory;
import com.example.moody.text_watcher.TextChangeQueue;
import com.example.moody.text_watcher.TextQueueIngester;
import com.example.moody.text_watcher.TypingEvent;
import com.github.anastr.speedviewlib.ProgressiveGauge;
import com.spark.submitbutton.SubmitButton;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.UUID;

public class Typing_Test extends AppCompatActivity {
    private EditText passwordEditText;
    private String uuid;

    private ProgressiveGauge speedometer;

    private String cal_text = "";
    private long cal_time = 0;

    private String final_text = "";
    private long start_time = 0;
    private long final_time = 0;
    private QueueIngester metadataIngester;
    private TextQueueIngester textIngester;

    private SQLiteDatabase mooddb;

    private ActionProcessButton submit;

    private int chosenNumber = -2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typing__test);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        passwordEditText = (EditText) findViewById(R.id.editText);
        passwordEditText.addTextChangedListener(passwordWatcher);

        submit = findViewById(R.id.submit);
//        SubmitButton submit = findViewById(R.id.submit);
//        submit.setEnabled(false);
        submit.setMode(ActionProcessButton.Mode.ENDLESS);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRadioDialog();
            }
        });

        speedometer = findViewById(R.id.gauge);
        speedometer.setMinMaxSpeed(0, 350);
        speedometer.speedTo(0);

        start_time = DateTime.now().getMillis();
        uuid = UUID.randomUUID().toString();
        SQLService service = new SQLService();
        mooddb = service.new MoodDbHelper(this.getApplicationContext()).getWritableDatabase();
        Database.getInstance().setDatabase(mooddb);
        ActivityQueue activityQueue = JavaActivityQueue.getInstance();
        textIngester = new TextQueueIngester();
        metadataIngester = new QueueIngester(5, service);
        textIngester.setRunning(true);
        metadataIngester.setRunning(true);
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {
                textIngester.startIngestActivities(activityQueue);
            }
        }).start();

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {
                metadataIngester.startIngestActivities(activityQueue, uuid);
            }
        }).start();
    }

    private void showRadioDialog(){
        if (final_text.length() < 30 ){
            Toast.makeText(Typing_Test.this,"Text is too short, at least 30 characters.",Toast.LENGTH_SHORT).show();
            return;
        }


        final_time = DateTime.now().getMillis();
        String s1 = new String(Character.toChars(Integer.parseInt("1F604", 16)));
        String s2 = new String(Character.toChars(Integer.parseInt("1F603", 16)));
        String s3 = new String(Character.toChars(Integer.parseInt("1F610", 16)));
        String s4= new String(Character.toChars(Integer.parseInt("1F643", 16)));
        String s5 = new String(Character.toChars(Integer.parseInt("1F62D", 16)));
        final String[] radioItems = new String[]{s1,s2,s3,s4,s5};

        AlertDialog.Builder radioDialog = new AlertDialog.Builder(this);
        radioDialog.setTitle("Choose your mood:");
        radioDialog.setIcon(R.mipmap.ic_launcher_round);

        radioDialog.setSingleChoiceItems(radioItems, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chosenNumber = which;
//                Toast.makeText(Typing_Test.this,String.valueOf(chosenNumber),Toast.LENGTH_SHORT).show();
            }
        });

        radioDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showFinishDialog();
            }
        });

        radioDialog.create().show();
    }

    private void showFinishDialog(){
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_layout,null);
        SubmitButton dialogBtnConfirm = (SubmitButton) dialogView.findViewById(R.id.submit);

        TitanicTextView tv = (TitanicTextView) dialogView.findViewById(R.id.titanic_tv);
        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
        new Titanic().start(tv);
        metadataIngester.setRunning(false);
        textIngester.setRunning(false);
        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(this);


        // here the data should be stored to the database.
        if (chosenNumber == -2){
            chosenNumber = 0;
        }

        double total_time = (float)(final_time-start_time)/1000.0;

        mooddb.execSQL(
                "insert into inputdata values ('"+uuid+"',"+total_time+",'"+final_text+"',"+chosenNumber+");"
        );

        TextView total_length_value = dialogView.findViewById(R.id.total_length_textview);
        TextView average_speed_value = dialogView.findViewById(R.id.average_speed_textview);
        TextView total_time_value = dialogView.findViewById(R.id.total_time_textview);

        DecimalFormat decimalFormat=new DecimalFormat(".00");

        String text_length = String.valueOf(final_text.length());
        total_length_value.setText(text_length);
        String total_time_string = decimalFormat.format(total_time)+" seconds";
        total_time_value.setText(total_time_string);
        String average_speed = String.valueOf((float)(final_text.length())/(float)(final_time-start_time)*60000);
        average_speed_value.setText(average_speed);


        layoutDialog.setView(dialogView);

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
                }, 1100);

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
            typingEvent.setId(uuid);
            queue.addActivity(typingEvent);

            float speed = (float)(s.toString().length()- cal_text.length())/(float)(time- cal_time)*60000;
            speedometer.speedTo(speed);
            System.out.printf("%f\n",speed);
            cal_text = s.toString();
            cal_time = time;
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            TextChangeQueue queue = QueueFactory.getAfterChangeInstance();
            long time = DateTime.now().getMillis();
            TypingEvent typingEvent = new TypingEvent();
            typingEvent.setTime(time);
            typingEvent.setText(s.toString());
            typingEvent.setId(uuid);
            queue.addActivity(typingEvent);

            final_text = s.toString();

//            submit.setEnabled(s.toString().length() >= 30);
        }

        @Override
        public void afterTextChanged(Editable s) {
            return;
        }
    };
}
