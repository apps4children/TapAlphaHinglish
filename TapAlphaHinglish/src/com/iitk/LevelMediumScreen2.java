package com.iitk;
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
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
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

import com.iitk.database.TestAdapter;
import com.iitk.hindi.R;
public class LevelMediumScreen2 extends Activity implements AdapterView.OnItemClickListener  
{
	StringBuilder sb = new StringBuilder();
	TestAdapter mDbHelper;
	String wrongQuestion=null;
	
	int counter=0,truecounter=0,level=1,soundCounter=0,wrongCounter=0;
	TextView selection,dailogView;
	String[] alphabet,soundAlphabets;
	GridView gv ;
	final Context context = this;
	Dialog dialog;
	
	//sound handling
	SoundManager snd;
	int delay = 1000;
	Timer timer;
	int a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,nana;
	
	//color
	String[] color ={"#864555","#006400","#800080","#D3C698","#9B9FAB",
		             "#708090","#2F4F4F","#EE82EE","#7B3F00","#FF007F",
		             "#8800CC","#0099CC","#D9007E","#00CC00","#FFFF00"};
	
	Timer myTimer;
	public void onCreate(Bundle icicle) 
	{
		super.onCreate(icicle);requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game_medium);
		alphabet=randomCharacter();
		selection =(TextView) findViewById(R.id.selectionmedium);
		gv= (GridView) findViewById(R.id.gridmedium);
		gv.setAdapter(new ImageAdapter(this));
		gv.setOnItemClickListener(this);
		Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fly_in_from_top_corner);
        gv.setAnimation(anim);
        anim.start();
		sound();
		timer();
		
		Bundle extras = getIntent().getExtras();  
        String wrong= extras.getString("wrong"); 
        sb.append(wrong);
		
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
	    arr=new String[]{"L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        }
        else 
        arr=new String[]{"l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
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
		dialog = new Dialog(LevelMediumScreen2.this);
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
        	//repeating sounds and clear target screen
        	//timer();
        	selection.setText("");
        }
        });
		dialog.show();
	}
	public void sound()
	{
		snd = new SoundManager(getApplicationContext());
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        l=snd.load(R.raw.l);
        m=snd.load(R.raw.m);
        n=snd.load(R.raw.n);
        o=snd.load(R.raw.o);
        p=snd.load(R.raw.p);
        q=snd.load(R.raw.q);
        r=snd.load(R.raw.r);
        s=snd.load(R.raw.s);
        t=snd.load(R.raw.t);
        u=snd.load(R.raw.u);
        v=snd.load(R.raw.v);
        w=snd.load(R.raw.w);
        x=snd.load(R.raw.x);
        y=snd.load(R.raw.y);
        z=snd.load(R.raw.z);
        nana=snd.load(R.raw.nana);
	}
	public void play(int id)
	{
		if(soundAlphabets[id].equalsIgnoreCase("L"))
			snd.play(l);
		else if(soundAlphabets[id].equalsIgnoreCase("M"))
			snd.play(m);
		else if(soundAlphabets[id].equalsIgnoreCase("N"))
			snd.play(n);
		else if(soundAlphabets[id].equalsIgnoreCase("O"))
			snd.play(o);
		else if(soundAlphabets[id].equalsIgnoreCase("P"))
			snd.play(p);
		else if(soundAlphabets[id].equalsIgnoreCase("Q"))
			snd.play(q);
		else if(soundAlphabets[id].equalsIgnoreCase("R"))
			snd.play(r);
		else if(soundAlphabets[id].equalsIgnoreCase("S"))
			snd.play(s);
		else if(soundAlphabets[id].equalsIgnoreCase("T"))
			snd.play(t);
		else if(soundAlphabets[id].equalsIgnoreCase("U"))
			snd.play(u);
		else if(soundAlphabets[id].equalsIgnoreCase("V"))
			snd.play(v);
		else if(soundAlphabets[id].equalsIgnoreCase("W"))
			snd.play(w);
		else if(soundAlphabets[id].equalsIgnoreCase("X"))
			snd.play(x);
		else if(soundAlphabets[id].equalsIgnoreCase("Y"))
			snd.play(y);
		else if(soundAlphabets[id].equalsIgnoreCase("Z"))
			snd.play(z);
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
		//
	     if(sb.toString().length()==0)
	     sb.append("No mistakes");
	     savescore(sb.toString());
	     //
	     
		int DELAY = 1000;
	    Handler handler = new Handler();
	    handler.postDelayed(new Runnable() 
	    {            
	        public void run() 
	        {
	            Intent intent = new Intent(LevelMediumScreen2.this, LevelMediumGameOver.class);
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
	    
	    public void savescore(String data)
	    {
	    	int id;
	    	SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
	    	String playerName= sharedPref.getString("playerName", null);
	    	id= sharedPref.getInt("playerID",0);
	    	//id=Integer.parseInt(playerID);
	    	
	    	System.out.println("Player Name at Level1:="+playerName);
	    	System.out.println("Player Id="+id);
	    	if(playerName!=null&&playerName.length()>0)
	    	{
	    		mDbHelper.createDatabase();       
	    		mDbHelper.open(); 
	    		mDbHelper.updateLevel(id, data,"level2"); 
	    		mDbHelper.close();
	    	}
	    }
	}