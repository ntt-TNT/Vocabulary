package com.example.vocabulary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.vocabulary.db.WordsDBHelper;

public class MainActivity extends AppCompatActivity {
    WordsDBHelper mDbHelper;
    RadioGroup rg_main;

    public static int screen=0;


    private static String TAG="LIFE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStatusBarTranslucent(MainActivity.this);
        setContentView(R.layout.activity_main);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.d(TAG, "onCreate: 横屏");
            screen = 1;

        } else if (this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "onCreate: 竖屏");
            screen = 0;
//            changeFragment(new HomeFragment());
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment,new HomeFragment())
                .addToBackStack(null)
                .commit();

        mDbHelper = new WordsDBHelper(this);


//        Translate translate = new Translate();
//        Log.d(TAG, "返回json");
        rg_main = findViewById(R.id.rg_main);
        Log.d(TAG, "onCreate: 1");
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i=0; i<rg_main.getChildCount(); i++){
                    Log.d(TAG, "onCreate: 2");
                    RadioButton rb = (RadioButton) group.getChildAt(i);
                    if(rb.isChecked()){
                        setIndexSelected(i);
                        break;
                    }
                }
            }
        });

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        mDbHelper.close();
    }


    //通过index判断当前加载哪个界面
    public void setIndexSelected(int index) {
        switch (index)
        {
            case 0:
                changeFragment(new HomeFragment());
                break;
            case 1:
                changeFragment(new MyFragment());
                break;

            default:
                break;
        }
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();//开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment , fragment);
        transaction.commit();

        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.hide(new WordFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_menu:
                HomeFragment homeFragment = new HomeFragment();
                changeFragment(new HomeFragment());
                return true;
            case R.id.add_menu:
                Log.d(TAG, "onOptionsItemSelected: ");
                MyFragment myFragment = new MyFragment();
                LayoutInflater inflater = getLayoutInflater();
                myFragment.insertDialog(inflater,MainActivity.this,mDbHelper);
                changeFragment(new MyFragment());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //全屏显示
    public static void setStatusBarTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}