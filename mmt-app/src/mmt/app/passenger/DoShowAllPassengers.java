package mmt.app.passenger;

//import mmt.DefaultSelector;
//import mmt.DefaultVisitor;
import mmt.TicketOffice;
import pt.tecnico.po.ui.Command;
import java.util.Map;
import mmt.Passenger;
//FIXME import other classes if necessary

/**
 * §3.3.1. Show all passengers.
 */
public class DoShowAllPassengers extends Command<TicketOffice> {

  /**
   * @param receiver
   */
  public DoShowAllPassengers(TicketOffice receiver) {
    super(Label.SHOW_ALL_PASSENGERS, receiver);
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    _display.addLine(_receiver.showAllPassengers());
    _display.display();
  }
}
