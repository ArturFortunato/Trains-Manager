package mmt;

import java.io.Serializable;

public class Normal extends Category implements Serializable {

    public Normal(Passenger passenger) {
        super(passenger, 0.0);
    }
    
    public String toString() {
        return "NORMAL";
    }
}
