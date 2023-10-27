package com.cruise.project_cruise.webSocketConfig;

import com.cruise.project_cruise.service.MypageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/testSocket")
@RestController
public class WebsocketTest {

    //private static final List<Session> sessions = new ArrayList<Session>();
    private static final List<Session> sessions = new ArrayList<Session>();

    Map<String, WebSocketSession> users = new ConcurrentHashMap<String, WebSocketSession>();

    @Autowired
    private MypageService mypageService;

    @OnOpen
    public void open(Session socketSession){
        System.out.println("웹소켓 연결");
        sessions.add(socketSession);
        System.out.println("세션에 올라간 user >>>>>>>"+socketSession.getId());
    }

    @OnMessage
    public void getMsg(Session socketSession, String msg) throws Exception{

        //세션에서 가져온 이메일
        //String email = (String) session.getAttribute("email");

        //my_alert에서 크루넘으로 비교해 가져온 이메일들과 세션에서 가져온 이메일들을 비교해서
        //같으면 알림 보내고 다르면 안보내기

        //String alertEmail = mypageService.getAlertEmail(crewNum);

        String[] strs = msg.split(","); //,로 구부
        if(strs != null && strs.length == 2){
            String clientMsg = strs[0];
            String alertEmail = strs[1];

            System.out.println("알림 갈 이메일 >>>>>>>>>>" + alertEmail);



        }


//        for (int i = 0; i < sessions.size(); i++) {
//            if (!socketSession.getId().equals(sessions.get(i).getId())) {
//                try {
//                    sessions.get(i).getBasicRemote().sendText("상대 : "+msg);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }else{
//                try {
//                    sessions.get(i).getBasicRemote().sendText("나 : "+msg);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }


    @OnClose
    public void close(Session socketSession) {
        sessions.remove(socketSession);
        System.out.println("세션 닫음"+socketSession.getId());

    }

//    @OnError
//    public void onError(Session session, Throwable thr) {}


}
