package mobi.acpm.inspeckage.hooks;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

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

        final Class<?> clazz = XposedHelpers.findClass("java.lang.StringFactory",loadPackageParam.classLoader);

        XposedHelpers.findAndHookMethod(clazz,"newEmptyString",xc_methodHook);
        XposedHelpers.findAndHookMethod(clazz,"newStringFromBytes",byte[].class,xc_methodHook);
        XposedHelpers.findAndHookMethod(clazz,"newStringFromBytes",byte[].class,int.class,xc_methodHook);
        XposedHelpers.findAndHookMethod(clazz,"newStringFromBytes",byte[].class,int.class,int.class,xc_methodHook);
        XposedHelpers.findAndHookMethod(clazz,"newStringFromBytes",byte[].class,int.class,int.class,java.lang.String.class,xc_methodHook);
        XposedHelpers.findAndHookMethod(clazz,"newStringFromBytes",byte[].class,int.class,int.class,java.nio.charset.Charset.class,xc_methodHook);
        XposedHelpers.findAndHookMethod(clazz,"newStringFromBytes",byte[].class,java.lang.String.class,xc_methodHook);
        XposedHelpers.findAndHookMethod(clazz,"newStringFromBytes",byte[].class,java.nio.charset.Charset.class,xc_methodHook);
        XposedHelpers.findAndHookMethod(clazz,"newStringFromChars",char[].class,xc_methodHook);
        XposedHelpers.findAndHookMethod(clazz,"newStringFromChars",char[].class,int.class,int.class,xc_methodHook);
        XposedHelpers.findAndHookMethod(clazz,"newStringFromCodePoint",int[].class,int.class,int.class,xc_methodHook);
        XposedHelpers.findAndHookMethod(clazz,"newStringFromStringBuffer",java.lang.StringBuffer.class,xc_methodHook);
        XposedHelpers.findAndHookMethod(clazz,"newStringFromStringBuilder",java.lang.StringBuilder.class,xc_methodHook);

        // native method
//        XposedHelpers.findAndHookMethod(clazz,"newStringFromChars",int.class,int.class,char[].class,xc_methodHook);
//        XposedHelpers.findAndHookMethod(clazz,"newStringFromBytes",byte[].class,int.class,int.class,int.class,xc_methodHook);
//        XposedHelpers.findAndHookMethod(clazz,"newStringFromString",java.lang.String.class,xc_methodHook);


    }
}
