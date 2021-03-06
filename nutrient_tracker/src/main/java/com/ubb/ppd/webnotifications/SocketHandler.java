package com.ubb.ppd.webnotifications;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ubb.ppd.domain.model.Entity;
import com.ubb.ppd.domain.model.adapters.LocalDateTimeAdapter;
import com.ubb.ppd.domain.model.dto.DTO;
import com.ubb.ppd.domain.model.notification.Action;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class SocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ReentrantLock reentrantLock = new ReentrantLock(true);
    public final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
            .serializeNulls()
            .create();

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        for (WebSocketSession webSocketSession : this.sessions) {
            if (!webSocketSession.equals(session)) {
                webSocketSession.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        this.sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        this.sessions.remove(session);
    }

    public <E extends Entity<T>, T extends Serializable> void notifySessions(DTO<E, T> entity, Action action, long userId) throws Exception {
        var data = new HashMap<String, Object>();
        data.put("entity", entity);
        data.put("userId", userId);

        var payload = new HashMap<String, Object>();
        payload.put("data", data);
        payload.put("actionType", action.toString());
        TextMessage textMessage = new TextMessage(gson.toJson(payload));
        for (WebSocketSession webSocketSession : this.sessions) {
            try {
                reentrantLock.lock();
                webSocketSession.sendMessage(textMessage);
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}
