package com.iitk;
import com.iitk.database.TestAdapter;
import com.iitk.hindi.R;
import com.iitk.report.DisplayRecord;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
public class GameActivity extends Activity implements OnClickListener
{
    /** Called when the activity is first created. */
	String str,alphaType;
	MediaPlayer mediaPlayer;
	Dialog dialog;
	EditText name;
	int saveroll;
	TestAdapter mDbHelper; 
	String savename="";
	
	Intent playEasy;
	Intent playMedium;
	Intent playHard;
	static GameActivity activityMain;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        activityMain=this;
        
        mDbHelper = new TestAdapter(this);
        
        View newButton = findViewById(R.id.new_button);
        newButton.setOnClickListener(this);
        
        View levelButton =findViewById(R.id.level_button);
        levelButton.setOnClickListener(this);
        
        View aboutButton = findViewById(R.id.instruction_button);
        aboutButton.setOnClickListener(this);
        
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
      
        View gamescore = findViewById(R.id.score);
        gamescore.setOnClickListener(this);
               
        mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.gamestart);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) 
        {
           mediaPlayer.start();
        }
        });
        mediaPlayer.start();
        userForm();
        
        playEasy= new Intent(this,LevelEasyScreen1.class);
        playMedium= new Intent(this,LevelMediumScreen1.class);
        playHard= new Intent(this,LevelHardScreen1.class);
    }
    public void onClick(View v) 
    {
    	SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        //str=sharedPreferences.getString("level","easy");
        alphaType=sharedPreferences.getString("alpha","capital");
    	switch (v.getId()) 
    	{
    	case R.id.new_button:
    		alert();
        	break;
    	case R.id.level_button:
    		Intent settings = new Intent(this,Settings.class);
        	startActivity(settings);
        	break;
    	case R.id.score:
    		Intent score = new Intent(this,DisplayRecord.class);
        	startActivity(score);
        	break;
    	case R.id.instruction_button:
    	    Intent i = new Intent(this,Instruction.class);
    	    startActivity(i);
    	break;
    	case R.id.exit_button:
            finish();
            System.exit(0);
       	break;
    	}
    }

    public void userForm()
	{
		dialog = new Dialog(GameActivity.this);
		dialog.setContentView(R.layout.userdetail);
		dialog.setTitle("Player Details");
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);

        name=(EditText) dialog.findViewById(R.id.name);
        Button save= (Button)dialog.findViewById(R.id.save);
        Button cancel= (Button)dialog.findViewById(R.id.cancel);
		save.setOnClickListener(new OnClickListener() 
        {
        public void onClick(View v) 
        {
        savename=name.getText().toString();
        if (name ==null|| name.length() == 0) 
      	{
		name.setError("Enter Your Name");
		name.requestFocus();
		}
        else
      	{
         int userid;
         userid=Integer.parseInt(savePlayerNameDB(savename));//inserting name in database and fetching id
         savePlayerNameApplication(savename, userid);//saving name and id to shared preference
      	 dialog.dismiss();
      	}
        }
        });
		cancel.setOnClickListener(new OnClickListener() 
        {
        public void onClick(View v) 
        {
        	savePlayerNameApplication("",0);//saving null in name and  0 in id to shared preference to ignore recording
        	dialog.dismiss();
        }
        });
		dialog.show();
	}
    
    public void savePlayerNameApplication(String name,int id)
    {
    SharedPreferences sharedPref= getSharedPreferences("mypref", MODE_PRIVATE);
    SharedPreferences.Editor editor= sharedPref.edit();
    editor.putString("playerName",name);
    editor.putInt("playerID",id);
    editor.commit();
    }
    
    public String savePlayerNameDB(String name) 
    {
    	String id;
		mDbHelper.createDatabase();       
		mDbHelper.open(); 
		id=mDbHelper.insertStudent(name); 
		mDbHelper.close();
		return id;
	}
    
    public void alert()
    {
    	String[] levels = {"Easy /आसान","Medium /मध्यम","Hard /कठिन"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//builder.setTitle("Select Game Level");
		builder.setItems(levels, new DialogInterface.OnClickListener() 
		{
		    public void onClick(DialogInterface dialog, int item) 
		    {
              if(item==0)
              {
            	  startActivity(playEasy);
              }
              else if(item==1)
              {
            	  startActivity(playMedium);
              }
              else
              startActivity(playHard);
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
    }
    
    @Override
    protected void onPause() {
      super.onPause();
      mediaPlayer.pause();
    }
    @Override
    protected void onResume() {
      super.onResume();
      mediaPlayer.start();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() 
    {
            super.onBackPressed();
            this.finish();
    }
}