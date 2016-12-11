package com.example.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;

public class SettingItemView extends RelativeLayout {

	private static final String NAME_SPACE = "http://schemas.android.com/apk/res/com.example.mobilesafe";
	private TextView tv_title;
	private TextView tv_des;
	private CheckBox cb_box;
	private String mDestitle;
	private String mDesoff;
	private String mDeson;

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		View.inflate(context, R.layout.setting_item_view, this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_des = (TextView) findViewById(R.id.tv_des);
		cb_box = (CheckBox) findViewById(R.id.cb_box);
		
		initAttr(attrs);
		
		tv_title.setText(mDestitle);
	}

	/**
	 * 获取属性中的自定义属性
	 */
	private void initAttr(AttributeSet attrs) {
		mDestitle = attrs.getAttributeValue(NAME_SPACE, "destitle");
		mDesoff = attrs.getAttributeValue(NAME_SPACE, "desoff");
		mDeson = attrs.getAttributeValue(NAME_SPACE, "deson");
		
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public SettingItemView(Context context) {
		this(context,null);
	}

	/**
	 * 判断是否是开启的方法
	 * @return 返回当前SettingItemView是否选中状态	true开启(checkBox返回true)	false关闭(checkBox返回true)
	 */
	public boolean isChecked(){		
		return cb_box.isChecked(); 
	}
	
	/**
	 * 开启和关闭 SettingItemView
	 * @param isCheck false 关闭, true 开启
	 */
	public void setChecked(boolean isCheck){
		cb_box.setChecked(isCheck);
		if(isCheck){
			tv_des.setText(mDesoff);
		}else{
			tv_des.setText(mDeson);
		}
	}
}
