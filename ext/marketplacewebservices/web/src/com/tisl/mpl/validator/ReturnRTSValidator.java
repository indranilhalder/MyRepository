/**
 * 
 */
package com.tisl.mpl.validator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.wsdto.ReturnInitiateRequestDTO;

/**
 * @author Dileep
 *
 */
public class ReturnRTSValidator implements Validator
{
	
	/**
	 * 
	 */
	private static final String VALIDATION_ORDER_LINES = "orderLines[";


	@Override
	public boolean supports(final Class clazz)
	{
		return ReturnInitiateRequestDTO.class.isAssignableFrom(clazz);
	}

	
	@Override
	public void validate(Object object, Errors errors)
	{
		ReturnInitiateRequestDTO data = (ReturnInitiateRequestDTO) object;
		Assert.notNull(errors, "Errors object must not be null");
		if(data != null && CollectionUtils.isNotEmpty(data.getOrderLines()))
		{
			for(int i = 0;i < data.getOrderLines().size();i++)
			{
				if(StringUtils.isEmpty(data.getOrderLines().get(i).getOrderId()))
				{
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, VALIDATION_ORDER_LINES+ i +"].orderId", MarketplacewebservicesConstants.FIELD_REQD);
				}
				if(StringUtils.isEmpty(data.getOrderLines().get(i).getTransactionId()))
				{
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, VALIDATION_ORDER_LINES+ i +"].transactionId", MarketplacewebservicesConstants.FIELD_REQD);
				}
				if(StringUtils.isEmpty(data.getOrderLines().get(i).getInterfaceType()))
				{
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, VALIDATION_ORDER_LINES+ i +"].interfaceType", MarketplacewebservicesConstants.FIELD_REQD);
				}
				if(StringUtils.isEmpty(data.getOrderLines().get(i).getReasonCode()))
				{
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, VALIDATION_ORDER_LINES+ i +"].reasonCode", MarketplacewebservicesConstants.FIELD_REQD);
				}
				if(StringUtils.isEmpty(data.getOrderLines().get(i).getRefundMode()))
				{
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, VALIDATION_ORDER_LINES+ i +"].refundMode", MarketplacewebservicesConstants.FIELD_REQD);
				}
				if(StringUtils.isEmpty(data.getOrderLines().get(i).getReturnStoreId()))
				{
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, VALIDATION_ORDER_LINES+ i +"].returnStoreId", MarketplacewebservicesConstants.FIELD_REQD);
				}	
			}
		}
		else
		{
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "OrderLines", MarketplacewebservicesConstants.FIELD_REQD);
		}
	}
}
