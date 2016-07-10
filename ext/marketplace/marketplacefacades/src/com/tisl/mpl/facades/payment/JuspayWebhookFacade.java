/**
 *
 */
package com.tisl.mpl.facades.payment;

import java.text.ParseException;


/**
 * @author TCS
 *
 */
public interface JuspayWebhookFacade
{
	/**
	 * This method inserts webhook data
	 *
	 * @param webhookData
	 * @throws ParseException
	 */
	void insertWekhookData(final String webhookData) throws ParseException;

}
