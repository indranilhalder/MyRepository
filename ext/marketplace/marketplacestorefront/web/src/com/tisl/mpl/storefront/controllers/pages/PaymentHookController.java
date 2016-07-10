/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.payment.JuspayWebhookFacade;


/**
 * @author TCS
 */
@Controller
@RequestMapping(value = "/webhook")
public class PaymentHookController
{
	private static final Logger LOG = Logger.getLogger(PaymentHookController.class);

	@Autowired
	private JuspayWebhookFacade juspayWebhookFacade;

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	@ResponseBody
	public void webHookDataInsert(@RequestParam final String webhookData) throws ParseException
	{
		//Inserting data
		try
		{
			juspayWebhookFacade.insertWekhookData(webhookData);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.debug(" ERROR :  Webhook Saving Error: " + e);
		}
	}

	@RequestMapping(value = "/insertget", method = RequestMethod.GET)
	@ResponseBody
	public String webHookDataInsertGet(@RequestParam final String webhookData) throws ParseException
	{
		//Inserting data
		try
		{
			LOG.info("This is a successful message>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + webhookData);
			return "success";
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(" ERROR :  Webhook Saving Error: " + e);
			return "failure";
		}
	}
}
