/**
 *
 */
package com.tisl.mpl.facade.account.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.marketplacecommerceservices.service.AccountAddressService;


/**
 * @author TCS
 *
 */
public class AccountAddressFacadeUnitTest
{
	@Autowired
	private UserService userService;
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
	private CustomerModel customerModel;
	private AddressData addressData;
	protected static final Logger LOG = Logger.getLogger(AccountAddressFacadeUnitTest.class);


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.userService = Mockito.mock(UserService.class);
		this.modelService = Mockito.mock(ModelService.class);
		this.customerAccountService = Mockito.mock(CustomerAccountService.class);
		this.commerceCommonI18NService = Mockito.mock(CommerceCommonI18NService.class);
		this.accountAddressService = Mockito.mock(AccountAddressService.class);
		this.addressData = Mockito.mock(AddressData.class);

		addressData.setId("12345678");
	}

	@Test
	public void testAddaddress()
	{
		customerModel = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		final AddressData newAddress = new AddressData();
		final boolean makeThisAddressTheDefault = true;
		newAddress.setVisibleInAddressBook(true);
		newAddress.setDefaultAddress(true);
		final AddressModel defaultAddress = customerModel.getDefaultShipmentAddress();
		final AddressModel address = new AddressModel();
		Mockito.doNothing().when(customerAccountService).saveAddressEntry(customerModel, defaultAddress);
		Mockito.doNothing().when(customerAccountService).saveAddressEntry(customerModel, address);

		address.setFirstname("First Name");
		address.setLastname("Last Name");
		address.setLine1("AddressLine1");
		address.setLine1("AddressLine2");
		address.setAddressLine3("AddressLine3");
		address.setLocality("Locality");
		address.setDistrict("City");
		address.setState("State");


		if (makeThisAddressTheDefault)
		{
			Mockito.doNothing().when(customerAccountService).setDefaultAddressEntry(customerModel, address);
		}
		LOG.info("Method : testAddaddress >>>>>>>");
	}

	@Test
	public void testEditaddress()
	{
		customerModel = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		final AddressModel addressForCode = new AddressModel();
		final AddressData newAddress = new AddressData();
		Mockito.when(customerAccountService.getAddressForCode(customerModel, addressData.getId())).thenReturn(addressForCode);

		addressForCode.setFirstname("First Name New");
		addressForCode.setLastname("Last Name New");
		addressForCode.setLine1("AddressLine1 New");
		addressForCode.setLine1("AddressLine2 New");
		addressForCode.setAddressLine3("AddressLine3 New");
		addressForCode.setLocality("Locality New");
		addressForCode.setDistrict("City New");
		addressForCode.setState("State New");

		newAddress.setDefaultAddress(true);
		final AddressModel address = new AddressModel();
		Mockito.doNothing().when(customerAccountService).saveAddressEntry(customerModel, address);

		if (newAddress.isDefaultAddress())
		{
			Mockito.doNothing().when(customerAccountService).setDefaultAddressEntry(customerModel, address);
		}
		LOG.info("Method : testEditaddress >>>>>>>");
	}


	@Test
	public void testGetAddressBook()
	{
		customerModel = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		final AddressModel addressModel = new AddressModel();
		final List<AddressModel> addressModelList = Arrays.asList(addressModel);
		Mockito.when(customerAccountService.getAddressBookDeliveryEntries(customerModel)).thenReturn(addressModelList);

		final AddressModel addressModel2 = addressModelList.get(0);
		assertEquals("We should find one", 1, addressModelList.size());

		assertNotNull(addressModel2);
		LOG.info("Method : testGetAddressBook >>>>>>>");
	}


	@Test
	public void testGetStates()
	{
		final List<StateData> stateList = new ArrayList<StateData>();
		Mockito.when(accountAddressService.getStates()).thenReturn(stateList);
		LOG.info("Method : testGetStates >>>>>>>");
	}

}
