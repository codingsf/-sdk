package com.cwmobi.cp;
/**
 * @author fyq
 * @date 2016-5-17 下午2:58:11
 * ndk类接口
 */
public class a {

    
    static {
        System.loadLibrary("cwcp");
    }
    
    /**
     * 通过序号取出对应字符串
     * @param index
     * @return
     */
    native public static String d(int index);
    
    /**
     * 加载dex
     * @param path 路径
     */
    public native static Object b(String dexpath, String optpath, String loadpath, Object loader);
    
    /**
     * 调用dex api方法
     * @param index
     * @param params
     */
    public native static void c(int index, Object[] params);
    
    
    
}
