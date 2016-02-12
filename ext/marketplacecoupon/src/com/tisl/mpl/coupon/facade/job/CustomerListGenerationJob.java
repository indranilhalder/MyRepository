/**
 *
 */
package com.tisl.mpl.coupon.facade.job;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.coupon.constants.MarketplacecouponConstants;
import com.tisl.mpl.coupon.service.MplCustomerDetailsService;


/**
 * @author TCS
 *
 */
public class CustomerListGenerationJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOG = Logger.getLogger(CustomerListGenerationJob.class);

	@Resource(name = "customerDetailsService")
	private MplCustomerDetailsService customerDetailsService;

	@Autowired
	private ConfigurationService configurationService;

	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String COLON_DELIMITER = ":";
	private static final String HYPHEN_DELIMITER = "-";
	private static final String NEW_LINE_SEPARATOR = "\n";

	//CSV file header
	private static final String FILE_HEADER = MarketplacecouponConstants.CUSTOMER_LIST_FILE_HEADER;


	@Override
	public PerformResult perform(final CronJobModel mailJob)
	{
		try
		{
			//Fetches All Customers
			final List<CustomerModel> customerList = customerDetailsService.getCustomer();

			if (null != customerList)
			{
				//put customer data in the POJO class
				writeDataIntoCsv(customerList);
			}

			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);

		}
		catch (final Exception e)
		{
			LOG.error("**********Error*************" + e.getMessage());
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
	}

	/**
	 * @Desctiption This method takes the list of customers and sets in the CSV file to be generated in a specified
	 *              location
	 *
	 * @param customerList
	 */
	void writeDataIntoCsv(final List<CustomerModel> customerList)
	{
		FileWriter fileWriter = null;
		final File rootFolder1 = new File(configurationService.getConfiguration().getString(
				MarketplacecouponConstants.CUSTOMER_LIST_FILE_LOCATION), MarketplacecouponConstants.CUSTOMER_LIST_FILE_NAME
				+ System.currentTimeMillis()
				+ configurationService.getConfiguration().getString(MarketplacecouponConstants.CUSTOMER_LIST_FILE_EXTENSION));
		try
		{
			rootFolder1.getParentFile().mkdirs();
			fileWriter = new FileWriter(rootFolder1);

			//Write the CSV file header
			fileWriter.append(FILE_HEADER);

			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			//Write a new student object list to the CSV file
			for (final CustomerModel customer : customerList)
			{
				fileWriter.append(customer.getUid());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getFirstName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getLastName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getOriginalUid());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getMobileNumber());
				fileWriter.append(COMMA_DELIMITER);
				if (customer.getGender() != null)
				{
					fileWriter.append(customer.getGender().getCode());
				}
				fileWriter.append(COMMA_DELIMITER);

				final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				if (customer.getCreationtime() != null)
				{
					fileWriter.append(dateFormat.format(customer.getCreationtime()));
				}
				fileWriter.append(COMMA_DELIMITER);
				if (customer.getDefaultShipmentAddress() != null)
				{
					final AddressModel address = customer.getDefaultShipmentAddress();
					fileWriter.append(address.getAddressType());
					fileWriter.append(COLON_DELIMITER);
					fileWriter.append(address.getLine1());
					fileWriter.append(HYPHEN_DELIMITER);
					fileWriter.append(address.getLine2());
					fileWriter.append(HYPHEN_DELIMITER);
					fileWriter.append(address.getPostalcode());
					fileWriter.append(HYPHEN_DELIMITER);
					fileWriter.append(address.getTown());
					fileWriter.append(HYPHEN_DELIMITER);
					if (address.getCountry() != null)
					{
						final CountryModel countryModel = address.getCountry();
						final Set<ZoneModel> regions = new HashSet<ZoneModel>(countryModel.getZones());
						final Iterator<ZoneModel> it = regions.iterator();
						final StringBuffer country = new StringBuffer();
						while (it.hasNext())
						{
							final ZoneModel zone = it.next();
							country.append(zone.getCode());
						}

						fileWriter.append(country);
					}
					fileWriter.append(HYPHEN_DELIMITER);
					fileWriter.append(address.getPhone1());
					fileWriter.append(HYPHEN_DELIMITER);
					fileWriter.append(address.getPhone2());
				}
				fileWriter.append(COMMA_DELIMITER);
				if (customer.getDateOfBirth() != null)
				{
					fileWriter.append(dateFormat.format(customer.getDateOfBirth()));
				}
				fileWriter.append(COMMA_DELIMITER);
				if (customer.getDateOfAnniversary() != null)
				{
					fileWriter.append(dateFormat.format(customer.getDateOfAnniversary()));
				}
				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append(NEW_LINE_SEPARATOR);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error in CsvFileWriter !!!");
		}
		finally
		{
			try
			{
				fileWriter.flush();
				fileWriter.close();
			}
			catch (final IOException e)
			{
				LOG.error("Error while flushing/closing fileWriter !!!");
			}
		}
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

}
