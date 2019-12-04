package com.smartcourier.beans;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity implementation class for Entity: Delivery
 *
 */
/**
 * @author User
 *
 */
/**
 * @author User
 *
 */
@Entity
@Table(name="Delivery"
/*uniqueConstraints=
@UniqueConstraint(columnNames={"courier_id", "monthInYear"})*/
)
/*	We distinguish between the three types of deliveries that exist in our database: 
• 	Type 0: Deliveries that have not yet been assigned to a courier because they have not yet been distributed by the algorithm.
•	Type 1: Deliveries were distributed by the algorithm to a courier, but the courier has not courier has not yet confirmed that he is willing to distribute them.
•	Type 2: deliveries were distributed by the algorithm to the courier and were confirmed by the courier that he is willing to distribute them.
•	Type 3: Deliveries that have already been delivered to the destination and therefore will no longer play a role in the algorithm.
*/
public class Delivery implements Serializable {

	@Id
	@GeneratedValue 
	//DB's and algorithm's details
	private Long id;
	//private String name;
	private Integer isUrgent;
	private Double latitude;
	private Double longitude;
    private Integer type;
    private String address;
    private String phone;
	
   
    //updates in report
    private String claimant;
    private String entrance;
    private String floor;
    private String box;
    
	/*private Integer type;//There are 4 types of deliveries: Type0, Type1, Type 2 and Type 3 as described in section 3.6.
	private String date;
	    private String duedate;

	private String pasted_on_door;
	private String reveiwer_name;
	private String subarea;
	private String address;
	private String area;

	//Delivery's details
	private String claimant;
	private String name;
	private String phone;
	private String box;
	private String duedate;
	private String date;	
	private String not_found;
	private String reveiwer_name;
	private String entrance;
	private String num_of_floor;
	private String private_house;
	private String signed;
	private String pasted_on_door;
	private String text;*/
	//Salaray's details
	//private String monthInYear; 
	//private String price;
	
    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
	private Courier Courier;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
	private Region region;*/
	
	// Specifies the Delivery table does not contain an COURIER column, but 
	// an COURIER_ID column with a foreign key. And creates a join to
	// lazily fetch the owner
	/*@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COURIER_ID")
	private Courier courier;
	*/
	// Specifies the Delivery table does not contain an owner REGION, but 
	// an REGION_ID column with a foreign key. And creates a join to
	// lazily fetch the owner
	
    @ManyToOne(fetch=FetchType.LAZY) 
    @JsonIgnore
	private Region region;
		
    @ManyToOne(fetch=FetchType.LAZY) 
    @JsonIgnore
	private Courier courier;
    
    
	private static final long serialVersionUID = 1L;

	public Delivery() {
		super();
	}   
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}   
		
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getClaimant() {
		return claimant;
	}
	public void setClaimant(String claimant) {
		this.claimant = claimant;
	}
	public String getEntrance() {
		return entrance;
	}
	public void setEntrance(String entrance) {
		this.entrance = entrance;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getBox() {
		return box;
	}
	public void setBox(String box) {
		this.box = box;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	/*public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}*/
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	public Courier getCourier() {
		return courier;
	}
	public void setCourier(Courier courier) {
		this.courier = courier;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getIsUrgent() {
		return isUrgent;
	}
	public void setIsUrgent(Integer isUrgent) {
		this.isUrgent = isUrgent;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	/*public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getMonthInYear() {
		return monthInYear;
	}
	public void setMonthInYear(String monthInYear) {
		this.monthInYear = monthInYear;
	}
	
	public String getPrice() {
		return this.price;
	}

	public void setPrice(String price) {
		this.price = price;
	}   
	*/
	
	/*public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}   


	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	


	public String getSubarea() {
		return subarea;
	}
	public void setSubarea(String subarea) {
		this.subarea = subarea;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getUrgent() {
		return urgent;
	}
	public void setUrgent(String urgent) {
		this.urgent = urgent;
	}
	public String getClaimant() {
		return claimant;
	}
	public void setClaimant(String claimant) {
		this.claimant = claimant;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBox() {
		return box;
	}
	public void setBox(String box) {
		this.box = box;
	}
	public String getDuedate() {
		return duedate;
	}
	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getNot_found() {
		return not_found;
	}
	public void setNot_found(String not_found) {
		this.not_found = not_found;
	}
	public String getReveiwer_name() {
		return reveiwer_name;
	}
	public void setReveiwer_name(String reveiwer_name) {
		this.reveiwer_name = reveiwer_name;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getEntrance() {
		return entrance;
	}
	public void setEntrance(String entrance) {
		this.entrance = entrance;
	}
	public String getNum_of_floor() {
		return num_of_floor;
	}
	public void setNum_of_floor(String num_of_floor) {
		this.num_of_floor = num_of_floor;
	}
	public String getPrivate_house() {
		return private_house;
	}
	public void setPrivate_house(String private_house) {
		this.private_house = private_house;
	}
	public String getSigned() {
		return signed;
	}
	public void setSigned(String signed) {
		this.signed = signed;
	}
	public String getPasted_on_door() {
		return pasted_on_door;
	}
	public void setPasted_on_door(String pasted_on_door) {
		this.pasted_on_door = pasted_on_door;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Courier getCourier() {
		return courier;
	}
	public void setCourier(Courier courier) {
		this.courier = courier;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}

	
	
	
	/*public Courier getCourier() {
		return courier;
	}
	public void setCourier(Courier courier) {
		this.courier = courier;
	}*/
	/*public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}*/
   
}
