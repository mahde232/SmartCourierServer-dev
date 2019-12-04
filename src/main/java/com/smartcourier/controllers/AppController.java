package com.smartcourier.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcourier.beans.Courier;
import com.smartcourier.beans.User;
import com.smartcourier.dao.AppDao;
import com.smartcourier.dao.CourierDao;
import com.smartcourier.model.LoginIn;
import com.smartcourier.model.LoginOut;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/app")
@Api(value="App Management")
public class AppController {

	public static final Logger logger = LoggerFactory.getLogger(AppController.class);

	@Autowired
	AppDao appDao;

	@Autowired
	CourierDao courierDao;
	
	@ApiOperation(value="login", response= Iterable.class)
	@PutMapping("/authenticate")
	public LoginOut login(@RequestBody LoginIn loginIn) {
		String username = loginIn.getUsername().toLowerCase();
		Boolean success = true;
		LoginOut loginOut = new LoginOut();
		User user = appDao.findByUsername(username);
		if(user != null) {
			if(user.getPassword().equals(loginIn.getPassword())) {
				loginOut.setId(user.getId());
				loginOut.setUsername(username);
				loginOut.setToken(UUID.randomUUID().toString());
				//Here we should insert the UUID to HashMap, and every time the user send a request he should send the request with providing his token and we should validate that the token is exist in the HashMap before we let him do anything.
				return loginOut;
			} else{
				success = false;
			}
		} else{
			success = false;
		}
		
		if(success == false){
			loginOut.setErrorMessage("סיסמא או שם משתמש לא נכונים");
		}
		
		return loginOut;
	}
	
	@ApiOperation(value="loginCourier", response= Iterable.class)
	@PutMapping("/courier/authenticate")
	public LoginOut loginByCourier(@RequestBody LoginIn loginIn) {
		String email = loginIn.getUsername().toLowerCase();
		Boolean success = true;
		LoginOut loginOut = new LoginOut();
		//User user = appDao.findByUsername(username);
		Courier courier = courierDao.findByEmail(email);
		if(courier != null) {
			if(courier.getPassword().equals(loginIn.getPassword())) {
				loginOut.setId(courier.getId());
				loginOut.setUsername(email);
				loginOut.setToken(UUID.randomUUID().toString());
				//Here we should insert the UUID to HashMap, and every time the user send a request he should send the request with providing his token and we should validate that the token is exist in the HashMap before we let him do anything.
				return loginOut;
			} else{
				success = false;
			}
		} else{
			success = false;
		}
		
		if(success == false){
			loginOut.setErrorMessage("סיסמא או אימייל לא נכונים");
		}
		
		return loginOut;
	}
	
	@ApiOperation(value="Get user", response= Iterable.class)
	@GetMapping("/user/{username}")
	public User getUser(@PathVariable(value = "username") String username) {
		username = username.toLowerCase();
		return appDao.findByUsername(username);
	}
	
	@GetMapping("/user/getAll")
	public List<User> getAllUsers(){
		List<User> useres = appDao.findAll();
		return useres;
	}
	

	@ApiOperation(value="Create user", response= Iterable.class)//This is for sinning up a secretary user (full privilege).
	@PutMapping("/user/create")
	public User createUser(@RequestBody User user) {
		if (user != null){
			String username = user.getUsername();
			if (username != null)
				username = username.toLowerCase();
		}
		
		if(appDao.findByUsername(user.getUsername()) == null){
			return appDao.save(user);
		} else{
			return null;
		}
	}
	
	
	@ApiOperation(value="Update user", response= Iterable.class)
	@PutMapping("/user/update")//Put is used for overwriting exist resource.
	public User updateUser(@RequestBody User user) {
		if(user == null) return null;
		String username = user.getUsername();
		if (username == null) return null;
		username = username.toLowerCase();
		User currentUser = appDao.findByUsername(username);
		if(currentUser != null){
			appDao.delete(currentUser);
			return appDao.save(user);
		}else 
			return null;
	}
	
	@ApiOperation(value="Delete user", response= Iterable.class)
	@DeleteMapping("/user/delete/{username}")
	public Boolean deleteUser(@PathVariable(value = "username") String username) {
		username = username.toLowerCase();
		User user = appDao.findByUsername(username);
		if(user != null){
			appDao.delete(user);
			return true;
		} else{
			return true;
		}
	}
}









