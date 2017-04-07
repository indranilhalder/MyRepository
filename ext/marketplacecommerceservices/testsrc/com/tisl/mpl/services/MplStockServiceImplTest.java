/**
 *
 */
package com.tisl.mpl.services;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ordersplitting.model.StockLevelModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.MplStockDao;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplStockServiceImpl;


/**
 * @author TCS
 *
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class MplStockServiceImplTest
{

	@Mock
	private MplStockDao mplStockDao;

	public void setUp()
	{
		final MplStockServiceImpl mplStockServiceImpl = new MplStockServiceImpl();
		mplStockServiceImpl.setMplStockDao(mplStockDao);
	}

	@Test
	public void testmplPriceRowServiceimpl()
	{
		//TISSEC-50
		final StockLevelModel stockDetails = new StockLevelModel();
		final String skuID = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter skuID
		final String skuIDs = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter skuIDs

		given(mplStockDao.getStockDetail(skuID)).willReturn(stockDetails);

		given(mplStockDao.getStockDetail(skuIDs)).willReturn(stockDetails);





	}

}
