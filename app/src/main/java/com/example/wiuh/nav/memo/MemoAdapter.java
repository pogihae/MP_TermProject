package com.example.wiuh.nav.memo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wiuh.R;
import com.example.wiuh.model.Memo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {

    private final List<Memo> localMemoList;
    private Context context;

    MemoAdapter(List<Memo> localMemoList) {
        this.localMemoList = localMemoList;
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
        holder.onBind(localMemoList.get(position));

        //set random color for every memo
        int colorcode = getRandomColor();
        holder.mnote.setBackgroundColor(holder.itemView.getResources().getColor(colorcode, null));
    }

    @Override
    public int getItemCount() {
        return localMemoList.size();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void updateList(List<Memo> list) {
        final MemoDiffCallback diffCallback = new MemoDiffCallback(this.localMemoList, list);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        localMemoList.clear();
        localMemoList.addAll(list);
        diffResult.dispatchUpdatesTo(this);
    }

    private int getRandomColor() {
        List<Integer> colorcode = new ArrayList<>();
        colorcode.add(R.color.gray);
        colorcode.add(R.color.pink);
        colorcode.add(R.color.green);
        colorcode.add(R.color.sand);
        colorcode.add(R.color.color1);
        colorcode.add(R.color.color2);
        colorcode.add(R.color.color4);

        Random random = new Random();
        int number = random.nextInt(colorcode.size());
        return colorcode.get(number);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        private final TextView notetitle;
        private final TextView notecontent;
        LinearLayout mnote;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            notetitle = itemView.findViewById(R.id.memo_title);
            notecontent = itemView.findViewById(R.id.memo_content);
            mnote = itemView.findViewById(R.id.memo);
            this.context = context;

        }

        public void onBind(Memo m) {
            notetitle.setText(m.title);
            notecontent.setText(m.body);
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, MemoDetailActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("title", m.title);
                bundle.putString("body", m.body);
                bundle.putString("key", m.key);

                intent.putExtras(bundle);
                context.startActivity(intent);
            });
        }
    }
}