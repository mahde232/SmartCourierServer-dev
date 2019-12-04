package com.smartcourier.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartcourier.beans.User;

@Repository
public interface AppDao extends JpaRepository<User, Long>{
	
	public User findByUsername(String username);
	
}
