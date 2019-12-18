package com.example.moody;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moody.AAChartCoreLib.AAChartConfiger.AAChartModel;
import com.example.moody.AAChartCoreLib.AAChartConfiger.AAChartView;
import com.example.moody.AAChartCoreLib.AAChartConfiger.AASeriesElement;
import com.example.moody.AAChartCoreLib.AAChartEnum.AAChartType;
import com.example.moody.AAChartCoreLib.AAOptionsModel.AAPie;
import com.example.moody.persistence.SQLService;

import java.math.BigDecimal;
import java.util.ArrayList;

public class DataVirtualization extends AppCompatActivity {
    private final String[] mood_type = new String[]{

            new String(Character.toChars(Integer.parseInt("1F604", 16))),
            new String(Character.toChars(Integer.parseInt("1F603", 16))),
            new String(Character.toChars(Integer.parseInt("1F610", 16))),
            new String(Character.toChars(Integer.parseInt("1F643", 16))),
            new String(Character.toChars(Integer.parseInt("1F62D", 16))) };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_virtualization);

        ArrayList<String> mood_vs_speed = new ArrayList<>();
        ArrayList<Float> speed = new ArrayList<>();


        Object[][] mood_id = new Object[5][2];
        for (int i = 0; i < mood_type.length; i++) {
            mood_id[i][0] = mood_type[i];
            mood_id[i][1] = 0.0;
        }

        ArrayList<String> mood_vs_error = new ArrayList<>();
        ArrayList<Float> error_rate = new ArrayList<>();


        SQLService service = new SQLService();
        SQLiteDatabase db = service.new MoodDbHelper(this.getApplicationContext()).getReadableDatabase();
        Cursor cursor = db.rawQuery("select round(avg(length(input_text)/total_time),3),mood from inputdata group by mood;",null);
        while (cursor.moveToNext()) {
            speed.add(cursor.getFloat(0));
            mood_vs_speed.add(mood_type[cursor.getInt(1)]);
        }

        cursor = db.rawQuery("select mood,round(sum(1.0/(select count(*) from inputdata)),2) from inputdata group by mood order by mood;",null);
        while (cursor.moveToNext()) {
            int mood = cursor.getInt(0);
            mood_id[mood][0] = mood_type[mood];
            mood_id[mood][1] = cursor.getFloat(1);
        }

        cursor = db.rawQuery("select round(avg(error_count*1.0/char_count)*100,2),mood from inputdata join TypingData TD on inputdata.batch_id = TD.batch_id group by mood;",null);
        while (cursor.moveToNext()) {
            error_rate.add(cursor.getFloat(0));
            mood_vs_error.add(mood_type[cursor.getInt(1)]);
        }

        cursor.close();
        db.close();


        AAChartView MT_view = findViewById(R.id.MT);

//        AAChartModel MT_model = new AAChartModel()
//                .chartType(AAChartType.Area)
//                .categories(id.toArray(new String[0]))
//                .dataLabelsEnabled(true)
//                .yAxisGridLineWidth(0f)
//                .legendEnabled(false)
//                .yAxisTitle("seconds")
//                .title("MOOD VS TIME")
//                .series(new AASeriesElement[]{
//                        new AASeriesElement()
//                                .data(mood_vs_id.toArray(new String[0]))
//                });

        AAChartModel MT_model = new AAChartModel()
                .chartType(AAChartType.Pie)
                .title("MOOD VS TIME")
                .dataLabelsEnabled(true)
                .yAxisTitle("%")
                .series(new AAPie[] {
                        new AAPie()
                                .innerSize("20%")
                                .data(mood_id)}
                );


        MT_view.aa_drawChartWithChartModel(MT_model);

        AAChartView TSM_view = findViewById(R.id.TSM);

        AAChartModel TSM_model = new AAChartModel()
                .chartType(AAChartType.Column)
                .categories(mood_vs_speed.toArray(new String[0]))
                .dataLabelsEnabled(true)
                .yAxisGridLineWidth(0f)
                .legendEnabled(false)
                .yAxisTitle("characters/secs")
                .title("AVERAGE SPEED VS MOOD")
                .series(new AASeriesElement[]{
                        new AASeriesElement()
                                .data(speed.toArray(new Float[0]))
                });

        TSM_view.aa_drawChartWithChartModel(TSM_model);

        AAChartView EM_view = findViewById(R.id.EM);

        AAChartModel EM_model = new AAChartModel()
                .chartType(AAChartType.Column)
                .categories(mood_vs_error.toArray(new String[0]))
                .dataLabelsEnabled(true)
                .yAxisGridLineWidth(0f)
                .yAxisTitle("%")
                .title("ERROR RATE VS MOOD")
                .legendEnabled(false)
                .series(new AASeriesElement[]{
                        new AASeriesElement()
                                .data(error_rate.toArray(new Float[0]))
                });

        EM_view.aa_drawChartWithChartModel(EM_model);
    }
}
