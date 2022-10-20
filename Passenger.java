public class Passenger {

    private int size;
    private int arrivalTime;
    private int dest;


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public Passenger(){
        size = 0;
        arrivalTime = 0;
        dest = 0;
    }

    public Passenger(int size, int arrivalTime, int dest) {
        this.size = size;
        this.arrivalTime = arrivalTime;
        this.dest = dest;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "size=" + size +
                ", arrivalTime=" + arrivalTime +
                ", dest=" + dest +
                '}';
    }
}
