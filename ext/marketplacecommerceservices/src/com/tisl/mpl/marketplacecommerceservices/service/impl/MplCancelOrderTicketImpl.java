/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.data.SendTicketLineItemData;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.marketplacecommerceservices.service.MplCancelOrderTicket;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.service.TicketCreationCRMservice;


/**
 * @author TCS
 *
 */
public class MplCancelOrderTicketImpl implements MplCancelOrderTicket
{
	protected static final Logger LOG = Logger.getLogger(MplCancelOrderTicketImpl.class);

	@Autowired
	private TicketCreationCRMservice ticketCreate;
	@Autowired
	private ModelService modelService;
	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;


	/**
	 * This method creates cancellation ticket in CRM for order cancelled from EBS console. This cancel ticket is in
	 * order level.
	 *
	 * @param orderModel
	 * @return boolean
	 *
	 */
	@Override
	public boolean createCancelTicket(final OrderModel orderModel)
	{
		final List<SendTicketLineItemData> lineItemDataList = new ArrayList<SendTicketLineItemData>();
		final SendTicketRequestData sendTicketRequestData = new SendTicketRequestData();
		boolean returnflag = false;
		if (null != orderModel.getChildOrders() && !orderModel.getChildOrders().isEmpty())
		{
			for (final OrderModel suborder : orderModel.getChildOrders())
			{
				if (null != suborder.getEntries() && !suborder.getEntries().isEmpty())
				{
					for (final AbstractOrderEntryModel entry : suborder.getEntries())
					{
						final SendTicketLineItemData sendTicketLineItemData = new SendTicketLineItemData();

						if (entry.getQuantity().doubleValue() > 0)
						{
							if (StringUtils.isNotEmpty(entry.getOrderLineId()))
							{
								sendTicketLineItemData.setLineItemId(entry.getOrderLineId());
							}
							else
							{
								sendTicketLineItemData.setLineItemId(entry.getTransactionID());
							}
							//TODO: Change after valid code is given
							sendTicketLineItemData.setCancelReasonCode("06");
						}
						lineItemDataList.add(sendTicketLineItemData);
					}
				}
			}
		}

		sendTicketRequestData.setCustomerID(orderModel.getUser().getUid());
		sendTicketRequestData.setLineItemDataList(lineItemDataList);
		sendTicketRequestData.setOrderId(orderModel.getCode());
		//sendTicketRequestData.setSubOrderId(orderModel.getChildOrders().get(0).getCode());
		sendTicketRequestData.setTicketType("C");
		//TISEE-980
		sendTicketRequestData.setRefundType("S");
		try
		{
			getTicketCreate().ticketCreationModeltoWsDTO(sendTicketRequestData);
			returnflag = true;
		}
		catch (final NullPointerException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
		}
		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
		}
		catch (final JAXBException e)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
		}
		catch (final Exception e)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
		}
		return returnflag;
	}


	@Override
	public void refundMapping(final String refundID, final OrderModel orderModel)
	{
		//TISPRO-216 Starts
		if (CollectionUtils.isNotEmpty(orderModel.getChildOrders()))
		{
			for (final OrderModel subOrderModel : orderModel.getChildOrders())
			{
				if (subOrderModel != null && CollectionUtils.isNotEmpty(subOrderModel.getEntries()))
				{
					for (final AbstractOrderEntryModel subOrderEntryModel : subOrderModel.getEntries())
					{
						{
							if (subOrderEntryModel != null)
							{
								double refundAmount = 0D;
								double deliveryCost = 0D;
								if (subOrderEntryModel.getCurrDelCharge() != null)
								{
									deliveryCost = subOrderEntryModel.getCurrDelCharge().doubleValue();
								}

								refundAmount = subOrderEntryModel.getNetAmountAfterAllDisc().doubleValue() + deliveryCost;
								refundAmount = mplJusPayRefundService.validateRefundAmount(refundAmount, subOrderModel);

								// Making RTM entry to be picked up by webhook job
								final RefundTransactionMappingModel refundTransactionMappingModel = getModelService().create(
										RefundTransactionMappingModel.class);
								refundTransactionMappingModel.setRefundedOrderEntry(orderModel.getChildOrders().get(0).getEntries()
										.get(0));
								refundTransactionMappingModel.setJuspayRefundId(refundID);
								refundTransactionMappingModel.setCreationtime(new Date());
								//refundTransactionMappingModel.setRefundTime(new Date());
								refundTransactionMappingModel.setRefundType(JuspayRefundType.CANCELLED_FOR_RISK);
								refundTransactionMappingModel.setRefundAmount(new Double(refundAmount));//TISPRO-216 : Refund amount Set in RTM
								getModelService().save(refundTransactionMappingModel);
							}
						}
					}
				}
			}
		}
		//TISPRO-216 Ends
	}

	//Getters and setters
	/**
	 * @return the ticketCreate
	 */
	public TicketCreationCRMservice getTicketCreate()
	{
		return ticketCreate;
	}


	/**
	 * @param ticketCreate
	 *           the ticketCreate to set
	 */
	public void setTicketCreate(final TicketCreationCRMservice ticketCreate)
	{
		this.ticketCreate = ticketCreate;
	}


	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}


	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}




}
