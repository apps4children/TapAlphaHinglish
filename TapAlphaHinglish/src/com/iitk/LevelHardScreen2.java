package com.iitk;
import com.iitk.hindi.R;
import com.iitk.util.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
public class LevelHardScreen2 extends Activity 
{
	String [] resultsAlphabets= {"N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	int counter;
	String[] alphabets;
	GridView gridView;
	private ArrayList<Button> mButtons = new ArrayList<Button>();
	MyDragEventListener myDragEventListener = new MyDragEventListener();
	Button source,target,blinkText;
	String sourceText,targetText;
	
	//animation variables
	Animation animAlpha;
	//sound
	SoundManager snd;
	int swap;
	public void onCreate(Bundle savedInstanceState) 
	{
	   super.onCreate(savedInstanceState);
	   requestWindowFeature(Window.FEATURE_NO_TITLE);
	   setContentView(R.layout.game_hard);
	  //animation
	   animAlpha= AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
	   //sound
	   snd = new SoundManager(getApplicationContext());
       this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
       swap=snd.load(R.raw.swap);
	   alphabets=randomCharacter();
	   
	   //
	   DisplayMetrics metrics = new DisplayMetrics();
	   getWindowManager().getDefaultDisplay().getMetrics(metrics);
	   int width = metrics.widthPixels;
	   int height = metrics.heightPixels;
	   height=height-height*23/100;
	   //
	   
	   Button button= null;
	    for (int i=0; i<13; i++) 
	    {
	     button =(Button)getLayoutInflater().inflate(R.layout.cell_hard, null);
	     button.setMinimumHeight(height/4);
	     button.setText(alphabets[i]);
	     button.setId(i);
	     button.setTag(alphabets[i]);
	     button.setOnTouchListener(new MyTouchListener());
	     button.setOnDragListener(myDragEventListener);
	     mButtons.add(button);
	     
	     if(alphabets[i].equalsIgnoreCase(resultsAlphabets[0]))
	     {
	    	 blink(button);
	     }
	    }
	     button =(Button)getLayoutInflater().inflate(R.layout.cell_hard, null);
	     button.setMinimumHeight(height/4);
	     button.setId(14);
	     button.setTag("14");
	     button.setOnTouchListener(new MyTouchListener());
	     button.setOnDragListener(myDragEventListener);
	     mButtons.add(button);
	     gridView = (GridView) findViewById(R.id.gridhard);
	     gridView.setAdapter(new CustomAdapter(mButtons));
	     Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fly_in_from_top_corner);
	     gridView.setAnimation(anim);
	     anim.start();
	     
	     Button back=(Button)findViewById(R.id.back);
	        back.setOnClickListener(new Button.OnClickListener()
	        {
	        	public void onClick(View v) 
	        	{
	        	finish();
	        }});
	 }
	public String[] randomCharacter()
	{
		String [] alpha;
		SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String alphaType=sharedPreferences.getString("alpha","capital");
        if(alphaType.equals("capital"))
        {
	    alpha=new String[]{"N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        }
        else 
        alpha=new String[]{"n","o","p","q","r","s","t","u","v","w","x","y","z"};
        Random random = new Random();
        LinkedHashSet<String> randomChar=new LinkedHashSet<String>();
        while(randomChar.size()<13)
        {
        	int selectedChar= random.nextInt(alpha.length); 
        	randomChar.add(alpha[selectedChar]);
	    }
        String[] alphabet=randomChar.toArray(new String[0]);
 		return alphabet;
	 }
	private static class MyDragShadowBuilder extends View.DragShadowBuilder 
	{
	    private static Drawable shadow;
	    public MyDragShadowBuilder(View v) 
	    {
	      super(v);
	      shadow = new ColorDrawable(Color.LTGRAY);
	    }
	    @Override
	    public void onProvideShadowMetrics (Point size, Point touch)
	    {
	      int width = getView().getWidth();
	      int height = getView().getHeight();
	      shadow.setBounds(0, 0, width, height);
	      size.set(width, height);
	      touch.set(width / 2, height / 2);
	    }
	    @Override
	    public void onDrawShadow(Canvas canvas) 
	    {
	      shadow.draw(canvas);
	    }
	}
	protected class MyDragEventListener implements View.OnDragListener 
	 {
	   public boolean onDrag(View v, DragEvent event) 
	   {
	   target=(Button)v;  
	   final int action = event.getAction();
	   switch(action) 
	   {
	   case DragEvent.ACTION_DRAG_STARTED:
		   //System.out.println("Drag Started="+v.getTag());
        return true;
	   case DragEvent.ACTION_DRAG_ENTERED:
		  // System.out.println("Drag Entered="+v.getTag());
	    return true;
	   case DragEvent.ACTION_DRAG_LOCATION:
		   //System.out.println("Drag Location="+v.getTag());
	    return true;
	   case DragEvent.ACTION_DRAG_EXITED:
		  // System.out.println("Drag Exited="+v.getTag());
	    return true;
	   case DragEvent.ACTION_DROP:
       	   //System.out.println("Hello sunil="+v.getTag());
		   targetText=(String) target.getText();
	   	   ((Button)target).setText(sourceText);
		   ((Button)source).setText(targetText);
		   
		   if(source.getTag().equals(v.getTag()))
			   ((Button)source).setText(sourceText);
		   else
		   {
			   snd.play(swap);
			   //source.startAnimation(animAlpha);
			   target.startAnimation(animAlpha);
			   blinkText.clearAnimation();
		   }
		   result();
		     		   
	    return true;
	   case DragEvent.ACTION_DRAG_ENDED:
	    if (!event.getResult())
	    	((Button)source).setText(sourceText);
	    return true;
	  
	   default:
	    return false;
	   }
	  } 
	 }
	private final class MyTouchListener implements OnTouchListener 
	{
	    public boolean onTouch(View v, MotionEvent motionEvent) 
	    {
	      if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) 
	      {
	  		Button view=(Button)v;
			source=(Button) v;
			sourceText=(String) view.getText();
			view.setText("");
			ClipData.Item item = new ClipData.Item((CharSequence)view.getTag());
			String[] clipDescription = {ClipDescription.MIMETYPE_TEXT_PLAIN};
		    ClipData dragData = new ClipData((CharSequence)view.getText(),clipDescription,item);
		    DragShadowBuilder myShadow = new MyDragShadowBuilder(v);
		    v.startDrag(dragData,myShadow,v,0);
	        return true;
	      } 
	      else 
	      {return false;}
	    }
	 }
	public void result()
	{
		counter=0;
		Button btn=null;
		for(int i=0;i<gridView.getCount()-1;i++)
		{
			btn=(Button) gridView.getItemAtPosition(i);
			String btnText=(String) btn.getText();
			if(btnText.equalsIgnoreCase(resultsAlphabets[i]))
			{
			Drawable background = this.getResources().getDrawable(R.drawable.right_border); 
			btn.setBackgroundDrawable(background);
			counter++;
		    }
		    else
		    {
			Drawable background = this.getResources().getDrawable(R.drawable.border); 
			btn.setBackgroundDrawable(background);
		    }
		}
		if(counter==13)
		{
			newScreen();
  		}
	}
	public void newScreen()
	{		
		int DELAY = 2000;
	    Handler handler = new Handler();
	    handler.postDelayed(new Runnable() 
	    {            
	        public void run() 
	        {
	            Intent intent = new Intent(LevelHardScreen2.this, LevelHardGameOver.class);
	            startActivity(intent);     
	            overridePendingTransition(R.anim.bottom_in,R.anim.top_out); 
	            finish();
	        }
	    }, DELAY);
	}
	public void blink(Button tv)
	{
		blinkText=tv;
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(500); //You can manage the time of the blink with this parameter
		anim.setStartOffset(100);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		blinkText.startAnimation(anim);
	}
}