package br.edu.ifpb.tsi.pdm.pdmproject;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

	Context context;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, AlarmService.class);
		context.startService(service);
//		
//		Uri uri = Uri.parse("http://www.google.com.br/");
//		Intent it = new Intent(Intent.ACTION_VIEW, uri);
//		
//		PendingIntent pi = PendingIntent.getActivity(context, 1, it, 0);
//
//		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//		builder.setContentIntent(pi);
//		
//		builder.setTicker("Voce tem tarefas pra fazer!");
//		builder.setContentText(intent.getAction());
//
//		builder.setContentInfo("Atençao");
//		builder.setContentTitle("Fonte");
//		builder.setSmallIcon(R.drawable.ic_launcher);
//		builder.setAutoCancel(true);
//		//long[] pattern = { 500, 500, 500, 500, 500, 500, 500, 500, 500 };
//		//builder.setVibrate(pattern);
//		builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//
//		builder.setSubText("sub text");
//
//		NotificationManager manager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
//		manager.notify(1, builder.build());
	}

}