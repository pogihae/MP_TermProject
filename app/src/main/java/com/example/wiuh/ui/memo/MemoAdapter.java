package com.example.wiuh.ui.memo;

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
import com.example.wiuh.activity.memo.MemoDetailActivity;
import com.example.wiuh.model.Memo;
import com.example.wiuh.ui.BaseDiffCallback;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {
    private final List<Integer> colorList = Arrays.asList(
            R.color.gray, R.color.pink, R.color.green,
            R.color.sand, R.color.color1, R.color.color2,
            R.color.color4
    );
    private final Random random = new Random();

    private final List<Memo> localMemoList;
    private final Context context;

    public MemoAdapter(List<Memo> localMemoList, Context context) {
        this.localMemoList = localMemoList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return localMemoList.size();
    }

    public void updateList(List<Memo> list) {
        final BaseDiffCallback<Memo> diffCallback = new BaseDiffCallback<>(this.localMemoList, list);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        localMemoList.clear();
        localMemoList.addAll(list);
        diffResult.dispatchUpdatesTo(this);
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
        int colorCode = getRandomColor();
        holder.noteContainer.setBackgroundColor(
                holder.itemView.getResources().getColor(colorCode, null)
        );
    }

    private int getRandomColor() {
        int number = random.nextInt(colorList.size());
        return colorList.get(number);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        private final TextView title;
        private final TextView content;
        LinearLayout noteContainer;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            title = itemView.findViewById(R.id.memo_title);
            content = itemView.findViewById(R.id.memo_content);
            noteContainer = itemView.findViewById(R.id.memo);
            this.context = context;
        }

        public void onBind(Memo m) {
            title.setText(m.title);
            content.setText(m.body);
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