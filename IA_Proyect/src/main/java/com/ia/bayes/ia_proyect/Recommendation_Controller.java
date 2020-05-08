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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Bryan
 */
public class Recommendation_Controller {
    
    public static int count = 1;

    //Here we call the laplace class and/or recalculate probabilites 
    public List<String> Return_Top_5(String feature_values, BayesNetwork BN, Double rating) throws SQLException {

        String[] data = feature_values.split("\\,");
        String[] genre_data = data[5].split("\\|");
        String[] p_k_data = data[6].split("\\|");
        

        //1. we add a probability given a certain weight
        HashMap<String, Double> recalc = new HashMap<>();
        HashMap<String, Double> recalc_rated_movies = new HashMap<>();
        Double prob_1 = 0.0;
        Double prob_2 = 0.0;
        Double prob = 0d;
        Double rating_val = 0.0;

        if (rating >= 5.0) {

            for (int i = 0; i < 4; i++) {
                recalc = BN.Features.get(i).getFeature();
                recalc.compute((data[i + 1] + "|1").trim(), (key, val) -> val + (-1)*Math.log(((1 - Math.exp(val)) / 2)));
                recalc.compute((data[i + 1] + "|0").trim(), (key, val) -> val + (-1)*Math.log(((1 - Math.exp(val)) / 2)));
                
                for (String key1 : recalc.keySet()){

                    if(!key1.substring(0, key1.length() - 2).equals(data[i + 1].trim())){
                        recalc.compute(key1, (key, val) -> val *= 3);
                    }
                }
            }

            for (int j = 0; j < genre_data.length; j++) {
                recalc = BN.Features.get(4).getFeature();
                recalc.compute((genre_data[j] + "|1").trim(), (key, val) -> val + (-1)*Math.log(((1 - Math.exp(val)) / 2)));
                recalc.compute((genre_data[j] + "|0").trim(), (key, val) -> val + (-1)*Math.log(((1 - Math.exp(val)) / 2)));
                
                for (String key1 : recalc.keySet()){
                    if(!key1.substring(0, key1.length() - 2).equals(genre_data[j].trim())){
                        recalc.compute(key1, (key, val) -> val *= 3);
                    }
                }
            }
            
            //TODO: eliminar pais
            for (int i = 6; i < BN.Features.size(); i++) {
                recalc = BN.Features.get(i).getFeature();
                recalc.compute((data[i + 1] + "|1").trim(), (key, val) -> val + (-1)*Math.log(((1 - Math.exp(val)) / 2)));
                recalc.compute((data[i + 1] + "|0").trim(), (key, val) -> val + (-1)*Math.log(((1 - Math.exp(val)) / 2)));
                
                for (String key1 : recalc.keySet()){
                    if(!key1.substring(0, key1.length() - 2).equals(data[i + 1].trim())){
                        recalc.compute(key1, (key, val) -> val *= 3);
                    }
                }
            }

        } else {

            if (!"NULL".equals(genre_data[0]) || !"".equals(genre_data[0])) {
                for (int j = 0; j < genre_data.length; j++) {
                    recalc = BN.Features.get(5).getFeature();
                    recalc.compute((genre_data[j] + "|1").trim(), (key, val) -> val + (-1)*Math.log(((1 - Math.exp(val)) / 2)));
                    recalc.compute((genre_data[j] + "|0").trim(), (key, val) -> val + (-1)*Math.log((1 - Math.exp(val)) / 2));
                    
                    for (String key1 : recalc.keySet()){
                    if(!key1.substring(0, key1.length() - 2).equals(genre_data[j + 1].trim())){
                        recalc.compute(key1, (key, val) -> val *= 3);
                    }
                }
                    
                }
                //recalc.computeIfPresent(, remappingFunction)
            }
            //we only recalculate the genre

        }

        //2. we calculate the sum of each prob given its table p(y=0) + p(Fi|y) (twice for y=1)
        String connectionURL = "jdbc:sqlserver://urluniversity.database"
                + ".windows.net:1433;database=IA_TRAIN_SET_DATABASE;"
                + "user=IA_USER@urluniversity;password=@Bcde54321;"
                + "encrypt=true;trustServerCertificate=false;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        Connection con = DriverManager.getConnection(connectionURL);

        Statement st = con.createStatement();
        ResultSet result = st.executeQuery("SELECT * FROM Imdb");

        while (result.next()) {

            StringBuilder sb = new StringBuilder();
            /*
             director = result.getString(2);
            actor_1 = result.getString(11);
            actor_2 = result.getString(7);
            actor_3 = result.getString(15);
            movie_title = result.getString(12);
            content_rating = result.getString(22);
            country = result.getString(21);
            score = result.getString(26);
            plot_keywords = result.getString(17);
             */
            
            sb.append(result.getString(2)).append(",");
            sb.append(result.getString(11)).append(",");
            sb.append(result.getString(7)).append(",");
            sb.append(result.getString(15)).append(",");
            sb.append(result.getString(10)).append(",");
            //sb.append(result.getString(17)).append(",");
            sb.append(result.getString(21)).append(",");
            sb.append(result.getString(22));

            String[] val = sb.toString().split("\\,");
            
           
            
            if(result.getString(12).trim().equals(data[0].trim())){
                continue;
            }

            prob_1 = BN.priori.getFeature().get("1");
            prob_2 = BN.priori.getFeature().get("0");

            //first 3 probs
            for (int i = 0; i < 3; i++) {
                if (!"NULL".equals(val[i]) && !"".equals(val[i])) {
                    prob_1 += BN.Features.get(i).getFeature().get((val[i] + "|1").trim());
                    prob_2 += BN.Features.get(i).getFeature().get((val[i] + "|1").trim());

                } else {
                       for (int j = 0; j < 10; j++) {
                        prob_1 += Math.log(0.000000000000000000000000001);
                        prob_2 += Math.log(0.000000000000000000000000001);
                    }
                    break;
                }
            }

            String[] genre = val[4].split("\\|");

            if (!"NULL".equals(genre[0]) && !"".equals(genre[0])) {

                for (String genre1 : genre) {
                    prob_1 += BN.Features.get(4).getFeature().get((genre1 + "|1").trim());
                    prob_2 += BN.Features.get(4).getFeature().get((genre1 + "|0").trim());
                }

            }else{
                for (int j = 0; j < 10; j++) {
                        prob_1 += Math.log(0.000000000000000000000000001);
                        prob_2 += Math.log(0.000000000000000000000000001);
                    }
            }
            //4 and 5 needs to be splitted


            for (int i = 5; i < BN.Features.size(); i++) {
                if (!"NULL".equals(val[i]) || !"".equals(val[i])) {
                    prob_1 += BN.Features.get(i).getFeature().get((val[i] + "|1").trim());
                    prob_2 += BN.Features.get(i).getFeature().get((val[i] + "|0").trim());
                } else {
                    
                    for (int j = 0; j < 10; j++) {
                        prob_1 += Math.log(0.000000000000000000000000001);
                        prob_2 += Math.log(0.000000000000000000000000001);
                    }
                    
                    break;
                }

            }
            
            

            //prob = Math.exp(prob_1) /(Math.exp(prob_1) + Math.exp(prob_2));
            recalc_rated_movies.put(result.getString(12) + "|1", prob_1);

            //prob = Math.exp(prob_2)/(Math.exp(prob_1) + Math.exp(prob_2));
            recalc_rated_movies.put(result.getString(12) + "|0", prob_2);

        }

        //3. we sort to find out which movie are at the top given the new weights
        recalc_rated_movies = sortValues(recalc_rated_movies);

        List<String> return_values = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            return_values.add(recalc_rated_movies.keySet().toArray()[i].toString() + "|"
                    + recalc_rated_movies.values().toArray()[i].toString());
        }
        //4. return the values

        return return_values;
    }

    private static HashMap sortValues(HashMap map) {

        List list = new LinkedList(map.entrySet());
        //Custom Comparator  
        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        //copying the sorted list in HashMap to preserve the iteration order  
        HashMap sortedHashMap = new LinkedHashMap<>();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        count = count + 1;
        return sortedHashMap;
    }

}
