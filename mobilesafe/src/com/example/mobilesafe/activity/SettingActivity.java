package com.example.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.ConstantValue;
import com.example.mobilesafe.utils.SpUtil;
import com.example.mobilesafe.view.SettingItemView;

public class SettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initUpdate();
	}

	/**
	 * 版本更新开关
	 */
	private void initUpdate() {
		final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
		//获取已有开关的状态
		boolean open_update = SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, false);
		siv_update.setChecked(open_update);
		
		siv_update.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				boolean checked = siv_update.isChecked();
				siv_update.setChecked(!checked);
				//存储用户修改的状态
				SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !checked);
			}
		});
	}
}
