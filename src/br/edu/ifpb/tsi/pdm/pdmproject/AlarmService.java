package br.edu.ifpb.tsi.pdm.pdmproject;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class AlarmService extends Service{

	private NotificationManager mManager;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);

		NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
		notification.setTicker("Você tem tarefas a fazer!");
		notification.setContentText(intent.getAction());
		// notification.setContentInfo("AtenÃ§ao");
		notification.setContentTitle("Estudar para o ...");
		notification.setSmallIcon(R.drawable.ic_launcher);
		notification.setAutoCancel(true);

		mManager.notify(0, notification.build());
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
