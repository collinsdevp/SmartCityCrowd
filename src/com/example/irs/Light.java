package com.example.irs;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.view.MenuItem;
import android.widget.TextView;

public class Light extends Activity implements SensorEventListener {
    private SensorManager mgr;
    private Sensor light;
    private TextView text,text1;
    private StringBuilder msg = new StringBuilder(2048);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        light = mgr.getDefaultSensor(Sensor.TYPE_LIGHT);
        
        text = (TextView) findViewById(R.id.text);
        text1 = (TextView) findViewById(R.id.textView1);
        
        ActionBar ab = getActionBar(); 
	       ab.setDisplayHomeAsUpEnabled(true);
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
	    switch (item.getItemId()) {
	        case android.R.id.home:

	        	Intent intent = new Intent(this,MainActivity.class);
	         	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	startActivity(intent);
	            return true;
	            default:
	            return super.onOptionsItemSelected(item); 
	    }
	}

    @Override
    protected void onResume() {
        mgr.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
      super.onResume();
    }

    @Override
    protected void onPause() {
        mgr.unregisterListener(this, light);
      super.onPause();
    }

  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    msg.insert(0, sensor.getName() + " accuracy changed: " + accuracy +
        (accuracy==1?" (LOW)":(accuracy==2?" (MED)":" (HIGH)")) + "\n");
    text.setText(msg);
    text.invalidate();
  }

  public void onSensorChanged(SensorEvent event) {
    text1.setText("Got a sensor event: " + event.values[0] + " SI lux units\n");
    text.setText(msg);
    text.invalidate();
  }
}