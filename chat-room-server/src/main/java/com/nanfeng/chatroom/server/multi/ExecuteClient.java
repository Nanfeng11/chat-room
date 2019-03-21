package com.nanfeng.chatroom.server.multi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端处理客户端连接的任务
 * 1、注册
 * 2、私聊
 * 3、群聊
 * 4、退出
 * 5、显示当前在线用户《待扩展》
 * 6、统计用户活跃度《待扩展》
 * Author：nanfeng
 * Created:2019/2/24
 */

public class ExecuteClient implements Runnable {

    //为什么是static?
        //因为存放当前在线的所有用户，如果是一个private Map，Map隶属于ExcuteClient的成员属性，
        // 这样它属于对象，然后后边每次都new了一个对象，意味着Map会有多个，不能共享，所以是static
    //为什么选一个安全的Map？
        //当前是一个多线程的聊天室，Map将会被多个线程访问，为了保证线程安全，我们得选一个安全的Map实现类
    /**
     * 在线用户集合
     */
    private static final Map<String,Socket> ONLINE_USER_MAP = new ConcurrentHashMap<>();

    //与客户端交互，传一个客户端
    private final Socket client;

    public ExecuteClient(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        try {

            //获取客户端输入
            InputStream clientInput = this.client.getInputStream();
            Scanner scanner = new Scanner(clientInput);
            /**
             * 1、注册：userName:<name>
             * 2、私聊：private:<name>:<message>
             * 3、群聊：group:<message>
             * 4、退出：bye
             */
            while(true){

                String line = scanner.nextLine();
                if(line.startsWith("userName")){
                    String userName = line.split("\\:")[1];
                    //TODO 参数校验
                    this.register(userName,client);
                    continue;
                }
                if(line.startsWith("private")){
                    String userName = line.split("\\:")[1];
                    String message = line.split("\\:")[2];
                    //TODO 参数校验
                    this.privateChat(userName,message);
                    continue;
                }
                if(line.startsWith("group")){
                    String message = line.split("\\:")[1];
                    //TODO 参数校验
                    this.groupChat(message);
                    continue;
                }
                if(line.equals("bye")){
                    this.quit();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void quit() {

        String currentUserName = this.getCurrentUserName();
        System.out.println("用户："+currentUserName+"下线");
        Socket socket = ONLINE_USER_MAP.remove(currentUserName);
        this.sendMessage(socket,"bye");
        printOnlineUser();

    }

    private void groupChat(String message) {

        for(Socket socket:ONLINE_USER_MAP.values()){
            if(socket.equals(this.client)){
                continue;
            }
            this.sendMessage(socket,this.getCurrentUserName()+"说："+message);
        }

    }

    private void privateChat(String userName, String message) {

        String currentUserName = this.getCurrentUserName();

        Socket target = ONLINE_USER_MAP.get(userName);
        if(target!=null){
            this.sendMessage(target,currentUserName+" 对你说： "+message);
        }
    }

    private void register(String userName, Socket client) {
        System.out.println(userName+"加入到聊天室"+client.getRemoteSocketAddress());
        ONLINE_USER_MAP.put(userName,client);
        printOnlineUser();
        sendMessage(this.client,userName+"注册成功！");
    }

    private String getCurrentUserName(){
        //遍历Map
        String currentUserName = "";
        for(Map.Entry<String,Socket> entry : ONLINE_USER_MAP.entrySet()){
            if(this.client.equals(entry.getValue())){
                currentUserName = entry.getKey();
                break;
            }
        }
        return currentUserName;
    }

    private void sendMessage(Socket socket, String message){
        try {
            OutputStream clientOutPut = socket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(clientOutPut);
            writer.write(message+"\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printOnlineUser(){
        System.out.println("当前在线人数："+ ONLINE_USER_MAP.size()+"\n"+"用户名如下列表：");
        for(Map.Entry<String,Socket> entry : ONLINE_USER_MAP.entrySet()){
            System.out.println(entry.getKey());
        }
    }

}
