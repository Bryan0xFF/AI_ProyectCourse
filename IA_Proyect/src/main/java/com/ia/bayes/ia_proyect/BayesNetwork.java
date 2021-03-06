/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ia.bayes.ia_proyect;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bryan
 */
public class BayesNetwork {
    
    PrioriSet priori;
    List<FeatureVal> Features = new ArrayList<>();
    
    public BayesNetwork(PrioriSet priori){
    
        this.priori = priori;
        
    }
    
    public Boolean setFeature(FeatureVal feature) throws Exception{
        
        if(feature != null){
            feature.SetCPT(3.0, priori.getPriori_name());
            Features.add(feature);
        }else{
            return false;
        }
        
        return true;
    }
    
     public Boolean setFeatureSplit(FeatureVal feature) throws Exception{
        
        if(feature != null){
            feature.SetSplittedCPT(feature.getFeature_name(), 3.0, priori.getPriori_name());
            Features.add(feature);
        }else{
            return false;
        }
        
        return true;
    }
    
}