# AuctionApp-Driver
On – demand transportation services such as Uber and Lyft made it possible for people to book a cab at their location in minimum possible time. They offer a dynamic pricing model, in which fares are higher during periods of high demands of a ride. When rides are high in demand in the certain area, drivers and passengers have no option to accept or reject ride. To solve this problem, we have used a double auction mechanism, which allows multiple Drivers and multiple Passengers to simultaneously submit their bids to an auction marketplace. Passengers with higher bids will be matched with Drivers who had higher asking prices to ensure best possible arrangement. The marketplace determines who won the auction and then chooses a common reasonable price for winners. In this project, we created an Android application using Android SDK and Firebase SDK for drivers and passengers. The backend is built on Google’s scalable infrastructure Google App Engine. We have used a Google cloud endpoints framework for building the RESTFUL Webservices which can be shared by mobile clients and web clients. We have used Objectify, is a Java data access API, to access the Google App Engine datastore. The Auction Manager can start the auction for any location for a certain amount of time. Both Drivers and Passengers can participate in the auction by submitting their bids from an application. Matched Drivers and passengers will be notified as soon as the auction gets finished. The demonstration of the application shows the effective use of double auction mechanism for an effective cab auction
