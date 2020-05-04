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
    
    public Boolean setFeature(FeatureVal feature){
        
        if(feature != null){
            Features.add(feature);
        }else{
            return false;
        }
        
        return true;
    }
    
}
