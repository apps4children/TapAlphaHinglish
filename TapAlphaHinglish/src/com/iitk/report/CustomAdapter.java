package com.iitk.report;
import java.util.ArrayList;
import com.iitk.database.Student;
import com.iitk.hindi.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
	 
    private ArrayList<Student> student;
    Context c;
    
    CustomAdapter(ArrayList<Student> data1, Context c){
    	student = data1;
    }
    public int getCount() 
    {
        // TODO Auto-generated method stub
        return student.size();
    }
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return student.get(position);
    }
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
   
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        final Student st= student.get(position);
        
         View v = convertView;

         if (v == null) 
         {
             LayoutInflater inflater = LayoutInflater.from(parent.getContext());
             v = inflater.inflate(R.layout.list_item_message, parent, false);
         }
 
           //ImageView image = (ImageView) v.findViewById(R.id.icon);
           TextView name = (TextView)v.findViewById(R.id.name);
           TextView date = (TextView)v.findViewById(R.id.date);
           TextView level1 = (TextView)v.findViewById(R.id.level1);
           TextView level2 = (TextView)v.findViewById(R.id.level2);
           TextView level1status = (TextView)v.findViewById(R.id.level1status);
           TextView level2status = (TextView)v.findViewById(R.id.level2status);

           ImageView delete=(ImageView)v.findViewById(R.id.delete);
           delete.setTag(st);
 
           //image.setImageResource(msg.icon);
           name.setText(st.getName().toUpperCase());
           date.setText(st.getDate()); 
           level1.setText(st.getLevel1());
           level2.setText(st.getLevel2());

           if(st.getLevel1()==null||st.getLevel1().equals(""))
        	   level1status.setText("Not Played");
           else
        	   level1status.setText("Played");
           
           if(st.getLevel2()==null||st.getLevel2().equals(""))
        	   level2status.setText("Not Played");
           else
        	   level2status.setText("Played");                 
                        
        return v;
       }
    
    public void deleteRow(Student st) 
	{
		if(this.student.contains(st)) 
		{
			this.student.remove(st);
		}
	}
}