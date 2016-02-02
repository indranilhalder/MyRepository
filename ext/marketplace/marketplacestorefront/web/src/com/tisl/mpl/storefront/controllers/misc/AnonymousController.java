/**
 *
 */
package com.tisl.mpl.storefront.controllers.misc;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 *
 * This class is written purely for testing purpose. Should not be merged with production code.
 *
 * @author 768233
 *
 */
@Controller
@RequestMapping(value = "/anon")
public class AnonymousController extends AbstractController
{

	private static final Logger LOG = Logger.getLogger(AnonymousController.class);

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;

	@ResponseBody
	@RequestMapping(value = "/order/{code}", method = RequestMethod.GET)
	public OrderData getOrder(@PathVariable final String code) throws UnsupportedEncodingException
	{

		OrderData orderDetails = null;
		try
		{
			LOG.debug("Got order code ======> " + code);
			orderDetails = orderFacade.getOrderDetailsForCodeWithoutUser(code);
			LOG.debug("orderDetails ====> " + orderDetails);
			return orderDetails;
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.warn("Attempted to load a order that does not exist or is not visible", e);
			return orderDetails;
		}
		catch (final IllegalArgumentException ae)
		{
			LOG.warn("Attempted to load a order that does not exist or is not visible", ae);
			return orderDetails;

		}

	}
}
