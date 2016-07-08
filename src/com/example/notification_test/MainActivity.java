package com.example.notification_test;

import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RemoteViews;

public class MainActivity extends Activity {
	private int NOTIFICATION_ID = 100001;
	private int NOTIFICATION_ID2 = 122000;
	private int number=0;
	BroadcastReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		receiver=new MyReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("ACTION_CLICK_BUTTON");
		registerReceiver(receiver, filter);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}
	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send_message:
			notificationMessage();
			break;
		case R.id.btn_send_message2:
			sendMessage();
			break;
		case R.id.btn_cancle:
			cancleMessage();
			break;
		case R.id.btn_update_progress:
			progressMessage();
			break;
		case R.id.btn_play_control:
			playControl();
			break;

		default:
			break;
		}
	}

	private void playControl() {
		NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Builder builder=new Builder(this);
		
		RemoteViews views=new RemoteViews(getPackageName(), R.layout.notification_activity);
		
		
		Intent intent1=new Intent(this,SecondActivity.class);
		PendingIntent pi1=PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.btn_pre, pi1);
		
		Intent intent2=new Intent(this,SecondActivity.class);
		PendingIntent pi2=PendingIntent.getBroadcast(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.btn_play, pi2);
		
		Intent intent3=new Intent(this,SecondActivity.class);
		PendingIntent pi3=PendingIntent.getBroadcast(this, 0, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.btn_next, pi3);
		
		builder.setContent(views).setSmallIcon(R.drawable.image_test).setTicker("滚动消息");
		
		Notification n=builder.build();
		manager.notify(444512,n);
		
		
	}

	private void progressMessage() {
		final NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		final Builder builder=new Builder(this);
		builder.setContentTitle("音乐下载")
		.setProgress(100, 0, false).setSmallIcon(R.drawable.icon).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.image_test))
		.setTicker("音乐下载");
		
		
		manager.notify(NOTIFICATION_ID2,builder.build());
		new Thread(){
			public void run() {
				
				try {
					sleep(1000);
					for (int i = 1; i <=10; i++) {
						builder.setProgress(100, i*10, false);
						//builder.setSubText(""+i*10+"%");
						manager.notify(NOTIFICATION_ID2,builder.build());
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent=new Intent(MainActivity.this,SecondActivity.class);
				PendingIntent pi=PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				builder.setContentIntent(pi);
				builder.setProgress(0, 0, false)
				.setContentText("下载完成");
				manager.notify(NOTIFICATION_ID2,builder.build());
			}
		}.start();
		
		
		
		
	}
	class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("info", "点击了第二个按钮："+intent.getAction());
		}
	}
	private void cancleMessage() {
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.cancel(NOTIFICATION_ID);

	}

	private void sendMessage() {
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Builder builder = new Builder(this);
		builder.setSmallIcon(R.drawable.icon)
				.setTicker("下载图片。。。。。")
				.setOngoing(true)
				.setLargeIcon(
						BitmapFactory.decodeResource(getResources(),
								R.drawable.image_test))
				.setWhen(System.currentTimeMillis()).setSubText("下载："+number+"%")
				.setContentTitle("这是标题").setContentText("这是主文本内容").setAutoCancel(true);
		Intent intent=new Intent(this,SecondActivity.class);
		PendingIntent i=PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(i);
		Notification n = builder.build();
		if (number<=100) {
			number+=10;
		}else {
			number=0;
		return;
		}
		manager.cancel(NOTIFICATION_ID);
		manager.notify(NOTIFICATION_ID, n);

	}

	private void notificationMessage() {

		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Builder builder = new Builder(this);
		builder.setSmallIcon(R.drawable.icon)
				.setTicker("这是滚动内容....")
				.setOngoing(true)
				.setLargeIcon(
						BitmapFactory.decodeResource(getResources(),
								R.drawable.image_test))
				.setWhen(System.currentTimeMillis()).setSubText("这是子内容")
				.setContentTitle("这是标题").setContentText("这是主文本内容");

		Notification n = builder.build();
		manager.notify(NOTIFICATION_ID, n);

	}

}
