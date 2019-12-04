package com.smartcourier.beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Entity implementation class for Entity: Region
 *
 */
/**
 * @author User
 *
 */
@Entity
@Table(name="Region", 
uniqueConstraints=
@UniqueConstraint(columnNames={"id"})
)
public class Region implements Serializable {

	@Id
	@GeneratedValue 
	//DB's and algorithm's details
	private Long id;
	private String regionName;
	private Integer threshold;//Inside createDelivery method in DeliveryController class, the threshold of the region of the created delivery will be compared with the number of the deliveries in that region. f the threshold has been exceeded, then the distribution algorithm will be called on that region.
	/*

    @OneToMany(
            mappedBy = "region", 
            cascade = CascadeType.ALL, 
            orphanRemoval = true
        )
	private List<Delivery> delivery;


*/
	@ManyToMany(fetch=FetchType.EAGER, 
			cascade = { 
		    CascadeType.PERSIST, 
		    CascadeType.MERGE
		})
	@JoinTable(name = "region_courier",
		    joinColumns = @JoinColumn(name = "region_id"),
		    inverseJoinColumns = @JoinColumn(name = "courier_id")
		)
	private Set<Courier> courier = new HashSet<>();
	
	// The 'mappedBy = "region"' attribute specifies that
	// the 'private Region region;' field in delivery owns the
	// relationship (i.e. contains the foreign key for the query to
	// find all deliveries for a region.
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="region")
    //@JsonManagedReference
	private List<Delivery> delivery;
	

	/*public Set<Courier> getCourier() {
		return courier;
	}

	public void setCourier(Set<Courier> courier) {
		this.courier = courier;
	}*/

	private static final long serialVersionUID = 1L;

	public Region() {
		super();
		threshold = 10; // The default is that the distribution algorithm will run on this region if this region have more then 10 deliveries from type 0 and type 1.
	}
   
	
	public Set<Courier> getCourier() {
		return courier;
	}

	public void setCourier(Set<Courier> courier) {
		this.courier = courier;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	public List<Delivery> getDelivery() {
		return delivery;
	}

	public void setDelivery(List<Delivery> delivery) {
		this.delivery = delivery;
	}

	
	
	/*public List<Delivery> getDelivery() {
		return delivery;
	}

	public void setDelivery(List<Delivery> delivery) {
		this.delivery = delivery;
	}*/
	
}
