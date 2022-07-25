/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Mohammedi
 */
public class QoS {

    private int F, M, N, S; // SecondFrame
    private int Host, RAM, BW, Storage, PES, MIPS, DC; // ThirdFrame
    private int VMs, RAM_min, RAM_max, BW_min, BW_max, Storage_VM, MIPS_min, MIPS_max; // ForthFrame
    private int Cloudlets, Length_min, Length_max; // FifthFrame
    private int sim; //SixthFrame

    public QoS() {
        F = 100;
        M = F / 10;
        N = F / 10;
        S = F / 2;
        Host = 8;
        RAM = 20480;
        BW = 10240;
        Storage = 1024000;
        PES = 4;
        MIPS = 6000;
        DC = 4;
        VMs = 80;
        RAM_min = 1024;
        RAM_max = 5120;
        BW_min = 100;
        BW_max = 500;
        Storage_VM = 10240;
        MIPS_min = 1000;
        MIPS_max = 5000;
        Cloudlets = 500;
        Length_min = 3000;
        Length_max = 10000;
        sim = 10;
    }

    public int getF() {
        return F;
    }

    public void setF(int F) {
        this.F = F;
    }

    public int getM() {
        return M;
    }

    public void setM(int M) {
        this.M = M;
    }

    public int getN() {
        return N;
    }

    public void setN(int N) {
        this.N = N;
    }

    public int getS() {
        return S;
    }

    public void setS(int S) {
        this.S = S;
    }

    public int getHost() {
        return Host;
    }

    public void setHost(int Host) {
        this.Host = Host;
    }

    public int getRAM() {
        return RAM;
    }

    public void setRAM(int RAM) {
        this.RAM = RAM;
    }

    public int getBW() {
        return BW;
    }

    public void setBW(int BW) {
        this.BW = BW;
    }

    public int getStorage() {
        return Storage;
    }

    public void setStorage(int Storage) {
        this.Storage = Storage;
    }

    public int getPES() {
        return PES;
    }

    public void setPES(int PES) {
        this.PES = PES;
    }

    public int getMIPS() {
        return MIPS;
    }

    public void setMIPS(int MIPS) {
        this.MIPS = MIPS;
    }

    public int getDC() {
        return DC;
    }

    public void setDC(int DC) {
        this.DC = DC;
    }

    public int getVMs() {
        return VMs;
    }

    public void setVMs(int VMs) {
        this.VMs = VMs;
    }

    public int getRAM_min() {
        return RAM_min;
    }

    public void setRAM_min(int RAM_min) {
        this.RAM_min = RAM_min;
    }

    public int getRAM_max() {
        return RAM_max;
    }

    public void setRAM_max(int RAM_max) {
        this.RAM_max = RAM_max;
    }

    public int getBW_min() {
        return BW_min;
    }

    public void setBW_min(int BW_min) {
        this.BW_min = BW_min;
    }

    public int getBW_max() {
        return BW_max;
    }

    public void setBW_max(int BW_max) {
        this.BW_max = BW_max;
    }

    public int getStorage_VM() {
        return Storage_VM;
    }

    public void setStorage_VM(int Storage_VM) {
        this.Storage_VM = Storage_VM;
    }

    public int getMIPS_min() {
        return MIPS_min;
    }

    public void setMIPS_min(int MIPS_min) {
        this.MIPS_min = MIPS_min;
    }

    public int getMIPS_max() {
        return MIPS_max;
    }

    public void setMIPS_max(int MIPS_max) {
        this.MIPS_max = MIPS_max;
    }

    public int getCloudlets() {
        return Cloudlets;
    }

    public void setCloudlets(int Cloudlets) {
        this.Cloudlets = Cloudlets;
    }

    public int getLength_min() {
        return Length_min;
    }

    public void setLength_min(int Length_min) {
        this.Length_min = Length_min;
    }

    public int getLength_max() {
        return Length_max;
    }

    public void setLength_max(int Length_max) {
        this.Length_max = Length_max;
    }
    
    public int getSim() {
        return sim;
    }

    public void setSim(int sim) {
        this.sim = sim;
    }
}
