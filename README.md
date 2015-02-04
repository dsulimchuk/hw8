Modern Web Application Development for Java Programmers Homework 8
==

###### Integrate the JMS code in the application from Unit 7 that uses AngularJS, RESTful service and WebSocket.
1. The Web browser creates WebSocket connection for recieving notification (register self sending current userId and productId)
2. The Web browser makes a Rest call when user bid on product (POST)
3. Rest creates a JMS Message and place it on incomingbids queue
4. Bidding engine comsumes messages, processing bids and sends Bid confirmation to JMS confirmation queue
5. JMS consumer receive the Bids confirmation from confirmation queue, and sends them to clients over WebSocket

###### to run demo:
1. In web folder run "npm install && bower install && grunt build".
2. In auction_jaxrs folder run "./gradlew build" and then manually deploy "auction_jaxrs-1.0.war" under WildFly
