package com.example.vocabulary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    WordsDBHelper mDbHelper;
    RadioGroup rg_main;



    private static String TAG="LIFE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mDbHelper = new WordsDBHelper(this);


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
        mDbHelper.close();
    }


    //通过index判断当前加载哪个界面
    public void setIndexSelected(int index) {
        switch (index)
        {
            case 0:
                changeFragment(new RightFragment());
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


}