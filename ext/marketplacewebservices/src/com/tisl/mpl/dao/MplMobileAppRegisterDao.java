/**
 *
 */
package com.tisl.mpl.dao;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.wsdto.MplUserResultWsDto;


/**
 * @author TCS
 *
 */
public interface MplMobileAppRegisterDao
{

	public MplUserResultWsDto mobileKeyRegistration(final String originalUid, final String platform, final String deviceKey,
			final String isActive) throws EtailNonBusinessExceptions;


}
