/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import com.tis.mpl.facade.data.TicketStatusUpdate;
import com.tisl.mpl.core.model.MplWebCrmModel;
import com.tisl.mpl.core.model.MplWebCrmTicketModel;
import com.tisl.mpl.facades.cms.data.WebFormData;
import com.tisl.mpl.marketplacecommerceservices.daos.MplWebFormDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService;
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

	//	@Resource
	//	private BusinessProcessService businessProcessService;

	@Resource
	private MplWebFormDao mplWebFormDao;

	@Resource
	private ClientIntegration clientIntegration;

	@Resource(name = "ticketCreate")
	private TicketCreationCRMservice ticketCreationService;

	@Resource
	private ConfigurationService configurationService;

	//	@Resource(name = "orderModelService")
	//	private OrderModelService orderModelService;

	//	@Resource(name = "orderConverter")
	//	private Converter<OrderModel, OrderData> orderConverter;


	private static final String SUCCESS = "success";
	private static final String FAILURE = "failure";


	//	private static final Logger LOG = Logger.getLogger(MplWebFormServiceImpl.class);

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
		return mplWebFormDao.checkDuplicateWebCRMTickets(formData);
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

			if (null != duplicateResult && duplicateResult.equalsIgnoreCase("success"))
			{
				final TicketMasterXMLData ticketMasterXMLData = populateWebformTicketData(mplWebCrmTicketModel);
				ticketCreationService.ticketCreationCRM(ticketMasterXMLData);
				sentResult = SUCCESS;
			}
		}
		else
		// Duplicate check disabled
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
		return ticketCreationService.populateWebFormData(mplWebCrmTicketModel);
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
	public boolean sendWebFormTicket(final MplWebCrmTicketModel webFormModel)
	{
		//send to PI
		return clientIntegration.sendWebFormTicket(webFormModel);
	}
}
