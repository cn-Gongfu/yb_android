package com.example.mobilesafe.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.ConstantValue;
import com.example.mobilesafe.utils.SpUtil;
import com.example.mobilesafe.utils.ToastUtil;

public class HomeActivity extends Activity {

	private GridView gv_home;
	private String[] mTitleStrs;
	private int[] mDrawableIds;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		initUI();
		
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		//准备数据(文字(9组),图片(9张))
		mTitleStrs = new String[]{
				"手机防盗","通信卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"
		};

		mDrawableIds = new int[]{
				R.drawable.home_safe,R.drawable.home_callmsgsafe,
				R.drawable.home_apps,R.drawable.home_taskmanager,
				R.drawable.home_netmanager,R.drawable.home_trojan,
				R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings
		};

		gv_home.setAdapter(new Myadapter());
		
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					//开启对话框
					showDialog();
					break;
					
				case 8:
					Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
					startActivity(intent);
					break;
				default:
					break;
				}
				
			}
		});
	}

	/**
	 * 弹出输入密码对话框
	 */
	protected void showDialog() {
		String pwd = SpUtil.getString(getApplicationContext(), ConstantValue.MOBILESAFE_PWD, "");
		if(TextUtils.isEmpty(pwd)){
			showSetPwdDialog();
		}else{
			showConfirmPwdDialog();
		}		
	}

	/**
	 * 确认密码对话框
	 */
	private void showConfirmPwdDialog() {
		Builder builder = new AlertDialog.Builder(HomeActivity.this);
		final AlertDialog dialog = builder.create();
		final View view = View.inflate(getApplicationContext(), R.layout.dialog_confirm_pwd, null);
		dialog.setView(view);
		dialog.show();
		
		Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		
		bt_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText et_confirm_pwd = (EditText) view.findViewById(R.id.et_confirm_pwd);
				String confirmPwd = et_confirm_pwd.getText().toString();
				if(!TextUtils.isEmpty(confirmPwd)){
					String pwd = SpUtil.getString(getApplicationContext(), ConstantValue.MOBILESAFE_PWD, "");
					if(pwd.equals(confirmPwd)){
						Intent intent = new Intent(getApplicationContext(),TestActivity.class);
						startActivity(intent);
						dialog.dismiss();
					}else{
						ToastUtil.show(getApplicationContext(), "密码错误");
					}
				}else{
					ToastUtil.show(getApplicationContext(), "密码不能为空");
				}
				dialog.dismiss();
			}
		});
		
		bt_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				dialog.dismiss();
			}
		});
		
		
	}

	/**
	 * 设置密码对话框
	 */
	private void showSetPwdDialog() {
		Builder builder = new AlertDialog.Builder(HomeActivity.this);
		final AlertDialog dialog = builder.create();
		final View view = View.inflate(getApplicationContext(), R.layout.dialog_set_pwd, null);
		dialog.setView(view);
		dialog.show();
		
		Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		
		bt_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText et_set_pwd = (EditText) view.findViewById(R.id.et_set_pwd);
				EditText et_confirm_pwd = (EditText) view.findViewById(R.id.et_confirm_pwd);
				
				String pwd = et_set_pwd.getText().toString();
				String confirmPwd = et_confirm_pwd.getText().toString();
				
				
				if(!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(confirmPwd)){
					if(pwd.equals(confirmPwd)){
						Intent intent = new Intent(getApplicationContext(),TestActivity.class);
						startActivity(intent);
						dialog.dismiss();
						
						SpUtil.putString(getApplicationContext(), ConstantValue.MOBILESAFE_PWD, pwd);
						
					}else{
						ToastUtil.show(getApplicationContext(), "两次输入的密码不一致");
					}
				}else{
					ToastUtil.show(getApplicationContext(), "密码不能为空");
				}
				
			}
		});
		
		bt_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
		
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		gv_home = (GridView) findViewById(R.id.gv_home);
		
	}
	
	class Myadapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mTitleStrs.length;
		}

		@Override
		public Object getItem(int position) {			
			return mTitleStrs[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
			ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
			TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
			iv_icon.setImageResource(mDrawableIds[position]);
			tv_title.setText(mTitleStrs[position]);
			return view;
		}
		
	}
}
