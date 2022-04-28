package es.ucm.fdi.tienda;

import android.app.Activity;
import android.content.Intent;

import es.ucm.fdi.R;

public class ThemeUtils {

    /*
    Clase de utilidades para llevar a cabo los cambios de tema
     */

    public final static int AZUL = 0;
    public final static int AMARILLO = 1;
    public final static int VERDE = 2;
    public final static int ROJO = 3;
    public final static int MORADO = 4;

    private static int cTheme = MORADO;

    // Función para cambiar el tema de la actividad principal
    public static void changeToTheme(Activity activity, int theme) {
        cTheme = theme;
        activity.finish(); // Termina el main activity anterior y abre otro
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity){
        switch (cTheme)
        {
            case AZUL:
                activity.setTheme(R.style.Theme2_TiendaDeRecompensas);
                break;
            case AMARILLO:
                activity.setTheme(R.style.Theme3_TiendaDeRecompensas);
                break;
            case VERDE:
                activity.setTheme(R.style.Theme4_TiendaDeRecompensas);
                break;
            case ROJO:
                activity.setTheme(R.style.Theme5_TiendaDeRecompensas);
                break;
            default:
                activity.setTheme(R.style.Theme_TiendaDeRecompensas);
        }

    }

}