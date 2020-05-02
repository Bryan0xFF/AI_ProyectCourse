/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ia.bayes.ia_proyect;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Bryan
 */
public class App {
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionURL = "jdbc:sqlserver://urluniversity.database.windows.net:1433;database=IA_TRAIN_SET_DATABASE;user=IA_USER@urluniversity;password=@Bcde54321;"
                + "             encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        Connection con = DriverManager.getConnection(connectionURL);
        System.out.println("Nos conectamos");
        
        
        Statement st = con.createStatement();
        ResultSet result = st.executeQuery("SELECT imdb_score FROM Imdb");
        
        
        while(result.next()){
            String data = result.getString(1);
            System.out.println(data);
        }
        
        //INSERTION INTO DATABASE SINCE AZURE DOESNT ALLOW STUDENT SUBSCRIPTIONS TO USE BLOB STORAGE
        /*
        String[] lines = null;

        try {

            FileInputStream fstream = new FileInputStream("C://BULK/movie_metadata.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine = "";

//Read File Line By Line
int count = 0;
            while ((strLine = br.readLine()) != null) {
                
                count++;
                lines = strLine.split(",");
                
                for(int i = 0; i < lines.length; i++){
                    if(lines[i].length() == 0 && i != 2 && i!= 3 && i!= 4 && i!=5 && i !=7 && i !=8 && i != 12 && i != 13 && i!= 15 && i !=18 && i!= 22 && i != 23 && i != 24 && i !=25 && i != 26 && i !=27){
                        lines[i] = "NULL";
                    }
                    else if(lines[i].length() == 0){
                        lines[i] = "-12345";
                    }
                    
                    if(lines[i].contains("\"")){
                        lines[i].replaceAll("\"", "");
                    }
                }
                
                String query = "INSERT INTO Imdb values(" + "'" +lines[0].replace("'", "''") + "'" + "," + "'" + lines[1].replace("'", "''") + "'" + "," + Integer.parseInt(lines[2])
                        + "," + Integer.parseInt(lines[3]) + "," + Integer.parseInt(lines[4])
                        + "," + Integer.parseInt(lines[5]) + "," + "'" + lines[6].replace("'", "''") + "'" + "," + Integer.parseInt(lines[7])
                        + "," + Integer.parseInt(lines[8]) + "," + "'" + lines[9].replace("'", "''") + "'" + "," + "'" + lines[10].replace("'", "''") + "'" 
                                + "," + "'"+ lines[11].replace("'", "''") + "'"
                        + "," + Integer.parseInt(lines[12]) + ","
                        + Integer.parseInt(lines[13]) + "," + "'" + lines[14].replace("'", "''") + "'" + "," + Integer.parseInt(lines[15]) + ","
                        + "'" + lines[16].replace("'", "''") + "'" + "," + "'" + lines[17].replace("'", "''") + "'" + "," + Integer.parseInt(lines[18])
                        + "," + "'" + lines[19].replace("'", "''") + "'" + "," + "'" + lines[20].replace("'", "''") + "'" + "," + "'" + lines[21].replace("'", "''") 
                        + "'" + "," + Integer.parseInt(lines[22]) + 
                        "," + Integer.parseInt(lines[23]) + "," + Integer.parseInt(lines[24]) + "," + Double.parseDouble(lines[25]) + "," + Double.parseDouble(lines[26])
                        + "," + Integer.parseInt(lines[27].substring(0, lines[27].length() - 1)) + ")";
                
                query.replaceAll("\\b-12345\\b", "NULL");
                
                 
                
                st = con.createStatement();
                st.executeUpdate(query);
            }

//Close the input stream
            fstream.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(lines);
        }
        */
    }
}
