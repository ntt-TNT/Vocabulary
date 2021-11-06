package com.youdao.sdk.ydtranslatedemo;

/**
 * @(#)BrowserActivity.java, 2013年10月22日. Copyright 2013 Yodao, Inc. All
 * rights reserved. YODAO PROPRIETARY/CONFIDENTIAL.
 * Use is subject to license terms.
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.common.Constants;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;
import com.youdao.sdk.ydtranslatedemo.utils.SwListDialog;
import com.youdao.sdk.ydtranslatedemo.utils.SwListDialog.ItemListener;
import com.youdao.sdk.ydtranslatedemo.utils.ToastUtils;

/**
 * @author lukun
 */
public class TranslateActivity extends Activity {

    // 查询列表
    private ListView translateList;

    private TranslateAdapter adapter;

    private List<TranslateData> list = new ArrayList<TranslateData>();
    private List<Translate> trslist = new ArrayList<Translate>();


    private ProgressDialog progressDialog = null;

    private Handler waitHandler = new Handler();

    private EditText fanyiInputText;

    private InputMethodManager imm;

    private TextView fanyiBtn;

    TextView languageSelectFrom;

    TextView languageSelectTo;

    private Translator translator;
    Handler handler = new Handler();

    String[] lang = new String[]{"自动", "中文", "日文", "英文", "韩文", "法文", "俄文", "西班牙文", "葡萄牙文", "越南文",
            "繁体中文", "印地文", "德文", "阿拉伯文", "印尼文", "波兰文", "丹麦文", "挪威文", "意大利文", "匈牙利文", "印度文", "泰文", "马来文", "南非荷兰文", "波斯尼亚文", "保加利亚文",
            "粤语", "加泰隆文", "克罗地亚文", "捷克文", "丹麦文", "荷兰文", "爱沙尼亚文", "斐济文", "芬兰文", "希腊文", "海地克里奥尔文", "希伯来文", "白苗文", "匈牙利文", "斯瓦希里文",
            "克林贡文", "拉脱维亚文", "立陶宛文", "马来文", "马耳他文", "挪威文", "波斯文", "波兰文", "克雷塔罗奥托米文", "罗马尼亚文", "塞尔维亚文(西里尔文)", "塞尔维亚文(拉丁文)", 
            "斯洛伐克文", "斯洛文尼亚文", "瑞典文", "塔希提文", "泰文", "汤加文", "土耳其文", "乌克兰文", "乌尔都文", "威尔士文", "尤卡坦玛雅文", "阿尔巴尼亚文", "阿姆哈拉文", 
            "亚美尼亚文", "阿塞拜疆文", "孟加拉文", "巴斯克文", "白俄罗斯文", "宿务文", "科西嘉文", "世界文", "菲律宾文", "弗里西文", "加利西亚文", "格鲁吉亚文", "古吉拉特文", 
            "豪萨文", "夏威夷文", "冰岛文", "伊博文", "爱尔兰文", "爪哇文", "卡纳达文", "哈萨克文", "高棉文", "库尔德文", "柯尔克孜文", "老挝文", "拉丁文", "卢森堡文", "马其顿文", 
            "马尔加什文", "马拉雅拉姆文", "毛利文", "马拉地文", "蒙古文", "缅甸文", "尼泊尔文", "齐切瓦文", "普什图文", "旁遮普文", "萨摩亚文", "苏格兰盖尔文", "塞索托文", "修纳文", 
            "信德文", "僧伽罗文", "索马里文", "巽他文", "塔吉克文", "泰米尔文", "泰卢固文", "乌兹别克文", "南非科萨文", "意第绪文", "约鲁巴文", "南非祖鲁文"};

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
        setContentView(R.layout.translate_list);

        fanyiInputText = (EditText) findViewById(R.id.fanyiInputText);

        fanyiBtn = (TextView) findViewById(R.id.fanyiBtn);

        translateList = (ListView) findViewById(R.id.commentList);

        imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);

        View view = this.getLayoutInflater().inflate(R.layout.translate_head,
                translateList, false);
        translateList.addHeaderView(view);
        adapter = new TranslateAdapter(this, list, trslist);

        translateList.setAdapter(adapter);

        fanyiBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                query();
            }
        });

        languageSelectFrom = (TextView) findViewById(R.id.languageSelectFrom);
        languageSelectFrom.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectLanguage(languageSelectFrom);
            }
        });
        languageSelectTo = (TextView) findViewById(R.id.languageSelectTo);
        languageSelectTo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectLanguage(languageSelectTo);
            }
        });
//        query();
    }

    private void selectLanguage(final TextView languageSelect) {
        final String str[] = lang;
        List<String> items = new ArrayList<String>();
        for (String s : str) {
            items.add(s);
        }

        SwListDialog exitDialog = new SwListDialog(TranslateActivity.this,
                items);
        exitDialog.setItemListener(new ItemListener() {

            @Override
            public void click(int position, String title) {

                String language = languageSelect.getText().toString();
                languageSelect.setText(title);
                String from = languageSelectFrom.getText().toString();
                String to = languageSelectTo.getText().toString();

                String lan = "中文";
//                if (!from.contains(lan) && !to.contains(lan)
//                        && !to.contains("自动") && !from.contains("自动")) {
//                    ToastUtils.show("源文言或者目标文言其中之一必须为" + lan);
//                    languageSelect.setText(language);
//                    return;
//                }
            }
        });
        exitDialog.show();
    }

    private void query() {
        showLoadingView("正在查询");

        // 源文言或者目标文言其中之一必须为中文,目前只支持中文与其他几个文种的互译
        String from = languageSelectFrom.getText().toString();
        String to = languageSelectTo.getText().toString();
        final String input = fanyiInputText.getText().toString();

        Language langFrom = Language.getLanguageByName(from);
        // 若设置为自动，则查询自动识别源文言，自动识别不能保证完全正确，最好传源文言类型
        // Language langFrosm = LanguageUtils.getLangByName("自动");

        Language langTo = Language.getLanguageByName(to);

        TranslateParameters tps = new TranslateParameters.Builder()
                .source("youdao")
                .from(langFrom)
                .to(langTo)
                .strict("true")
                .build();// appkey可以省
        final long start = System.currentTimeMillis();

        translator = Translator.getInstance(tps);
        translator.lookup(input, "requestId", new TranslateListener() {
            @Override
            public void onResult(final Translate result, String input, String requestId) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TranslateData td = new TranslateData(
                                System.currentTimeMillis(), result);

                        long end = System.currentTimeMillis();
                        long time = end-start;
                        Log.i("1111111111111111","111111111查词时间"+time);

                        list.add(td);
                        trslist.add(result);
                        adapter.notifyDataSetChanged();
                        translateList.setSelection(list.size() - 1);
                        dismissLoadingView();
                        imm.hideSoftInputFromWindow(
                                fanyiInputText.getWindowToken(), 0);
                    }
                });
            }

            @Override
            public void onError(final TranslateErrorCode error, String requestId) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.show("查询错误:" + error.getCode());
                        dismissLoadingView();
                    }
                });
            }
            @Override
            public void onResult(List<Translate> results, List<String> inputs, List<TranslateErrorCode> errors, String requestId) {

            }
        });
    }

    private void showLoadingView(final String text) {
        waitHandler.post(new Runnable() {

            @Override
            public void run() {
                if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.setMessage(text);
                    progressDialog.show();
                }
            }
        });

    }

    private void dismissLoadingView() {
        waitHandler.post(new Runnable() {

            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });

    }

    public void postQuery(final Translate bean) {
        showLoadingView("正在翻译，请稍等");
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
