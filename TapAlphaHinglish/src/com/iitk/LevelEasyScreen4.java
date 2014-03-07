package com.iitk;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.TreeSet;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.iitk.database.TestAdapter;
import com.iitk.hindi.R;
public class LevelEasyScreen4 extends Activity implements AdapterView.OnItemClickListener 
{
	StringBuilder sb = new StringBuilder();
	TestAdapter mDbHelper;
	String wrongQuestion=null;
	
	int counter=0,truecounter=0,level=3,wrongCounter=0;
	TextView selection,dailogView,blinkText;
	String[] alphabet,sortedAlphabet;
	GridView gv ;
	final Context context = this;
	Dialog dialog;
	
	//sound
	SoundManager snd;
	int s,t,u,v,w,x,y,z,nana;
	
	@Override
	public void onCreate(Bundle icicle) 
	{
		super.onCreate(icicle);requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game);
		
		mDbHelper = new TestAdapter(this);
		
		alphabet=randomCharacter();
		selection = (TextView) findViewById(R.id.selection);
		gv= (GridView) findViewById(R.id.grid);
		gv.setAdapter(new ImageAdapter(this));
		gv.setOnItemClickListener(this);
		Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fly_in_from_top_corner);
        gv.setAnimation(anim);
        anim.start();
		
		snd = new SoundManager(getApplicationContext());
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        s=snd.load(R.raw.s);
        t=snd.load(R.raw.t);
        u=snd.load(R.raw.u);
        v=snd.load(R.raw.v);
        w=snd.load(R.raw.w);
        x=snd.load(R.raw.x);
        y=snd.load(R.raw.y);
        z=snd.load(R.raw.z);
        nana=snd.load(R.raw.nana);
        
        Bundle extras = getIntent().getExtras();  
        String wrong= extras.getString("wrong"); 
        sb.append(wrong);
        
        Button back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v) 
        	{
        	finish();
        }});
        
	}
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
	{
		selection.setText(alphabet[position]);
		selection.setTextColor(Color.parseColor(LevelEasyScreen1.AllGradients().get(position)));
		if(!alphabet[position].equals(sortedAlphabet[counter]))
		{
	    ++wrongCounter;
	    snd.play(nana);
		//v.setBackgroundColor(Color.RED);
	    //Drawable background = this.getResources().getDrawable(R.drawable.wrong_border); 
		//v.setBackgroundDrawable(background);
		alert();
		//
		   if(wrongQuestion==null){
			  //sb.append("\n");
			  wrongQuestion=sortedAlphabet[counter];
			  sb.append(sortedAlphabet[counter]+"-"+alphabet[position]);
		   }
		   else if(wrongQuestion.equalsIgnoreCase(sortedAlphabet[counter])){
			   sb.append(","+alphabet[position]);
		   }
		   else if(!wrongQuestion.equalsIgnoreCase(sortedAlphabet[counter])){
			   //sb.append("\n");
			   wrongQuestion=sortedAlphabet[counter];
			   sb.append(sortedAlphabet[counter]+"-"+alphabet[position]);  
		   }
		  //
		}
		else if(alphabet[position].equals(sortedAlphabet[counter]))
		{
			//
			if(wrongQuestion!=null&&wrongQuestion.equalsIgnoreCase(sortedAlphabet[counter]))
			sb.append(","+alphabet[position]);
			else
			sb.append(sortedAlphabet[counter]+"-"+alphabet[position]);
			sb.append("\n");
  		    //
			if(alphabet[position].equalsIgnoreCase((String) blinkText.getText()))               //
			{                  //Clear animation
			v.clearAnimation();//
			}                  //
			
			play(position);
			v.setBackgroundColor(Color.TRANSPARENT);
			v.setOnClickListener(null);
			++truecounter;
			if(truecounter==8)
			{
				newScreen();
			}
			counter++;
		}	
	}
	public String[] randomCharacter()
	{
		String [] arr;
		SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String alphaType=sharedPreferences.getString("alpha","capital");
        if(alphaType.equals("capital"))
        {
	    arr=new String[]{"S","T","U","V","W","X","Y","Z"};	
        }
        else 
        arr=new String[]{"s","t","u","v","w","x","y","z"};
        Random random = new Random();
        LinkedHashSet<String> str=new LinkedHashSet<String>();
        while(str.size()<8)
        {
        	int select = random.nextInt(arr.length); 
            str.add(arr[select]);
	    }
        String[] alphabet = str.toArray(new String[0]);
        TreeSet<String> sorted=new TreeSet<String>();
        sorted.addAll(str);
        sortedAlphabet=sorted.toArray(new String[0]);
		return alphabet;
	}
	public void alert()
	{
		dialog = new Dialog(LevelEasyScreen4.this);
		dialog.setContentView(R.layout.dailog);
		dailogView= (TextView) dialog.findViewById(R.id.dialogView);
		dialog.setTitle("Click Below");
		dialog.setCancelable(true);
		dailogView.setText(sortedAlphabet[counter]);
		
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
	public void play(int id)
	{
		if(alphabet[id].equals("S"))
			snd.play(s);
		else if(alphabet[id].equalsIgnoreCase("T"))
			snd.play(t);
		else if(alphabet[id].equalsIgnoreCase("U"))
			snd.play(u);
		else if(alphabet[id].equalsIgnoreCase("V"))
			snd.play(v);
		else if(alphabet[id].equalsIgnoreCase("W"))
			snd.play(w);
		else if(alphabet[id].equalsIgnoreCase("X"))
			snd.play(x);
		else if(alphabet[id].equalsIgnoreCase("Y"))
			snd.play(y);
		else if(alphabet[id].equalsIgnoreCase("Z"))
			snd.play(z);
	}
	public void newScreen()
	{	
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
	            Intent intent = new Intent(LevelEasyScreen4.this, LevelEasyGameOver.class);
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
	    return 8;
		}
		public View getView(int position, View convertView, ViewGroup parent){
		View MyView = convertView;
		if( convertView == null )
		{
		LayoutInflater li = getLayoutInflater();
		MyView = li.inflate(R.layout.cell, null);
		}
		TextView tv = (TextView)MyView.findViewById(R.id.gridcell);
		tv.setText(alphabet[position]);
		tv.setTextColor(Color.parseColor(LevelEasyScreen1.AllGradients().get(position)));
		
		String str=(String) tv.getText();
	    if(str.equalsIgnoreCase(sortedAlphabet[0]))
	    {
	    blink(tv);
	    }
		return MyView;
		}
		public Object getItem(int arg0) {
			return null;
		}
		public long getItemId(int arg0) {
			return 0;
		}
	}
	public static ArrayList<String> AllGradients(){
        ArrayList<String> list = new ArrayList<String>();
        list.add("#006400");
        list.add("#DA70D6");
        list.add("#9400D3");
        list.add("#FE0388");
        
        list.add("#0099CC");
        list.add("#D9007E");
        list.add("#2F4F4F");
        list.add("#731134");    
        return list;
    }
	
	public void blink(TextView tv)
	{
		blinkText=tv;
		Animation animBlink= new AlphaAnimation(0.0f, 1.0f);
        animBlink.setDuration(500); //You can manage the time of the blink with this parameter
        animBlink.setStartOffset(100);
        animBlink.setRepeatMode(Animation.REVERSE);
        animBlink.setRepeatCount(Animation.INFINITE);
		blinkText.startAnimation(animBlink);
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
    		mDbHelper.updateLevel(id, data,"level1"); 
    		mDbHelper.close();
    	}
    }
}