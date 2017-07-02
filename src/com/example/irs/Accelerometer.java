package com.example.irs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.widget.TextView;


import android.app.AlertDialog.Builder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Accelerometer extends Activity  implements SensorEventListener {
   
private SensorManager mSensorManager;
private Sensor mAccelerometer;


public static 	SQLiteDatabase db;
public static TextView tv1,tv3,tv5,tv6,tv7,tv8,tv9,tv10;
public static Button start;
public EditText ed1;
public static Timer myTimer;
public static  float[] sens;


public static double[] loc={0,0};

private LocationManager mManager;
private Location mCurrentLocation;

List<NameValuePair> nameValuePairs;


private long startTime = 0L;
public static long counter=0l;
private Handler customHandler = new Handler();

long timeInMilliseconds = 0L;
long timeSwapBuff = 0L;
long updatedTime = 0L;

public static String value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_accelerometer);
		 ActionBar ab = getActionBar(); 
	       ab.setDisplayHomeAsUpEnabled(true);
		Toast.makeText(this, "Accelerometer", Toast.LENGTH_SHORT).show();
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		tv1 = (TextView)findViewById(R.id.textView1);
		tv3 = (TextView)findViewById(R.id.textView3);
		tv5 = (TextView)findViewById(R.id.textView5);
		tv6 = (TextView)findViewById(R.id.textView6);
		tv7 = (TextView)findViewById(R.id.textView7);
		tv8 = (TextView)findViewById(R.id.textView8);
		ed1 = (EditText)findViewById(R.id.editText1);
		
		
		tv9  = (TextView)findViewById(R.id.textView9); 
		tv10  = (TextView)findViewById(R.id.textView10);
		
		tv5.setText("0:00:000");
	   
		
		 db= openOrCreateDatabase("/data/data/com.example.irs/ReadingsDB.db", Context.MODE_PRIVATE, null);
		  db.execSQL("DROP TABLE IF EXISTS reading");
		db.execSQL("CREATE TABLE IF NOT EXISTS reading(counter varchar, sec VARCHAR, x VARCHAR, y VARCHAR,z VARCHAR, lat VARCHAR, lon VARCHAR, datetime VARCHAR, altitude VARCHAR);");
		
		mManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		 Bundle extras = getIntent().getExtras();
         if (extras != null) {
              value = extras.getString("usernameKey");
              
              // get macid without :
              
              value = value.replace(":", ""); 
         }
       
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

	 protected void onResume() {
	        super.onResume();
	        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
	        if(!mManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	            AlertDialog.Builder builder = new AlertDialog.Builder(this);
	            builder.setTitle("Location Manager");
	            builder.setMessage("We would like to use your location, "
	                    + "but GPS is currently disabled.\n"
	                    + "Would you like to change these settings now?");
	            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                    //Launch settings, allowing user to make a change
	                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                    startActivity(i);
	                }
	            });
	            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                    //No location service, no Activity
	                    finish();
	                }
	            });
	            builder.create().show();
	        }

	        mCurrentLocation = mManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	        updateDisplay();

	        int minTime = 0;
	        float minDistance = 0;
	        mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mListener);
	        
	    }

	    protected void onPause() {
	        super.onPause();
	        mSensorManager.unregisterListener(this);
	        mManager.removeUpdates(mListener);
	    }

	    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

	    public void onSensorChanged(SensorEvent event) {
	    	
	    	 
	        final float[] values = event.values;

	        sens = event.values;

	        
	       tv1.setText(String.format("X: %1$1.2f   Y: %2$1.2f   Z: %3$1.2f", values[0],values[1],values[2]));
	      
	    }
	    
	    public void Intent_Start(View view){
	 
	    startTime = 0L;
	   	 timeInMilliseconds = 0L;
	   	 timeSwapBuff = 0L;
	   	 updatedTime = 0L;

	    	 MyTimerTask myTask = new MyTimerTask();
	    	 
	    	 String regexStr1 = "^0[0-9]*$";
	    	 
	 	   	boolean digitsOnly = TextUtils.isDigitsOnly(ed1.getText());
	 	   	
	    	 if(ed1.getText().toString().trim().isEmpty() || ed1.getText().toString().trim().matches(regexStr1) || !digitsOnly)
	    	 {
	 
	    		 showMessage("Error", "Invalid input  ");
	    	 }
	    	 else{
	    			 
	    		 if(ed1.getText().toString().equals("5")){
	 	    		tv5.setText("0:00:001");
		 	    	startTime = SystemClock.uptimeMillis();
		 			customHandler.postDelayed(updateTimerThread, 0);
		    		 Toast.makeText(this, "Recording Started ... ", Toast.LENGTH_SHORT).show();
		    		 Long frequency = Long.parseLong("190");
		    		 myTimer = new Timer();
		 	         myTimer.schedule(myTask, 0,frequency);
	    		 }else if(ed1.getText().toString().equals("10")){
		 	    		tv5.setText("0:00:001");
			 	    	startTime = SystemClock.uptimeMillis();
			 			customHandler.postDelayed(updateTimerThread, 0);
			    		 Toast.makeText(this, "Recording Started ... ", Toast.LENGTH_SHORT).show();
			    		 Long frequency = Long.parseLong("32");
			    		 myTimer = new Timer();
			 	         myTimer.schedule(myTask, 0,frequency);
	    		 }else if(ed1.getText().toString().equals("20")){
		 	    		tv5.setText("0:00:001");
			 	    	startTime = SystemClock.uptimeMillis();
			 			customHandler.postDelayed(updateTimerThread, 0);
			    		 Toast.makeText(this, "Recording Started ... ", Toast.LENGTH_SHORT).show();
			    		 Long frequency = Long.parseLong("31");
			    		 myTimer = new Timer();
			 	         myTimer.schedule(myTask, 0,frequency);
	    		 }
	    		 else if(ed1.getText().toString().equals("25")){
		 	    		tv5.setText("0:00:001");
			 	    	startTime = SystemClock.uptimeMillis();
			 			customHandler.postDelayed(updateTimerThread, 0);
			    		 Toast.makeText(this, "Recording Started ... ", Toast.LENGTH_SHORT).show();
			    		 Long frequency = Long.parseLong("32");
			    		 myTimer = new Timer();
			 	         myTimer.schedule(myTask, 0,frequency);
	    		 }
	    		 else if(ed1.getText().toString().equals("30")){
		 	    		tv5.setText("0:00:001");
			 	    	startTime = SystemClock.uptimeMillis();
			 			customHandler.postDelayed(updateTimerThread, 0);
			    		 Toast.makeText(this, "Recording Started ... ", Toast.LENGTH_SHORT).show();
			    		 Long frequency = Long.parseLong("20");
			    		 myTimer = new Timer();
			 	         myTimer.schedule(myTask, 0,frequency);
	    		 }else if(ed1.getText().toString().equals("35")){
		 	    		tv5.setText("0:00:001");
			 	    	startTime = SystemClock.uptimeMillis();
			 			customHandler.postDelayed(updateTimerThread, 0);
			    		 Toast.makeText(this, "Recording Started ... ", Toast.LENGTH_SHORT).show();
			    		 Long frequency = Long.parseLong("1");
			    		 myTimer = new Timer();
			 	         myTimer.schedule(myTask, 0,frequency);
	    		 }else{
	    			 
	    			 showMessage("Freq Error", "Out of boundary");
	    		 }
	    	}
	         
	    }
	    
public void Intent_Stop(View view){
			counter=0l;
			timeSwapBuff += timeInMilliseconds;
			customHandler.removeCallbacks(updateTimerThread);
	    	Toast.makeText(this, "Recording Stopped ... ", Toast.LENGTH_SHORT).show();
	    	  
	    	Cursor c=db.rawQuery("SELECT * FROM reading", null);
    		if(c.getCount()==0)
    		{
    			showMessage("Error", "No records found");
    			return;
    		}
    		StringBuffer buffer=new StringBuffer();
	
    		buffer.append("Count   "+"Time     "+"   X     " +"      Y     "+"     Z 	 "+"\n");
    		while(c.moveToNext())
    		{
    			
    			buffer.append(c.getString(0)+" | "+c.getString(1)+" | "+""+c.getString(2)+" | "+""+c.getString(3)+" | "+""+c.getString(4)+"\n");
    			counter++;
    		}
    		tv8.setText("Number of records : "+Long.toString(counter));
    		showMessage("Accelerometer Data", buffer.toString());
    		myTimer.cancel(); //
    		
}
	    
public void showMessage(String title,String message)
{
	Builder builder=new Builder(this);
	builder.setCancelable(true);
	builder.setTitle(title);
	builder.setMessage(message);
	builder.show();
}	   
	

public void Intent_Reset(View view){
	counter=0l;
	timeSwapBuff += timeInMilliseconds;
	customHandler.removeCallbacks(updateTimerThread);
	counter=0l;
	tv5.setText("0:00:000");
	tv8.setText("Number of records : "+Long.toString(counter));
	 startTime = 0L;
   	 timeInMilliseconds = 0L;
   	 timeSwapBuff = 0L;
   	 updatedTime = 0L;
	if(myTimer !=null){
	myTimer.cancel();
	}
	Accelerometer.db.execSQL("delete from  reading;");
	Accelerometer.db.execSQL("vacuum;");
	
	Toast.makeText(this, "Record reset", Toast.LENGTH_SHORT).show();
}

public void Intent_Transfer(View view){
	  
	Toast.makeText(this, "Transfering ...", Toast.LENGTH_SHORT).show();
	
	Cursor c=db.rawQuery("SELECT * FROM reading", null);
	
	insertToDatabase("1","","","","","","","",""); 
	while(c.moveToNext())
	{
		insertToDatabase(null,c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8));

	}
}

private void updateDisplay() {
    if(mCurrentLocation == null) {
        tv3.setText("Determining Your Location...");
    } else {
    	loc[1] = mCurrentLocation.getLatitude();
    	loc[0] = mCurrentLocation.getLongitude();

    	tv3.setText("");
    	tv6.setText(String.format("Lon: %.8f", mCurrentLocation.getLongitude()));
    	tv7.setText(String.format("Lat: %.8f", mCurrentLocation.getLatitude()));
    	
    	// get elevation from google
    	Elevation_Insert("https://maps.googleapis.com/maps/api/elevation/json?locations="+ mCurrentLocation.getLatitude()+","+mCurrentLocation.getLongitude()+"&key=AIzaSyCEhYHn0AQJC7RyfdhUpi3-ff0Qy44E464");
         
    }
}
private LocationListener mListener = new LocationListener() {
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateDisplay();
    }

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
};


private void insertToDatabase(final String truncate, final String sec, final String x,final String  y, final String z,final String lat, final String lon, final String datetime, final String alt){
    class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //InputStream is = null;
        	
          

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("truncate", truncate));
                nameValuePairs.add(new BasicNameValuePair("sec", sec));
                nameValuePairs.add(new BasicNameValuePair("x", x));
                nameValuePairs.add(new BasicNameValuePair("y", y));
                nameValuePairs.add(new BasicNameValuePair("z", z));
                nameValuePairs.add(new BasicNameValuePair("lon", lon));
                nameValuePairs.add(new BasicNameValuePair("lat", lat));
                nameValuePairs.add(new BasicNameValuePair("datetime", datetime));
                nameValuePairs.add(new BasicNameValuePair("macid", value));
                nameValuePairs.add(new BasicNameValuePair("altitude", alt));
                HttpClient httpClient = new DefaultHttpClient();
               HttpPost httpPost = new HttpPost("https://smartcitycrowd.000webhostapp.com/acc_android.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity entity = response.getEntity();
                //is = entity.getContent();


            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }
            return "Transfer Successful";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        
	}
    }
    SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
    sendPostReqAsyncTask.execute(sec,x,y,z,lon,lat,datetime,alt);
}

//method for timer
private Runnable updateTimerThread = new Runnable() {

	public void run() {

		timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

		updatedTime = timeSwapBuff + timeInMilliseconds;

		int secs = (int) (updatedTime / 1000);
		int mins = secs / 60;
		secs = secs % 60;
		int milliseconds = (int) (updatedTime % 1000);
		tv5.setText("" + mins + ":"
				+ String.format("%02d", secs) + ":"
				+ String.format("%03d", milliseconds));
		customHandler.postDelayed(this, 0);
	}

};

private void Elevation_Insert(final String url){ 
class AsyncTaskParseJson extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... arg0) {
        final String TAG = "AsyncTaskParseJson.java";
        
        // set your json string url here
        String yourJsonStringUrl = url;
 
        // contacts JSONArray
        JSONArray dataJsonArr = null;
    	String ele = null;
        try {

            // instantiate our json parser
            JSONParser jParser = new JSONParser();

            // get json string from url
            JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);

            // get the array of results
            dataJsonArr = json.getJSONArray("results");


                JSONObject c = dataJsonArr.getJSONObject(0);

                // Storing each json item in variable
                 ele = c.getString("elevation");

                // show the values in our logcat
                Log.e(TAG, "Altitude: " + ele);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ele;
        
    }

    @Override
    protected void onPostExecute(String strFromDoInBg) {
    	tv10.setText("Elevation : "+strFromDoInBg+" meters");
    	tv9.setText(strFromDoInBg);
    	  super.onPostExecute(strFromDoInBg);
    }
}
AsyncTaskParseJson sendPostReqAsyncTask = new AsyncTaskParseJson();
sendPostReqAsyncTask.execute(url);
}

}


class MyTimerTask extends TimerTask {
	
	private String getDateTime() {
		Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        mdformat.setTimeZone(TimeZone.getTimeZone("US/Mountain")); 
        String strDate = mdformat.format(calendar.getTime());
    	return strDate;
    	
}

	
	  public void run() {
		  
		  Accelerometer.db.execSQL("INSERT INTO reading(counter,sec,x,y,z,lat,lon,datetime,altitude) VALUES('"+ ++Accelerometer.counter+"','"+Accelerometer.tv5.getText()+"','"+String.format(" %1$1.2f",Accelerometer.sens[0])+"','"+String.format(" %1$1.2f",Accelerometer.sens[1])+"','"+String.format(" %1$1.2f",Accelerometer.sens[2])+"','"+String.format(" %1$1.8f",Accelerometer.loc[1])+"','"+String.format(" %1$1.8f",Accelerometer.loc[0])+"','"+getDateTime()+"','"+Accelerometer.tv9.getText()+"');");
			    
	  }
}


