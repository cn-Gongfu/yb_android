package com.example.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	public static void show(Context con, String content){
		Toast.makeText(con, content, 0).show();
	}
}
