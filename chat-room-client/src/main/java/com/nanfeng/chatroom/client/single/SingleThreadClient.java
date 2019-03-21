package com.nanfeng.chatroom.client.single;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Author：nanfeng
 * Created:2019/2/23
 */
public class SingleThreadClient {

    public static void main(String[] args) {

        try {

            //通过命令行获取参数
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

            //创建客户端，连接到服务器
            Socket clientSocket = new Socket(host,port);

            //发送数据，接收数据
            //发送数据
            OutputStream clientOutput = clientSocket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(clientOutput);
            writer.write("你好，我是客户端\n");
            //网络传输，数据多，报文分包，到客户端组装（PrientStream这个类有自动flush）
            writer.flush();
            //接收数据
            InputStream clientInput = clientSocket.getInputStream();
            Scanner scanner = new Scanner(clientInput);
            String serverData = scanner.nextLine();
            System.out.println("来自服务器端的数据："+serverData);

            //客户端关闭
            clientSocket.close();
            clientInput.close();
            clientOutput.close();
            System.out.println("客户端关闭");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
