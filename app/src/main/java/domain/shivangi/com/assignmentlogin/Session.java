package domain.shivangi.com.assignmentlogin;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shiavngi Pandey on 01/06/2018.
 */

public class Session {
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private Context context;
    public Session(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("key", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setEmail(String email){
        editor.putString("email",email);
        editor.apply();
    }
    public void setNumber(String number){
        editor.putString("number",number);
        editor.apply();
    }
    public void setPassword(String password){
        editor.putString("password",password);
        editor.apply();
    }
    public void setEnabled(boolean isEnabled){
        editor.putBoolean("enabled",isEnabled);
        editor.apply();
    }
    public void setFingerAuth(boolean fingerAuth){
        editor.putBoolean("finger",fingerAuth);
        editor.apply();
    }
    public String getEmail(){
        return sharedPreferences.getString("email",null);
    }
    public  String getNumber(){
        return sharedPreferences.getString("number", null);
    }
    public String getPassword(){
        return sharedPreferences.getString("password",null);
    }
    public boolean isEnabled(){
        return sharedPreferences.getBoolean("enabled",false);
    }
    public boolean getFingerAuth(){
        return sharedPreferences.getBoolean("finger",true);
    }
}
