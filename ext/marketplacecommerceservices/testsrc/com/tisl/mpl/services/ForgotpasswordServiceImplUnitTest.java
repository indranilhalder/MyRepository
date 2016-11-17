/**
 *
 */
package com.tisl.mpl.services;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.model.process.ForgottenPasswordProcessModel;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.ForgetPasswordServiceImpl;


/**
 * @author 666220
 *
 */
@UnitTest
public class ForgotpasswordServiceImplUnitTest
{

	@Mock
	private ForgottenPasswordProcessModel forgottenPasswordProcessModel;
	@Mock
	private ForgetPasswordServiceImpl forgetPasswordServiceImpl;
	@Mock
	private BusinessProcessService businessProcessService;
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
		this.forgottenPasswordProcessModel = Mockito.mock(ForgottenPasswordProcessModel.class);
		this.forgetPasswordServiceImpl = Mockito.mock(ForgetPasswordServiceImpl.class);
		this.businessProcessService = Mockito.mock(BusinessProcessService.class);
		this.modelService = Mockito.mock(ModelService.class);
		this.secureTokenService = Mockito.mock(SecureTokenService.class);
		this.userService = Mockito.mock(ExtendedUserService.class);
		this.customerModel = Mockito.mock(CustomerModel.class);
		this.data = Mockito.mock(SecureToken.class);
		//		final long startTime = System.currentTimeMillis();
	}

	@Test
	public void testForgottenPasswordEmail()
	{
		final String constant = "forgottenPassword-" + "100000001" + "-" + (System.currentTimeMillis()) + "ms";
		final String securePasswordUrl = "/forgotPassword";
		final boolean isMobile = true;
		Mockito.when(customerModel.getOriginalUid()).thenReturn("abc@xyz.com");
		Mockito.when(secureTokenService.encryptData(data)).thenReturn(token);
		Mockito.doNothing().when(modelService).save(customerModel);
		forgetPasswordServiceImpl.setBusinessProcessService(businessProcessService);
		Mockito.when(businessProcessService.createProcess(constant, "forgottenPasswordEmailProcess")).thenReturn(
				forgottenPasswordProcessModel);
		Mockito.doNothing().when(modelService).save(forgottenPasswordProcessModel);
		Mockito.doNothing().when(businessProcessService).startProcess(forgottenPasswordProcessModel);
		forgetPasswordServiceImpl.forgottenPasswordEmail(customerModel, securePasswordUrl, Boolean.valueOf(isMobile));
	}

	@Test
	public void testUpdatePassword() throws TokenInvalidatedException
	{
		final String newPassword = "Tata@1234";
		Mockito.when(secureTokenService.decryptData(token)).thenReturn(data);
		Mockito.when(userService.getUserForUid(data.getData())).thenReturn(customerModel);
		Mockito.when(customerModel.getToken()).thenReturn(token2);
		Mockito.doNothing().when(modelService).save(customerModel);
		forgetPasswordServiceImpl.updatePassword(token2, newPassword);
	}
}
