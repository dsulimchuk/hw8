package com.farata.course.mwd.auction.engine;

import com.farata.course.mwd.auction.entity.Bid;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ds on 02/02/15.
 */
public class BidsMessageListener implements javax.jms.MessageListener{
    private static final Logger log = Logger.getLogger(BidsMessageListener.class.getName());
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
            log.log(Level.SEVERE, "Exception: ", e);
        }
    }
}
