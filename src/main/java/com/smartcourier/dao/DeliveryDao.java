package com.smartcourier.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartcourier.beans.Courier;
import com.smartcourier.beans.Delivery;
import com.smartcourier.beans.Region;

@Repository
public interface DeliveryDao extends JpaRepository<Delivery, Long>{
	public List<Delivery> findByRegionAndType(Region region, Integer type);
	public List<Delivery> findByCourierAndRegion(Courier courier , Region region);
	public List<Delivery> findByCourierAndType(Courier courier , Integer type);
	public List<Delivery> findByCourierAndRegionAndType(Courier courier , Region region, Integer type);

}
