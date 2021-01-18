package com.example.weny.speech_recognition;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ContactManager;
import com.iflytek.cloud.util.ResourceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SpeechHelper {
    private static final String TAG = SpeechHelper.class.getName();

    public static final String APP_ID = "5d2da071";
    private static VoiceWakeuper wakeuper;

    // 语音合成对象
    private static SpeechSynthesizer mTts;

    public static String voicerCloud="xiaoyan";

    public static String speechString;

    private static String keep_alive = "1";
    private static String ivwNetMode = "0";
    private static SpeechRecognizer mIat;
    private static String engineType="cloud";

    public static void init(final Context context){
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "="+APP_ID);
    }

    public static void startWake(final Context context, final OnSpeechWakeListener onSpeechWakeListener){
        wakeuper = VoiceWakeuper.createWakeuper(context, new InitListener() {
            @Override
            public void onInit(int i) {

            }
        });
        // 清空参数
        wakeuper.setParameter(SpeechConstant.PARAMS, null);
        // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
//        wakeuper.setParameter(SpeechConstant.IVW_THRESHOLD, "0:" + curThresh);
        // 设置唤醒模式
        wakeuper.setParameter(SpeechConstant.IVW_SST, "wakeup");
        // 设置持续进行唤醒
        wakeuper.setParameter(SpeechConstant.KEEP_ALIVE, keep_alive);
        // 设置闭环优化网络模式
        wakeuper.setParameter(SpeechConstant.IVW_NET_MODE, ivwNetMode);
        // 设置唤醒资源路径
        wakeuper.setParameter(SpeechConstant.IVW_RES_PATH, getResource(context));
        // 设置唤醒录音保存路径，保存最近一分钟的音频
//        wakeuper.setParameter(SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath() + "/msc/ivw.wav");
//        wakeuper.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        // 如有需要，设置 NOTIFY_RECORD_DATA 以实时通过 onEvent 返回录音音频流字节
        //wakeuper.setParameter( SpeechConstant.NOTIFY_RECORD_DATA, "1" );
        wakeuper.startListening(new WakeuperListener(){



            @Override
            public void onBeginOfSpeech(){

            }

            @Override
            public void onResult(WakeuperResult wakeuperResult){
                String resultString = wakeuperResult.getResultString();
                Log.e("SpeechHelper",resultString);
                if(onSpeechWakeListener!=null){
                    onSpeechWakeListener.onSpeechWake(resultString);
                }
//                Toast.makeText(context, resultString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SpeechError speechError) {
                Log.e("SpeechHelper",speechError.getErrorCode()+","+speechError.getMessage());
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }

            @Override
            public void onVolumeChanged(int i) {

            }
        });
    }

    public static void endWake(){
        if(wakeuper!=null){
            wakeuper.stopListening();
            wakeuper.destroy();
        }
        if(mTts!=null){
            mTts.stopSpeaking();
            mTts.destroy();
        }
        if(mIat!=null){
            mIat.stopListening();
            mIat.destroy();
        }

    }


    public static void startSpeech(Context context){
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
    }

    public static void asynContact(Context context, final AsnyContactListener asnyContactListener){
        ContactManager mgr = ContactManager.createManager(context,
                new ContactManager.ContactListener() {
                    @Override
                    public void onContactQueryFinish(String s, boolean b) {
                        asnyContactListener.ansyContact(s);
                    }
                });
        mgr.asyncQueryAllContactsName();
    }

    public static void endSpeech(){
        if( null != mTts ){
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }

    public static void release(){
        SpeechUtility.getUtility().destroy();

        if( null != mTts ){
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }


        if( null != wakeuper ){
            wakeuper.destroy();
        }
    }

    private static String getResource(Context context) {
        final String resPath = ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "ivw/" + APP_ID + ".jet");
        Log.d(TAG, "resPath: " + resPath);
        return resPath;
    }

    public static void speechStr(Context context,String s) {
        if(mTts==null){
            speechString = s;
            startSpeech(context);
            setParam();
        }else {
            speech(s);
        }
    }


    /**
     * 初始化监听。
     */
    private static InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
                speech(speechString);
            }
        }
    };

    private static void speech(String str){
        int code = mTts.startSpeaking(str, new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {

            }

            @Override
            public void onSpeakPaused() {

            }

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {

            }

            @Override
            public void onCompleted(SpeechError speechError) {

            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }


    public interface OnSpeechWakeListener{
        void onSpeechWake(String msg);
    }

    /**
     * 参数设置
     */
    private static  void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置合成
        //设置使用云端引擎
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME,voicerCloud);
        //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY,"1");//支持实时音频流抛出，仅在synthesizeToUri条件下支持
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");

        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
    }



    public static void startSpeace2Text(Context context, final MyRecognizerListener listener){

        if (wakeuper!=null&&wakeuper.isListening()) {
            wakeuper.stopListening();
        }

        if(mIat==null){
            //初始化识别无UI识别对象
            //使用SpeechRecognizer对象，可根据回调消息自定义界面；
            mIat = SpeechRecognizer.createRecognizer(context, new InitListener() {
                @Override
                public void onInit(int i) {

                }
            });

            //设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
            mIat.setParameter( SpeechConstant.CLOUD_GRAMMAR, null );
            mIat.setParameter( SpeechConstant.SUBJECT, null );
            //设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
            mIat.setParameter(SpeechConstant.RESULT_TYPE, "plain");
            //此处engineType为“cloud”
            mIat.setParameter( SpeechConstant.ENGINE_TYPE, engineType );
            //设置语音输入语言，zh_cn为简体中文
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            //设置结果返回语言
            mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
            // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
            //            //取值范围{1000～10000}
            mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
            //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
            //自动停止录音，范围{0~10000}
            mIat.setParameter(SpeechConstant.VAD_EOS, "1000" +
                    "");
            //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
            mIat.setParameter(SpeechConstant.ASR_PTT,"1");



        }
        //开始识别，并设置监听器
        mIat.startListening(new RecognizerListener() {
            @Override
            public void onVolumeChanged(int i, byte[] bytes) {

            }

            @Override
            public void onBeginOfSpeech() {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                listener.onResult(recognizerResult.getResultString(),b);
            }

            @Override
            public void onError(SpeechError speechError) {
                listener.onError(speechError.getErrorDescription(),speechError.getErrorCode());
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }

    public static void stopSpeach2Text(){
        mIat.stopListening();
    }



    public static interface MyRecognizerListener{
        void onResult(String result,boolean b);

        void onError(String errMsg,int code);
    }

    public static interface AsnyContactListener{
        void ansyContact(String str);
    }

}
