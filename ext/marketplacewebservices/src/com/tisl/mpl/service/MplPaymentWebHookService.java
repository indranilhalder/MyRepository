/**
 *
 */
package com.tisl.mpl.service;

import java.text.ParseException;


/**
 * @author TCS
 *
 */
public interface MplPaymentWebHookService
{
	/**
	 * This method is used to insert data in Webhook table when Juspay will post data to Commerce everytime there will a
	 * Payment/Refund Successful
	 *
	 * @param webhookData
	 */
	public void insertWekhookData(final String webhookData) throws ParseException;
}
