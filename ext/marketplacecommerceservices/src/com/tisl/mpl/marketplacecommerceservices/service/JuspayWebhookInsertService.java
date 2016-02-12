/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.text.ParseException;


/**
 * @author TCS
 *
 */
public interface JuspayWebhookInsertService
{

	/**
	 * This method is used to insert data in Webhook table when Juspay will post data to Commerce everytime there will a
	 * Payment/Refund Successful
	 *
	 * @param webhookData
	 */
	public void insertWekhookData(final String webhookData) throws ParseException;

}

