package com.mobi_apps.kelvinkeegan.codenamealarmclock.alert;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.mobi_apps.kelvinkeegan.codenamealarmclock.Alarm;
import com.mobi_apps.kelvinkeegan.codenamealarmclock.QRcode.IntentIntegrator;
import com.mobi_apps.kelvinkeegan.codenamealarmclock.QRcode.IntentResult;
import com.mobi_apps.kelvinkeegan.codenamealarmclock.R;

import java.io.InputStream;

public class AlarmAlertActivity extends AppCompatActivity {

	public Alarm alarm = new Alarm();
	private MediaPlayer mediaPlayer = new MediaPlayer();

	private StringBuilder answerBuilder = new StringBuilder();

	private Vibrator vibrator;

	private boolean alarmActive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		setContentView(R.layout.alarm_alert);

		Bundle bundle = this.getIntent().getExtras();

	//	alarm = (Alarm) bundle.getSerializable("alarm");
		this.setTitle(alarm.getAlarmName());

		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);

		PhoneStateListener phoneStateListener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				switch (state) {
					case TelephonyManager.CALL_STATE_RINGING:
						Log.d(getClass().getSimpleName(), "Incoming call: "
								+ incomingNumber);
						try {
							mediaPlayer.pause();
						} catch (IllegalStateException e) {

						}
						break;
					case TelephonyManager.CALL_STATE_IDLE:
						Log.d(getClass().getSimpleName(), "Call State Idle");
						try {
							mediaPlayer.start();
						} catch (IllegalStateException e) {

						}
						break;
				}
				super.onCallStateChanged(state, incomingNumber);
			}
		};

		telephonyManager.listen(phoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);

		// Toast.makeText(this, answerString, Toast.LENGTH_LONG).show();

		startAlarm();

	}

	@Override
	protected void onResume() {
		super.onResume();
		alarmActive = true;
	}

	private void startAlarm() {

		if (alarm.getAlarmTonePath() != "") {
			mediaPlayer = new MediaPlayer();
			if (alarm.getVibrate()) {
				vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
				long[] pattern = {1000, 200, 200, 200};
				vibrator.vibrate(pattern, 0);
			}
			try {
				mediaPlayer.setVolume(1.0f, 1.0f);
				mediaPlayer.setDataSource(this,
						Uri.parse(alarm.getAlarmTonePath()));
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mediaPlayer.setLooping(true);
				mediaPlayer.prepare();
				mediaPlayer.start();

			} catch (Exception e) {
				mediaPlayer.release();
				alarmActive = false;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (!alarmActive)
			super.onBackPressed();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		StaticWakeLock.lockOff(this);
	}

	@Override
	protected void onDestroy() {
		try {
			if (vibrator != null)
				vibrator.cancel();
		} catch (Exception e) {

		}
		try {
			mediaPlayer.stop();
		} catch (Exception e) {

		}
		try {
			mediaPlayer.release();
		} catch (Exception e) {

		}
		super.onDestroy();
	}



	public void button(View view) {
		IntentIntegrator integrator = new IntentIntegrator(AlarmAlertActivity.this);
		integrator.initiateScan();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult != null) {
			// handle scan result
			Toast.makeText(AlarmAlertActivity.this, "Ooops! Try Pushing the back button harder next time :-P", Toast.LENGTH_SHORT).show();
		}
		if (resultCode == -1) {
			Toast.makeText(AlarmAlertActivity.this, "Keegan say's GoodMorning! :)" , Toast.LENGTH_SHORT).show();
			finish();

		}
	}

}



