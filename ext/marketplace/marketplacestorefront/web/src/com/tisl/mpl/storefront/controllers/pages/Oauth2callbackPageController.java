/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */

package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractLoginPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.LoginForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.product.data.ExtRegisterData;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FBConnection;
import com.tisl.mpl.storefront.controllers.helpers.FBGraph;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.controllers.helpers.GoogleAuthHelper;
import com.tisl.mpl.storefront.security.cookie.LuxuryEmailCookieGenerator;
import com.tisl.mpl.storefront.web.forms.ExtRegisterForm;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * Controller for home page
 */
@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(RequestMappingUrlConstants.LINK_OAUTH2_CALLBACK)
public class Oauth2callbackPageController extends AbstractLoginPageController
{
	private static final String UTF_8 = "UTF-8"; //Sonar fix
	// CMS Pages
	@Resource(name = ModelAttributetConstants.ACCOUNT_BREADCRUMB_BUILDER)
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;
	//	@Resource(name = ModelAttributetConstants.SIMPLE_BREADCRUMB_BUILDER)
	//	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;
	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;
	@Autowired
	private RegisterCustomerFacade registerCustomerFacade;
	@Autowired
	private SessionService session;
	private static final String OAuth_2_CMS_PAGE = "oauth2callback";
	private HttpSessionRequestCache httpSessionRequestCache;

	@Autowired
	private LuxuryEmailCookieGenerator luxuryEmailCookieGenerator;

	@Resource(name = ModelAttributetConstants.HTTP_SESSION_REQUEST_CACHE)
	public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache)
	{
		this.httpSessionRequestCache = accHttpSessionRequestCache;
	}

	@Autowired
	private CartFacade cartFacade;

	protected static final String REDIRECT_URL_CHOOSE_DELIVERY_METHOD = "/checkout/multi/delivery-method/choose";

	private static final Logger LOG = Logger.getLogger(Oauth2callbackPageController.class);

	/**
	 * @description This method is called to call the social media (Facebook or Google) to get the respective user id for
	 *              social registration and login
	 * @param referer
	 * @param form
	 * @param bindingResult
	 * @param model
	 * @param request
	 * @param response
	 * @param redirectModel
	 * @return String
	 * @throws CMSItemNotFoundException
	 * @throws IOException
	 * @throws JSONException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String oauth2callback(@RequestHeader(value = ModelAttributetConstants.REFERER, required = false) final String referer,
			final ExtRegisterForm form, final BindingResult bindingResult, final Model model, final HttpServletRequest request,
			final HttpServletResponse response, final RedirectAttributes redirectModel) throws CMSItemNotFoundException,
			IOException, JSONException
	{
		try
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(OAuth_2_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(OAuth_2_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs(null));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);

			final GoogleAuthHelper googleAuthHelper = new GoogleAuthHelper();
			final FBConnection fbConnection = new FBConnection();

			if (request.getParameter(ModelAttributetConstants.CODE) == null
					&& request.getParameter(ModelAttributetConstants.STATE) == null)
			{
				request.getSession().setAttribute(ModelAttributetConstants.STATE, googleAuthHelper.getStateToken());
				session.setAttribute(ModelAttributetConstants.STATE, googleAuthHelper.getStateToken());
				model.addAttribute(ModelAttributetConstants.URL_VISIT, googleAuthHelper.buildLoginUrl());
				session.setAttribute(ModelAttributetConstants.STATE, googleAuthHelper.getStateToken());
				model.addAttribute(ModelAttributetConstants.URL_VISIT_FOR_FACEBOOK, fbConnection.getFBAuthUrl());
			}
			else if (request.getParameter(ModelAttributetConstants.CODE) != null
					&& request.getParameter(ModelAttributetConstants.STATE) != null
					&& request.getParameter(ModelAttributetConstants.STATE).equals(
							request.getSession().getAttribute(ModelAttributetConstants.STATE)))
			{
				//Google code
				request.getSession().removeAttribute(ModelAttributetConstants.STATE);
				session.removeAttribute(ModelAttributetConstants.STATE);
				final String userInfo = googleAuthHelper.getUserInfoJson(request.getParameter(ModelAttributetConstants.CODE));
				final JSONObject jsonObject = new JSONObject(userInfo);

				// get a String from the JSON object

				final String email = (String) jsonObject.get(ModelAttributetConstants.EMAIL);

				if (email != null && !email.isEmpty())
				{
					model.addAttribute(ModelAttributetConstants.EMAIL, email);
					form.setEmail(email);
				}
				final String socialLogin = ModelAttributetConstants.GOOGLE;
				session.setAttribute(ModelAttributetConstants.SOCIAL_LOGIN, ModelAttributetConstants.SOCIAL_LOGIN);
				//return processRegisterUserRequestForOAuth2(referer, form, bindingResult, model, request, response, redirectModel,socialLogin);

				return processRegisterUserRequestForOAuth2(form, bindingResult, model, request, response, socialLogin);
			}
			else if (request.getParameter(ModelAttributetConstants.CODE) != null)
			{
				//Facebook Code
				final String accessToken = fbConnection.getAccessToken(request.getParameter(ModelAttributetConstants.CODE));

				final FBGraph fbGraph = new FBGraph(accessToken);
				final String graph = fbGraph.getFBGraph();
				final Map<String, String> fbProfileData = fbGraph.getGraphData(graph);

				final String fbEmail = fbProfileData.get(ModelAttributetConstants.EMAIL);
				if (fbEmail != null && !fbEmail.isEmpty())
				{
					model.addAttribute(ModelAttributetConstants.ID, fbEmail);
					form.setEmail(fbEmail);
				}
				final String socialLogin = ModelAttributetConstants.FACEBOOK;
				session.setAttribute(ModelAttributetConstants.SOCIAL_LOGIN, ModelAttributetConstants.SOCIAL_LOGIN);
				return processRegisterUserRequestForOAuth2(form, bindingResult, model, request, response, socialLogin);
			}
			return ControllerConstants.Views.Pages.Oauth2callback.oauth2callback;
		}
		catch (final IllegalArgumentException e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0012));
			session.removeAttribute(ModelAttributetConstants.SOCIAL_LOGIN);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			session.removeAttribute(ModelAttributetConstants.SOCIAL_LOGIN);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			session.removeAttribute(ModelAttributetConstants.SOCIAL_LOGIN);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}

	}

	/**
	 * Gigya Social Login
	 *
	 *
	 */
	@ResponseBody
	@RequestMapping(value = MarketplacecheckoutaddonConstants.SOCIALLOGIN, method = RequestMethod.GET)
	public String socialLogin(@RequestParam(ModelAttributetConstants.REFERER) final String referer,
			@RequestParam(ModelAttributetConstants.EMAIL_ID) final String emailId,
			@RequestParam(ModelAttributetConstants.F_NAME) final String fName,
			@RequestParam(ModelAttributetConstants.L_NAME) final String lName,
			@RequestParam(ModelAttributetConstants.UID) final String uid,
			@RequestParam(ModelAttributetConstants.TIMESTAMP) final String timestamp,
			@RequestParam(ModelAttributetConstants.SIGNATURE) final String signature,
			@RequestParam(ModelAttributetConstants.PROVIDER) final String provider,
			@RequestParam(value = ModelAttributetConstants.GENDER, required = false) final String gender,
			final ExtRegisterForm form, final BindingResult bindingResult, final Model model, final HttpServletRequest request,
			final HttpServletResponse response, final RedirectAttributes redirectModel) throws CMSItemNotFoundException, IOException

	{

		try
		{
			LOG.debug("Method socialLogin, REFERER " + referer);
			LOG.debug("Method socialLogin, EMAIL ID " + emailId);
			LOG.debug("Method socialLogin, FIRST NAME " + fName);
			LOG.debug("Method socialLogin,LAST NAME " + lName);
			LOG.debug("Method socialLogin, UID " + uid);
			LOG.debug("Method socialLogin, TIMESTAMP " + timestamp);
			LOG.debug("Method socialLogin, SIGNATURE " + signature);
			LOG.debug("Method socialLogin,PROVIDER " + provider);
			LOG.debug("Method socialLogin, GENDER " + gender);


			final String fbEmail = emailId;

			if (fbEmail != null && !fbEmail.isEmpty())
			{
				model.addAttribute(ModelAttributetConstants.ID, fbEmail);
				form.setEmail(fbEmail);
			}
			if (StringUtils.isNotEmpty(fName))
			{
				form.setFirstName(fName);
			}
			if (StringUtils.isNotEmpty(lName))
			{
				form.setLastName(lName);
			}

			if (StringUtils.isNotEmpty(gender))
			{
				form.setGender(gender);
			}


			String socialLogin = "";

			if (provider.equalsIgnoreCase("googleplus"))
			{
				socialLogin = ModelAttributetConstants.GOOGLE;
			}
			else if (provider.equalsIgnoreCase("facebook"))
			{
				socialLogin = ModelAttributetConstants.FACEBOOK;
			}

			session.setAttribute(ModelAttributetConstants.SOCIAL_LOGIN, ModelAttributetConstants.SOCIAL_LOGIN);

			if (referer != null)
			{
				storeReferer(referer, request, response);
			}

			return processRegisterUserRequestForOAuth2(form, bindingResult, model, request, response, socialLogin);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}


		return ControllerConstants.Views.Pages.Oauth2callback.oauth2callback;
	}

	/**
	 * @description this method is for populate CMS page for Oauth2Callback
	 * @return AbstractPageModel
	 */
	@Override
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId(OAuth_2_CMS_PAGE);
	}


	/**
	 * @description This method is called to redirect the user to my account after successful registration using social
	 *              media
	 * @return String
	 */
	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		if (httpSessionRequestCache.getRequest(request, response) != null)
		{
			return httpSessionRequestCache.getRequest(request, response).getRedirectUrl();
		}


		if (cartFacade.hasEntries())
		{
			return REDIRECT_URL_CHOOSE_DELIVERY_METHOD;
		}

		return RequestMappingUrlConstants.LINK_MY_ACCOUNT;
	}

	/**
	 * @description this method returns the oauth2callback jsp page
	 * @return String
	 */
	@Override
	protected String getView()
	{
		return ControllerConstants.Views.Pages.Oauth2callback.oauth2callback;
	}

	/**
	 * @description this method is internally called for registration using social media
	 * @param form
	 * @param bindingResult
	 * @param model
	 * @param request
	 * @param response
	 * @param redirectModel
	 * @param socialLogin
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	/*
	 * private String processRegisterUserRequestForOAuth2(final RegisterForm form, final BindingResult bindingResult,
	 * final Model model, final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes
	 * redirectModel, final String socialLogin) throws CMSItemNotFoundException
	 */

	private String processRegisterUserRequestForOAuth2(final ExtRegisterForm form, final BindingResult bindingResult,
			final Model model, final HttpServletRequest request, final HttpServletResponse response, final String socialLogin)
			throws CMSItemNotFoundException
	{
		try
		{
			if (bindingResult.hasErrors())
			{
				model.addAttribute(form);
				model.addAttribute(new LoginForm());
				model.addAttribute(new GuestForm());
				GlobalMessages.addErrorMessage(model, MessageConstants.FORM_GLOBAL_ERROR);
				return handleRegistrationError(model);
			}
			ExtRegisterData data = null;
			data = new ExtRegisterData();
			data.setLogin(form.getEmail());
			data.setFirstName(form.getFirstName());
			data.setLastName(form.getLastName());
			data.setGender(form.getGender());
			if (StringUtils.isNotEmpty(socialLogin) && socialLogin.equalsIgnoreCase(ModelAttributetConstants.FACEBOOK))
			{
				data.setSocialMediaType(ModelAttributetConstants.FACEBOOK);
			}
			else if (StringUtils.isNotEmpty(socialLogin) && socialLogin.equalsIgnoreCase(ModelAttributetConstants.GOOGLE))
			{
				data.setSocialMediaType(ModelAttributetConstants.GOOGLE);
			}

			//data.setUid(getGigyaUID());//to-do
			final boolean isMobile = false;

			LOG.debug("Method processRegisterUserRequestForOAuth2, isMobile " + isMobile);
			LOG.debug("Method processRegisterUserRequestForOAuth2, EMAIL " + form.getEmail());
			LOG.debug("Method processRegisterUserRequestForOAuth2, PASSWORD  " + data.getPassword());

			data = registerCustomerFacade.registerSocial(data, isMobile);

			if (null != data)
			{
				getAutoLoginStrategy().login(form.getEmail().toLowerCase(), data.getPassword(), request, response);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}

		session.setAttribute(ModelAttributetConstants.SOCIAL_LOGIN_C, socialLogin);
		luxuryEmailCookieGenerator.addCookie(response, encrypt(form.getEmail()));
		return getSuccessRedirect(request, response);
	}

	/**
	 * @param originalUid
	 * @return
	 */
	private String encrypt(final String originalUid)
	{
		final String encryptionKey = "encryptor key";
		final String encryptedText = encryptAES(originalUid.trim(), encryptionKey);

		LOG.debug("String to Encrypt: " + originalUid);
		LOG.debug("Encrypted: " + encryptedText);
		return encryptedText;
	}

	/**
	 * @param trim
	 */
	private String encryptAES(final String encryptionText, final String key)
	{
		String encryptedText = null;
		try
		{

			final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

			cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));

			encryptedText = Base64.encodeBase64String(cipher.doFinal(encryptionText.getBytes(UTF_8)));


		}
		catch (final Exception e)
		{

			LOG.error("Error while encrypting: " + e.toString());
		}
		return encryptedText;


	}

	private SecretKeySpec getSecretKey(final String encryptionKey)
	{
		MessageDigest sha = null;
		byte[] key = null;
		try
		{

			key = encryptionKey.getBytes(UTF_8);

			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit
			LOG.debug("Key Length" + key.length);

			LOG.debug(new String(key, UTF_8));




		}
		catch (final NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new SecretKeySpec(key, "AES");
	}

	protected void storeReferer(final String referer, final HttpServletRequest request, final HttpServletResponse response)
	{
		LOG.debug("Method storeReferer, REFERER " + referer);

		if (StringUtils.isNotBlank(referer) && !StringUtils.endsWith(referer, RequestMappingUrlConstants.LINK_LOGIN))
		{
			httpSessionRequestCache.saveRequest(request, response);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/socialLoginAjax", method = RequestMethod.GET)
	public String socialLoginNew(@RequestParam(ModelAttributetConstants.REFERER) final String referer,
			@RequestParam(ModelAttributetConstants.EMAIL_ID) final String emailId,
			@RequestParam(ModelAttributetConstants.F_NAME) final String fName,
			@RequestParam(ModelAttributetConstants.L_NAME) final String lName, @RequestParam("token") final String token,
			@RequestParam(ModelAttributetConstants.PROVIDER) final String provider, final ExtRegisterForm form,
			final BindingResult bindingResult, final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws CMSItemNotFoundException, IOException

	{

		try
		{
			LOG.debug("Method socialLogin, REFERER " + referer);
			LOG.debug("Method socialLogin, EMAIL ID " + emailId);
			LOG.debug("Method socialLogin, FIRST NAME " + fName);
			LOG.debug("Method socialLogin,LAST NAME " + lName);
			LOG.debug("Method socialLogin, TOKEN " + token);
			LOG.debug("Method socialLogin,PROVIDER " + provider);


			if (StringUtils.isNotEmpty(emailId))
			{
				model.addAttribute(ModelAttributetConstants.ID, emailId);
				form.setEmail(emailId);
			}
			if (StringUtils.isNotEmpty(fName))
			{
				form.setFirstName(fName);
			}
			else
			{
				form.setFirstName(MarketplacecommerceservicesConstants.SINGLE_SPACE);
			}
			if (StringUtils.isNotEmpty(lName))
			{
				form.setLastName(lName);
			}
			else
			{
				form.setLastName(MarketplacecommerceservicesConstants.SINGLE_SPACE);
			}


			String socialLogin = "";

			if (provider.equalsIgnoreCase("googleplus"))
			{
				socialLogin = ModelAttributetConstants.GOOGLE;
			}
			else if (provider.equalsIgnoreCase("facebook"))
			{
				socialLogin = ModelAttributetConstants.FACEBOOK;
			}

			session.setAttribute(ModelAttributetConstants.SOCIAL_LOGIN, ModelAttributetConstants.SOCIAL_LOGIN);

			if (referer != null)
			{
				storeReferer(referer, request, response);
			}
			return processRegisterUserRequestForOAuth2(form, bindingResult, model, request, response, socialLogin);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}


		return ControllerConstants.Views.Pages.Oauth2callback.oauth2callback;
	}

}