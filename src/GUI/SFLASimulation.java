/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author PC-GAMER
 */
public class SFLASimulation {
    private int num;
    private double makespan, cout;
    
    public SFLASimulation(int num, QoS qos, int m, int c){
        this.num = num;
        start(qos, m, c);
    }
    
    public void start(QoS qos, int m, int c){
        double tab1[] = new double[num];
        double tab2[] = new double[num];
        double sum1=0, sum2=0;
        for (int i = 0; i < num; i++) {
            Cloud d = new Cloud(qos, m, c);
            tab1[i]=d.getMs();
            tab2[i]=d.getCs();
            sum1+=tab1[i];
            sum2+=tab2[i];
        }
        makespan = sum1/num;
        cout = sum2/num;
    }

    public double getMakespan() {
        return makespan;
    }

    public void setMakespan(double makespan) {
        this.makespan = makespan;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }
    
}
