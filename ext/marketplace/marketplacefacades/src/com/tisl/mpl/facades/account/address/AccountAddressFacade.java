package com.tisl.mpl.facades.account.address;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.marketplacecommerceservices.service.AccountAddressService;


/**
 * @author TCS
 *
 */
public class AccountAddressFacade
{
	@Resource
	private UserService userService;

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
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the addressReversePopulator
	 */
	public Populator<AddressData, AddressModel> getAddressReversePopulator()
	{
		return addressReversePopulator;
	}

	/**
	 * @param addressReversePopulator
	 *           the addressReversePopulator to set
	 */
	public void setAddressReversePopulator(final Populator<AddressData, AddressModel> addressReversePopulator)
	{
		this.addressReversePopulator = addressReversePopulator;
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

	/**
	 * @return the commerceCommonI18NService
	 */
	public CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	/**
	 * @param commerceCommonI18NService
	 *           the commerceCommonI18NService to set
	 */
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

	/**
	 * @return the addressConverter
	 */
	public Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}

	/**
	 * @param addressConverter
	 *           the addressConverter to set
	 */
	public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
	{
		this.addressConverter = addressConverter;
	}

	/**
	 * @return the accountAddressService
	 */
	public AccountAddressService getAccountAddressService()
	{
		return accountAddressService;
	}

	/**
	 * @param accountAddressService
	 *           the accountAddressService to set
	 */
	public void setAccountAddressService(final AccountAddressService accountAddressService)
	{
		this.accountAddressService = accountAddressService;
	}

	@Autowired
	private ModelService modelService;
	@Autowired
	private Populator<AddressData, AddressModel> addressReversePopulator;
	@Autowired
	private CustomerAccountService customerAccountService;
	@Autowired
	private CommerceCommonI18NService commerceCommonI18NService;
	@Autowired
	private Converter<AddressModel, AddressData> addressConverter;
	@Autowired
	private AccountAddressService accountAddressService;
	protected static final Logger LOG = Logger.getLogger(AccountAddressFacade.class);

	/**
	 * @description method is called to add an Address to the Customer's AddressBook
	 * @param newAddress
	 */
	public void addaddress(final AddressData newAddress)
	{

		try
		{
			validateParameterNotNullStandardMessage(MarketplacecommerceservicesConstants.ADDRESS_DATA, newAddress);
			LOG.debug(MarketplacecommerceservicesConstants.ADDRESTYPE_EQUALS_ADDADDRESS + newAddress.getAddressType());
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

			final boolean makeThisAddressTheDefault = newAddress.isDefaultAddress()
					|| (currentCustomer.getDefaultShipmentAddress() == null && newAddress.isVisibleInAddressBook());
			final AddressModel existingAddress = isDuplicateAddress(newAddress, currentCustomer);
			AddressModel addressmodel = null;
			if (null != existingAddress)
			{
				addressmodel = existingAddress;
			}
			else
			{
				addressmodel = modelService.create(AddressModel.class);
				addressReversePopulator.populate(newAddress, addressmodel);
				addressmodel.setCellphone(newAddress.getPhone());
				addressmodel.setDistrict(newAddress.getState());
				addressmodel.setAddressType(newAddress.getAddressType());
				addressmodel.setLocality(newAddress.getLocality());
				addressmodel.setAddressLine3(newAddress.getLine3());
			}

			customerAccountService.saveAddressEntry(currentCustomer, addressmodel);
			newAddress.setId(addressmodel.getPk().toString());

			if (makeThisAddressTheDefault)
			{
				customerAccountService.setDefaultAddressEntry(currentCustomer, addressmodel);
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}

	/**
	 * @param newAddress
	 * @param currentCustomer
	 * @return AddressModel
	 */
	private AddressModel isDuplicateAddress(final AddressData newAddress, final CustomerModel currentCustomer)
	{
		try
		{
			final StringBuilder fullNewAddress = new StringBuilder();
			if (null != newAddress.getFirstName())
			{
				fullNewAddress.append(newAddress.getFirstName().trim());
			}
			if (null != newAddress.getLastName())
			{
				fullNewAddress.append(newAddress.getLastName().trim());
			}
			if (null != newAddress.getLine1())
			{
				fullNewAddress.append(newAddress.getLine1().trim());
			}
			if (null != newAddress.getLine2())
			{
				fullNewAddress.append(newAddress.getLine2().trim());
			}
			if (null != newAddress.getLine3())
			{
				fullNewAddress.append(newAddress.getLine3().trim());
			}
			if (null != newAddress.getTown())
			{
				fullNewAddress.append(newAddress.getTown().trim());
			}
			if (null != newAddress.getState())
			{
				fullNewAddress.append(newAddress.getState().trim());
			}
			if (null != newAddress.getPostalCode())
			{
				fullNewAddress.append(newAddress.getPostalCode().trim());
			}
			if (null != newAddress.getPhone())
			{
				fullNewAddress.append(newAddress.getPhone().trim());
			}
			LOG.info(MarketplacecommerceservicesConstants.FULL_ADDRESS_FROM_FORM + fullNewAddress);

			for (final AddressModel addressModel : currentCustomer.getAddresses())
			{
				final StringBuilder fullAddress = new StringBuilder();
				if (null != addressModel.getFirstname())
				{
					fullAddress.append(addressModel.getFirstname().trim());
				}
				if (null != addressModel.getLastname())
				{
					fullAddress.append(addressModel.getLastname().trim());
				}
				if (null != addressModel.getStreetname())
				{
					fullAddress.append(addressModel.getStreetname().trim());
				}
				if (null != addressModel.getStreetnumber())
				{
					fullAddress.append(addressModel.getStreetnumber().trim());
				}
				if (null != addressModel.getAddressLine3())
				{
					fullAddress.append(addressModel.getAddressLine3().trim());
				}
				if (null != addressModel.getTown())
				{
					fullAddress.append(addressModel.getTown().trim());
				}
				if (null != addressModel.getDistrict())
				{
					fullAddress.append(addressModel.getDistrict().trim());
				}
				if (null != addressModel.getPostalcode())
				{
					fullAddress.append(addressModel.getPostalcode().trim());
				}
				if (null != addressModel.getPhone1())
				{
					fullAddress.append(addressModel.getPhone1().trim());
				}
				LOG.info(MarketplacecommerceservicesConstants.FULL_ADDRESS_FROM_MODEL + fullAddress);
				if (fullNewAddress.toString().equals(fullAddress.toString()))
				{
					return addressModel;
				}
			}
			return null;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to edit Address of the Customer
	 * @param addressData
	 */
	public void editAddress(final AddressData addressData)
	{
		try
		{
			LOG.debug(MarketplacecommerceservicesConstants.ADDRESTYPE_EQUALS_ADDADDRESS + addressData.getAddressType());
			validateParameterNotNullStandardMessage(MarketplacecommerceservicesConstants.ADDRESS_DATA, addressData);
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			final AddressModel addressModel = customerAccountService.getAddressForCode(currentCustomer, addressData.getId());
			addressModel.setRegion(null);
			addressReversePopulator.populate(addressData, addressModel);
			addressModel.setCellphone(addressData.getPhone());
			addressModel.setDistrict(addressData.getState());
			addressModel.setAddressType(addressData.getAddressType());
			addressModel.setLocality(addressData.getLocality());
			addressModel.setAddressLine3(addressData.getLine3());
			customerAccountService.saveAddressEntry(currentCustomer, addressModel);
			if (addressData.isDefaultAddress())
			{
				customerAccountService.setDefaultAddressEntry(currentCustomer, addressModel);
			}
			else if (addressModel.equals(currentCustomer.getDefaultShipmentAddress()))
			{
				customerAccountService.clearDefaultAddressEntry(currentCustomer);
			}
		}
		catch (final ConversionException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to get the AddressBook of the customer
	 * @return List
	 */
	public List<AddressData> getAddressBook()
	{
		try
		{
			final Collection<AddressModel> addresses = customerAccountService
					.getAddressBookDeliveryEntries((CustomerModel) userService.getCurrentUser());

			if (addresses != null && !addresses.isEmpty())
			{
				final Collection<CountryModel> deliveryCountries = commerceCommonI18NService.getAllCountries();

				final List<AddressData> result = new ArrayList<AddressData>();
				final AddressData defaultAddress = getDefaultAddress();

				for (final AddressModel address : addresses)
				{
					if (address.getCountry() != null)
					{
						final boolean validForSite = deliveryCountries != null && deliveryCountries.contains(address.getCountry());
						if (validForSite)
						{
							final AddressData addressData = addressConverter.convert(address);
							addressData.setPhone(address.getCellphone());
							addressData.setState(address.getDistrict());
							addressData.setAddressType(address.getAddressType());
							addressData.setLocality(address.getLocality());
							addressData.setLine3(address.getAddressLine3());

							if (defaultAddress != null && defaultAddress.getId() != null
									&& defaultAddress.getId().equals(addressData.getId()))
							{
								addressData.setDefaultAddress(true);
								result.add(0, addressData);
							}
							else
							{
								result.add(addressData);
							}
						}
					}
				}

				return result;
			}
			return Collections.emptyList();
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to get the Default Address of the Customer
	 * @return AddressData
	 */
	public AddressData getDefaultAddress()
	{
		try
		{
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			AddressData defaultAddressData = null;

			final AddressModel defaultAddress = customerAccountService.getDefaultAddress(currentCustomer);
			if (defaultAddress != null)
			{
				defaultAddressData = addressConverter.convert(defaultAddress);
				defaultAddressData.setPhone(defaultAddress.getCellphone());
				defaultAddressData.setState(defaultAddress.getDistrict());
				defaultAddressData.setAddressType(defaultAddress.getAddressType());
				defaultAddressData.setLocality(defaultAddress.getLocality());
				defaultAddressData.setLine3(defaultAddress.getAddressLine3());
			}
			else
			{
				final List<AddressModel> addresses = customerAccountService.getAddressBookEntries(currentCustomer);
				if (CollectionUtils.isNotEmpty(addresses))
				{
					defaultAddressData = addressConverter.convert(addresses.get(0));
					defaultAddressData.setPhone(addresses.get(0).getCellphone());
					defaultAddressData.setState(addresses.get(0).getDistrict());
					defaultAddressData.setAddressType(addresses.get(0).getAddressType());
					defaultAddressData.setLocality(addresses.get(0).getLocality());
					defaultAddressData.setLine3(addresses.get(0).getAddressLine3());
				}
			}
			return defaultAddressData;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to get the AddressCode
	 * @param code
	 * @return AddressData
	 */
	public AddressData getAddressForCode(final String code)
	{
		try
		{
			final AddressModel defaultAddress = customerAccountService.getAddressForCode(
					(CustomerModel) userService.getCurrentUser(), code);
			if (defaultAddress != null)
			{
				final AddressData addressData = addressConverter.convert(defaultAddress);
				addressData.setState(defaultAddress.getDistrict());
				addressData.setAddressType(defaultAddress.getAddressType());
				addressData.setLocality(defaultAddress.getLocality());
				addressData.setLine3(defaultAddress.getAddressLine3());
				return addressData;
			}
			return null;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @return List<StateData>
	 */
	public List<StateData> getStates()
	{
		try
		{
			List<StateData> stateList = new ArrayList<StateData>();
			stateList = accountAddressService.getStates();
			return stateList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}
}
