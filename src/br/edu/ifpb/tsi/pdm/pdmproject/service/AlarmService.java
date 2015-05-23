package br.edu.ifpb.tsi.pdm.pdmproject.service;

import br.edu.ifpb.tsi.pdm.pdmproject.R;
import br.edu.ifpb.tsi.pdm.pdmproject.R.drawable;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class AlarmService extends Service{

	private NotificationManager mManager;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.getApplicationContext();
		mManager = (NotificationManager) this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
		notification.setTicker("Voc� tem tarefas a fazer!");
		notification.setContentText(intent.getAction());

		notification.setContentTitle(intent.getStringExtra("ATIVIDADE") + " de " + intent.getStringExtra("DISCIPLINA"));
		notification.setContentInfo("Em " + intent.getStringExtra("DATA_HORA"));
		notification.setSmallIcon(R.drawable.ic_launcher);
		notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		notification.setAutoCancel(true);

		mManager.notify(0, notification.build());
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
