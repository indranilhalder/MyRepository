/**
 *
 */
package com.tisl.mpl.v2.controller;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.MplPaymentWebHookFacade;


/**
 * @author TCS
 */
@Controller
//@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 1800)
public class PaymentWebHookController
{
	private static final Logger LOG = Logger.getLogger(PaymentWebHookController.class);
	@Autowired
	private MplPaymentWebHookFacade mplPaymentWebHookFacade;

	@RequestMapping(value = "/{baseSiteId}/webHookDataInsert", method = RequestMethod.POST)
	@ResponseBody
	public void webHookDataInsert(@RequestParam final String webhookData) throws ParseException
	{
		//Inserting data
		try
		{
			mplPaymentWebHookFacade.insertWekhookData(webhookData);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.debug(" ERROR :  Webhook Saving Error: " + e);
		}
	}
}
