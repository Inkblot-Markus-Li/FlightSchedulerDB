package flightschedulerwenyli4233;
import java.sql.*;
import java.util.Calendar;

public class Booking {
    private static String URL = "jdbc:derby://localhost:1527/FlightSchedulerDBWenLi4233";
    private static String username = "java";
    private static String password = "java";
    
    private Connection connection = null;
    private PreparedStatement check = null;
    private PreparedStatement waitList = null;
    private PreparedStatement selectStatus = null;
    private PreparedStatement complete = null;
    private PreparedStatement getFlightSeats = null;
    private PreparedStatement checkWaitlist = null;
    private PreparedStatement getWaitlist = null;
    private PreparedStatement getBooklist = null;
    private PreparedStatement addingDate = null;
    private PreparedStatement waitlistFeedback = null;

 public Connection ConnectionToDB(){
          if (connection == null)
        {
            try
            {
               
                connection = DriverManager.getConnection(URL, username, password);
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        }
        return connection;
    }
    public Boolean createBooking(String date, String flightID, String name)
    {
        if (bookingCheckpoint(date, flightID, name))
            return true;
        else {
            Waitlisted(date, flightID, name);
            return false;
        }       
    }
    public void Waitlisted(String date, String flightID, String name){
                
                java.sql.Timestamp CurrentTS = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());            
                try{
                ConnectionToDB();
                
                waitList = connection.prepareStatement("INSERT into WAITLIST (W_NAME, W_DATE, W_FLIGHTID, W_TIMESTAMP) values (?, ?,?, ?)");
                
                waitList.setString(1, name);
                waitList.setString(2, date);
                waitList.setString(3, flightID);
                waitList.setTimestamp(4, CurrentTS);
                
                waitList.executeUpdate();
            }
            catch (SQLException except){
                except.printStackTrace();
            }
            waitList = null;
    }
     public String getWaitlist(String flightID, String date)
    {
        
        String out = "";
        ConnectionToDB();
        int count = 1;
        
        try {
            getWaitlist = connection.prepareStatement("SELECT * FROM WAITLIST");
            ResultSet rs = getWaitlist.executeQuery();
            
            while(rs.next()){

                String selectedName = rs.getString("W_NAME");
                String selectedFlightID = rs.getString("W_FLIGHTID");
                String selectedDate = rs.getString("W_DATE");
                java.sql.Timestamp TS = rs.getTimestamp("W_TIMESTAMP");
                
                
                    out = out.concat(count + ": " + selectedName + " is currently in the waitlist for flight "+selectedFlightID+" on "+selectedDate+". TS: [" + TS + "]\n");
                    count++;
          
            }
        }
        catch (Exception except){
                except.printStackTrace();
        } 
        getWaitlist = null;
        return out;
    }
       public String getBooklist(String flightID, String date)
    {
        
        String out = "";
        ConnectionToDB();
        int count = 1;
        
        try {
            getBooklist = connection.prepareStatement("SELECT * FROM BOOKING");
            ResultSet rs = getBooklist.executeQuery();
            
            while(rs.next()){

                String selectedName = rs.getString("B_NAME");
                String selectedFlightID = rs.getString("B_FLIGHTID");
                String selectedDate = rs.getString("B_DATE");
                java.sql.Timestamp TS = rs.getTimestamp("B_TIMESTAMP");
                
                
                    out = out.concat(count + ": " + selectedName + " is currently in the booklist for flight "+selectedFlightID+" on "+selectedDate+". TS: [" + TS + "]\n");
                    count++;
          
            }
        }
        catch (Exception except){
                except.printStackTrace();
        } 
        getBooklist = null;
        return out;
    }
   
    public boolean bookingCheckpoint(String date, String flightID, String name)
        {
        java.sql.Timestamp CurrentTS = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());   
        if (seatCheckpoint(date, flightID)){
            try{
                ConnectionToDB();
                
                check = connection.prepareStatement("INSERT into BOOKING (B_NAME, B_DATE, B_FLIGHTID, B_TIMESTAMP) values (?, ?,?, ?)");
                
                check.setString(1, name);
                check.setString(2, date);
                check.setString(3, flightID);  
                check.setTimestamp(4, CurrentTS);
                check.executeUpdate();

                check = null;

            }
            catch(SQLException e){
                e.printStackTrace();
            }
            return true;
        }
        else {
            return false;
        }
    }
    
    
    
    public Boolean seatCheckpoint(String date, String flightID){
        ConnectionToDB();
        int count = 0; 
        try 
        {
            complete = connection.prepareStatement("SELECT * FROM BOOKING"); 
            ResultSet set = complete.executeQuery(); 
            int realSeatsTaken = getSeatNumber(flightID); 
            while(set.next())
            {
                                        
                String selectedFlight = set.getString("B_FLIGHTID");
                String selectedDate = set.getString("B_DATE");
                
                if(selectedFlight.equals(flightID) && selectedDate.equals(date))
                {
                    count++;
                }  
                 
            }   
            if (realSeatsTaken <= count) 
            {
                complete = null;
                return false;
            }                                                         
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }  
        complete = null;
        return true;
        
    }        
    public int getSeatNumber(String flightID) 
    {
        ConnectionToDB();

    try {
           getFlightSeats = connection.prepareStatement("select * from FLIGHT"); 
           ResultSet rs = getFlightSeats.executeQuery();
            
            while(rs.next()){
                String selectedFlight = rs.getString("F_FLIGHTID");
                
                if(selectedFlight.equals(flightID))
                {
                    int selectedSeat = rs.getInt("F_SEATS");
                    return selectedSeat;
                }  
            }                 
        }
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }        
    
        return 0;
    }
    public String statusDisplay(String name){
        String status = "Status for: " + name + " \n ";
        String booked = "Booked Flights:\n";
        String waitlisted = "Waitlisted Flights:\n";
        int count = 1;
        ConnectionToDB();
        
        try {
            selectStatus = connection.prepareStatement("SELECT * FROM BOOKING");
            ResultSet rs = selectStatus.executeQuery();
            
            while(rs.next()){
                String selectedFlight = rs.getString("B_FLIGHTID");
                String selectedDay = rs.getString("B_DATE");
                String selectedName = rs.getString("B_NAME");
                java.sql.Timestamp TS = rs.getTimestamp("B_TIMESTAMP");
                
                if(selectedName.equals(name)){
                    booked = booked.concat(count + ": Flight " + selectedFlight + " on " + selectedDay + ". Reservation timestamp: [" + TS + "]\n");
                    count++;
                }
            }
            booked = booked.concat("\n");
            count = 1;
            
            checkWaitlist = connection.prepareStatement("SELECT * FROM WAITLIST");
            rs = checkWaitlist.executeQuery();
            while(rs.next()){
                String selectedFlight = rs.getString("W_FLIGHTID");
                String selectedDate = rs.getString("W_DATE");
                String selectedName = rs.getString("W_NAME");
                java.sql.Timestamp TS = rs.getTimestamp("W_TIMESTAMP");
                
                if(selectedName.equals(name)){
                    waitlisted = waitlisted.concat(count + ": Flight " + selectedFlight + " on " + selectedDate + ". Waitlisted timestamp: [" + TS + "]\n");
                }
            }
            waitlisted = waitlisted.concat("\n");
        }
        catch (Exception except){
                except.printStackTrace();
        }
        selectStatus = null;
        checkWaitlist = null;
        status = status.concat(booked);
        status = status.concat(waitlisted);
        return status;
        
        
    }
     public boolean ScheduledPassengers(String flightID) {
        ConnectionToDB();
        
        try {
            selectStatus = connection.prepareStatement("SELECT * FROM BOOKING");
            ResultSet rs = selectStatus.executeQuery();
            
            while(rs.next()){
                String selectedFlight = rs.getString("B_FLIGHTID");
                
                if(selectedFlight.equals(flightID)){
                    return true;
                }
            }
        }
        catch (Exception except){
                except.printStackTrace();
        }
        selectStatus = null;
        return false;
    }
     
      public void Rebooking(String flightID){
        ConnectionToDB();
         
        try {
            selectStatus = connection.prepareStatement("SELECT * FROM BOOKING");
            ResultSet rs = selectStatus.executeQuery();
            
            while(rs.next()){
                String selectedFlight = rs.getString("B_FLIGHTID");
                String selectedDate = rs.getString("B_DATE");
                String name = rs.getString("B_NAME");
                
                if(selectedFlight.equals(flightID))
                {
                    String renewedID = NewFlight(flightID, selectedDate);
                    DeleteBooking(name, selectedDate);
                    MakeReservation(selectedDate, renewedID, name);
                }
            }
        }
        catch (Exception except){
                except.printStackTrace();
        }
        selectStatus = null;
    }
        public Boolean MakeReservation(String date, String flightID, String name)
    {
        if (bookingCheckpoint(date, flightID, name))
            return true;
        else {
            Waitlisted(date, flightID, name);
            return false;
        }       
    }
         public String NewFlight(String flightID, String date){
        ConnectionToDB();
        try {
            selectStatus = connection.prepareStatement("SELECT * FROM FLIGHT");
            ResultSet rs = selectStatus.executeQuery();
            
            while(rs.next()){
                String selectedFlight = rs.getString("F_FLIGHTID");
                
                if((!selectedFlight.equals(flightID)) && seatCheckpoint(date, selectedFlight)){
                    return selectedFlight;
                }
            }
        }
        catch (Exception except){
                except.printStackTrace();
        }
        selectStatus = null;
        return null;
    }
         public void DeleteBooking(String name, String date){
        ConnectionToDB();
        String sql = "DELETE from BOOKING where B_NAME = ? and B_DATE = ?";
        try
        {
            selectStatus = connection.prepareStatement(sql);
            
            selectStatus.setString(1, name);
            selectStatus.setString(2, date);


            selectStatus.executeUpdate();
            selectStatus = null;

        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
        

    }
         public void AddNewDate(String date) {
        String sql = "INSERT into DATE(F_DATES) VALUES(?)";
        try{
            ConnectionToDB();
            addingDate = connection.prepareStatement(sql);

            addingDate.setString(1, date);

            addingDate.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        addingDate = null;
    }
            public boolean checkWaitlist(String name,String date)
    {
        ConnectionToDB();
        try 
        {
            waitlistFeedback = connection.prepareStatement("SELECT * from WAITLIST");
            ResultSet rs = waitlistFeedback.executeQuery();
            
            while(rs.next())
            {

                String selectedDate = rs.getString("W_DATE");
                String selectedName = rs.getString("W_NAME");
                
                if(selectedName.equals(name)&&selectedDate.equals(date))
                {
                    return true;
                
                }
            }
        }
        catch (Exception except)
        {
            except.printStackTrace();
        } 
        waitlistFeedback = null;
        return false;
    }
               public void DeleteFromWaitList(String name, String date)
    {
        ConnectionToDB();
        String sql = "DELETE from WAITLIST where W_NAME =? and W_DATE =?";
        try{
            selectStatus = connection.prepareStatement(sql);
            
            selectStatus.setString(1, name);
            selectStatus.setString(2, date);

                    
            selectStatus.executeUpdate();
            selectStatus = null;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
        public String customerInfo(String flightID, String date){


        ConnectionToDB();
        int x = 1;
        String info = "";
        try {
            
            selectStatus = connection.prepareStatement("SELECT * FROM BOOKING");
            ResultSet set = selectStatus.executeQuery();
            
            while(set.next()){

                String name = set.getString("B_NAME");
                
                
                info += x + ") " + name + " on flight " + flightID + "\n";
                x++;
                
            }
        }
        catch (Exception except){
                except.printStackTrace();
        }
        selectStatus = null;
        return info;
}
}
  

    
    
  
    
    
    



 