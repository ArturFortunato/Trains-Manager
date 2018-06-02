package mmt;

import java.io.Serializable;

public class Frequente extends Category implements Serializable{
    
    public Frequente(Passenger passenger) {
        super(passenger, 0.15);
    }
    
    @Override
    public String toString() {
        return "FREQUENTE";
    }
}
