/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.wsdto.MplUserResultWsDto;


/**
 * @author TCS
 *
 */
public interface MplMobileAppRegisterService
{


	public MplUserResultWsDto mobileKeyRegistration(final String originalUid, final String platform, final String deviceKey,
			final String isActive);

}
