/**
 *
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;

import java.io.InputStream;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.ordersync.OrderSyncUtility;


/**
 * @author TCS
 *
 */
/**
 * Main Controller for OrderSync via PI
 *
 *
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/")
public class OrderSyncController
{
	private static final Logger LOG = Logger.getLogger(OrderSyncController.class);
	@Resource(name = "mplOrderSyncUtility")
	private OrderSyncUtility mplOrderSyncUtility;

	@RequestMapping(value = "/orderStatusSync", method = RequestMethod.POST)
	//@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public void orderSync(final InputStream orderSyncXML) throws RequestParameterException, JAXBException
	{

		try
		{
			LOG.debug("Starting Sync");
			mplOrderSyncUtility.syncOrder(orderSyncXML);
		}

		catch (final Exception e)
		{
			LOG.error("Error in Order Sync");
		}
	}

}
