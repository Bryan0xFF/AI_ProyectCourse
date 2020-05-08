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
    private String Feature_name = "";
    
    //TODO: Method which calculates argmax of CPT
    
    public FeatureVal(String Feature_name){
        this.Feature_name = Feature_name;
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
     * @param priori_name
     * @throws Exception
     */
    public void SetCPT(Double k, String priori_name) throws Exception {
        
        Laplace_Smooth LS = new Laplace_Smooth();
        setFeature(LS.Calculate_Laplace(getFeature_name(), k, priori_name));

    } 
    
    private void ComputeSplitCPT(String Feature_name, Double k, String priori_name) throws ClassNotFoundException, SQLException{
        
        Laplace_Smooth LS = new Laplace_Smooth();
        
        setFeature(LS.Laplace_Smoothing_SplitData(Feature_name, k, priori_name));
    }
    
    public void SetSplittedCPT(String Feature_name, Double k, String priori_name) throws ClassNotFoundException, SQLException{
        
        ComputeSplitCPT(Feature_name, k, priori_name);
    }

    /**
     * @return the Feature
     */
    public HashMap<String, Double> getFeature() {
        return Feature;
    }

    /**
     * @param Feature the Feature to set
     */
    public void setFeature(HashMap<String, Double> Feature) {
        this.Feature = Feature;
    }

    /**
     * @return the Feature_name
     */
    public String getFeature_name() {
        return Feature_name;
    }

    /**
     * @param Feature_name the Feature_name to set
     */
    public void setFeature_name(String Feature_name) {
        this.Feature_name = Feature_name;
    }

}