/**
 *
 */
package com.tisl.mpl.facades.webform;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tis.mpl.facade.data.TicketStatusUpdate;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.keygenerator.MplPrefixablePersistentKeyGenerator;
import com.tisl.mpl.core.model.MplWebCrmModel;
import com.tisl.mpl.core.model.MplWebCrmTicketModel;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.cms.data.NodeFormData;
import com.tisl.mpl.facades.cms.data.WebForm;
import com.tisl.mpl.facades.cms.data.WebFormData;
import com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService;
import com.tisl.mpl.wsdto.CRMWebFormDataRequest;
import com.tisl.mpl.wsdto.CRMWebFormDataResponse;
import com.tisl.mpl.wsdto.CRMWsData;


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
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	@Resource(name = "webFormDataConverter")
	private Converter<WebFormData, MplWebCrmTicketModel> webFormDataConverter;

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
	public String sendWebformTicket(final WebFormData formData) throws Exception
	{
		MplWebCrmTicketModel webFormModel = new MplWebCrmTicketModel();
		final WebFormData ticketData = new WebFormData();
		//Setting ECOM request prefix as E to for COMM triggered Ticket
		prefixableKeyGenerator.setPrefix(MarketplacecommerceservicesConstants.TICKETID_PREFIX_E);
		final String commerceTicketId = prefixableKeyGenerator.generate().toString();
		formData.setCommerceTicketId(commerceTicketId);
		//checking ticket is duplicate or not in Commerce
		if (checkDuplicateWebCRMTickets(formData))
		{
			webFormModel = webFormDataConverter.convert(ticketData, webFormModel);
			mplWebFormService.sendWebFormTicket(webFormModel);
		}
		return commerceTicketId;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.webform.MplWebFormFacade#getCrmParentChildNodes(java.lang.String)
	 */
	@Override
	public List<WebFormData> getCrmParentChildNodes(final String nodeParent)
	{
		// YTODO Auto-generated method stub
		return null;
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.webform.MplWebFormFacade#getCrmParentChildNodes(java.lang.String)
	 */
	@Override
	public List<CRMWsData> getAllWebCRMTreedata()
	{
		final List<CRMWsData> crmL1Data = new ArrayList<CRMWsData>();
		List<CRMWsData> crmL2Data = new ArrayList<CRMWsData>();
		List<CRMWsData> crmL3Data = new ArrayList<CRMWsData>();
		List<CRMWsData> crmL4Data = new ArrayList<CRMWsData>();

		try
		{
			final List<MplWebCrmModel> webCrmL1Models = mplWebFormService.getWebCRMParentNodes();


			for (final MplWebCrmModel crmModel : webCrmL1Models)
			{
				//L1
				final CRMWsData nodeL1 = new CRMWsData();
				nodeL1.setNodeType(crmModel.getNodeType());
				nodeL1.setNodeCode(crmModel.getNodeCrmCode());
				nodeL1.setNodeDesc(crmModel.getNodeText());
				nodeL1.setTicketAnswer((crmModel.getTicketAnswer()));
				nodeL1.setCreateTicketAllowed(crmModel.isCreateTicketAllowed());
				nodeL1.setNodeDisplayAllowed(crmModel.isNodeDisplayAllowed());
				//crmData.add(nodeL1);
				//L2
				final List<MplWebCrmModel> webCrmL2Models = mplWebFormService.getWebCRMByNodes(crmModel.getNodeCrmCode());
				crmL2Data = new ArrayList<CRMWsData>();
				for (final MplWebCrmModel crmL2Model : webCrmL2Models)
				{
					final CRMWsData nodeL2 = new CRMWsData();
					nodeL2.setNodeType(crmL2Model.getNodeType());
					nodeL2.setNodeCode(crmL2Model.getNodeCrmCode());
					nodeL2.setNodeDesc(crmL2Model.getNodeText());
					nodeL2.setTicketAnswer((crmL2Model.getTicketAnswer()));
					nodeL2.setCreateTicketAllowed(crmL2Model.isCreateTicketAllowed());
					nodeL2.setNodeDisplayAllowed(crmL2Model.isNodeDisplayAllowed());

					//L3

					final List<MplWebCrmModel> webCrmL3Models = mplWebFormService.getWebCRMByNodes(crmL2Model.getNodeCrmCode());
					crmL3Data = new ArrayList<CRMWsData>();
					for (final MplWebCrmModel crmL3Model : webCrmL3Models)
					{
						final CRMWsData nodeL3 = new CRMWsData();
						nodeL3.setNodeType(crmL3Model.getNodeType());
						nodeL3.setNodeCode(crmL3Model.getNodeCrmCode());
						nodeL3.setNodeDesc(crmL3Model.getNodeText());
						nodeL3.setTicketAnswer((crmL3Model.getTicketAnswer()));
						nodeL3.setCreateTicketAllowed(crmL3Model.isCreateTicketAllowed());
						nodeL3.setNodeDisplayAllowed(crmL3Model.isNodeDisplayAllowed());



						//L4
						final List<MplWebCrmModel> webCrmL4Models = mplWebFormService.getWebCRMByNodes(crmL3Model.getNodeCrmCode());

						crmL4Data = new ArrayList<CRMWsData>();
						for (final MplWebCrmModel crmL4Model : webCrmL4Models)
						{
							final CRMWsData nodeL4 = new CRMWsData();
							nodeL4.setNodeType(crmL4Model.getNodeType());
							nodeL4.setNodeCode(crmL4Model.getNodeCrmCode());
							nodeL4.setNodeDesc(crmL4Model.getNodeText());
							nodeL4.setTicketAnswer((crmL4Model.getTicketAnswer()));
							nodeL4.setCreateTicketAllowed(crmL4Model.isCreateTicketAllowed());
							nodeL4.setNodeDisplayAllowed(crmL4Model.isNodeDisplayAllowed());

							crmL4Data.add(nodeL4);

						}
						nodeL3.setChildren(crmL4Data);
						crmL3Data.add(nodeL3);
					}
					nodeL2.setChildren(crmL3Data);
					crmL2Data.add(nodeL2);
				}
				nodeL1.setChildren(crmL2Data);
				crmL1Data.add(nodeL1);


			}


		}
		catch (final Exception e)
		{
			LOG.error(e);


		}

		return crmL1Data;


	}

	@Override
	public CRMWebFormDataResponse getTicketSubmitForm(final CRMWebFormDataRequest crmTicket)
	{
		final CRMWebFormDataResponse mplCRMWebFormResponseData = new CRMWebFormDataResponse();
		final WebFormData formData = new WebFormData();
		String commerceRef = null;
		try
		{
			formData.setComment(crmTicket.getComment());
			final CustomerData currentUser = customerFacade.getCurrentCustomer();
			if (currentUser != null)
			{
				formData.setCustomerId(currentUser.getUid());
			}
			formData.setOrderCode(crmTicket.getOrderCode());
			formData.setSubOrderCode(crmTicket.getSubOrderCode());
			formData.setTransactionId(crmTicket.getTransactionId());
			formData.setAttachments(new ArrayList(Arrays.asList(crmTicket.getAttachments().split(","))));

			commerceRef = sendWebformTicket(formData);
			// API response set
			if (commerceRef != null)
			{
				mplCRMWebFormResponseData.setReferenceNum(commerceRef);
				mplCRMWebFormResponseData.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				mplCRMWebFormResponseData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
		}
		catch (final Exception e)
		{
			LOG.error("ticketFormSave" + e);
			mplCRMWebFormResponseData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return mplCRMWebFormResponseData;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.webform.MplWebFormFacade#getTicketSubmitForm()
	 */
	@Override
	public List<CRMWebFormDataRequest> getTicketSubmitForm()
	{
		// YTODO Auto-generated method stub
		return null;
	}

}
