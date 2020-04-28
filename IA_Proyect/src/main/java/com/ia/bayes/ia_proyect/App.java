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
        String connectionURL = "jdbc:sqlserver://DESKTOP-6SPG2D8\\SQLEXPRESS:1433;databaseName=IA_TRAINING_SET;user=IA_USER;password=abcde54321";
        Connection con = DriverManager.getConnection(connectionURL);
        System.out.println("Nos conectamos");
        
        /*
        Statement st = con.createStatement();
        ResultSet result = st.executeQuery("Select * from Producto");
        
        while(result.next()){
            int Id = result.getInt(1);
        }
        */
    }
}
