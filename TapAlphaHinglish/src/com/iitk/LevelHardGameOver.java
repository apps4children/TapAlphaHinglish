package com.iitk;
import com.iitk.hindi.R;
import com.iitk.report.DisplayRecord;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class LevelHardGameOver extends Activity implements OnClickListener
{
	final Context context = this;
	String score,updatedScore;
	TextView tv;

	
	Intent playEasy;
	Intent playMedium;
	Intent playHard;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_over);
                
        View play= findViewById(R.id.play);
        play.setOnClickListener(this);
        
        View next =findViewById(R.id.next);
        next.setOnClickListener(this);        
        
        View exit =findViewById(R.id.exit);
        exit.setOnClickListener(this); 
        gameOverSound();
        
        playEasy= new Intent(this,LevelEasyScreen1.class);
        playMedium= new Intent(this,LevelMediumScreen1.class);
        playHard= new Intent(this,LevelHardScreen1.class);
    }
    public void onClick(View v) 
    {
    	switch (v.getId()) 
    	{
    	case R.id.play:
    		alert();
        	break;
    	case R.id.next:
    		Intent score = new Intent(this,DisplayRecord.class);
        	startActivity(score);
        	break;
    	case R.id.exit:
    		finish();
    		GameActivity.activityMain.finish();
    		super.finish();
        	break;
    	}
    }
    public void gameOverSound()
    {
    	MediaPlayer music = MediaPlayer.create(LevelHardGameOver.this, R.raw.gameover);
        music.start();
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
            	  finish();
            	  startActivity(playEasy);
              }
              else if(item==1)
              {
            	  finish();
            	  startActivity(playMedium);
              }
              else
              {
                  finish();
                  startActivity(playHard);
              }
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
    }
}