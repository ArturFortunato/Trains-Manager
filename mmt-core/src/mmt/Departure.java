package mmt;

import java.io.Serializable;
import java.time.LocalTime;

public class Departure implements Serializable {
    private Station _station;
    private LocalTime _time;
    
    public Departure(Station station, LocalTime time) {
        _station = station;
        _time = time;
    }
    public Station getStation() {
        return _station;
    }
    
    public LocalTime getTime() {
        return _time;
    }
    
    public String toString() {
        return _time.toString() + " " + _station.getName();
    }
}
