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
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException, Exception{
        
        //Creating the bayes network
        PrioriSet priori = new PrioriSet();
        priori.ComputePT("imdb_score");
        
        BayesNetwork BN = new BayesNetwork(priori);
        
        //add the Features to the Bayes Network
        FeatureVal director = new FeatureVal("director_name");
        BN.setFeature(director);
        
        FeatureVal genre = new FeatureVal("genre");
        BN.setFeatureSplit(genre);
        
        FeatureVal plot_keywords = new FeatureVal("plot_keywords");
        BN.setFeatureSplit(plot_keywords);
        
        FeatureVal actor_1 = new FeatureVal("actor_1_name");
        BN.setFeature(actor_1);
        
        FeatureVal actor_2 = new FeatureVal("actor_2_name");
        BN.setFeature(actor_2);
        
        FeatureVal actor_3 = new FeatureVal("actor_3_name");
        BN.setFeature(actor_3);
        
        FeatureVal country = new FeatureVal("country");
        BN.setFeature(country);
        
        FeatureVal content_rating = new FeatureVal("content_rating");
        BN.setFeature(content_rating);
        
        
    }
}