/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author PC-GAMER
 */
public class RRSimulation {
    private int num;
    private double makespan;
    
    public RRSimulation(int num, QoS qos){
        this.num = num;
        start(qos);
    }
    
    public void start(QoS qos){
        double tab[] = new double[num];
        double sum=0;
        for (int i = 0; i < num; i++) {
            RoundRobine rr = new RoundRobine(qos);
            tab[i]=rr.getMakespan();
            sum+=tab[i];
        }
        makespan = sum/num;
    }

    public double getMakespan() {
        return makespan;
    }

    public void setMakespan(double makespan) {
        this.makespan = makespan;
    }
}
