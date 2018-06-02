package mmt;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.time.LocalTime;

public class Station implements Serializable {
    private String _name;
    private Map<Integer, LocalTime> _timeTable = new TreeMap<Integer, LocalTime>();
    
    public Station(String name) {
        _name = name;
    }
    
    public void addTime(LocalTime l, int id) {
        _timeTable.put(id, l);
    }
    public Map<Integer, LocalTime> getTimeTable() {
        return _timeTable;
    }
    
    public String getName() {
        return _name;
    }
    
    public boolean equals(Object o) {
        if (o instanceof Station) {
            Station s = (Station) o;
            return _name.equals(s.getName());
        }
        return false;
    }
    
    public String toString() {
        return _name;
    }
}
