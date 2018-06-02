package mmt;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.time.Duration;
import java.time.LocalDate;

public class Passenger implements Serializable {

    private String _name;
    private int _id;
    private Duration _timeTravelled = Duration.parse("PT0H0M");
    private double _totalValue = 0.0;
    private double _lastTen = 0.0;
    private ArrayList<Itinerary> _itineraries = new ArrayList<Itinerary>();
    private Category _category;
    
    public Passenger(int id, String name) {
        _id = id;
        _name = name;
        _category = new Normal(this);
    }
    
    public void setName(String newName) {
        _name = newName;
    }
    
    public void setCategory(Category c) {
        _category = c;
    }
    
    public double getLastTen() {
        return _lastTen;
    }
    
    public String getName() {
        return _name;
    }
    
    public int getId() {
        return _id;
    }
    
    public ArrayList<Itinerary> getItineraries() {
        return _itineraries;
    }
    
    public void addItinerary(Itinerary i) {
        _itineraries.add(i);
        _timeTravelled = _timeTravelled.plusMinutes(i.getDuration().toMinutes());
        _totalValue += i.getPrice() * (1 - _category.getDiscount());
        _lastTen += i.getPrice();
        if (_itineraries.size() >= 10)
            _lastTen -= _itineraries.get(_itineraries.size() - 10).getPrice();
        _category.changeCategory();  
    }
    
    public int numberOfItineraries() {
        return _itineraries.size();
    }
    
    public String showItineraries() {
        int size = _itineraries.size();
        int i = 1;
        Collections.sort(_itineraries, new Comparator<Itinerary>() {
            public int compare(Itinerary i1, Itinerary i2) {
                if (!i1.getDate().equals(i2.getDate())) 
                    return i1.getDate().compareTo(i2.getDate());
                else
                    return (int)(i2.getPrice() - i1.getPrice());
            }
        });
        String str = String.format("== Passageiro %d: %s ==\n", _id, _name);
        for(Itinerary itinerary: _itineraries) {
            str += "\nItinerário " + i++ + " para ";
            str += itinerary.getDate() + String.format(" @ %.2f\n", itinerary.getPrice());
            str += itinerary.toString();
        }
        return str;
    }   
    
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        String str = String.format("%d|%s|%s|%d|%.2f|", _id, _name, _category.toString(), _itineraries.size(), _totalValue);
        long hours = _timeTravelled.toHours();
        long minutes = _timeTravelled.toMinutes() - (hours * 60);
        return str + String.format("%02d:%02d", hours, minutes);
    }
}
