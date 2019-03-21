package com.nanfeng.chatroom.server.multi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author：nanfeng
 * Created:2019/2/24
 */
public class MultiThreadServer {

    public static void main(String[] args) {

        //默认端口6666
        int port = 6666;
        if(args.length>0){
            try {
                port = Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                System.out.println("端口参数不正确，采用默认端口"+port);
            }
        }

        //准备线程池（4种---自己实例化；单线程池；fixed固定容量的，可重复使用的；无限制的catch）
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("等待客户端连接。。。");
            while (true){
                Socket client = serverSocket.accept();
                //提交任务
                executorService.submit(new ExecuteClient(client));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
