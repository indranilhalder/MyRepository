/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.io.DataInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.event.SubmitRequestEvent;
import com.tisl.mpl.marketplacecommerceservices.service.ContactUsService;
import com.tisl.mpl.model.cms.components.ContactUsUserDetailsModel;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.web.forms.MplContactUsForm;
import com.tisl.mpl.storefront.web.forms.validator.MplContactUsFormValidator;
import com.tisl.mpl.util.ExceptionUtil;



/**
 * @author TCS
 *
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/contact")
public class ContactUsPageController extends AbstractPageController
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "mplContactUsFormValidator")
	private MplContactUsFormValidator mplContactUsFormValidator;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "eventService")
	private EventService eventService;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "defaultEmailService")
	private EmailService emailService;

	@Resource(name = "contactUsService")
	private ContactUsService contactUsService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;

	final static String CONTACT_PAGE_ID = "contact";

	final static String CONTACT_PAGE_BREADCRUMB = "Contact Us";

	private static final String RECAPTCHA_PUBLIC_KEY_PROPERTY = "recaptcha.publickey";

	private static final String LAST_LINK_CLASS = "active";

	/**
	 * @param model
	 * @return string
	 * @throws CMSItemNotFoundException
	 *            This method renders the contact us page with the contact us form
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getContactUsForm(final Model model) throws CMSItemNotFoundException
	{
		final MplContactUsForm contactUsForm = new MplContactUsForm();


		if (userService.getCurrentUser() != null)
		{
			final CustomerModel customer = (CustomerModel) userService.getCurrentUser();
			contactUsForm.setEmailId(customer.getOriginalUid());
		}


		model.addAttribute(contactUsForm);
		model.addAttribute("recaptchaKey", getCaptchaKey());
		final ContentPageModel contactUsPage = getContentPageForLabelOrId(CONTACT_PAGE_ID);
		storeCmsPageInModel(model, getContentPageForLabelOrId(CONTACT_PAGE_ID));

		model.addAttribute("metaRobots", "noindex,follow");
		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(contactUsPage.getKeywords());
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(contactUsPage.getDescription());
		setUpMetaData(model, metaKeywords, metaDescription);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				Collections.singletonList(new Breadcrumb("#", CONTACT_PAGE_BREADCRUMB, LAST_LINK_CLASS)));
		return getViewForPage(model);

	}

	/**
	 * @param mplContactUsForm
	 * @param bindingResult
	 * @param model
	 * @return string
	 * @throws CMSItemNotFoundException
	 * @throws IOException
	 *            This method validates the form,uploads the file and sends the email to contact center with the uploaded
	 *            details
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String submitForm(final MplContactUsForm mplContactUsForm, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request) throws CMSItemNotFoundException, IOException
	{
		try
		{
			mplContactUsFormValidator.validate(mplContactUsForm, bindingResult);

			if (bindingResult.hasErrors())
			{
				GlobalMessages.addErrorMessage(model, MessageConstants.FORM_GLOBAL_ERROR);
				model.addAttribute("error", Boolean.TRUE);
				model.addAttribute("recaptchaKey", getCaptchaKey());

				storeCmsPageInModel(model, getContentPageForLabelOrId(CONTACT_PAGE_ID));
				model.addAttribute("metaRobots", "noindex,follow");
				model.addAttribute(WebConstants.BREADCRUMBS_KEY,
						Collections.singletonList(new Breadcrumb("#", CONTACT_PAGE_BREADCRUMB, LAST_LINK_CLASS)));
				return getViewForPage(model);

			}
			else
			{
				final ContactUsUserDetailsModel userDetail = modelService.create(ContactUsUserDetailsModel.class);
				final String orderCode = mplContactUsForm.getOrderDetails();
				final SubmitRequestEvent submitRequestEvent = new SubmitRequestEvent();
				if (!orderCode.isEmpty())
				{
					userDetail.setOrderDetails(orderCode);
					submitRequestEvent.setOrderCode(orderCode);
				}

				if (!StringUtils.isEmpty(orderCode) && contactUsService.getOrderModel(orderCode).size() > 0)
				{
					final OrderModel order = contactUsService.getOrderModel(orderCode).get(0);
					submitRequestEvent.setCustomer((CustomerModel) order.getUser());
				}
				final String message = mplContactUsForm.getMessage();
				if (!message.isEmpty())
				{
					userDetail.setMessageDetails(message);
					submitRequestEvent.setMessage(message);
				}





				final String issueDetails = mplContactUsForm.getIssueDetails();
				final String customerEmailId = mplContactUsForm.getEmailId();
				final String issueType = request.getParameter("selectedTabsByName");


				if (!issueDetails.isEmpty())
				{
					submitRequestEvent.setIssueDetails(issueDetails);
				}

				if (!customerEmailId.isEmpty())
				{
					submitRequestEvent.setCustomerEmailId(customerEmailId);
				}
				if (!issueType.isEmpty())
				{
					submitRequestEvent.setIssueType(issueType);
				}




				final MultipartFile multipartFile = mplContactUsForm.getFile();

				if (!multipartFile.isEmpty())
				{
					//final String filePath = configurationService.getConfiguration().getString("contactus.file.path");
					final String uploadedFileName = mplContactUsForm.getFile().getOriginalFilename().toLowerCase();
					String fileName = null;
					if (!uploadedFileName.isEmpty() && uploadedFileName.lastIndexOf('.') > 0)
					{
						final int index = uploadedFileName.lastIndexOf('.');
						fileName = uploadedFileName.substring(0, index) + "_" + new Timestamp(System.currentTimeMillis()) + "."
								+ uploadedFileName.substring(index + 1);

					}

					final EmailAttachmentModel emailAttachment = emailService.createEmailAttachment(
							new DataInputStream(multipartFile.getInputStream()), fileName, "application/octet-stream");
					userDetail.setUploadDetails(fileName);
					submitRequestEvent.setAttachedFileName(emailAttachment.getCode());
				}

				modelService.save(userDetail);
				GlobalMessages.addInfoMessage(model, "contact.submit.success");
				submitRequestEvent.setSite(baseSiteService.getCurrentBaseSite());
				eventService.publishEvent(submitRequestEvent);

			}
			return getContactUsForm(model);


		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.CONTACT_SYSTEM_ERROR);

		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.B2002));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.CONTACT_SYSTEM_ERROR);

		}

	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String redirect()
	{
		return REDIRECT_PREFIX + "/contact";
	}

	/**
	 * @return captcha key This method fetches the captcha key from local.properties
	 */
	private String getCaptchaKey()
	{
		final String recaptchaKey = configurationService.getConfiguration().getString(RECAPTCHA_PUBLIC_KEY_PROPERTY);

		return recaptchaKey;
	}
}
