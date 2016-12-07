package com.example.mobilesafe.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.StreamUtil;


public class SplashActivity extends Activity {

	static final String tag = "SplashActivity";
    private TextView tv_version_name;
	private int mLocalVersionCode;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        
        //初始化UI
        initUI();
        
        //初始化数据
        initData();
    }

	/**
	 * 初始化数据方法
	 */
	private void initData() {
		//1.应用版本名称
		tv_version_name.setText("版本名称:" + getVersionName());
		//2,获取本地版本号
		mLocalVersionCode = getVersionCode();
		//3,获取服务器版本号(客户端发请求,服务端给响应,(json,xml))
		//http://www.oxxx.com/update74.json?key=value  返回200 请求成功,流的方式将数据读取下来
		//json中内容包含:
		/* 更新版本的版本名称
		 * 新版本的描述信息
		 * 服务器版本号
		 * 新版本apk下载地址*/
		
		checkVersion();
	}
	
	/**
	 * 检测版本号
	 */
	private void checkVersion() {
		new Thread(){
			public void run() {	
				//发送请求获取数据  ,参数是请求json的链接地址
				//http://192.168.1.2/versioninfo.json 测试阶段不是最优
				//10.0.2.2 仅限于模拟器访问电脑的tomcat
				try {
					URL url = new URL("http://10.0.2.2:8080/versioninfo.json");
					//1. 开启一个连接
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					//2. 设置常见的请求参数(请求头)
					
					//请求超时
					connection.setConnectTimeout(2000);
					//读取超时
					connection.setReadTimeout(2000);
					
					//3. 默认就是GET请求
//					connection.setRequestMethod("POST");
					
					//4. 获取请求成功响应码
					if(connection.getResponseCode() == 200){
						//5. 以流的形式,将数据取出来
						InputStream inputStream = connection.getInputStream();
						//6. 将流转成字符串
						String json = StreamUtil.stream2String(inputStream);
						Log.i(tag, json);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
		
	}

	/**
	 * 返回版本号
	 * @return 非0, 代表获取成功
	 */
	private int getVersionCode(){
		//1,包管理者对象packageManager
		PackageManager pm = getPackageManager();
		try {
			//2,从包的管理者对象中,获取指定包名的基本信息(版本名称,版本号),传0代表获取基本信息
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			//3,获取版本名称
			return info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return 0;
	}
	
	/**
	 * 获取版本名称，从清单文件中
	 * @return 应用的版本名称，返回null表示异常
	 */
	private String getVersionName(){
		//1,包管理者对象packageManager
		PackageManager pm = getPackageManager();
		try {
			//2,从包的管理者对象中,获取指定包名的基本信息(版本名称,版本号),传0代表获取基本信息
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			//3,获取版本名称
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}

	/**
	 * 初始化UI方法
	 */
	private void initUI() {
		tv_version_name = (TextView) findViewById(R.id.tv_version_name);
	}
    
    
}
