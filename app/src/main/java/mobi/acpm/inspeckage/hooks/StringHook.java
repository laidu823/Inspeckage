package mobi.acpm.inspeckage.hooks;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * hook String
 * Created by tczang on 18/1/24.
 */

public class StringHook extends XC_MethodHook {

    public static final String TAG = "Inspeckage_String:";
    private static StringBuffer sb;

    public static void initAllHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {

        XC_MethodHook xc_methodHook = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object result = param.getResult();
                if (result != null && result instanceof String) {
                    String str = (String) result;
                    sb = new StringBuffer();
                    sb.append("StringFactory.newStringFromXXXX(" +str+")");
                    XposedBridge.log(TAG + sb.toString());
                    sb = new StringBuffer();
                }
            }
        };

        XposedBridge.hookAllConstructors(String.class,xc_methodHook);
        XposedBridge.hookAllConstructors(StringBuilder.class,xc_methodHook);
        XposedBridge.hookAllConstructors(StringBuffer.class,xc_methodHook);

//        Class<?> clazz = null;
//        try {
//            clazz = loadPackageParam.classLoader.loadClass("java.lang.StringFactory");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        findAndHookMethod(clazz, "newStringFromBytes", xc_methodHook);
//        findAndHookMethod(clazz,  "newStringFromChars", xc_methodHook);
//        findAndHookMethod(clazz,  "newStringFromStringBuffer", xc_methodHook);
//        findAndHookMethod(clazz,  "newStringFromCodePoints", xc_methodHook);
//        findAndHookMethod(clazz,  "newStringFromStringBuilder", xc_methodHook);

//        try {
//
//        } catch (Exception e) {
//            XposedBridge.log("can't found StringFactory");
//        }

    }
}
