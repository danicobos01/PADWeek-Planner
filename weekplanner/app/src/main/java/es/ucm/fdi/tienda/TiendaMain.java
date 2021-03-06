package es.ucm.fdi.tienda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.ucm.fdi.Calendario.DailyCalendarActivity;
import es.ucm.fdi.Calendario.MonthCalendarActivity;
import es.ucm.fdi.MainActivity;
import es.ucm.fdi.R;
import es.ucm.fdi.TempEstad.DbHelper;
import es.ucm.fdi.TempEstad.Estadisticas;
import es.ucm.fdi.TempEstad.TempActivity;
import es.ucm.fdi.TempEstad.Utilidades;

public class TiendaMain extends AppCompatActivity /* implements View.OnClickListener*/ {

    private RecyclerView rv;
    private ArrayList<RecompensaInfo> recArr;
    private RecompensasAdapter adapter;
    private DatabaseOrg dbAdapter;
    Button dia;
    Button semana;
    Button mes;
    Button tienda;
    Button temp;
    Button estad;
    static TextView tv_monedas;

    private int monedas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_tienda);

        dbAdapter = new DatabaseOrg(getApplicationContext());
        recArr = new ArrayList<RecompensaInfo>();
        recArr = dbAdapter.leerRecompensasFromDB();
        tv_monedas = findViewById(R.id.tv_monedas);
        getMonedasFromBD();
        adapter = new RecompensasAdapter(recArr, this, dbAdapter, monedas);


        rv = findViewById(R.id.recycler);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();


        tienda = (Button) findViewById(R.id.Tienda);
        tienda.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(TiendaMain.this, TiendaMain.class));
            }
        });
        dia = (Button) findViewById(R.id.Dia);
        dia.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(TiendaMain.this, DailyCalendarActivity.class));
            }
        });
        semana = (Button) findViewById(R.id.Semana);
        semana.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(TiendaMain.this, MainActivity.class));
            }
        });
        mes = (Button) findViewById(R.id.Mes);
        mes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(TiendaMain.this, MonthCalendarActivity.class));
            }
        });
        temp = (Button) findViewById(R.id.Temporizador);
        temp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(TiendaMain.this, TempActivity.class));
            }
        });
        estad = (Button) findViewById(R.id.Estadisticas);
        estad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(TiendaMain.this, Estadisticas.class));
            }
        });
    }



    private void getMonedasFromBD() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db == null) {
            Toast.makeText(this, "Error al conectar con la BBDD", Toast.LENGTH_SHORT).show();
        }
        else {
            String query = "SELECT * FROM " + Utilidades.TABLA_MONEDAS;
            Cursor c = db.rawQuery(query, null);
            if (!c.moveToFirst()) {
                monedas = 0;
                tv_monedas.setText("0 PC");
            }
            else {
                monedas = c.getInt(0);
                tv_monedas.setText("" + monedas + " PC");
            }
            db.close();
        }
    }


    // Funcion que se llama cuando se pulsa el bot??n de informaci??n en la tienda de recompensas
    public void onButtonShowPopupWindowClick(View view) {

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        // crea la popup window
        int width = 900;
        int height = 900;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Muestra la popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        // Intento de hacer el popup flotable


        // Oscurecer fondo mientras la popup window est?? activa
        View container = (View) popupWindow.getContentView().getParent();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.7f;
        wm.updateViewLayout(container, p);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }

        // Elimina la popup window una vez se toca la pantalla
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public static void cambiarMonedasToBD(int monedas, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            Toast.makeText(context, "Error al conectar con la BBDD", Toast.LENGTH_SHORT).show();
        }
        else {
            ContentValues cv = new ContentValues();
            cv.put(Utilidades.MONEDAS_CANTIDAD, monedas);
            int rows = db.update(Utilidades.TABLA_MONEDAS, cv, null, null);
            if (rows < 1) {
                Toast.makeText(context, "No se han cambiado las monedas en la BD", Toast.LENGTH_SHORT).show();
            }
        }

    }

}