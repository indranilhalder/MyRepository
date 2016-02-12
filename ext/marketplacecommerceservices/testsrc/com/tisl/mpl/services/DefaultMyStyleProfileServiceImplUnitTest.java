/**
 *
 */
package com.tisl.mpl.services;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MplStyleProfileModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MyStyleProfileDao;
import com.tisl.mpl.marketplacecommerceservices.service.impl.DefaultMyStyleProfileServiceImpl;


/**
 * @author TCS
 *
 */
@UnitTest
public class DefaultMyStyleProfileServiceImplUnitTest
{
	@Autowired
	private ModelService modelService;
	@Autowired
	private UserService userService;
	private static final Logger LOG = Logger.getLogger(DefaultMyStyleProfileServiceImplUnitTest.class);


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		final DefaultMyStyleProfileServiceImpl defaultMyStyleProfileServiceImpl = new DefaultMyStyleProfileServiceImpl();

		final MyStyleProfileDao myStyleProfileDao = Mockito.mock(MyStyleProfileDao.class);
		defaultMyStyleProfileServiceImpl.setMyStyleProfileDao(myStyleProfileDao);

		//		this.productService = Mockito.mock(ProductService.class);
		//		this.categoryService = Mockito.mock(CategoryService.class);
		this.modelService = Mockito.mock(ModelService.class);
		this.userService = Mockito.mock(UserService.class);
	}

	@Test
	public void testSaveCategoryData()
	{
		final CustomerModel customer = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);

		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();

		final MplStyleProfileModel oModel = customer.getMyStyleProfile();
		oModel.setPreferredCategory(categoryList);
		oModel.setIsStyleProfileCreated(Boolean.FALSE);
		customer.setMyStyleProfile(oModel);

		Mockito.doNothing().when(modelService).save(oModel);
		Mockito.doNothing().when(modelService).save(customer);
		LOG.info("Method : testSaveCategoryData >>>>>>>");
	}

	@Test
	public void testSaveBrandData()
	{
		final CustomerModel customer = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);

		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();

		final MplStyleProfileModel oModel = customer.getMyStyleProfile();
		oModel.setPreferredCategory(categoryList);
		oModel.setIsStyleProfileCreated(Boolean.FALSE);
		customer.setMyStyleProfile(oModel);

		Mockito.doNothing().when(modelService).save(oModel);
		Mockito.doNothing().when(modelService).save(customer);
		LOG.info("Method : testSaveCategoryData >>>>>>>");
	}

	@Test
	public void testSaveGenderData()
	{
		final CustomerModel customer = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);

		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();

		final MplStyleProfileModel oModel = customer.getMyStyleProfile();
		oModel.setPreferredCategory(categoryList);
		oModel.setIsStyleProfileCreated(Boolean.FALSE);
		customer.setMyStyleProfile(oModel);

		Mockito.doNothing().when(modelService).save(oModel);
		Mockito.doNothing().when(modelService).save(customer);
		LOG.info("Method : testSaveCategoryData >>>>>>>");
	}
}
