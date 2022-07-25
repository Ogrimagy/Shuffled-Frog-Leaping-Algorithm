package GUI;

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

public class RoundRobine {

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
    private static Cloudlet[] cloudlet;

    /**
     * The vmtable.
     */
    private static Vm[] vm;
    
    /**
     * HashMap[key = Cloudlet_id, Value = VM_id]
     */
    private HashMap<Integer, Integer> map = new HashMap<>();
    
    /**
     * The Makespan value.
     */
    private double makespan;
    
    /**
     * The QoS object.
     */
    private static QoS qos;

    public RoundRobine(QoS qos) {
        
        this.qos = qos;
        
        cloudlet = new Cloudlet[qos.getCloudlets()];
        vm = new Vm[qos.getVMs()];

        Log.printLine("Starting CloudSim...");

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
            for (int i = 1; i <= qos.getDC(); i++) {
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
                int mips = (int) (qos.getMIPS_min() + (Math.random() * qos.getMIPS_max()));
                long size = qos.getStorage_VM(); //image size (MB)
                int ram = (int) (qos.getRAM_min() + (Math.random() * qos.getRAM_max())); //vm memory (MB)
                long bw = (long) (qos.getBW_min() + (Math.random() * qos.getBW_max()));

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
                long length = (long) (qos.getLength_min() + (Math.random() * qos.getLength_max()));
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
            for (int i = 0, j = 0; j < cloudlet.length; j++) {
                broker.bindCloudletToVm(cloudlet[j].getCloudletId(), vm[i].getId());
                map.put(cloudlet[j].getCloudletId(), vm[i].getId());
                i++;
                if (i == (vm.length - 1)) {
                    i = 0;
                }
            }

            // Sixth step: Starts the simulation
            CloudSim.startSimulation();

            // Final step: Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList();

            CloudSim.stopSimulation();

            printCloudletList(newList);
            
            makespan = calculateMakespan();
            System.out.println("Makespan :"+ makespan);

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

        int mips = qos.getMIPS();

        // 3. Create PEs and add these into a list.
        for (int i = 0; i < qos.getPES(); i++) {
            peList.add(new Pe(i, new PeProvisionerSimple(4 * mips))); // need to store Pe id and MIPS Rating
        }

        //4. Create Host with its id and list of PEs and add them to the list of machines
        int ram = qos.getRAM(); //host memory (MB)
        long storage = qos.getStorage(); //host storage
        int bw = qos.getBW();

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
    
    public double calculateMakespan() {
        double max = 0;
        double makespan;
        for (int i = 0; i < vm.length; i++) {
            if (map.containsValue(vm[i].getId())) {
                makespan = 0;
                for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                    if (entry.getValue().equals(vm[i].getId())) {
                        makespan += cloudlet[entry.getKey()].getCloudletLength();// Cloud.getCloudlet()[entry.getKey()] = Cloud.getCloudlet().getCloudletId()
                    }
                }
                makespan /= RoundRobine.getVm()[i].getMips();
                if (max < makespan) {
                    max = makespan;
                }
            }
        }
        return max;
    }

    public double getMakespan() {
        return makespan;
    }

    public void setMakespan(double makespan) {
        this.makespan = makespan;
    }


    public static void main(String[] args) {
        new RoundRobine(new QoS());
    }

    public static Cloudlet[] getCloudlet() {
        return cloudlet;
    }

    public static void setCloudlet(Cloudlet[] cloudlet) {
        RoundRobine.cloudlet = cloudlet;
    }

    public static Vm[] getVm() {
        return vm;
    }

    public static void setVm(Vm[] vm) {
        RoundRobine.vm = vm;
    }

}
