/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ia.bayes.ia_proyect;

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
        ResultSet result = st.executeQuery("SELECT director_name FROM Imdb");
        
        
        while(result.next()){
            String data = result.getString(1);
            System.out.println(data);
        }
        
    }
}
