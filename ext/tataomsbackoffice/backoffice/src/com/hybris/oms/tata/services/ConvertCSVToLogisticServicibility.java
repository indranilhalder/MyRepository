/**
 * 
 */
package com.hybris.oms.tata.services;




import java.io.File;

import org.apache.log4j.Logger;



/**
 * @author Saood
 * 
 */
public class ConvertCSVToLogisticServicibility
{
	private static final Logger LOG = Logger.getLogger(ConvertCSVToLogisticServicibility.class.getName());


	public void convertInBean(final File inputfile)
	{

		try
		{
			LOG.info("I'm from ConvertCSVToLogisticServicibility class");

		}
		catch (final Exception e)
		{
			LOG.info("***********Exception**************" + e.toString());
		}

	}
}
