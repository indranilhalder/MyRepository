/**
 *
 */
package com.tisl.mpl.facades;

import com.tisl.mpl.wsdto.MplUserResultWsDto;


/**
 * @author TCS
 *
 */
public interface MplMobileAppRegisterFacade
{

	public MplUserResultWsDto mobileKeyRegistration(final String originalUid, final String platform, final String deviceKey,
			final String isActive);


}
