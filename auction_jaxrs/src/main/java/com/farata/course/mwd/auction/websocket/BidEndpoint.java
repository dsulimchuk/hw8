package com.farata.course.mwd.auction.websocket;

import com.farata.course.mwd.auction.data.DataEngine;
import com.farata.course.mwd.auction.engine.AuctionEngine;
import com.farata.course.mwd.auction.entity.Product;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@ServerEndpoint("/api/ws")
public class BidEndpoint {
    private static final Logger log = Logger.getLogger(BidEndpoint.class.getName());
    private static Set<BidEndpoint> allEndpoints = new HashSet<>();
    private static final DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private DataEngine dataEngine;
    private Integer userId;
    private Integer productId;
    private Session session;
    public BidEndpoint() {
        log.info("!!!!!!!NEW BID ENDPOINT");
    }

    @Inject
    public void setDataEngine(DataEngine dataEngine) {
        this.dataEngine = dataEngine;
    }


    public transient List<Session> participantList = new ArrayList<>();

    @OnMessage
    public void onMessage(String textMessage, Session mySession) {
        String[] keys = textMessage.split(":");
        this.productId = Integer.parseInt(keys[0]);
        this.userId = Integer.parseInt(keys[1]);
        log.info("CONNECTED " + mySession);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        allEndpoints.remove(this);
        System.err.println("Closing: " + closeReason.getReasonPhrase());
    }

    @OnError
    public void onError(Session session, Throwable t) {
        System.err.println("Error: " + t.getLocalizedMessage());
    }

    @OnOpen
    public void showTime(Session session){
        this.session = session;
        allEndpoints.add(this);
        log.info("open new session" + session);
    }


    public static void sendToAllProductVisitors(Product product, AuctionEngine auction){

        allEndpoints.stream()
                .filter(s -> s.productId.equals(product.getId()))
                .forEach(s -> {
                    Session curSession = s.session;
                    if (curSession.isOpen()) {
                        try{
                            JsonObject productReport = auction.getJsonReportForProduct(product).build();
                            curSession.getBasicRemote().sendText("product::" + productReport);
                        } catch (IOException ioe) {
                            log.info(ioe.getMessage());
                        }
                    }
                });
    }
    public static void sendToUser(Integer userId, String msg){
        allEndpoints.stream()
                .filter(s -> s.userId.equals(userId))
                .forEach(s -> {
                    Session curSession = s.session;
                    if (curSession.isOpen()) {
                        try {
                            curSession.getBasicRemote().sendText("notification:" + msg);
                        } catch (IOException ioe) {
                            log.info(ioe.getMessage());
                        }
                    }
                });
    }


}
