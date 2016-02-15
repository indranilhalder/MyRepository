/**
 *
 */
package com.tisl.mpl.services;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPriceRowDao;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplPriceRowServiceImpl;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class MplPriceRowServiceImplTest
{

	@Mock
	private MplPriceRowDao mplPriceRowDao;
	@Autowired
	private CatalogVersionService catalogVersionService;

	public void setUp()
	{
		final MplPriceRowServiceImpl mplPriceRowServiceimpl = new MplPriceRowServiceImpl();
		mplPriceRowServiceimpl.setMplPriceRowDao(mplPriceRowDao);
	}

	@Test
	public void testmplPriceRowServiceimpl()
	{
		final CatalogVersionModel catalogVersionModel = getCatalogVersion();

		final List<PriceRowModel> priceRowDetails = new ArrayList<PriceRowModel>();
		final String skuID = "skuID";
		final String skuIDs = "skuIDs";

		given(mplPriceRowDao.getPriceRowDetail(catalogVersionModel, skuID)).willReturn(priceRowDetails);

		given(mplPriceRowDao.getAllPriceRowDetail(catalogVersionModel, skuIDs)).willReturn(priceRowDetails);

		final ProductModel product = Mockito.mock(ProductModel.class);
		final List<SellerInformationModel> sellerinfolist = new ArrayList<SellerInformationModel>();
		final PriceRowModel pricedetail = Mockito.mock(PriceRowModel.class);

		given(product.getSellerInformationRelator()).willReturn(sellerinfolist);

		final SellerInformationModel seller = Mockito.mock(SellerInformationModel.class);

		given(seller.getSellerArticleSKU()).willReturn(skuID);

		given(mplPriceRowDao.getPriceRowDetailForSKUWithStockCheck(catalogVersionModel, skuID)).willReturn(pricedetail);



	}

	/**
	 * @return
	 *
	 */
	private CatalogVersionModel getCatalogVersion()
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_ID,
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_VERSION);
		return catalogVersionModel;
	}
}
