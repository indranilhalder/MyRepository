/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.model.StateModel;
import com.tisl.mpl.xml.pojo.MplCustomerWsData;



/**
 * @author TCS
 *
 */

@SuppressWarnings(
{ "PMD" })
public class MplCustomerWebServiceImpl implements MplCustomerWebService
{
	private static final Logger LOG = Logger.getLogger(MplCustomerWebServiceImpl.class);

	@Resource(name = "mplClientAccountAddressService")
	private MplClientAccountAddressService mplClientAccountAddressService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CatalogVersionService catalogVersionService;
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MarketplacecclientservicesConstants.DMY_DATE_FORMAT);

	/**
	 * @param customerWsData
	 * @description method is called to create CRM Customer
	 */
	@Override
	public void customerDataToCRM(final MplCustomerWsData customerWsData)
	{

		final Client client = Client.create();
		WebResource webResource = null;
		Marshaller marshaller = null;
		String xmlString;
		ClientResponse response = null;
		final StringWriter stringWriter = new StringWriter();
		LOG.debug("Realtime call CRM : inside customerDataToCRM");
		if (null != client && null != configurationService)
		{
			final String password = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_PASSWORD);
			final String userId = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_USERID);
			client.addFilter(new HTTPBasicAuthFilter(userId, password));
			webResource = client.resource(UriBuilder.fromUri(configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.CRM_CUSTOMER_CREATEUPDATE_URL)).build());
		}

		try
		{
			final JAXBContext context = JAXBContext.newInstance(MplCustomerWsData.class);

			if (null != context)
			{
				marshaller = context.createMarshaller();

			}
			if (null != marshaller)
			{
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			}

			if (null != marshaller)
			{
				marshaller.marshal(customerWsData, stringWriter);
			}
			xmlString = stringWriter.toString();
			LOG.debug(xmlString);
			if (null != webResource)
			{
				response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(xmlString)
						.post(ClientResponse.class);
			}
			LOG.debug(response);
		}

		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);

		}
	}

	/**
	 * @param customerModel
	 * @param create_update_flag
	 *           , can be I-insert/U-update.
	 * @description method is called to update details of CRM customer
	 */
	@Override
	public void customerModeltoWsData(final CustomerModel customerModel, final String create_update_flag,
			final boolean isBlackListed)
	{
		final MplCustomerWsData customer = new MplCustomerWsData();
		final MplCustomerWsData.CustomerCreateUpdate customerCreateUpdate = new MplCustomerWsData.CustomerCreateUpdate();
		MplCustomerWsData.CustomerCreateUpdate.CommPreferences commPreference = new MplCustomerWsData.CustomerCreateUpdate.CommPreferences();
		MplCustomerWsData.CustomerCreateUpdate.Subscription subscription = new MplCustomerWsData.CustomerCreateUpdate.Subscription();
		MplCustomerWsData.CustomerCreateUpdate.Subscription.Categories categoryWS = null;
		List<MplCustomerWsData.CustomerCreateUpdate.Subscription.Categories> categoryWSList = new ArrayList<>();
		final List<MplCustomerWsData.CustomerCreateUpdate> customerCreateUpdateList = customer.getCustomerCreateUpdate();
		try
		{
			LOG.debug("Inside customer model to wsdata");
			customerCreateUpdate.setUpSertFlag(create_update_flag.toUpperCase());
			if (null != customerModel)
			{
				if (StringUtils.isNotEmpty(customerModel.getUid()))
				{
					customerCreateUpdate.setCustomerID(Integer.valueOf(customerModel.getUid()));
					LOG.debug("customer id" + customerModel.getUid());
				}
				if (StringUtils.isNotEmpty(customerModel.getOriginalUid()))
				{
					customerCreateUpdate.setEmailID(customerModel.getOriginalUid());
				}
				if (StringUtils.isNotEmpty(create_update_flag)
						&& create_update_flag.equalsIgnoreCase(MarketplacecclientservicesConstants.CUSTOMER_UPDATE_INDICATOR))
				{
					if (isBlackListed) //isBlackListed == true
					{
						customerCreateUpdate.setIsBlackListed(MarketplacecclientservicesConstants.X);
					}
					if (StringUtils.isNotEmpty(customerModel.getFirstName()))
					{
						customerCreateUpdate.setFirstName(customerModel.getFirstName());
					}

					if (StringUtils.isNotEmpty(customerModel.getLastName()))
					{
						customerCreateUpdate.setLastName(customerModel.getLastName());
					}
					if (null != customerModel.getDateOfBirth())
					{
						customerCreateUpdate.setDateOfBirth(simpleDateFormat.format(customerModel.getDateOfBirth()));
					}
					if (null != customerModel.getDateOfAnniversary())
					{
						customerCreateUpdate.setDateOfMarriage(simpleDateFormat.format(customerModel.getDateOfAnniversary()));
					}
					if (null != customerModel.getGender())
					{
						final Gender gender = customerModel.getGender();
						if (null != gender && StringUtils.isNotEmpty(gender.getCode()))
						{
							if (gender.getCode().equalsIgnoreCase(MarketplacecclientservicesConstants.MALE))
							{
								customerCreateUpdate.setGender(MarketplacecclientservicesConstants.M);
							}
							if (gender.getCode().equalsIgnoreCase(MarketplacecclientservicesConstants.FEMALE))
							{
								customerCreateUpdate.setGender(MarketplacecclientservicesConstants.F);
							}
							if (gender.getCode().equalsIgnoreCase(MarketplacecclientservicesConstants.UNKNOWN))
							{
								customerCreateUpdate.setGender(MarketplacecclientservicesConstants.U);
							}
						}
					}
					if (null != customerModel.getDefaultShipmentAddress()
							&& null != customerModel.getDefaultShipmentAddress().getCellphone())
					{
						customerCreateUpdate.setPhoneNumber(Long.valueOf(customerModel.getDefaultShipmentAddress().getCellphone()));
					}

					if (customerModel.getMarketplacepreference() != null)
					{
						//For Frequency
						if (null != customerModel.getMarketplacepreference().getEmailFrequency())
						{
							customerCreateUpdate.setFrequency(customerModel.getMarketplacepreference().getEmailFrequency().getCode());
						}
						//For Comm Preference
						commPreference = new MplCustomerWsData.CustomerCreateUpdate.CommPreferences();
						if (customerModel.getMarketplacepreference().getIsInterestedInEmail() != null)
						{
							commPreference.setCommPref("Mail");
							commPreference.setSubscribed(MarketplacecclientservicesConstants.X);
						}
						else
						{
							commPreference.setCommPref("SMS");
							commPreference.setSubscribed(MarketplacecclientservicesConstants.X);
						}
						customerCreateUpdate.setCommPreferences(commPreference);
						//For Subscription
						categoryWSList = new ArrayList<>();
						subscription = new MplCustomerWsData.CustomerCreateUpdate.Subscription();
						for (final CategoryModel category : customerModel.getMarketplacepreference().getPreferredCategory())
						{
							if (!(category instanceof ClassificationClassModel))
							{
								categoryWS = new MplCustomerWsData.CustomerCreateUpdate.Subscription.Categories();
								if (category.getName(Locale.ENGLISH) != null)
								{
									categoryWS.setCategory(category.getName(Locale.ENGLISH));
								}
								categoryWS.setSubscribed(MarketplacecclientservicesConstants.X);
								categoryWSList.add(categoryWS);
							}
						}
						subscription.setCategories(categoryWSList);
						subscription.setSubscribed(MarketplacecclientservicesConstants.X);
						customerCreateUpdate.setSubscription(subscription);
					}
					/** get Customer address starts here **/
					//customerAddressList = customerCreateUpdate.getAddresses();
					LOG.debug("call get customer address");
					customerCreateUpdate.setAddresses(getCustomerAddress(customerModel));
					/** get Customer address ends here **/
				}
			} //End of null check of customer model
			customerCreateUpdateList.add(customerCreateUpdate);
			LOG.debug("**********data added in customer create update list************");

			customerDataToCRM(customer);
		}
		catch (

		final Exception ex)

		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
		}

	}

	/**
	 * @Description: Returns Catalog Data
	 * @return catalogVersionModel
	 */
	public CatalogVersionModel catalogData()
	{
		final String catalogId = configurationService.getConfiguration().getString("cronjob.promotion.catelog", "");
		final String catalogVersionName = configurationService.getConfiguration().getString("cronjob.promotion.catalogVersionName",
				"");
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(catalogId, catalogVersionName);
		return catalogVersionModel;
	}

	/**
	 * @Description: Returns Category Model
	 * @return List<CategoryModel>
	 */
	public List<CategoryModel> allCategories(final CategoryModel catModel)
	{
		final List<CategoryModel> productCategoryData = new ArrayList<CategoryModel>();
		List<CategoryModel> superCategoryData = new ArrayList<CategoryModel>();
		if (null != catModel)
		{
			productCategoryData.add(catModel);
			superCategoryData = new ArrayList<CategoryModel>(categoryService.getAllSupercategoriesForCategory(catModel));
			if (!superCategoryData.isEmpty())
			{
				for (final CategoryModel categoryModel : superCategoryData)
				{
					productCategoryData.add(categoryModel);
				}
			}
		}
		return productCategoryData;
	}


	/**
	 * @param customerModel
	 */
	public List<MplCustomerWsData.CustomerCreateUpdate.Addresses> getCustomerAddress(final CustomerModel customerModel)
	{
		final List<MplCustomerWsData.CustomerCreateUpdate.Addresses> customerAddressList = new ArrayList<>();
		List<AddressModel> addressModelList;
		if (null != customerModel.getAddresses() && customerModel.getAddresses() instanceof List)
		{
			addressModelList = (List<AddressModel>) customerModel.getAddresses();
		}
		else
		{
			addressModelList = new ArrayList(customerModel.getAddresses());
		}


		MplCustomerWsData.CustomerCreateUpdate.Addresses customerAddress;

		for (final AddressModel addressModel : addressModelList)
		{
			if (null != addressModel)
			{
				customerAddress = new MplCustomerWsData.CustomerCreateUpdate.Addresses();
				LOG.debug("*****populating address data********");
				if (StringUtils.isNotEmpty(addressModel.getLine1()))
				{
					customerAddress.setAddress1(addressModel.getLine1());
				}

				if (StringUtils.isNotEmpty(addressModel.getLine2()))
				{
					customerAddress.setAddress2(addressModel.getLine2());
				}
				if (StringUtils.isNotEmpty(addressModel.getAddressLine3()))
				{
					customerAddress.setAddress3(addressModel.getAddressLine3());
				}


				if (StringUtils.isNotEmpty(addressModel.getPostalcode()))
				{
					customerAddress.setPincode(Long.valueOf(addressModel.getPostalcode()));
				}

				if (StringUtils.isNotEmpty(addressModel.getDistrict()))
				{
					try
					{
						final StateModel stateModel = mplClientAccountAddressService.getStateByDescription(addressModel.getDistrict());
						if (null != stateModel)
						{
							customerAddress.setState(stateModel.getRegion());
						}
					}
					catch (final Exception e)
					{
						LOG.debug("*****address state error skip********");
						//continue;
					}
				}
				if (StringUtils.isNotEmpty(addressModel.getTown()))
				{
					customerAddress.setCity(addressModel.getTown());
				}

				if (null != addressModel.getCountry() && StringUtils.isNotEmpty(addressModel.getCountry().getIsocode()))
				{
					customerAddress.setCountry(addressModel.getCountry().getIsocode());
				}
				if (null != customerModel.getDefaultShipmentAddress()
						&& customerModel.getDefaultShipmentAddress().equals(addressModel))
				{
					final String defaultflag = MarketplacecclientservicesConstants.X;
					customerAddress.setDefaultFlag(defaultflag);
				}
				if (null != addressModel.getAddressType())
				{
					customerAddress.setAddressType(addressModel.getAddressType());
				}
				customerAddressList.add(customerAddress);
				LOG.debug("added to customer list");
			}
		} //End of address loop
		return customerAddressList;
	}

	/**
	 * @return the mplClientAccountAddressService
	 */
	public MplClientAccountAddressService getMplClientAccountAddressService()
	{
		return mplClientAccountAddressService;
	}

	/**
	 * @param mplClientAccountAddressService
	 *           the mplClientAccountAddressService to set
	 */
	public void setMplClientAccountAddressService(final MplClientAccountAddressService mplClientAccountAddressService)
	{
		this.mplClientAccountAddressService = mplClientAccountAddressService;
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
	 * @return the categoryService
	 */
	public CategoryService getCategoryService()
	{
		return categoryService;
	}

	/**
	 * @param categoryService
	 *           the categoryService to set
	 */
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	/**
	 * @return the catalogVersionService
	 */
	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	/**
	 * @param catalogVersionService
	 *           the catalogVersionService to set
	 */
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	/**
	 * @return the simpleDateFormat
	 */
	public SimpleDateFormat getSimpleDateFormat()
	{
		return simpleDateFormat;
	}

}
