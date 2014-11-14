package com.tcxy.httpclient2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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
	String result="";
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			System.out.println("���յ�����Ϣ");
			textView.setText((String)msg.obj); // ��ʾ��õĽ��
			super.handleMessage(msg);
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView=(TextView) findViewById(R.id.tv_result);
		button = (Button) findViewById(R.id.button1); 
		// Ϊ��ť��ӵ����¼�������
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// ����һ�����̣߳����ڴ������ϻ�ȡ�ļ�
				new Thread(new Runnable() {
					public void run() {
						try {
							send();
						} catch (Exception e) {

							e.printStackTrace();
						}
						Message m = handler.obtainMessage(); // ��ȡһ��Message
						m.obj=result;
						handler.sendMessage(m); // ������Ϣ
					}
				}).start(); // �����߳�

			}
		});
	}

	public void send() throws JSONException, UnsupportedEncodingException {
		String target = "http://www.fuhehr.com/user/schBaseLogin";	//Ҫ�ύ��Ŀ���ַ
		HttpClient httpclient = new DefaultHttpClient();	//����HttpClient����
		HttpPost httpRequest = new HttpPost(target);	//����HttpPost����
		//��Ҫ���ݵĲ������浽List������
		//List<NameValuePair> params = new ArrayList<NameValuePair>();
		//params.add(new BasicNameValuePair("param", "post"));	//��ǲ���
		JSONObject schBase = new JSONObject();
		JSONObject nameAndPasword = new JSONObject();
		nameAndPasword.put("name", "nanjing");
		nameAndPasword.put("pwd", "nanjing");
		schBase.put("schBase", nameAndPasword);
		StringEntity s = new StringEntity(schBase.toString());
		s.setContentType("application/json");
		s.setContentEncoding("UTF-8");
		//params.add(new BasicNameValuePair("name", "nanjing"));	//�ǳ�
		//params.add(new BasicNameValuePair("pwd", "nanjing"));	//����
		try {

			httpRequest.setEntity(s);

			//httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //���ñ��뷽ʽ
			HttpResponse httpResponse = httpclient.execute(httpRequest);	//ִ��HttpClient����
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
				HttpEntity entity = httpResponse.getEntity();
				InputStream inputStream = entity.getContent();

				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader reader = new BufferedReader(inputStreamReader);// ���ַ����õġ�
				result= reader.readLine();
				System.out.println(result);
				reader.close();// �ر�������
			}else{
				System.out.println("������");
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();	//����쳣��Ϣ
		} catch (ClientProtocolException e) {
			e.printStackTrace();	//����쳣��Ϣ
		} catch (IOException e) {
			e.printStackTrace();	//����쳣��Ϣ
		}
	}
}