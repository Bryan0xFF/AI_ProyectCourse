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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bryan
 */
public class UI_Controller {
    
    public List<String> RateMovie(Double value, String movie_name, BayesNetwork BN) throws SQLException{
         
        //given a certain BN we rate the movie and add the probabilities to each CPT
        //and recalculate each obtained value
        
        //1. we split the data and recalculate the probs of the CPT
        Recommendation_Controller RC = new Recommendation_Controller();
        List<String> data = RC.Return_Top_5(movie_name, BN, value);
        
        
        
        //search the movie in the database
        
        
        return data;
    }
    
    public List<String> returnGatheredData(String movie_name) throws SQLException{
        
        String director = "";
        String movie_title = "";
        String actor_1, actor_2, actor_3 = "";
        String  genre = "";
        String country = "";
        String content_rating = "";
        String score = "";
        List<String> gathered_data = new ArrayList<>();
        
        String connectionURL = "jdbc:sqlserver://urluniversity.database"
                + ".windows.net:1433;database=IA_TRAIN_SET_DATABASE;"
                + "user=IA_USER@urluniversity;password=@Bcde54321;"
                + "encrypt=true;trustServerCertificate=false;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        Connection con = DriverManager.getConnection(connectionURL);
        
        Statement st = con.createStatement();
        ResultSet result = st.executeQuery("SELECT * FROM Imdb WHERE movie_title like '%" + movie_name + "%'");
        
        //we get only the data of the feature set
        while(result.next()){
            
            director = result.getString(2);
            actor_1 = result.getString(11);
            actor_2 = result.getString(7);
            actor_3 = result.getString(15);
            genre = result.getString(10);
            movie_title = result.getString(12);
            content_rating = result.getString(22);
            country = result.getString(21);
            score = result.getString(26);
            
            StringBuilder sb = new StringBuilder();
            sb.append(movie_title).append(",");
            sb.append(director).append(",");
            sb.append(actor_1).append(",");
            sb.append(actor_2).append(",");
            sb.append(actor_3).append(",");
            sb.append(genre).append(",");
            sb.append(country).append(",");
            sb.append(content_rating).append(",");
            sb.append(score);
            

            
            gathered_data.add(sb.toString());
            
        }
        
        return gathered_data;
    }
}
