package com.example.wiuh.nav.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wiuh.R;
import com.example.wiuh.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    static final String POST = "post";

    private final List<Post> localData;

    private Context context;

    PostAdapter(List<Post> localData) {
        this.localData = localData;
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
        holder.onBind(localData.get(position));
    }

    @Override
    public int getItemCount() {
        return localData.size();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void updateList(List<Post> list) {
        this.localData.clear();
        localData.addAll(list);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        private final TextView textView;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
            this.context = context;
        }

        public void onBind(Post p) {
            textView.setText(p.title);
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, PostActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("title",p.title);
                bundle.putString("body",p.body);
                bundle.putString("author",p.author);
                bundle.putString("uid",p.uid);
                bundle.putString("key",p.key);

                intent.putExtras(bundle);
                context.startActivity(intent);
            });
        }
    }
}
