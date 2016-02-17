/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.stock.strategies.CommerceAvailabilityCalculationStrategy;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.stock.strategy.StockLevelProductStrategy;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.tisl.mpl.marketplacecommerceservices.service.ExtStockService;


/**
 * @author TCS
 *
 */
public class DefaultExtCommerceStockServiceTest
{
	private DefaultExtCommerceStockService defaultExtCommerceStockService;

	@Mock
	private ExtStockService stockService;
	@Mock
	private CommerceAvailabilityCalculationStrategy commerceStockLevelCalculationStrategy;
	@Mock
	private WarehouseSelectionStrategy warehouseSelectionStrategy;
	@Mock
	private StockLevelProductStrategy stockLevelProductStrategy;


	@Before
	public void setUp()
	{
		defaultExtCommerceStockService = new DefaultExtCommerceStockService();

		defaultExtCommerceStockService.setStockLevelProductStrategy(stockLevelProductStrategy);
		defaultExtCommerceStockService.setWarehouseSelectionStrategy(warehouseSelectionStrategy);
		defaultExtCommerceStockService.setCommerceStockLevelCalculationStrategy(commerceStockLevelCalculationStrategy);
		defaultExtCommerceStockService.setStockService(stockService);
	}



	@Test
	public void testGetStockLevelStatusForProductAndBaseStore()
	{
		final StockLevelStatus stockLevelStatMock = Mockito.mock(StockLevelStatus.class);
		final BaseStoreModel baseStoreMock = Mockito.mock(BaseStoreModel.class);
		final String articleSKUID = new String();
		given(defaultExtCommerceStockService.getStockLevelStatusForProductAndBaseStore(baseStoreMock, articleSKUID)).willReturn(
				stockLevelStatMock);
	}

	@Test
	public void testGetStockLevelStatusForProductAndBaseStore1()
	{
		final Long availabilityTime = Mockito.mock(Long.class);

		final String articleSKUID = new String();
		final BaseStoreModel baseStoreMock = Mockito.mock(BaseStoreModel.class);
		given(defaultExtCommerceStockService.getStockLevelForProductAndBaseStore(articleSKUID, baseStoreMock)).willReturn(
				availabilityTime);
	}

	@Test
	public void testgetStockLevelStatusForProductAndPointOfService()
	{
		final String articleSKUID = new String();
		final PointOfServiceModel pointOfService = Mockito.mock(PointOfServiceModel.class);
		final StockLevelStatus stockLevelStatMock = Mockito.mock(StockLevelStatus.class);

		given(defaultExtCommerceStockService.getStockLevelStatusForProductAndPointOfService(articleSKUID, pointOfService))
				.willReturn(stockLevelStatMock);
	}

	@Test
	public void testgetStockLevelStatusForProductAndPointOfService1()
	{
		final String articleSKUID = new String();
		final PointOfServiceModel pointOfService = Mockito.mock(PointOfServiceModel.class);
		final Long availabilityTime = Mockito.mock(Long.class);

		given(defaultExtCommerceStockService.getStockLevelForProductAndPointOfService(articleSKUID, pointOfService)).willReturn(
				availabilityTime);
	}

}
