package com.example.irs;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {

	EditText ed1,ed2;
	 StringBuilder sb;
	 public static  CharSequence result;
	  public static HttpResponse response;
	   public static String   response1;
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		 ActionBar ab = getActionBar(); 
	       ab.setDisplayHomeAsUpEnabled(true);
	       
	       ed1 = (EditText)findViewById(R.id.editText1);
	       ed2 = (EditText)findViewById(R.id.editText2);
	

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
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
	
	public void Intent_Back(View view){
		
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);

	}
	
	public void Intent_Login(View view){
		
		QueryToDatabase(ed1.getText().toString(),ed2.getText().toString());

		
	}
	
	private void Main_Intent(String username){
		
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		i.putExtra("usernameKey",username);
		startActivity(i);
	}

	public void showMessage(String title,String message)
	{
		Builder builder=new Builder(this);
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.show();
	}	
	
	
	private void QueryToDatabase(final String  username, final String pass1){
	    class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
	        @Override
	        protected String doInBackground(String... params) {

	            InputStream is = null;

	            try {
	                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	                nameValuePairs.add(new BasicNameValuePair("username", username));
	                nameValuePairs.add(new BasicNameValuePair("pass1", pass1));
	                HttpClient httpClient = new DefaultHttpClient();
	               HttpPost httpPost = new HttpPost("https://x.com/.php");
	                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	                ResponseHandler<String> responseHandler = new BasicResponseHandler(); 
			    
			    // getting the response for php online
	                response1 = httpClient.execute(httpPost, responseHandler);
	     			
	     				
	                if(response1.equals("Auth bad")){
	                	 runOnUiThread(new Runnable() {
	         		        public void run() {
	         		         Toast.makeText(Login.this,"Login Failed", Toast.LENGTH_SHORT).show(); 
	         		        }
	         		    });
	         		    
	    			}else {
	    				runOnUiThread(new Runnable() {
	         		        public void run() {
	         		         Toast.makeText(Login.this,"Login Successful", Toast.LENGTH_SHORT).show(); 
	         		        }
	         		    });
	    				Main_Intent(response1);
	    			}
			         
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
	    sendPostReqAsyncTask.execute(username, pass1);
	}
	
		
}
