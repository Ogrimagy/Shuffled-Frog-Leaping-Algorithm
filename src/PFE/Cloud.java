package PFE;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Ogrimagy
 */
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class Cloud {

    /**
     * The cloudlet list.
     */
    private static List<Cloudlet> cloudletList;

    /**
     * The vmlist.
     */
    private static List<Vm> vmlist;

    /**
     * The cloudlet table.
     */
    private static Cloudlet[] cloudlet = new Cloudlet[1000];

    /**
     * The vmtable.
     */
    private static Vm[] vm = new Vm[100];

    /**
     * F: Population, M, Bumber of memplexes, N: Number of iterations, S: Stop
     * creterian
     */
    private static int F, M, N, S;

    /**
     * HashMap [key = Frog_id, value = Frog]
     */
    private static HashMap<Integer, Frog> frogs = new HashMap();
    
    /**
     * Return the values after 10 simulations
     */
    private static double ms,cs;

    /**
     * Creates main() to run this example
     */
    public Cloud() {

        Log.printLine("Starting CloudSim...");

        F = 100;
        M = F/10;
        N = F/10;
        S = F/2;

        try {
            // First step: Initialize the CloudSim package. It should be called
            // before creating any entities.
            int num_user = 1;   // number of cloud users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            // Initialize the GridSim library
            CloudSim.init(num_user, calendar, trace_flag);

            // Second step: Create Datacenters
            //Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
            for (int i = 1; i <= 4; i++) {
                @SuppressWarnings("unused")
                Datacenter datacenter0 = createDatacenter("Datacenter_" + i);
            }

            //Third step: Create Broker
            DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();

            //Fourth step: Create 80 virtual machine
            vmlist = new ArrayList<Vm>();

            int pesNumber = 1; //number of cpus
            for (int vmid = 0; vmid < vm.length; vmid++) {

                //VM description
                int mips = (int) (1000 + (Math.random() * 5000));
                long size = 10240; //image size (MB)
                int ram = (int) (1024 + (Math.random() * 5120)); //vm memory (MB)
                long bw = (long) (100 + (Math.random() * 500));

                String vmm = "Xen"; //VMM name

                vm[vmid] = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
                vmlist.add(vm[vmid]);

            }

            //submit vm list to the broker
            broker.submitVmList(vmlist);

            //Fifth step: Create two Cloudlets
            cloudletList = new ArrayList<Cloudlet>();

            cloudletList = new ArrayList<Cloudlet>();

            for (int id = 0; id < cloudlet.length; id++) {

                //Cloudlet properties
                long length = (long) (3000 + (Math.random() * 10000));
                long fileSize = 300;
                long outputSize = 300;
                UtilizationModel utilizationModel = new UtilizationModelFull();

                cloudlet[id] = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
                cloudlet[id].setUserId(brokerId);

                //add the cloudlets to the list
                cloudletList.add(cloudlet[id]);

            }

            //submit cloudlet list to the broker
            broker.submitCloudletList(cloudletList);

            //bind the cloudlets to the vms. This way, the broker
            // will submit the bound cloudlets only to the specific VM
            for (int i = 0; i < F; i++) {
                Frog frog = new Frog(i);
                frogs.put(i, frog);
            }

            SFLA sfla = new SFLA(frogs);
            Frog opt_frog = sfla.start();

            for (Map.Entry<Integer, Integer> set : opt_frog.getMap().entrySet()) {
                broker.bindCloudletToVm(set.getKey(), opt_frog.getMap().get(set.getKey()));
            }

            // Sixth step: Starts the simulation
            CloudSim.startSimulation();

            // Final step: Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList();

            CloudSim.stopSimulation();

            printCloudletList(newList);
            
            ms = opt_frog.getM();
            cs = opt_frog.getC();
            
            System.out.println("Avec Minimization - Makespan: "+opt_frog.getMs()+", cout: "+opt_frog.getCs());
            System.out.println("Sans Minimization - Makespan: "+opt_frog.getM()+", cout: "+opt_frog.getC());
            System.out.println("Objective Value: " + opt_frog.getTotal());

            Log.printLine("CloudSim finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
    }

    private static Datacenter createDatacenter(String name) {

        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store
        //    our machine
        List<Host> hostList = new ArrayList<Host>(); //8

        // 2. A Machine contains one or more PEs or CPUs/Cores.
        // In this example, it will have only one core.
        List<Pe> peList = new ArrayList<Pe>(); //4

        int mips = 6000;

        // 3. Create PEs and add these into a list.
        for (int i = 0; i < 4; i++) {
            peList.add(new Pe(i, new PeProvisionerSimple(4 * mips))); // need to store Pe id and MIPS Rating
        }

        //4. Create Host with its id and list of PEs and add them to the list of machines
        int ram = 20480; //host memory (MB)
        long storage = 1024000; //host storage
        int bw = 10240;

        //in this example, the VMAllocatonPolicy in use is SpaceShared. It means that only one VM
        //is allowed to run on each Pe. As each Host has only one Pe, only one VM can run on each Host.
        for (int hostId = 0; hostId < 8; hostId++) {
            hostList.add(
                    new Host(
                            hostId,
                            new RamProvisionerSimple(ram),
                            new BwProvisionerSimple(bw),
                            storage,
                            peList,
                            new VmSchedulerSpaceShared(peList)
                    )
            ); // This is our first machine
        }

        // 5. Create a DatacenterCharacteristics object that stores the
        //    properties of a data center: architecture, OS, list of
        //    Machines, allocation policy: time- or space-shared, time zone
        //    and its price (G$/Pe time unit).
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;		// the cost of using memory in this resource
        double costPerStorage = 0.001;	// the cost of using storage in this resource
        double costPerBw = 0.0;			// the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

        // 6. Finally, we need to create a PowerDatacenter object.
        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

    //We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
    //to the specific rules of the simulated scenario
    private static DatacenterBroker createBroker() {

        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

    /**
     * Prints the Cloudlet objects
     *
     * @param list list of Cloudlets
     */
    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
                + "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");

                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId()
                        + indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())
                        + indent + indent + dft.format(cloudlet.getFinishTime()));
            }
        }

    }

    public static double calculatePrice(Vm vm) {
        if (1000 <= vm.getMips() && vm.getMips() < 2000) {
            return 1;
        } else if (2000 <= vm.getMips() && vm.getMips() < 3000) {
            return 2;
        } else if (3000 <= vm.getMips() && vm.getMips() < 4000) {
            return 3;
        } else if (4000 <= vm.getMips() && vm.getMips() < 5000) {
            return 4;
        } else 
            return 5;
    }
    
    public static void main(String[] args) {
        new Cloud();
    }

    public static Cloudlet[] getCloudlet() {
        return cloudlet;
    }

    public static void setCloudlet(Cloudlet[] cloudlet) {
        Cloud.cloudlet = cloudlet;
    }

    public static Vm[] getVm() {
        return vm;
    }

    public static void setVm(Vm[] vm) {
        Cloud.vm = vm;
    }

    public static int getF() {
        return F;
    }

    public static void setF(int F) {
        Cloud.F = F;
    }

    public static int getM() {
        return M;
    }

    public static void setM(int M) {
        Cloud.M = M;
    }

    public static int getN() {
        return N;
    }

    public static void setN(int N) {
        Cloud.N = N;
    }

    public static int getS() {
        return S;
    }

    public static void setS(int S) {
        Cloud.S = S;
    }

    public static double getMs() {
        return ms;
    }

    public static void setMs(double ms) {
        Cloud.ms = ms;
    }

    public static double getCs() {
        return cs;
    }

    public static void setCs(double cs) {
        Cloud.cs = cs;
    }

}
