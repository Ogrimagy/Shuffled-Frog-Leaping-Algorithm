package RoundRobin;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.Arrays;

/**
 *
 * @author PC-GAMER
 */
public class Main {
    public static void main(String[] args) {
        double tab1[] = new double[10];
        double sum1=0;
        for (int i = 0; i < 10; i++) {
            RoundRobine rr = new RoundRobine();
            tab1[i]=rr.getMakespan();
            sum1+=tab1[i];
        }
        System.out.println("tab1 = "+Arrays.toString(tab1));
        System.out.println("Makespan : "+sum1/10);
        
    }
}
