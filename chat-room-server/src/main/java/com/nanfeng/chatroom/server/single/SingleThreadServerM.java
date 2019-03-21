package com.nanfeng.chatroom.server.single;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Author：nanfeng
 * Created:2019/2/24
 */
public class SingleThreadServerM {

    public static void main(String[] args ) throws Exception{

        ServerSocket serverSocket = null;
        Scanner readFromClient = null;
        PrintStream sendMessageToClient = null;

        try {
            //建立服务端基站
            serverSocket = new ServerSocket(6666);
            System.out.println("等待客户端连接。。。");

            //一直阻塞到有客户端连接
            Socket client = serverSocket.accept();
            System.out.println("有新的客户端连接，端口号为："+client.getPort());
            System.out.println(client.getLocalPort());

            //获取此链接的输入输出流
            //输入使用Scanner，输出使用打印流
           readFromClient = new Scanner(client.getInputStream());
           sendMessageToClient = new PrintStream(client.getOutputStream(),true,"UTF-8");
            //数据的输入输出
            if(readFromClient.hasNext()){
                System.out.println("客户端说："+readFromClient.nextLine());
            }
            sendMessageToClient.println("Hi,I'm server!");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //基站关闭
            serverSocket.close();
            //关闭包装流
            readFromClient.close();
            sendMessageToClient.close();
        }
    }

}
