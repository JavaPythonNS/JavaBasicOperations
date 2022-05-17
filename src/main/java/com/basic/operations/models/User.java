package com.basic.operations.models;


import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "user")
public class User {

	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
	
	public User() {
		
	}
	
	public User(String id, String email, String password, Role role) {
		this.id = id;
		this.email = email;
		this.password = PASSWORD_ENCODER.encode(password);
		this.role = role;
	}
	
	@Id
	private String id;
	
	@Email(message = "Email should be valid")
	private String email;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String phoneNo;
	
	private String image;
	
	private String deviceId;
	
	private String deviceType;
	
	private String stripeAccountId;
	
	private String stripeAccountVerifiedStatus;
	
	private String stripeVerificationError;
	
	private String stripeCustomerId;
    
	private String stripeBankAccountId;
    
	private double latitude;
	
	private double longitude;
	
	private double previousLatitude;
	
	private double previousLongitude;
	
	private int status;
	
	private String secretKey;
	
	private String socialId;
	
	private String loginType;
	
	private int sendReserveNotification;
	
	private int sendSweepingNotification;
	
	private Date actionPerformed;
	
	private String bankName;
	
	private String currency;
	
	private String licensePlateNumber;
	
	@Column(name = "lastNotificationSentTimeForCurb", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastNotificationSentTimeForCurb;
	
	@Column(name = "lastNotificationSentTimeForSweeping", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastNotificationSentTimeForSweeping;
	
	@Column(name = "lastLatLongUpdationTime", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLatLongUpdationTime;
	
	/*@ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JoinColumn(name = "vehicletype")
	private VehicleType vehicletype;*/
	 
	@NotNull(message="Role is required.")
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(name = "role")
	private Role role;
	
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(name = "currentRole")
	private Role currentRole;
	

	
	@Transient
	private int noOfAddedParkings;
	
	@Transient
	private double rating;
	
	/*@JsonIgnore
	@OneToMany(mappedBy = "sender", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<ChatMessages> senderChatMessagesList = new HashSet<ChatMessages>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "receiver", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<ChatMessages> receiverChatMessagesList = new HashSet<ChatMessages>();
	
	@JsonIgnore
	@OneToMany(mappedBy="user",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
	private Set<ContactUs> contactUs = new HashSet<ContactUs>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "sender", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<Notifications> senderNotificationsList = new HashSet<Notifications>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "receiver", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<Notifications> receiverNotificationsList = new HashSet<Notifications>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<NotificationSetting> notificationSettingList = new HashSet<NotificationSetting>();*/
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}
	
	@JsonProperty
	public void setPassword(String password) {
		this.password=PASSWORD_ENCODER.encode(password);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getStripeAccountId() {
		return stripeAccountId;
	}

	public void setStripeAccountId(String stripeAccountId) {
		this.stripeAccountId = stripeAccountId;
	}
	
	public String getStripeAccountVerifiedStatus() {
		return stripeAccountVerifiedStatus;
	}

	public void setStripeAccountVerifiedStatus(String stripeAccountVerifiedStatus) {
		this.stripeAccountVerifiedStatus = stripeAccountVerifiedStatus;
	}

	public String getStripeVerificationError() {
		return stripeVerificationError;
	}

	public void setStripeVerificationError(String stripeVerificationError) {
		this.stripeVerificationError = stripeVerificationError;
	}

	public String getStripeCustomerId() {
		return stripeCustomerId;
	}

	public void setStripeCustomerId(String stripeCustomerId) {
		this.stripeCustomerId = stripeCustomerId;
	}

	public String getStripeBankAccountId() {
		return stripeBankAccountId;
	}

	public void setStripeBankAccountId(String stripeBankAccountId) {
		this.stripeBankAccountId = stripeBankAccountId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getPreviousLatitude() {
		return previousLatitude;
	}

	public void setPreviousLatitude(double previousLatitude) {
		this.previousLatitude = previousLatitude;
	}

	public double getPreviousLongitude() {
		return previousLongitude;
	}

	public void setPreviousLongitude(double previousLongitude) {
		this.previousLongitude = previousLongitude;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public int getSendReserveNotification() {
		return sendReserveNotification;
	}

	public void setSendReserveNotification(int sendReserveNotification) {
		this.sendReserveNotification = sendReserveNotification;
	}

	public int getSendSweepingNotification() {
		return sendSweepingNotification;
	}

	public void setSendSweepingNotification(int sendSweepingNotification) {
		this.sendSweepingNotification = sendSweepingNotification;
	}

	public Date getActionPerformed() {
		return actionPerformed;
	}

	public void setActionPerformed(Date actionPerformed) {
		this.actionPerformed = actionPerformed;
	}
	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

	public Date getLastNotificationSentTimeForCurb() {
		return lastNotificationSentTimeForCurb;
	}

	public void setLastNotificationSentTimeForCurb(Date lastNotificationSentTimeForCurb) {
		this.lastNotificationSentTimeForCurb = lastNotificationSentTimeForCurb;
	}

	public Date getLastNotificationSentTimeForSweeping() {
		return lastNotificationSentTimeForSweeping;
	}

	public void setLastNotificationSentTimeForSweeping(Date lastNotificationSentTimeForSweeping) {
		this.lastNotificationSentTimeForSweeping = lastNotificationSentTimeForSweeping;
	}

	public Date getLastLatLongUpdationTime() {
		return lastLatLongUpdationTime;
	}

	public void setLastLatLongUpdationTime(Date lastLatLongUpdationTime) {
		this.lastLatLongUpdationTime = lastLatLongUpdationTime;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public Role getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(Role currentRole) {
		this.currentRole = currentRole;
	}

	public int getNoOfAddedParkings() {
		return noOfAddedParkings;
	}

	public void setNoOfAddedParkings(int noOfAddedParkings) {
		this.noOfAddedParkings = noOfAddedParkings;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
	
}
	
