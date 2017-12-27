/**
 *
 */
package com.tisl.mpl.facades.webform;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
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
import com.tisl.mpl.facades.cms.data.WebFormOrder;
import com.tisl.mpl.facades.cms.data.WebFormOrderLine;
import com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.OrderStatusCodeMasterModel;
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
	@Resource
	private ModelService modelService;
	@Resource(name = "webFormDataConverter")
	private Converter<WebFormData, MplWebCrmTicketModel> webFormDataConverter;
	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facades.webform.MplWebFormFacade#getWebCRMForm()
	 */
	@Override
	public WebForm getWebCRMForm()
	{
		final WebForm form = new WebForm();
		List<MplWebCrmModel> webCrmModels = new ArrayList<>();
		final List<NodeFormData> nodes = new ArrayList<>();
		CustomerModel currentUser = null;
		try
		{
			currentUser = (CustomerModel) userService.getCurrentUser();
			if (currentUser != null && !userService.isAnonymousUser(currentUser))
			{
				form.setEmailId(currentUser.getOriginalUid());
				form.setMobile(currentUser.getMobileNumber());
				form.setName(currentUser.getName());
			}

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
				node.setParentNode(crmModel.getNodeParent());
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
		try
		{
			webCrmModels = mplWebFormService.getWebCRMByNodes(nodeParent);
			for (final MplWebCrmModel crmModel : webCrmModels)
			{
				final NodeFormData node = new NodeFormData();
				node.setNodeType(crmModel.getNodeType());
				node.setNodeCode(crmModel.getNodeCrmCode());
				node.setNodeDesc(crmModel.getNodeText());
				node.setTicketType(crmModel.getTicketType());
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
		String commerceTicketId = null;
		try
		{
			MplWebCrmTicketModel webFormModel = modelService.create(MplWebCrmTicketModel.class);
			//checking ticket is duplicate or not in Commerce
			if (!checkDuplicateWebCRMTickets(formData))
			{
				//Setting ECOM request prefix as E to for COMM triggered Ticket
				prefixableKeyGenerator.setPrefix(MarketplacecommerceservicesConstants.TICKETID_PREFIX_E);
				commerceTicketId = prefixableKeyGenerator.generate().toString();
				formData.setCommerceTicketId(commerceTicketId);
				webFormModel = webFormDataConverter.convert(formData);
				//save Ticket in Commerce
				modelService.save(webFormModel);
				//send Ticket to CRM/PI
				mplWebFormService.sendWebFormTicket(webFormModel);
			}
			else
			{
				commerceTicketId = "duplicate";
			}
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
		return commerceTicketId;
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
				nodeL1.setTicketType(crmModel.getTicketType());
				nodeL1.setCreateTicketAllowed(crmModel.isCreateTicketAllowed());
				nodeL1.setNodeDisplayAllowed(crmModel.isNodeDisplayAllowed());
				//setting L0 node
				nodeL1.setNodeL0(crmModel.getNodeParent());
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
					nodeL2.setTicketType(crmL2Model.getTicketType());
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
						nodeL3.setTicketType(crmL3Model.getTicketType());
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
							nodeL4.setTicketType(crmL4Model.getTicketType());
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
			final String ticketSubType = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.CRM_WEBFORM_TICKET_SUB, "L1C1");

			final CustomerData currentUser = customerFacade.getCurrentCustomer();
			if (currentUser != null)
			{
				formData.setCustomerId(currentUser.getUid());
			}

			formData.setCustomerName(crmTicket.getContactName());
			formData.setL0code(crmTicket.getNodeL0());
			formData.setL1code(crmTicket.getNodeL1());
			formData.setL2code(crmTicket.getNodeL2());
			formData.setL3code(crmTicket.getNodeL3());
			formData.setL4code(crmTicket.getNodeL4());
			formData.setCustomerMobile(crmTicket.getContactMobile());
			formData.setCustomerEmail(crmTicket.getContactEmail());
			formData.setTicketType(crmTicket.getTicketType());
			if (crmTicket.getNodeL1().equalsIgnoreCase(ticketSubType))
			{
				formData.setTicketSubType(MarketplacecommerceservicesConstants.CRM_WEBFORM_TICKET_SUB_ORDER);
			}
			else
			{
				formData.setTicketSubType(MarketplacecommerceservicesConstants.CRM_WEBFORM_TICKET_SUB_NONORDER);
			}

			formData.setOrderCode(crmTicket.getOrderCode());
			formData.setSubOrderCode(crmTicket.getSubOrderCode());
			formData.setTransactionId(crmTicket.getTransactionId());
			formData.setAttachments(crmTicket.getAttachmentFiles());
			if (StringUtils.isNotEmpty(crmTicket.getComment()))
			{
				formData.setComment(crmTicket.getComment());
			}

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

	@Override
	public WebFormOrder getWebOrderLines(final PageableData pageableData)
	{
		final WebFormOrder form = new WebFormOrder();
		List<WebFormOrderLine> orderLines = new ArrayList<WebFormOrderLine>();
		String imgURl = null;
		String consignmentStatus = null;
		String currentStatus = null;
		OrderStatusCodeMasterModel customerStatusModel = null;
		try
		{
			final SearchPageData<OrderHistoryData> searchPageDataParentOrder = mplOrderFacade
					.getPagedFilteredParentOrderHistory(pageableData);
			if (searchPageDataParentOrder != null && CollectionUtils.isNotEmpty(searchPageDataParentOrder.getResults()))
			{
				// Order Line
				orderLines = new ArrayList<WebFormOrderLine>();
				final Map<String, OrderStatusCodeMasterModel> orderStatusCodeMap = orderModelService.getOrderStatusCodeMasterList();
				for (final OrderHistoryData orderHistoryData : searchPageDataParentOrder.getResults())
				{

					//Parent Order
					final OrderData orderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderHistoryData.getCode());
					//this scenario will occour only when product is missing in order entries.
					if (null == orderDetails)
					{
						continue;
					}
					//SUb Order
					if (CollectionUtils.isNotEmpty(orderDetails.getSellerOrderList()))
					{
						for (final OrderData subOrderData : orderDetails.getSellerOrderList())
						{
							for (final OrderEntryData line : subOrderData.getEntries())
							{
								final WebFormOrderLine orderLine = new WebFormOrderLine();
								if (StringUtils.isNotEmpty(orderDetails.getCode()))
								{
									orderLine.setOrderCode(orderDetails.getCode());
								}
								if (null != subOrderData && StringUtils.isNotEmpty(subOrderData.getCode()))
								{
									orderLine.setSubOrderCode(subOrderData.getCode());
								}
								if (StringUtils.isNotEmpty(line.getTransactionId()))
								{
									orderLine.setTransactionId(line.getTransactionId());
								}
								if (null != orderDetails.getCreated())
								{
									final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM ,YYYY");
									orderLine.setOrderDate(sdf.format(orderDetails.getCreated()));
								}
								if (line.getAmountAfterAllDisc() != null
										&& StringUtils.isNotEmpty(line.getAmountAfterAllDisc().getFormattedValue()))
								{
									orderLine.setProdTotalPrice(line.getAmountAfterAllDisc().getFormattedValue());
								}
								if (line.getProduct() != null && CollectionUtils.isNotEmpty(line.getProduct().getImages()))
								{
									for (final ImageData imageData : line.getProduct().getImages())
									{
										if (imageData.getFormat().equalsIgnoreCase(MarketplacecommerceservicesConstants.THUMBNAIL))
										{
											imgURl = imageData.getUrl();
											break;
										}

									}
								}
								orderLine.setProdImageURL(imgURl);
								if (line.getProduct() != null && StringUtils.isNotEmpty(line.getProduct().getName()))
								{
									orderLine.setProdTitle(line.getProduct().getName());
								}
								//TISPRDT-7784
								if (line.getConsignment() != null && line.getConsignment().getStatus() != null
										&& StringUtils.isNotEmpty(line.getConsignment().getStatus().getCode())
										&& MapUtils.isNotEmpty(orderStatusCodeMap))
								{
									consignmentStatus = line.getConsignment().getStatus().getCode();
									customerStatusModel = orderStatusCodeMap.get(consignmentStatus);
									if (null != customerStatusModel && null != customerStatusModel.getResponseStatus())
									{
										//TISPRDT-7895
										currentStatus = customerStatusModel.getResponseStatus();
									}
									else
									{
										currentStatus = MarketplacecommerceservicesConstants.EMPTY;
									}
								}
								else
								{
									currentStatus = MarketplacecommerceservicesConstants.EMPTY;
								}
								orderLine.setCustomerOrderStatus(currentStatus);

								orderLines.add(orderLine);
							}
						}
					}
				}
				form.setOrderLineDatas(orderLines);

				if (searchPageDataParentOrder.getPagination() != null)
				{
					form.setTotalOrderLines(String.valueOf(searchPageDataParentOrder.getPagination().getTotalNumberOfResults()));
				}
				form.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				form.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			}

		}
		catch (final Exception e)
		{
			LOG.error("getWebOrderLines" + e);
		}
		return form;
	}
}
