package com.tisl.mpl.services;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.impl.DefaultConfigurationService;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;


public class ExtendedCustomerEmailResolutionServiceTest
{
	@Autowired
	public static final String DEFAULT_CUSTOMER_KEY = "customer.email.default";
	private DefaultConfigurationService configurationService;
	public static final String DEFAULT_CUSTOMER_EMAIL = "demo@example.com";
	private Configuration configuration;
	private static final Logger LOG = Logger.getLogger(ExtendedCustomerEmailResolutionServiceTest.class);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.configurationService = Mockito.mock(DefaultConfigurationService.class);
		this.configuration = Mockito.mock(Configuration.class);
	}

	@Test
	public void testEmailValidationForCustomer()
	{
		//TISSEC-50
		final CustomerModel customer = mock(CustomerModel.class);
		final String email = MarketplacecclientservicesConstants.EMPTY;//TODO : Please enter email
		Mockito.when(customer.getOriginalUid()).thenReturn(email);
		given(configurationService.getConfiguration()).willReturn(configuration);
		Mockito.when(configuration.getString(DEFAULT_CUSTOMER_KEY, DEFAULT_CUSTOMER_EMAIL)).thenReturn("Successful");
		LOG.info("Method : testEmailValidationForCustomer >>>>>>>");

	}
}
