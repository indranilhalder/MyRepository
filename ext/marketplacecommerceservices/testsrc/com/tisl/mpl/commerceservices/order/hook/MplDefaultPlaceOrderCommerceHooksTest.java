/**
 *
 */
package com.tisl.mpl.commerceservices.order.hook;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.JuspayEBSResponseDataModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplFraudModelService;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.util.OrderStatusSpecifier;


/**
 * @author TCS
 *
 */
@SuppressWarnings("deprecation")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class MplDefaultPlaceOrderCommerceHooksTest
{
	@Mock
	private final OrderModel orderModel = Mockito.mock(OrderModel.class);
	@Mock
	private final CODPaymentInfoModel codPaymentInfo = Mockito.mock(CODPaymentInfoModel.class);
	@Mock
	private MplOrderDao mplOrderDao;
	@Mock
	private MplFraudModelService mplFraudModelService;
	@Autowired
	private MplCommerceCartService mplCommerceCartService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private OrderStatusSpecifier orderStatusSpecifier;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		//this.mplDefaultPlaceOrderCommerceHooksTest = Mockito.mock(MplDefaultPlaceOrderCommerceHooksTest.class);
	}

	@Test
	public void afterPlaceOrder() throws InvalidCartException
	{
		final CommerceOrderResult commerceOrderResult = new CommerceOrderResult();
		final List<OrderModel> orderList = new ArrayList<OrderModel>();

		//TISSEC-50
		orderModel.setCode("");//TODO : Please enter code
		orderModel.setDeliveryCost(Double.valueOf(1234));
		orderModel.setFraudulent(Boolean.FALSE);

		orderModel.setGuid("");//TODO : Please enter guid
		orderModel.setPaymentInfo(codPaymentInfo);

		commerceOrderResult.setOrder(orderModel);
		orderList.add(orderModel);
		commerceOrderResult.setOrders(orderList);

		final ArrayList<JuspayEBSResponseDataModel> riskList = new ArrayList<JuspayEBSResponseDataModel>();
		final JuspayEBSResponseDataModel risk = new JuspayEBSResponseDataModel();

		//TISSEC-50
		risk.setEbs_bin_country("");//TODO : Please enter ebs bin country
		risk.setEbsRiskPercentage("");//TODO : Please enter ebs risk percentage
		riskList.add(risk);

		final MplPaymentAuditModel mplAudit = new MplPaymentAuditModel();
		mplAudit.setRiskData(riskList);

		given(mplOrderDao.getAuditList("")).willReturn(mplAudit);//TODO : Please enter guid
		riskList.addAll(mplAudit.getRiskData());
		mplFraudModelService.updateFraudModel(orderModel, mplAudit);
	}

	@Test
	public void beforeSubmitOrder() throws InvalidCartException
	{
		final CommerceOrderResult paramCommerceOrderResult = new CommerceOrderResult();

		orderModel.setCode("");//TODO : Please enter code
		orderModel.setDeliveryCost(Double.valueOf(1234));
		orderModel.setFraudulent(Boolean.FALSE);

		orderModel.setGuid("");//TODO : Please enter guid
		orderModel.setPaymentInfo(codPaymentInfo);
		createOrderEntry(orderModel);
		given(paramCommerceOrderResult.getOrder()).willReturn(orderModel);

		orderModel.setGuid("");//TODO : Please enter guid
		final String sequenceGeneratorApplicable = MarketplacecclientservicesConstants.GENERATE_ORDER_SEQUENCE.trim();
		if (sequenceGeneratorApplicable.equalsIgnoreCase(MarketplacecclientservicesConstants.TRUE))
		{

			final String orderIdSequence = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter order id sequence
			given(mplCommerceCartService.generateOrderId()).willReturn(orderIdSequence);
			orderModel.setCode(orderIdSequence);
		}
		else
		{
			final Random rand = new Random();
			orderModel.setCode(Integer.toString((rand.nextInt(900000000) + 100000000)));
		}

		final List<OrderModel> orderList = new ArrayList<OrderModel>();

		orderModel.setCode("");//TODO : Please enter code
		orderModel.setDeliveryCost(Double.valueOf(1234));
		orderModel.setFraudulent(Boolean.FALSE);

		orderModel.setGuid("");//TODO : Please enter guid
		orderList.add(orderModel);
		orderModel.setType("Parent");
		modelService.save(orderModel);

		final String realEbs = "payment.ebs.chek.realtimecall";
		realEbs.equalsIgnoreCase("Y");
		orderStatusSpecifier.setOrderStatus(orderModel, OrderStatus.PAYMENT_SUCCESSFUL);
		given(paramCommerceOrderResult.getOrder()).willReturn(orderModel);
	}

	private AbstractOrderModel createOrderEntry(final OrderModel Order)
	{
		final OrderModel orderModel = new OrderModel();
		final Map<String, List<AbstractOrderEntryModel>> sellerEntryMap = new HashMap<String, List<AbstractOrderEntryModel>>();
		final List<AbstractOrderEntryModel> abstractOrderEntryList = new ArrayList<AbstractOrderEntryModel>();
		final AbstractOrderEntryModel abstractOrderEntryModel = new AbstractOrderEntryModel();

		final ProductModel productModel = new ProductModel();
		productModel.setMrp(Double.valueOf(2000));

		productModel.setName("");//TODO : Please enter valid product name
		abstractOrderEntryModel.setProduct(productModel);
		abstractOrderEntryModel.setBasePrice(Double.valueOf(2000));
		abstractOrderEntryModel.setTotalPrice(Double.valueOf(2000));

		final BuyBoxModel buyBoxModel = new BuyBoxModel();
		buyBoxModel.setAvailable(Integer.valueOf(2500));
		buyBoxModel.setMrp(Double.valueOf(850));
		buyBoxModel.setPrice(Double.valueOf(2000));


		//TISSEC-50

		buyBoxModel.setProduct("");//TODO : Please enter product
		buyBoxModel.setSellerArticleSKU("");//TODO : Please enter seller article sku
		buyBoxModel.setSellerId("");//TODO : Please enter seller id
		buyBoxModel.setSellerName("");//TODO : Please enter seller name

		buyBoxModel.setWeightage(Double.valueOf(2));

		buyBoxModel.setSellerType("");//TODO : Please enter seller type

		final AddressModel addressModel = new AddressModel();


		addressModel.setFirstname("");//TODO : Please enter first name
		addressModel.setLastname("");//TODO : Please enter last name
		addressModel.setLine1("");//TODO : Please enter line1
		addressModel.setLine2("");//TODO : Please enter line2

		final List<PaymentModeModel> paymentModeList = new ArrayList<PaymentModeModel>();
		final PaymentModeModel paymentMode = new PaymentModeModel();

		paymentMode.setCode("");//TODO : Please enter code
		paymentModeList.add(paymentMode);

		final DeliveryModeModel deliveryMode = new DeliveryModeModel();

		deliveryMode.setCode("");//TODO : Please enter delivery mode
		deliveryMode.setSupportedPaymentModes(paymentModeList);

		final MplZoneDeliveryModeValueModel mplZoneDeliveryModeModel = new MplZoneDeliveryModeValueModel();

		mplZoneDeliveryModeModel.setProductCode("");//TODO : Please enter product code
		mplZoneDeliveryModeModel.setDeliveryMode(deliveryMode);

		abstractOrderEntryModel.setConvenienceChargeApportion(Double.valueOf(122));

		abstractOrderEntryModel.setDescription("");//TODO : Please enter description
		abstractOrderEntryModel.setQuantity(Long.valueOf(1));
		abstractOrderEntryModel.setCurrDelCharge(Double.valueOf(150));

		abstractOrderEntryModel.setOrderLineId("");//TODO : Please enter order line id

		abstractOrderEntryModel.setParentTransactionID("");//TODO : Please enter transaction id
		abstractOrderEntryModel.setDeliveryAddress(addressModel);
		abstractOrderEntryModel.setMplDeliveryMode(mplZoneDeliveryModeModel);

		abstractOrderEntryModel.setBuyBoxInfo(buyBoxModel);

		abstractOrderEntryModel.setEntryNumber(Integer.valueOf(0));
		abstractOrderEntryList.add(abstractOrderEntryModel);
		createPaymentTransactionModel(orderModel);
		orderModel.setEntries(abstractOrderEntryList);

		return Order;
	}

	private void createPaymentTransactionModel(final OrderModel orderModel)
	{
		final PaymentTransactionModel paymentTransactionModel = new PaymentTransactionModel();

		paymentTransactionModel.setCode("");//TODO : Please enter code
		final List<PaymentTransactionEntryModel> paymentTransactionEntrylist = createPaymentTransactionEntryModel(paymentTransactionModel);
		paymentTransactionModel.setEntries(paymentTransactionEntrylist);

		final List<PaymentTransactionModel> paymentTransactions = new ArrayList<PaymentTransactionModel>();
		paymentTransactions.add(paymentTransactionModel);
		orderModel.setPaymentTransactions(paymentTransactions);
	}

	private List<PaymentTransactionEntryModel> createPaymentTransactionEntryModel(
			final PaymentTransactionModel paymentTransactionModel)
	{
		final List<PaymentTransactionEntryModel> paymentTransactionEntrylist = new ArrayList<PaymentTransactionEntryModel>();
		final PaymentTransactionEntryModel paymentTransactionEntryModel = new PaymentTransactionEntryModel();


		paymentTransactionEntryModel.setPaymentTransaction(paymentTransactionModel);
		final CurrencyModel currency = new CurrencyModel();

		currency.setIsocode("");//TODO : Please enter valid iso code
		currency.setSymbol("");//TODO : Please enter valid symbol
		paymentTransactionEntryModel.setCurrency(currency);
		paymentTransactionEntryModel.setTransactionStatus("payment.transactionStatus");


		paymentTransactionEntryModel.setCode("");//TODO : Please enter code
		paymentTransactionEntryModel.setAmount(new BigDecimal(12345));
		paymentTransactionEntryModel.setTransactionStatusDetails("payment.transactionStatusDetails");

		paymentTransactionEntryModel.setType(PaymentTransactionType.AUTHORIZATION);

		final PaymentTypeModel paymentTypeModel = new PaymentTypeModel();

		paymentTypeModel.setMode("");//TODO : Please enter mode
		paymentTransactionEntryModel.setPaymentMode(paymentTypeModel);
		paymentTransactionEntrylist.add(paymentTransactionEntryModel);
		return paymentTransactionEntrylist;
	}



}
