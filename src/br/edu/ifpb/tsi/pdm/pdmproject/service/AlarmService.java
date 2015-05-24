package br.edu.ifpb.tsi.pdm.pdmproject.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import br.edu.ifpb.tsi.pdm.pdmproject.R;

public class AlarmService extends Service{

	private NotificationManager mManager;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.getApplicationContext();
		mManager = (NotificationManager) this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
		notification.setTicker("Você tem tarefas a fazer!");
		notification.setContentText(intent.getAction());

		notification.setContentTitle(intent.getStringExtra("ATIVIDADE") + " de " + intent.getStringExtra("DISCIPLINA"));
		notification.setContentText("Hora de estudar!");
		notification.setContentInfo("Entrega: " + intent.getStringExtra("DATA_HORA") + ".");
		notification.setSmallIcon(R.drawable.small);
		notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		notification.setAutoCancel(true);

		mManager.notify(0, notification.build());
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
