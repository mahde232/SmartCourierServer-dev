package com.smartcourier.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.smartcourier.dao.CourierDao;
import com.smartcourier.dao.DeliveryDao;
import com.smartcourier.dao.RegionDao;

import ABCalgorithm.ABCalgorithm;
import ABCalgorithm.Distribution;
import ABCalgorithm.Division;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/region")
@Api(value="Region Management")
public class RegionController {

	public static final Logger logger = LoggerFactory.getLogger(RegionController.class);
	private ABCalgorithm beeColony = new ABCalgorithm();	

	@Autowired
	RegionDao regionDao;
	
	@Autowired
	CourierDao courierDao;
		
	@Autowired
	DeliveryDao deliveryDao;
	
	@GetMapping("/getAll")
	public List<Region> getAllRegion(){
		List<Region> regions = regionDao.findAll();
		//for(Region region: regions)
			//region.setCourier(null);//Not important for us.
		return regions;
	}
	
	@ApiOperation(value="Get region", response= Iterable.class)
	@GetMapping("/get/{regionId}")
	public Region getRegionById(@PathVariable(value = "regionId") Long regionId){
		Region region = regionDao.findOne(regionId);
		return region;
	}
	
	@ApiOperation(value="Create region", response= Iterable.class)
	@PutMapping("/create")
	public Region createRegion(@RequestBody Region region) {
		regionDao.save(region);
		return region;
	}

	
	@ApiOperation(value="Update region", response= Iterable.class)//Please use this to create new delivery (because every delivery have a region).
	@PutMapping("/update/{regionId}")
	public Region addDeliveryToRegion(@PathVariable(value = "regionId") Long regionId, @RequestBody Delivery delivery) throws Exception 
	{
		Region region = regionDao.findOne(regionId);
		if(region != null)
		{
			delivery.setRegion(region);
			delivery.setType(0);//Deliveries that have not yet been assigned to a courier because they have not yet been distributed by the algorithm.
			deliveryDao.save(delivery);
			//Region savedRegion = regionDao.findOne(regionId);
			if( ( region.getDelivery().size()  > region.getThreshold() ) && ( region.getCourier().size() > 0 )) //If the number of deliveries in this region is higher then the region threshold, then run the distribution algorithm.
			{
				 Set<Courier> forTesting = region.getCourier();
				 ArrayList<Delivery> deliveriesToDistributeInRegion = new ArrayList<Delivery>(deliveryDao.findByRegionAndType(region,0));
				 ArrayList<Delivery> deliveriesFromType1 = (ArrayList<Delivery>) deliveryDao.findByRegionAndType(region,1);
			     deliveriesToDistributeInRegion.addAll(deliveriesFromType1);
				 Distribution distribution = beeColony.runABCalgorithm(region, deliveriesToDistributeInRegion);
				 //Save result from ABCalgorithm to DB.
				 for(Division division: distribution.getDivisions())
				 {
					 Courier courier = division.getCourier();
					 for(Delivery deliveryToCourier: division.getDeliveries())
					 {
						 //Deliveries that have been assigned to a courier by the algorithm, but the courier didn't approved that he is willing to deliver them.
						 deliveryToCourier.setType(1);
						 deliveryDao.delete(deliveryToCourier);
						 deliveryToCourier.setCourier(courier);
						 deliveryDao.save(deliveryToCourier);
					 }
				 }
			}
			return region;
		} else{
			return null;
		}
	}
	
	@ApiOperation(value="Get region", response= Iterable.class)
	@GetMapping("/assign/{regionId}/{courierId}")
	public Region assignCourierToRegion(@PathVariable(value = "regionId") Long regionId, @PathVariable(value = "courierId") Long couriderId) {
		Region region = regionDao.findOne(regionId);
		Courier courier = courierDao.findOne(couriderId);
		if(region != null && courier != null){
			region.getCourier().add(courier);
			regionDao.save(region);
			return region;
		} else{
			return null;
		}
	}
	
	@ApiOperation(value="Get region", response= Iterable.class)
	@GetMapping("/unassign/{regionId}/{courierId}")
	public boolean unassignCourierToRegion(@PathVariable(value = "regionId") Long regionId, @PathVariable(value = "courierId") Long courierId) {
		Region region = regionDao.findOne(regionId);
		Courier courier = courierDao.findOne(courierId);
		region.getCourier().remove(courier);
		regionDao.save(region);
        return true;
	}
	
	@ApiOperation(value="Delete region", response= Iterable.class)
	@DeleteMapping("/delete/{regionId}")
	public Boolean deleteRegion(@PathVariable(value = "regionId") Long regionId) {
		//Region will be deleted only if it have 0 deliveries.
		Region currentRegion = regionDao.findOne(regionId);
		if(currentRegion != null){
			if(currentRegion.getDelivery().size() <= 0)
			{
				regionDao.delete(currentRegion);
				return true;
			}
		return false;
		} else{
			return false;
		}
	}
	
	@ApiOperation(value="Delete delivery", response= Iterable.class)
	@DeleteMapping("/delete/{regionId}/{deliveryId}")
	public Boolean deleteDeliveryInRegion(@PathVariable(value = "regionId") Long regionId, @PathVariable(value = "deliveryId") Long deliveryId) {
		//Region will be deleted only if it have 0 deliveries.
		Region currentRegion = regionDao.findOne(regionId);
		boolean isSucceeded = true;
		if(currentRegion != null)
		{
			Delivery deliveryToRemove = deliveryDao.findOne(deliveryId);
			deliveryToRemove.setCourier(null);
			deliveryToRemove.setRegion(null);
			deliveryDao.save(deliveryToRemove);//Unassigned delivery from courier and region.
			deliveryDao.delete(deliveryToRemove);//Delete delivery from DB.
		}
		return isSucceeded;
	}
	
	@ApiOperation(value="Get courier's regions", response= Iterable.class)
	@GetMapping("getRegions/{courierId}")
	public List<Region> getCourierRegions(@PathVariable(value = "courierId") Long courierId) {
		List<Region> regions = regionDao.findByCourier(courierDao.findOne(courierId));
		for(Region region: regions)
		{
			region.setCourier(null);
			region.setDelivery(null);
		}
		return regions;
	}
	
	@ApiOperation(value="Get courier's deliveries", response= Iterable.class)
	@GetMapping("getDeliveries/{regionId}/{courierId}")
	public List<Delivery> getCourierDeliveries(@PathVariable(value = "courierId") Long courierId, @PathVariable(value = "regionId") Long regionId) {
		List<Delivery> deliveries = deliveryDao.findByCourierAndRegion(courierDao.findOne(courierId) , regionDao.findOne(regionId));
		/*for(Delivery delivery: deliveries)
		{
			delivery.setCourier(null);
			delivery.setRegion(null);
		}*/
		return deliveries;
	}
	
	@ApiOperation(value="Get courier's deliveries with type 1 or 2", response= Iterable.class)
	@GetMapping("getDeliveries/{regionId}/{courierId}/toDeliver")
	public List<Delivery> getCourierDeliveriesToDeliver(@PathVariable(value = "courierId") Long courierId, @PathVariable(value = "regionId") Long regionId) {
		List<Delivery> deliveries = deliveryDao.findByCourierAndRegionAndType(courierDao.findOne(courierId) , regionDao.findOne(regionId), 1);
		deliveries.addAll(deliveryDao.findByCourierAndRegionAndType(courierDao.findOne(courierId) , regionDao.findOne(regionId), 2));
		//Change delivery to type 2 since the user request to distribute them.
		for(Delivery delivery: deliveries){
			delivery.setType(2);//Couriers approve the assigning of the delivery to him.
			deliveryDao.save(delivery);
		}
		return deliveries;
	}
}
