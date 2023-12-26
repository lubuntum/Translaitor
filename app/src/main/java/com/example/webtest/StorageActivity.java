package com.example.webtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.webtest.adapters.LangPairAdapter;
import com.example.webtest.database.Database;
import com.example.webtest.database.LangPair;

import java.util.List;

public class StorageActivity extends AppCompatActivity
        implements LangPairAdapter.LangPairViewHolder.OnItemLongClickListener {
    public LangPairAdapter adapter;
    public RecyclerView langPairListView;
    public List<LangPair> langPairList;
    public ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage);
        langPairListView = findViewById(R.id.lang_pairs_list);
        progress = findViewById(R.id.progress);
        initLangPairList();
    }
    private void initLangPairList(){
        progress.setVisibility(View.VISIBLE);
        Runnable loadLangPairs = ()->{
            langPairList = Database.getAllLangPairs();
            if(langPairList == null) return;
            runOnUiThread(()->{
                progress.setVisibility(View.INVISIBLE);
                adapter = new LangPairAdapter(getBaseContext(), langPairList, this);
                langPairListView.setAdapter(adapter);
            });
        };
        new Thread(loadLangPairs).start();
    }


    @Override
    public void itemClick(int position) {
        Runnable removeLangPairRnb = ()->{
            Database.removeLangPair(adapter.getItemAtPosition(position));
            runOnUiThread(()->{
                adapter.removeItemAtPosition(position);
                Toast.makeText(this, "Удаление успешно", Toast.LENGTH_SHORT).show();
            });
        };
        new Thread(removeLangPairRnb).start();

    }
}