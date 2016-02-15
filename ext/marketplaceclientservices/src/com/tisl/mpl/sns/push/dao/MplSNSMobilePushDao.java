/**
 *
 */
package com.tisl.mpl.sns.push.dao;

import de.hybris.platform.core.model.user.CustomerModel;

import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface MplSNSMobilePushDao
{
	public CustomerModel getCustomerProfileDetail(final String originalUid) throws ClientEtailNonBusinessExceptions;

	public CustomerModel getCustomerProfileDetailForUid(final String uid) throws ClientEtailNonBusinessExceptions;

}
