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
public interface GenerateOTPDao
{
	/**
	 *
	 * @param customerPK
	 * @param OTPType
	 * @return List<OTPModel>
	 */
	List<OTPModel> fetchOTP(final String customerPK, final OTPTypeEnum OTPType);
}
