/**
 *
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * @author TCS
 *
 */



public class CustomerUpdateEmailContext extends AbstractEmailContext<StoreFrontCustomerProcessModel>
{




	private static final String EMAIL_ID = "emailId";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String NICK_NAME = "nickName";
	private static final String MOBILE_NUMBER = "mobileNumber";
	private static final String DATE_OF_BIRTH = "dob";
	private static final String DATE_OF_ANNIVERSARY = "doa";
	private static final String GENDER = "gender";
	private static final String DATE_UPDATE = "dateOfUpdate";
	private static final String LINK_MYACCOUNT = "linkMyAccount";
	private static final String PASSWORD = "pwd";

	private static final String IS_FIRST = "isFirst";
	private static final String IS_EMAIL_ID = "isEmailId";
	private static final String IS_FIRST_NAME = "isFirstName";
	private static final String IS_LAST_NAME = "isLastName";
	private static final String IS_NICK_NAME = "isNickName";
	private static final String IS_MOBILE_NUMBER = "isMobileNumber";
	private static final String IS_DATE_OF_BIRTH = "isDateOfBirth";
	private static final String IS_DATE_OF_ANNIVERSARY = "isDateOfAnniversary";
	private static final String IS_GENDER = "isGender";
	private static final String IS_PWD = "isPwd";


	private Converter<UserModel, CustomerData> customerConverter;
	private CustomerData customerData;
	@Autowired
	private SessionService sessionService;

	@SuppressWarnings("unused")
	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{


		super.init(storeFrontCustomerProcessModel, emailPageModel);
		customerData = getCustomerConverter().convert(getCustomer(storeFrontCustomerProcessModel));
		final CustomerModel customer = getCustomer(storeFrontCustomerProcessModel);
		final String displayName = customer.getDisplayName();
		int count = 0;
		if (StringUtils.isEmpty(displayName))
		{
			put(DISPLAY_NAME, displayName);
		}
		put(EMAIL, storeFrontCustomerProcessModel.getCustomer().getOriginalUid());

		if (null != getCustomer(storeFrontCustomerProcessModel))
		{
			final List<String> updatedDetailListForVm = new ArrayList<String>();
			final List<String> updatedDetailList = storeFrontCustomerProcessModel.getCustomerProfileList();
			if (getCustomer(storeFrontCustomerProcessModel) != null)
			{
				for (final String key : updatedDetailList)
				{
					if (key.equalsIgnoreCase(MarketplacecommerceservicesConstants.EMAIL_ID_suffix))
					{
						put(IS_EMAIL_ID, Boolean.TRUE);
						if (count > 0)
						{
							put(EMAIL_ID, ",Email ID");
							updatedDetailListForVm.add(",Email ID");
						}
						else
						{
							put(IS_FIRST, Boolean.TRUE);
							put(EMAIL_ID, "Email ID");
							updatedDetailListForVm.add(0, "Email ID");
						}
						count = count + 1;
					}
					else if (key.equalsIgnoreCase(MarketplacecommerceservicesConstants.F_NAME_suffix))
					{
						put(IS_FIRST_NAME, Boolean.TRUE);
						if (count > 0)
						{
							put(FIRST_NAME, ",First Name");
							updatedDetailListForVm.add(",First Name");
						}
						else
						{
							put(IS_FIRST, Boolean.TRUE);
							put(FIRST_NAME, "First Name");
							updatedDetailListForVm.add(0, "First Name");
						}
						count = count + 1;
					}
					else if (key.equalsIgnoreCase(MarketplacecommerceservicesConstants.L_NAME_suffix))
					{
						put(IS_LAST_NAME, Boolean.TRUE);
						if (count > 0)
						{
							put(LAST_NAME, ",Last Name");
							updatedDetailListForVm.add(",Last Name");
						}
						else
						{
							put(IS_FIRST, Boolean.TRUE);
							put(LAST_NAME, "Last Name");
							updatedDetailListForVm.add(0, "Last Name");
						}
						count = count + 1;
					}
					else if (key.equalsIgnoreCase(MarketplacecommerceservicesConstants.N_NAME_suffix))
					{
						put(IS_NICK_NAME, Boolean.TRUE);
						if (count > 0)
						{
							put(NICK_NAME, ",Nick Name");
							updatedDetailListForVm.add(",Nick Name");
						}
						else
						{
							put(IS_FIRST, Boolean.TRUE);
							put(NICK_NAME, "Nick Name");
							updatedDetailListForVm.add(0, "Nick Name");
						}
						count = count + 1;
					}
					else if (key.equalsIgnoreCase(MarketplacecommerceservicesConstants.D_OF_BIRTH_suffix))
					{
						put(IS_DATE_OF_BIRTH, Boolean.TRUE);
						if (count > 0)
						{
							put(DATE_OF_BIRTH, ",Date Of Birth");
							updatedDetailListForVm.add(",Date Of Birth");
						}
						else
						{
							put(IS_FIRST, Boolean.TRUE);
							put(DATE_OF_BIRTH, "Date Of Birth");
							updatedDetailListForVm.add(0, "Date Of Birth");
						}
						count = count + 1;
					}
					else if (key.equalsIgnoreCase(MarketplacecommerceservicesConstants.D_OF_ANNIVERSARY_suffix))
					{
						put(IS_DATE_OF_ANNIVERSARY, Boolean.TRUE);
						if (count > 0)
						{
							put(DATE_OF_ANNIVERSARY, ",Date Of Anniversary");
							updatedDetailListForVm.add(",Date Of Anniversary");
						}
						else
						{
							put(IS_FIRST, Boolean.TRUE);
							put(DATE_OF_ANNIVERSARY, "Date Of Anniversary");
							updatedDetailListForVm.add(0, "Date Of Anniversary");
						}
						count = count + 1;
					}
					else if (key.equalsIgnoreCase(MarketplacecommerceservicesConstants.M_NUMBER_suffix))
					{
						put(IS_MOBILE_NUMBER, Boolean.TRUE);
						if (count > 0)
						{
							put(MOBILE_NUMBER, ",Mobile Number");
							updatedDetailListForVm.add(",Mobile Number");
						}
						else
						{
							put(IS_FIRST, Boolean.TRUE);
							put(MOBILE_NUMBER, "Mobile Number");
							updatedDetailListForVm.add(0, "Mobile Number");
						}
						count = count + 1;
					}
					else if (key.equalsIgnoreCase(MarketplacecommerceservicesConstants.GENDER_suffix))
					{
						put(IS_GENDER, Boolean.TRUE);
						if (count > 0)
						{
							put(GENDER, ",Gender");
							updatedDetailListForVm.add(",Gender");
						}
						else
						{
							put(IS_FIRST, Boolean.TRUE);
							put(GENDER, "Gender");
							updatedDetailListForVm.add(0, "Gender");
						}
						count = count + 1;
					}
					else if (key.equalsIgnoreCase(MarketplacecommerceservicesConstants.PASSWORD_suffix))
					{
						put(IS_PWD, Boolean.TRUE);
						if (count > 0)
						{
							put(PASSWORD, ",Password");
							updatedDetailListForVm.add(",Password");
						}
						else
						{
							put(IS_FIRST, Boolean.TRUE);
							put(PASSWORD, "Password");
							updatedDetailListForVm.add(0, "Password");
						}
						count = count + 1;
					}
				}
				put("updatedDetailListForVm", updatedDetailListForVm);
				final DateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy  hh:mm a");
				final Date date = new Date();
				final String UpdateDate = dateFormat.format(date);

				put(DATE_UPDATE, UpdateDate);

				put(LINK_MYACCOUNT, storeFrontCustomerProcessModel.getCustomerUpdateProfileURL());
				put(EMAIL, customerData.getDisplayUid());

			}
		}
		sessionService.removeAttribute("updatedDetailList");
	}

	@Override
	protected BaseSiteModel getSite(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel)
	{
		return storeFrontCustomerProcessModel.getSite();
	}

	@Override
	protected CustomerModel getCustomer(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel)
	{
		return storeFrontCustomerProcessModel.getCustomer();
	}

	protected Converter<UserModel, CustomerData> getCustomerConverter()
	{
		return customerConverter;
	}

	@Required
	public void setCustomerConverter(final Converter<UserModel, CustomerData> customerConverter)
	{
		this.customerConverter = customerConverter;
	}

	public CustomerData getCustomer()
	{
		return customerData;
	}


	@Override
	protected LanguageModel getEmailLanguage(final StoreFrontCustomerProcessModel businessProcessModel)
	{
		return businessProcessModel.getLanguage();
	}
}
