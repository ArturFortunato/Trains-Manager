package mmt.app.main;

import java.io.IOException;
import mmt.exceptions.MissingFileAssociationException;

import mmt.TicketOffice;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;

//FIXME import other classes if necessary

/**
 * §3.1.1. Save to file under current name (if unnamed, query for name).
 */
public class DoSave extends Command<TicketOffice> {
  
  private Input<String> fileName;
  
  /**
   * @param receiver
   */
  public DoSave(TicketOffice receiver) {
    super(Label.SAVE, receiver);
    if (_receiver.getFileName() == null)
        fileName = _form.addStringInput(Message.newSaveAs());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    if (_receiver.getSave()){
        try {
            if(_receiver.getFileName() == null){
                _form.parse();
                _receiver.setFileName(fileName.value());
                _receiver.save(fileName.value());
            }
            _receiver.save(_receiver.getFileName());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
  }
}
