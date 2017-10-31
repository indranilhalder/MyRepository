/**
 *
 */
package com.tisl.mpl.service;


import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.webform.model.WebFormTicketProcessModel;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author TCS
 *
 */



public class ClientIntegrationImpl implements ClientIntegration
{
	private static final Logger LOG = Logger.getLogger(TicketCreationCRMserviceImpl.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Autowired
	private BusinessProcessService businessProcessService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.service.ClientIntegration#sendWebFormTicket()
	 */
	@Override
	public String sendWebFormTicket()
	{

		try
		{
			final WebFormTicketProcessModel webFormTicketProcessModel = (WebFormTicketProcessModel) businessProcessService
					.createProcess("WebFormTicket-process-" + System.currentTimeMillis(), "WebFormTicket-process");
			//webFormTicketProcessModel.setMplWebCrmTicket(null);// to-do
			businessProcessService.startProcess(webFormTicketProcessModel);
			//LOG.error("CustomOmsShipmentSyncAdapter: in the CustomOmsShipmentSyncAdapter.startAutomaticRefundProcess() for Order #"
			//		+ orderModel.getCode());
		}
		catch (final Exception e)
		{
			System.out.println("========================" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.service.ClientIntegration#checkDuplicateWebFormTicket(java.lang.String)
	 */
	@Override
	public String checkDuplicateWebFormTicket(final String stringXml) throws JAXBException
	{
		// YTODO Auto-generated method stub
		return null;
	}
}
