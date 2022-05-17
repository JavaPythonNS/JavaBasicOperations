package com.basic.operations.util;

import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
public class Commons {
	
	public static String generateCodeForPassword(){
		int length = 8;
	    boolean useLetters = true;
	    boolean useNumbers = true;
	    String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
	    return generatedString;
	}
	
	//Genrate filename 
	public static String getFileName(){
			String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
			int RANDOM_STRING_LENGTH = 10;
			StringBuffer randStr = new StringBuffer();
			for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
				int number;
				int randomInt = 0;
				Random randomGenerator = new Random();
				randomInt = randomGenerator.nextInt(52);
				if (randomInt - 1 == -1) {
					number = randomInt;
				} else {
					number = randomInt - 1;
				}
				char ch = CHAR_LIST.charAt(number);
				randStr.append(ch);
			}
			return randStr.toString();
	 }
	    
}
