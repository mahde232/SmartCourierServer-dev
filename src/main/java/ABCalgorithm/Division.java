package ABCalgorithm;

import java.util.ArrayList;

import com.smartcourier.beans.Courier;
import com.smartcourier.beans.Delivery;

public class Division {

	Courier courier;
	
	ArrayList<Delivery> deliveries = new ArrayList<Delivery>();

	public Courier getCourier() {
		return courier;
	}

	public void setCourier(Courier courier) {
		this.courier = courier;
	}

	public ArrayList<Delivery> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(ArrayList<Delivery> deliveries) {
		this.deliveries = deliveries;
	}
	
	
}
