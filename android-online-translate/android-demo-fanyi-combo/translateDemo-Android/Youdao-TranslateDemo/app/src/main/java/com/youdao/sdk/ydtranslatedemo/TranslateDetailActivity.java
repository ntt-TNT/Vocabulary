package com.youdao.sdk.ydtranslatedemo;

/**
 * @(#)BrowserActivity.java, 2013�?10�?22�?. Copyright 2013 Yodao, Inc. All
 * rights reserved. YODAO PROPRIETARY/CONFIDENTIAL.
 * Use is subject to license terms.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.youdao.sdk.common.Constants;
import com.youdao.sdk.common.YouDaoLog;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslatedemo.utils.ToastUtils;

import java.util.List;

/**
 * @author lukun
 */
public class TranslateDetailActivity extends Activity {

    public static void open(Activity activity, TranslateData news, Translate trs) {
        Intent in = new Intent(activity, TranslateDetailActivity.class);
        in.putExtra("news", news);
        in.putExtra("trs", trs);
        activity.startActivity(in);
    }

    TranslateData translateData;
    Translate trs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(Activity.RESULT_OK);
        try {
            getWindow().requestFeature(Window.FEATURE_PROGRESS);
            getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
                    Window.PROGRESS_VISIBILITY_ON);
        } catch (Exception e) {
        }
        setContentView(R.layout.translate_detail);

        translateData = (TranslateData) this.getIntent().getSerializableExtra(
                "news");
        trs = (Translate) this.getIntent().getSerializableExtra(
                "trs");
        TextView input = (TextView) findViewById(R.id.input);
        Button start = (Button) findViewById(R.id.startBtn);
        if (TextUtils.isEmpty(trs.getSpeakUrl())) {
            start.setVisibility(View.GONE);
        }
        start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                playVoice(trs.getSpeakUrl());
            }
        });

        Button usstart = (Button) findViewById(R.id.usstartBtn);
        if (TextUtils.isEmpty(trs.getUSSpeakUrl())) {
            usstart.setVisibility(View.GONE);
        }
        usstart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                playVoice(trs.getUSSpeakUrl());
            }
        });

        Button ukstart = (Button) findViewById(R.id.ukstartBtn);
        if (TextUtils.isEmpty(trs.getUKSpeakUrl())) {
            ukstart.setVisibility(View.GONE);
        }
        ukstart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                playVoice(trs.getUKSpeakUrl());
            }
        });

        TextView translation = (TextView) findViewById(R.id.translation);
        Button start1 = (Button) findViewById(R.id.startBtn1);
        if (TextUtils.isEmpty(trs.getResultSpeakUrl())) {
            start1.setVisibility(View.GONE);
        }
        start1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                playVoice(trs.getResultSpeakUrl());
            }
        });
        TextView spell = (TextView) findViewById(R.id.spell);
        TextView ukSpell = (TextView) findViewById(R.id.ukSpell);
        TextView usSpell = (TextView) findViewById(R.id.usSpell);
        TextView means = (TextView) findViewById(R.id.means);
        TextView webmeans = (TextView) findViewById(R.id.webmeans);
        TextView moreBtn = (TextView) findViewById(R.id.moreBtn);
        TextView detailBtn = findViewById(R.id.detailBtn);
        TextView detailText = findViewById(R.id.detailText);
        TextView wfsText = findViewById(R.id.wfs);
        if (!TextUtils.isEmpty(translateData.getTranslate().getQuery())) {
            input.setText("输入：" + translateData.getQuery());
        }

        if (!TextUtils.isEmpty(translateData.translates())) {
            translation.setText("翻译结果：\n" + translateData.translates());
        }

        if (!TextUtils.isEmpty(translateData.getTranslate().getPhonetic())) {
            spell.setText("发音：" + translateData.getTranslate().getPhonetic());
        }

        if (!TextUtils.isEmpty(translateData.getTranslate().getUkPhonetic())) {
            ukSpell.setText("英式发音："
                    + translateData.getTranslate().getUkPhonetic());
        }

        if (!TextUtils.isEmpty(translateData.getTranslate().getUsPhonetic())) {
            usSpell.setText("美式发音："
                    + translateData.getTranslate().getUsPhonetic());
        }

        if (!TextUtils.isEmpty(translateData.means())) {
            means.setText("查词结果：\n" + translateData.means());
        }

        if (!TextUtils.isEmpty(translateData.webMeans())) {
            webmeans.setText("网络释义：\n" + translateData.webMeans());
        }
        List<Translate.WF> wfs = translateData.getTranslate().getWfs();
        String wfsString = "";
        if (wfs != null && wfs.size() > 0) {
            for (int i = 0; i < wfs.size(); i++) {
                wfsString += wfs.get(i).getName() + ":" + wfs.get(i).getValue() + "\n";
            }
            wfsText.setVisibility(View.VISIBLE);
            wfsText.setText(wfsString);
        } else {
            wfsText.setVisibility(View.GONE);
        }
        moreBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                trs.openMore(TranslateDetailActivity.this);
//                //注意，若用户没安装有道词典，开发者可自己实现deeplink的跳转
//                if(!trs.openDict(TranslateDetailActivity.this)){
//                	 //获取deeplink链接
//                    String deeplinkUrl = trs.getDictWebUrl();
//                    //处理deeplink链接，可通过自定义浏览器打开
//                TranslateForwardHelper.toBrowser(TranslateDetailActivity.this, deeplinkUrl);
//                }
            }
        });
        detailText.setText(trs.getJson());

        detailBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.detailText).getVisibility() == View.GONE) {
                    findViewById(R.id.detailText).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.detailText).setVisibility(View.GONE);
                }
            }
        });

    }

    public synchronized void playVoice(String speakUrl) {
        YouDaoLog.e(AudioMgr.PLAY_LOG + "TranslateDetailActivity click to playVoice speakUrl = " + speakUrl);
        if (!TextUtils.isEmpty(speakUrl) && speakUrl.startsWith("http")) {
            ToastUtils.show("正在发音");
            AudioMgr.startPlayVoice(speakUrl, new AudioMgr.SuccessListener() {
                @Override
                public void success() {
                    YouDaoLog.e(AudioMgr.PLAY_LOG + "TranslateDetailActivity playVoice success");
                }

                @Override
                public void playover() {
                    YouDaoLog.e(AudioMgr.PLAY_LOG + "TranslateDetailActivity playover");
                }
            });
        }
    }

    public void loginBack(View view) {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void finish() {
        super.finish();
    }

}
