package com.basic.operations.controllers;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.basic.operations.models.User;
import com.basic.operations.services.IUserServices;

@RestController
@RequestMapping("/user/")
public class UserController {

	@Autowired
	IUserServices userServices;
	
	@Autowired
	private MessageSource messageSource;
    
    public String getMessage(String code, String locale) {
    	 return messageSource.getMessage(code, null, new Locale(locale));
	}
	
	@RequestMapping(value="add",method=RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> add(@RequestBody @Valid User user, BindingResult result){
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(user == null) {
			response.put("data", "");
			response.put("status", "Fail");
			response.put("code", "400");
			response.put("message", "Bad Request");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}else if(result.hasFieldErrors()) {
			response.put("data", "");
			response.put("status", "Fail");
			response.put("code", "400");
			response.put("message", result.getFieldError().getDefaultMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}else {
			userServices.persist(user);
			
			response.put("data", "");
			response.put("status", "OK");
			response.put("code", "200");
			response.put("message", "Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="login",method=RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> login(@RequestBody String jsonStr){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			System.out.println("login under user controller called");
			JSONObject jsonObject = new JSONObject(jsonStr);
			User user = userServices.findByEmail(jsonObject.getString("email"));
			if(user != null) {
				Boolean isCorrect = User.PASSWORD_ENCODER.matches(jsonObject.getString("password"), user.getPassword());
				if(isCorrect) {
					response.put("data", user);
					response.put("status", "OK");
					response.put("code", "200");
					response.put("message", messageSource.getMessage(
							"message.user.login", null, Locale.forLanguageTag("en-US")));
					return new ResponseEntity<>(response, HttpStatus.OK);
				}else {
					response.put("data", "");
					response.put("status", "Fail");
					response.put("code", "400");
					response.put("message", messageSource.getMessage(
							"message.user.password", null, Locale.forLanguageTag("en-US")));
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			}else {
				response.put("data", "");
				response.put("status", "Fail");
				response.put("code", "400");
				response.put("message", messageSource.getMessage(
						"message.user.email", null, Locale.forLanguageTag("en-US")));
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("result", "Error");
			response.put("status", "500");
			response.put("message", e.getMessage());
			response.put("data", "");
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="{id}/getProfile")
	public ResponseEntity<Map<String, Object>> getProfile(@PathVariable("id") String id){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			User user = userServices.findById(id);
			response.put("data", user);
			response.put("status", "OK");
			response.put("code", "200");
			response.put("message", messageSource.getMessage(
					"message.user.login", null, Locale.forLanguageTag("en-US")));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("result", "Error");
			response.put("status", "500");
			response.put("message", e.getMessage());
			response.put("data", "");
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
