/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.tisl.mpl.core.model.OTPModel;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.enums.OTPTypeEnum;


/**
 * @author TCS
 *
 */
public interface OTPGenericService
{

	/**
	 * Calculates the checksum using the credit card algorithm. This algorithm has the advantage that it detects any
	 * single mistyped digit and any single transposition of adjacent digits.
	 *
	 * @param num
	 *           the number to calculate the checksum for
	 * @param digits
	 *           number of significant places in the number
	 *
	 * @return the checksum of num
	 */
	public int calcChecksum(long num, int digits);

	/**
	 * This method uses the JCE to provide the HMAC-SHA-1 algorithm. HMAC computes a Hashed Message Authentication Code
	 * and in this case SHA1 is the hash algorithm used.
	 *
	 * @param keyBytes
	 *           the bytes to use for the HMAC-SHA-1 key
	 * @param text
	 *           the message or text to be authenticated.
	 *
	 * @throws NoSuchAlgorithmException
	 *            if no provider makes either HmacSHA1 or HMAC-SHA-1 digest algorithms available.
	 * @throws InvalidKeyException
	 *            The secret provided was not a valid HMAC-SHA-1 key.
	 *
	 */
	public byte[] hmacSha1(byte[] keyBytes, byte[] text) throws NoSuchAlgorithmException, InvalidKeyException;

	/**
	 * This method generates an OTP value for the given set of parameters.
	 *
	 * @param secret
	 *           the shared secret
	 * @param movingFactor
	 *           the counter, time, or other value that changes on a per use basis.
	 * @param codeDigits
	 *           the number of digits in the OTP, not including the checksum, if any.
	 * @param addChecksum
	 *           a flag that indicates if a checksum digit should be appended to the OTP.
	 * @param truncationOffset
	 *           the offset into the MAC result to begin truncation. If this value is out of the range of 0 ... 15, then
	 *           dynamic truncation will be used. Dynamic truncation is when the last 4 bits of the last byte of the MAC
	 *           are used to determine the start offset.
	 * @throws NoSuchAlgorithmException
	 *            if no provider makes either HmacSHA1 or HMAC-SHA-1 digest algorithms available.
	 * @throws InvalidKeyException
	 *            The secret provided was not a valid HMAC-SHA-1 key.
	 *
	 * @return A numeric String in base 10 that includes {@link codeDigits} digits plus the optional checksum digit if
	 *         requested.
	 */
	public String otpOutput(byte[] secret, long movingFactor, int codeDigits, boolean addChecksum, int truncationOffset)
			throws NoSuchAlgorithmException, InvalidKeyException;

	/**
	 * This method generates random number for OTP
	 *
	 * @param userId
	 *
	 * @return long
	 *
	 */
	public long randomLongNumberGenerattion(String userId);


	/**
	 * This method generates OTP
	 *
	 * @param userIdorEmail
	 * @param OTPType
	 * @return String
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 *
	 */
	public String generateOTP(String userIdorEmail, String OTPType, String mobileNumber) throws InvalidKeyException,
			NoSuchAlgorithmException;

	/**
	 * This method validates OTP
	 *
	 * @param customerPKorEmail
	 * @param enteredOTPNumber
	 * @param OTPType
	 * @param expiryTime
	 * @return boolean
	 *
	 */
	public OTPResponseData validateOTP(String customerPKorEmail, String mobileNo, String enteredOTPNumber, OTPTypeEnum OTPType,
			long expiryTime);

	/**
	 * This method returns the latest OTP against a customer
	 *
	 * @param customerID
	 * @param OTPType
	 * @return String
	 *
	 */
	public String getLatestOTP(String customerID, OTPTypeEnum OTPType);

	public OTPModel getLatestOTPModel(String customerID, OTPTypeEnum OTPType);
}
