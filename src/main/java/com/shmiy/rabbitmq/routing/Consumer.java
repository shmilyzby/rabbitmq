package com.shmiy.rabbitmq.routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static Runnable runnable = new Runnable() {
        public void run() {
            // 1.创建连接工厂
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("182.92.217.213");
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("admin");
            connectionFactory.setPassword("admin");
            connectionFactory.setVirtualHost("/");

            final String queueName = Thread.currentThread().getName();
            Connection connection = null;
            Channel channel = null;

            try {
                // 2.创建连接 Connection
                connection = connectionFactory.newConnection("生产者1");
                // 3.通过连接获取通道 Channel
                channel = connection.createChannel();
                // 4.通过通道声明交换机，声明队列，绑定关系，路由key，发送消息和接收消息
//                channel.basicConsume(queueName, true, new DeliverCallback() {
//                    public void handle(String consumerTag, Delivery message) throws IOException {
//                        System.out.println("收到消息是" + Thread.currentThread().getName() + ":  " + new String(message.getBody(), "utf-8"));
//                    }
//                }, new CancelCallback() {
//                    public void handle(String consumerTag) throws IOException {
//                        System.out.println("接受失败。。。");
//                    }
//                });

                Channel finalChannel = channel;
                finalChannel.basicConsume(queueName, true, new DeliverCallback() {

                    public void handle(String consumerTag, Delivery delivery) throws IOException {
                        System.out.println(delivery.getEnvelope().getDeliveryTag());
                        System.out.println(queueName + "收到消息是" + new String(delivery.getBody(), "utf-8"));
                    }
                }, new CancelCallback() {
                    public void handle(String consumerTag) throws IOException {
                        System.out.println("接受失败。。。");
                    }
                });

                System.out.println("开始接受消息");
                System.in.read();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }finally {
                // 7.关闭连接
                if (channel != channel){
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
                // 8.关闭通道
                if (connection != null){
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    };


    public static void main(String[] args) {
        new Thread(runnable,"queue1").start();
        new Thread(runnable,"queue2").start();
        new Thread(runnable,"queue3").start();
    }
}
