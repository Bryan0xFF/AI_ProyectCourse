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
import java.util.HashMap;

/**
 *
 * @author Bryan
 */
public class Laplace_Smooth {

    public HashMap<String, Double> Calculate_Laplace(String RV, int k, HashMap<String, Double> priori) {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionURL = "jdbc:sqlserver://urluniversity.database"
                    + ".windows.net:1433;database=IA_TRAIN_SET_DATABASE;user="
                    + "IA_USER@urluniversity;password=@Bcde54321;"
                    + "encrypt=true;trustServerCertificate=false;"
                    + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionURL);

            Statement st = con.createStatement();
            ResultSet result = st.executeQuery("SELECT rating FROM Imdb");

            while (result.next()) {
                String data = result.getString(1);
                System.out.println(data);
            }

            //compute laplacian
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        
        return null;
    }
    
    /**
     * compute first prior probabilities for our model, due time 
     * the compute is not scalable, meaning by force it needs to be 
     * Boolean values, until coming updates
     * @param k
     * @return 
     */
    public HashMap<String, Double> Calculate_Laplace(int k){
        
        HashMap<String, Double> prioriTable = new HashMap<>();
        Double count1 = 0d;
        Double count2 = 0d;
        
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionURL = "jdbc:sqlserver://urluniversity.database"
                    + ".windows.net:1433;database=IA_TRAIN_SET_DATABASE;user="
                    + "IA_USER@urluniversity;password=@Bcde54321;"
                    + "encrypt=true;trustServerCertificate=false;"
                    + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionURL);

            Statement st = con.createStatement();
            ResultSet result = st.executeQuery("SELECT imdb_score FROM Imdb WHERE imdb_score > 5");

            while (result.next()) {
                count1 = result.getDouble(1);
                System.out.println(count1);
            }
            
            st = con.createStatement();
            result = st.executeQuery("SELECT count(*) FROM Imdb");
            
            while(result.next()){
                count2 = result.getDouble(1);
                count2 = count2 - count1;
                System.out.println(count2);
            }

            //compute laplacian
            Double prob = (count1 + k)/((count1 + count2) + k*2);
            prioriTable.put("rating", prob);
            
            prob = (count2 + k)/((count1 + count2) + k*2);
            prioriTable.put("no rating", prob);
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        
        //apply natural log to compute probabilities in the log-space
        //due to floating point underflow
        
        return prioriTable;
    }

}
