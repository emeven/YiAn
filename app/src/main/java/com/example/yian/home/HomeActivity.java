package com.example.yian.home;

import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.yian.Apollo.Publish;
import com.example.yian.R;
import com.example.yian.view.BottomBar;

import org.achartengine.ChartFactory;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {

    //mqtt信息存放
    private String heartRate;//存放心率
    private String spO2; //存放血氧
    private String longitude,latitude;//存放经纬度
    private String fallDown;

    //服务器登录信息
    private String host = "tcp://101.132.173.189:1883";
    private String userName = "admin";
    private String passWord = "IOT423!!!";

    private Handler handler;
    private MqttClient client;

    //主题名称
    private String  heartRateTopic="HeartRate";
    private String SpO2Topic="SpO2";
    private String longitudeTopic="Longitude";
    private String latitudeTopic="Latitude";
    private String FallDownTopic="Help Signal";

    private MqttConnectOptions options;
    private ScheduledExecutorService scheduler;
    private int tag=1;
    private String getTopicName="Topic";
    private BottomBar bottomBar;
    private String a="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //服务器打开，那么开始接收
        init();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //显示出获取到的内容

                if(getTopicName.equals("HeartRate")){
                    heartRate=(String) msg.obj;
                    //Toast.makeText(HomeActivity.this,(String) msg.obj,Toast.LENGTH_SHORT).show();
                }
                if(getTopicName.equals("SpO2")){
                    spO2=(String)msg.obj;
                    //Toast.makeText(HomeActivity.this,(String) msg.obj,Toast.LENGTH_SHORT).show();
                }
                if(getTopicName.equals("Longitude")){
                    longitude=(String)msg.obj;
                    Toast.makeText(HomeActivity.this,(String) msg.obj,Toast.LENGTH_SHORT).show();
                }
                if(getTopicName.equals("Latitude")){
                    latitude=(String)msg.obj;
                    Toast.makeText(HomeActivity.this,(String) msg.obj,Toast.LENGTH_SHORT).show();
                }
                if(getTopicName.equals("Help Signal")){
                    fallDown=(String) msg.obj;
                    Toast.makeText(HomeActivity.this,"warning!",Toast.LENGTH_SHORT).show();
                    //振动事件，等待一秒，振动一秒，重复三次
                    Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                    vibrator.vibrate(new long[]{1000,1000,1000,1000,1000,1000},-1);
                }


                if (msg.what == 1) {
                    //Toast.makeText(ApolloReceive.this, (String) msg.obj,
                    //Toast.LENGTH_SHORT).show();
                    System.out.println("-----------------------------");
                } else if (msg.what == 2) {
                    Toast.makeText(HomeActivity.this, "与服务器连接成功！", Toast.LENGTH_SHORT).show();
                    //showIsConnected.setText("与服务器连接成功！");
                    try {
                        //开始订阅
                        client.subscribe(heartRateTopic, 1);
                        client.subscribe(SpO2Topic, 1);
                        client.subscribe(longitudeTopic, 1);
                        client.subscribe(latitudeTopic, 1);
                        client.subscribe(FallDownTopic,1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.what == 3) {
                    Toast.makeText(HomeActivity.this, "与服务器连接失败，系统正在重连...", Toast.LENGTH_SHORT).show();
                    //showIsConnected.setText("与服务器连接失败，系统正在重连...");
                }
            }
        };
        startReconnect();

        bottomBar = findViewById(R.id.home_bottom_bar);
        initBottomBar();
    }

    private void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if (!client.isConnected()) {
                    connect();
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }

    private void init() {
        try {
            //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(host, "LED", new MemoryPersistence());
            //MQTT的连接设置
            options = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示
            // 服务器会保留客户端的连接记录，这里设置为true表示
            // 每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            //设置连接的用户名
            options.setUserName(userName);
            //设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒
            // 的时间向客户端发送个消息判断客户端是否在线，但这
            // 个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            //设置回调
            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                    //连接丢失后，一般在这里面进行重连
                    System.out.println("connectionLost----------");
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish后会执行到这里
                    System.out.println("deliveryComplete---------"
                            + token.isComplete());
                }

                @Override
                public void messageArrived(String topicName, MqttMessage message)
                        throws Exception {
                    //subscribe后得到的消息会执行到这里面
                    System.out.println("messageArrived----------");
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = message.toString();
                    //resultTv.setText(msg.obj.toString());

                    getTopicName=topicName;
                    handler.sendMessage(msg);
                }
            });
//			connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    client.connect(options);
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (client != null && keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                client.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            scheduler.shutdown();
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }



    //与碎片进行通信，提供心率,血氧，经纬度等数据
    public String[] getData(){
        String[] mqttData = {heartRate, spO2,latitude,longitude};
        //String[] mqttData1 = {"1","2","3","4"};
        return mqttData;
    }


    void initBottomBar(){
        bottomBar.setContainer(R.id.fl_container)
                .setTitleBeforeAndAfterColor("#999999", "#6e8bc0")
                .addItem(HomeFragment.class,
                        "首页",
                        R.drawable.ic_home_unselected,
                        R.drawable.ic_home_selected)
                .addItem(MallFragment.class,
                        "商城",
                        R.drawable.ic_mall_unselected,
                        R.drawable.ic_mall_selected)
                .addItem(HousewiferyFragment.class,
                        "家政护工",
                        R.drawable.ic_housewifery_unselected,
                        R.drawable.ic_housewifery_selected)
                .addItem(OnlineAppointmentFragment.class,
                        "送医预约",
                        R.drawable.ic_online_appointment_unselected,
                        R.drawable.ic_online_appointment_selected)
                .addItem(SettingsFragment.class,
                        "设置",
                        R.drawable.ic_settings_unselected,
                        R.drawable.ic_settings_selected)
                .build();
    }

}
