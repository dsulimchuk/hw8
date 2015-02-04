package com.farata.course.mwd.auction.engine;

import com.farata.course.mwd.auction.entity.Bid;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.function.Consumer;

/**
 * Created by ds on 02/02/15.
 */
public class BidsMessageListener implements javax.jms.MessageListener{
    private Consumer consumer;
    public BidsMessageListener(Consumer<Bid> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onMessage(Message message) {
        Bid bid = null;
        try {
            bid = message.getBody(Bid.class);
            consumer.accept(bid);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
