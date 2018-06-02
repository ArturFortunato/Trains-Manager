package mmt.app.main;

import mmt.TicketOffice;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * §3.1.1. Open existing document.
 */
public class DoOpen extends Command<TicketOffice> {
  /**
   * @param receiver
   */
  private Input<String> fileName;
  
  public DoOpen(TicketOffice receiver) {
    super(Label.OPEN, receiver);
    fileName = _form.addStringInput(Message.openFile());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    try {
        _form.parse();
        _receiver.load(fileName.value());
    }
    catch (FileNotFoundException e) {
        _display.popup(Message.fileNotFound());
    }
    catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
  }
}
