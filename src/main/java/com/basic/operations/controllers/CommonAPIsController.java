package com.basic.operations.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.basic.operations.models.User;
import com.basic.operations.services.IRoleService;
import com.basic.operations.services.IUserServices;
import com.basic.operations.util.Commons;
import com.basic.operations.util.MailUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/")
public class CommonAPIsController {

	@Autowired
	IUserServices userServices;

	@Autowired
	IRoleService roleServices;

	@Autowired
	private MessageSource messageSource;

	public String getMessage(String code, String locale) {
		return messageSource.getMessage(code, null, new Locale(locale));
	}

	@RequestMapping(value = "signUpUser", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> signUpOwner(@RequestBody @Valid User objUser, BindingResult result,
			HttpServletRequest request) throws JsonProcessingException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			if (result.hasErrors()) {
				Map<String, Object> errorBean = new HashMap<String, Object>();
				errorBean.put("result", "Fail");
				errorBean.put("message", result.getFieldError());
				errorBean.put("status", "400");
				return new ResponseEntity<>(errorBean, HttpStatus.BAD_REQUEST);
			} else {
				User users1 = userServices.checkEmail(objUser.getEmail());

				if (users1 == null) {

					String userId = UUID.randomUUID().toString().toUpperCase();
					objUser.setId(userId);
					objUser.setStatus(1);
					objUser.setSendReserveNotification(1);
					objUser.setSendSweepingNotification(1);
					objUser.setCurrentRole(objUser.getRole());
					User objUser2 = userServices.addUser(objUser);

					if (objUser2 != null) {
						objMap.put("data", objUser2);
						objMap.put("status", "OK");
						objMap.put("code", "200");
						objMap.put("message", "Signed Up Successfully.");
						return new ResponseEntity<>(objMap, HttpStatus.OK);
					} else {
						objMap.put("message", "Somthing went wrong.");
						objMap.put("status", "Fail");
						objMap.put("code", "500");
						objMap.put("data", "");
						return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				} else {
					objMap.put("message", "Email already registered with us.");
					objMap.put("status", "Fail");
					objMap.put("code", "406");
					objMap.put("data", "");
					return new ResponseEntity<>(objMap, HttpStatus.NOT_ACCEPTABLE);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			objMap.put("message", "Somthing went wrong.");
			objMap.put("status", "Fail");
			objMap.put("code", "500");
			objMap.put("data", "");
			return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "{id}/createProfile")
	public ResponseEntity<Map<String, Object>> createProfile(@PathVariable("id") String id,
			@RequestPart(value = "user") String user, @RequestPart(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) throws JsonProcessingException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			User objUsers = userServices.findById(id);
			if (objUsers != null) {
				User objUser2 = new ObjectMapper().readValue(user, User.class);
				User temp = null;
				if (objUsers.getEmail() == null) {
					temp = userServices.checkEmail(objUser2.getEmail());
				}
				if(temp == null) {
					objUsers.setFirstName(objUser2.getFirstName());
					objUsers.setLastName(objUser2.getLastName());
					objUsers.setPhoneNo(objUser2.getPhoneNo());
					objUsers.setEmail(objUser2.getEmail());
					objUsers.setLicensePlateNumber(objUser2.getLicensePlateNumber());
					if (file != null) {
						String dir = "SapeApp/resources/userImages/" + objUsers.getId() + "/";
	
						if (!new File(dir).exists()) {
							new File(dir).mkdirs();
						} else {
							String[] myFiles = new File(dir).list();
							for (int i = 0; i < myFiles.length; i++) {
								File myFile = new File(new File(dir), myFiles[i]);
								myFile.delete();
							}
						}
	
						String fileName = file.getOriginalFilename();
						String ext = FilenameUtils.getExtension(fileName);
						fileName = Commons.getFileName() + "." + ext;
						InputStream fileContent = file.getInputStream();
						OutputStream outputStream = new FileOutputStream(new File(dir + "/" + fileName));
						int read = 0;
						byte[] bytes = new byte[1024];
						while ((read = fileContent.read(bytes)) != -1) {
							outputStream.write(bytes, 0, read);
						}
						String n = "userImages/" + objUsers.getId() + "/";
						objUsers.setImage(n + fileName);
						outputStream.close();
						fileContent.close();
					}
	
					User objUser3 = userServices.addUser(objUsers);
	
					if (objUser3 != null) {
						objMap.put("data", objUser3);
						objMap.put("status", "OK");
						objMap.put("code", "200");
						objMap.put("message", "Signed Up Successfully.");
						return new ResponseEntity<>(objMap, HttpStatus.OK);
					} else {
						objMap.put("message", "Something went wrong.");
						objMap.put("status", "Fail");
						objMap.put("code", "500");
						objMap.put("data", "");
						return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}else {
					objMap.put("message", "Email already registered with us.");
					objMap.put("status", "Fail");
					objMap.put("code", "406");
					objMap.put("data", "");
					return new ResponseEntity<>(objMap, HttpStatus.NOT_ACCEPTABLE);
				}

			} else {
				objMap.put("message", "User not found");
				objMap.put("status", "Fail");
				objMap.put("code", "401");
				objMap.put("data", "");
				return new ResponseEntity<>(objMap, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			objMap.put("message", "Something went wrong.");
			objMap.put("status", "Fail");
			objMap.put("code", "500");
			objMap.put("data", "");
			return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "{id}/getUserProfile")
	public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable("id") String id, HttpServletRequest request)
			throws JsonProcessingException {

		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			User objUsers = userServices.findById(id);
			if (objUsers != null) {
				objMap.put("data", objUsers);
				objMap.put("status", "OK");
				objMap.put("code", "200");
				objMap.put("message", "Successfully Received.");
				return new ResponseEntity<>(objMap, HttpStatus.OK);

			} else {
				objMap.put("message", "User not found");
				objMap.put("status", "Fail");
				objMap.put("code", "401");
				objMap.put("data", "");
				return new ResponseEntity<>(objMap, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			objMap.put("message", "Something went wrong.");
			objMap.put("status", "Fail");
			objMap.put("code", "500");
			objMap.put("data", "");
			return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "{id}/editProfile")
	public ResponseEntity<Map<String, Object>> editProfile(@PathVariable("id") String id,
			@RequestPart(value = "user") String user, @RequestPart(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) throws JsonProcessingException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			User objUsers = userServices.findById(id);
			if (objUsers != null) {

				// getActionPerformed(objUsers);
				User objUser2 = new ObjectMapper().readValue(user, User.class);
				objUsers.setFirstName(objUser2.getFirstName());
				objUsers.setLastName(objUser2.getLastName());
				objUsers.setPhoneNo(objUser2.getPhoneNo());
				objUsers.setLicensePlateNumber(objUser2.getLicensePlateNumber());

				if (file != null) {
					String dir = "BasicOperations/resources/userImages/" + objUsers.getId() + "/";

					if (!new File(dir).exists()) {
						new File(dir).mkdirs();
					}

					String fileName = file.getOriginalFilename();
					String ext = FilenameUtils.getExtension(fileName);
					fileName = Commons.getFileName() + "." + ext;
					InputStream fileContent = file.getInputStream();
					OutputStream outputStream = new FileOutputStream(new File(dir + "/" + fileName));
					int read = 0;
					byte[] bytes = new byte[1024];
					while ((read = fileContent.read(bytes)) != -1) {
						outputStream.write(bytes, 0, read);
					}
					String n = "userImages/" + objUsers.getId() + "/";
					objUsers.setImage(n + fileName);
					outputStream.close();
					fileContent.close();
				}

				User objUser3 = userServices.addUser(objUsers);
				if (objUser3 != null) {
					objMap.put("data", objUser3);
					objMap.put("status", "OK");
					objMap.put("code", "200");
					objMap.put("message", "Successfully Updated.");
					return new ResponseEntity<>(objMap, HttpStatus.OK);
				} else {
					objMap.put("message", "Something went wrong.");
					objMap.put("status", "Fail");
					objMap.put("code", "500");
					objMap.put("data", "");
					return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
				}

			} else {
				objMap.put("message", "User not found");
				objMap.put("status", "Fail");
				objMap.put("code", "401");
				objMap.put("data", "");
				return new ResponseEntity<>(objMap, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			objMap.put("message", "Something went wrong.");
			objMap.put("status", "Fail");
			objMap.put("code", "500");
			objMap.put("data", "");
			return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "socialLogin", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> socialLogin(@RequestBody @Valid User objUser, BindingResult result,
			HttpServletRequest request) throws JsonProcessingException {

		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			User objUser2 = userServices.checkSocialId(objUser.getSocialId());
			if (objUser2 == null) {

				String userId = UUID.randomUUID().toString().toUpperCase();
				objUser.setId(userId);
				objUser.setStatus(1);
				objUser.setSendReserveNotification(1);
				objUser.setSendSweepingNotification(1);
				objUser.setCurrentRole(objUser.getRole());
				
				User objUser3 = userServices.addUser(objUser);
				if (objUser3 != null) {
					objUser3.setNoOfAddedParkings(0);
					objMap.put("data", objUser3);
					objMap.put("status", "OK");
					objMap.put("code", "200");
					objMap.put("message", "Signed Up Successfully.");
					return new ResponseEntity<>(objMap, HttpStatus.OK);
				} else {
					objMap.put("message", "Somthing went wrong.");
					objMap.put("status", "Fail");
					objMap.put("code", "500");
					objMap.put("data", "");
					return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				if (objUser2.getStatus() == 1) {
					objUser2.setDeviceId(objUser.getDeviceId());
					objUser2.setDeviceType(objUser.getDeviceType());
					objUser2.setCurrentRole(userServices.getRoleById(objUser.getRole().getId()));
					User objUser3 = userServices.addUser(objUser2);
					if (objUser3 != null) {
						objMap.put("data", objUser3);
						objMap.put("status", "OK");
						objMap.put("code", "200");
						objMap.put("message", "Login Successfully.");
						return new ResponseEntity<>(objMap, HttpStatus.OK);
					} else {
						objMap.put("message", "Somthing went wrong.");
						objMap.put("status", "Fail");
						objMap.put("code", "500");
						objMap.put("data", "");
						return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				} else {
					objMap.put("message", "Your account has been disabled temporarly.");
					objMap.put("status", "Fail");
					objMap.put("code", "406");
					objMap.put("data", "");
					return new ResponseEntity<>(objMap, HttpStatus.NOT_ACCEPTABLE);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			objMap.put("message", "Somthing went wrong.");
			objMap.put("status", "Fail");
			objMap.put("code", "500");
			objMap.put("data", "");
			return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("loginApp")
	public ResponseEntity<Map<String, Object>> loginApp(@RequestBody String jsonStr, HttpServletRequest request)
			throws JsonProcessingException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			User objUser = new User();
			JSONObject jsonObject = new JSONObject(jsonStr);
			int role = jsonObject.getInt("role");
			if (jsonObject.has("email")) {
				objUser = userServices.checkEmail(jsonObject.getString("email"));
			}
			if (objUser != null) {
				// getActionPerformed(users);
				boolean password = User.PASSWORD_ENCODER.matches(jsonObject.getString("password"), objUser.getPassword());
				if (password) {
					if (objUser.getStatus() == 1) {

						if ((objUser.getRole().getId() == 2 || objUser.getRole().getId() == 3
								|| objUser.getRole().getId() == 4)
								&& (objUser.getRole().getId() == role || objUser.getRole().getId() == 4)) {
							
							if(jsonObject.has("deviceType") && jsonObject.get("deviceType") != null) {
								objUser.setDeviceType(jsonObject.getString("deviceType"));
							}
							if(jsonObject.has("deviceId") && jsonObject.get("deviceId") != null) {
								objUser.setDeviceId(jsonObject.getString("deviceId"));
							}
							objUser.setCurrentRole(userServices.getRoleById(role));
							userServices.addUser(objUser);
							
							objMap.put("data", objUser);
							objMap.put("status", "OK");
							objMap.put("code", "200");
							objMap.put("message", "Login Successfully.");
							return new ResponseEntity<>(objMap, HttpStatus.OK);
						} else {
							objMap.put("message", "Seems like you login with wrong credentails.");
							objMap.put("status", "Fail");
							objMap.put("code", "406");
							objMap.put("data", "");
							return new ResponseEntity<>(objMap, HttpStatus.NOT_ACCEPTABLE);
						}

					} else {
						objMap.put("message", "Your account has been temporarily disabled.");
						objMap.put("status", "Fail");
						objMap.put("code", "406");
						objMap.put("data", "");
						return new ResponseEntity<>(objMap, HttpStatus.NOT_ACCEPTABLE);
					}
				} else {
					objMap.put("message", "Password entered by you is invalid.");
					objMap.put("status", "Fail");
					objMap.put("code", "406");
					objMap.put("data", "");
					return new ResponseEntity<>(objMap, HttpStatus.NOT_ACCEPTABLE);
				}
			} else {
				objMap.put("message", "Email entered by you is invalid.");
				objMap.put("status", "Fail");
				objMap.put("code", "406");
				objMap.put("data", "");
				return new ResponseEntity<>(objMap, HttpStatus.NOT_ACCEPTABLE);
			}

		} catch (Exception e) {
			e.printStackTrace();
			objMap.put("message", "Somthing went wrong.");
			objMap.put("status", "Fail");
			objMap.put("code", "500");
			objMap.put("data", "");
			return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("forgetPassword")
	public ResponseEntity<Map<String, Object>> forgetPassword(@RequestBody String jsonStr, HttpServletRequest request)
			throws JsonProcessingException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			String email = jsonObject.getString("email");
			User objUser = userServices.checkEmail(email);
			if (objUser != null) {
				String password = Commons.generateCodeForPassword();
				objUser.setPassword(password);
				userServices.updateUser(objUser);
				
				new MailUtility().emailForForgetPassword(objUser,password);
				objMap.put("message", "An email sent at given address. Please check your mail to reset your password.");
				objMap.put("status", "OK");
				objMap.put("code", "200");
				objMap.put("data", "");
				return new ResponseEntity<>(objMap, HttpStatus.OK);
			} else {
				objMap.put("message", "Email address is not registered with us.");
				objMap.put("status", "Fail");
				objMap.put("code", "401");
				objMap.put("data", "");
				return new ResponseEntity<>(objMap, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
			objMap.put("message", "Somthing went wrong.");
			objMap.put("status", "Fail");
			objMap.put("code", "500");
			objMap.put("data", "");
			return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("resetPassword")
	public ResponseEntity<Map<String, Object>> verifyAppAccount(@RequestBody String objString)
			throws JsonProcessingException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(objString);
			String email = jsonObject.getString("email");
			// String id = jsonObject.getString("id");
			User objUsers = userServices.checkEmail(email);

			if (objUsers != null) {
				objMap.put("message", "Request Received for password change.");
				objMap.put("status", "OK");
				objMap.put("code", "200");
				objMap.put("data", objUsers);
				return new ResponseEntity<>(objMap, HttpStatus.OK);
			} else {
				objMap.put("message", "You are not autherized.");
				objMap.put("status", "Fail");
				objMap.put("code", "401");
				objMap.put("data", "");
				return new ResponseEntity<>(objMap, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			objMap.put("message", "Somthing went wrong.");
			objMap.put("status", "Fail");
			objMap.put("code", "500");
			objMap.put("data", "");
			return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "{id}/changePassword")
	public ResponseEntity<Map<String, Object>> changePassword(@PathVariable("id") String id,
			@RequestBody String objString, HttpServletRequest request) throws JsonProcessingException {

		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			User objUsers = userServices.findById(id);
			if (objUsers != null) {
				// getActionPerformed(objUsers);

				JSONObject jsonObject = new JSONObject(objString);
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				boolean isMatched = passwordEncoder.matches(jsonObject.getString("currentPassword"),
						objUsers.getPassword());
				if (isMatched) {
					String newPassword = jsonObject.getString("newPassword");
					objUsers.setPassword(newPassword);
					User users = userServices.addUser(objUsers);
					if (users != null) {
						objMap.put("message", "Password changed successfully");
						objMap.put("status", "OK");
						objMap.put("code", "200");
						objMap.put("data", objUsers);
						return new ResponseEntity<>(objMap, HttpStatus.OK);
					} else {
						objMap.put("message", "Something went wrong. Please try after some time.");
						objMap.put("status", "Fail");
						objMap.put("code", "500");
						objMap.put("data", "");
						return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				} else {
					objMap.put("message",
							"Your current password is incorrect. Please try again with correct password.");
					objMap.put("status", "Fail");
					objMap.put("code", "423");
					objMap.put("data", "");
					return new ResponseEntity<>(objMap, HttpStatus.LOCKED);
				}

			} else {
				objMap.put("message", "User not found");
				objMap.put("status", "Fail");
				objMap.put("code", "406");
				objMap.put("data", "");
				return new ResponseEntity<>(objMap, HttpStatus.NOT_ACCEPTABLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			objMap.put("message", "Somthing went wrong.");
			objMap.put("status", "Fail");
			objMap.put("code", "500");
			objMap.put("data", "");
			return new ResponseEntity<>(objMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
