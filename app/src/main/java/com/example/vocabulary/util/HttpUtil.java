package com.example.wordbook.util;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    /**
     * 在新线程中发送网络请求
     *
     * @param address  网络地址 http://dict-co.iciba.com/api/dictionary.php?w=go&key=0CD3A4C079D2D23C683BBFF96300E924
     * @param listener HttpCallBackListener接口的实现类;
     *                 onFinish方法为访问成功后的回调方法;
     *                 onError为访问不成功时的回调方法
     */
    public static void sentHttpRequest(final String address,
                                       final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream = connection.getInputStream();
                    if (listener != null) {
                        listener.onFinish(inputStream);
                    }
                    System.out.println("HttpUtil：");
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onError();
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
