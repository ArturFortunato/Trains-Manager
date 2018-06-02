package mmt;

import java.io.Serializable;

public abstract class Category implements Serializable {

    private Passenger _passenger;
    private double _discount;
    
    public Category(Passenger passenger, double discount) {
        _passenger = passenger;
        _discount = discount;
    }
    
    public double getDiscount() {
        return _discount;
    }
    
    public void changeCategory() {
        if (_passenger.getLastTen() < 250)
            _passenger.setCategory(new Normal(_passenger));
        else if (_passenger.getLastTen() < 2500)
            _passenger.setCategory(new Frequente(_passenger));
        else
            _passenger.setCategory(new Especial(_passenger));
    }
    
    public abstract String toString();
}
