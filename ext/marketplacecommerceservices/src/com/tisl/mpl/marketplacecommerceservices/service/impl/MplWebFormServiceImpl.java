/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tis.mpl.facade.data.TicketStatusUpdate;
import com.tisl.mpl.core.model.MplWebCrmModel;
import com.tisl.mpl.core.model.MplWebCrmTicketModel;
import com.tisl.mpl.facades.cms.data.WebFormData;
import com.tisl.mpl.marketplacecommerceservices.daos.MplWebFormDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.service.ClientIntegration;
import com.tisl.mpl.service.TicketCreationCRMservice;
import com.tisl.mpl.wsdto.TicketMasterXMLData;



/**
 * @author TCS
 */
public class MplWebFormServiceImpl implements MplWebFormService
{
	@Resource
	private ModelService modelService;

	@Resource
	private BusinessProcessService businessProcessService;

	@Resource
	private MplWebFormDao mplWebFormDao;

	@Resource
	private ClientIntegration clientIntegration;

	@Resource
	private TicketCreationCRMservice ticketCreationService;

	@Resource
	private ConfigurationService configurationService;

	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;

	@Resource(name = "orderConverter")
	private Converter<OrderModel, OrderData> orderConverter;

	@Resource(name = "webFormDataConverter")
	private Converter<WebFormData, MplWebCrmTicketModel> webFormDataConverter;

	private static final String SUCCESS = "success";
	private static final String FAILURE = "failure";


	private static final Logger LOG = Logger.getLogger(MplWebFormServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService#getWebCRMParentNodes()
	 */
	@Override
	public List<MplWebCrmModel> getWebCRMParentNodes()
	{
		// YTODO Auto-generated method stub
		return mplWebFormDao.getWebCRMParentNodes();
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService#getWebCRMByNodes(java.lang.String)
	 */
	@Override
	public List<MplWebCrmModel> getWebCRMByNodes(final String nodeParent)
	{
		return mplWebFormDao.getWebCRMByNodes(nodeParent);
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService#getWebCRMTicket(java.lang.String)
	 */
	@Override
	public MplWebCrmTicketModel getWebCRMTicket(final String commerceTicketId)
	{
		return mplWebFormDao.getWebCRMTicket(commerceTicketId);
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService#checkDuplicateWebCRMTickets(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkDuplicateWebCRMTickets(final WebFormData formData)
	{
		return mplWebFormDao.checkDuplicateWebCRMTickets(formData.getTicketType(), formData.getOrderCode(),
				formData.getSubOrderCode(), formData.getTransactionId(), formData.getL0code(), formData.getL1code(),
				formData.getL2code(), formData.getL3code(), formData.getL4code(), formData.getCustomerId());
	}

	/**
	 * This method is created to send the web form ticket to PI after duplication check ( TPR- 5989 )
	 *
	 * @param mplWebCrmTicketModel
	 * @return the success/failure message
	 * @throws Exception
	 */
	@Override
	public String sendTicketToPI(final MplWebCrmTicketModel mplWebCrmTicketModel) throws Exception
	{
		String duplicateResult = null;
		String sentResult = FAILURE;
		final String duplicateCheckEnable = configurationService.getConfiguration().getString("webform.duplicate.check", "Y");
		if (duplicateCheckEnable.equalsIgnoreCase("Y"))
		{
			duplicateResult = ticketCreationService.checkDuplicateWebFormTicket(mplWebCrmTicketModel);
		}
		if (null != duplicateResult && duplicateResult.equalsIgnoreCase("success"))
		{
			final TicketMasterXMLData ticketMasterXMLData = populateWebformTicketData(mplWebCrmTicketModel);
			ticketCreationService.ticketCreationCRM(ticketMasterXMLData);
			sentResult = SUCCESS;
		}
		return sentResult;
	}

	@Override
	public TicketMasterXMLData populateWebformTicketData(final MplWebCrmTicketModel mplWebCrmTicketModel) throws Exception
	{
		OrderEntryData orderEntry = null;
		final OrderModel subOrderModel = orderModelService.getOrder(mplWebCrmTicketModel.getSubOrderCode());//Sub order model
		final OrderData orderData = orderConverter.convert(subOrderModel); //model converted to data
		for (final OrderEntryData entry : orderData.getEntries())
		{
			if (null != entry.getTransactionId())
			{
				if (entry.getTransactionId().equalsIgnoreCase(mplWebCrmTicketModel.getTransactionId()))
				{
					orderEntry = entry;
				}
			}
		}
		final TicketMasterXMLData ticketMasterXMLData = ticketCreationService.populateWebFormData(mplWebCrmTicketModel,
				subOrderModel, orderData, orderEntry);

		return ticketMasterXMLData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService#webformTicketStatusUpdate(com.tis.mpl.facade
	 * .data.TicketStatusUpdate)
	 */
	@Override
	public boolean webformTicketStatusUpdate(final TicketStatusUpdate ticketStatusUpdate)
	{
		final MplWebCrmTicketModel crmTicket = mplWebFormDao.getWebCRMTicket(ticketStatusUpdate.getEcommRequestId());
		crmTicket.setStatus(ticketStatusUpdate.getStatus());
		crmTicket.setCrmTicketRef(ticketStatusUpdate.getCrmTicketID());
		//Save updated send by CRM
		modelService.save(crmTicket);
		return true;
	}

	@Override
	public boolean sendWebFormTicket(final WebFormData ticketData)
	{
		if (checkDuplicateWebCRMTickets(ticketData))
		{
			final MplWebCrmTicketModel webFormModel = new MplWebCrmTicketModel();
			return clientIntegration.sendWebFormTicket(webFormDataConverter.convert(ticketData, webFormModel));
		}
		else
		{
			return false;
		}
	}


}
