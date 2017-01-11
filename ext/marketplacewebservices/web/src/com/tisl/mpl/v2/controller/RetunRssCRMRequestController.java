/**
 * 
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.wsdto.ReturnRequestDTO;
import com.tisl.mpl.wsdto.ReturnRssRequestDTO;

/**
 * @author Pankaj
 *
 */

@Controller
@RequestMapping(value = "/{baseSiteId}")
public class RetunRssCRMRequestController
{
	private static final String APPLICATION_TYPE = "application/xml";
	private static final Logger LOG = Logger.getLogger(RetunRssCRMRequestController.class);
	
	@Autowired
	private CancelReturnFacade cancelReturnFacade;
	
	
	@RequestMapping(value = "/returnCRMRSSRequest", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public void rssCRMRequest(@RequestBody final ReturnRssRequestDTO returnRequest) throws WebserviceValidationException
	{
		try
		{
			ReturnRequestDTO returnRequestDTO = new ReturnRequestDTO();
			if (StringUtils.isNotEmpty(returnRequest.getCustomerID()))
			{
				returnRequestDTO.setUserId(returnRequest.getCustomerID());
			}
			if (StringUtils.isNotEmpty(returnRequest.getEcomRequestId()))
			{
				returnRequestDTO.setEcomRequestId(returnRequest.getEcomRequestId());
			}

			if (StringUtils.isNotEmpty(returnRequest.getTicketId()))
			{
				returnRequestDTO.setTicketId(returnRequest.getTicketId());
			}
			if (StringUtils.isNotEmpty(returnRequest.getOrderID()))
			{
				returnRequestDTO.setOrderCode(returnRequest.getOrderID());
			}
			if (StringUtils.isNotEmpty(returnRequest.getLineItemId()))
			{
				returnRequestDTO.setTransactionId(returnRequest.getLineItemId());
			}
			if (StringUtils.isNotEmpty(returnRequest.getTicketType()))
			{
				returnRequestDTO.setTicketType(returnRequest.getTicketType());
			}
			if (StringUtils.isNotEmpty(returnRequest.getTicketSubType()))
			{
				returnRequestDTO.setTicketSubType(returnRequest.getTicketSubType());
			}
			if (StringUtils.isNotEmpty(returnRequest.getCanc_Ret_Reas()))
			{
				returnRequestDTO.setReturnReasonCode(returnRequest.getCanc_Ret_Reas());
			}
			if (StringUtils.isNotEmpty(returnRequest.getRefund_Type()))
			{
				returnRequestDTO.setRefundType(returnRequest.getRefund_Type());
			}
			
			if (StringUtils.isNotEmpty(returnRequest.getFlag()))
			{
				returnRequestDTO.setFlag(returnRequest.getFlag());
			}
			
			LOG.info("RssRetunCRMController CRM To Commerce Rss interface");
			cancelReturnFacade.returnRssCRMRequest(returnRequestDTO);
		}
		catch (Exception exception)
		{
			LOG.error("RetunRssCRMRequestController::::" + exception.getMessage());
		}

	}

}
	
