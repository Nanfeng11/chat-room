package com.nanfeng.chatroom.client.multi;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Author：nanfeng
 * Created:2019/2/24
 */
public class WriteDataToServerThread extends Thread{

    private final Socket client;

    public WriteDataToServerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        try{
            OutputStream clientOutput = client.getOutputStream();
            //writer准备给服务器发数据
            OutputStreamWriter writer = new OutputStreamWriter(clientOutput);
            //Scanner用来从命令行读数据
            Scanner scanner = new Scanner(System.in);
            while (true){
                System.out.println("请输入消息：");
                String message = scanner.nextLine();
                //给服务器发数据
                writer.write(message+"\n");
                writer.flush();

                if(message.equals("bye")){
                    //表示客户端要关闭
                    client.close();
                    break;
                }
            }

        }catch (IOException e){

        }

    }
}
