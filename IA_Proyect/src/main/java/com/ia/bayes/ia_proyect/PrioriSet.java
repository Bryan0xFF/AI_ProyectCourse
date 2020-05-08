/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ia.bayes.ia_proyect;

import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author Bryan
 */
public class PrioriSet {
    
    private HashMap<String, Double> Feature = new HashMap<>();
    private String priori_name = "";
    
    //TODO: Method which calculates argmax of CPT
    
    public void ComputePT(String prioriValue) throws SQLException{
        
        setPriori_name(prioriValue);
        
       Laplace_Smooth LS = new Laplace_Smooth();
       
        setFeature(LS.Calculate_Laplace_Priori(prioriValue, 3d));
       
    }

    /**
     * @return the priori_name
     */
    public String getPriori_name() {
        return priori_name;
    }

    /**
     * @param priori_name the priori_name to set
     */
    public void setPriori_name(String priori_name) {
        this.priori_name = priori_name;
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
}
