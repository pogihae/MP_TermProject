package com.example.wiuh.ui.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wiuh.R;
import com.example.wiuh.activity.board.post.PostDetailActivity;
import com.example.wiuh.model.Post;
import com.example.wiuh.ui.BaseDiffCallback;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private final List<Post> localPostList;

    private final Context context;

    public PostAdapter(List<Post> localData, Context context) {
        this.localPostList = localData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_community, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(localPostList.get(position));
    }

    @Override
    public int getItemCount() {
        return localPostList.size();
    }

    public void updateList(List<Post> list) {
        final BaseDiffCallback<Post> diffCallback = new BaseDiffCallback<>(this.localPostList, list);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        localPostList.clear();
        localPostList.addAll(list);
        diffResult.dispatchUpdatesTo(this);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        private final TextView title;
        private final TextView body;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            title = itemView.findViewById(R.id.name);
            body = itemView.findViewById(R.id.body);
            this.context = context;
        }

        public void onBind(Post p) {
            title.setText(p.title);
            body.setText(p.body);
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, PostDetailActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("title", p.title);
                bundle.putString("body", p.body);
                bundle.putString("author", p.author);
                bundle.putString("uid", p.uid);
                bundle.putString("key", p.key);
                bundle.putInt("like", p.like);

                intent.putExtras(bundle);
                context.startActivity(intent);
            });
        }
    }
}