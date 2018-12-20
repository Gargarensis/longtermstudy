package usi.memotion2personal.gathering.gatheringServices.Notifications;

import android.annotation.SuppressLint;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import usi.memotion2personal.gathering.gatheringServices.Notifications.Utils.Log;

/**
 * Created by abhinavmerotra
 */

@SuppressLint("NewApi")
public class NotificationMonitorService extends NotificationListenerService 
{

	private final Log log = new Log();

	
	@SuppressLint("NewApi")
	@Override
	public void onNotificationPosted(StatusBarNotification sbn) 
	{ 
		log.v("A new notification has been posted!"); 

		
		
		final StatusBarNotification sbn_final = sbn;
		Thread thread = new Thread() 
		{
		    @Override
		    public void run() {
		    	//log data		
				new NotificationDataCollector(getApplicationContext()).onNotificationPosted(sbn_final); 
		    }
		};
		thread.start();
	}

	
	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) 
	{
		log.v("A notification has been removed!");

		final StatusBarNotification sbn_final = sbn;
		Thread thread = new Thread() 
		{
		    @Override
		    public void run() {
				//log data
				new NotificationDataCollector(getApplicationContext()).onNotificationRemoved(sbn_final);
		    }
		};
		thread.start();
	}
}
