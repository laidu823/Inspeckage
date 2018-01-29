package mobi.acpm.inspeckage.util;

import com.google.gson.Gson;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by tczang on 18/1/17.
 */

public class MethodHookUtil {

    public static void hookAllConstructor(String className, XC_LoadPackage.LoadPackageParam lpparam) {

        try {
            for (Constructor<?> constructor : lpparam.classLoader.loadClass(className).getConstructors()) {
                if (Modifier.isPublic(constructor.getModifiers())) {
                    List<Object> parameterTypesAndCallbackList = new ArrayList<>();
                    parameterTypesAndCallbackList.addAll(Arrays.asList(constructor.getParameterTypes()));
                    parameterTypesAndCallbackList.add(methodSignatureHook());
                    try {
                        XposedHelpers.findAndHookConstructor(Class.forName(className), parameterTypesAndCallbackList.toArray());
                    } catch (Exception e) {
                        XposedBridge.log(String.format("method not found %s (%s) ", className, new Gson().toJson(constructor.getParameterTypes())));
                    }
                }

            }
        } catch (ClassNotFoundException e) {
            XposedBridge.log(String.format("%s not found", className));
        }
    }

    public static void hookAllMethods(String className, XC_LoadPackage.LoadPackageParam lpparam) {
        hookAllMethods(className,lpparam,methodSignatureHook());
    }

    public static void hookAllMethods(String className, XC_LoadPackage.LoadPackageParam lpparam, XC_MethodHook xcMethodHook) {

        try {
            final Class<?> mClass = XposedHelpers.findClass(className, lpparam.classLoader);
            for (Method method : mClass.getDeclaredMethods()) {
                // only hook public not native method
                if (Modifier.isPublic(method.getModifiers()) && !Modifier.isNative(method.getModifiers())) {
                    List<Object> parameterTypesAndCallbackList = new ArrayList<>();
                    parameterTypesAndCallbackList.addAll(Arrays.asList(method.getParameterTypes()));
                    parameterTypesAndCallbackList.add(xcMethodHook);

                    try {
                        XposedHelpers.findAndHookMethod(mClass, method.getName(), parameterTypesAndCallbackList.toArray());
                        XposedBridge.log(String.format("add hook method %s (%s) ", className, new Gson().toJson(method.getParameterTypes())));
                    } catch (Exception e) {
                        XposedBridge.log(String.format("method not found %s (%s) ", className, new Gson().toJson(method.getParameterTypes())));
                    }
                }
            }
        } catch (Exception e) {
            XposedBridge.log(String.format("%s not found", className));
        }
    }

    public static XC_MethodHook methodSignatureHook() {
        return new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String signFormat = "%s@%s -> %s (%s) == %s";

                List<String> paramRawList = new ArrayList<>();
                List<String> paramBase64List = new ArrayList<>();
                List<String> paramStringList = new ArrayList<>();

                String className = param.method.getDeclaringClass().getName();
                String methodName = param.method.getName();

                if (param.args != null) {
                    for (Object o : param.args) {
                        paramRawList.add(o.getClass().getCanonicalName() + ":" + new Gson().toJson(o));
                        paramBase64List.add(o.getClass().getCanonicalName() + ":" + BeanUtil.object2Base64(o));
                        paramStringList.add(o.getClass().getCanonicalName() + ":" + BeanUtil.object2String(o));
                    }
                }

                String resultRaw = null;
                String resultBase64 = null;
                String resultJSONString = null;
                if (!param.hasThrowable() && param.getResult() != null) {
                    resultRaw = param.getResult().getClass().getCanonicalName() + " : " + new Gson().toJson(resultRaw);
                    resultBase64 = param.getResult().getClass().getCanonicalName() + " : " + BeanUtil.object2Base64(param.getResult());
                    resultJSONString = param.getResult().getClass().getCanonicalName() + " : " + BeanUtil.object2String(param.getResult());
                }

                XposedBridge.log("method hook raw : " + String.format(signFormat, className, param.thisObject, methodName, new Gson().toJson(paramRawList), resultRaw) + "\n" +
                        "method hook base64 : " + String.format(signFormat, className, param.thisObject, methodName, new Gson().toJson(paramBase64List), resultBase64) + "\n" +
                        "method hook string : " + String.format(signFormat, className, param.thisObject, methodName, new Gson().toJson(paramStringList), resultJSONString) + "\n\n"
                );
            }
        };

    }
}
