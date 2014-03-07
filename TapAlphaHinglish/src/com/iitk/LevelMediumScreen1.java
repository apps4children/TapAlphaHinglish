package com.iitk;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import com.iitk.hindi.R;

public class LevelMediumScreen1 extends Activity implements AdapterView.OnItemClickListener  
{
	StringBuilder sb = new StringBuilder();
	String wrongQuestion=null;
	
	ArrayList<String> list=null;
	int counter=0,truecounter=0,level=1,soundCounter=0,wrongCounter=0;
	TextView selection,dailogView;
	Button repeatSound;
	String[] alphabet,soundAlphabets;
	GridView gv ;
	final Context context = this;
	Dialog dialog;
	
	//sound handling
	SoundManager snd;

	int delay = 1000;
	int period = 10000;  //repeat every 10 sec. 
	Timer timer;
	int a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,nana,verygood;
	//color
	String[] color ={"#864555","#006400","#800080","#D3C698","#9B9FAB",
	        "#708090","#2F4F4F","#EE82EE","#7B3F00","#FF007F",
	        "#8800CC","#0099CC","#D9007E","#00CC00","#FFFF00"};
	
	
	Timer myTimer;
	MediaPlayer mediaPlayer;

	public void onCreate(Bundle icicle) 
	{
		super.onCreate(icicle);requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game_medium);
		
		mediaPlayer = MediaPlayer.create(LevelMediumScreen1.this, R.raw.medium_hindi);//play on load
		mediaPlayer.start();//play on load
		
		alphabet=randomCharacter();
		selection =(TextView) findViewById(R.id.selectionmedium);
		//repeatSound=(Button) findViewById(R.id.repeatsound);
		gv= (GridView) findViewById(R.id.gridmedium);
		gv.setAdapter(new ImageAdapter(this));
		gv.setOnItemClickListener(this);
		Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fly_in_from_top_corner);
        gv.setAnimation(anim);
        anim.start();
		
		sound();
		timer();
		
        //repeat sounds
		myTimer = new Timer();
		myTimer.scheduleAtFixedRate(new MyTimerTask(), 5000, 10000);
		
		 Button back=(Button)findViewById(R.id.back);
	        back.setOnClickListener(new Button.OnClickListener()
	        {
	        	public void onClick(View v) 
	        	{
	        		myTimer.cancel();
	        	    finish();
	            }
	         });
		
	}
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
	{
		
		myTimer.cancel();//cancel repeat sound if if user press any buttons

		selection.setText(alphabet[position]);
		selection.setTextColor(Color.parseColor(color[position]));
		if(!alphabet[position].equals(soundAlphabets[counter]))
		{
		++wrongCounter;
		snd.play(nana);
		//v.setBackgroundColor(Color.RED);
		//Drawable background = this.getResources().getDrawable(R.drawable.wrong_border); 
		//v.setBackgroundDrawable(background);
		alert();
		//
		   if(wrongQuestion==null)
		   {
		      wrongQuestion=soundAlphabets[counter];
			  sb.append(soundAlphabets[counter]+"-"+alphabet[position]);
		   }
		   else if(wrongQuestion.equalsIgnoreCase(soundAlphabets[counter]))
		   {
		      sb.append(","+alphabet[position]);
		   }
		   else if(!wrongQuestion.equalsIgnoreCase(soundAlphabets[counter]))
		   {
		      //sb.append("\n");
			  wrongQuestion=soundAlphabets[counter];
			  sb.append(soundAlphabets[counter]+"-"+alphabet[position]);  
		   }
		//
		}
		else if(alphabet[position].equals(soundAlphabets[counter]))
		{
			//
			if(wrongQuestion!=null&&wrongQuestion.equalsIgnoreCase(soundAlphabets[counter]))
			sb.append(","+alphabet[position]);
			else
			sb.append(soundAlphabets[counter]+"-"+alphabet[position]);
			sb.append("\n");
  		    //
			//v.setBackgroundColor(0x30FF0000);
			v.setBackgroundColor(Color.TRANSPARENT);
			v.setOnClickListener(null);
			++truecounter;
			if(truecounter==15)
				++level;
			if(level==2)
			{
               newScreen();
			}
			counter++;
			soundCounter++;
			if(soundCounter<=14)
				timer();
		}
		
		if(soundCounter<=14)
		{
		myTimer = new Timer();
		myTimer.scheduleAtFixedRate(new MyTimerTask(),5000,10000);//again start timer
		}
	}
	public String[] randomCharacter()
	{
		String [] arr;
		SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String alphaType=sharedPreferences.getString("alpha","capital");
        if(alphaType.equals("capital"))
        {
	    arr=new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O"};
        }
        else 
        arr=new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o"};
        Random random = new Random();
        LinkedHashSet<String> randomChar=new LinkedHashSet<String>();
        LinkedHashSet<String> randomCharForSoundPlay=new LinkedHashSet<String>();
        while(randomChar.size()<15)
        {
        	int selectedChar= random.nextInt(arr.length); 
        	randomChar.add(arr[selectedChar]);
	    }
        while(randomCharForSoundPlay.size()<15)
        {
        	int selectedCharForSoundPlay = random.nextInt(arr.length); 
        	randomCharForSoundPlay.add(arr[selectedCharForSoundPlay]);
	    }
        String[] alphabet = randomChar.toArray(new String[0]);
        soundAlphabets=randomCharForSoundPlay.toArray(new String[0]);
		return alphabet;
	}
	public void alert()
	{
		dialog = new Dialog(LevelMediumScreen1.this);
		dialog.setContentView(R.layout.dailog);
		dailogView= (TextView) dialog.findViewById(R.id.dialogView);
		dialog.setTitle("Click Below");
		dialog.setCancelable(true);
		dailogView.setText(soundAlphabets[counter]);
		
		Button ok =(Button) dialog.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() 
        {
        public void onClick(View v) 
        {
        	dialog.dismiss();
        	selection.setText("");
        }
        });
		dialog.show();
	}
	public void sound()
	{
		snd = new SoundManager(getApplicationContext());
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        a=snd.load(R.raw.a);
        b=snd.load(R.raw.b);
        c=snd.load(R.raw.c);
        d=snd.load(R.raw.d);
        e=snd.load(R.raw.e);
        f=snd.load(R.raw.f);
        g=snd.load(R.raw.g);
        h=snd.load(R.raw.h);
        i=snd.load(R.raw.i);
        j=snd.load(R.raw.j);
        k=snd.load(R.raw.k);
        l=snd.load(R.raw.l);
        m=snd.load(R.raw.m);
        n=snd.load(R.raw.n);
        o=snd.load(R.raw.o);
        nana=snd.load(R.raw.nana);
        verygood=snd.load(R.raw.verygood);
	}
	public void play(int id)
	{
		if(soundAlphabets[id].equalsIgnoreCase("A"))
			snd.play(a);
		else if(soundAlphabets[id].equalsIgnoreCase("B"))
			snd.play(b);
		else if(soundAlphabets[id].equalsIgnoreCase("C"))
			snd.play(c);
		else if(soundAlphabets[id].equalsIgnoreCase("D"))
			snd.play(d);
		else if(soundAlphabets[id].equalsIgnoreCase("E"))
			snd.play(e);
		else if(soundAlphabets[id].equalsIgnoreCase("F"))
			snd.play(f);
		else if(soundAlphabets[id].equalsIgnoreCase("G"))
			snd.play(g);
		else if(soundAlphabets[id].equalsIgnoreCase("H"))
			snd.play(h);
		else if(soundAlphabets[id].equalsIgnoreCase("I"))
			snd.play(i);
		else if(soundAlphabets[id].equalsIgnoreCase("J"))
			snd.play(j);
		else if(soundAlphabets[id].equalsIgnoreCase("K"))
			snd.play(k);
		else if(soundAlphabets[id].equalsIgnoreCase("L"))
			snd.play(l);
		else if(soundAlphabets[id].equalsIgnoreCase("M"))
			snd.play(m);
		else if(soundAlphabets[id].equalsIgnoreCase("N"))
			snd.play(n);
		else if(soundAlphabets[id].equalsIgnoreCase("O"))
			snd.play(o);
	}
	public void timer()
	{
		timer= new Timer();
        timer.schedule( new TimerTask()
        {
           public void run() 
           { 
               play(soundCounter);
           }
         },delay);
	}
	
	public void newScreen()
	{		
		timer.cancel();
		int DELAY = 1000;
	    Handler handler = new Handler();
	    handler.postDelayed(new Runnable() 
	    {            
	        public void run() 
	        {
	        	snd.play(verygood);
	            Intent intent = new Intent(LevelMediumScreen1.this, LevelMediumScreen2.class);
	            intent.putExtra("wrong", sb.toString());
	            startActivity(intent);    
	            overridePendingTransition(R.anim.bottom_in,R.anim.top_out);
	            finish();
	        }
	    }, DELAY);
	}
	
	//gradient class and methods below
	public class ImageAdapter extends BaseAdapter
	{
	    Context MyContext;
		public ImageAdapter(Context _MyContext){
		MyContext = _MyContext;
		}
		public int getCount(){
	    return 15;
		}
		public View getView(int position, View convertView, ViewGroup parent){
		View MyView = convertView;
		if( convertView == null )
		{
		LayoutInflater li = getLayoutInflater();
		MyView = li.inflate(R.layout.cell_medium, null);
		}
		TextView tv = (TextView)MyView.findViewById(R.id.mediumgridcell);
		tv.setText(alphabet[position]);
		tv.setTextColor(Color.parseColor(color[position]));
		return MyView;
		}
		public Object getItem(int arg0) {
			return null;
		}
		public long getItemId(int arg0) {
			return 0;
		}
	}
	
	//automatically repeat sound after 20 seconds
	private class MyTimerTask extends TimerTask 
	{
	    @Override
	    public void run()
	    {
	        runOnUiThread(new Runnable() 
	        {
	            public void run() 
	            {
	               //code to get and send location information
                   play(soundCounter);
	            }
	        });
	    }
	}
	
	@Override
    protected void onPause() {
      super.onPause();
    }
    @Override
    protected void onResume() {
      super.onResume();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
        	myTimer.cancel();
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}