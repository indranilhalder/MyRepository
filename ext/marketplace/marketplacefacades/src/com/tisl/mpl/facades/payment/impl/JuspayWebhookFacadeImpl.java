/**
 *
 */
package com.tisl.mpl.facades.payment.impl;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.facades.payment.JuspayWebhookFacade;
import com.tisl.mpl.marketplacecommerceservices.service.JuspayWebhookInsertService;


/**
 * @author TCS
 *
 */
public class JuspayWebhookFacadeImpl implements JuspayWebhookFacade
{
	@Autowired
	private JuspayWebhookInsertService juspayWebhookInsertService;

	/**
	 * @param webhookData
	 */
	@Override
	public void insertWekhookData(final String webhookData) throws ParseException
	{
		getJuspayWebhookInsertService().insertWekhookData(webhookData);
	}

	/**
	 * @return the juspayWebhookInsertService
	 */
	public JuspayWebhookInsertService getJuspayWebhookInsertService()
	{
		return juspayWebhookInsertService;
	}

	/**
	 * @param juspayWebhookInsertService
	 *           the juspayWebhookInsertService to set
	 */
	public void setJuspayWebhookInsertService(final JuspayWebhookInsertService juspayWebhookInsertService)
	{
		this.juspayWebhookInsertService = juspayWebhookInsertService;
	}



}

