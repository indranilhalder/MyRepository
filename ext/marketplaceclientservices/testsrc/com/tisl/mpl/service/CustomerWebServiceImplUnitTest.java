/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.wsdto.CustomerCreateWSDTO;
import com.tisl.mpl.wsdto.CustomerUpdateAddressWSDTO;
import com.tisl.mpl.wsdto.CustomerUpdateTrialWSDTO;


/**
 * @author TCS
 *
 */
public class CustomerWebServiceImplUnitTest
{

	private MplCustomerWebServiceImpl customerWebServiceImpl;
	private CustomerCreateWSDTO request;
	private CustomerUpdateTrialWSDTO request1;
	private CustomerUpdateAddressWSDTO request2;
	@Mock
	private CustomerModel customerModel;
	@Mock
	private AddressModel addressModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		customerWebServiceImpl = new MplCustomerWebServiceImpl();
	}

	@Test
	public void customerModeltoWsDTOTest()
	{
		Mockito.when(request.getCustCreationFlag()).thenReturn("I");
		Mockito.when(request.getCustomerID()).thenReturn("10000");
		Mockito.when(request.getEmailID()).thenReturn("abc@tcs.com");
		//customerWebServiceImpl.customerModeltoWsData(customerModel, "I");

	}

	public void customerModeltoWsDTO1Test()
	{
		Mockito.when(request1.getCustCreationFlag()).thenReturn("U");
		Mockito.when(request1.getCustomerID()).thenReturn("10000");
		Mockito.when(request1.getEmailID()).thenReturn("abc@tcs.com");
		Mockito.when(request1.getFirstName()).thenReturn("ABC");
		Mockito.when(request1.getLastName()).thenReturn("TCS");
		Mockito.when(request1.getDateOfAnniversary()).thenReturn("DATE");
		Mockito.when(request1.getDateOfBirth()).thenReturn("Date");
		Mockito.when(request1.getGender()).thenReturn("FEMALE");
		Mockito.when(request1.getMobileNumber()).thenReturn("6765654345");
		//customerWebServiceImpl.customerModeltoWsData(customerModel, "U");
	}

	public void customerUpdateAddressWSDTO()
	{
		Mockito.when(request2.getCustCreationFlag()).thenReturn("U");
		Mockito.when(request2.getAddress1()).thenReturn("NewTown");
		Mockito.when(request2.getAddress2()).thenReturn("Eco-Space");
		Mockito.when(request2.getFirstName()).thenReturn("ABC");
		Mockito.when(request2.getLastName()).thenReturn("TCS");
		Mockito.when(request2.getCity()).thenReturn("Kolkata");
		Mockito.when(request2.getCountry()).thenReturn("INDIA");
		Mockito.when(request2.getDistrict()).thenReturn("West Bengal");
		Mockito.when(request2.getDefaultFlag()).thenReturn("Y");
		Mockito.when(request2.getPincode()).thenReturn("987876");
		Mockito.when(request2.getMobileNumber()).thenReturn("6765654345");
		//customerWebServiceImpl.customeraddressdetailsupdate(addressModel, customerModel);
	}
}
