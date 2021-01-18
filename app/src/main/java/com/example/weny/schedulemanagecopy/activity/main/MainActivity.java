package com.example.weny.schedulemanagecopy.activity.main;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.schedulemanagecopy.fragment.CalendarFragment;
import com.jimmy.common.base.app.BaseActivity;
import com.jimmy.common.util.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    CalendarFragment calendarFragment;

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getRootViewId() {
        return R.id.m_root_view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onSetContentViewAfter() {
        calendarFragment = new CalendarFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame,calendarFragment);
        fragmentTransaction.commit();

        initSensorManager();
        initViews();
    }

    EditText edTime,edD;
    TextView textView;
    private void initViews() {
        textView = findViewById(R.id.text);
        edTime = findViewById(R.id.edit_tiem);
        edD = findViewById(R.id.edit_dd);
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = edTime.getText().toString();

                String s = edD.getText().toString();

                UPTATE_INTERVAL_TIME = Integer.parseInt(time);
                DX = Float.parseFloat(s);

                Toast.makeText(MainActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFile();

            }
        });
        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endFile();
            }
        });
        
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        calendarFragment.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private SensorManager sensorManager;
    private Sensor accelerometer;



    protected void initSensorManager(){
        Log.e("initSensorManager","initSensorManager1");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        Log.e("initSensorManager","initSensorManager2");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(sensorEventListener);
    }

    // 速度阈值，当摇晃速度达到这值后产生作用
    private static final int SPEED_SHRESHOLD = 200;

    // 上次检测时间
    private long lastUpdateTime;

    private float delta0X,delta0Y,delta0Z;
    private float delta1X,delta1Y,delta1Z;

    private int tFlag = TFLAG_NO;
    private static final int TFLAG_NO=0;
    private static final int TFLAG_T1=1;


    // 两次检测的时间间隔
    float lastX,lastY,lastZ;
    private int UPTATE_INTERVAL_TIME =60;
    private float DX =6f;

    private float lastSqrt;
    private int satisfyCount=0;


    //重力感应监听
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
//             现在检测时间
            long currentUpdateTime = System.currentTimeMillis();
            // 两次检测的时间间隔
            long timeInterval = currentUpdateTime - lastUpdateTime;
            // 判断是否达到了检测时间间隔
            if (timeInterval < UPTATE_INTERVAL_TIME)
            return;
            // 现在的时间变成last时间
            lastUpdateTime = currentUpdateTime;
            // 获得x,y,z坐标
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            // 获得x,y,z的变化值
            float deltaX = x - lastX;
            float deltaY = y - lastY;
            float deltaZ = z - lastZ;
            // 将现在的坐标变成last坐标
            lastX = x;
            lastY = y;
            lastZ = z;
            LogUtils.e("onSensorChanged_delta","xxxxx:"+deltaX);
            LogUtils.e("onSensorChanged_delta","yyyyy:"+deltaY);
            LogUtils.e("onSensorChanged_delta","zzzzz:"+deltaZ);


            LogUtils.e("onSensorChanged=","xxxxx:"+x);
            LogUtils.e("onSensorChanged=","yyyyy:"+y);
            LogUtils.e("onSensorChanged=","zzzzz:"+z);


            if(outputStreamWriter!=null){
                String data = x+"   "+y+"   "+z+"\n";
                arrayList.add(data);
                if(arrayList.size()>20){
                    arrayList.remove(0);
                }
                sb.delete(0,sb.length());
                for(int i=0;i<arrayList.size();i++){
                    sb.append(arrayList.get(i)).append("\n");
                }
                textView.setText(sb.toString());


                try {
                    Log.e("data","data:"+data);
                    outputStreamWriter.write(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            //sqrt 返回最近的双近似的平方根
            double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
                * deltaZ)/ timeInterval * 10000;
            //  MyLog.v("thelog", "===========log===================");
            // 达到速度阀值，发出提示
//            if (speed >= SPEED_SHRESHOLD) {
            //                    startSleepTask();
                if(tFlag == TFLAG_T1){
                    delta1X = deltaX;
                    delta1Y = deltaY;
                    delta1Z = deltaZ;
                    tFlag = TFLAG_NO;
                    //开始判断 t0 和 t1的方向是否相近
                    if(isOK(x,y,z)){
//                        Toast.makeText(MainActivity.this, "触发", Toast.LENGTH_SHORT).show();
                    }else {


                    }

                }
                if(tFlag==TFLAG_NO){
                    delta0X = deltaX;
                    delta0Y = deltaY;
                    delta0Z = deltaZ;
                    tFlag=TFLAG_T1;
                }

//            }
        }

        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }
    };
    ArrayList<String> arrayList = new ArrayList<>();
    StringBuilder sb = new StringBuilder();




    private boolean isOK(float x,float y,float z){
        float ddx = delta0X-delta1X;
        float ddY = delta0Y-delta1Y;
        float ddZ = delta0Z-delta1Z;

        double sqrt = Math.sqrt(ddx * ddx + ddY * ddY + ddZ * ddZ);


        Log.e("satisfyCount",satisfyCount+"");
        if(sqrt>DX){
            satisfyCount++;
        }else {
            satisfyCount=0;
        }
        if(satisfyCount>=3){
            Toast.makeText(MainActivity.this, "触发+"+sqrt, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


    private void endFile() {
        if(outputStreamWriter!=null){
            try {
                outputStreamWriter.close();
                Toast.makeText(this, "记录成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        outputStreamWriter = null;
    }

    OutputStreamWriter outputStreamWriter;

    private void startFile() {
        if(outputStreamWriter!=null){
            return;
        }
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/testData";
        String fileName = System.currentTimeMillis()+"-"+edTime.getText().toString()+".txt";
        try {
            File parent = new File(path);
            if(!parent.exists()){
                parent.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path,fileName));
            outputStreamWriter =new OutputStreamWriter(fileOutputStream);
            Toast.makeText(this, "开始记录", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
