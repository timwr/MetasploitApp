package com.msf.metasploit.rpc;

import android.app.IntentService;
import android.content.Intent;

public class RpcService extends IntentService {
    
	public RpcService() {
		super(RpcService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
	    
        MsfController.getInstance().handleIntent(intent, this);

		// String title = "MSF: " + host;
		// String status = "Connected";
		// Notification notification = new NotificationCompat.Builder(this)
		// .setContentTitle(title).setContentText(status)
		// .setSmallIcon(R.drawable.ic_launcher).build();
		// Intent notificationIntent = new Intent(this, MainActivity.class);
		// PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
		// notificationIntent, 0);
		// notification.setLatestEventInfo(this, title, status, pendingIntent);
		// startForeground(123, notification);

	}
}
