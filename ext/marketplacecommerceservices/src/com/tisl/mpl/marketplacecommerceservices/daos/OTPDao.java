/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.List;

import com.tisl.mpl.core.model.OTPModel;
import com.tisl.mpl.enums.OTPTypeEnum;


/**
 * @author TCS
 *
 */
public interface OTPDao
{
	/**
	 * This method fetches the OTP with respect to the customer
	 *
	 * @param customerPK
	 * @param OTPType
	 * @return List<OTPModel>
	 */
	List<OTPModel> fetchOTP(String customerPK, OTPTypeEnum OTPType);
	List<OTPModel> fetchOTP(String customerPK,String mobileNo, OTPTypeEnum OTPType);

}
