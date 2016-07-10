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

		//TISSEC-50
		deliveryModeModel.setName("");//TODO : Please enter name
		deliveryModeModel.setCode("");//TODO : Please enter code
		zoneDeliveryModeModel.setDeliveryMode(deliveryModeModel);

		productModelMock.setDescription("");//TODO : Please enter description
		productModelMock.setName("");//TODO : Please enter name
		orderModel.setTotalTaxValuesInternal("");//TODO : Please enter total tax value
		orderModel.setDeliveryCost(cost);
		orderModel.setConvenienceCharges(convCharge);
		orderModel.setStatus(orderStatus);
		orderModel.setCode("");//TODO : Please enter code

		orderEntryModelMock.setOrder(orderModel);
		orderEntryModelMock.setCurrDelCharge(curDelCharge);
		orderEntryModelMock.setOrder(orderModel);
		orderEntryModelMock.setSelectedUSSID("");//TODO : Please enter selected ussid
		orderEntryModelMock.setDeliveryMode(deliveryModeModel);
		orderEntryModelMock.setBasePrice(baseprice);
		orderEntryModelMock.setOrder(orderModel);
		orderEntryModelMock.setProductPromoCode("");//TODO : Please enter product promo code
		orderEntryModelMock.setProdLevelPercentageDisc(discount);
		orderEntryModelMock.setNetAmountAfterAllDisc(actualValue);
		orderEntryModelMock.setCurrDelCharge(new Double(1520));
		orderEntryModelMock.setProduct(productModelMock);
		orderEntryModelMock.setCartPromoCode("");//TODO : Please enter cart promo code
		orderEntryModelMock.setCartLevelDisc(discount);
		orderEntryModelMock.setGiveAway(new Boolean(true));
		orderEntryModelMock.setAssociatedItems(itemList);

		sellerInfoModelMock.setSellerSKU("");//TODO : Please enter seller sku
		sellerInfoModelMock.setSellerID("");//TODO : Please enter seller id
		sellerInfoModelMock.setProductSource(productModelMock);

		richAttrModelMock.setSellerFulfilledShippingCharge(shippingCharge);
		richAttrModelMock.setWarrantyPeriod("");//TODO : Please enter warranty period

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
		orderEntryModelMock.setOrderLineId("");//TODO : Please enter orderline id
		orderEntryModelMock.setTransactionID("");//TODO : Please enter transaction id

		//customOmsOrderLinePopulator.populate(orderEntryModelMock, orderLineMock);

	}

	@Test
	public void TestpopulateRichAttribute(final List<RichAttributeModel> richAttributeModelListMock, final OrderLine target)
	{
		//final List<RichAttributeModel> richAttributeModelListMock = new ArrayList<RichAttributeModel>();
		final RichAttributeModel richAttributeModelMock = Mockito.mock(RichAttributeModel.class);
		//final OrderLine orderLineMock = Mockito.mock(OrderLine.class);
		//final ItemModel itemModelMock = Mockito.mock(ItemModel.class);

		//TISSEC-50
		richAttributeModelMock.setExchangeAllowedWindow("");//TODO : Please enter exchange allowed window
		richAttributeModelMock.setWarrantyPeriod("");//TODO : Please enter warranty period
		richAttributeModelMock.setReturnWindow("");//TODO : Please enter return window
		richAttributeModelMock.setReplacementWindow("");//TODO : Please enter replacement window
		richAttributeModelMock.setExchangeAllowedWindow("");//TODO : Please enter exchange allowed window


		target.setCancellationAllowed("");//TODO : Please enter cancellation allowed
		target.setReturnsAllowed("");//TODO : Please enter returns allowed
		target.setReplacementAllowed("");//TODO : Please enter replacement allowed
		target.setExchangeAllowed("");//TODO : Please enter exchange allowed

		richAttributeModelListMock.add(richAttributeModelMock);
		//customOmsOrderLinePopulator.populateRichAttribute(richAttributeModelListMock, orderLineMock);

	}

}
