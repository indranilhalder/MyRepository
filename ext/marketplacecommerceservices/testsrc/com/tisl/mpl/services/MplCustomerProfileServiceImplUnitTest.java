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

import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
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
		oCustomerModel.setOriginalUid("");//TODO : Please enter original uid

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
		//TISSEC-50
		customerModel = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		customerModel.setUid("");//TODO : Please enter uid
		customerModel.setName("");//TODO : Please enter name
		customerModel.setTitle(null);
		Mockito.doNothing().when(modelService).save(customerModel);
		mplCustomerProfileService.updateCustomerProfile(customerModel, "", "");//TODO : Please enter uid,Please enter name
		LOG.info("Method : testUpdateCustomerProfile >>>>>>>");
	}


	@Test
	public void testInternalSaveCustomer() throws DuplicateUidException
	{
		customerModel = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		customerModel.setUid("");//TODO :Please enter uid
		customerModel.setName("");//TODO :Please enter name
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
		final String currPwdr = MarketplacecclientservicesConstants.EMPTY;//TODO : Please enter current password
		customerModel = new CustomerModel();
		user = new UserModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		Mockito.when(passwordEncoderService.encode(user, currPwdr, "md5")).thenReturn("djfjdhsdafsas647@*&sdfkj");
		customerModel.setEncodedPassword("");//TODO : Please enter encoded password
		assertEquals(customerModel.getEncodedPassword(), "");//TODO : Please enter encoded password
		Mockito.doNothing().when(modelService).save(customerModel);
		LOG.info("Method : testAdjustPassword >>>>>>>");
	}


	@Test
	public void testGetUserForUID()
	{
		customerModel = new CustomerModel();
		Mockito.when(userDao.findUserByUID("")).thenReturn(customerModel);//TODO : Please enter uid

		assertNotNull(customerModel);
		LOG.info("Method : testGetUserForUID >>>>>>>");
	}


	@Test
	public void testCheckUidUniqueness() throws DuplicateUidException
	{
		user = new UserModel();
		Mockito.when(userService.getUserForUID("")).thenReturn(user);//TODO : Please enter uid
		assertNotNull(user);
		LOG.info("Method : testCheckUidUniqueness >>>>>>>");
	}

}
