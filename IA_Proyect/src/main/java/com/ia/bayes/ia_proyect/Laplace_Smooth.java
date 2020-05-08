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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Bryan
 */
public class Laplace_Smooth {

    public HashMap<String, Double> Calculate_Laplace(String Feature_name, Double k, String priori_name) throws SQLException {

        HashMap<String, Double> CPT = new HashMap<>();
        Double reduced_universe = 0d;
        Double complement = 0d;
        Double feature_summation = 0d;

        String connectionURL = "jdbc:sqlserver://urluniversity.database"
                + ".windows.net:1433;database=IA_TRAIN_SET_DATABASE;"
                + "user=IA_USER@urluniversity;password=@Bcde54321;"
                + "encrypt=true;trustServerCertificate=false;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        Connection con = DriverManager.getConnection(connectionURL);

        //this gets the Fi count
//            Statement st = con.createStatement();
//            ResultSet result = st.executeQuery("select "
//                    + "count( distinct " + Feature_name + ") from Imdb");
        //we count how many times Fi is observed in the reduced universed given Xi
        //this gives us the CPT prob
        Statement st = con.createStatement();
        ResultSet result = st.executeQuery("SELECT count(distinct " + Feature_name + ") FROM Imdb");

        while (result.next()) {
            feature_summation = result.getDouble(1);
        }

        //get all the observed events Fi of a reduce universe
        result = st.executeQuery("SELECT COUNT(" + Feature_name + ") FROM Imdb WHERE " + priori_name + " >= 5");

        while (result.next()) {
            reduced_universe = result.getDouble(1);
        }

        //gets the complement of the marginal of Fi
        result = st.executeQuery("SELECT count(" + Feature_name + ") from Imdb  WHERE imdb_score < 5");

        while (result.next()) {
            complement = result.getDouble(1);
        }

        //now we compute the CPT 
        //1. prob of (Fi|X=x)
        //2. Prob of (Fi|X=~x)
        //O(2*n)
        String query = "SELECT DISTINCT " + Feature_name + ", count(" + Feature_name + ") from Imdb "
                + "WHERE " + priori_name + " >= 5 GROUP BY " + Feature_name;

        result = st.executeQuery(query);

        while (result.next()) {
            String Fi = result.getString(1);

            //we apply here the P-LAP(Fi|Y)
            Double PLAP = Math.log((result.getDouble(2) + k) / (reduced_universe + k * feature_summation));

            CPT.put((Fi + "|1").trim(), PLAP);
            CPT.put((Fi + "|0").trim(), Math.log(k / (reduced_universe + k * feature_summation)));
        }
        
        //get the reduced universe of the prob of 0
        result = st.executeQuery("SELECT COUNT(" + Feature_name + ") FROM Imdb WHERE " + priori_name + " < 5");

        while (result.next()) {
            reduced_universe = result.getDouble(1);
        }

        result = st.executeQuery("SELECT DISTINCT " + Feature_name + ", count(" + Feature_name + ") from Imdb "
                + "WHERE " + priori_name + " < 5 GROUP BY " + Feature_name);

        while (result.next()) {

            String negate_Fi = result.getString(1);

            Double prob = Math.log((result.getDouble(2) + k) / (reduced_universe + k * feature_summation));

            if (CPT.containsKey((negate_Fi + "|0").trim())) {
                CPT.compute((negate_Fi + "|0").trim(), (key, val) -> val = prob);
                
            }else{
                CPT.put(negate_Fi + "|0", prob);
            }
            
            if (!CPT.containsKey((negate_Fi + "|1").trim())) {
                CPT.put((negate_Fi + "|1").trim(), Math.log(k / (reduced_universe + k * feature_summation)));
            }
            
            
        }

        return CPT;
    }

    /**
     * compute first prior probabilities for our model, due time the compute is
     * not scalable, meaning by force it needs to be Boolean values, until
     * coming updates
     *
     * @param k
     * @return
     */
    public HashMap<String, Double> Calculate_Laplace_Priori(String priori_feature, Double k) {

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
            String query = "SELECT count(" + priori_feature + ") FROM Imdb WHERE " + priori_feature + " >= 5";
            ResultSet result = st.executeQuery(query);

            while (result.next()) {
                count1 = result.getDouble(1);
                System.out.println(count1);
            }

            st = con.createStatement();
            result = st.executeQuery("SELECT count(*) FROM Imdb");

            while (result.next()) {
                count2 = result.getDouble(1);
                count2 = count2 - count1;
                System.out.println(count2);
            }

            //compute laplacian
            //apply natural log to compute probabilities in the log-space
            //due to floating point underflow
            Double prob = (count1 + k) / ((count1 + count2) + k * 2);
            prioriTable.put("1", Math.log(prob));

            prob = (count2 + k) / ((count1 + count2) + k * 2);
            prioriTable.put("0", Math.log(prob));

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }

        return prioriTable;
    }

    /**
     * Compute CPT tables given a special form of string data use of parallel
     * data stream to accelerate data convergence
     *
     * @param Feature_value
     * @param k
     *
     * @return
     */
    public HashMap<String, Double> Laplace_Smoothing_SplitData(String Feature_value, Double k, String priori_name) throws SQLException {

        //retrieve data from server
        Double universe_1 = 0d;
        Double universe_0 = 0d;
        List<String> retrieved_data = new ArrayList<>();
        List<String> total_features = new CopyOnWriteArrayList<>();
        ConcurrentHashMap<String, Double> individual_data_count_1 = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Double> individual_data_count_0 = new ConcurrentHashMap<>();

        String connectionURL = "jdbc:sqlserver://urluniversity.database"
                + ".windows.net:1433;database=IA_TRAIN_SET_DATABASE;"
                + "user=IA_USER@urluniversity;password=@Bcde54321;"
                + "encrypt=true;trustServerCertificate=false;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        Connection con = DriverManager.getConnection(connectionURL);

        Statement st = con.createStatement();
        ResultSet result = st.executeQuery("SELECT COUNT(" + Feature_value + ") FROM Imdb WHERE " + priori_name + " >= 5");

        while (result.next()) {
            universe_1 = result.getDouble(1);
        }
        
        result = st.executeQuery("SELECT COUNT(" + Feature_value + ") FROM Imdb WHERE " + priori_name + " < 5");
        
        while(result.next()){
            universe_0 = result.getDouble(1);
        }

        st = con.createStatement();
        result = st.executeQuery("SELECT " + Feature_value + " from Imdb WHERE " + priori_name + " >= 5");

        while (result.next()) {
            retrieved_data.add(result.getString(1));
        }

        //count needs to be done local due to spliteration of data needs that needs to be analyzed
        retrieved_data.parallelStream().forEach(x -> {

            String[] split = x.split("\\|");

            for (String split1 : split) {
                individual_data_count_1.computeIfPresent((split1.trim() + "|1").trim(), (key, val) -> val += 1);
                individual_data_count_1.computeIfAbsent((split1.trim() + "|1").trim(), (key) -> 1.0);
                
                if(!individual_data_count_0.contains((split1 + "|0").trim())){
                    individual_data_count_0.put((split1 + "|0").trim(), 0.0);
                }

                if (total_features.contains(split1)) {

                } else {
                    total_features.add(split1);
                }
            }

        });

        result = st.executeQuery("SELECT " + Feature_value + " FROM Imdb WHERE " + priori_name + " < 5");
        retrieved_data.clear();

        while (result.next()) {
            retrieved_data.add(result.getString(1));
        }

        //count needs to be done local due to spliteration of data needs that needs to be analyzed
        retrieved_data.parallelStream().forEach(x -> {

            String[] split = x.split("\\|");

            for (String split1 : split) {
                individual_data_count_0.computeIfPresent((split1 + "|0").trim(), (key, val) -> val += 1);
                individual_data_count_0.computeIfAbsent((split1 + "|0").trim(), (key) -> 1.0);
                
                 if(!individual_data_count_1.contains((split1 + "|1").trim())){
                    individual_data_count_1.put((split1 + "|1").trim(), 0.0);
                }
                
                if (total_features.contains(split1)) {

                } else {
                    total_features.add(split1);
                }
            }

        });

        final Double lambda_universe_1 = universe_1;
        final Double lambda_universe_0 = universe_0;

        //we compute the laplacian of the obtained individual counts of each HashMap<>, given 1:
        individual_data_count_1.replaceAll((key, val) -> Math.log((val + k) / (lambda_universe_1 + k * total_features.size())));

        individual_data_count_0.replaceAll((key, val) -> Math.log((val + k) / (lambda_universe_0 + k * total_features.size())));

        HashMap<String, Double> return_val = new HashMap<>();

        return_val.putAll(individual_data_count_1);
        return_val.putAll(individual_data_count_0);

        //return all merged values
        return return_val;
    }

}
