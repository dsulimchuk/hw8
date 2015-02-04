package com.farata.course.mwd.auction.engine.service;

public class Notifications {
    public static String getMsg(Notification notification) {
        switch (notification) {
            case OK_YOUR_BID_IS_WINNING: return "ok:Your bid is winning";

            case OK_SUCCESS_PLACE_BID: return "ok:Bid accepted";

            case NOTIF_YOUR_BID_IS_OVERBIDDED: return "warning:Your bid is overbidded";

            case ERR_BID_LESS_MIN_PRICE: return "error:Error! Your bid is less then minimum price";

            case ERR_AUCT_IS_CLOSE:return  "error:Error! Auction is closed";
        }
        return null;
    }
}
