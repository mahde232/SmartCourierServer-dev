package com.smartcourier.controllers;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcourier.beans.Courier;
import com.smartcourier.beans.Delivery;
import com.smartcourier.beans.Region;
import com.smartcourier.beans.User;
import com.smartcourier.dao.CourierDao;
import com.smartcourier.dao.DeliveryDao;
import com.smartcourier.dao.RegionDao;

import ABCalgorithm.Division;

import com.smartcourier.dao.AppDao;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/courier")
@Api(value="Courier Management")
public class CourierController {

	public static final Logger logger = LoggerFactory.getLogger(CourierController.class);

	@Autowired
	AppDao appDao;
	
	@Autowired
	CourierDao courierDao;

	@Autowired
	RegionDao regionDao;
	
	@Autowired
	DeliveryDao deliveryDao;
	
	@ApiOperation(value="Get courier", response= Iterable.class)
	@GetMapping("/{courierId}")
	public Courier getCourierById(@PathVariable(value = "courierId") Long courierId) {
		return courierDao.findOne(courierId);
	}
	
	@GetMapping("/getAll")
	public List<Courier> getAllCouriers(){
		return courierDao.findAll();
	}
	
	@ApiOperation(value="Delete courier", response= Iterable.class)
	@DeleteMapping("/delete/{courierId}")
	public Boolean deleteCourier(@PathVariable(value = "courierId") Long courierId) {
		Courier courier = courierDao.findOne(courierId);
		if(courier != null)
		{
			for(Delivery delivery: courier.getDelivery())
			{
				//unassigned delivery from this courier.
				if(delivery.getCourier().getId() == courierId)
				{
					delivery.setCourier(null);
					delivery.setType(0);//This delivery has not been assigned to any courier.
					deliveryDao.save(delivery);
				}
			}
			for(Region region : courier.getRegion())
			{
				region.getCourier().remove(courier);
				regionDao.save(region);
			}
			courierDao.delete(courier);
	        return true;
		}else
			return false;
	}
	
	@ApiOperation(value="Update delivery", response= Iterable.class)
	@GetMapping("/update/{courierId}/{deliveryId}")
	public Delivery assignDeliveryToCourier(@PathVariable(value = "deliveryId") Long deliveryId, @PathVariable(value = "courierId") Long courierId) {
		Courier courier = courierDao.findOne(courierId);
		Delivery delivery = deliveryDao.findOne(deliveryId);
		if(courier != null && delivery != null){
			delivery.setType(1);//: Deliveries were distribute by the algorithm (Or manually by the clients) to a courier but the courier has has not yet confirmed that he is willing to distribute them.
			delivery.setCourier(courier);
			Delivery savedDelivery = deliveryDao.save(delivery);
			return savedDelivery;
		} else{
			return null;
		}
	}	
	
	/*@ApiOperation(value="Delete courier", response= Iterable.class)
	@DeleteMapping("/delete/{courierId}")
	public Boolean deleteCourier(@PathVariable(value = "courierId") Long courierId) {
		List<User> users = appController.getAllUsers();
		for(User user : users){
			if(user.getCourier().getId().equals(courierId)){
				break;
			}
		}
		Courier courier = courierDao.findOne(courierId);
		if(courier != null){
			courierDao.delete(courier);
			return true;
		} else{
			return false;
		}
	}*/
	
	@ApiOperation(value="Create courier", response= Iterable.class)
	@PutMapping("/create")
	public Courier createCourier(@RequestBody Courier courier) {
		Courier courierFind = courierDao.findByEmail(courier.getEmail());
		if( courierFind != null)
			return null;
		courierDao.save(courier);
		return courier;
	}
	
	@ApiOperation(value="Update courier", response= Iterable.class)
	@PutMapping("/update/{courierId}")
	public Courier updateCourier(@PathVariable(value = "courierId") Long courierId, @RequestBody Courier courier) {
		Courier currentCourier = courierDao.findOne(courierId);
		if(currentCourier != null){
			courierDao.delete(currentCourier);
			return courierDao.save(courier);
		} else{
			return null;
		}
	}
	
	@ApiOperation(value="GetAll courier deliveries", response= Iterable.class)
	@GetMapping("/getDeliveries/{courierId}")
	public List<Delivery> getCourierDeliveries(@PathVariable(value = "courierId") Long courierId) {
		Courier currentCourier = courierDao.findOne(courierId);
		if(currentCourier != null)
			return currentCourier.getDelivery();
		else return null;
	}
	
	
	@ApiOperation(value="GetAll courier deliveries", response= Iterable.class)
	@GetMapping("/getDeliveries/{courierId}/toDeliver")
	public List<Delivery> getCourierDeliveriesToDeliver(@PathVariable(value = "courierId") Long courierId) {
		Courier courier = courierDao.findOne(courierId);
		List<Delivery> deliveriesType1 = deliveryDao.findByCourierAndType(courier, 1);//Deliveries assigned to couriers for delivering.
		deliveriesType1.addAll(deliveryDao.findByCourierAndType(courier, 2));//Deliveries assigned to couriers for delivering.
		if(deliveriesType1 != null)
			return deliveriesType1;
		else return null;
	}
	
	@ApiOperation(value="GetAll courier deliveries", response= Iterable.class)
	@GetMapping("/getDeliveries/{courierId}/delivered")
	public List<Delivery> getCourierDeliveredDeliveries(@PathVariable(value = "courierId") Long courierId) {
		Courier courier = courierDao.findOne(courierId);
		List<Delivery> deliveriesType1 = deliveryDao.findByCourierAndType(courier, 3);//Deliveries assigned to couriers for delivering.
		if(deliveriesType1 != null)
			return deliveriesType1;
		else return null;
	}
}
