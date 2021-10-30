package com.example.vocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.app.YouDaoApplication;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;

import java.util.List;

public class HomeFragment extends Fragment{
    private static String TAG="LIFE";
    Handler handler = new Handler();
    private Translator translator;

    private TextView content;
    private EditText edit_query;
    private Button query;

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);

        content = view.findViewById(R.id.content);
        edit_query = view.findViewById(R.id.edit_query);
        query = view.findViewById(R.id.query);

        Log.d(TAG, "onCreateView: ");
//        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated: ");
        query.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), edit_query.getText().toString(), Toast.LENGTH_SHORT).show();
//                query();
                WordFragment wordFragment = WordFragment.newWord(edit_query.getText().toString());
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment,new WordFragment())
//                        .commit();
//                Toast.makeText(getActivity(), "success2", Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                if (MainActivity.screen==0){
                    transaction.add(R.id.fragment_port, wordFragment);
                }else {
                    transaction.add(R.id.fragment_land, wordFragment);
                }


                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
    }


    public void init(View view){
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WordFragment wordFragment = WordFragment.newWord(v.findViewById(R.id.edit_query).toString());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment,new WordFragment())
                        .commit();

            }
        });


    }


}
