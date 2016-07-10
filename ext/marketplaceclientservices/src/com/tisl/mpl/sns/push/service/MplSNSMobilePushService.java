package com.tisl.mpl.sns.push.service;

import de.hybris.platform.core.model.user.CustomerModel;

import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.wsdto.PushNotificationData;


/*
 *TCS
 */


public interface MplSNSMobilePushService
{
	public void setUpNotification(final String emailId, PushNotificationData pushData);

	public CustomerModel getCustForUId(final String uid) throws ClientEtailNonBusinessExceptions;
}
