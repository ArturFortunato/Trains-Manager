package mmt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mmt.exceptions.BadDateSpecificationException;
import mmt.exceptions.BadTimeSpecificationException;
import mmt.exceptions.ImportFileException;
import mmt.exceptions.InvalidPassengerNameException;
import mmt.exceptions.MissingFileAssociationException;
import mmt.exceptions.NoSuchPassengerIdException;
import mmt.exceptions.NoSuchServiceIdException;
import mmt.exceptions.NoSuchStationNameException;
import mmt.exceptions.NoSuchItineraryChoiceException;
import mmt.exceptions.NonUniquePassengerNameException;
import java.util.Map;

/**
 * Façade for handling persistence and other functions.
 */
public class TicketOffice {

  /** The object doing most of the actual work. */
  private TrainCompany _trains = new TrainCompany();
  private String fileName;
  private boolean save = false;


  /** Deletes all passenger information and file association */
  
  public void reset() {
    fileName = null;
    //_trains.reset();
    Map<Integer, Service> services = _trains.getServices();
    _trains = new TrainCompany(services);
    save = true;
  }
  
  /** File related funtions: save, load, importFile */
  
  public void save(String file) throws IOException {
    ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
    oos.writeObject(_trains);
    oos.close();
    save = false;
  } 

  public void load(String file) throws FileNotFoundException, IOException, ClassNotFoundException {
    ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
    _trains = (TrainCompany) ois.readObject();
    ois.close(); 
    fileName = file;
  }

  public void importFile(String datafile) throws ImportFileException {
    _trains.importfile(datafile);
    save = true;
  }
  
  /**Passengers functions*/
  
  public String showAllPassengers() {
    return _trains.showAllPassengers();
  }
  
    
  public String showPassenger(int id) throws NoSuchPassengerIdException {
        return _trains.showPassenger(id);
  }
  
    
  public void registerPassenger(String name) throws NonUniquePassengerNameException {
    _trains.registerPassenger(name);
    save = true;
  }
  
  public void changePassengerName(String name, int id) throws NonUniquePassengerNameException, NoSuchPassengerIdException {
    _trains.changePassengerName(name, id);
    save = true;
  }
  
  /** Services functions */
  
  public String showAllServices() {
    return _trains.showAllServices();
  }
  
  public String showServiceById(int id) throws NoSuchServiceIdException {
    return _trains.showServiceById(id);
  }
  
  public String showServicesDeparting(String name) throws NoSuchStationNameException {
    return _trains.showServicesDeparting(name);
  }
  
  public String showServicesArriving(String name) throws NoSuchStationNameException {
    return _trains.showServicesArriving(name);
  }
  
  /**Itineraries functions */
  
  public String showAllItineraries() {
    return _trains.showAllItineraries();
  }
  
  public String showPassengerItineraries(int id) throws NoSuchPassengerIdException {
    return _trains.showPassengerItineraries(id);
  }
  
  public String search(int passengerId, String departureStation, String arrivalStation, String departureDate, 
                       String departureTime) throws NoSuchPassengerIdException, NoSuchStationNameException,
                       NoSuchItineraryChoiceException, BadDateSpecificationException, BadTimeSpecificationException {
    return _trains.searchItineraries(departureStation, arrivalStation, departureDate, departureTime);    
  }
  
  public void commitItinerary(int itineraryNumber, int passengerId) throws NoSuchItineraryChoiceException {
    _trains.commitItinerary(passengerId, itineraryNumber);
  }

  /** Getters and setters */
  
  public Map<Integer, Passenger> getPassengersFromCompany() {
    return _trains.getPassengers();
  }
  
  public String getFileName() {
    return fileName;
  }
  
  public void setFileName(String name) {
    fileName = name;
  }
  
  public boolean getSave() {
    return save;
  }
}  



