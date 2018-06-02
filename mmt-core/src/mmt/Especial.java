package mmt;

import java.io.Serializable;

public class Especial extends Category implements Serializable {
    
    public Especial(Passenger passenger) {
        super(passenger, 0.5);
    }
    
    @Override
    public String toString() {
        return "ESPECIAL";
    }
}
