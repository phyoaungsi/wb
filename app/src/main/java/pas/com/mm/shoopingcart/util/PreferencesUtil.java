package pas.com.mm.shoopingcart.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import pas.com.mm.shoopingcart.common.Application;


public class PreferencesUtil {

    private final String TAG = getClass().getSimpleName();

    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_IS_EXTERNAL_LOGGED_IN = "is_external_logged_in"; // 20180320
    public static final String KEY_LOGGED_IN_USER_ID = "logged_in_user_id";
    public static final String KEY_LOGGED_IN_USER_FULL_NAME = "logged_in_user_fullname";
    public static final String KEY_LOGGED_IN_USER_CITY_CODE = "logged_in_user_city_code";
    public static final String KEY_LOGGED_IN_USER_AUTH_KEY = "logged_in_user_auth_key";
    public static final String KEY_GUEST_USER_CITY_CODE = "guest_user_city_code";

    private static volatile PreferencesUtil sInstance;
    private static final Object mLock = new Object();
    private final SharedPreferences mSharedPreferences;
    private Context mContext;


    public PreferencesUtil(Context context) {

        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
    }

    public static PreferencesUtil getInstance(Context context) {
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new PreferencesUtil(context);
                }
            }
        }
        return sInstance;
    }

    public static void setInstance(PreferencesUtil instance) {
        sInstance = instance;
    }

    public static void reset() {
        if (sInstance != null) {
            sInstance.resetData();
        }
        sInstance = null;
    }

    public void resetData() {
        mSharedPreferences.edit().clear().apply();
    }

    public boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);

    }

    public void putBoolean(String key, boolean value) {
        mSharedPreferences
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    public int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public void putInt(String key, int value) {
        mSharedPreferences
                .edit()
                .putInt(key, value)
                .apply();
    }

    public Long getLong(String key) {
        return mSharedPreferences.getLong(key, 0);
    }

    public void putLong(String key, Long value) {
        mSharedPreferences
                .edit()
                .putLong(key, value)
                .apply();
    }

    public void putString(String key, String value) {
        mSharedPreferences
                .edit()
                .putString(key, value)
                .apply();
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defValue) {
        return mSharedPreferences
                .getString(key, defValue);
    }

}
