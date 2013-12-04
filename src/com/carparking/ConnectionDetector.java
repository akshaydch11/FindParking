package com.carparking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector
{
	private Context context;

	public ConnectionDetector(Context paramContext)
	{
		this.context = paramContext;
	}

	public boolean isConnectingToInternet()
	{
		ConnectivityManager localConnectivityManager = (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] arrayOfNetworkInfo = null;
		if (localConnectivityManager != null)
		{
			arrayOfNetworkInfo = localConnectivityManager.getAllNetworkInfo();
			if (arrayOfNetworkInfo == null);
		}
		for (int i = 0; ; i++)
		{
			if (i >= arrayOfNetworkInfo.length)
				return false;
			if (arrayOfNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED)
				return true;
		}
	}
}
