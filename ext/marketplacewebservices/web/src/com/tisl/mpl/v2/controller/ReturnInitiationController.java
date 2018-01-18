/**
 *
 */
package com.tisl.mpl.v2.controller;



import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.data.OrderLineData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.OrderLineDTO;
import com.tisl.mpl.wsdto.ReturnInitiateRequestDTO;
import com.tisl.mpl.wsdto.ReturnInitiateResponse;



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

	@Resource(name = "returnRTSValidator")
	private Validator returnRTSValidator;

	private static final String APPLICATION_TYPE = "application/xml";
	private static final Logger LOG = Logger.getLogger(ReturnInitiationController.class);

	@RequestMapping(method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public ReturnInitiateResponse initiationRequest(@RequestBody final ReturnInitiateRequestDTO returnRequest)
			throws WebserviceValidationException
	{
		Marshaller marshaller = null;
		final StringWriter stringWriter = new StringWriter();
		final List<OrderLineData> listdata = new ArrayList<OrderLineData>();
		final ReturnInitiateResponse responseDTO = new ReturnInitiateResponse();

		final Errors errors = new BeanPropertyBindingResult(returnRequest, "returnRequest");
		returnRTSValidator.validate(returnRequest, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
		try
		{
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
				//TPR-5954 || Start
				if (StringUtils.isBlank(data.getSubReasonCode()))
				{
					lineData.setSubReasonCode(data.getSubReasonCode());
				}
				if (StringUtils.isBlank(data.getComments()))
				{
					lineData.setComments(data.getComments());
				}
				//TPR-5954 || Start
				listdata.add(lineData);
			}
			if (CollectionUtils.isNotEmpty(listdata))
			{
				LOG.info("Return initiation from SP with the Seller order Id : >>>>>> " + listdata.get(0).getOrderId());
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
				try
				{
					final JAXBContext context = JAXBContext.newInstance(ReturnInitiateResponse.class);
					if (null != context)
					{
						marshaller = context.createMarshaller();
					}
					if (null != marshaller)
					{
						marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					}
					if (null != marshaller)
					{
						marshaller.marshal(responseDTO, stringWriter);
					}
					LOG.info(" Return initiation From SP Response " + stringWriter.toString());
				}
				catch (final JAXBException e)
				{
					LOG.error("Error in order Cancellation from SP Response");
					e.printStackTrace();
				}
				return responseDTO;
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				responseDTO.setErrorCode(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				responseDTO.setErrorCode(e.getErrorCode());
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				responseDTO.setErrorCode(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				responseDTO.setErrorCode(e.getErrorCode());
			}
		}
		catch (final Exception e)
		{
			responseDTO.setErrorCode(e.getMessage());
			LOG.error("ReturnInitiationController Error getting" + e.getMessage());
		}
		return responseDTO;
	}
}
