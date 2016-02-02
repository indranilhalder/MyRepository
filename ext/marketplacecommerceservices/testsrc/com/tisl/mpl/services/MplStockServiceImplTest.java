/**
 *
 */
package com.tisl.mpl.services;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ordersplitting.model.StockLevelModel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
		MplStockServiceImpl mplStockServiceImpl = new MplStockServiceImpl();
		mplStockServiceImpl.setMplStockDao(mplStockDao);
	}

	@Test
	public void testmplPriceRowServiceimpl()
	{
		final List<StockLevelModel> stockDetails = new ArrayList<StockLevelModel>();
		final String skuID = "skuID";
		final String skuIDs = "skuIDs";

		given(mplStockDao.getStockDetail(skuID)).willReturn(stockDetails);

		given(mplStockDao.getStockDetail(skuIDs)).willReturn(stockDetails);





	}

}
