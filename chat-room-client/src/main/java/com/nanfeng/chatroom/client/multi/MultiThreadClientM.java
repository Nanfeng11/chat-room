package com.nanfeng.chatroom.client.multi;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 多线程版本客户端 读写分离 读写分别作为一个进程
 * Author：nanfeng
 * Created:2019/2/24
 */

//读取服务器发来信息的线程
class ReadFromServer implements Runnable{

    private Socket client;
    //通过构造方法传入通信的socket
    public ReadFromServer(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        Scanner readFromServer = null;

        //获取输入流，读取服务器发来的信息
        try {
            readFromServer = new Scanner(client.getInputStream());
            //Scanner遇到空格就会换行，设置遇到\n再换行
            readFromServer.useDelimiter("\n");

            //不断读取服务器信息
            while(true){
                if(readFromServer.hasNext()){
                    String str = readFromServer.nextLine();
                    System.out.println("服务器发来的信息为："+str);
                }
                if(client.isClosed()){
                    System.out.println("客户端已关闭");
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            readFromServer.close();
        }
    }
}

//向服务器发送信息的线程
class SendMessageToServer implements Runnable{

    private Socket client;

    public SendMessageToServer(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        //获取键盘输入，向服务器发送信息
        Scanner in = new Scanner(System.in);
        in.useDelimiter("\n");
        PrintStream sendMessageToServer = null;
        try {
            //获取输出流，向服务器发送消息
            sendMessageToServer = new PrintStream(client.getOutputStream(),true,"UTF-8");

            while(true){
                System.out.println("请输入要发送的信息。。。");
                if(in.hasNextLine()){
                    String strToServer = in.nextLine();
                    sendMessageToServer.println(strToServer);
                    if(strToServer.contains("byebye")){
                        System.out.println("关闭客户端");
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sendMessageToServer.close();
            in.close();
        }

    }
}

public class MultiThreadClientM {

    public static void main(String[] args) throws IOException {

        //建立与服务器连接
        Socket client = new Socket("127.0.0.1",6666);

        //创建读写进程与服务器通信
        Thread readThread = new Thread(new ReadFromServer(client));
        Thread sendThread = new Thread(new SendMessageToServer(client));
        readThread.start();
        sendThread.start();

    }

}
