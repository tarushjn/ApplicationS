/**
 * Created by User1 on 30/3/2017.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
//import org.*;

public class DBConnectionClass {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://brainwizfoundation.org/brainqdv_brainvis";
    static final String userName = "brainqdv_vis-adm";
    static final String dbName = "brainqdv_brainvis";
    static final String pass = "brainwiz@123";

    //Execute Main after regular intervals
    public static void main(String args[]) {
        createConnection();
    }

    //Create Connection when Main Executes
    public static void createConnection() {

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, userName, pass);
            checkUnprocessedImages(conn);

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
        }
    }

    //Check Unprocessed Images Table. Receive Connection as Argument
    public static void checkUnprocessedImages(Connection conn){

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql;
            sql = "Select * FROM unprocessed_images";
            int num = 0;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("image_ID");
                num = num + 1;
            }

            System.out.println("Total Images: " + num);

            //If 0 images Exist in Unprocessed Images Table, Exit and Close the Connection
            if(num == 0){
                System.out.println("No Images Exist.... Exiting!!!");
                rs.close();
                stmt.close();
                conn.close();
            }

            //If multiple images exist un Unprocessed Images Table
            //1. Map Image ID and URL from Unprocessed Images and Image Details Tables
            //2. For Each Image ID and URL Tuple and pass it on to Face Detector
            //3. Close the Connection

            else{


                System.out.println("Total :" + num + " images exist. Spawning Threads");
                Statement stmt1 = null;
                Statement stmt2 = null;
                String sql1 = "Select * FROM unprocessed_images";
                String sql2 = "Select * FROM image_details";
                ResultSet rs1,rs2;

                try {
                    stmt1 = conn.createStatement();
                    stmt2 = conn.createStatement();

                    rs1 = stmt1.executeQuery(sql1);
                    rs2 = stmt2.executeQuery(sql2);

                    while (rs1.next()) {
                        int id1 = rs1.getInt("image_ID");
                        while(rs2.next()){
                            int id2 = rs2.getInt("image_ID");
                            String url = rs2.getString("image_URL");
                            if(id1 == id2)
                            {
                                //System.out.println("Image ID  :" + id1 + "    is the same as Image ID :" + id2 + "    is Lying at URL :" + url);
                                CallAPIClass fd = new CallAPIClass(id1, url);
                                //fd.run();
                                new Thread(fd).start();
                                continue;
                            }
                            else
                                continue;
                        }
                        rs2.beforeFirst();
                    }
                }

                catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }

        catch(SQLException e) {
            e.printStackTrace();
        }

        try {
            conn.close();
        }

        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}