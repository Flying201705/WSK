package com.nova.game;

import com.nova.game.net.ChannelManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetworkBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			int netWorkType = getNetType(context);
			ChannelManager.getInstance().updateNetType(netWorkType);
		}
	}
	
	/**
	 * 获取当前的网络状态 ：没有网络0：WIFI网络1：3G网络2：2G网络3
	 * 
	 * @param context
	 * @return
	 */
	private int getNetType(Context context) {
		int netType = 0;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = 1;// wifi
		} else if (nType == ConnectivityManager.TYPE_MOBILE) {
			int nSubType = networkInfo.getSubtype();
			TelephonyManager mTelephony = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
					&& !mTelephony.isNetworkRoaming()) {
				netType = 2;// 3G
			} else {
				netType = 3;// 2G
			}
		}
		return netType;
	}
}
