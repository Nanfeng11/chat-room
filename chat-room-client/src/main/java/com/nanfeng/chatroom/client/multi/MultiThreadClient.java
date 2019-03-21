package com.nanfeng.chatroom.client.multi;

import java.io.IOException;
import java.net.Socket;

/**
 * Author：nanfeng
 * Created:2019/2/24
 */
public class MultiThreadClient {

    public static void main(String[] args) {

        try {
            int port = 6666;
            if(args.length>0){
                try {
                    port = Integer.parseInt(args[0]);
                }catch (NumberFormatException e){
                    System.out.println("端口参数不正确，采用默认端口"+port);
                }
            }
            String host = "127.0.0.1";
            if(args.length>1){
                host = args[1];
                //对host进行格式校验
//                if(!cheak(host)){
//                    System.out.println("host参数不对，采用默认host"+host);
//                }
            }

            final Socket socket = new Socket(host,port);
            //往服务器发送数据
            new WriteDataToServerThread(socket).start();
            //从服务器读取数据
            new ReadDataFromServerThread(socket).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
