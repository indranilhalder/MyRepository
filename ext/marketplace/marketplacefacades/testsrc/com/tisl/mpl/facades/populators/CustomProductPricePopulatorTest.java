/**
 *
 */
package com.tisl.mpl.facades.populators;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.tisl.mpl.marketplacecommerceservices.service.MplPriceRowService;


/**
 * @author TCS
 *
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CustomProductPricePopulatorTest
{
	private CustomProductPricePopulator pricePopulator;
	@Mock
	private CommercePriceService commercePriceService;
	@Mock
	private MplPriceRowService mplPriceRowService;

	@Mock
	private PriceDataFactory priceDataFactory;

	@Before
	public void setUp()
	{
		pricePopulator = new CustomProductPricePopulator();
		pricePopulator.setCommercePriceService(commercePriceService);
		pricePopulator.setMplPriceRowService(mplPriceRowService);
		pricePopulator.setPriceDataFactory(priceDataFactory);
	}

	/**
	 * JUnit testing for PricePoopulator
	 */

	@Test
	public void testPricePoopulator()
	{
		final ProductData productData = Mockito.mock(ProductData.class);
		final List<SellerInformationData> sellerDataList = new ArrayList<SellerInformationData>();
		final SellerInformationData sellerInformationData = Mockito.mock(SellerInformationData.class);
		final ProductModel productModel = Mockito.mock(ProductModel.class);
		sellerDataList.add(sellerInformationData);
		given(productData.getSeller()).willReturn(sellerDataList);
		final Date date = new Date();

		//final GenericUtilityMethods abstractImpl = Mockito.mock(GenericUtilityMethods.class);
		//final Class<GenericUtilityMethods> genericUtilityMethods = GenericUtilityMethods.class;
		//final String sellerArticleSKUs = "3113131311341";
		//final PriceRowModel PriceRowModel = Mockito.mock(PriceRowModel.class);
		//final Map<String, PriceRowModel> mopMap = new HashMap<String, PriceRowModel>();
		final PriceRowModel priceRowModel = Mockito.mock(PriceRowModel.class);
		given(priceRowModel.getPrice()).willReturn(new Double(0.0));
		given(priceRowModel.getSpecialPrice()).willReturn(new Double(0.0));
		given(priceRowModel.getPromotionStartDate()).willReturn(date);
		given(priceRowModel.getPromotionEndDate()).willReturn(date);
		final PriceData priceData = Mockito.mock(PriceData.class);
		final PriceData pData = Mockito.mock(PriceData.class);
		final PriceDataFactory priceDataFactory = Mockito.mock(PriceDataFactory.class);
		given(priceData.getValue()).willReturn(new BigDecimal(0.0));
		final PriceDataType priceType = Mockito.mock(PriceDataType.class);
		given(priceDataFactory.create(priceType, new BigDecimal(0.0), "INR")).willReturn(pData);
		pricePopulator.populate(productModel, productData);
	}
}
