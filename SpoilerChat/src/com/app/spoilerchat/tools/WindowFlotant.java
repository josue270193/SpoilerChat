package com.app.spoilerchat.tools;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.RelativeLayout;

import com.app.spoilerchat.ChatActivity;
import com.app.spoilerchat.R;

public class WindowFlotant extends Service {

	
	GestureDetector gestureDetector;
	private WindowManager windowManager;
	
	private WindowManager.LayoutParams paramsF;
	private int initialX;
	private int initialY;
	private float initialTouchX;
	private float initialTouchY;
	boolean onMovimiento 									= false;
	
	private static final int ANIMATION_FRAME_RATE 			= 30;
	private static final int TRAY_DIM_X_DP 					= 60;	
	private static final int TRAY_DIM_Y_DP 					= 100; 	
	private RelativeLayout layout_flotant;
	private RelativeLayout layout_flotant_mensaje;
	
	private int id_notificacion 							= 1000;
	private static final String NOTIFICATION_DELETED_ACTION = "NOTIFICATION_DELETED";
	private int numero_mensajes								= 0;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override 
	public void onCreate() {
		super.onCreate();

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		layout_flotant = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.layout_flotant, null);
		layout_flotant_mensaje = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.layout_flotant_mensaje, null);
		
		layout_flotant.addView(layout_flotant_mensaje);
		
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				dpToPixels(TRAY_DIM_X_DP, getResources()),
				dpToPixels(TRAY_DIM_Y_DP, getResources()),
				WindowManager.LayoutParams.TYPE_PHONE, 
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, 
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 100;
		paramsF = params;
		
		crearNotificacion();
		//windowManager.addView(chatHead, params);
		windowManager.addView(layout_flotant, params);
		
		
		// GESTION DE TOUCH Y GESTOS AL VIEW
		gestureDetector = new GestureDetector(this, new GestureListener());		
		try {
			
			layout_flotant.setOnTouchListener(new View.OnTouchListener() {
	
				@Override public boolean onTouch(View v, MotionEvent event) {
										
					if (!gestureDetector.onTouchEvent(event)){
						if (event.getAction() == MotionEvent.ACTION_MOVE){
							if (onMovimiento){
								paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
								paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
								windowManager.updateViewLayout(layout_flotant, paramsF);
							}
							
						}
						if (event.getAction() == MotionEvent.ACTION_UP){							
							onMovimiento = false;	
							int ancho = getWidth();
							if (ancho/2 >= paramsF.x){
								paramsF.x = 0;
							}else{							
								paramsF.x = ancho;
							}
							windowManager.updateViewLayout(layout_flotant, paramsF);
						}												
					}
					return false;
				}
			});
		} catch (Exception e) {		
		}

		
		// ANIMACION Y MODIFICACIONES ESTICA AL VIEW
		/*
		layout_flotant.postDelayed(new Runnable() {
				@Override
				public void run() {
					
					layout_flotant.setVisibility(View.VISIBLE);

					
				}
		}, ANIMATION_FRAME_RATE);
		*/
	}

	private int dpToPixels(int dp, Resources resources) {
		return (int)(resources.getDisplayMetrics().density*dp + 0.5f);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		onMovimiento = false;	
		int ancho = getWidth();
		if (ancho/2 >= paramsF.x){
			paramsF.x = 0;
		}else{							
			paramsF.x = ancho;
		}
		windowManager.updateViewLayout(layout_flotant, paramsF);
	}
	
	private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
        	
        	initialX = paramsF.x;
			initialY = paramsF.y;
			initialTouchX = e.getRawX();
			initialTouchY = e.getRawY();

            return true;
        }
        
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e){
        	actualizarNotificacion();
        	
        	AnimationSet animations = new AnimationSet(true);
			animations.setFillAfter(true);		 
			
			Animation animationAlpha = new AlphaAnimation(0.0f, 1.0f);
			animationAlpha.setDuration(1000);
			animationAlpha.setStartOffset(500);
			
			animations.addAnimation(animationAlpha);
			
			layout_flotant_mensaje.setVisibility(View.VISIBLE);
			layout_flotant_mensaje.startAnimation(animations);
			layout_flotant_mensaje.setVisibility(View.INVISIBLE);
        	return true;
        }
                

		@Override
        public void onLongPress(MotionEvent e){
        	
			onMovimiento = true;			
        	return;
        }
        
        @Override
        public boolean onDoubleTap(MotionEvent e) {
        	regresarApp();     	
            return true;
        }
    }

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public int getWidth() {
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        int width;
        if (Build.VERSION.SDK_INT >= 13) {
            display.getSize(size);
            width = size.x;

        } else {
            width = display.getWidth();
        }
        return width;
    }
	
	protected void regresarApp() {
		//unregisterReceiver(receiver);
		
		final Intent notificationIntent = new Intent(this, ChatActivity.class);
		notificationIntent.setAction(Intent.ACTION_MAIN);
		notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(notificationIntent);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	Log.e("NOTIFICACION", "ELIMINADO");
        	stopSelf();
            unregisterReceiver(this);
        }
    };
    
    private void actualizarNotificacion() {
    	PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), Intent.FLAG_ACTIVITY_NEW_TASK);
    	NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentIntent(pendingIntent)
		        .setContentTitle("SpoilerChat - ID")
		        .setDefaults(Notification.DEFAULT_ALL);

		mBuilder.setContentText("Mensajes recibidos")
        		.setNumber(++numero_mensajes);
		
		mNotificationManager.notify(id_notificacion, mBuilder.build());
    }
    
	private void crearNotificacion() {

        Intent intent_eliminar = new Intent(NOTIFICATION_DELETED_ACTION);
        PendingIntent intent_eliminar_pendiente = PendingIntent.getBroadcast(this, 0, intent_eliminar, 0);
        registerReceiver(receiver, new IntentFilter(NOTIFICATION_DELETED_ACTION));
        
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle("SpoilerChat - ID")
		        .setContentText("Haga click para regresar a la aplicacion")
		        .setDeleteIntent(intent_eliminar_pendiente);

		Intent resultIntent = new Intent(this, ChatActivity.class);
		resultIntent.setAction("android.intent.action.MAIN");
		resultIntent.addCategory("android.intent.category.LAUNCHER"); 
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(ChatActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(id_notificacion, mBuilder.build());
	}
	
	@Override
	public void onDestroy() {
		if (layout_flotant != null) 
			windowManager.removeView(layout_flotant);
	}

}