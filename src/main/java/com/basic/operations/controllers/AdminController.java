package com.basic.operations.controllers;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.basic.operations.models.AppVersion;
import com.basic.operations.models.User;
import com.basic.operations.services.IUserServices;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
@RequestMapping("/admin/")
public class AdminController {

	@Autowired
	IUserServices userServices;

	@PostMapping("checkAuthentication")
	public ResponseEntity<Map<String, Object>> checkAuthentication(@RequestBody String objStr,
			HttpServletRequest request) throws JsonProcessingException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(objStr);
			User user = userServices.findByEmail(jsonObject.getString("email"));
			if (User.PASSWORD_ENCODER.matches(jsonObject.getString("password"), user.getPassword())) {
				objMap.put("message", "Admin login successfully.");
				objMap.put("status", "ok");
				objMap.put("code", "200");
				objMap.put("data", user);
				return new ResponseEntity<>(objMap, HttpStatus.OK);
			} else {
				objMap.put("message", "Email or password incorrect");
				objMap.put("status", "fail");
				objMap.put("code", "401");
				objMap.put("data", "");
				return new ResponseEntity<>(objMap, HttpStatus.OK);
			}
		} catch (Exception e) {
			objMap.put("message", "Something went wrong");
			objMap.put("status", "fail");
			objMap.put("code", "500");
			objMap.put("data", "");
			return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("logoutAdmin")
	public ResponseEntity<Map<String, Object>> logoutAdmin(HttpServletRequest request)
			throws JsonProcessingException, ParseException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			HttpSession httpSession = request.getSession();
			httpSession.removeAttribute("adminId");
			httpSession.invalidate();
			objMap.put("status", "ok");
			objMap.put("code", "200");
			objMap.put("data", "");
			objMap.put("message", "Admin logout successfull.");
			return new ResponseEntity<>(objMap, HttpStatus.OK);
		} catch (Exception e) {
			objMap.put("message", "Something went wrong");
			objMap.put("status", "fail");
			objMap.put("code", "500");
			objMap.put("data", "");
			return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("getLoggedInUserPassword")
	public ResponseEntity<Map<String, Object>> getLoggedInUserPassword(@RequestBody String objStr,
			HttpServletRequest request) throws JsonProcessingException, ParseException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(objStr);
			User users = userServices.getUserById(jsonObject.getString("id"));
			if (users != null) {
				objMap.put("message", "Password changed successfully.");
				objMap.put("password", users.getPassword());
				objMap.put("status", "Ok");
				objMap.put("code", "200");
				return new ResponseEntity<>(objMap, HttpStatus.OK);
			} else {
				objMap.put("message", "No account found.");
				objMap.put("status", "fail");
				objMap.put("code", "401");
				return new ResponseEntity<>(objMap, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			objMap.put("message", e.getMessage());
			objMap.put("status", "fail");
			objMap.put("code", "500");
			return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("changePassword")
	public ResponseEntity<Map<String, Object>> changePassword(@RequestBody String objStr, HttpServletRequest request)
			throws JsonProcessingException, ParseException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(objStr);
			User users = userServices.getUserById(jsonObject.getString("id"));
			if (users != null) {
				String password = jsonObject.getString("password");
				users.setPassword(password);
				userServices.save(users);
				objMap.put("message", "Password changed successfully.");
				objMap.put("status", "ok");
				objMap.put("code", "200");
				objMap.put("data", users);
				return new ResponseEntity<>(objMap, HttpStatus.OK);
			} else {
				objMap.put("message", "No account found.");
				objMap.put("status", "ok");
				objMap.put("code", "401");
				objMap.put("data", "");
				return new ResponseEntity<>(objMap, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			objMap.put("message", "Something went wrong");
			objMap.put("status", "fail");
			objMap.put("code", "500");
			objMap.put("data", "");
			return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("appVersionList")
	public ResponseEntity<Map<String, Object>> appVersionList() throws JsonProcessingException, ParseException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			List<AppVersion> list = userServices.getAppVersionList();
			objMap.put("list", list);
			objMap.put("status", "ok");
			objMap.put("code", "200");
			return new ResponseEntity<>(objMap, HttpStatus.OK);
		} catch (Exception e) {
			objMap.put("message", "Something went wrong");
			objMap.put("status", "fail");
			objMap.put("code", "500");
			objMap.put("data", "");
			return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("updateVersion")
	public ResponseEntity<Map<String, Object>> updateVersion(@RequestBody AppVersion objAppVersions,
			HttpServletRequest request) throws JsonProcessingException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			AppVersion objAppVersions2 = userServices.addAppVersions(objAppVersions);
			if (objAppVersions2 != null) {
				objMap.put("obj", objAppVersions2);
				objMap.put("status", "ok");
				objMap.put("code", "200");
				return new ResponseEntity<>(objMap, HttpStatus.OK);
			} else {
				objMap.put("message", "Something went wrong.");
				objMap.put("status", "fail");
				objMap.put("code", "500");
				return new ResponseEntity<>(objMap, HttpStatus.OK);
			}
		} catch (Exception e) {
			objMap.put("message", e.getMessage());
			objMap.put("status", "fail");
			objMap.put("code", "500");
			return new ResponseEntity<>(objMap, HttpStatus.OK);
		}
	}

	@GetMapping(value = "getUsersList")
	public ResponseEntity<Map<String, Object>> getUsersList(HttpServletRequest request) throws JsonProcessingException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			List<User> userList = userServices.getUsersList();
			objMap.put("message", "Users list fetched successfully.");
			objMap.put("status", "OK");
			objMap.put("code", "200");
			objMap.put("user", userList);
			return new ResponseEntity<>(objMap, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			objMap.put("message", "Something went wrong.");
			objMap.put("status", "Fail");
			objMap.put("code", "500");
			objMap.put("data", "");
			return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
