package com.shmiy.rabbitmq.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) {
        // 1.创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("182.92.217.213");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");

        Connection connection = null;
        Channel channel = null;

        try {
            // 2.创建连接 Connection
            connection = connectionFactory.newConnection("生产者1");
            // 3.通过连接获取通道 Channel
            channel = connection.createChannel();
            // 4.通过通道声明交换机，声明队列，绑定关系，路由key，发送消息和接收消息
            String queueName = "queue1";
            /**
             * @params1 队列名
             * @params2 是否持久化
             * @params3 排他性
             * @params4 随着最后一个消费者消费后，是否最后删除
             * @params5 携带附属参数
             */
            channel.queueDeclare(queueName, true, false, false, null);
            // 5.准备消息内容
            String msg = "hello world111";

            String exchangeName = "fanout-exchange";

            String routeKey = "";

            String exchangeType = "fanout";
            // 6.发送消息给队列 Queue
            channel.basicPublish(exchangeName,routeKey, null, msg.getBytes());
            System.out.println("消息发送成功");

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


}
