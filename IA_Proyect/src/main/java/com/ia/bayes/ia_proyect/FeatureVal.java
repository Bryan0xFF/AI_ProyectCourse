/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ia.bayes.ia_proyect;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Bryan
 */
public class FeatureVal {
    
    private HashMap<String, Double> Feature = new HashMap<>();
    
    //TODO: Method which calculates argmax of CPT
    
    /**
     * A setter for both Value k and probability prob for creating 
     * their CPT table
     * @param k
     * @param prob 
     */
    public void SetVal(String k, Double prob){
        
        Feature.put(k, prob);
        
    }
    
    public Double getVal(String k) {
     return Feature.get(k);
    }
    
    /**
     * Use to compute their corresponding CPT given a random variable RV.
     * To compute:
     * 1. Get count of every distinct Fi
     * 2. get count of (Fi|RV)
     * 3. get count of RV
     * 3. compute Laplace given k and dom RV
     *
     * @param RV
     * @param k
     * @param priori
     * @throws Exception
     */
    public void SetCPT(String RV, int k, HashMap<String, Double> priori) throws Exception {
        
        

        
        
        

    }
    
    private HashMap<String, Double> Laplace_Smoothing(String Feature_name, int k, HashMap<String, Double> priori) throws SQLException{
        
        String connectionURL = "jdbc:sqlserver://urluniversity.database"
                + ".windows.net:1433;database=IA_TRAIN_SET_DATABASE;"
                + "user=IA_USER@urluniversity;password=@Bcde54321;"
                    + "encrypt=true;trustServerCertificate=false;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionURL);
           
            
            
            //this gets the Fi count
            Statement st = con.createStatement();
            ResultSet result = st.executeQuery("select "
                    + "count( distinct " + Feature_name + ") from Imdb");
            
            //we compute n*k given n = total of RVi * k Priori column
            
        
        return null;
    }
    
    
}
