package mobi.acpm.inspeckage.util;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/**
 * Created by tczang on 18/1/17.
 */

public class AndroidStringUtil {

    private static final String STRING_FACTORY = "java.lang.StringFactory";

    public static String androidNewString(char[] chars){
        try {
            return (String) XposedHelpers.callStaticMethod(Class.forName(STRING_FACTORY),"newStringFromChars",chars);
        } catch (ClassNotFoundException e) {
            XposedBridge.log("cannot found StringFactory");
        }
        return null;
    }

    public static String androidNewString(byte[] bytes){
        try {
            return (String) XposedHelpers.callStaticMethod(Class.forName(STRING_FACTORY),"newStringFromBytes",bytes);
        } catch (ClassNotFoundException e) {
            XposedBridge.log("cannot found StringFactory");
        }
        return null;
    }
}
