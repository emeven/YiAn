package com.example.yian.Apollo;

import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/*基于Mqtt的提供消息发布的类*/
public class Publish {
    private static final long serialVersionUID = 1L;
    private MqttClient client;
    private String host = "tcp://101.132.173.189:1883";
    private String userName = "admin";
    private String passWord = "IOT423!!!";
    private MqttTopic topic;
    private MqttMessage message;
    private String myTopic = "LED";
    private String mes="H";
    public static Publish serv;


    public static Publish getInstance(String clind) {
        if (serv == null) {
            serv = new Publish(clind);
        }
        return serv;
    }

    public Publish(String clindeId) {
        try {
            client = new MqttClient(host, clindeId,
                    new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void open(Context context) {
        if (client != null) {
            connect();
            //Toast.makeText(context, "服务器打开", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOpen() {
        boolean connected = false;
        if (client != null) {
            connected = client.isConnected();
        }
        return connected;
    }

    public void close(Context context) {
        if (client.isConnected()) {
            try {
                client.disconnect();
                serv = null;
                Toast.makeText(context, "服务器关闭连接", Toast.LENGTH_SHORT).show();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void setMessage(String s) {
        this.mes = s;
        MqttDeliveryToken token = null;
        try {
            message = new MqttMessage();
            message.setQos(1);
            message.setRetained(true);
            System.out.println(message.isRetained() + "------ratained状态");
            message.setPayload(mes.getBytes());
            token = topic.publish(message);
            token.waitForCompletion();
            System.out.println(token.isComplete() + "========");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("connectionLost-----------");
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete---------" + token.isComplete());
                }

                @Override
                public void messageArrived(String topic, MqttMessage arg1)
                        throws Exception {
                    System.out.println("messageArrived----------");
                }
            });
            topic = client.getTopic(myTopic);
            client.connect(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
