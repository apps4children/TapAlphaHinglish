package com.iitk.database;

import java.io.IOException; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context; 
import android.database.Cursor; 
import android.database.SQLException; 
import android.database.sqlite.SQLiteDatabase; 
import android.util.Log;  
public class TestAdapter  
{ 
    protected static final String TAG = "DataAdapter"; 
 
    private final Context mContext; 
    private SQLiteDatabase mDb; 
    private DataBaseHelper mDbHelper; 
    int startIndex, endIndex;

    public TestAdapter(Context context){ 
        this.mContext = context; 
        mDbHelper = new DataBaseHelper(mContext); 
    } 
    public TestAdapter createDatabase() throws SQLException{ 
        try{ 
            mDbHelper.createDataBase(); 
        }catch (IOException mIOException){ 
            Log.e(TAG, mIOException.toString() + "UnableToCreateDatabase"); 
            throw new Error("UnableToCreateDatabase");
        } 
        return this; 
    } 
    public TestAdapter open() throws SQLException{ 
        try{ 
            mDbHelper.openDataBase(); 
            mDbHelper.close(); 
            mDb = mDbHelper.getReadableDatabase(); 
        }catch (SQLException mSQLException){ 
            Log.e(TAG, "open >>"+ mSQLException.toString()); 
            throw mSQLException; 
        } 
        return this; 
    } 
    public void close(){ 
        mDbHelper.close(); 
    } 
    
    /*********************************************Fetch all student id**********************************************/
    public String insertStudent(String name) 
    { 
    	String TABLE_NAME="score";
    	String id=null;
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
    	try 
        { 
        ContentValues initialValues = new ContentValues();

        String dateString= dateFormat.format(date);

        initialValues.put("name",name);
        initialValues.put("date",dateString);
        initialValues.put("level1","");
        initialValues.put("level2","");
        initialValues.put("level3","");

        long recordID=mDb.insert(TABLE_NAME, null, initialValues);
        //converting long to string
        id= Long.toString(recordID);
        System.out.println("Record Inserted,ID===="+id);
        
        return id;
        } catch (SQLException mSQLException)  
        { 
            Log.e(TAG, "Inserting Name"+ mSQLException.toString()); 
            throw mSQLException; 
        } 
    	
    } 

    /*********************************************Fetch all student data**********************************************/
    public ArrayList<Student> browseAllStudent() 
    { 
    	ArrayList<Student> records=new ArrayList<Student>();
        try 
        { 
        	String sql ="select * from score"; 
        	Cursor cursor = mDb.rawQuery(sql,null); 
            if (cursor.moveToFirst())
            {
           	   do
           	   {   
           		  Student record = new Student(); 
           	      String id=cursor.getString(cursor.getColumnIndex("_id"));
           	      String name=cursor.getString(cursor.getColumnIndex("name"));
           	      String date=cursor.getString(cursor.getColumnIndex("date"));
           	      String level1=cursor.getString(cursor.getColumnIndex("level1"));
           	      String level2=cursor.getString(cursor.getColumnIndex("level2"));
           	      String level3=cursor.getString(cursor.getColumnIndex("level3"));
           	      
           	      System.out.println("Name="+name);

           	      //adding to ArrayList
           	      record.setId(id);
           	      record.setName(name);
           	      record.setDate(date);
           	      record.setLevel1(level1);
           	      record.setLevel2(level2);
           	      record.setLevel3(level3);
          	      
           	      records.add(record);
           	     
           	    }while(cursor.moveToNext());
             }
            cursor.close();            
            return records;
        } 
        catch (SQLException mSQLException)  
        { 
            Log.e(TAG, "getTestData >>"+ mSQLException.toString()); 
            throw mSQLException; 
        } 
     } 
    /*********************************************Fetch student data by name**********************************************/
    public ArrayList<Student> browseStudentByName(String username) 
    { 
    	ArrayList<Student> records=new ArrayList<Student>();
        try 
        { 
        	String sql ="select * from score where name like ?"; 
        	Cursor cursor = mDb.rawQuery(sql,new String[] { username }); 
            if (cursor.moveToFirst())
            {
           	   do
           	   { 
           		  Student record = new Student(); 
        	      String id=cursor.getString(cursor.getColumnIndex("_id"));
        	      String name=cursor.getString(cursor.getColumnIndex("name"));
        	      String date=cursor.getString(cursor.getColumnIndex("date"));
        	      String level1=cursor.getString(cursor.getColumnIndex("level1"));
        	      String level2=cursor.getString(cursor.getColumnIndex("level2"));
        	      String level3=cursor.getString(cursor.getColumnIndex("level3"));
        	      
        	      System.out.println("Name="+name);

        	      //adding to ArrayList
        	      record.setId(id);
        	      record.setName(name);
        	      record.setDate(date);
        	      record.setLevel1(level1);
        	      record.setLevel2(level2);
        	      record.setLevel3(level3);
       	      
        	      records.add(record);
           	     
           	    }while(cursor.moveToNext());
             }
            cursor.close();            
            return records;
        } 
        catch (SQLException mSQLException)  
        { 
            Log.e(TAG, "getTestData >>"+ mSQLException.toString()); 
            throw mSQLException; 
        } 
     } 
    /*********************************************Fetch student data by name**********************************************/
    public ArrayList<String> browseStudentByGroup() 
    { 
    	ArrayList<String> records=new ArrayList<String>();
        try 
        { 
        	String sql ="select * from score group by name"; 
        	Cursor cursor = mDb.rawQuery(sql,null); 
            if (cursor.moveToFirst())
            {
               records.add("All");
           	   do
           	   { 
           		   
           		  String username=cursor.getString(cursor.getColumnIndex("name"));
	              String name=username.substring(0, 1).toUpperCase() + username.substring(1);
      	          records.add(name);
           	     
           	    }while(cursor.moveToNext());
             }
            cursor.close();            
            return records;
        } 
        catch (SQLException mSQLException)  
        { 
            Log.e(TAG, "getTestData >>"+ mSQLException.toString()); 
            throw mSQLException; 
        } 
     } 
    /*********************************************Update Level**********************************************/
    public void updateLevel(int id,String data,String level) 
    { 
    	String TABLE_NAME="score";
    	try 
        { 
        ContentValues initialValues = new ContentValues();
        initialValues.put(level,data);
        mDb.update(TABLE_NAME, initialValues, "_id=" + id, null);
        }
    	catch (SQLException mSQLException)  
        { 
            Log.e(TAG, "Inserting Name"+ mSQLException.toString()); 
            throw mSQLException; 
        } 
    }
    public void deleteRecord(String id) 
    {
    	mDb.delete("score","_id" + "=?",new String[] {id});
    }
}
