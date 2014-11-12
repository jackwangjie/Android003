package com.tcxy.httpclient;

import java.io.IOException;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView textView;
	private Button button;
	private String result="";
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {

			if(result!=null){
				textView.setText((String)msg.obj);
			}
			super.handleMessage(msg);
		}




	};



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView=(TextView) findViewById(R.id.tv_content);
		button=(Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {



			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(){

					public void run(){
						String target="http://www.baidu.com";
						HttpClient httpclient = new DefaultHttpClient();
						HttpGet httpRequest = new HttpGet(target);
						HttpResponse httpReponse;

						try {
							httpReponse=httpclient.execute(httpRequest);
							if(httpReponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
								result=EntityUtils.toString(httpReponse.getEntity());
							}
							else{

								result="«Î«Û ß∞‹";
							}
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						Message msg = new Message();
						msg.obj=result;
						handler.sendMessage(msg);


					}



				}.start();

			}
		});



	}

}
