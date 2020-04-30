package com.geofuturelab.pswmm.Socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geofuturelab.pswmm.Config.GetHttpSessionConfigurator;
import com.geofuturelab.pswmm.Entity.SOMessage;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/SimulationOptionsSocket/{roomId}", configurator = GetHttpSessionConfigurator.class)
public class SimulationOptionsSocket {

    private Session session=null;
    private static final Map<String, Map<String,SimulationOptionsSocket>> rooms =new ConcurrentHashMap<>();
    private static JSONObject memberInfos = new JSONObject();

    @OnOpen
    public void onOpen(@PathParam("roomId") String roomId, Session session){
        this.session=session;
        if (!rooms.containsKey(roomId)){
            Map<String,SimulationOptionsSocket> room=new ConcurrentHashMap<>();
            rooms.put(roomId,room);
        }
    }

    @OnMessage
    public void onMessage(@PathParam("roomId") String roomId, String message){
        SOMessage messageObject = JSON.parseObject(message,SOMessage.class);
        String messageType = messageObject.getType();
        switch (messageType){
            case "connect":{
                JSONObject userInfo = messageObject.getUserInfo();
                String userId =userInfo.getString("userId");
                memberInfos.put(userId, userInfo);
                rooms.get(roomId).put(userId, this);
                broadcastMembersToRoom(roomId);
                break;
            }
            case "operation":{
                broadcastMessageToRoom(roomId, message);
            }
        }
    }

    @OnClose
    public void onClose(@PathParam("roomId") String roomId)
    {
        someoneOffline(roomId);
    }

    @OnError
    public void onError(@PathParam("roomId") String roomId,Throwable error)
    {
        someoneOffline(roomId);
    }

    private void broadcastMembersToRoom(String roomId){
        ArrayList<JSONObject> members = new ArrayList<>();
        for (Map.Entry<String, SimulationOptionsSocket> server : rooms.get(roomId).entrySet()) {
            members.add((JSONObject) memberInfos.get(server.getKey()));
        }
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "members");
        messageObject.put("memberList", members);
        for (Map.Entry<String, SimulationOptionsSocket> server : rooms.get(roomId).entrySet()) {
            try {
                server.getValue().session.getBasicRemote().sendText(messageObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void broadcastMessageToRoom(String roomId,String message){
        for (Map.Entry<String, SimulationOptionsSocket> server : rooms.get(roomId).entrySet()) {
            if (!this.equals(server.getValue())) {
                try {
                    server.getValue().session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void someoneOffline(String roomId){
        for (Map.Entry<String, SimulationOptionsSocket> server : rooms.get(roomId).entrySet()) {
            if (server.getValue().equals(this)) {
                rooms.get(roomId).remove(server.getKey());
                memberInfos.remove(server.getKey());
                break;
            }
        }
        broadcastMembersToRoom(roomId);
    }
}
