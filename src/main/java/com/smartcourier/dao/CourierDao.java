package com.smartcourier.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartcourier.beans.Courier;

@Repository
public interface CourierDao extends JpaRepository<Courier, Long>{

	Courier findByEmail(String email);
	
}
