/**
 *
 */
package com.tisl.mpl.fulfilmentprocess.actions.order;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.CODReturnOMSSyncProcessModel;
import com.tisl.mpl.core.model.GlobalCodeMasterModel;
import com.tisl.mpl.marketplacecommerceservices.service.GlobalCodeService;
import com.tisl.mpl.mplcommerceservices.service.data.RefundInfo;
import com.tisl.mpl.service.MplRefundStatusService;
import com.tisl.mpl.xml.pojo.RefundInfoResponse;


/**
 * @author 554618
 *
 */
public class CODReturnOMSSyncAction extends AbstractProceduralAction<CODReturnOMSSyncProcessModel>
{

	private static final Logger LOG = Logger.getLogger(CODReturnOMSSyncAction.class);

	@Autowired
	private ModelService modelService;
	@Autowired
	private GlobalCodeService globalCodeService;
	@Autowired
	private MplRefundStatusService mplRefundStatusService;


	@Override
	public void executeAction(final CODReturnOMSSyncProcessModel process) throws Exception
	{

		final String transactionId = process.getTransactionId();
		String transactionStatus = process.getTransactionStatus();//RETURN_COMPLETED or REFUND_INITIATED or REFUND_IN_PROGRESS
		final OrderModel orderModel = process.getOrder();
		if (null != orderModel)
		{
			LOG.error("Inside CODReturnOMSSyncAction with Order " + orderModel.getCode() + " with transactionID:: " + transactionId
					+ " and transactionStatus::" + transactionStatus);

			try
			{
				GlobalCodeMasterModel globalCode = null;
				if (null != transactionStatus)
				{
					globalCode = globalCodeService.getGlobalMasterCode(transactionStatus);
				}

				if (null != globalCode && null != globalCode.getGlobalCode())
				{
					transactionStatus = globalCode.getGlobalCode();
				}
				final String referenceNumber = orderModel.getParentReference().getCode();
				LOG.error("transactionStatus::" + transactionStatus + " referenceNumber::" + referenceNumber);

				try
				{
					final List<RefundInfo> refundInfolist = new ArrayList<RefundInfo>();//empty refundInfoList

					final RefundInfoResponse resp = mplRefundStatusService.refundStatusDatatoWsdto(refundInfolist, referenceNumber,
							transactionId, transactionStatus, null);
					if (resp != null && "true".equalsIgnoreCase(resp.getReceived()))
					{
						LOG.error("Order Synced to OMS");
					}
					else
					{
						throw new Exception();
					}
				}
				catch (final Exception e)
				{
					LOG.error("Exception while calling to oms " + e.getMessage());
					throw e;
				}

			}
			catch (final Exception e)
			{
				LOG.error("Exception in CODReturnOMSSyncAction " + e.getMessage());
				throw e;
			}
		}
	}


	/**
	 * @return the globalCodeService
	 */
	public GlobalCodeService getGlobalCodeService()
	{
		return globalCodeService;
	}


	/**
	 * @param globalCodeService
	 *           the globalCodeService to set
	 */
	public void setGlobalCodeService(final GlobalCodeService globalCodeService)
	{
		this.globalCodeService = globalCodeService;
	}

	/**
	 * @return the modelService
	 */
	@Override
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the mplRefundStatusService
	 */
	public MplRefundStatusService getMplRefundStatusService()
	{
		return mplRefundStatusService;
	}


	/**
	 * @param mplRefundStatusService
	 *           the mplRefundStatusService to set
	 */
	public void setMplRefundStatusService(final MplRefundStatusService mplRefundStatusService)
	{
		this.mplRefundStatusService = mplRefundStatusService;
	}


}
