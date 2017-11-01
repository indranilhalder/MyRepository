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

import com.tisl.mpl.core.model.MplWebCrmModel;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.cms.data.Node;
import com.tisl.mpl.facades.cms.data.WebForm;
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
		final List<Node> nodes = new ArrayList<>();
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
				final Node node = new Node();
				node.setCategory(crmModel.getNodeType());
				node.setNodeCode(crmModel.getNodeCrmCode());
				node.setNodeDesc(crmModel.getNodeText());
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
		final List<Node> nodes = new ArrayList<>();
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
				final Node node = new Node();
				node.setCategory(crmModel.getNodeType());
				node.setNodeCode(crmModel.getNodeCrmCode());
				node.setNodeDesc(crmModel.getNodeText());
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
	public boolean checkDuplicateWebCRMTickets(final String ticketType, final String orderCode, final String subOrderCode,
			final String transactionId, final String L0code, final String L1code, final String L2code, final String L3code,
			final String L4code, final String customerId)
	{
		// YTODO Auto-generated method stub
		return mplWebFormService.checkDuplicateWebCRMTickets(ticketType, orderCode, subOrderCode, transactionId, L0code, L1code,
				L2code, L3code, L4code, customerId);
	}

}
