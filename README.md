# OOP With the Web - Flight Scheduler DB

Flight Scheduler GUI with the following functions utilizing Java & Apache Derby:

Booking Process: 

The customer will be able to book a flight for a requested date, if there are seats available. If seats are not available, the customer will be put on a waitlist for that flight. The waitlist must be maintained by timestamps or in other words, the order in which customers booked their flights (first in, first out).

Current Status:

The current status will display the customers that have been booked for a flight on a specified date.

Wait List: 

The waitlist will display all the customers waiting for flights on a specified date.

Add Flight Seats & Add New Flight:

The flight name will be a placeholder, string identifier and the number of seats will be a placeholder, integer. 

Cancel Booked Flight: 

Removes a booking for a specified customer on a specified date – if the customer is on the waitlist, his/her entry will be removed. 

Add Flight Date: 

Adds a newly available date for customers to book flight(s). 

Customer Status: 

The flight and date for each flight a customer has booked and/or is waitlisted for will be displayed.

Remove Flight Date: 

Any customers that had been booked for a flight on a specified date must be rebooked with another flight for that day if possible in priority sequence. Any customers on the waitlist for that flight will also be deleted.

