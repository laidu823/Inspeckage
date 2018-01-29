package mobi.acpm.inspeckage.util;

import android.util.Base64;

import com.google.gson.Gson;

/**
 * Created by tczang on 18/1/17.
 */

public class BeanUtil {

    public static String object2String(Object o) {

        if (o instanceof char[]) {
            return AndroidStringUtil.androidNewString((char[]) o);
        }
        if (o instanceof byte[]) {
            return new String((byte[]) o);
        }
        if (o instanceof String) {
            return (String) o;
        }
        return new Gson().toJson(o);
    }

    public static String object2Base64(Object o) {

        if (o instanceof char[]) {
            return Base64.encodeToString(AndroidStringUtil.androidNewString((char[]) o).getBytes(),0);
        }
        if (o instanceof byte[]) {
            return Base64.encodeToString((byte[])o,0);
        }
        if (o instanceof String) {
            return (String) o;
        }
        return new Gson().toJson(o);
    }
}
