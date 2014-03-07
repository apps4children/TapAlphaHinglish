package com.iitk;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import com.iitk.hindi.R;

public class Settings extends PreferenceActivity
{
@Override
protected void onCreate(Bundle savedInstanceState) 
{
   super.onCreate(savedInstanceState);
   addPreferencesFromResource(R.xml.settings);
   
   Preference button = (Preference)getPreferenceManager().findPreference("exitlink");      
   if(button!= null) 
   {
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() 
        {

        	public boolean onPreferenceClick(Preference arg0)
        	{
                        finish();   
                        return true;
             }
        });     
   }
}
}
