package com.example.wiuh.ui.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wiuh.R;
import com.example.wiuh.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
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
        private final TextView textView;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(v -> Toast.makeText(context, textView.getText(), Toast.LENGTH_LONG).show());
        }

        public void onBind(Post p) {
            textView.setText(p.body);
        }
    }
}
