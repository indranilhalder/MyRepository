/**
 *
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.TrackOrderWsDTO;


/**
 * @author Techouts
 *
 */
@JsonAutoDetect
@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
@Controller
@RequestMapping(value = "/{baseSiteId}/trackOrder")
@CacheControl(directive = CacheControlDirective.NO_CACHE)
public class TrackOrdersController extends BaseController

{
	private static final Logger LOG = Logger.getLogger(TrackOrdersController.class);

	@Resource(name = "trackOrderWsDTOValidator")
	private Validator trackOrderWsDTOValidator;
	@Autowired
	private MplOrderFacade mplOrderFacade;

	private final static String FieldName_EmailId = "emailId";
	private final static String FieldName_OrderCode = "orderCode";
	private final static String FieldName_CaptchaCode = "captchaCode";

	@RequestMapping(value = "/wihoutLoginTrackOrder", method = RequestMethod.GET)
	@ResponseBody
	public TrackOrderWsDTO trackOrderWihoutLogin(@RequestParam("orderCode") final String orderCode,
			@RequestParam("emailId") final String emailId, @RequestParam("captchaCode") final String captchaCode)
			throws WebserviceValidationException
	{
		final TrackOrderWsDTO trackOrderWsDTO = new TrackOrderWsDTO();
		trackOrderWsDTO.setOrderCode(orderCode);
		trackOrderWsDTO.setEmailId(emailId);
		trackOrderWsDTO.setCaptchaCode(captchaCode);
		LOG.debug("Track Order Ws DTO Field values***** " + trackOrderWsDTO);
		final TrackOrderWsDTO response = new TrackOrderWsDTO();
		try
		{
			try
			{
				validate(trackOrderWsDTO, "trackOrder", trackOrderWsDTOValidator);

			}
			catch (final WebserviceValidationException webserExc)
			{

				final Errors err = (Errors) webserExc.getValidationObject();

				if (FieldName_EmailId.equals(err.getFieldError().getField()))
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.F0001);
				}
				else if (FieldName_OrderCode.equals(err.getFieldError().getField()))
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.F0002);
				}
				else if (FieldName_CaptchaCode.equals(err.getFieldError().getField()))
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.F0003);
				}
			}
			try
			{
				final OrderModel orderModel = mplOrderFacade.getOrderForAnonymousUser(orderCode);
				final UserModel userModel = orderModel.getUser();
				final CustomerModel custModel = (CustomerModel) userModel;
				if (null != custModel && null != custModel.getOriginalUid() && custModel.getOriginalUid().equals(emailId))
				{
					response.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					return response;
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.F0005);
				}

			}
			catch (final Exception e)
			{
				if (e instanceof EtailBusinessExceptions)
				{
					throw (EtailBusinessExceptions) e;
				}

				LOG.error("Error while tracking order for " + orderCode + " and " + emailId + " " + e.getMessage());
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.F0004);
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				response.setError(e.getErrorMessage());
				response.setErrorCode(e.getErrorCode());
			}
			response.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return response;
	}

}
