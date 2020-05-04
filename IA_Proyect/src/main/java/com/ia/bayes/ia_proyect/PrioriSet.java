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
    
    //TODO: Method which calculates argmax of CPT
    
    private void ComputePT(String prioriValue) throws SQLException{
        
       Laplace_Smooth LS = new Laplace_Smooth();
       
       Feature = LS.Calculate_Laplace(prioriValue, 3d, Feature);
       
    }
}
