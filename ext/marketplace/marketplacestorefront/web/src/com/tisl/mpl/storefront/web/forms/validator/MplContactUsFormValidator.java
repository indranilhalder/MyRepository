/**
 *
 */
package com.tisl.mpl.storefront.web.forms.validator;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.tisl.mpl.marketplacecommerceservices.service.ContactUsService;
import com.tisl.mpl.storefront.web.forms.MplContactUsForm;


/**
 * @author TCS
 *
 */
@Component("mplContactUsFormValidator")
public class MplContactUsFormValidator implements Validator
{
	public static final String FILE_PATTERN = "(.*?).(doc|docx|jpg|jpeg|png|bmp)$";
	//public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

	@Resource(name = "contactUsService")
	private ContactUsService contactUsService;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(final Class<?> aClass)
	{
		// YTODO Auto-generated method stub
		return MplContactUsForm.class.equals(aClass);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(final Object object, final Errors errors)
	{
		// YTODO Auto-generated method stub

		final MplContactUsForm contactUsForm = (MplContactUsForm) object;
		final String orderCode = contactUsForm.getOrderDetails();
		final String emailId = contactUsForm.getEmailId();
		//final String message = contactUsForm.getMessage();
		final MultipartFile file = contactUsForm.getFile();


		if (!orderCode.isEmpty() && contactUsService.getOrderModel(orderCode).size() == 0)
		{

			errors.rejectValue("orderDetails", "contact.order.invalid");


		}

		if (!orderCode.isEmpty() && contactUsService.getOrderModel(orderCode).size() > 0)
		{
			final OrderModel order = contactUsService.getOrderModel(orderCode).get(0);
			final CustomerModel customer = (CustomerModel) order.getUser();
			if (!customer.getOriginalUid().equalsIgnoreCase(emailId))
			{
				errors.rejectValue("emailId", "contact.email.invalid");
			}
		}

		if (file != null && file.getSize() > 1000000L)
		{
			errors.rejectValue("file", "contact.file.sizeError");
		}

		if (file != null && !file.getOriginalFilename().isEmpty()
				&& !file.getOriginalFilename().toLowerCase().matches(FILE_PATTERN))
		{

			errors.rejectValue("file", "contact.file.formatError");
		}
	}
}
