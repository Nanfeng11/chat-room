package com.nanfeng.chatroom.client.single;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Author：nanfeng
 * Created:2019/2/24
 */
public class SingleThreadClientM {

    public static void main(String[] args) throws IOException {

        Socket client = null;
        Scanner readFromServer = null;
        PrintStream writeMessageToServer = null;

        try {
            //尝试与服务器进行连接
            client = new Socket("127.0.0.1",6666);

            //获取此连接的输入输出流
            readFromServer = new Scanner(client.getInputStream());
            writeMessageToServer = new PrintStream(client.getOutputStream());
            //进行数据的输入输出
            writeMessageToServer.println("Hi,I'm Client!");
            if(readFromServer.hasNext()){
                System.out.println("服务器发来的消息为："+readFromServer.nextLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            client.close();
            readFromServer.close();
            writeMessageToServer.close();
        }

    }

}
