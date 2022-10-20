public class Bus{

    private int cap;
    private int passengerInBus;
    private int timeToRest;
    private int route;
    private int nextStop;
    private int toNextStop;

    private PassengerQueue busPQ;


    public Bus(int timeToRest, int route, int nextStop, int toNextStop, PassengerQueue busPQ) {
        this.timeToRest = timeToRest;
        this.route = route;
        this.nextStop = nextStop;
        this.toNextStop = toNextStop;
        this.busPQ = busPQ;
    }

    public int unload(int dest){
        int a = 0;
        for (int i = 0; i < busPQ.size(); i++){ // Go through bus
               if(busPQ.getpQ().get(i).getDest() == nextStop){
                   a+= busPQ.getpQ().get(i).getSize();
                   busPQ.dequeue(busPQ.getpQ().get(i));
               }
        }
        return a;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "timeToRest=" + timeToRest +
                ", route=" + route +
                ", nextStop=" + nextStop +
                ", toNextStop=" + toNextStop +
                ", busPQ=" + busPQ +
                '}';
    }

    public  int getCap() {
        return cap;
    }

    public  void setCap(int cap) {
        this.cap = cap;
    }

    public int getTimeToRest() {
        return timeToRest;
    }

    public void setTimeToRest(int timeToRest) {
        this.timeToRest = timeToRest;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public int getNextStop() {
        return nextStop;
    }

    public void setNextStop(int nextStop) {
        this.nextStop = nextStop;
    }

    public int getToNextStop() {
        return toNextStop;
    }

    public void setToNextStop(int toNextStop) {
        this.toNextStop = toNextStop;
    }

    public PassengerQueue getBusPQ() {
        return busPQ;
    }

    public void setBusPQ(PassengerQueue busPQ) {
        this.busPQ = busPQ;
    }

    public int getPassengerInBus() {
        return passengerInBus;
    }

    public void setPassengerInBus(int passengerInBus) {
        this.passengerInBus = passengerInBus;
    }
}
