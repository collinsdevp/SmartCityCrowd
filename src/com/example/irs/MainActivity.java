package com.example.irs;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

	public final static String Accelerometer_pkg = "com.example.irs.Accelerometer";
	public final static String Orientation_pkg = "com.example.irs.Orientation";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    	
    	Intent intent = new Intent(this,Accelerometer.class);
    	
    	//String  data = "123".toString();
    	
    //	intent.putExtra(Accelerometer_pkg, data);
    	
    	startActivity(intent);
    }

public void Intent_Orientation(View view){
	
	Intent intent = new Intent(this,Orientation.class);
	
	//String  data = "123".toString();
	
//	intent.putExtra(Orientation_pkg, data);
	
	startActivity(intent);
}

public void Intent_Light(View view){
	
	Intent intent = new Intent(this,Light.class);
	
	//String  data = "123".toString();
	
//	intent.putExtra(Accelerometer_pkg, data);
	
	startActivity(intent);
}

}