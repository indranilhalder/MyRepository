/**
 *
 */
package com.tisl.mpl.integration.oms.order.populators.test;

import de.hybris.platform.commerceservices.externaltax.TaxCodeStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.integration.commons.services.OndemandTaxCalculationService;
import de.hybris.platform.integration.oms.order.service.ProductAttributeStrategy;
import de.hybris.platform.integration.oms.order.strategies.OrderEntryNoteStrategy;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.hybris.oms.domain.order.OrderLine;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.integration.oms.order.populators.CustomOmsOrderLinePopulator;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
public class CustomOmsOrderLinePopulatorTest
{

	@Mock
	private CustomOmsOrderLinePopulator customOmsOrderLinePopulator;
	@Mock
	private MplSellerInformationService mplSellerInformationService;
	@Mock
	private TaxCodeStrategy taxCodeStrategy;
	@Mock
	private ProductAttributeStrategy productAttributeStrategy;
	@Mock
	private OrderEntryNoteStrategy orderEntryNoteStrategy;
	@Mock
	private OndemandTaxCalculationService ondemandTaxCalculationService;


	@Before
	public void setUp()
	{
		customOmsOrderLinePopulator = new CustomOmsOrderLinePopulator();
		customOmsOrderLinePopulator.setMplSellerInformationService(mplSellerInformationService);
		customOmsOrderLinePopulator.setTaxCodeStrategy(taxCodeStrategy);
		customOmsOrderLinePopulator.setOndemandTaxCalculationService(ondemandTaxCalculationService);
		customOmsOrderLinePopulator.setProductAttributeStrategy(productAttributeStrategy);
		customOmsOrderLinePopulator.setOrderEntryNoteStrategy(orderEntryNoteStrategy);

	}


	@Test
	public void populate() throws ConversionException
	{
		final OrderEntryModel orderEntryModelMock = Mockito.mock(OrderEntryModel.class);
		final OrderLine orderLineMock = Mockito.mock(OrderLine.class);

		final SellerInformationModel sellerInfoModelMock = Mockito.mock(SellerInformationModel.class);
		final RichAttributeModel richAttrModelMock = Mockito.mock(RichAttributeModel.class);
		final ProductModel productModelMock = Mockito.mock(ProductModel.class);
		final AbstractOrderModel orderModel = Mockito.mock(AbstractOrderModel.class);
		final de.hybris.platform.core.enums.OrderStatus orderStatus = Mockito.mock(de.hybris.platform.core.enums.OrderStatus.class);

		//final DeliveryFulfillModesEnum delFulfillMode = Mockito.mock(DeliveryFulfillModesEnum.class);
		final DeliveryModeModel deliveryModeModel = Mockito.mock(DeliveryModeModel.class);
		final MplZoneDeliveryModeValueModel zoneDeliveryModeModel = Mockito.mock(MplZoneDeliveryModeValueModel.class);


		// OrderStatus orderstatus = Mockito.mock(OrderStatus.class);
		final List<String> itemList = new ArrayList<String>();
		final String item = new String("new item");
		itemList.add(item);


		final Integer shippingCharge = new Integer(234);
		final Double baseprice = new Double(2354.602);
		final Double curDelCharge = new Double(350);
		final Double cost = new Double(1000);
		final Double convCharge = new Double(150);
		final Double discount = new Double(12);
		final Double actualValue = new Double(1200);
		final List<RichAttributeModel> richAttributeModelList = new ArrayList<RichAttributeModel>();

		deliveryModeModel.setName("HOme");
		deliveryModeModel.setCode("56777");
		zoneDeliveryModeModel.setDeliveryMode(deliveryModeModel);

		productModelMock.setDescription("Product delivered");
		productModelMock.setName("shirt");
		orderModel.setTotalTaxValuesInternal("10");
		orderModel.setDeliveryCost(cost);
		orderModel.setConvenienceCharges(convCharge);
		orderModel.setStatus(orderStatus);
		orderModel.setCode("o1234");

		orderEntryModelMock.setOrder(orderModel);
		orderEntryModelMock.setCurrDelCharge(curDelCharge);
		orderEntryModelMock.setOrder(orderModel);
		orderEntryModelMock.setSelectedUSSID("123653098765485130011716");
		orderEntryModelMock.setDeliveryMode(deliveryModeModel);
		orderEntryModelMock.setBasePrice(baseprice);
		orderEntryModelMock.setOrder(orderModel);
		orderEntryModelMock.setProductPromoCode("promo");
		orderEntryModelMock.setProdLevelPercentageDisc(discount);
		orderEntryModelMock.setNetAmountAfterAllDisc(actualValue);
		orderEntryModelMock.setCurrDelCharge(new Double(1520));
		orderEntryModelMock.setProduct(productModelMock);
		orderEntryModelMock.setCartPromoCode("PC8900");
		orderEntryModelMock.setCartLevelDisc(discount);
		orderEntryModelMock.setGiveAway(new Boolean(true));
		orderEntryModelMock.setAssociatedItems(itemList);

		sellerInfoModelMock.setSellerSKU("987654321");
		sellerInfoModelMock.setSellerID("1234456");
		sellerInfoModelMock.setProductSource(productModelMock);

		richAttrModelMock.setSellerFulfilledShippingCharge(shippingCharge);
		richAttrModelMock.setWarrantyPeriod("Two years");

		richAttributeModelList.add(richAttrModelMock);

		testPopulateData(orderEntryModelMock, orderLineMock);
		TestpopulateRichAttribute(richAttributeModelList, orderLineMock);


		sellerInfoModelMock.setRichAttribute(richAttributeModelList);
	}

	@Test
	public void testPopulateData(final OrderEntryModel orderEntryModelMock, final OrderLine orderLineMock)
	{
		/*
		 * final OrderEntryModel orderEntryModelMock = Mockito.mock(OrderEntryModel.class); final OrderLine orderLineMock
		 * = Mockito.mock(OrderLine.class);
		 */

		orderEntryModelMock.setConvenienceChargeApportion(new Double(1200));
		orderEntryModelMock.setGiveAway(new Boolean(true));
		orderEntryModelMock.setOrderLineId("OLID1234");
		orderEntryModelMock.setTransactionID("TranID1234");

		//customOmsOrderLinePopulator.populate(orderEntryModelMock, orderLineMock);

	}

	@Test
	public void TestpopulateRichAttribute(final List<RichAttributeModel> richAttributeModelListMock, final OrderLine target)
	{
		//final List<RichAttributeModel> richAttributeModelListMock = new ArrayList<RichAttributeModel>();
		final RichAttributeModel richAttributeModelMock = Mockito.mock(RichAttributeModel.class);
		//final OrderLine orderLineMock = Mockito.mock(OrderLine.class);
		//final ItemModel itemModelMock = Mockito.mock(ItemModel.class);


		richAttributeModelMock.setExchangeAllowedWindow("2 hours");
		richAttributeModelMock.setWarrantyPeriod("Six months");
		richAttributeModelMock.setReturnWindow("two Dyas");
		richAttributeModelMock.setReplacementWindow("Five Days");
		richAttributeModelMock.setExchangeAllowedWindow("Four Days");


		target.setCancellationAllowed("10 Dyas");
		target.setReturnsAllowed("5 days");
		target.setReplacementAllowed("5 Days");
		target.setExchangeAllowed("2 Days");

		richAttributeModelListMock.add(richAttributeModelMock);
		//customOmsOrderLinePopulator.populateRichAttribute(richAttributeModelListMock, orderLineMock);

	}

}
