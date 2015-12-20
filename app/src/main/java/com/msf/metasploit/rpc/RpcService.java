package com.msf.metasploit.rpc;

import android.app.IntentService;
import android.content.Intent;

import com.msf.metasploit.MsfApplication;

public class RpcService extends IntentService {
    
	public RpcService() {
		super(RpcService.class.getSimpleName());
	}

	private MsfMain msfMain;
    private MsfController msfController;

	@Override
	public void onCreate() {
		super.onCreate();
		msfMain = MsfApplication.getMsfMain();
        msfController = MsfApplication.getMsfController();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		msfController.handleIntent(intent);

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

	public void handleIntent(Intent intent) {
        System.err.println("intent " + intent);

	}
}
