package com.example.vocabulary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vocabulary.db.WordsDBHelper;
import com.example.vocabulary.util.HttpUtil;
import com.example.vocabulary.util.JinshanParseUtil;

import java.io.IOException;

import okhttp3.Response;

public class WordActivity extends AppCompatActivity{

    private static final String TAG = "WordActivity";

    private String word;
    private String strMeaning;
    private String strSample;
    WordsDBHelper mDbHelper;
    private ImageView add_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        Intent intent = getIntent();

        if (intent!=null){
            word = intent.getStringExtra("wordName");
            Log.d(TAG, word);
            TextView tv_query = (TextView) findViewById(R.id.tv_query);

            tv_query.setText(word);
            query(getWindow().getDecorView());
        }

        mDbHelper = new WordsDBHelper(this);

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public static void actionStart(Context context, String word) {
        Intent intent = new Intent(context, WordActivity.class);
        intent.putExtra("wordName", word);
        context.startActivity(intent);
    }

    public void query(View view){
        try {
            //金山查词网址，默认xml，使用中
            final String urlxml = "https://dict-co.iciba.com/api/dictionary.php?w=" + word + "&key=9AA9FA4923AC16CED1583C26CF284C3F";
            //金山查词网址，可选json，&type=json  ，因为缺少例句，未使用
            String url = "https://dict-co.iciba.com/api/dictionary.php?w=" + word + "&type=json&key=9AA9FA4923AC16CED1583C26CF284C3F";

            if (JinshanParseUtil.isEnglish(word)) {
                HttpUtil.sendOkHttpRequest(urlxml, new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
//                        Toast.makeText(getContext(), "获取翻译数据失败！", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: 获取翻译数据失败！");
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {

//                        Toast.makeText(getContext(), "成功1！", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: 成功");
                        final String result = response.body().string();
                        Log.d(TAG, result);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(view.getContext(),"error",Toast.LENGTH_SHORT).show();

                                JinshanParseUtil.parseJinshanEnglishToChineseXMLWithPull(result);

                                SharedPreferences pref = getSharedPreferences("JinshanEnglishToChinese", Context.MODE_PRIVATE);

                                String queryText = pref.getString("queryText", word);
                                String voiceEnText = pref.getString("voiceEnText", "空");
                                String voiceEnUrlText = pref.getString("voiceEnUrlText", "空");
                                String voiceAmText = pref.getString("voiceAmText", "空");
                                String voiceAmUrlText = pref.getString("voiceAmUrlText", "空");
                                String meanText = pref.getString("meanText", "空");
                                String exampleText = pref.getString("exampleText", "空");

                                strMeaning = meanText;
                                strSample = exampleText;

                                Log.d(TAG, "run: " + meanText);


                                TextView query = (TextView) view.findViewById(R.id.tv_query);
                                TextView enVoiceLab = (TextView) view.findViewById(R.id.en_voice);
                                ImageView enVoiceImg = (ImageView) view.findViewById(R.id.iv_en_voice);
                                TextView enVoiceText = (TextView) view.findViewById(R.id.en_voice_text);

                                TextView amVoiceLab = (TextView) view.findViewById(R.id.am_voice);
                                ImageView amVoiceImg = (ImageView) view.findViewById(R.id.iv_am_voice);
                                TextView amVoiceText = (TextView) view.findViewById(R.id.am_voice_text);

                                TextView baseMean = (TextView) view.findViewById(R.id.base_mean);
                                TextView examples = (TextView) view.findViewById(R.id.related_examples);

                                enVoiceLab.setText("英式发音：");
                                amVoiceLab.setText("美式发音：");
                                amVoiceLab.setVisibility(View.VISIBLE);
                                amVoiceImg.setVisibility(View.VISIBLE);
                                amVoiceText.setVisibility(View.VISIBLE);

                                query.setText(queryText);
                                enVoiceText.setText(voiceEnText);
                                amVoiceText.setText(voiceAmText);

                                baseMean.setText(meanText);
                                examples.setText(exampleText);

                                enVoiceImg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            MediaPlayer mediaPlayer;
                                            mediaPlayer = MediaPlayer.create(WordActivity.this, Uri.parse(voiceEnUrlText));
                                            mediaPlayer.start();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                amVoiceImg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            MediaPlayer mediaPlayer;
                                            mediaPlayer = MediaPlayer.create(WordActivity.this,Uri.parse(voiceAmUrlText));
                                            mediaPlayer.start();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }
                        });
                    }


                });
            } else {
                HttpUtil.sendOkHttpRequest(url, new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
//                        Toast.makeText(getContext(), "获取翻译数据失败！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        final String result = response.body().string();
                        Log.d(TAG, result);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                JinshanParseUtil.parseJinshanChineseToEnglishJSONWithGson(result);

                                SharedPreferences pref = getSharedPreferences("JinshanChineseToEnglishBaseMean", Context.MODE_PRIVATE);

                                String queryText = pref.getString("queryText", word);
                                String voiceText = pref.getString("voiceText", "空");
                                String voiceUrlText = pref.getString("voiceUrlText", "空");
                                String meanText = pref.getString("meanText", "空");

                                strMeaning = meanText;

                                TextView query = (TextView) view.findViewById(R.id.tv_query);
                                TextView enVoiceLab = (TextView) view.findViewById(R.id.en_voice);
                                ImageView enVoiceImg = (ImageView) view.findViewById(R.id.iv_en_voice);
                                TextView enVoiceText = (TextView) view.findViewById(R.id.en_voice_text);

                                TextView amVoiceLab = (TextView) view.findViewById(R.id.am_voice);
                                ImageView amVoiceImg = (ImageView) view.findViewById(R.id.iv_am_voice);
                                TextView amVoiceText = (TextView) view.findViewById(R.id.am_voice_text);

                                TextView baseMean = (TextView) view.findViewById(R.id.base_mean);


                                enVoiceLab.setText("拼音：");
                                amVoiceLab.setVisibility(View.GONE);
                                amVoiceImg.setVisibility(View.GONE);
                                amVoiceText.setVisibility(View.GONE);

                                query.setText(queryText);
                                enVoiceText.setText(voiceText);
                                baseMean.setText(meanText);


                                enVoiceImg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            MediaPlayer mediaPlayer;
                                            mediaPlayer = MediaPlayer.create(WordActivity.this,Uri.parse(voiceUrlText));
                                            mediaPlayer.start();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                        });
                    }

                });

                HttpUtil.sendOkHttpRequest(urlxml, new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
//                        Toast.makeText(getContext(), "获取翻译数据失败！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        final String result = response.body().string();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String example = JinshanParseUtil.parseJinshanChineseToEnglishXMLWithPull(result);

                                TextView examples = (TextView) view.findViewById(R.id.related_examples);
                                examples.setText(example);

                                strSample = example;
                            }
                        });
                    }


                });
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(WordActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}