/**
 *
 */
package com.tisl.mpl.services;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;


/**
 * @author 666220
 *
 */
@UnitTest
public class ForgotpasswordServiceImplUnitTest
{


	private ModelService modelService;

	private ExtendedUserService userService;

	private SecureTokenService secureTokenService;
	private CustomerModel customerModel;
	private static final String token = "dhcdsvcbdlkjvb";
	private static final String token2 = "flkjhfiksfdjksldfj";
	private SecureToken data;


	@Before
	public void setUp()
	{

		MockitoAnnotations.initMocks(this);

		this.modelService = Mockito.mock(ModelService.class);
		this.secureTokenService = Mockito.mock(SecureTokenService.class);
		this.userService = Mockito.mock(ExtendedUserService.class);
		this.customerModel = Mockito.mock(CustomerModel.class);
		this.data = Mockito.mock(SecureToken.class);
	}

	@Test
	public void testForgottenPasswordEmail() throws IllegalArgumentException
	{
		Mockito.when(customerModel.getOriginalUid()).thenReturn("abc@xyz.com");
		Mockito.when(secureTokenService.encryptData(data)).thenReturn(token);
		Mockito.doNothing().when(modelService).save(customerModel);

	}

	@Test
	public void testUpdatePassword() throws IllegalArgumentException
	{
		Mockito.when(secureTokenService.decryptData(token)).thenReturn(data);
		Mockito.when(userService.getUserForUid(data.getData())).thenReturn(customerModel);
		Mockito.when(customerModel.getToken()).thenReturn(token2);
		Mockito.doNothing().when(modelService).save(customerModel);
	}
}
