package com.example.wiuh.ui.community;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewItem {
    private Drawable iconDrawable;
    private String titleStr;
    private String contentsStr;

    public void setIcon(Drawable icon){
        iconDrawable=icon;
    }
    public void setTitle(String title){
        titleStr=title;
    }
    public void setContents(String contents){
        contentsStr=contents;
    }

    public Drawable getIcon(){
        return this.iconDrawable;
    }
    public String getTitle(){
        return this.titleStr;
    }
    public String getContents(){
        return this.contentsStr;
    }


}



