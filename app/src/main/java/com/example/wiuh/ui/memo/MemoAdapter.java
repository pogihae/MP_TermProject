package com.example.wiuh.ui.memo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wiuh.MemoActivity;
import com.example.wiuh.R;
import com.example.wiuh.model.Memo;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder>{
    private final List<Memo> localData;

    private Context context;

    MemoAdapter(List<Memo> localData) {
        this.localData = localData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memo_layout, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(localData.get(position));
    }

    @Override
    public int getItemCount() {
        return localData.size();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void updateList(List<Memo> list) {
        this.localData.clear();
        localData.addAll(list);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        private final TextView textView;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            textView = itemView.findViewById(R.id.memo_title);
            this.context = context;
        }

        public void onBind(Memo m) {
            textView.setText(m.title);
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, MemoActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("title",m.title);
                bundle.putString("body",m.body);

                intent.putExtras(bundle);
                context.startActivity(intent);
            });
        }
    }
}
