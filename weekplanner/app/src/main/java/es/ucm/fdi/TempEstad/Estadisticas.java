package es.ucm.fdi.TempEstad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

import es.ucm.fdi.Calendario.DailyCalendarActivity;
import es.ucm.fdi.Calendario.MonthCalendarActivity;
import es.ucm.fdi.MainActivity;
import es.ucm.fdi.R;

import es.ucm.fdi.TempEstad.DbHelper;
import es.ucm.fdi.TempEstad.Utilidades;
import es.ucm.fdi.tienda.TiendaMain;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class Estadisticas extends AppCompatActivity {
    Button dia;
    Button semana;
    Button mes;
    Button tienda;
    Button temp;
    Button estad;

    private PieChartView pieChart;

    private ArrayList<String> asignaturas;
    private ArrayList<Integer> tiempos;

    private TextView tv_asignatura;
    private TextView tv_tiempo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        asignaturas = new ArrayList<String>();
        tiempos = new ArrayList<Integer>();

        tv_asignatura = findViewById(R.id.tv_asignatura);
        tv_tiempo = findViewById(R.id.tv_tiempo);

        pieChart = (PieChartView) findViewById(R.id.graphic_pie_chart);
        rellenarAsignaturas();
        rellenarPieChart();

        tienda = (Button) findViewById(R.id.Tienda);
        tienda.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(Estadisticas.this, TiendaMain.class));
            }
        });
        dia = (Button) findViewById(R.id.Dia);
        dia.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(Estadisticas.this, DailyCalendarActivity.class));
            }
        });
        semana = (Button) findViewById(R.id.Semana);
        semana.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(Estadisticas.this, MainActivity.class));
            }
        });
        mes = (Button) findViewById(R.id.Mes);
        mes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(Estadisticas.this, MonthCalendarActivity.class));
            }
        });
        temp = (Button) findViewById(R.id.Temporizador);
        temp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(Estadisticas.this, TempActivity.class));
            }
        });
        estad = (Button) findViewById(R.id.Estadisticas);
        estad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(Estadisticas.this, Estadisticas.class));
            }
        });
    }

    @SuppressLint("Range")
    private void rellenarAsignaturas() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db == null) {
            Toast.makeText(this, "Error al conectar con la BBDD", Toast.LENGTH_SHORT).show();
        }
        else {
            Cursor c = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_ASIGNATURAS, null);
            if (!c.moveToFirst()) Toast.makeText(this, "No se han encontrado asignaturas", Toast.LENGTH_SHORT).show();
            do {
                asignaturas.add(c.getString(c.getColumnIndex(Utilidades.ASIGNATURA_NOMBRE)));
                tiempos.add(c.getInt(c.getColumnIndex(Utilidades.ASIGNATURA_TIEMPO)));
            } while (c.moveToNext());
        }
    }

    private void rellenarPieChart() {
        //Lista de valores
        ArrayList<SliceValue> values = new ArrayList<SliceValue>();
        //Lista de colores
        final ArrayList<Integer> colorData = new ArrayList<Integer>();
        //Información de la etiqueta
        final ArrayList<String> titleData = new ArrayList<String>();

        //10 colores
        colorData.add(Color.parseColor("#85B74F"));
        colorData.add(Color.parseColor("#009BDB"));
        colorData.add(Color.parseColor("#FF0000"));
        colorData.add(Color.parseColor("#9569F8"));
        colorData.add(Color.parseColor("#F87C67"));
        colorData.add(Color.parseColor("#F1DA3D"));
        colorData.add(Color.parseColor("#87EA39"));
        colorData.add(Color.parseColor("#48AEFA"));
        colorData.add(Color.parseColor("#4E5052"));
        colorData.add(Color.parseColor("#D36458"));

        //10 etiquetas
        for (int i = 0; i < asignaturas.size(); i++) {
            titleData.add(asignaturas.get(i));
            SliceValue sliceValue = new SliceValue(tiempos.get(i), colorData.get(i));
            values.add(sliceValue);
        }

        final PieChartData pieChartData = new PieChartData();
        pieChartData.setHasLabels(true);
        pieChartData.setHasLabelsOnlyForSelected(false);
        pieChartData.setHasLabelsOutside(false);
        pieChartData.setHasCenterCircle(false);
        pieChartData.setValues(values);

        pieChart.setPieChartData(pieChartData);
        pieChart.setValueSelectionEnabled(true);
        pieChart.setAlpha(0.9f);
        pieChart.setCircleFillRatio(0.9f);

        pieChart.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, SliceValue sliceValue) {
                tv_asignatura.setText(titleData.get(i));
                tv_tiempo.setText("La has estudiado " + ((int) sliceValue.getValue()) + " segundos");
            }

            @Override
            public void onValueDeselected() {
                tv_asignatura.setText("");
                tv_tiempo.setText("");
            }
        });

    }

}
