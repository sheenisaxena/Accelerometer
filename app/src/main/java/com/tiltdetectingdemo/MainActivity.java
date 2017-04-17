package com.tiltdetectingdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView textView;
    private ImageView image;
    HorizontalScrollView horizontal_sv;
    boolean forFirstTimeOnly = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        horizontal_sv = (HorizontalScrollView) findViewById(R.id.horizontal_sv);

        //declaring Sensor Manager and sensor type
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //locate views
        textView = (TextView) findViewById(R.id.txt);
        image = (ImageView) findViewById(R.id.img);

//        image.setOnClickListener(this);
        image.requestFocus();
        image.performClick();

        // belwo code is working to get centered image instead of from left but by adding this,scrolling in horizontal scrollview is not working
        image.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // Get ImageView height width here <------------
                        if (forFirstTimeOnly) {
                            int scrollX = (image.getLeft() - (getDeviceWidth(MainActivity.this) / 2)) + (image.getWidth() / 2);
                            horizontal_sv.smoothScrollTo(scrollX, 0);
                            forFirstTimeOnly = false;
                        }
                    }
                });

        // below code for user that user not bale to scroll via touch on image
        horizontal_sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                return false;
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];

        if (x < 0) {
//                image.setImageResource(R.drawable.right);
            textView.setText("You tilt the device right");
//                horizontal_sv.scrollTo((horizontal_sv.getChildAt(0).getWidth()) / 4 , (int)y);
            horizontal_sv.scrollTo((((int) horizontal_sv.getScrollX()) - 30), (int) y);
        }
        if (x > 0) {
//                image.setImageResource(R.drawable.left);
            textView.setText("You tilt the device left");
//                horizontal_sv.scrollTo((horizontal_sv.getChildAt(0).getWidth()) / 4 , (int)y);
            horizontal_sv.scrollTo((((int) horizontal_sv.getScrollX()) + 30), (int) y);
        }

      /*  if (Math.abs(x) > Math.abs(y)) {
            if (x < 0) {
//                image.setImageResource(R.drawable.right);
                textView.setText("You tilt the device right");
//                horizontal_sv.scrollTo((horizontal_sv.getChildAt(0).getWidth()) / 4 , (int)y);
                horizontal_sv.scrollTo((((int)horizontal_sv.getScrollX()) - 10) , (int)y);
            }
            if (x > 0) {
//                image.setImageResource(R.drawable.left);
                textView.setText("You tilt the device left");
//                horizontal_sv.scrollTo((horizontal_sv.getChildAt(0).getWidth()) / 4 , (int)y);
                horizontal_sv.scrollTo((((int)horizontal_sv.getScrollX()) + 10) , (int)y);
            }
        } else {
            if (y < 0) {
//                image.setImageResource(R.drawable.up);
                textView.setText("You tilt the device up");
            }
            if (y > 0) {
//                image.setImageResource(R.drawable.down);
                textView.setText("You tilt the device down");
//                horizontal_sv.scrollTo((int)x, (int)y);
            }
        }*/
        if (x > (-2) && x < (2) && y > (-2) && y < (2)) {
//            image.setImageResource(R.drawable.center);
            textView.setText("Not tilt device");
            int scrollX = (image.getLeft() - (getDeviceWidth(MainActivity.this) / 2)) + (image.getWidth() / 2);
            horizontal_sv.smoothScrollTo(scrollX, 0);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister Sensor listener
        sensorManager.unregisterListener(this);
    }

    public static int getDeviceWidth(Activity activity) {

        WindowManager wm = activity.getWindowManager();
        Point point = new Point();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wm.getDefaultDisplay().getSize(point);
            return point.x;
        } else {
            return wm.getDefaultDisplay().getWidth();
        }
    }

   /* @Override
    public void onClick(View v) {
        int scrollX = (image.getLeft() - (getDeviceWidth(MainActivity.this) / 2)) + (image.getWidth() / 2);
        horizontal_sv.smoothScrollTo(scrollX, 0);
    }*/
}
