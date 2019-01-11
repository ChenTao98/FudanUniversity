package com.app.zpvoh.imgcluster.sqlUtils;

import java.io.IOException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

public class SCP {

    private volatile static SCP scpInstance;

    private String user;
    private String pass;
    private String host;
    private Connection connection;
    private SCPClient scpClient;
    private Boolean isAuthed;

    private SCP(String user, String pass, String host){
        this.user = user;
        this.pass = pass;
        this.host = host;
    }

    public static SCP getScpUtilsInstance(String user, String pass, String host){

        if(scpInstance == null) {
            synchronized(SCP.class) {
                if(scpInstance == null) {
                    scpInstance = new SCP(user,pass,host);
                }
            }
        }
        return scpInstance;
    }


    public void connect(){
        connection = new Connection(host);
        try {
            connection.connect();
            isAuthed = connection.authenticateWithPassword(user,pass);
            // scp 连接
            scpClient = connection.createSCPClient();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    public void close(){
        connection.close();
//        sftPv3Client.close()
    }

    public boolean getIsAuthed(){
        return isAuthed;
    }

    // 拷贝文件到服务器
    public void putFile(String filePath, String aimPath){
        try {
            if(scpClient != null){
                scpClient.put(filePath,aimPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}