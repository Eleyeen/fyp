package com.hostelfinder.hostellocator.Databases;

import android.content.Context;
import android.content.SharedPreferences;

import com.hostelfinder.hostellocator.Common.LoginSignUp.User_Startup_Screen;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences UserSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_FULLNAME = "FullName";
    public static final String KEY_USERNAME = "UserName";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_PASSWORD = "Password";

    public SessionManager(Context _context) {
        context = _context;
        UserSession = context.getSharedPreferences("UserLoginSesison", Context.MODE_PRIVATE);
        editor = UserSession.edit();
    }

    public void CreateLoginSession(String FullName, String Username, String Email, String Password) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_FULLNAME, FullName);
        editor.putString(KEY_USERNAME, Username);
        editor.putString(KEY_EMAIL, Email);
        editor.putString(KEY_PASSWORD, Password);

        editor.commit();
    }

    public HashMap<String, String> getUserDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();
        userData.put(KEY_FULLNAME, UserSession.getString(KEY_FULLNAME, null));
        userData.put(KEY_USERNAME, UserSession.getString(KEY_USERNAME, null));
        userData.put(KEY_EMAIL, UserSession.getString(KEY_EMAIL, null));
        userData.put(KEY_PASSWORD, UserSession.getString(KEY_PASSWORD, null));

        return userData;
    }

    public boolean checkLogin() {
        if (UserSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else return false;
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }
}