/**
 *
 */
package com.tisl.mpl.facades;

import java.text.ParseException;


/**
 * @author TCS
 *
 */
public interface MplPaymentWebHookFacade
{
	public void insertWekhookData(final String webhookData) throws ParseException;
}
