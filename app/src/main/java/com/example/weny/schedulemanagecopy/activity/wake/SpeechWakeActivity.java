package com.example.weny.schedulemanagecopy.activity.wake;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.speech_recognition.SpeechHelper;
import com.jimmy.common.base.app.BaseActivity;
import com.jimmy.common.bean.MyContacts;
import com.jimmy.common.util.ContactUtils;
import com.jimmy.common.util.LogUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class SpeechWakeActivity extends BaseActivity {

    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.contact_tv)
    TextView contactTv;
    @BindView(R.id.wake)
    Button wakeBtn;
    @BindView(R.id.ansy_contact)
    Button ansyContact;
    @BindView(R.id.ansy_contact_tv)
    TextView ansyContactTv;


    StringBuilder str = new StringBuilder();
    StringBuilder contactSb;
    ArrayList<MyContacts> allContacts;
    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getRootViewId() {
        return R.id.m_root_view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_speech_wake;
    }

    @Override
    protected void onSetContentViewAfter() {
        allContacts = ContactUtils.getAllContacts(this);
        findViewById(R.id.wake).setOnClickListener(view -> {

            str =new StringBuilder();
            tv.setText(str);
            contactSb= new StringBuilder();
            contactTv.setText("");

//            SpeechHelper.startWake(SpeechWakeActivity.this, msg -> {
//
//                SpeechHelper.speechStr(SpeechWakeActivity.this,"麦小客正在倾听");
//
//                SpeechHelper.startSpeace2Text(SpeechWakeActivity.this, new SpeechHelper.MyRecognizerListener() {
//                    @Override
//                    public void onResult(String result, boolean b) {
//                        LogUtils.e("speach",result);
//
//                        str.append(result);
//                        tv.setText(str);
//                        if(allContacts!=null){
//                            for(MyContacts contacts:allContacts){
//                                if(result.contains(contacts.getName())&&!contactSb.toString().contains(contacts.getName())){
//                                    contactSb.append("name: ")
//                                            .append(contacts.getName())
//                                            .append(" ,phone: ")
//                                            .append(contacts.getPhone())
//                                            .append("\n");
//                                    contactTv.setText(contactSb.toString());
//                                }
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(String errMsg,int code) {
//                        LogUtils.e("speach",errMsg+","+code);
//                    }
//                });
//            });


            CharSequence text = wakeBtn.getText();
            if(text.equals("开始")){
                wakeBtn.setText("完成");
                SpeechHelper.startSpeace2Text(SpeechWakeActivity.this, new SpeechHelper.MyRecognizerListener() {

                    @Override
                    public void onResult(String result, boolean b) {

                        str.append(result);
                        tv.setText(str);

                        String result1 = result;
                        result1 = result1.replaceAll("1","一");
                        result1 =result1.replaceAll("2","二");
                        result1 =result1.replaceAll("3","三");
                        result1 =result1.replaceAll("4","四");
                        result1 =result1.replaceAll("5","五");
                        result1 =result1.replaceAll("6","六");
                        result1 =result1.replaceAll("7","七");
                        result1 =result1.replaceAll("8","八");
                        result1 =result1.replaceAll("9","九");


                        if(allContacts!=null){
                            for(MyContacts contacts:allContacts){
                                String name1 = contacts.getName();
                                name1 =name1.replaceAll("1","一");
                                name1 =name1.replaceAll("2","二");
                                name1 =name1.replaceAll("4","四");
                                name1 =name1.replaceAll("5","五");
                                name1 =name1.replaceAll("6","六");
                                name1 =name1.replaceAll("7","七");
                                name1 =name1.replaceAll("8","八");
                                name1 =name1.replaceAll("9","九");
                                if(result1.contains(name1)&&!contactSb.toString().contains(contacts.getName())){
                                    contactSb.append("name: ")
                                            .append(contacts.getName())
                                            .append(" ,phone: ")
                                            .append(contacts.getPhone())
                                            .append("\n");
                                    contactTv.setText(contactSb.toString());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(String errMsg, int code) {




                    }
                });
            }else {
                wakeBtn.setText("开始");
                SpeechHelper.endSpeech();
            }
        });



        ansyContact.setOnClickListener(view -> {
              asynContact();

        });

        requestPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpeechHelper.endWake();
    }


    private void asynContact(){
        SpeechHelper.asynContact(this, str -> {
            ansyContactTv.setText(str);
        });

    }

    private void requestPermissions(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permission!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.LOCATION_HARDWARE,Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS},0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
