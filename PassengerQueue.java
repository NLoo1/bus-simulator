import java.util.LinkedList;


public class PassengerQueue extends LinkedList {

    private LinkedList<Passenger> pQ;

    public PassengerQueue() {
        pQ = new LinkedList<Passenger>();
    }

    public PassengerQueue(LinkedList<Passenger> pQ) {
        this.pQ = pQ;
    }

    public LinkedList<Passenger> getpQ() {
        return pQ;
    }

    public void setpQ(LinkedList<Passenger> pQ) {
        this.pQ = pQ;
    }

    public void enqueue(Passenger p){
        pQ.add(p);
    }

    public Passenger dequeue(Passenger p){
        pQ.remove(p);
        return p;
    }

}
