package com.iitk.util;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class CustomAdapter extends BaseAdapter 
{
	private ArrayList<Button> mButtons = null;
	String[] color ={"#864555","#006400","#800080","#24D9F5","#8800CC",
            "#A4C739","#2F4F4F","#A7F7F7","#7B3F00","#FF007F",
            "#8800CC","#824762","#FB5E00","#00CC00","#FFFF00"};
    public CustomAdapter(ArrayList<Button> b) 
    {
        mButtons = b;
    }
	public int getCount() 
	{
		return mButtons.size();
	}
	public Object getItem(int position) 
	{
		return (Object) mButtons.get(position);
	}
	public long getItemId(int position) 
	{
		//in our case position and id are synonymous
		return position;
	}
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		Button button;
        if (convertView == null) 
        {
            button = mButtons.get(position);
            button.setTextColor(Color.parseColor(color[position]));
        } 
        else 
        {
            button = (Button) convertView;
        }
        
        return button;
	}

}
