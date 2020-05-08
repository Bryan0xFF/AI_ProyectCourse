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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Bryan
 */
public class App {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, Exception {

        //Creating the bayes network
        PrioriSet priori = new PrioriSet();
        priori.ComputePT("imdb_score");

        BayesNetwork BN = new BayesNetwork(priori);

        //add the Features to the Bayes Network
        FeatureVal director = new FeatureVal("director_name");
        BN.setFeature(director);

        FeatureVal actor_1 = new FeatureVal("actor_1_name");
        BN.setFeature(actor_1);

        FeatureVal actor_2 = new FeatureVal("actor_2_name");
        BN.setFeature(actor_2);

        FeatureVal actor_3 = new FeatureVal("actor_3_name");
        BN.setFeature(actor_3);

        FeatureVal genre = new FeatureVal("genre");
        BN.setFeatureSplit(genre);


        FeatureVal country = new FeatureVal("country");
        BN.setFeature(country);

        FeatureVal content_rating = new FeatureVal("content_rating");
        BN.setFeature(content_rating);

        //instantiating variables for UI
        Boolean out = false;
        Scanner s = new Scanner(System.in);
        UI_Controller UI = new UI_Controller();
        List<String> movie_list = new ArrayList<>();

        while (!out) {

            System.out.println("Select one:");
            System.out.println("1. Watch a movie");
            System.out.println("2. exit");
            String data = s.nextLine();

            switch (data) {
                case "1":
                    System.out.println("Write down the movie you want to watch");
                    data = s.nextLine();
                    movie_list = UI.returnGatheredData(data);
                    Boolean selection = false;
                    
                    if (movie_list.isEmpty()) {
                        System.out.println("Movie doesn't exists in database!");
                        break;
                    }

                    while (!selection) {

                        int count = 1;
                        System.out.println("Select one option of movie");

                        for (int i = 0; i < movie_list.size(); i++) {
                            String[] split = movie_list.get(i).split("\\,");
                            
                            if("".equals(split[0])){
                                selection = true;
                                break;
                            }
                            
                            System.out.println(count + ". " + split[0]);
                            count++;
                        }
                        data = s.nextLine();

                        try {

                            String sel = movie_list.get(Integer.parseInt(data) - 1);
                            System.out.println("Rate movie [1-10] (Can be 5.9, 5.0)");
                            String rate = s.nextLine();

                            String[] split = movie_list.get(Integer.parseInt(data) - 1).split("\\,");

                            System.out.println("Because you saw " + split[0]);
                            List<String> rated = UI.RateMovie(Double.parseDouble(rate), sel, BN);

                            count = 1;
                            for (int i = 0; i < rated.size(); i++) {
                                System.out.println(count + ". " + rated.get(i));
                                count++;
                            }
                            
                            System.out.println("press any button to return");
                            s.nextLine();
                            selection = true;
                            

                        } catch (Exception e) {

                            selection = true;
                            System.out.println("Please select an integer value.");
                        }
                    }

                    break;

                case "2":

                    break;

                case "3":
                    out = true;
                    break;

                default:
                    break;
            }
        }

    }
}
