package com.example.wordbook.util;

import java.io.InputStream;

public interface HttpCallbackListener {
    //访问完成
    public void onFinish(InputStream inputStream);
    //访问出错
    void onError();
}