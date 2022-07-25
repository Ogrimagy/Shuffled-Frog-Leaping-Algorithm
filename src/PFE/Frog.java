/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PFE;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author PC-GAMER
 */
public class Frog {

    /**
     * The frog id.
     */
    private int id_frog;

    /**
     * HashMap[key = Cloudlet_id, Value = VM_id]
     */
    private HashMap<Integer, Integer> map;

    /**
     * Table to generate random Vm_ids for each frog.
     */
    private int tab[];

    /**
     * The Makespan value.
     */
    private double makespan = 0;

    /**
     * The cost value.
     */
    private double cost = 0;

    /**
     * The energy value.
     */
    private double energy = 0;

    /**
     * The objective value.
     */
    private double total = 0;
    
    private double m,c,ms,cs;

    public Frog(int id_frog) {
        this.id_frog = id_frog;
        tab = generateTab();
        map = fillMap();
        makespan = calculateMakespan();
        cost = calculateCost();
    }

    public Frog(int id_frog, int[] tab) {
        this.id_frog = id_frog;
        this.tab = tab;
        map = fillMap();
        makespan = calculateMakespan();
        cost = calculateCost();
    }

    private int[] generateTab() {
        int[] Tab = new int[Cloud.getCloudlet().length];
        for (int i = 0; i < Tab.length; i++) {
            int r = (int) (0 + (Math.random() * (Cloud.getVm().length - 1)));
            Tab[i] = r;
        }
        return Tab;
    }

    public HashMap<Integer, Integer> fillMap() {
        HashMap<Integer, Integer> mop = new HashMap();
        for (int i = 0; i < Cloud.getCloudlet().length; i++) {
            mop.put(Cloud.getCloudlet()[i].getCloudletId(), Cloud.getVm()[tab[i]].getId());
        }
        return mop;
    }

    public double calculateMakespan() {
        double max = 0;

        for (int i = 0; i < Cloud.getVm().length; i++) {
            if (map.containsValue(Cloud.getVm()[i].getId())) {
                makespan = 0;
                for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                    if (entry.getValue().equals(Cloud.getVm()[i].getId())) {
                        makespan += Cloud.getCloudlet()[entry.getKey()].getCloudletLength();// Cloud.getCloudlet()[entry.getKey()] = Cloud.getCloudlet().getCloudletId()
                    }
                }
                makespan /= Cloud.getVm()[i].getMips();
                if (max < makespan) {
                    max = makespan;
                }
            }
        }
        return max;
    }

    public double calculateCost() {
        for (int i = 0; i < Cloud.getCloudlet().length; i++) {
            cost += Cloud.getCloudlet()[i].getCloudletLength() / Cloud.getVm()[tab[i]].getMips() * Cloud.calculatePrice(Cloud.getVm()[tab[i]]);
        }
        return cost;
    }

    public double calculateEnergy() {
        double cons = 1;
        for (int i = 0; i < Cloud.getCloudlet().length; i++) {
            double vi = 220;
            double fi = 1;
            double wri = Cloud.getCloudlet()[i].getCloudletLength() / Cloud.getVm()[tab[i]].getMips();
            energy += vi * vi * fi * wri;
        }
        energy *= cons;
        return energy;
    }

    public int getId_frog() {
        return id_frog;
    }

    public void setId_frog(int id_frog) {
        this.id_frog = id_frog;
    }

    public HashMap<Integer, Integer> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, Integer> map) {
        this.map = map;
    }

    public int[] getTab() {
        return tab;
    }

    public void setTab(int[] tab) {
        this.tab = tab;
    }

    public double getMakespan() {
        return makespan;
    }

    public void setMakespan(double makespan) {
        this.makespan = makespan;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }

    public double getC() {
        return c;
    }

    public double getMs() {
        return ms;
    }

    public void setMs(double ms) {
        this.ms = ms;
    }

    public double getCs() {
        return cs;
    }

    public void setCs(double cs) {
        this.cs = cs;
    }

    public void setC(double c) {
        this.c = c;
    }

    
}
