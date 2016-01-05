package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.service.MplClickToCallWebService;
import com.tisl.mpl.sms.facades.SendSMSFacade;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.wsdto.ClickToCallWsDTO;


/**
 * @author TCS
 *
 */
@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = RequestMappingUrlConstants.LINK_CLICK_TO_CHAT_CALL)
public class ClickToChatCallController extends AbstractPageController
{
	@Resource
	private OTPGenericService otpService;
	@Resource
	private UserService userService;
	@Resource
	private ConfigurationService configurationService;
	@Resource(name = "mplClickToCallService")
	private MplClickToCallWebService cTocWebService;
	@Autowired
	private SendSMSFacade sendSMSFacade;

	/**
	 * @desc this method loads the chat pop up and if user is logged in takes the email address and phone no to chat
	 *       window
	 * @param model
	 * @return ModelAndView
	 */
	@RequestMapping(value = "chat", method = RequestMethod.GET)
	public ModelAndView clickToChat(ModelAndView model)
	{

		final Map<String, String> reasons = reasonCodePopulation();
		final String clickToChatUrl = getConfigurationService().getConfiguration().getString("c2c.chat.url");
		model = loggedInCustomerDetails(model);
		model.addObject("reasons", reasons);
		model.addObject("chatUrl", clickToChatUrl);
		model.setViewName(ControllerConstants.Views.Pages.ClickToChatCall.chatPage);
		return model;
	}

	/**
	 * @desc this method communicates with third party chat API window
	 * @param model
	 * @return ModelAndView
	 */
	@RequestMapping(value = "invoke-chat", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> clickToChatWebService(
			@RequestParam(value = ModelAttributetConstants.EMAIL_ID, required = true, defaultValue = "") final String emailId,
			@RequestParam(value = ModelAttributetConstants.CONTACTNO, required = true, defaultValue = "") final String contactNo,
			@RequestParam(value = ModelAttributetConstants.CUSTOMERNAME, required = true, defaultValue = "") final String customerName,
			@RequestParam(value = ModelAttributetConstants.REASON, required = true, defaultValue = "") final String reason)
	{
		Map<String, String> valOTPMap = new HashMap<String, String>();
		valOTPMap = validateC2CForm(emailId, customerName, contactNo);
		return valOTPMap;

	}

	/**
	 * @desc this method loads the chat pop up and if user is logged in takes the email address and phone no to call
	 * @param model
	 * @return ModelAndView
	 */
	@RequestMapping(value = "call", method = RequestMethod.GET)
	public ModelAndView clickToCall(ModelAndView model)
	{
		final Map<String, String> reasons = reasonCodePopulation();
		model = loggedInCustomerDetails(model);
		model.addObject("reasons", reasons);
		model.setViewName(ControllerConstants.Views.Pages.ClickToChatCall.callPage);
		return model;
	}


	/**
	 * @desc this method loads the chat pop up and if user is logged in takes the email address and phone no to call
	 * @param emailId
	 *           , contactNo
	 * @return ModelAndView
	 */
	@RequestMapping(value = "generateOTP", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> clickToCallOTPGenerate(
			@RequestParam(value = ModelAttributetConstants.EMAIL_ID, required = true, defaultValue = "") final String emailId,
			@RequestParam(value = ModelAttributetConstants.CONTACTNO, required = true, defaultValue = "") final String contactNo,
			@RequestParam(value = ModelAttributetConstants.CUSTOMERNAME, required = true, defaultValue = "") final String customerName,
			@RequestParam(value = ModelAttributetConstants.REASON, required = true, defaultValue = "") final String reason)
	{
		Map<String, String> genOTPMap = new HashMap<String, String>();
		genOTPMap = validateC2CForm(emailId, customerName, contactNo);

		if (genOTPMap.isEmpty())
		{
			try
			{
				final String otp = getOtpService().generateOTP(emailId, OTPTypeEnum.C2C.getCode(), contactNo);
				sendSMSFacade.sendSms(MarketplacecommerceservicesConstants.SMS_SENDER_ID,
						MarketplacecommerceservicesConstants.SMS_MESSAGE_C2C_OTP
								.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, otp),
						contactNo);
			}
			catch (final InvalidKeyException e)
			{
				throw new EtailNonBusinessExceptions(e);
			}
			catch (final NoSuchAlgorithmException e)
			{
				throw new EtailNonBusinessExceptions(e);
			}
			catch (final Exception e)
			{
				throw new EtailNonBusinessExceptions(e);
			}

			genOTPMap.put("otp_generated", "true");
			genOTPMap.put(ModelAttributetConstants.EMAIL_ID, emailId);
			genOTPMap.put(ModelAttributetConstants.CONTACTNO, contactNo);
			genOTPMap.put(ModelAttributetConstants.CUSTOMERNAME, customerName);
			genOTPMap.put(ModelAttributetConstants.REASON, reason);
			return genOTPMap;

		}
		else
		{
			return genOTPMap;
		}
	}

	/**
	 * @desc this method validates the OTP
	 * @param emailId
	 *           , contactNo , otp
	 * @return ModelAndView
	 * @throws IOException
	 */
	@SuppressWarnings("boxing")
	@RequestMapping(value = "validateOTP", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> clickToCallvalidateOTP(
			@RequestParam(value = ModelAttributetConstants.EMAIL_ID, required = true, defaultValue = "") final String emailId,
			@RequestParam(value = ModelAttributetConstants.CONTACTNO, required = true, defaultValue = "") final String contactNo,
			@RequestParam(value = ModelAttributetConstants.CUSTOMERNAME, required = true, defaultValue = "") final String customerName,
			@RequestParam(value = ModelAttributetConstants.REASON, required = true, defaultValue = "") final String reason,
			@RequestParam(value = "otp", required = true, defaultValue = "") final String otp) throws IOException
	{

		Map<String, String> valOTPMap = new HashMap<String, String>();
		valOTPMap = validateC2CForm(emailId, customerName, contactNo);

		if (valOTPMap.isEmpty() && null != otp && !otp.isEmpty())
		{
			final OTPResponseData otpResponseData = getOtpService().validateOTP(emailId, contactNo, otp, OTPTypeEnum.C2C, Long
					.valueOf(getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.TIMEFOROTP)));
			if (otpResponseData.getOTPValid())
			{
				/*
				 * Conecting to Genius's server
				 */
				final ClickToCallWsDTO c2cDto = new ClickToCallWsDTO();
				String cTocResponse = null;
				c2cDto.setCustomerEmail(emailId);
				c2cDto.setCustomerMobile(contactNo);
				c2cDto.setCustomerName(customerName);
				c2cDto.setReasonToCall(reason);
				try
				{
					cTocResponse = cTocWebService.clickToCallWebService(c2cDto);
				}
				catch (final Exception e)
				{
					LOG.error(e.getMessage());
				}
				if (cTocResponse == null)
				{
					valOTPMap.put("click_to_call_response", "");
				}
				else
				{
					valOTPMap.put("click_to_call_response", cTocResponse);
				}
				valOTPMap.put("valid_otp", "true");
			}
			else
			{
				valOTPMap.put("invalid_otp", getConfigurationService().getConfiguration().getString("c2c.invalid_otp"));
			}
		}
		else
		{
			if (null == otp || otp.isEmpty())
			{
				valOTPMap.put("error_otp", getConfigurationService().getConfiguration().getString("c2c.error_otp"));
				return valOTPMap;
			}
			return valOTPMap;
		}
		return valOTPMap;
	}

	/**
	 * @return the otpService
	 */
	public OTPGenericService getOtpService()
	{
		return otpService;
	}

	/**
	 * @param otpService
	 *           the otpService to set
	 */
	public void setOtpService(final OTPGenericService otpService)
	{
		this.otpService = otpService;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 *
	 * @param email
	 * @param name
	 * @param contact
	 * @return boolean
	 */
	public Map<String, String> validateC2CForm(final String email, final String name, final String contact)
	{
		final Map<String, String> errors = new LinkedHashMap<String, String>();

		if (null == name || name.isEmpty() || name.matches("^\\s*$"))
		{
			errors.put("error_name", getConfigurationService().getConfiguration().getString("c2c.error_name"));
		}
		if (null == email || email.isEmpty())
		{
			errors.put("error_email", getConfigurationService().getConfiguration().getString("c2c.error_email"));
		}
		else if (!validateEmail(email))
		{
			errors.put("error_email", getConfigurationService().getConfiguration().getString("c2c.error_email"));
		}
		if (null == contact || contact.isEmpty())
		{
			errors.put("error_contact", getConfigurationService().getConfiguration().getString("c2c.error_contact"));
		}
		else if (!validateMobileNo(contact))
		{
			errors.put("error_contact", getConfigurationService().getConfiguration().getString("c2c.error_contact"));
		}
		return errors;

	}

	/**
	 * @ Desc This method validates the email address
	 *
	 * @param email
	 * @return
	 */
	public boolean validateEmail(final String email)
	{
		Pattern pattern;
		Matcher matcher;

		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		if (null != email)
		{
			pattern = Pattern.compile(EMAIL_PATTERN);
			matcher = pattern.matcher(email);
			return matcher.matches();
		}
		else
		{
			return false;
		}
	}

	/**
	 * @desc This method validates mobile length and number
	 * @param mobileNo
	 * @return boolean
	 */
	public boolean validateMobileNo(final String mobileNo)
	{

		return (mobileNo != null && mobileNo.matches("\\d{10}"));

		//	Sonar Fix
		//			if (mobileNo != null && mobileNo.matches("\\d{10}"))
		//			{
		//				return true;
		//			}
		//			else
		//			{
		//				return false;
		//			}


	}

	/**
	 * Common method for getting reason from local.properties
	 *
	 * @return Map<String, String>
	 */
	public Map<String, String> reasonCodePopulation()
	{

		final Map<String, String> reasons = new LinkedHashMap<String, String>();
		final String reasonCode = getConfigurationService().getConfiguration().getString("c2c.reason_code");
		try
		{
			final String[] reasonCodeArray = reasonCode.split(",");
			if (null != reasonCodeArray)
			{
				for (final String code : reasonCodeArray)
				{
					if (null != code)
					{
						reasons.put(code, code);
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
		return reasons;
	}

	/**
	 * This method provides information for logged in customer
	 *
	 * @param model
	 * @return ModelAndView
	 */
	public ModelAndView loggedInCustomerDetails(final ModelAndView model)
	{

		final CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();
		final String mobileNo = customerModel.getMobileNumber();
		final String emailId = customerModel.getOriginalUid();
		final String customerFirstName = customerModel.getFirstName();
		final String customerLastName = customerModel.getLastName();
		String name = null;

		if (null != customerFirstName && null != customerLastName)
		{
			name = customerFirstName + " " + customerLastName;
		}
		else if (null != customerFirstName && null == customerLastName)
		{
			name = customerFirstName.trim();
		}
		else if (null == customerFirstName && null != customerLastName)
		{
			name = customerLastName.trim();
		}
		else
		{
			name = "";
		}


		if (null != emailId)
		{
			model.addObject(ModelAttributetConstants.EMAIL_ID, emailId);
		}
		else
		{
			model.addObject(ModelAttributetConstants.EMAIL_ID, "");
		}
		if (null != mobileNo)
		{
			model.addObject("mobileNo", mobileNo);
		}
		else
		{
			model.addObject("mobileNo", "");
		}
		model.addObject("name", name);

		return model;
	}
}
