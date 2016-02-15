/**
 *
 */
package com.tisl.mpl.seller.product.facades.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.enums.ClickAndCollectEnum;
import com.tisl.mpl.core.enums.DeliveryFulfillModesEnum;
import com.tisl.mpl.core.enums.ExpressDeliveryEnum;
import com.tisl.mpl.core.enums.HomeDeliveryEnum;
import com.tisl.mpl.core.enums.PaymentModesEnum;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.RichAttributeData;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class DefaultBuyBoxFacadeTest
{
	@Autowired
	private ConfigurationService configurationService;
	@Resource
	private BuyBoxService buyBoxService;
	@Resource(name = "mplDeliveryCostService")
	private MplDeliveryCostService mplDeliveryCostService;
	@Resource(name = "productDetailsHelper")
	private ProductDetailsHelper productDetailsHelper;


	private final BuyBoxFacadeImpl facadeImpl = new BuyBoxFacadeImpl();

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		//		facadeImpl.setPriceDataFactory(priceDataFactory);
		facadeImpl.setMplDeliveryCostService(mplDeliveryCostService);
	}

	@Test
	public void testBuyBoxPriceDetails()
	{
		final BuyBoxData buyboxData = new BuyBoxData();
		final String productCode = "987654321";
		assertNotNull(buyBoxService.buyboxPrice(productCode));
		assertFalse(buyBoxService.buyboxPrice(productCode).isEmpty());
		buyboxData.setAllOOStock(MarketplaceFacadesConstants.N);
		assertEquals(MarketplaceFacadesConstants.N, buyboxData.getAllOOStock());
		assertNotNull(buyBoxService.buyboxPrice(productCode).get(0).getSpecialPrice());
		final double spPrice = buyBoxService.buyboxPrice(productCode).get(0).getSpecialPrice().doubleValue();
		final double roundedSpPrice = Math.round((spPrice) * 100) / 100;
		final PriceData priceData = productDetailsHelper.formPriceData(new Double(roundedSpPrice));
		buyboxData.setPrice(priceData);
		assertNotNull(buyboxData.getPrice());
		assertNotNull(buyBoxService.buyboxPrice(productCode).get(0).getSellerName());
		assertNotNull(buyBoxService.buyboxPrice(productCode).get(0).getSellerArticleSKU());
		assertNotNull(buyBoxService.buyboxPrice(productCode).get(0).getAvailable());
		assertNotNull(buyBoxService.buyboxPrice(productCode).get(0).getMrp());
		final PriceData mrpPriceData = productDetailsHelper.formPriceData(new Double(buyBoxService.buyboxPrice(productCode).get(0)
				.getMrp().doubleValue()));
		buyboxData.setMrpPriceValue(mrpPriceData);
		assertNotNull(buyboxData.getMrpPriceValue());
		final int sellerSize = 1;
		assertEquals(sellerSize, buyBoxService.buyboxPrice(productCode).size() - 1);
		assertNotNull(buyBoxService.buyboxPrice(productCode).get(1).getSpecialPrice());
		assertEquals(100d, buyBoxService.buyboxPrice(productCode).get(1).getSpecialPrice().doubleValue(), 0);
		final double roundedMinPrice = Math
				.round(buyBoxService.buyboxPrice(productCode).get(1).getSpecialPrice().doubleValue() * 100) / 100;
		final PriceData minPriceData = productDetailsHelper.formPriceData(new Double(roundedMinPrice));
		buyboxData.setMinPrice(minPriceData);
		assertNotNull(buyboxData.getMinPrice());
		assertEquals(buyboxData, facadeImpl.buyboxPrice(productCode));
	}

	@Test
	public void testGetsellersDetails()
	{
		final List<SellerInformationData> SellerInformationDataList = new ArrayList<SellerInformationData>();
		final BuyBoxData buyboxData = new BuyBoxData();
		final String productCode = "987654321";
		assertFalse(buyBoxService.getsellersDetails(productCode).isEmpty());
		final BuyBoxModel buyBox = new BuyBoxModel();
		final RichAttributeModel rich = new RichAttributeModel();
		final Map<BuyBoxModel, RichAttributeModel> resultMap = new HashMap<BuyBoxModel, RichAttributeModel>();
		assertNotNull(resultMap.entrySet());
		assertFalse(resultMap.entrySet().isEmpty());
		//final Map<BuyBoxModel, RichAttributeModel> entry = new HashMap<BuyBoxModel, RichAttributeModel>();
		//TODO


		final SellerInformationData sellerData = new SellerInformationData();
		assertNotNull(buyBox.getAvailable());
		final String availableStock = "10";
		buyboxData.setAllOOStock(availableStock);
		assertNotNull(buyBox.getWeightage());
		assertNotNull(buyBox.getSellerId());
		assertNotNull(buyBox.getSpecialPrice());
		final Double spPrice = buyBox.getSpecialPrice();
		assertNotNull(productDetailsHelper.formPriceData(spPrice));
		assertNotNull(productDetailsHelper.formPriceData(new Double(0.0D)));
		assertNotNull(buyBox.getPrice());
		final Double price = buyBox.getSpecialPrice();
		assertNotNull(productDetailsHelper.formPriceData(price));
		assertNotNull(productDetailsHelper.formPriceData(new Double(0.0D)));
		assertNotNull(buyBox.getMrp());
		final Double mrp = buyBox.getMrp();
		assertNotNull(productDetailsHelper.formPriceData(mrp));
		assertNotNull(productDetailsHelper.formPriceData(new Double(0.0D)));
		assertNotNull(buyBox.getSellerArticleSKU());
		assertFalse(buyBox.getSellerArticleSKU().isEmpty());
		assertNotNull(buyBox.getSellerName());
		assertFalse(buyBox.getSellerName().isEmpty());
		final String articleSKU = buyBox.getSellerArticleSKU();
		assertNotNull(productDetailsHelper.getDeliveryModeLlist(rich, articleSKU));
		assertNotNull(rich.getShippingModes());
		assertNotNull(rich.getShippingModes().getCode());
		assertFalse(rich.getShippingModes().getCode().isEmpty());
		assertNotNull(rich.getDeliveryFulfillModes());
		assertNotNull(rich.getDeliveryFulfillModes());
		assertEquals(rich.getDeliveryFulfillModes(), DeliveryFulfillModesEnum.TSHIP);
		assertNotNull(rich.getDeliveryFulfillModes().getCode());
		assertFalse(rich.getDeliveryFulfillModes().getCode().isEmpty());
		assertNotNull(rich.getPaymentModes());
		assertNotNull(rich.getPaymentModes().getCode());
		assertEquals((PaymentModesEnum.COD).toString(), rich.getPaymentModes().getCode());
		assertEquals((PaymentModesEnum.BOTH).toString(), rich.getPaymentModes().getCode());
		assertNotNull(rich.getReturnWindow());
		assertFalse(rich.getReturnWindow().isEmpty());
		assertNotNull(rich.getReplacementWindow());
		assertFalse(rich.getReplacementWindow().isEmpty());
		SellerInformationDataList.add(sellerData);
		assertEquals(SellerInformationDataList, facadeImpl.getsellersDetails(productCode));
	}

	@Test
	public void testGetRichAttributeDetails()
	{
		final RichAttributeData richData = new RichAttributeData();
		final ProductModel productModel = new ProductModel();
		final String buyboxid = "12345";
		assertNotNull(configurationService.getConfiguration().getString("attribute.new.display"));
		assertNotNull(productModel.getSellerInformationRelator());
		final SellerInformationModel seller = new SellerInformationModel();
		assertNotNull(seller.getSellerArticleSKU());
		assertFalse(seller.getSellerArticleSKU().isEmpty());
		assertNotNull(seller.getRichAttribute());
		assertFalse(seller.getRichAttribute().isEmpty());
		final RichAttributeModel rich = new RichAttributeModel();
		assertNotNull(rich.getPaymentModes());
		assertNotNull(rich.getPaymentModes().getCode());
		assertEquals((PaymentModesEnum.COD).toString(), rich.getPaymentModes().getCode());
		assertEquals((PaymentModesEnum.BOTH).toString(), rich.getPaymentModes().getCode());
		assertNotNull(rich.getDeliveryFulfillModes());
		assertNotNull(rich.getDeliveryFulfillModes().getCode());
		assertEquals(HomeDeliveryEnum.YES, rich.getHomeDelivery());
		assertEquals(ExpressDeliveryEnum.YES, rich.getExpressDelivery());
		assertEquals(ClickAndCollectEnum.YES, rich.getClickAndCollect());
		assertNotNull(seller.getStartDate());
		assertNotNull(seller.getStartDate());
		assertEquals(richData, facadeImpl.getRichAttributeDetails(productModel, buyboxid));
	}

}
