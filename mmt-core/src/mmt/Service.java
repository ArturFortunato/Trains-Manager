package mmt;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.time.Duration;

public class Service implements Serializable {

    private int id;
    private double price;
    private Duration duration;
    private ArrayList<Departure> departures = new ArrayList<Departure>();
    
    public Service(int id, double price, ArrayList<Departure> departures) {
        this.id = id;
        this.price = price;
        this.departures = departures;
        this.duration = Duration.between(departures.get(0).getTime(), departures.get(departures.size() - 1).getTime());
    }
    
    public int getId() {
        return id;
    } 
    
    public double getPrice() {
        return price;
    }
    
    public LocalTime getStationTime(Station name) {
        LocalTime l = null;
        for(Departure d: departures)
            if (d.getStation().equals(name)) {
                l = d.getTime();
                break;
            }
        return l;
    }
    
    public ArrayList<Departure> getDepartures() {
        return departures;
    }
    
    public Station getNext(Station station) {
        boolean end = false;
        for(Departure d: departures) {
            if (end == true)
                return d.getStation();
            if(d.getStation().equals(station))
                end = true;
        }
        return null;
    }
    
    public int getDepartureLength() {
        return departures.size();
    }
    
    public Duration diffTime(Station init, Station end) {
        LocalTime in;
        int i;
        Departure d = null;
        Duration dur = Duration.parse("PT0H0M");
        for (i = 0; i < departures.size(); i++ ) {
            d = departures.get(i);
            if (init.equals(d.getStation())) 
                break;
        }
        in = d.getTime();
        for (; i < departures.size(); i++ ) {
            d = departures.get(i);
            if (end.equals(d.getStation()))
                break; 
        }
        dur = Duration.between(in, d.getTime());
        return dur;
    }
    
    public double getFractionPrice(Station init, Station end) {
        double dur = (double) diffTime(init, end).toMinutes();
        return (dur / duration.toMinutes()) * price;
    }
    
    public boolean hasAfter(Station dep, Station arr) {
        int i;
        for(i = 0; !departures.get(i).getStation().equals(dep); i++);
        for( ; i < departures.size(); i++)
            if (departures.get(i).getStation().equals(arr))
                return true;
        return false;
    }
    
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        int size = departures.size();
        String str = String.format("Serviço #%d @ %.2f\n", id, price) ;
        for (int i = 0; i < size; i++) 
            str += departures.get(i).toString() + "\n";
        return str;
    } 
    
    public String toString(Station init, Station end) {
        int i;
        String str = String.format("Serviço #%d @ %.2f\n", id, getFractionPrice(init, end));
        for(i = 0; !init.equals(departures.get(i).getStation()); i++);
        str += departures.get(i).toString() + "\n";
        for(++i; !end.equals(departures.get(i - 1).getStation()); i++)
            str += departures.get(i).toString() + "\n";
        return str;
    }
}
