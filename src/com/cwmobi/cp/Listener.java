package com.cwmobi.cp;

public interface Listener {

    /**
     * 加载成功
     */
    void onLoadSuccess();
    
    /**
     * 广告加载失败
     */
    void onLoadFailed();
    
    
    /**
     * 展示广告
     */
    void onDisplay();
    
    /**
     * 广告点击
     */
    void onClickAd();
    
    /**
     * 广告关闭
     */
    void onClose();
    
}
