/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.converters.populator.AddressPopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;


/**
 * @author TCS
 *
 */
public class CustomAddressPopulator extends AddressPopulator
{

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerAccountService customerAccountService;

	@Override
	public void populate(final AddressModel source, final AddressData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		//DSC_006 : Fix for Checkout Address State display issue
		super.populate(source, target);
		target.setState(source.getDistrict());

		target.setAddressType(source.getAddressType()); //Added for checkout journey modification
		target.setDefaultAddress(isDefaultAddress(target)); //Added for checkout journey modification
		target.setLine3(source.getAddressLine3()); //Added for checkout journey modification
		target.setLocality(source.getLocality()); //Added for checkout journey modification
		if (null != source.getCity())
		{
			target.setCity(source.getCity());//added for stores
		}
		if (null != source.getLandmark())
		{
			target.setLandmark(source.getLandmark());//added for stores
		}

	}

	//Added for checkout journey modification
	public boolean isDefaultAddress(final AddressData target)
	{
		boolean result = false;
		//final AddressData addressData = getUserFacade().getDefaultAddress();
		if (userService.getCurrentUser() instanceof CustomerModel)
		{
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			final AddressModel defaultAddress = customerAccountService.getDefaultAddress(currentCustomer);
			result = (defaultAddress != null && defaultAddress.getPk() != null && defaultAddress.getPk().toString()
					.equalsIgnoreCase(target.getId())) ? true : false;
		}
		return result;
	}


	/**
	 * @return the userFacade
	 */
	public UserFacade getUserFacade()
	{
		return userFacade;
	}

	/**
	 * @param userFacade
	 *           the userFacade to set
	 */
	public void setUserFacade(final UserFacade userFacade)
	{
		this.userFacade = userFacade;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the customerAccountService
	 */
	public CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	/**
	 * @param customerAccountService
	 *           the customerAccountService to set
	 */
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

}
