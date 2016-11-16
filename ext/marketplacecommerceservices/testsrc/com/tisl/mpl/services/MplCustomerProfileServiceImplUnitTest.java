package com.tisl.mpl.services;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doThrow;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.PasswordMismatchException;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.helper.MplEnumerationHelper;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCustomerProfileDao;
import com.tisl.mpl.marketplacecommerceservices.daos.impl.ExtendedUserDaoImpl;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCustomerProfileServiceImpl;


/**
 * @author 594031
 *
 */

@UnitTest
public class MplCustomerProfileServiceImplUnitTest
{
	@Autowired
	private MplCustomerProfileDao mplCustomerProfileDao;
	@Autowired
	private ModelService modelService;
	@Autowired
	private MplEnumerationHelper mplEnumerationHelper;
	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoderService passwordEncoderService;
	@Autowired
	private ExtendedUserDaoImpl userDao;
	@Autowired
	private CustomerModel customerModel;
	@Autowired
	private UserModel user;

	@Mock
	private ExtendedUserService extUserService;
	@Mock
	private EnumerationValueModel enumerationValueModel;

	private MplCustomerProfileServiceImpl mplCustomerProfileService;

	private static final Logger LOG = Logger.getLogger(MplCustomerProfileServiceImplUnitTest.class);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		this.mplCustomerProfileService = Mockito.mock(MplCustomerProfileServiceImpl.class);

		this.mplCustomerProfileDao = Mockito.mock(MplCustomerProfileDao.class);
		this.mplCustomerProfileService.setMplCustomerProfileDao(mplCustomerProfileDao);

		this.modelService = Mockito.mock(ModelService.class);
		this.mplCustomerProfileService.setModelService(modelService);

		this.mplEnumerationHelper = Mockito.mock(MplEnumerationHelper.class);
		this.mplCustomerProfileService.setMplEnumerationHelper(mplEnumerationHelper);

		this.userService = Mockito.mock(UserService.class);
		this.mplCustomerProfileService.setUserService(userService);

		this.passwordEncoderService = Mockito.mock(PasswordEncoderService.class);
		this.mplCustomerProfileService.setPasswordEncoderService(passwordEncoderService);

		this.userDao = Mockito.mock(ExtendedUserDaoImpl.class);
		this.mplCustomerProfileService.setUserDao(userDao);

		this.customerModel = Mockito.mock(CustomerModel.class);
		this.user = Mockito.mock(UserModel.class);
		this.extUserService = Mockito.mock(ExtendedUserService.class);
		this.enumerationValueModel = Mockito.mock(EnumerationValueModel.class);
	}

	@Test
	public void testGetCustomerProfileDetail()
	{
		//		customerModel.setOriginalUid("");//TODO : Please enter original uid
		final String emailId = "moumita.patra@tcs.com";
		final List<CustomerModel> customerModels = Arrays.asList(customerModel);
		Mockito.when(mplCustomerProfileDao.getCustomerProfileDetail(customerModel)).thenReturn(customerModels);

		final List<CustomerModel> customerProfileDetail = mplCustomerProfileDao.getCustomerProfileDetail(customerModel);

		assertEquals("We should find one", 1, customerProfileDetail.size());
		assertEquals("And should equals what the mock returned", customerModels.get(0), customerProfileDetail.get(0));

		assertNotNull(customerModels);
		LOG.info("Method : testGetCustomerProfileDetail >>>>>>>");
		mplCustomerProfileService.getCustomerProfileDetail(emailId);
	}


	@Test
	public void testUpdateCustomerProfile()
	{
		//TISSEC-50
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		final String name = "test";
		final String uid = "100000001";
		//		customerModel.setUid("");//TODO : Please enter uid
		//		customerModel.setName("");//TODO : Please enter name
		//		customerModel.setTitle(null);
		Mockito.doNothing().when(modelService).save(customerModel);
		mplCustomerProfileService.updateCustomerProfile(customerModel, "", "");//TODO : Please enter uid,Please enter name
		LOG.info("Method : testUpdateCustomerProfile >>>>>>>");
		mplCustomerProfileService.updateCustomerProfile(customerModel, name, uid);
	}


	@Test
	public void testInternalSaveCustomer() throws DuplicateUidException
	{
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		//		customerModel.setUid("");//TODO :Please enter uid
		//		customerModel.setName("");//TODO :Please enter name
		//		customerModel.setTitle(null);
		Mockito.doNothing().when(modelService).save(customerModel);
		LOG.info("Method : testInternalSaveCustomer >>>>>>>");
	}

	@Test
	public void testGetGenders()
	{
		final List<EnumerationValueModel> enumList = Arrays.asList(enumerationValueModel);
		Mockito.when(mplEnumerationHelper.getEnumerationValuesForCode(Gender._TYPECODE)).thenReturn(enumList);
		mplCustomerProfileService.getGenders();
		LOG.info("Method : testGetGenders >>>>>>>			List Size : " + enumList.size());
	}

	@Test
	public void testChangeUid() throws DuplicateUidException, PasswordMismatchException
	{
		final String uid = "100000001";
		final String currentPassword = "Tata@1234";
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		mplCustomerProfileService.changeUid(uid, currentPassword);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testAdjustPassword()
	{
		final String currPwdr = MarketplacecclientservicesConstants.EMPTY;//TODO : Please enter current password
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		Mockito.when(passwordEncoderService.encode(user, currPwdr, "md5")).thenReturn("djfjdhsdafsas647@*&sdfkj");
		final String expected = passwordEncoderService.encode(customerModel, currPwdr, "md5");
		Assert.assertEquals(expected, customerModel.getEncodedPassword());
		Mockito.doNothing().when(modelService).save(customerModel);
		Assert.assertNotSame(customerModel.getEncodedPassword(), "");
		doThrow(new RuntimeException()).when(customerModel).getEncodedPassword();
		LOG.info("Method : testAdjustPassword >>>>>>>");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testCheckUniquenessOfEmail()
	{
		final String uid = "100000001";
		Assert.assertNotSame(customerModel.getOriginalUid(), uid);
		mplCustomerProfileService.checkUniquenessOfEmail(uid);
		//		Assert.assertFalse(extUserService.getCheckUniqueId(uid));
		//		given(Boolean.valueOf(extUserService.getCheckUniqueId(uid))).willReturn(Boolean.valueOf(false));
		//		Assert.assertTrue(extUserService.getCheckUniqueId(id));
		//		given(Boolean.valueOf(extUserService.getCheckUniqueId(id))).willReturn(Boolean.valueOf(true));
	}

	@Test
	public void testSetUserPassword()
	{
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		Mockito.doNothing().when(modelService).save(customerModel);
	}

	@SuppressWarnings(
	{ "deprecation", "unused" })
	@Test
	public void testGetUserForUID()
	{
		final String uid = "100000001";
		Mockito.when(userDao.findUserByUID(uid)).thenReturn(customerModel);//TODO : Please enter uid
		//		final CustomerModel cusModel = null;
		//		Assert.assertNull(cusModel);
		doThrow(new RuntimeException()).when(userDao).findUserByUID(uid);
		LOG.info("Method : testGetUserForUID >>>>>>>");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testCheckUidUniqueness() throws DuplicateUidException
	{
		//		Mockito.when(userService.getUserForUID("")).thenReturn(user);//TODO : Please enter uid
		//		assertNotNull(user);
		final String uid = "100000001";
		Assert.assertFalse(extUserService.getCheckUniqueId(uid));
		//		given(Boolean.valueOf(extUserService.getCheckUniqueId(uid))).willReturn(Boolean.FALSE);
		doThrow(new RuntimeException()).when(extUserService).getCheckUniqueId(uid);
		LOG.info("Method : testCheckUidUniqueness >>>>>>>");
	}
}
