/**
 *
 */
package com.tisl.mpl.facades.webform;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tis.mpl.facade.data.TicketStatusUpdate;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.keygenerator.MplPrefixablePersistentKeyGenerator;
import com.tisl.mpl.core.model.MplWebCrmModel;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.cms.data.NodeFormData;
import com.tisl.mpl.facades.cms.data.WebForm;
import com.tisl.mpl.facades.cms.data.WebFormData;
import com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService;


/**
 * @author TCS
 *
 */
public class MplDefaultWebFormFacade implements MplWebFormFacade
{
	protected static final Logger LOG = Logger.getLogger(MplDefaultWebFormFacade.class);

	@Resource
	private MplWebFormService mplWebFormService;

	@Resource
	private UserService userService;
	@Resource
	private ConfigurationService configurationService;
	@Resource
	private MplOrderFacade mplOrderFacade;
	@Resource
	private MplCheckoutFacade mplCheckoutFacade;
	@Autowired
	private MplPrefixablePersistentKeyGenerator prefixableKeyGenerator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facades.webform.MplWebFormFacade#getWebCRMForm()
	 */
	@Override
	public WebForm getWebCRMForm(final PageableData pageableData)
	{
		final WebForm form = new WebForm();
		List<MplWebCrmModel> webCrmModels = new ArrayList<>();
		final List<NodeFormData> nodes = new ArrayList<>();
		CustomerModel currentUser = null;
		final List<OrderData> orderDatas = new ArrayList<>();
		try
		{
			currentUser = (CustomerModel) userService.getCurrentUser();
			if (currentUser != null)
			{
				form.setEmailId(currentUser.getOriginalUid());
				form.setMobile(currentUser.getMobileNumber());
				form.setName(currentUser.getName());
			}

			final SearchPageData<OrderHistoryData> searchPageDataParentOrder = mplOrderFacade
					.getPagedFilteredParentOrderHistory(pageableData);
			for (final OrderHistoryData orderHistoryData : searchPageDataParentOrder.getResults())
			{
				final OrderData orderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderHistoryData.getCode());
				//this scenario will occour only when product is missing in order entries.
				if (null == orderDetails)
				{
					continue;
				}
				orderDatas.addAll(orderDetails.getSellerOrderList());
			}
			form.setOrderDatas(orderDatas);

			webCrmModels = mplWebFormService.getWebCRMParentNodes();
			for (final MplWebCrmModel crmModel : webCrmModels)
			{
				final NodeFormData node = new NodeFormData();
				node.setNodeType(crmModel.getNodeType());
				node.setNodeCode(crmModel.getNodeCrmCode());
				node.setNodeDesc(crmModel.getNodeText());
				node.setCreateTicketAllowed(Boolean.valueOf(crmModel.isCreateTicketAllowed()));
				node.setNodeDisplayAllowed(Boolean.valueOf(crmModel.isNodeDisplayAllowed()));
				node.setTicketAnswer(crmModel.getTicketAnswer());
				nodes.add(node);
			}

			form.setNodes(nodes);
		}
		catch (final Exception e)
		{
			LOG.error("getWebCRMForm" + e);
		}
		return form;
	}


	@Override
	public WebForm getWebCRMChildren(final String nodeParent)
	{
		final WebForm form = new WebForm();
		List<MplWebCrmModel> webCrmModels = new ArrayList<>();
		final List<NodeFormData> nodes = new ArrayList<>();
		CustomerModel currentUser = null;
		try
		{
			currentUser = (CustomerModel) userService.getCurrentUser();
			if (currentUser != null)
			{
				form.setEmailId(currentUser.getOriginalUid());
				form.setMobile(currentUser.getMobileNumber());
				form.setName(currentUser.getName());
			}

			webCrmModels = mplWebFormService.getWebCRMByNodes(nodeParent);
			for (final MplWebCrmModel crmModel : webCrmModels)
			{
				final NodeFormData node = new NodeFormData();
				node.setNodeType(crmModel.getNodeType());
				node.setNodeCode(crmModel.getNodeCrmCode());
				node.setNodeDesc(crmModel.getNodeText());
				node.setCreateTicketAllowed(Boolean.valueOf(crmModel.isCreateTicketAllowed()));
				node.setNodeDisplayAllowed(Boolean.valueOf(crmModel.isNodeDisplayAllowed()));
				node.setTicketAnswer(crmModel.getTicketAnswer());
				nodes.add(node);
			}

			form.setNodes(nodes);
		}
		catch (final Exception e)
		{
			LOG.error("getWebCRMForm" + e);
		}
		return form;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facades.webform.MplWebFormFacade#checkDuplicateWebCRMTickets(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkDuplicateWebCRMTickets(final WebFormData formData)
	{
		try
		{
			return mplWebFormService.checkDuplicateWebCRMTickets(formData);
		}
		catch (final Exception e)
		{
			LOG.error("checkDuplicateWebCRMTickets" + e);
			return false;
		}
	}

	/**
	 * This method is created to update the ticket status in commerce DB, from the realtime response received from CRM
	 * (TPR-5989)
	 *
	 * @param ticketStatusUpdate
	 * @return success/failure response
	 */
	@Override
	public boolean webFormticketStatusUpdate(final TicketStatusUpdate ticketStatusUpdate)
	{
		try
		{
			return mplWebFormService.webformTicketStatusUpdate(ticketStatusUpdate);
		}
		catch (final Exception e)
		{
			LOG.error("webFormticketStatusUpdate" + e);
			return false;
		}
	}

	/**
	 * This method is created to send the ticket to CRM via PI
	 *
	 * @param formData
	 * @return the success/failure boolean response
	 */
	@Override
	public boolean sendWebformTicket(final WebFormData formData) throws Exception
	{
		//Setting ECOM request prefix as E to for COMM triggered Ticket
		prefixableKeyGenerator.setPrefix(MarketplacecommerceservicesConstants.TICKETID_PREFIX_E);
		formData.setCommerceTicketId(prefixableKeyGenerator.generate().toString());
		//Sending ticket to CRM via PI
		return mplWebFormService.sendWebFormTicket(formData);
	}

}
