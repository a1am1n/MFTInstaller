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


    public static User getUserData(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "container_prefs", 0);
        User user = complexPreferences.getObject("user", User.class);
        return user;
    }
    public static void setUserData(User user, Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "container_prefs", 0);
        complexPreferences.putObject("user", user);
        complexPreferences.commit();
    }
}
