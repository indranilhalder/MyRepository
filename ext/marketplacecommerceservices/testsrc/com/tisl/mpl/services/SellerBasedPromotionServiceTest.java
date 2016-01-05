/**
 *
 */
package com.tisl.mpl.services;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.promotion.dao.SellerBasedPromotionDao;
import com.tisl.mpl.promotion.service.SellerBasedPromotionServiceImpl;


/**
 * @author TCS
 *
 */
@UnitTest
public class SellerBasedPromotionServiceTest
{
	private SellerBasedPromotionDao sellerBasedPromotionDao;
	private SellerBasedPromotionServiceImpl sellerBasedPromotionServiceImpl;
	private SellerInformationModel sellerModel;
	private CatalogVersionModel version;
	private final static String CODE = "123654720";


	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		this.sellerBasedPromotionServiceImpl = new SellerBasedPromotionServiceImpl();
		this.sellerBasedPromotionDao = Mockito.mock(SellerBasedPromotionDao.class);
		this.sellerBasedPromotionServiceImpl.setSellerBasedPromotionDao(sellerBasedPromotionDao);
		this.version = Mockito.mock(CatalogVersionModel.class);
		this.sellerModel = Mockito.mock(SellerInformationModel.class);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void fetchSellerInformation()
	{
		final List<SellerInformationModel> sellerData = Arrays.asList(sellerModel);
		Mockito.when(sellerBasedPromotionDao.fetchSellerInformation(CODE, version)).thenReturn(sellerData);
		final List<SellerInformationModel> actual = sellerBasedPromotionServiceImpl.fetchSellerInformation(CODE, version);
		Assert.assertEquals(actual, sellerData);
	}
}
