package com.example.irs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {

	 StringBuilder sb = null;
	 TextView tv4;
	 EditText ed1,ed2, ed3;
	 String result = null;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		 ActionBar ab = getActionBar(); 
	       ab.setDisplayHomeAsUpEnabled(true);
	       
	       tv4= (TextView)findViewById(R.id.textView4);
	       ed1  = (EditText)findViewById(R.id.editText1);
	    	ed2  = (EditText)findViewById(R.id.editText2);
	       ed3 = (EditText)findViewById(R.id.editText3);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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
	
	public void Intent_Register(View view){
		insertToDatabase(ed1.getText().toString(), ed2.getText().toString(),ed3.getText().toString());
		
		if(ed1.getText().toString().equals("") || ed2.getText().toString().equals("") || ed3.getText().toString().equals("")){
			  showMessage("Error","Empty field");
			  
		}else{
			if(ed2.getText().toString().equals(ed3.getText().toString())){
				runOnUiThread(new Runnable() {
     		        public void run() {
     		         Toast.makeText(Register.this,"Registration Complete", Toast.LENGTH_SHORT).show(); 
     		        }
     		    });
		  ed1.setText("");
		  ed2.setText("");
		  ed3.setText("");

			}else{
				 showMessage("Error","passwords does not match");
			}
		}
		
		}
	
	public void Intent_Back(View view){
		
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
	}
	
	public void showMessage(String title,String message)
	{
		Builder builder=new Builder(this);
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.show();
	}	   
		
		
	
	private void insertToDatabase(final String  username, final String pass1,final String pass2){
	    class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
	        @Override
	        protected String doInBackground(String... params) {

	            InputStream is = null;

	            try {
	                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	                nameValuePairs.add(new BasicNameValuePair("username", username));
	                nameValuePairs.add(new BasicNameValuePair("pass1", pass1));
	                nameValuePairs.add(new BasicNameValuePair("pass2", pass2));
	                HttpClient httpClient = new DefaultHttpClient();
	               HttpPost httpPost = new HttpPost("https://irscloud.000webhostapp.com/.php");
	                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	                HttpResponse response = httpClient.execute(httpPost);

	                HttpEntity entity = response.getEntity();
	                is = entity.getContent();
	                
	                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			         sb = new StringBuilder();
			         sb.append(reader.readLine());
			         result=sb.toString();
			         
			     
	            } catch (ClientProtocolException e) {

	            } catch (IOException e) {

	            }
	            
	    	
	            return "Transfer Successful";
	        }

	        @Override
	        protected void onPostExecute(String result) {
	            super.onPostExecute(result);
	           
	          //  Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
	            
	            // showMessage("Transfer", result);
	        }
	    }
	    SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
	    sendPostReqAsyncTask.execute(username, pass1, pass2);
	}
}
