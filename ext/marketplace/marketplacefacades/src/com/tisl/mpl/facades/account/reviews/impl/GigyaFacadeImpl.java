/**
 *
 */
package com.tisl.mpl.facades.account.reviews.impl;

import de.hybris.platform.core.model.user.CustomerModel;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.facades.account.reviews.GigyaFacade;
import com.tisl.mpl.service.GigyaService;
import com.tisl.mpl.wsdto.GigyaWsDTO;


/**
 * @author TCS
 *
 */
public class GigyaFacadeImpl implements GigyaFacade
{
	@Autowired
	public GigyaService gigyaService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facades.account.reviews.GigyaFacade#notifyGigya(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void notifyGigya(final String siteUid, final String gigyaUid, final String fName, final String lName,
			final String eMail, final String gigyaMethod)
	{
		gigyaService.notifyGigya(siteUid, gigyaUid, fName, lName, eMail, gigyaMethod);

	}

	/**
	 * @param customerModel
	 * @param isNewUser
	 * @return List
	 */
	@Override
	public GigyaWsDTO gigyaLoginHelper(final CustomerModel customerModel, final boolean isNewUser)
	{

		return gigyaService.gigyaLoginHelperforMobile(customerModel, isNewUser);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.facades.account.reviews.GigyaFacade#ratingLogoutHelper(de.hybris.platform.core.model.user.CustomerModel
	 * )
	 */
	@Override
	public void ratingLogoutHelper(final CustomerModel customerModel)
	{
		gigyaService.ratingLogoutHelper(customerModel);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facades.account.reviews.GigyaFacade#validateSignature(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean validateSignature(final String uid, final String timestamp, final String signature)
	{
		// YTODO Auto-generated method stub
		return gigyaService.validateSignature(uid, timestamp, signature);
	}
}
