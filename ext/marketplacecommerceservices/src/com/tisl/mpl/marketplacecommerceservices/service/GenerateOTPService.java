/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.tisl.mpl.enums.OTPTypeEnum;


/**
 * @author 682160
 * 
 */
public interface GenerateOTPService
{

	public int calcChecksum(long num, int digits);

	public byte[] hmacSha1(byte[] keyBytes, byte[] text) throws NoSuchAlgorithmException, InvalidKeyException;

	public String otpOutput(byte[] secret, long movingFactor, int codeDigits, boolean addChecksum, int truncationOffset)
			throws NoSuchAlgorithmException, InvalidKeyException;

	public long randomLongNumberGenerattion(String userId);

	public String generateOTPNumber(String userId, String OTPType, String emailId) throws InvalidKeyException,
			NoSuchAlgorithmException;

	/**
	 * @param uid
	 * @param enteredOTPNumber
	 * @param oTPType
	 * @param expiryTime
	 * @return
	 */
	public boolean validaterOTP(String uid, String enteredOTPNumber, OTPTypeEnum oTPType, long expiryTime);

	public String generateTempPassword(final String custId) throws InvalidKeyException, NoSuchAlgorithmException;



}
