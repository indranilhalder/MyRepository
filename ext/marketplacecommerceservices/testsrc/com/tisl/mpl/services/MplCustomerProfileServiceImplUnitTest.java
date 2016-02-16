package com.tisl.mpl.services;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.helper.MplEnumerationHelper;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCustomerProfileDao;
import com.tisl.mpl.marketplacecommerceservices.daos.impl.ExtendedUserDaoImpl;
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

	private MplCustomerProfileServiceImpl mplCustomerProfileService;

	private static final Logger LOG = Logger.getLogger(MplCustomerProfileServiceImplUnitTest.class);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		this.mplCustomerProfileService = new MplCustomerProfileServiceImpl();

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
	}

	@Test
	public void testGetCustomerProfileDetail()
	{
		final CustomerModel oCustomerModel = new CustomerModel();
		oCustomerModel.setOriginalUid("testuser1@test.com");

		final CustomerModel oCustomerModelList = new CustomerModel();
		final List<CustomerModel> customerModels = Arrays.asList(oCustomerModelList);
		Mockito.when(mplCustomerProfileDao.getCustomerProfileDetail(oCustomerModel)).thenReturn(customerModels);

		final List<CustomerModel> customerProfileDetail = mplCustomerProfileDao.getCustomerProfileDetail(oCustomerModel);

		assertEquals("We should find one", 1, customerProfileDetail.size());
		assertEquals("And should equals what the mock returned", customerModels.get(0), customerProfileDetail.get(0));

		assertNotNull(customerModels);
		LOG.info("Method : testGetCustomerProfileDetail >>>>>>>");
	}


	@Test
	public void testUpdateCustomerProfile()
	{
		customerModel = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		customerModel.setUid("testuser1@test.com");
		customerModel.setName("Test");
		customerModel.setTitle(null);
		Mockito.doNothing().when(modelService).save(customerModel);
		mplCustomerProfileService.updateCustomerProfile(customerModel, "testuser1@test.com", "Test");
		LOG.info("Method : testUpdateCustomerProfile >>>>>>>");
	}


	@Test
	public void testInternalSaveCustomer() throws DuplicateUidException
	{
		customerModel = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		customerModel.setUid("testuser1@test.com");
		customerModel.setName("Test");
		customerModel.setTitle(null);
		Mockito.doNothing().when(modelService).save(customerModel);
		LOG.info("Method : testInternalSaveCustomer >>>>>>>");
	}

	@Test
	public void testGetGenders()
	{
		final EnumerationValueModel enumListObj = new EnumerationValueModel();
		final List<EnumerationValueModel> enumList = Arrays.asList(enumListObj);
		Mockito.when(mplEnumerationHelper.getEnumerationValuesForCode(Gender._TYPECODE)).thenReturn(enumList);

		LOG.info("Method : testGetGenders >>>>>>>			List Size : " + enumList.size());
	}

	@Test
	public void testAdjustPassword() throws PasswordMismatchException
	{
		final String currPwdr = "currPwd@123456.com";
		customerModel = new CustomerModel();
		user = new UserModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		Mockito.when(passwordEncoderService.encode(user, currPwdr, "md5")).thenReturn("djfjdhsdafsas647@*&sdfkj");
		customerModel.setEncodedPassword("djfjdhsdafsas647@*&sdfkj");
		assertEquals(customerModel.getEncodedPassword(), "djfjdhsdafsas647@*&sdfkj");
		Mockito.doNothing().when(modelService).save(customerModel);
		LOG.info("Method : testAdjustPassword >>>>>>>");
	}


	@Test
	public void testGetUserForUID()
	{
		customerModel = new CustomerModel();
		Mockito.when(userDao.findUserByUID("testuser1@test.com")).thenReturn(customerModel);

		assertNotNull(customerModel);
		LOG.info("Method : testGetUserForUID >>>>>>>");
	}


	@Test
	public void testCheckUidUniqueness() throws DuplicateUidException
	{
		user = new UserModel();
		Mockito.when(userService.getUserForUID("testuser1@test.com")).thenReturn(user);
		assertNotNull(user);
		LOG.info("Method : testCheckUidUniqueness >>>>>>>");
	}

}
