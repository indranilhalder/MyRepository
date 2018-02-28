/**
 * 
 */
package com.tisl.mpl.storefront.web.forms.validator;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.storefront.web.forms.EgvDetailForm;


/**
 * @author Pankaj
 *
 */
@Component("mplEgvFormValidator")
public class MplEgvFormValidator
{

	public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
	@Autowired
	private ConfigurationService configurationService;
	


	public boolean validateEmailAddress(final String email)
	{
		final Pattern pattern = Pattern.compile(EMAIL_REGEX);
		final Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}



	public boolean validate(final EgvDetailForm egvDetailForm) throws UnsupportedEncodingException
	{
		return validateStandardFields(egvDetailForm);
	}

	private boolean validateStandardFields(final EgvDetailForm egvDetailForm) throws UnsupportedEncodingException
	{
		boolean validateFlag = true;

		if (egvDetailForm != null)
		{
			 int minPrice=configurationService.getConfiguration().getInt("mpl.buyingEgv.minPrice");	
			 int maxPrice=configurationService.getConfiguration().getInt("mpl.buyingEgv.maxPrice");	

			if (egvDetailForm.getGiftRange() >= minPrice && egvDetailForm.getGiftRange() <= maxPrice)
			{
				validateFlag = true;
			}
			else
			{
				return false;
			}

			if (validateEmailAddress(egvDetailForm.getToEmailAddress()))
			{
				validateFlag = true;
			}
			else
			{
				return false;
			}
			
		}
		else
		{
			return false;
		}


		return validateFlag;

	}

}
