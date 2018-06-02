package mmt.app.itineraries;

import mmt.TicketOffice;
import mmt.app.exceptions.BadDateException;
import mmt.app.exceptions.BadTimeException;
import mmt.app.exceptions.NoSuchItineraryException;
import mmt.app.exceptions.NoSuchPassengerException;
import mmt.app.exceptions.NoSuchStationException;
import mmt.exceptions.BadDateSpecificationException;
import mmt.exceptions.BadTimeSpecificationException;
import mmt.exceptions.NoSuchPassengerIdException;
import mmt.exceptions.NoSuchStationNameException;
import mmt.exceptions.NoSuchItineraryChoiceException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import java.time.LocalTime;
import java.time.LocalDate;

/**
 * §3.4.3. Add new itinerary.
 */
public class DoRegisterItinerary extends Command<TicketOffice> {

  private Input<Integer> id;
  private Input<String> departure;
  private Input<String> arrival;
  private Input<String> date;
  private Input<String> time;
  private Input<Integer> choice;

  /**
   * @param receiver
   */
  public DoRegisterItinerary(TicketOffice receiver) {
    super(Label.REGISTER_ITINERARY, receiver);
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    _form.clear();
    id = _form.addIntegerInput(Message.requestPassengerId());
    departure = _form.addStringInput(Message.requestDepartureStationName());
    arrival = _form.addStringInput(Message.requestArrivalStationName());
    date = _form.addStringInput(Message.requestDepartureDate());
    time = _form.addStringInput(Message.requestDepartureTime());
    _form.parse();
    try {
      String s = (_receiver.search(id.value(), departure.value(), arrival.value(), date.value(), time.value()));
      if (s != null) {
        _display.addLine(s);
        _display.display();
        _form.clear();
        choice = _form.addIntegerInput(Message.requestItineraryChoice());
        _form.parse();
        _receiver.commitItinerary(choice.value(), id.value());
      }
      else
        _receiver.commitItinerary(0,id.value());
    } 
    catch (NoSuchPassengerIdException e) {
      throw new NoSuchPassengerException(e.getId());
    } 
    catch (NoSuchStationNameException e) {
      throw new NoSuchStationException(e.getName());
    } 
    catch (NoSuchItineraryChoiceException e) {
      throw new NoSuchItineraryException(e.getPassengerId(), e.getItineraryId());
    } 
    catch (BadDateSpecificationException e) {
      throw new BadDateException(e.getDate());
    } 
    catch (BadTimeSpecificationException e) {
      throw new BadTimeException(e.getTime());
    }
  }
}
