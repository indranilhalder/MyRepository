/**
 *
 */
package com.tisl.mpl.facade.impl;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.facades.MplPaymentWebHookFacade;
import com.tisl.mpl.service.MplPaymentWebHookService;


/**
 * @author TCS
 *
 */
public class MplPaymentWebHookFacadeImpl implements MplPaymentWebHookFacade
{
	@Autowired
	private MplPaymentWebHookService mplPaymentWebHookService;

	/**
	 * @param webhookData
	 */
	@Override
	public void insertWekhookData(final String webhookData) throws ParseException
	{
		mplPaymentWebHookService.insertWekhookData(webhookData);
	}

}
