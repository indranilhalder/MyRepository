/**
 *
 */
package com.tisl.mpl.v2.controller;



import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.data.OrderLineData;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.wsdto.OrderLineDTO;
import com.tisl.mpl.wsdto.ReturnInitiateRequestDTO;
import com.tisl.mpl.wsdto.ReturnInitiateResponseDTO;



/**
 * @author Dileep
 *
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/returnInitiation")
public class ReturnInitiationController extends BaseController
{
	@Autowired
	private CancelReturnFacade cancelReturnFacade;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ReturnInitiateResponseDTO initiationRequest(@RequestBody final ReturnInitiateRequestDTO returnRequest)
	{

		final List<OrderLineData> listdata = new ArrayList<OrderLineData>();
		for (final OrderLineDTO data : returnRequest.getOrderLines())
		{
			final OrderLineData lineData = new OrderLineData();
			lineData.setOrderId(data.getOrderId());
			lineData.setTransactionId(data.getTransactionId());
			lineData.setInterfaceType(data.getInterfaceType());
			lineData.setReasonCode(data.getReasonCode());
			lineData.setRefundMode(data.getRefundMode());
			lineData.setReturnStoreId(data.getReturnStoreId());
			lineData.setStoreCreditNo(data.getStoreCreditNo());
			listdata.add(lineData);
		}
		if (CollectionUtils.isNotEmpty(listdata))
		{
			final ReturnInitiateResponseDTO responseDTO = new ReturnInitiateResponseDTO();
			final List<OrderLineData> responseList = cancelReturnFacade.returnInitiationForRTS(listdata);
			if (CollectionUtils.isNotEmpty(responseList))
			{
				final List<OrderLineDTO> orderLines = new ArrayList<OrderLineDTO>();
				for (final OrderLineData response : responseList)
				{
					final OrderLineDTO dto = new OrderLineDTO();
					dto.setOrderId(response.getOrderId());
					dto.setTransactionId(response.getTransactionId());
					dto.setIsReturnEligible(response.getIsReturnEligible());
					dto.setIsReturnInitiated(response.getIsReturnInitiated());
					orderLines.add(dto);
				}
				responseDTO.setOrderLines(orderLines);
			}
			return responseDTO;
		}
		return new ReturnInitiateResponseDTO();
	}
}
