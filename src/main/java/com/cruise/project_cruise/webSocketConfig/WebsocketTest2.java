package com.cruise.project_cruise.webSocketConfig;

import com.cruise.project_cruise.service.MypageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/testSocket")
@Service
public class WebsocketTest2 extends TextWebSocketHandler {

    //private static final List<Session> sessions = new ArrayList<Session>();
    private static final List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();

    Map<String, WebSocketSession> users = new ConcurrentHashMap<String, WebSocketSession>();



//    public String  getEmail (HttpSession emailSession){
//        String email = (String) emailSession.getAttribute("email");
//
//        return email;
//    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

       String email = getEmail(session);

        System.out.println("웹소켓 연결");
        System.out.println("연결된 세션 >>>>>>" + email);

        sessions.add(session);
        users.put(email, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        // 특정 유저에게 보낼 메시지 내용 추출
        String msg = message.getPayload();
        if(msg != null) {
            String[] strs = msg.split(",");
            log("먼 로그일까..??? "+strs.toString());
            if(strs != null && strs.length == 2) {
                String clientMsg = strs[0];
                String alertEmail = strs[1]; // 알림 갈 이메일

                System.out.println("알림 갈 이메일 >>>>>>>>>>" + alertEmail);

                WebSocketSession targetSession = users.get(alertEmail);  // 메시지를 받을 세션 조회

                System.out.println("두두둥 >>>>>>" + targetSession);
                // 실시간 접속시
                if(targetSession!=null) {
                    TextMessage tmpMsg = new TextMessage("알림 왔다..");
                    targetSession.sendMessage(tmpMsg);
                    System.out.println("갔다...!!!!");
                }
            }
        }
    }

    //연결이 끊어진 후
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("세션 닫음"+session.getId());
    }

    // 에러 발생시
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log(session.getId() + " 익셉션 발생: " + exception.getMessage());

    }
    // 로그 메시지
    private void log(String logmsg) {
        System.out.println(new Date() + " : " + logmsg);
    }

    // 웹소켓에 id 가져오기
    // 접속한 유저의 http세션을 조회하여 id를 얻는 함수
    private String getEmail(WebSocketSession session) {
        Map<String, Object> httpSession = session.getAttributes();
        String email = (String) httpSession.get("email"); // 세션에 저장된 m_id 기준 조회
        System.out.println("진짜 이메일 >>>>" + email);

        return email==null? null: email;
    }
}


