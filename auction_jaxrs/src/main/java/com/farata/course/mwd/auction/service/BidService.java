package com.farata.course.mwd.auction.service;

import com.farata.course.mwd.auction.data.DataEngine;
import com.farata.course.mwd.auction.engine.AuctionEngine;
import com.farata.course.mwd.auction.engine.config.MainConfig;
import com.farata.course.mwd.auction.entity.Bid;
import com.farata.course.mwd.auction.entity.Product;
import com.farata.course.mwd.auction.entity.User;
import com.farata.course.mwd.auction.websocket.BidEndpoint;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Path("bid")
@Produces("application/json")
public class BidService {

    private DataEngine dataEngine;
    private AuctionEngine auction;
    private UserService userService;
    private static AtomicInteger bidCounter;
@Inject
MainConfig mainConfig;
    static{
        bidCounter = new AtomicInteger(0);
    }
    private BidEndpoint bidEndpoint;

    @Inject
    public void setBidEndpoint(BidEndpoint bidEndpoint) {
        this.bidEndpoint = bidEndpoint;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Inject
    public void setAuction(AuctionEngine auction) {
        this.auction = auction;
    }

    @Inject
    public void setDataEngine(DataEngine dataEngine) {
        this.dataEngine = dataEngine;
    }

    public BidService() {
    }

    private static final Logger log = Logger.getLogger(BidService.class.getName());

    @Inject
    MainConfig conf;

    @Resource(lookup ="java:/ConnectionFactory")
    ConnectionFactory connectionFactory;

    @Resource(lookup = "queue/incomingbids")
    Queue incQueue;


//    @GET
//    @Path("/{id}/")
//    public Bid getBid(@PathParam("id") int id, @Context HttpHeaders headers) {
//        return new Bid(id, new BigDecimal(42));
//    }

    @POST
    @Consumes("application/json")
    public Response placeBid(JsonObject bidJson) {
        Product product = dataEngine.findProductById(bidJson.getInt("productId"));
        User user = userService.getUserByID(bidJson.getInt("userId"));
        BigDecimal amount = new BigDecimal(bidJson.getString("amount"));
        int desiredQuantity = bidJson.getInt("desiredQuantity");

        Bid bid = new Bid(bidCounter.incrementAndGet(), product, amount, desiredQuantity, user, ZonedDateTime.now(), false);

        // Send a message to the queue
        sendBidToQueue(bid);

        return Response.ok().build();
    }



    private void sendBidToQueue(Bid bid){

        try (JMSContext context =
               connectionFactory.createContext(conf.DEFAULT_USERNAME, conf.DEFAULT_PASSWORD)) {
            log.info("\n Sending Bid to the queue");

            JMSProducer producer = context.createProducer();

            producer.send(incQueue, bid);
        }
    }

    private void getBidConfirmations() {

    }
}
