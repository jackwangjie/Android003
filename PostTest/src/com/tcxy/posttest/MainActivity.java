package com.tcxy.posttest;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button button;
	private TextView textView;

	private Handler handler =new Handler(){

		@Override
		public void handleMessage(Message msg) {
			textView.setText((String)msg.obj);
			System.out.println("我收到了消息");// 显示获得的结果
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textView=(TextView) findViewById(R.id.tv_post);
		button=(Button) findViewById(R.id.post);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 创建一个新线程，用于从网络上获取文件
				new Thread(new Runnable() {
					public void run() {
						try {
							URL url=new URL("http://www.fuhehr.com/user/schBaseLogin");
							HttpURLConnection s = (HttpURLConnection) url.openConnection();
							s.setRequestMethod("POST");
							s.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
							s.setRequestProperty("Accept-Encoding","UTF-8");
							s.setRequestProperty("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
							s.setRequestProperty("Content-Type", "application/json");
							s.setRequestProperty("Content-Length","46");
							s.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; rv:33.0) Gecko/20100101 Firefox/33.0");
							//conn.setRequestProperty("Content-Type", "application/json");
							s.setDoInput(true); // 向连接中写入数据
							s.setDoOutput(true); // 从连接中读取数据
							JSONObject schBase = new JSONObject();
							JSONObject nameAndPasword = new JSONObject();
							nameAndPasword.put("name", "nanjing");
							nameAndPasword.put("pwd", "nanjing");
							schBase.put("schBase", nameAndPasword);
							OutputStream out = new BufferedOutputStream(s.getOutputStream());
							out.write(schBase.toString().getBytes());
							Log.i("MYLOG",Integer.toString(schBase.toString().length()));
							//out.close();
							//out.writeBytes(param);//将要传递的数据写入数据输出流
							out.flush();	//输出缓存
							out.close();	//关闭数据输出流
							if (s.getResponseCode() == HttpURLConnection.HTTP_OK) {
								InputStream in = s.getInputStream();

								BufferedReader bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));

								Message msg = new Message();

								String temp = null;

								StringBuffer sb = new StringBuffer();

								while((temp=bf.readLine())!=null)
								{
									sb.append(temp);	        
								}
								msg.obj = sb.toString();
								handler.sendMessage(msg);
								in.close();
							}
							s.disconnect();	//断开连接
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start(); // 开启线程

			}
		} );

	}


}
