package helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class PrefUtils {

    public static void setLogin(Context ctx,boolean status){

        SharedPreferences  preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("user-login", status);
        editor.commit();
    }

    public static boolean  getLogin(Context ctx){
        SharedPreferences  preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean status =preferences.getBoolean("user-login", false);
        return  status;
    }
}
