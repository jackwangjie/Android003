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

			System.out.println("我收到了消息");
			textView.setText((String)msg.obj); // 显示获得的结果
			super.handleMessage(msg);
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView=(TextView) findViewById(R.id.tv_result);
		button = (Button) findViewById(R.id.button1); 
		// 为按钮添加单击事件监听器
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// 创建一个新线程，用于从网络上获取文件
				new Thread(new Runnable() {
					public void run() {
						try {
							send();
						} catch (Exception e) {

							e.printStackTrace();
						}
						Message m = handler.obtainMessage(); // 获取一个Message
						m.obj=result;
						handler.sendMessage(m); // 发送消息
					}
				}).start(); // 开启线程

			}
		});
	}

	public void send() throws JSONException, UnsupportedEncodingException {
		String target = "http://www.fuhehr.com/user/schBaseLogin";	//要提交的目标地址
		HttpClient httpclient = new DefaultHttpClient();	//创建HttpClient对象
		HttpPost httpRequest = new HttpPost(target);	//创建HttpPost对象
		//将要传递的参数保存到List集合中
		//List<NameValuePair> params = new ArrayList<NameValuePair>();
		//params.add(new BasicNameValuePair("param", "post"));	//标记参数
		JSONObject schBase = new JSONObject();
		JSONObject nameAndPasword = new JSONObject();
		nameAndPasword.put("name", "nanjing");
		nameAndPasword.put("pwd", "nanjing");
		schBase.put("schBase", nameAndPasword);
		StringEntity s = new StringEntity(schBase.toString());
		s.setContentType("application/json");
		s.setContentEncoding("UTF-8");
		//params.add(new BasicNameValuePair("name", "nanjing"));	//昵称
		//params.add(new BasicNameValuePair("pwd", "nanjing"));	//内容
		try {

			httpRequest.setEntity(s);

			//httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //设置编码方式
			HttpResponse httpResponse = httpclient.execute(httpRequest);	//执行HttpClient请求
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//如果请求成功
				HttpEntity entity = httpResponse.getEntity();
				InputStream inputStream = entity.getContent();

				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader reader = new BufferedReader(inputStreamReader);// 读字符串用的。
				result= reader.readLine();
				System.out.println(result);
				reader.close();// 关闭输入流
			}else{
				System.out.println("出错了");
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();	//输出异常信息
		} catch (ClientProtocolException e) {
			e.printStackTrace();	//输出异常信息
		} catch (IOException e) {
			e.printStackTrace();	//输出异常信息
		}
	}
}