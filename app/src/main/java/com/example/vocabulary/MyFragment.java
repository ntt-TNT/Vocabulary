package com.example.vocabulary;

//import android.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.vocabulary.Bean.Word;
import com.example.vocabulary.db.WordsDBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyFragment extends Fragment {
    private static String TAG="LIFE";
    private SearchView searchView;
    private ListView listView;
    private List<HashMap<String,String>> data = new ArrayList<>();
    SimpleAdapter simpleAdapter;
    private LinearLayout linearLayout;

    private View view;


    WordsDBHelper mDbHelper;

    Uri wordsUri = Uri.parse("content://com.example.vocabulary.mContentProvider/Vocabulary");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_fragment, container, false);
        init(view);

        Log.d(TAG, "onCreateView: ");

        return view;
    }


    private void init(View view) {
        mDbHelper = new WordsDBHelper(this.getActivity());

        listView = view.findViewById(R.id.my_list);
//        listView.setVisibility(View.GONE);//设置listview先不可见
        searchView = view.findViewById(R.id.search);

        getAll(mDbHelper,view);
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
//                    Filter filter = simpleAdapter.getFilter();
//                    filter.filter(newText);
                    setWordsListView(searchWords(newText));
                }else {
                    setWordsListView(data);
                }
                return false;
            }
        });

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                getActivity().getMenuInflater().inflate(R.menu.word_item_menu, menu);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView strWord = view.findViewById(R.id.word_name);
                Toast.makeText(getActivity(),strWord.getText().toString(),Toast.LENGTH_LONG).show();
//                WordFragment wordFragment = WordFragment.newWord(strWord.getText().toString());
//
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//                transaction.add(R.id.fragment, wordFragment);
//
//                transaction.addToBackStack(null);
//
//                transaction.commit();

                if (MainActivity.screen==0){
                    WordActivity.actionStart(getActivity(),strWord.getText().toString());

                    Intent intent = new Intent(getActivity(),WordActivity.class);
                    startActivity(intent);
                }else {
                    WordFragment wordFragment = WordFragment.newWord(strWord.getText().toString());
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.add(R.id.fragment_land, wordFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

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

                        getAll(mDbHelper);
                        setWordsListView(data);
                    }
                })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .create()
                .show();

//        getAll(mDbHelper,view);
    }

    public void changeDialog(String strName){
        this.mDbHelper = mDbHelper;
        LayoutInflater inflater = getLayoutInflater();
        final LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.change,null);

        new AlertDialog.Builder(getActivity())
                .setTitle("修改单词")
                .setView(linearLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strMeaning=((EditText)linearLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        String strSample=((EditText)linearLayout.findViewById(R.id.txtSample)).getText().toString();

                        ChangeUserSql(strName,strMeaning,strSample,mDbHelper);

                        getAll(mDbHelper);
                        setWordsListView(data);
                    }
                })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .create()
                .show();


    }

    //使用Sql语句插入单词
    private void InsertUserSql(String strWord, String strMeaning, String strSample, WordsDBHelper mDbHelper){
        String sql="insert into  Vocabulary values(?,?,?)";
        //Gets the data repository in write mode*/

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Log.d(TAG, "InsertUserSql: "+strWord+strMeaning+strSample);
        db.execSQL(sql,new String[]{strWord,strMeaning,strSample});
    }

    //使用Sql语句修改单词
    private void ChangeUserSql(String strWord, String strMeaning, String strSample, WordsDBHelper mDbHelper){
        DeleteUserSql(strWord);
        InsertUserSql(strWord,strMeaning,strSample,mDbHelper);
    }

    //使用Sql语句删除单词
    private void DeleteUserSql(String strWord){
        String sql="delete from Vocabulary where WordName='"+strWord+"'";
        //Gets the data repository in write mode*/

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Log.d(TAG, "DeleteUserSql: ");
        db.execSQL(sql);
    }

    private void getAll(WordsDBHelper mDbHelper,View view){
        data = new ArrayList<>();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            cursor = view.getContext().getContentResolver().query(wordsUri,null,null,null);
        }else{
            throw new RuntimeException("SDK 版本过低");
        }
//                db.query("Vocabulary",null,null,null,null,null,null);
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

    private void getAll(WordsDBHelper mDbHelper){
        data = new ArrayList<>();

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


    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        TextView textId=null;
        TextView textWord=null;
        TextView textMeaning=null;
        TextView textSample=null;
        AdapterView.AdapterContextMenuInfo info=null;
        View itemView=null;
        switch (item.getItemId()){
            case R.id.action_delete:
                //删除单词
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                TextView textId =(TextView)itemView.findViewById(R.id.word_name);
                if(textId!=null){
                    String strName=textId.getText().toString();
                    DeleteUserSql(strName);
                }
                Toast.makeText(getContext(),"delete",Toast.LENGTH_LONG).show();
                break;
            case R.id.action_update:
                //修改单词
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                TextView textId1 =(TextView)itemView.findViewById(R.id.word_name);
                if(textId1!=null){
                    String strName=textId1.getText().toString();
                    changeDialog(strName);
                }
                Toast.makeText(getContext(),"delete",Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    public List<HashMap<String,String>> searchWords(String key) {
        List<HashMap<String,String>> list = new ArrayList<>();
        ArrayList<Word> wordsArrayList = new ArrayList<>();
        String sql = "SELECT * FROM " + Word.TABLE_NAME + " where WordName like '%"+key+"%'";
        String sortOrder = Word.COLUMN_NAME_WORD + " DESC";
        String selection1 = Word.COLUMN_NAME_WORD + " LIKE ?";
        String selection2 = Word.COLUMN_NAME_MEANING + " LIKE ?";
        String[] selectionArgs = {"%"+key+"%"};
        Cursor cursor1;
        Cursor cursor2;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        cursor1 = db.query(Word.TABLE_NAME, null, selection1, selectionArgs, null, null, null);
        cursor2 = db.query(Word.TABLE_NAME, null, selection2, selectionArgs, null, null, null);
        //数据库中有
        if (cursor1.getCount() > 0) {
            Log.i(TAG,"searchWords:找到"+cursor1.getCount()+"条记录");
            if (cursor1.moveToFirst()) {
                do {
                    HashMap<String,String> map = new HashMap<>();
                    map.put("word",cursor1.getString(cursor1.getColumnIndex(Word.COLUMN_NAME_WORD)));
                    map.put("meaning",cursor1.getString(cursor1.getColumnIndex(Word.COLUMN_NAME_MEANING)));
                    map.put("sample",cursor1.getString(cursor1.getColumnIndex(Word.COLUMN_NAME_SAMPLE)));
                    list.add(map);
                } while (cursor1.moveToNext());
            }
        } else {
            Log.d("测试", "数据库中没有");
        }
        cursor1.close();

        if (cursor2.getCount() > 0) {
            Log.i(TAG,"searchWords:找到"+cursor2.getCount()+"条记录");
            if (cursor2.moveToFirst()) {
                do {
                    HashMap<String,String> map = new HashMap<>();
                    map.put("word",cursor2.getString(cursor2.getColumnIndex(Word.COLUMN_NAME_WORD)));
                    map.put("meaning",cursor2.getString(cursor2.getColumnIndex(Word.COLUMN_NAME_MEANING)));
                    map.put("sample",cursor2.getString(cursor2.getColumnIndex(Word.COLUMN_NAME_SAMPLE)));
                    if (!list.contains(map)){
                        list.add(map);
                    }

                } while (cursor2.moveToNext());
            }
        } else {
            Log.d("测试", "数据库中没有");
        }

        return list;
    }


}
