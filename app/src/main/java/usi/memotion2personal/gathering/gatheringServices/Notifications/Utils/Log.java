package usi.memotion2personal.gathering.gatheringServices.Notifications.Utils;

/**
 * Created by abhinavmehrotra
 */

public class Log 
{

	private final String TAG = "MEMOTION";
	private final boolean debug = true;

	public void d(String message)
	{
		if(debug)
			android.util.Log.d(TAG, message);
	}

	public void e(String message)
	{
		if(debug)
			android.util.Log.e(TAG, message);
	}

	public void i(String message)
	{
		if(debug)
			android.util.Log.i(TAG, message);
	}

	public void v(String message)
	{
		if(debug)
			android.util.Log.v(TAG, message);
	}

	public void w(String message)
	{
		if(debug)
			android.util.Log.w(TAG, message);
	}
}
