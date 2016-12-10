package com.example.mobilesafe.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.StreamUtil;
import com.example.mobilesafe.utils.ToastUtil;


public class SplashActivity extends Activity {

	static final String tag = "SplashActivity";
    private TextView tv_version_name;
	private int mLocalVersionCode;
	private String mVersionDes;

	/**
	 * 更新新版本的状态码
	 */
	protected static final int UPDATE_VERSION = 100;
	/**
	 * 进入程序主界面状态码
	 */
	protected static final int ENTER_HOME = 101;
	
	protected static final int URL_ERROR = 103;
	protected static final int IO_ERROR = 104;
	protected static final int JSON_ERROR = 105;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_VERSION:
				//弹出对话框，提示用户更新
				showUpdateDialog();
				break;
				
			case ENTER_HOME:
				//进入应用程序主界面,activity跳转过程
				enterHome();
				break;
				
			case URL_ERROR:
				ToastUtil.show(getApplicationContext(), "url地址异常");
				enterHome();
				break;
				
			case IO_ERROR:
				ToastUtil.show(getApplicationContext(), "IO地址异常");
				enterHome();
				break;
				
			case JSON_ERROR:
				ToastUtil.show(getApplicationContext(), "JSON地址异常");
				enterHome();
				break;
			}
		};
	};
	private RelativeLayout rl_root_splash;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        
        //初始化UI
        initUI();
        
        //初始化数据
        initData();
        
        //初始化动画
        initAnimation();
    }

	
	/**
	 * 淡入的动画效果
	 */
	private void initAnimation() {
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(3000);
		
		rl_root_splash.setAnimation(alphaAnimation);
	}

	/**
	 * 弹出对话框，提示用户更新
	 */
	protected void showUpdateDialog() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("版本更新");
		builder.setMessage(mVersionDes);
		
		//积极按钮，立即更新
		builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//下载最新版本
				
			}
		});
		builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();			
			}
		});
		
		builder.show();
	}

	/**
	 * 跳转到应用主界面
	 */
	protected void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		//在开启一个新的界面后,将导航界面关闭(导航界面只可见一次)
		finish();
		
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
				
				long startTime = System.currentTimeMillis();
				//发送请求获取数据  ,参数是请求json的链接地址
				//http://192.168.1.2/versioninfo.json 测试阶段不是最优
				//10.0.2.2 仅限于模拟器访问电脑的tomcat
				Message msg = Message.obtain();
				try {
					URL url = new URL("http://192.168.1.2/versioninfo.json");
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
						
						//7. json 解析
						JSONObject jsonObject = new JSONObject(json);
						String versionCode = jsonObject.getString("versionCode");
						mVersionDes = jsonObject.getString("versionDes");
						String versionName = jsonObject.getString("versionName");
						String versionUrl = jsonObject.getString("downloadUrl");
						
						Log.i(tag, versionCode);
						Log.i(tag, mVersionDes);
						Log.i(tag, versionName);
						Log.i(tag, versionUrl);
						
						//8. 比对版本号， 如果服务器版本号大于本地版本号，提示用户更新
						if(mLocalVersionCode < Integer.parseInt(versionCode)){
							msg.what = UPDATE_VERSION;
						}else{
							msg.what = ENTER_HOME;
						}
						
						
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					msg.what = URL_ERROR;
				} catch (IOException e) {
					e.printStackTrace();
					msg.what = IO_ERROR;
				} catch (JSONException e) {
					e.printStackTrace();
					msg.what = JSON_ERROR;
				}finally{
					long endTime = System.currentTimeMillis();
					//指定睡眠时间,请求网络的时长超过4秒则不做处理
					//请求网络的时长小于4秒,强制让其睡眠满4秒钟
					if(endTime - startTime < 4000){
						try {
							Thread.sleep(4000 - (endTime - startTime));
						} catch (Exception e2) {
						}
					} 
					mHandler.sendMessage(msg);
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
		rl_root_splash = (RelativeLayout) findViewById(R.id.rl_root_splash);
	}
    
    
}
