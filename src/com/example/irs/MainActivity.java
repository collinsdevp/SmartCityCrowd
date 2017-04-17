package com.example.irs;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity {

	public final static String Accelerometer_pkg = "com.example.irs.Accelerometer";
	public final static String Orientation_pkg = "com.example.irs.Orientation";
	
	TextView tv;
	 String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.textView2);
   
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
public void Intent_Accelerometer(View view){
	

	if (!tv.getText().equals("")){
		Intent i = new Intent(getApplicationContext(), Accelerometer.class);
		i.putExtra("usernameKey",getMacAddress(this));
		startActivity(i);
	}else{
		Intent i = new Intent(getApplicationContext(), Accelerometer.class);
		startActivity(i);
	}

    	
    }

public void Intent_Orientation(View view){
	
	Intent intent = new Intent(this,Orientation.class);
	
	//String  data = "123".toString();
	
//	intent.putExtra(Orientation_pkg, data);
	
	startActivity(intent);
}


public void Intent_Register(View view){
	
	Intent intent = new Intent(this,Register.class);
	
	//String  data = "123".toString();
	
//	intent.putExtra(Orientation_pkg, data);
	
	startActivity(intent);
}

public void Get_Mac(View view){
	
	tv.setText("MAC ID: "+getMacAddress(this));
}


public String getMacAddress(Context context) {
    WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    String macAddress = wimanager.getConnectionInfo().getMacAddress();
    if (macAddress == null) {
        macAddress = "Device don't have mac address or wi-fi is disabled";
    }
    return macAddress;
}


}