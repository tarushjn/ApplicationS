import java.sql.*;

/**
 * Created by tarushjain on 01/04/17.
 */

public class DataWriterClass {

    int imgid, no_of_faces;
    String url;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://brainwizfoundation.org/brainqdv_brainvis";
    static final String userName = "brainqdv_vis-adm";
    static final String dbName = "brainqdv_brainvis";
    static final String pass = "brainwiz@123";

    DataWriterClass(int id, int num, String path){
/*        this.imgid = id;
        this.url = path;
        this.no_of_faces = num;*/

        this.imgid = 107;
        this.url = "www.mainsite.brainwiz.in/107.jpg";
        this.no_of_faces = 10;
    }


/*
    public void main(String args[]){
        createConnection();
    }
*/


    public void createConnection() {

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, userName, pass);
            System.out.println("Connection Successful....!!!");
            writeData(conn);

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

    public void writeData(Connection conn){

        int flag = 0;
        System.out.println("Connection Successful....!!!");
        Statement stmt = null;
        String sql;

        try {
            stmt = conn.createStatement();
            sql = "SELECT * FROM image_results";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs == null) {
                System.out.println("Image does not exist... Editing Results ...!!");
                PreparedStatement pstmt = null;
                String sql_insert;
                sql_insert = "INSERT INTO image_results (image_ID, no_of_faces, image_results_url) VALUE (?,?,?)";
                pstmt = conn.prepareStatement(sql_insert);
                pstmt.setInt(1, imgid);
                pstmt.setInt(2, no_of_faces);
                pstmt.setString(3, url);
                pstmt.executeUpdate();

//              stmt.executeQuery(sql_insert);
                String sql_del;
                sql_del = "DELETE from unprocessed_images where image_ID=" + this.imgid;
                stmt.executeQuery(sql_del);
                String sql_ins;
                sql_ins = "insert into processed_images (image_ID) values (" + this.imgid + ")";
                stmt.executeQuery(sql_ins);

            }

            else{
                while (rs.next()) {
                    int data_id = rs.getInt("image_ID");
                    if (this.imgid == data_id) {
                        flag = 1;
                        continue;
                    }

                    if (flag == 0) {

                        System.out.println("Image does not exist... Editing Results ...!!");
                        PreparedStatement pstmt = null;
                        String sql_insert;
                        sql_insert = "INSERT INTO image_results (image_ID, no_of_faces, image_results_url) VALUE (?,?,?)";
                        pstmt = conn.prepareStatement(sql_insert);
                        pstmt.setInt(1, imgid);
                        pstmt.setInt(2, no_of_faces);
                        pstmt.setString(3, url);
                        pstmt.executeUpdate();

//                  stmt.executeQuery(sql_insert);
                        String sql_del;
                        sql_del = "DELETE from unprocessed_images where image_ID=" + this.imgid;
                        stmt.executeQuery(sql_del);
                        String sql_ins;
                        sql_ins = "insert into processed_images (image_ID) values (" + this.imgid + ")";
                        stmt.executeQuery(sql_ins);

                    }

                }
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
