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
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.tisl.mpl.core.model.BrandModel;
import com.tisl.mpl.marketplacecommerceservices.service.MplPriceRowService;
import com.tisl.mpl.model.SellerInformationModel;


/**
 *
 * @author TCS
 *
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CustomProductBasicPopulatorTest
{
	private CustomProductBasicPopulator productBasicPopulator;
	@Mock
	private MplPriceRowService mplPriceRowService;

	@Before
	public void setUp()
	{
		productBasicPopulator = new CustomProductBasicPopulator();
		productBasicPopulator.setMplPriceRowService(mplPriceRowService);
	}

	/**
	 * JUnit Testing for ProductBasicPopulator
	 */

	@Test
	public void testProductBasicPopulator()
	{

		final ProductModel productModel = Mockito.mock(ProductModel.class);
		final ModelService modelService = Mockito.mock(ModelService.class);
		final VariantProductModel variantProductModel = Mockito.mock(VariantProductModel.class);
		final ProductData productData = Mockito.mock(ProductData.class);
		final Object object = Mockito.mock(Object.class);
		given(modelService.getAttributeValue(variantProductModel, "name")).willReturn(object);
		given(modelService.getAttributeValue(variantProductModel, "MANUFACTURERNAME")).willReturn(object);
		given(modelService.getAttributeValue(variantProductModel, "LISTINGID")).willReturn(object);
		given(modelService.getAttributeValue(variantProductModel, "MRP")).willReturn(object);
		final Collection<SellerInformationModel> sellerInformationList = new ArrayList<SellerInformationModel>();
		final SellerInformationModel sellerInformationModel = Mockito.mock(SellerInformationModel.class);
		sellerInformationList.add(sellerInformationModel);
		given(productModel.getSellerInformationRelator()).willReturn(sellerInformationList);
		given(sellerInformationModel.getSellerArticleSKU()).willReturn("Ussid");
		given(sellerInformationModel.getSellerName()).willReturn("Name");
		final Collection<BrandModel> brands = new ArrayList<BrandModel>();
		final BrandModel brandModel = Mockito.mock(BrandModel.class);
		brands.add(brandModel);
		given(brandModel.getName()).willReturn("BrandName");
		given(brandModel.getTypeDescription()).willReturn("BrandTypeDescription");
		final DeliveryModeModel deliveryModeModel = Mockito.mock(DeliveryModeModel.class);
		given(deliveryModeModel.getCode()).willReturn("Code");
		given(deliveryModeModel.getDescription()).willReturn("Description");
		given(deliveryModeModel.getName()).willReturn("Name");
		final PriceData priceData = Mockito.mock(PriceData.class);
		final PriceData pData = Mockito.mock(PriceData.class);
		final PriceDataFactory priceDataFactory = Mockito.mock(PriceDataFactory.class);
		given(priceData.getValue()).willReturn(new BigDecimal(0.0));
		final PriceDataType priceType = Mockito.mock(PriceDataType.class);
		given(priceDataFactory.create(priceType, new BigDecimal(0.0), "INR")).willReturn(pData);
		productBasicPopulator.populate(productModel, productData);
	}

}
