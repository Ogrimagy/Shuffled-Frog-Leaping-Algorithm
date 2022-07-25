/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PFE;

import java.util.Arrays;

/**
 *
 * @author PC-GAMER
 */
public class Main {
    public static void main(String[] args) {
        double tab1[] = new double[10];
        double tab2[] = new double[10];
        double sum1=0, sum2=0;
        for (int i = 0; i < 10; i++) {
            Cloud d = new Cloud();
            tab1[i]=d.getMs();
            tab2[i]=d.getCs();
            sum1+=tab1[i];
            sum2+=tab2[i];
        }
        System.out.println("tab1 = "+Arrays.toString(tab1));
        System.out.println("tab2 = "+Arrays.toString(tab2));
        System.out.println("Makespan : "+sum1/10+", Cout : "+sum2/10 );
        
    }
}
