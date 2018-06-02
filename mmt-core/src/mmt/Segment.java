package mmt;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.Duration;

public class Segment implements Serializable {
    private Service _service;
    private Station _start;
    private Station _end;
    private double _price;
    
    public Segment(Service service, Station start, Station end) {
        _service = service;
        _start = start;
        _end = end;
        _price = price();
    }
    
    public int getServiceId() {
        return _service.getId();
    }
    public Service getService() {
        return _service;
    }
    
    public LocalTime getDepTime() {
        return _service.getStationTime(_start);
    }
    
    public LocalTime getArrTime() {
        return _service.getStationTime(_end);
    }
    
    public double price() {
        return _service.getFractionPrice(_start, _end);
    }
    
    public Duration getDuration() {
        return _service.diffTime(_start, _end);
    }

    public Station getStart() {
        return _start;
    }
    
    public Station getEnd() {
        return _end;
    }
    
    public boolean equals(Object o) {
        if(o instanceof Segment) {
            Segment s = (Segment) o;
            if (_service.getId() == s.getServiceId())
                if (_start.equals(s.getStart()) && _end.equals(s.getEnd()))
                    return true;
        }
        return false;
    }
    
    @Override
    public String toString() { 
        return _service.toString(_start,_end);
    }
}
