package mmt;

import mmt.exceptions.BadDateSpecificationException;
import mmt.exceptions.BadEntryException;
import mmt.exceptions.BadTimeSpecificationException;
import mmt.exceptions.InvalidPassengerNameException;
import mmt.exceptions.NoSuchDepartureException;
import mmt.exceptions.NoSuchPassengerIdException;
import mmt.exceptions.NoSuchServiceIdException;
import mmt.exceptions.NoSuchStationNameException;
import mmt.exceptions.NoSuchItineraryChoiceException;
import mmt.exceptions.NonUniquePassengerNameException;
import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import mmt.exceptions.ImportFileException;

import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Collections;
import java.util.Comparator;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
/**
 * A train company has schedules (services) for its trains and passengers that
 * acquire itineraries based on those schedules.
 */
public class TrainCompany implements Serializable {

  /** Serial number for serialization. */
    private static final long serialVersionUID = 201708301010L;

    private Map<Integer, Passenger> _passengers = new TreeMap<Integer, Passenger>() ;
    private Map<Integer, Service> _services = new TreeMap<Integer, Service>();
    private Map<String, Station> _stations = new TreeMap<String, Station>(); 
    private ArrayList<Itinerary> itiTemp = new ArrayList<Itinerary>();
    private ArrayList<Itinerary> toRemove = new ArrayList<Itinerary>();
    
    public TrainCompany(Map<Integer, Service> services) {
        _services = services;
    }
    
    public TrainCompany() {}
    
    /**
    * @return Nothing
    */
    
    public void reset() {
        _passengers.clear();
    }
    
    /**
    * Imports passengers, services and itineraries from a file
    *
    * @param filename is the name of file to import
    * @throws ImportFileException on file read error
    * @see mmt.exceptions.NoSuchPassengerIdException
    * @return String with passenger #id
    */
    
    void importfile(String filename) throws ImportFileException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            Station station;
            // Reads all lines from file "filename"
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|");
                Pattern patPassenger = Pattern.compile("^(PASSENGER)");
                Pattern patService = Pattern.compile("^(SERVICE)");
                Pattern patItinerary = Pattern.compile("^(ITINERARY)");
                if (patPassenger.matcher(fields[0]).matches()){
                    // Registers passengers
                    registerPassenger(fields[1]);
                }
                else if (patService.matcher(fields[0]).matches()) {
                    // Creates and registers services
                    ArrayList<Departure> departures = new ArrayList<Departure>();
                    int id = Integer.parseInt(fields[1]);
                    double price = Double.parseDouble(fields[2]);
                    for(int i = 3; i < fields.length; i++) {
                        LocalTime l = LocalTime.parse(fields[i]);
                        if (_stations.containsKey(fields[++i]))
                            station = _stations.get(fields[i]);
                        else {
                            station = new Station(fields[i]);
                            _stations.put(fields[i], station);
                        }
                        station.addTime(l, id);
                        Departure d = new  Departure(station, l);
                        departures.add(d);
                    }
                    _services.put(id, new Service(id, price, departures));
                }
                else if (patItinerary.matcher(fields[0]).matches()) {
                    LocalDate date = LocalDate.parse(fields[2]);
                    double price = 0;
                    int it_id;
                    int id;
                    ArrayList<Segment> segments = new ArrayList<Segment>();
                    for(int i = 3; i < fields.length; i++) {
                        String[] parts = fields[i].split("/");
                        Station init = _stations.get(parts[1]);
                        Station end = _stations.get(parts[2]);
                        Segment s = new Segment(_services.get(Integer.parseInt(parts[0])), init, end);
                        price += s.price();
                        segments.add(s);
                    }
                    id = Integer.parseInt(fields[1]);
                    it_id = _passengers.get(id).numberOfItineraries() + 1;
                    Itinerary itinerary = new Itinerary(segments, price, date);
                    itinerary.setId(it_id);
                    _passengers.get(Integer.parseInt(fields[1])).addItinerary(itinerary);
                }
            }
        }
        catch (IOException e){
            throw new ImportFileException();
        }
        catch (NonUniquePassengerNameException e) {
            throw new ImportFileException();
        }
    }
    /**
    * Shows all passengers
    *
    * @return String with all passengers
    */
    
    public String showAllPassengers() {
        String str = "";
        int len = _passengers.size();
        for(int i = 0; i < len; i++) 
            str = str + _passengers.get(i).toString() + "\n";
        return str;
    }
    
    /**
    * Shows specific passenger
    *
    * @param id is the passenger identifier
    * @throws NoSuchPassengerIdException if the passenger #id doesn't exist
    * @see mmt.exceptions.NoSuchPassengerIdException
    * @return String with passenger #id
    */
    
    public String showPassenger(int id)  throws NoSuchPassengerIdException {
        if (id >= 0 && id < _passengers.size()) 
            return _passengers.get(id).toString();
        throw new NoSuchPassengerIdException(id);
    }
    
    /**
    * Registers a passenger
    *
    * @param name is the passenger's name
    * @throws NonUniquePassengerNameException if there's a passsenger with the same name
    * @see mmt.exceptions.NonUniquePassengerNameException
    * @return Nothing
    */
    
    public void registerPassenger(String name) throws NonUniquePassengerNameException {
        if(searchPassengerByName(name) != -1)
            throw new NonUniquePassengerNameException (name);
        Passenger p = new Passenger(_passengers.size(), name);
        _passengers.put(p.getId(), p);
    }
    
    /**
    * Changes a passenger name
    *
    * @param name is the passenger's new name
    * @param id is the passenger's id
    * @throws NonUniquePassengerNameException if the name is taken
    * @throws NoSuchPassengerIdException if there is no such passenger
    * @see mmt.exceptions.NonUniquePassengerNameException
    * @see mmt.exceptions.NoSuchPassengerIdException
    * @return Nothing
    */
    
    public void changePassengerName(String name, int id)  throws NonUniquePassengerNameException, NoSuchPassengerIdException {
        int searchid = searchPassengerByName(name);
        if(searchid != -1 && searchid != id)
            throw new NonUniquePassengerNameException (name);
        else if (_passengers.size() == 0 || id < 0 || id >= _passengers.size())
            throw new NoSuchPassengerIdException(id);
        _passengers.get(id).setName(name);
    }
    
    /**
    * Searches passenger by id
    *
    * @param name is the passenger name
    * @return int This returns passenger id (or -1 if it doesn't exist)
    */
    
    public int searchPassengerByName(String name) {
        int len = _passengers.size();
        for (int i = 0; i < len ; i++)
            if (name.equals(_passengers.get(i).getName()))
                return i;
        return -1;
    }
    
    /**
    * @return Map of passengers
    */
    
    public Map<Integer,Passenger> getPassengers() {
        return _passengers;
    }
 
    /**
    * Shows all services 
    *
    * @return String with all services
    */
    
    public String showAllServices() {
        String str = "";
        int len = _services.size();
        for(Map.Entry<Integer, Service> entry: _services.entrySet()){
            Service s = entry.getValue();
            str = str + s.toString();
        }
        return str;
    }
    
    /**
    * Show service 
    *
    * @param id is the service identifier
    * @throws NoSuchServiceIdException if there is no service #id
    * @see mmt.exceptions.NoSuchServiceIdException
    * @return String with the service #id 
    */
    
    public String showServiceById(int id) throws NoSuchServiceIdException {
        for(Map.Entry<Integer, Service> entry: _services.entrySet()){
            Service s = entry.getValue();
            if(s.getId() == id)
                return s.toString();
        }
        throw new NoSuchServiceIdException(id);
    }
    
    /**
    * Show all services departing from a station
    *
    * @param name is the station name
    * @return String with all services departing from station name
    */
    
    public String showServicesDeparting(String name) throws NoSuchStationNameException {
        boolean exists = false;
        for(Map.Entry<String, Station> e: _stations.entrySet())
            if (name.equals(e.getValue().getName()))
                exists = true;
        if(exists) {
            Map<LocalTime, Service> serv = new TreeMap<LocalTime, Service>();
            Service s;
            Departure d;
            Station station;
            String str = "";
            for(Map.Entry<Integer, Service> entry: _services.entrySet()){
                s = entry.getValue();
                d = s.getDepartures().get(0);
                station = d.getStation();
                if(station.getName().equals(name))
                    serv.put(d.getTime(), s);
            }
            for(Map.Entry<LocalTime, Service> entry: serv.entrySet())
                str += entry.getValue().toString();
            return str;
        }
        throw new NoSuchStationNameException(name);
    }
    
    /**
    * Show all services departing from a station
    *
    * @param name is the station name
    * @return String with all services arriving at station name
    */
    
    public String showServicesArriving(String name) throws NoSuchStationNameException {
        boolean exists = false;
        for(Map.Entry<String, Station> e: _stations.entrySet())
            if (name.equals(e.getValue().getName()))
                exists = true;
        if(exists) {
            Map<LocalTime, Service> serv = new TreeMap<LocalTime, Service>();
            Service s;
            Departure d;
            Station station;
            String str = "";
            int len;
            for(Map.Entry<Integer, Service> entry: _services.entrySet()){
                s = entry.getValue();
                len = s.getDepartures().size();
                d = s.getDepartures().get(len - 1);
                station = d.getStation();
                if(station.getName().equals(name))
                    serv.put(d.getTime(), s);
            }
            for(Map.Entry<LocalTime, Service> entry: serv.entrySet())
                str += entry.getValue().toString();
            return str;
        }
        throw new NoSuchStationNameException(name);
    }
    
    public Map<Integer, Service> getServices() {
        return _services;
    }

/*-----------------------ITINERARIES----------------------------------------------*/

    /**
    * Show all inineraries 
    *
    * @return String with all Inineraries
    */
    
    public String showAllItineraries() {
        String str="";
        for(Map.Entry<Integer, Passenger> p: _passengers.entrySet())
            if(p.getValue().numberOfItineraries() > 0) 
                str += p.getValue().showItineraries();
        return str;
    }
    
    /**
    * Show all itineraries of a specific passenger
    *
    * @param id is the passenger id
    * @throws NoSuchPassengerIdException if no passenger with id #id
    * @see mmt.exceptions.NoSuchPassengerIdException
    * @return String with passenger itineraries
    */
    
    public String showPassengerItineraries(int id) throws NoSuchPassengerIdException {
        if (id < 0 || id >=  _passengers.size())
            throw new NoSuchPassengerIdException(id);
        else if (_passengers.get(id).numberOfItineraries() == 0)
            return null;
        return _passengers.get(id).showItineraries();
    }
    
    /**
    * Search all possible itineraries between two stations and
    * return the string representation of this itineraries
    *
    * @param d is the departure station's name
    * @param a is the arrival station's name
    * @param date is the itinerary date
    * @param hour is the itinerary minimum time
    * @throws NoSuchStationNameException if no station d or a is found
    * @throws BadDateSpecificationException if date is in wrong format
    * @throws BadTimeSpecificationException if time is in wrong format
    * @see mmt.exceptions.NoSuchPassengerIdException
    * @see mmt.exceptions.BadDateSpecificationException;
    * @see mmt.exceptions.BadTimeSpecificationException;
    * @return String all possible itineraries
    */
    
    public String searchItineraries(String d, String a, String date, String hour) throws NoSuchStationNameException, BadDateSpecificationException, BadTimeSpecificationException{
        String str = "";
        LocalDate data;
        LocalTime time;
        if (!_stations.containsKey(d))
            throw new NoSuchStationNameException(d);
        else if (!_stations.containsKey(d))
            throw new NoSuchStationNameException(a);
        try {
            data = LocalDate.parse(date);
        }
        catch (DateTimeParseException e) {
            throw new BadDateSpecificationException(date);
        }
        try {
            time = LocalTime.parse(hour);
        }
        catch (DateTimeParseException e) {
            throw new BadTimeSpecificationException(hour);
        }
            ArrayList<Segment> segments = new ArrayList<Segment>();
            searchSegments(d, d, a, time, data, -1, segments);
            if (itiTemp.size() == 0)
                return null;
            Collections.sort(itiTemp, new Comparator<Itinerary>() {
                public int compare(Itinerary i1, Itinerary i2) {
                    if (i1.getDepTime().compareTo(i2.getDepTime()) != 0)
                        return i1.getDepTime().compareTo(i2.getDepTime());
                    else if (i1.getArrTime().compareTo(i2.getArrTime()) != 0)
                        return  i2.getArrTime().compareTo(i1.getArrTime());
                    return (int)(i2.getPrice() - i1.getPrice());
                }
            });
            itiTemp.removeAll(toRemove);
            toRemove.clear();
            return convertToString(itiTemp);
    }
    
    /**
    * Recursively search all possible itineraries between two stations
    *
    * @param lastSChange is the station's name of last service change
    * @param Cur is the current station's name
    * @param arrival is the arrival station's name
    * @param time is the minimum time for segment search
    * @param curServ is the current service's id
    * @param segments is a temporary list of segments that will compose the itinerary
    * @return Nothing
    */
    
    public void searchSegments(String lastSChange, String Cur, String Arrival,  LocalTime time, LocalDate date, int curServ, ArrayList<Segment> segments) {
        Station lastSerChange = _stations.get(lastSChange);
        Station cur = _stations.get(Cur);
        Station arr = _stations.get(Arrival);
        Segment s1;
        int id;
        int curTemp = curServ;
        boolean finish = false;
        for(Map.Entry<Integer, LocalTime> entry: cur.getTimeTable().entrySet()){
            if (entry.getValue().compareTo(time) >= 0) {
                id = entry.getKey();
                if (curServ == -1)
                    curServ = id;
                if (_services.get(id).hasAfter(cur, arr)) {
                    finish = true;
                    ArrayList<Segment> temp = new ArrayList<Segment>(segments);
                    if (id == curServ) {
                        s1 = new Segment(_services.get(curServ), _stations.get(lastSChange), arr);
                        temp.add(s1);
                        Itinerary itinerary = new Itinerary(temp, s1.price() , date);
                        if (canInsertItinerary(itinerary))
                            itiTemp.add(itinerary);

                    }
                    else {
                        if (!lastSerChange.equals(cur)) {
                            Segment s = new Segment(_services.get(curServ), lastSerChange, cur);
                            temp.add(s);
                        }
                        s1 = new Segment(_services.get(id), cur, arr);
                        temp.add(s1);
                        double price = getPrice(temp);
                        Itinerary itinerary = new Itinerary(temp, price , date);
                        if (canInsertItinerary(itinerary))
                            itiTemp.add(itinerary);
                    }
                }
            }
        }
        if (finish == false) {
            for(Map.Entry<Integer, LocalTime> entry: cur.getTimeTable().entrySet()){
                id = entry.getKey();
                if (entry.getValue().compareTo(time) >= 0) {
                    ArrayList<Segment> segments1 = new ArrayList<Segment>(segments);
                    if (curTemp == -1)
                        curTemp = id;
                    if (curTemp != id && lastSChange != Cur) {
                        Segment s2 = new Segment(_services.get(curServ), _stations.get(lastSChange), cur);
                        segments1.add(s2);
                        lastSChange = Cur;
                    }
                    Station next = _services.get(id).getNext(cur);
                    LocalTime time1 = entry.getValue();
                    while(next != null) {
                        searchSegments(lastSChange, next.getName(), arr.getName(), time1, date, id, segments1);
                        next = _services.get(id).getNext(next);
                    }
                }
            }
        }
    }
    
    /**
    * Insert itinerary in a specific passenger's itineraries list
    *
    * @param choice is the itinerary choice
    * @param id is the passenger id
    * @throws NoSuchItineraryChoiceException if itinerary choice does not exist
    * @see mmt.exceptions.NoSuchItineraryChoiceException;
    * @return Nothing
    */
    
    public void commitItinerary(int id, int choice) throws NoSuchItineraryChoiceException{
        if (choice == 0)
            itiTemp.clear();
        else if (choice <= itiTemp.size() && choice > 0) {
            int i = 0;
            Itinerary it = null;
            for (Itinerary itinerary: itiTemp)
                if (i++ == choice - 1) {
                    it = itinerary;
                    break;
                }
            _passengers.get(id).addItinerary(it);
            itiTemp.clear();
        }
        else {
            itiTemp.clear();
            throw new NoSuchItineraryChoiceException(id, choice);
        }
    }

    /**
    * Calculate the sum of prices of all segments in a segments list 
    * 
    * @param segments is the list of segments of a itinerary
    * @return sum of segment prices
    */
    
    public double getPrice(ArrayList<Segment> segments) {
        double price = 0;
        for(Segment s: segments)
            price += s.price();
        return price;
    }
    
    /**
    * Convert a list of itineraries into it's string representation
    *
    * @param itineraries is the list of possible itineraries
    * @return String representation of all itineraries in param itineraries
    */
    
    public String convertToString(ArrayList<Itinerary> itineraries) {
        String str = "";
        int i = 1;
        for(Itinerary itinerary: itineraries) {
            str += String.format("\nItinerário %d para %s @ %.2f", i++, itinerary.getDate(), itinerary.getPrice());
            str += "\n" + itinerary.toString();
        }
        return str;
    }
    
    /**
    * See if an itinerary is direct
    *
    * @param itinerary
    * @return true if itinerary is direct, false otherwise
    */
    
    public boolean direct(Itinerary itinerary) {
        if (itinerary.getSegments().size() == 1)
            return true;
        return false;
    }

    /**
    * See if it is possible to insert a speciffic itinerary in the
    * itinerary list: blocks repeated itineraries and itineraries beginning
    * in the same service if it arrives after the existing
    *
    * @param itinerary
    * @return true if the itinerary should be inserted on the list, false otherwise
    */
    
    public boolean canInsertItinerary(Itinerary itinerary) {
        Segment first = itinerary.getSegments().get(0);
        int firstService = first.getService().getId();
        for(Itinerary it: itiTemp)
            if (it.getFirstId() == firstService) {
                if (it.getArrTime().compareTo(itinerary.getArrTime()) <= 0 && !direct(it)) 
                    return false;
                if (it.equals(itinerary))
                    return false;
                if(it.getArrTime().compareTo(itinerary.getArrTime()) > 0 && !direct(it))
                    toRemove.add(it);  
            }
        return true;                
    }
}
