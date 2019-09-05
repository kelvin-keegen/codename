package com.mobi_apps.kelvinkeegan.codenamealarmclock.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mobi_apps.kelvinkeegan.codenamealarmclock.Alarm;
import com.mobi_apps.kelvinkeegan.codenamealarmclock.service.AlarmServiceBroadcastReciever;

public class AlarmAlertBroadcastReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent mathAlarmServiceIntent = new Intent(
				context,
				AlarmServiceBroadcastReciever.class);
		context.sendBroadcast(mathAlarmServiceIntent, null);

		StaticWakeLock.lockOn(context);
		Bundle bundle = intent.getExtras();
		final Alarm alarm = (Alarm) bundle.getSerializable("alarm");

		Intent mathAlarmAlertActivityIntent;

		mathAlarmAlertActivityIntent = new Intent(context, AlarmAlertActivity.class);

		mathAlarmAlertActivityIntent.putExtra("alarm", alarm);

		mathAlarmAlertActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(mathAlarmAlertActivityIntent);
	}

}
