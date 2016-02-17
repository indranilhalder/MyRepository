/**
 *
 */
package com.tisl.mpl.storefront.controllers.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.payment.JuspayWebhookFacade;


/**
 * @author 768233
 *
 */
@Controller
@RequestMapping(value = "/testhook")
public class WebHookController
{

	private static final Logger LOG = Logger.getLogger(WebHookController.class);

	@Autowired
	private JuspayWebhookFacade juspayWebhookFacade;

	/**
	 * This code will be removed once webhook is successfully tested.
	 *
	 * @param webhookData
	 * @return
	 * @throws ParseException
	 */
	@Deprecated
	@RequestMapping(value = "/addp", method = RequestMethod.POST)
	@ResponseBody
	public String webhookPost(@RequestParam final String webhookData) throws ParseException
	{
		//Inserting data
		LOG.info("In webhookPost");
		String status = MarketplacecommerceservicesConstants.EMPTYSPACE;
		try
		{
			juspayWebhookFacade.insertWekhookData(webhookData);
			status = "success";
			//return "success"; Sonar fix
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.debug(" ERROR :  Webhook Saving Error: " + e);
			status = "failure";
			//return "failure"; Sonar fix
		}
		return status;
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	@ResponseBody
	public String webhookPost(final HttpServletRequest req, final HttpServletResponse res) throws ParseException, IOException
	{

		String flag = "failure";
		String webhookData = "";
		final StringBuilder out = new StringBuilder();
		final InputStream is = req.getInputStream();
		if (null != is)
		{
			final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = reader.readLine()) != null)
			{
				out.append(line);
			}
			webhookData = out.toString();
			LOG.debug("Webhook data from juspay ====> " + webhookData);
		}

		try
		{
			if (null != webhookData && !StringUtils.isEmpty(webhookData))
			{
				juspayWebhookFacade.insertWekhookData(webhookData);
				flag = "success";
			}
			else
			{
				LOG.debug("No data for webhook from juspay");
				flag = "No data for webhook from juspay";
			}
			//return flag; Sonar fix
		}
		catch (final Exception e)
		{
			LOG.error("Exception while inserting webhook data", e);
			//return flag;
		}
		return flag;
	}

}
