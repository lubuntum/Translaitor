package com.example.webtest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.webtest.R;
import com.example.webtest.database.LangPair;

import java.util.List;

public class LangPairAdapter extends RecyclerView.Adapter<LangPairAdapter.LangPairViewHolder> {
    private List<LangPair> langPairList;
    private LayoutInflater inflater;
    private LangPairViewHolder.OnItemLongClickListener listener;
    private int resource = R.layout.lang_pair_item;
    public LangPairAdapter(Context context, List<LangPair> langPairs, LangPairViewHolder.OnItemLongClickListener listener){
        this.langPairList = langPairs;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }
    @NonNull
    @Override
    public LangPairViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(resource, parent, false);

        LangPairViewHolder viewHolder = new LangPairViewHolder(view, listener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LangPairViewHolder holder, int position) {
        LangPair langPair = langPairList.get(position);
        holder.langPairView.setText(String.format("%s - %s", langPair.original, langPair.translate));
    }

    @Override
    public int getItemCount() {
        return langPairList.size();
    }
    public LangPair getItemAtPosition(int position){
        return langPairList.get(position);
    }
    public void removeItemAtPosition(int pos){
        langPairList.remove(pos);
        notifyItemRemoved(pos);
    }

    public static class LangPairViewHolder extends RecyclerView.ViewHolder{
        public TextView langPairView;
        public LangPairViewHolder(@NonNull View itemView, OnItemLongClickListener listener) {
            super(itemView);
            langPairView = itemView.findViewById(R.id.lang_pair);
            itemView.setOnLongClickListener((view)->{
                listener.itemClick(getAdapterPosition());
                return false;
            });

        }

        public interface OnItemLongClickListener {
            void itemClick(int position);
        }
    }

}
