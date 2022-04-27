package es.ucm.fdi.weekplanner2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;

public class ThemeUtils {

    /*
    Clase de utilidades para llevar a cabo los cambios de tema
     */

    public final static int AZUL = 0;
    public final static int AMARILLO = 1;
    public final static int VERDE = 2;
    public final static int ROJO = 3;
    public final static int MORADO = 4;

    private static int theme = R.style.Theme_TiendaDeRecompensas;

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
                theme = R.style.Theme2_TiendaDeRecompensas;
                activity.setTheme(theme);
                break;
            case AMARILLO:
                theme = R.style.Theme3_TiendaDeRecompensas;
                activity.setTheme(theme);
                break;
            case VERDE:
                theme = R.style.Theme4_TiendaDeRecompensas;
                activity.setTheme(theme);
                break;
            case ROJO:
                theme = R.style.Theme5_TiendaDeRecompensas;
                activity.setTheme(theme);
                break;
            default:
                theme = R.style.Theme_TiendaDeRecompensas;
                activity.setTheme(theme);
        }

    }

    public static int getThemeActual(){
        return theme;
    }

}