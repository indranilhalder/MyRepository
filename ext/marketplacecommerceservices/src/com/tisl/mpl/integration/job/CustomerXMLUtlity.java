/**
 *
 */
package com.tisl.mpl.integration.job;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.FrequenciesModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.AccountAddressService;
import com.tisl.mpl.marketplacecommerceservices.service.BlacklistService;
import com.tisl.mpl.model.StateModel;
import com.tisl.mpl.service.MplClientAccountAddressService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.xml.pojo.MplCustomerWsData;
import com.tisl.mpl.xml.pojo.MplCustomerWsData.CustomerCreateUpdate.Addresses;


/**
 * @author TCS
 *
 */
public class CustomerXMLUtlity
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CustomerXMLUtlity.class.getName());

	@Autowired
	private AccountAddressService accountAddressService;

	@Resource(name = "mplClientAccountAddressService")
	private MplClientAccountAddressService mplClientAccountAddressService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Autowired
	private CategoryService categoryService;

	@Resource(name = "blacklistService")
	private BlacklistService blacklistService;

	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MarketplacecommerceservicesConstants.DMY_DATE_FORMAT);



	/**
	 * @Description: Populate XML Data corresponding to a customer
	 * @param customerData
	 */
	public void generateCustomerXMlData(final List<CustomerModel> customerData)
	{
		MplCustomerWsData bulkDataList = new MplCustomerWsData();
		String xmlString = MarketplacecommerceservicesConstants.EMPTY;
		Marshaller marshaller = null;
		final StringWriter stringWriter = new StringWriter();
		try
		{

			if (null != customerData && !customerData.isEmpty())
			{
				LOG.debug("inside generateCustomerXMlData****** calling populate customer data");
				bulkDataList = populateCustomerData(customerData);
				if (null != bulkDataList)
				{
					final JAXBContext context = JAXBContext.newInstance(MplCustomerWsData.class);
					if (null != context)
					{
						marshaller = context.createMarshaller();
					}
					if (null != marshaller)
					{
						marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
						marshaller.marshal(bulkDataList, stringWriter);
					}
					xmlString = stringWriter.toString();
					LOG.debug(xmlString);
					final String folderPath = configurationService.getConfiguration().getString("customerMaster.batchJob.folder.path");
					/** Create directory if not present */
					createDirectoryIfNeeded(folderPath);
					/*
					 * FileName is being fetched from local.properties file and changes might be required if we need to add
					 * timestamp to file names.
					 */
					final String fileName = configurationService.getConfiguration().getString("customerMaster.batchJob.fileName");
					final File xmlfile = new File(folderPath + fileName);
					xmlfile.setReadable(true);
					//	xmlfile.setWritable(true);
					//	xmlfile.setExecutable(true);
					FileUtils.writeStringToFile(xmlfile, xmlString);
					LOG.debug("File created successfully!!!");
				}
			}
			//Content
		}
		catch (final IOException e)
		{
			LOG.error("IO Exception!!!");
		}
		catch (final JAXBException ex)
		{
			LOG.error("JAXB Exception!!!");
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
		}
	}

	public static boolean isNumeric(final String str)
	{
		try
		{
			//final double isNumber = Double.parseDouble(str);
			//OG.debug("skipping OOTB user id" + isNumber);
			Double.parseDouble(str);
		}
		catch (final NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}

	public static boolean isValidEmailAddress(final String email)
	{
		boolean result = true;
		try
		{
			final InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		}
		catch (final AddressException ex)
		{
			result = false;
		}
		return result;
	}

	/**
	 * @description This method populates customer data into MplCustomerXMLData
	 * @param customerData
	 * @return MplCustomerXMLData
	 */
	public MplCustomerWsData populateCustomerData(final List<CustomerModel> customerData)
	{
		final MplCustomerWsData xmlData = new MplCustomerWsData();
		MplCustomerWsData.CustomerCreateUpdate customerCreateUpdate = new MplCustomerWsData.CustomerCreateUpdate();
		MplCustomerWsData.CustomerCreateUpdate.CommPreferences commPreference = new MplCustomerWsData.CustomerCreateUpdate.CommPreferences();
		MplCustomerWsData.CustomerCreateUpdate.Subscription subscription = new MplCustomerWsData.CustomerCreateUpdate.Subscription();
		MplCustomerWsData.CustomerCreateUpdate.Subscription.Categories categoryWS = null;
		List<MplCustomerWsData.CustomerCreateUpdate.Subscription.Categories> categoryWSList = new ArrayList<>();
		try
		{

			List<MplCustomerWsData.CustomerCreateUpdate> xmlDataCustomerCreateUpdateList; // removing final modifier
			xmlDataCustomerCreateUpdateList = xmlData.getCustomerCreateUpdate();
			for (final CustomerModel customer : customerData)
			{
				if (isNumeric(customer.getUid()) && isValidEmailAddress(customer.getOriginalUid()))
				{
					customerCreateUpdate = new MplCustomerWsData.CustomerCreateUpdate();
					if (null != customer.getCreationtime() && null != customer.getModifiedtime())
					{
						final boolean flag = compareDate(customer.getCreationtime(), customer.getModifiedtime());
						if (flag)
						{
							customerCreateUpdate.setUpSertFlag(MarketplacecommerceservicesConstants.INSERT);
						}
						else
						{
							customerCreateUpdate.setUpSertFlag(MarketplacecommerceservicesConstants.UPDATE);
						}
					}
					if (StringUtils.isNotEmpty(customer.getUid()))
					{
						customerCreateUpdate.setCustomerID(Integer.valueOf(customer.getUid()));
						LOG.info("customer id" + customer.getUid());
					}
					if (StringUtils.isNotEmpty(customer.getOriginalUid()))
					{
						customerCreateUpdate.setEmailID(customer.getOriginalUid());
					}
					final boolean isBlackListed = blacklistService.blacklistedOrNot(customer.getUid());
					LOG.debug("returned from blacklist service");
					if (isBlackListed) //isBlackListed == true
					{
						customerCreateUpdate.setIsBlackListed(MarketplacecommerceservicesConstants.X);
						//If a customer is blacklisted, it will treated as update
						customerCreateUpdate.setUpSertFlag(MarketplacecommerceservicesConstants.UPDATE);
					}
					if (StringUtils.isNotEmpty(customer.getFirstName()))
					{
						customerCreateUpdate.setFirstName(customer.getFirstName());
						//If a customer has updates his/her personal information it will treated as update
						customerCreateUpdate.setUpSertFlag(MarketplacecommerceservicesConstants.UPDATE);
					}
					if (StringUtils.isNotEmpty(customer.getLastName()))
					{
						customerCreateUpdate.setLastName(customer.getLastName());
					}
					if (null != customer.getDateOfBirth())
					{
						customerCreateUpdate.setDateOfBirth(simpleDateFormat.format(customer.getDateOfBirth()));
					}
					if (null != customer.getDateOfAnniversary())
					{
						customerCreateUpdate.setDateOfMarriage(simpleDateFormat.format(customer.getDateOfAnniversary()));
					}
					if (null != customer.getGender())
					{
						final Gender gender = customer.getGender();
						if (StringUtils.isNotEmpty(gender.getCode())
								&& (gender.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.MALE)))
						{
							customerCreateUpdate.setGender(MarketplacecommerceservicesConstants.M);
						}
						if (StringUtils.isNotEmpty(gender.getCode())
								&& (gender.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.FEMALE)))
						{
							customerCreateUpdate.setGender(MarketplacecommerceservicesConstants.F);
						}
						if (StringUtils.isNotEmpty(gender.getCode())
								&& (gender.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.UNKNOWN)))
						{
							customerCreateUpdate.setGender(MarketplacecommerceservicesConstants.U);
						}
					}
					if (null != customer.getDefaultShipmentAddress() && null != customer.getDefaultShipmentAddress().getCellphone())
					{
						customerCreateUpdate.setPhoneNumber(Long.valueOf(customer.getDefaultShipmentAddress().getCellphone()));
					}
					/** get Customer address starts here **/
					LOG.debug("Customer Preference started");
					//TISUAT-4755
					if (customer.getMarketplacepreference() != null)
					{

						//For Comm Preference
						commPreference = new MplCustomerWsData.CustomerCreateUpdate.CommPreferences();
						//TISSTRT-785 Remove Subscription Tag if the customer opts to unsubscribe from e-mail preferences
						if (customer.getMarketplacepreference().getIsInterestedInEmail() != null
								&& customer.getMarketplacepreference().getIsInterestedInEmail().booleanValue())
						{
							commPreference.setCommPref("Mail");
							commPreference.setSubscribed(MarketplacecclientservicesConstants.X);
							//For Frequency
							if (null != customer.getMarketplacepreference().getEmailFrequency())
							{
								customerCreateUpdate.setFrequency(customer.getMarketplacepreference().getEmailFrequency().getCode());
							}
						}

						customerCreateUpdate.setCommPreferences(commPreference);
						//For Subscription
						LOG.debug("Category started");
						categoryWSList = new ArrayList<>();
						subscription = new MplCustomerWsData.CustomerCreateUpdate.Subscription();
						if (null != customer.getMarketplacepreference().getPreferredCategory())
						{
							final List<CategoryModel> catModels = (List<CategoryModel>) customer.getMarketplacepreference()
									.getPreferredCategory();
							for (final CategoryModel category : catModels)
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
						}
						subscription.setCategories(categoryWSList);
						//TISSTRT-785 Remove Subscription Tag if the customer opts to unsubscribe from e-mail preferences
						if (categoryWSList.size() > 0)
						{
							subscription.setSubscribed(MarketplacecclientservicesConstants.X);
						}
						//subscription.setSubscribed(MarketplacecclientservicesConstants.X);
						customerCreateUpdate.setSubscription(subscription);

					}

					/** get Customer address starts here **/
					//customerAddressList = customerCreateUpdate.getAddresses();
					LOG.debug("call get customer address");
					final List<Addresses> customerAddressList = customerCreateUpdate.getAddresses();
					if (null != customerAddressList && customerAddressList.size() > 0)
					{ //If a customer has updates his/her address it will treated as update
						customerCreateUpdate.setUpSertFlag(MarketplacecommerceservicesConstants.UPDATE);
					}
					customerCreateUpdate.setAddresses(getCustomerAddress(customer));
					for (final FrequenciesModel frequency : customer.getFrequencyList())
					{
						customerCreateUpdate.setFrequency(frequency.toString());
					}
					/** get Customer address ends here **/
					xmlDataCustomerCreateUpdateList.add(customerCreateUpdate);
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
		}
		return xmlData;
	}

	/**
	 * @Description: Returns Category Model
	 * @return List<CategoryModel>
	 */
	public List<CategoryModel> allCategories(final CategoryModel catModel)
	{
		final List<CategoryModel> productCategoryList = new ArrayList<CategoryModel>();
		List<CategoryModel> superCategoryData = new ArrayList<CategoryModel>();
		if (null != catModel)
		{
			final CategoryModel categModel = categoryService.getCategoryForCode(catModel.getCode());
			productCategoryList.add(categModel);
			superCategoryData = new ArrayList<CategoryModel>(categoryService.getAllSupercategoriesForCategory(categModel));
			if (!superCategoryData.isEmpty())
			{
				for (final CategoryModel categoryModel : superCategoryData)
				{
					productCategoryList.add(categoryModel);
				}
			}
		}
		return productCategoryList;
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
	 * @description This method compares two date
	 * @param creationtime
	 * @param modifiedtime
	 * @return flag
	 */
	private boolean compareDate(final Date creationtime, final Date modifiedtime)
	{
		boolean flag = false;
		final Calendar cal1 = Calendar.getInstance();
		final Calendar cal2 = Calendar.getInstance();
		cal1.setTime(creationtime);
		cal2.setTime(modifiedtime);
		flag = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
		return flag;
	}

	/**
	 * @description This method creates a directory if it does not exists
	 * @param directoryName
	 */
	private void createDirectoryIfNeeded(final String directoryName)
	{
		if (StringUtils.isNotEmpty(directoryName))
		{
			final File theDir = new File(directoryName);
			// if the directory does not exist, create it
			if (!theDir.exists())
			{
				LOG.debug("creating directory: " + directoryName);
				theDir.mkdirs();
			}
		}
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
	 * @return the blacklistService
	 */
	public BlacklistService getBlacklistService()
	{
		return blacklistService;
	}

	/**
	 * @param blacklistService
	 *           the blacklistService to set
	 */
	public void setBlacklistService(final BlacklistService blacklistService)
	{
		this.blacklistService = blacklistService;
	}



	/**
	 * @return the simpleDateFormat
	 */
	public SimpleDateFormat getSimpleDateFormat()
	{
		return simpleDateFormat;
	}


}
