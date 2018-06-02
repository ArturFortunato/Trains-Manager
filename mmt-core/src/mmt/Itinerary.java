package mmt;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.text.DecimalFormat;

public class Itinerary implements Serializable {

    private double _price;
    private LocalDate _date;
    private int _id; 
    private Duration _duration;
    private ArrayList<Segment> _segments = new ArrayList<Segment>();
    
    public Itinerary(ArrayList<Segment> segments, double price, LocalDate date) {
        _segments = segments;
        _price = price;
        _date = date;
        _duration = setDuration();
    }
    
    public LocalDate getDate() {
            return _date;
    }
    
    public double getPrice() {
        return _price;
    }
    
    public ArrayList<Segment> getSegments() {
        return _segments;
    }
    
    public Duration getDuration() {
        return _duration;
    }
    
    public int getFirstId() {
        return _segments.get(0).getServiceId();
    }
    
    public LocalTime getDepTime() {
        return _segments.get(0).getDepTime();
    }
    
    public LocalTime getArrTime() {
        return _segments.get(_segments.size() - 1).getArrTime();
    }
    
    public Duration setDuration() {
        Duration duration = Duration.parse("PT0H0M");
        LocalTime arr;
        LocalTime dep;
        for(int i = 0; i < _segments.size(); i++ ) {
            duration = duration.plusMinutes(_segments.get(i).getDuration().toMinutes());
            if (i != _segments.size() - 1) {
                arr = _segments.get(i).getArrTime();
                dep = _segments.get(i + 1).getDepTime();
                duration = duration.plusMinutes(Duration.between(arr, dep).toMinutes());
            }
        }
        return duration;
    }
    
    public void setId(int id) {
        _id = id;
    }
    
    @Override
    public boolean equals(Object o) {
        Itinerary it = (Itinerary) o;
        if (it.getSegments().size() == _segments.size()){
            for(int i = 0; i < _segments.size(); i++)
                if (!_segments.get(i).equals(it.getSegments().get(i)))
                    return false;
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < _segments.size(); i++)
            str += _segments.get(i).toString();
        return str;
    }
}
