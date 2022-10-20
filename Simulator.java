import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Simulator {

    private int duration;
    private int numInBuses;
    private int numOutBuses;
    private int minGroupSize;
    private int maxGroupSize;
    private int capacity;
    private double arrivalProb;

    private double groupsServed = 0;
    private double totalTimeWaited = 0;

    private String[] inRoute = {"South P", "West", "SAC", "Chapin"};
    private String[] outRoute = {"South P", "PathMart", "Walmart", "Target"};

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String[] getInRoute() {
        return inRoute;
    }

    public void setInRoute(String[] inRoute) {
        this.inRoute = inRoute;
    }

    public String[] getOutRoute() {
        return outRoute;
    }

    public void setOutRoute(String[] outRoute) {
        this.outRoute = outRoute;
    }

    public Simulator(int numInBuses, int numOutBuses, int minGroupSize, int maxGroupSize, double arrivalProb, int capacity, int duration) {
        this.numInBuses = numInBuses;
        this.numOutBuses = numOutBuses;
        this.minGroupSize = minGroupSize;
        this.maxGroupSize = maxGroupSize;
        this.arrivalProb = arrivalProb;
        this.capacity = capacity;
        this.duration = duration;
    }

    public int getNumInBuses() {
        return numInBuses;
    }

    public void setNumInBuses(int numInBuses) {
        this.numInBuses = numInBuses;
    }

    public int getNumOutBuses() {
        return numOutBuses;
    }

    public void setNumOutBuses(int numOutBuses) {
        this.numOutBuses = numOutBuses;
    }

    public int getMinGroupSize() {
        return minGroupSize;
    }

    public void setMinGroupSize(int minGroupSize) {
        this.minGroupSize = minGroupSize;
    }

    public int getMaxGroupSize() {
        return maxGroupSize;
    }

    public void setMaxGroupSize(int maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getArrivalProb() {
        return arrivalProb;
    }

    public void setArrivalProb(double arrivalProb) {
        this.arrivalProb = arrivalProb;
    }

    public double getGroupsServed() {
        return groupsServed;
    }

    public void setGroupsServed(int groupsServed) {
        this.groupsServed = groupsServed;
    }

    public double getTotalTimeWaited() {
        return totalTimeWaited;
    }

    public void setTotalTimeWaited(int totalTimeWaited) {
        this.totalTimeWaited = totalTimeWaited;
    }

    @Override
    public String toString() {
        return "Simulator{" +
                "numInBuses=" + numInBuses +
                ", numOutBuses=" + numOutBuses +
                ", minGroupSize=" + minGroupSize +
                ", maxGroupSize=" + maxGroupSize +
                ", arrivalProb=" + arrivalProb +
                ", groupsServed=" + groupsServed +
                ", totalTimeWaited=" + totalTimeWaited +
                '}';
    }

    public double[] simulate(int duration) {

        String[] inRoute = {"South P", "West", "SAC", "Chapin"};
        String[] outRoute = {"South P", "PathMart", "Walmart", "Target"};


        // MAKE FOUR QUEUES FOR IN STOPS

        ArrayList<PassengerQueue> passengersAtInStop = new ArrayList<PassengerQueue>(4);
        passengersAtInStop.add(new PassengerQueue());
        passengersAtInStop.add(new PassengerQueue());
        passengersAtInStop.add(new PassengerQueue());
        passengersAtInStop.add(new PassengerQueue());

        // MAKE FOUR QUEUES FOR OUT STOPS

        ArrayList<PassengerQueue> passengersAtOutStop = new ArrayList<PassengerQueue>(4);
        passengersAtOutStop.add(new PassengerQueue());
        passengersAtOutStop.add(new PassengerQueue());
        passengersAtOutStop.add(new PassengerQueue());
        passengersAtOutStop.add(new PassengerQueue());

        // INIT IN BUSES

        ArrayList<Bus> inBus = new ArrayList<Bus>(numInBuses);
        for(int i = 0; i < numInBuses; i++){
            inBus.add(i, new Bus(30, 0,0,i*30, new PassengerQueue()));
        }
        inBus.get(0).setTimeToRest(0);

        // INIT OUT BUSES

        ArrayList<Bus> outBus = new ArrayList<Bus>(numOutBuses);
        for(int i = 0; i < numOutBuses; i++){
            outBus.add(i, new Bus(30, 1,0,i*30, new PassengerQueue()));
        }
        outBus.get(0).setTimeToRest(0);

        // MINUTE SIM

        for(int i = 0; i < duration; i++){
            if(rollPassenger(arrivalProb)){ // Each minute check if new passenger arrives
                int whichRoute = (int) (Math.random()*2);  // Roll for which route 0: in 1: out
                int whichStop = (int) (Math.random()*4);  // Roll for which stop
                int numPass = (int)(Math.random()*maxGroupSize)+minGroupSize; // Roll for num of passengers
                int whichDest = (int) (Math.random()*4); // Roll for destination

                while(whichStop == whichDest){ // Checks if destination and stop are equal, and re-rolls
                    whichDest = (int) (Math.random()*4);
                    whichStop = (int) (Math.random()*4);
                }

                if(whichRoute == 0){ // Enqueue new passenger at in route's passenger queue
                    passengersAtInStop.get(whichStop).enqueue(new Passenger(numPass, duration, whichDest));
                }
                else{ // Else, queue at out route
                    passengersAtOutStop.get(whichStop).enqueue(new Passenger(numPass, duration, whichDest));
                }

                // Go through in buses
                // Reduce time to rest by 1 IF time to rest > 0
                // Else if toNextStop = 0, go through respective bus (O n^2), unload(), and increment nextStop
                        // AFTER UNLOAD: i - passenger arrival time == totalTimeWaited
                        // add num of passengers to groupsServed
                        // Go through current stop's passenger queue (in), attempt to enqueue while respecting bus cap size
                // If nextStop == 3, set time To rest == 30, nextStop = 0

                for(int in = 0; in < inBus.size(); in++){ // Go through IN buses
                    if(inBus.get(in).getTimeToRest() > 0) inBus.get(in).setTimeToRest(inBus.get(in).getTimeToRest() -1); // Reduce time to rest
                    else{
                        inBus.get(in).setToNextStop(inBus.get(in).getToNextStop() -1);
                    }
                    if(inBus.get(in).getTimeToRest() == 0){ // If now at stop:
                        for(int c = 0; c < inBus.get(in).getBusPQ().size(); c++){ // Unload
                            totalTimeWaited += inBus.get(in).getBusPQ().getpQ().get(c).getArrivalTime() - duration;
                            groupsServed += 1;
                            inBus.get(in).setPassengerInBus(inBus.get(i).getPassengerInBus() - inBus.get(in).unload(inBus.get(in).getNextStop())); // Update passengers in bus
                            }

                        if(inBus.get(in).getNextStop() != 3){ // Change next stop
                            inBus.get(in).setNextStop(inBus.get(in).getToNextStop() + 1);
                            inBus.get(in).setToNextStop(30);
                        }
                        else {                     // Set buses to rest
                            inBus.get(in).setNextStop(0);
                            inBus.get(in).setTimeToRest(0);
                        }

                        // Now go through stop's passenger queue and attempt to enqueue
                        for(int enq = 0; enq < passengersAtInStop.size(); enq++){
                            try{
                                if(passengersAtInStop.get(in).getpQ().get(enq).getSize() < inBus.get(in).getCap()
                                        && (passengersAtInStop.get(in).getpQ().get(enq).getSize() + inBus.get(in).getPassengerInBus() <= inBus.get(in).getCap())
                                        && passengersAtInStop.get(in).getpQ().get(enq).getDest() == inBus.get(in).getNextStop()){
                                    inBus.get(in).getBusPQ().enqueue(passengersAtInStop.get(in).getpQ().get(enq));
                                    passengersAtInStop.get(in).dequeue(passengersAtInStop.get(in).getpQ().get(enq));
                                    inBus.get(in).setPassengerInBus(inBus.get(in).getPassengerInBus() + passengersAtInStop.get(in).getpQ().get(enq).getSize());
                                }
                            }catch(Exception ignored){

                            }

                        }

                        }
                    // Set buses to rest
//                    if(inBus.get(in).getNextStop() == 3 && inBus.get(in).getToNextStop() == 0){
//                        inBus.get(in).setNextStop(0);
//                        inBus.get(in).setTimeToRest(0);
//                    }



                }


                // Go through out buses

                for(int out = 0; out < outBus.size(); out++){
                    if(outBus.get(out).getTimeToRest() > 0) outBus.get(out).setTimeToRest(outBus.get(out).getTimeToRest() -1); // Reduce time to rest
                    else{
                        outBus.get(out).setToNextStop(outBus.get(out).getToNextStop() -1);
                    }
                    if(outBus.get(out).getTimeToRest() == 0){ // If now at stop
                        for(int c = 0; c < outBus.get(out).getBusPQ().size(); c++){ // Unload
                            totalTimeWaited += outBus.get(out).getBusPQ().getpQ().get(c).getArrivalTime() - duration;
                            groupsServed += 1;
                            outBus.get(out).setPassengerInBus(outBus.get(out).getPassengerInBus() - outBus.get(out).unload(outBus.get(out).getNextStop()));
                        }

                        if(outBus.get(out).getNextStop() != 3){ // Change next stop
                            outBus.get(out).setNextStop(outBus.get(out).getToNextStop() + 1);
                            outBus.get(out).setToNextStop(30);
                        }
                        else {                     // Set buses to rest
                            outBus.get(out).setNextStop(0);
                            outBus.get(out).setTimeToRest(0);
                        }

                        // Now go through stop's passenger queue and attempt to enqueue
                        for(int enq = 0; enq < passengersAtOutStop.size(); enq++){
                            try{
                                if(passengersAtOutStop.get(out).getpQ().get(enq).getSize() < inBus.get(out).getCap()
                                        && (passengersAtOutStop.get(out).getpQ().get(enq).getSize() + inBus.get(out).getPassengerInBus() <= inBus.get(out).getCap())
                                        && passengersAtOutStop.get(out).getpQ().get(enq).getDest() == inBus.get(i).getNextStop()){
                                    inBus.get(out).getBusPQ().enqueue(passengersAtOutStop.get(out).getpQ().get(enq));
                                    passengersAtInStop.get(out).dequeue(passengersAtOutStop.get(out).getpQ().get(enq));
                                    inBus.get(out).setPassengerInBus(inBus.get(out).getPassengerInBus() + passengersAtInStop.get(out).getpQ().get(enq).getSize());
                                }
                            }catch (Exception ignored){

                            };

                        }

                    }
                    // Set buses to rest
//                    if(inBus.get(in).getNextStop() == 3 && inBus.get(in).getToNextStop() == 0){
//                        inBus.get(in).setNextStop(0);
//                        inBus.get(in).setTimeToRest(0);
//                    }



                }

            }

        }
        System.out.println("TOTAL TIME " + totalTimeWaited);
        System.out.println("GROUPS " + groupsServed);
        return new double[]{totalTimeWaited / groupsServed, groupsServed};
    }


    public static Boolean rollPassenger(double p){
        return Math.random() >= p;
    }


    public static void main(String[] args) {

        int numInBuses, numOutBuses, minGroupSize, maxGroupSize, capacity, duration;
        double arrivalProb;

        Scanner input = new Scanner(System.in);

        System.out.println("Enter the number of In Route buses: ");
        numInBuses = input.nextInt();


        System.out.println("Enter the number of Out Route buses: ");
        numOutBuses = input.nextInt();


        System.out.println("Enter the minimum group size of passengers: ");
        minGroupSize = input.nextInt();


        System.out.println("Enter the maximum group size of passengers: ");
        maxGroupSize = input.nextInt();


        System.out.println("Enter the capacity of a bus: ");
        capacity = input.nextInt();


        System.out.println("Enter the arrival probability: "); // .2
        arrivalProb = input.nextDouble();

        System.out.println("Enter the duration of the simulation: "); // minutes
        duration = input.nextInt();


        Simulator x = new Simulator(numInBuses, numOutBuses, minGroupSize, maxGroupSize, arrivalProb, capacity, duration);

        System.out.println(Arrays.toString(x.simulate(duration)));

    }
}
