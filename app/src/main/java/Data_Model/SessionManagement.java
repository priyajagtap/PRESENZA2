package Data_Model;

/**
 * Created by infogird31 on 05/10/2016.
 */

        import java.util.HashMap;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.SharedPreferences.Editor;

        import com.infogird.www.presenza.MainActivity;

public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Presenza_shared_preferences";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User id (make variable public to access from outside)
    public static final String KEY_UID = "uid";

    // Name address (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Dept address (make variable public to access from outside)
    public static final String KEY_DEPT = "dept";

    // Post address (make variable public to access from outside)
    public static final String KEY_POST = "post";

    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    /**
     * Create login session
     * */
    public void createLoginSession(String uid, String name, String dept, String post){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_UID, uid);

        // Storing email in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_DEPT, dept);

        // Storing email in pref
        editor.putString(KEY_POST, post);

        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_UID, pref.getString(KEY_UID, null));

        // user email id
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_DEPT, pref.getString(KEY_DEPT, null));

        // user email id
        user.put(KEY_POST, pref.getString(KEY_POST, null));

        // return user
        return user;
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}