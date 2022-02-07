package flightschedulerwenyli4233; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database
{
    private static Connection connection;
    private static final String URL = "jdbc:derby://localhost:1527/flightschedulerdbwenli4233";
    private static final String username = "java";
    private static final String password = "java";
    
    private static ArrayList<String> flightIDList;
    private static ArrayList<String> datesList;
    private static ArrayList<String> flightNames;
    private static ArrayList<String> availableDates;
    private static ArrayList<String> customers;
    private static PreparedStatement getAllFlights;
    private static PreparedStatement getAllDates;
    private static PreparedStatement getAllCustomers;
    private static PreparedStatement insertNewCustomer;
    private static PreparedStatement flightComp;
    private static PreparedStatement seatComp;
    private static PreparedStatement deleteComp;
    private static ResultSet rs;

    
    
      public static Connection getConnection()
    {
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
    public Database()
    {
        try{
            flightIDList = new ArrayList();
            datesList = new ArrayList();
            connection = DriverManager.getConnection(URL, username, password);
            
            getAllDates = connection.prepareStatement("SELECT * FROM DATE");
            getAllDates.execute();
            ResultSet allDates = getAllDates.getResultSet();
            
            int n = 0;
            while(allDates.next()){
                datesList.add(allDates.getString("F_DATES"));
                n++;
            }
            
            getAllFlights = connection.prepareStatement("SELECT * FROM FLIGHT");
            getAllFlights.execute();
            ResultSet allFlights = getAllFlights.getResultSet();
            
            int m = 0;
            while(allFlights.next()){
                flightIDList.add(allFlights.getString("F_FLIGHTID"));
                m++;
            }
            
        }
        catch (SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public ArrayList getFlightList(){
        getConnection();
        flightNames = new ArrayList();
        try{
            getAllFlights.execute();
            ResultSet rs1 = getAllFlights.getResultSet();
            
            while(rs1.next()){
                flightNames.add(rs1.getString("F_FLIGHTID"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
        
        return flightNames;
    }
    
     public void addFlight(String flightID, int maxSeats) {
        getConnection();
        String sql = "insert into FLIGHT(F_FLIGHTID, F_SEATS) VALUES(?,?)";
        
        try{
            flightComp = connection.prepareStatement(sql);

            flightComp.setString(1, flightID);
            flightComp.setInt(2, maxSeats);

            flightComp.executeUpdate();
            flightComp = null;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public ArrayList getDatesList(){
        getConnection();
        availableDates = new ArrayList();
        try{
            getAllDates.executeQuery();
            ResultSet rs2 = getAllDates.getResultSet();
            while(rs2.next()){
                availableDates.add(rs2.getString("F_DATES"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
        return availableDates;
    }

    public static ArrayList<String> getAllCustomers() 
    {
        try
        {
            getConnection();
            customers = new ArrayList();
            getAllCustomers = connection.prepareStatement("select C_NAME from CUSTOMER");
            rs = getAllCustomers.executeQuery();
            while (rs.next())
            {
                customers.add(rs.getString(1));
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return customers;

    }
    public static void addNewCustomer(String customer) 
    {
        try
        {
            getConnection();
            insertNewCustomer = connection.prepareStatement("insert into CUSTOMER (C_NAME) values (?)");
            insertNewCustomer.setString(1, customer);
            insertNewCustomer.executeUpdate();

        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
     void deleteFlight(String flightID) {
        getConnection();
        String sql = "DELETE from FLIGHT where F_FLIGHTID = ?";
        
        try{
            flightComp = connection.prepareStatement(sql);

            flightComp.setString(1, flightID);

            flightComp.executeUpdate();
            flightComp = null;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }    
     void deleteDropWaitlist(String flightID) {
        getConnection();
        String sql = "DELETE from WAITLIST where W_FLIGHTID = ?";
        
        try{
            deleteComp = connection.prepareStatement(sql);

            deleteComp.setString(1, flightID);

            deleteComp.executeUpdate();
            deleteComp = null;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }    
      public int getMaxSeats(String flightID){
        getConnection();
        try {
            seatComp = connection.prepareStatement("SELECT * FROM FLIGHT");
            ResultSet rs = seatComp.executeQuery();
            
            while(rs.next()){
                String selectedFlight = rs.getString("F_FLIGHTID");
                int maxSeats = rs.getInt("F_SEATS");
                
                if(selectedFlight.equals(flightID)){
                   
                    return maxSeats;
                }
            }
        }
        catch (Exception except){
                except.printStackTrace();
        }
        return 0;
    }
        
    }
        
