package com.example.vocabulary;

//import android.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyFragment extends Fragment {
    private static String TAG="LIFE";
    private SearchView searchView;
    private ListView listView;
    private List<HashMap<String,String>> data = new ArrayList<>();
    SimpleAdapter simpleAdapter;

    private View view;


    WordsDBHelper mDbHelper;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_fragment, container, false);
        init(view);
        return view;
    }


    private void init(View view) {
        mDbHelper = new WordsDBHelper(this.getActivity());

        listView = view.findViewById(R.id.my_list);
//        listView.setVisibility(View.GONE);//设置listview先不可见
        searchView = view.findViewById(R.id.search);

        getAll(mDbHelper);
        setWordsListView(data);

        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    Filter filter = simpleAdapter.getFilter();
                    filter.filter(newText);
                }else {
                    setWordsListView(data);
                }
                return false;
            }
        });
    }


    public void insertDialog(LayoutInflater inflater, Activity activity , WordsDBHelper mDbHelper){
        Log.d(TAG, "insertDialog: ");
        this.mDbHelper = mDbHelper;
        final LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.insert,null);

        new AlertDialog.Builder(activity)
                .setTitle("新增单词")
                .setView(linearLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strWord=((EditText)linearLayout.findViewById(R.id.txtWord)).getText().toString();
                        String strMeaning=((EditText)linearLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        String strSample=((EditText)linearLayout.findViewById(R.id.txtSample)).getText().toString();

                        InsertUserSql(strWord,strMeaning,strSample,mDbHelper);

                    }
                })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .create()
                .show();

        setWordsListView(data);
    }

    //使用Sql语句插入单词
    private void InsertUserSql(String strWord, String strMeaning, String strSample, WordsDBHelper mDbHelper){
        String sql="insert into  Vocabulary values(?,?,?)";
        //Gets the data repository in write mode*/

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Log.d(TAG, "InsertUserSql: "+strWord+strMeaning+strSample);
        db.execSQL(sql,new String[]{strWord,strMeaning,strSample});
    }

    private void getAll(WordsDBHelper mDbHelper){

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.query("Vocabulary",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                HashMap<String,String> map = new HashMap<>();
                map.put("word",cursor.getString(cursor.getColumnIndex(Word.COLUMN_NAME_WORD)));
                map.put("meaning",cursor.getString(cursor.getColumnIndex(Word.COLUMN_NAME_MEANING)));
                map.put("sample",cursor.getString(cursor.getColumnIndex(Word.COLUMN_NAME_SAMPLE)));
                data.add(map);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }


    private void setWordsListView(List<HashMap<String,String>> data){
        if (super.getActivity()!=null){
            Log.d(TAG, "setWordsListView: "+data.toString());
            simpleAdapter = new SimpleAdapter(
                    this.getActivity(),
                    data,
                    R.layout.listview_item,
                    new String[]{"word","meaning"},
                    new int[]{R.id.word_name,R.id.word_meaning});
            listView.setAdapter(simpleAdapter);//绑定数据
            listView.setTextFilterEnabled(true);//打开过滤
        }

    }



}
