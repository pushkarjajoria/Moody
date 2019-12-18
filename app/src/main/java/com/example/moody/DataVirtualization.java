package com.example.moody;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moody.AAChartCoreLib.AAChartConfiger.AAChartModel;
import com.example.moody.AAChartCoreLib.AAChartConfiger.AAChartView;
import com.example.moody.AAChartCoreLib.AAChartConfiger.AASeriesElement;
import com.example.moody.AAChartCoreLib.AAChartEnum.AAChartType;
import com.example.moody.persistence.SQLService;

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

        SQLService service = new SQLService();
        SQLiteDatabase db = service.new MoodDbHelper(this.getApplicationContext()).getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from inputdata;",null);
        while (cursor.moveToNext()) {
            int personid = cursor.getInt(0);
            String name = cursor.getString(1);
            int age = cursor.getInt(2);
        }

        cursor.close();
        db.close();

        AAChartView MT_view = findViewById(R.id.MT);

        AAChartModel MT_model = new AAChartModel()
                .chartType(AAChartType.Area)
                .categories(new String[]{"10/2","10/3","10/4","10/5", "10/6","10/7","10/8","10/9","10/10"})
                .dataLabelsEnabled(true)
                .yAxisGridLineWidth(0f)
                .legendEnabled(false)
                .yAxisTitle("minutes")
                .title("MOOD VS TIME")
                .series(new AASeriesElement[]{
                        new AASeriesElement()
                                .data(new Object[]{7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6})
                });

        MT_view.aa_drawChartWithChartModel(MT_model);

        AAChartView TSM_view = findViewById(R.id.TSM);

        AAChartModel TSM_model = new AAChartModel()
                .chartType(AAChartType.Column)
                .categories(mood_type)
                .dataLabelsEnabled(true)
                .yAxisGridLineWidth(0f)
                .legendEnabled(false)
                .yAxisTitle("characters/minutes")
                .title("TOTAL SPEED VS MOOD")
                .series(new AASeriesElement[]{
                        new AASeriesElement()
                                .data(new Object[]{90,110,130,140,150})
                });

        TSM_view.aa_drawChartWithChartModel(TSM_model);

        AAChartView EM_view = findViewById(R.id.EM);

        AAChartModel EM_model = new AAChartModel()
                .chartType(AAChartType.Column)
                .categories(mood_type)
                .dataLabelsEnabled(true)
                .yAxisGridLineWidth(0f)
                .yAxisTitle("%")
                .title("ERROR RATE VS MOOD")
                .legendEnabled(false)
                .series(new AASeriesElement[]{
                        new AASeriesElement()
                                .data(new Object[]{4.2,16.2,33.8,44.7,88.9})
                });

        EM_view.aa_drawChartWithChartModel(EM_model);
    }
}
