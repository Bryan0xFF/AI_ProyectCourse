/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ia.bayes.ia_proyect;

import java.util.HashMap;

/**
 *
 * @author Bryan
 */
public class PrioriSet {
    
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
    
    private void ComputePT(String prioriValue){
        
       Laplace_Smooth LS = new Laplace_Smooth();
       
       Feature LS.Calculate_Laplace(prioriValue, 3, Feature);
       
    }
}
