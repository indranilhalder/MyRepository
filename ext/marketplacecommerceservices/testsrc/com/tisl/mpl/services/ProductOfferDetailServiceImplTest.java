/**
 *
 */
package com.tisl.mpl.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.tisl.mpl.marketplacecommerceservices.daos.ProductOfferDetailDao;
import com.tisl.mpl.marketplacecommerceservices.service.ProductOfferDetailService;


/**
 * @author TCS
 *
 */

//jUnit for displaying Non HMC configurable offer messages , TPR-589
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ProductOfferDetailServiceImplTest
{

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	private ProductOfferDetailDao productOfferDetailDao;

	@Mock
	private ProductOfferDetailService ProductOfferDetailService;

	@Test
	public void showOfferMessage()
	{
		//final String productCode = MarketplacecclientservicesConstants.EMPTY;//TODO :Please enter product code
		final String productCode = "987654321";

		final Map<String, Map<String, String>> stockLevelStatMock = new HashMap<String, Map<String, String>>();

		assertNotNull(ProductOfferDetailService.showOfferMessage(productCode));
		assertEquals(stockLevelStatMock, ProductOfferDetailService.showOfferMessage(productCode));
	}
}
