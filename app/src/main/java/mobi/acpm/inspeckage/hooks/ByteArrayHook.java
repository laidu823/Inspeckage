package mobi.acpm.inspeckage.hooks;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * hook byte[]
 * Created by tczang on 18/1/24.
 */
public class ByteArrayHook extends XC_MethodHook {

    public static final String TAG = "Inspeckage_byte[]:";
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
                }
            }
        };
        findAndHookConstructor(byte[].class,loadPackageParam.classLoader,byte[].class,xc_methodHook);

    }
}
