/**
 *
 */
package com.tisl.mpl.order.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.order.strategies.calculation.FindPaymentCostStrategy;
import de.hybris.platform.promotions.util.Tuple2;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.marketplacecommerceservices.daos.BuyBoxDao;


/**
 * @author TCS
 *
 */
public class MplDefaultCalculationService extends DefaultCalculationService
{

	private static final Logger LOG = Logger.getLogger(MplDefaultCalculationService.class);

	@Autowired
	private FindPaymentCostStrategy findPaymentCostStrategy;
	@Autowired
	private BuyBoxDao buyBoxDao;

	@Override
	protected void resetAdditionalCosts(final AbstractOrderModel order, final Collection<TaxValue> relativeTaxValues)
	{

		//System.out.println("order total including delivery charges" + order.getTotalPrice());
		LOG.debug("order total including delivery charges" + order.getTotalPrice());

		//order.setDeliveryCost(Double.valueOf(deliveryCostValue));
		// -----------------------------
		// set payment cost - convert if net or currency is different
		final PriceValue payCost = findPaymentCostStrategy.getPaymentCost(order);
		double paymentCostValue = 0.0;
		if (payCost != null)
		{
			paymentCostValue = convertPriceIfNecessary(payCost, order.getNet().booleanValue(), order.getCurrency(),
					relativeTaxValues).getValue();
		}
		order.setPaymentCost(Double.valueOf(paymentCostValue));
	}

	/**
	 * @return the findPaymentCostStrategy
	 */
	public FindPaymentCostStrategy getFindPaymentCostStrategy()
	{
		return findPaymentCostStrategy;
	}

	/**
	 * @param findPaymentCostStrategy
	 *           the findPaymentCostStrategy to set
	 */
	@Override
	public void setFindPaymentCostStrategy(final FindPaymentCostStrategy findPaymentCostStrategy)
	{
		this.findPaymentCostStrategy = findPaymentCostStrategy;
	}

	/**
	 * TPR-774
	 *
	 * @desc Override resetAllValues to pass mrp from addtocart to cart
	 * @throws CalculationException
	 */
	@Override
	protected void resetAllValues(final AbstractOrderEntryModel entry) throws CalculationException
	{
		// taxes
		//final Collection<TaxValue> entryTaxes = findTaxValues(entry); // Tax is not considered at Commerce end
		final Collection<TaxValue> entryTaxes = new ArrayList<>();
		entry.setTaxValues(entryTaxes);

		final AbstractOrderModel order = entry.getOrder();
		final CurrencyModel currency = order.getCurrency();

		final Tuple2<PriceValue, PriceValue> pv = findBasePriceForAddtoCart(entry);


		final PriceValue basePrice = convertPriceIfNecessary(pv.getFirst(), false, currency, entryTaxes);
		final PriceValue mrpPrice = convertPriceIfNecessary(pv.getSecond(), false, currency, entryTaxes);
		entry.setBasePrice(Double.valueOf(basePrice.getValue()));

		final List<DiscountValue> entryDiscounts = findDiscountValues(entry);
		entry.setDiscountValues(entryDiscounts);

		entry.setMrp(Double.valueOf(mrpPrice.getValue()));
	}


	/**
	 * TPR-774 getBasePriceForAddtoCart to pass mrp from addtocart to cart
	 *
	 * @param entry
	 * @return
	 * @throws CalculationException
	 */
	private Tuple2<PriceValue, PriceValue> findBasePriceForAddtoCart(final AbstractOrderEntryModel entry)
			throws CalculationException
	{
		final AbstractOrderEntry entryItem = getModelService().getSource(entry);
		try

		{
			final Tuple2<PriceValue, PriceValue> priceDataTuple = getBasePriceForAddtoCart(entryItem);
			return priceDataTuple;

		}
		catch (final JaloPriceFactoryException e)
		{
			throw new CalculationException(e);
		}
	}

	/**
	 * TPR-774 getBasePriceForAddtoCart to pass mrp from addtocart to cart
	 *
	 * @param entry
	 * @return
	 * @throws JaloPriceFactoryException
	 */
	private Tuple2<PriceValue, PriceValue> getBasePriceForAddtoCart(final AbstractOrderEntry entry)
			throws JaloPriceFactoryException
	{

		String ussid = "";
		final Object ussidObj = entry.getProperty("selectedUSSID");
		//final Map<String, PriceValue> priceData = new HashMap<String, PriceValue>();
		Double finalPrice = Double.valueOf(0.0);
		Double finalMrpPrice = Double.valueOf(0.0);
		if (null != ussidObj)
		{
			ussid = ussidObj.toString();

			final List<BuyBoxModel> buyBoxModelList = buyBoxDao.getBuyBoxPriceForUssId(ussid);



			if (CollectionUtils.isNotEmpty(buyBoxModelList))
			{
				//final Double specialPrice = buyBoxModelList.get(0).getSpecialPrice();
				final Double mopPrice = buyBoxModelList.get(0).getPrice();
				final Double mrpPrice = buyBoxModelList.get(0).getMrp();
				if (mopPrice != null && mopPrice.doubleValue() > 0.0)
				{
					finalPrice = mopPrice;
				}
				else if (mrpPrice != null && mrpPrice.doubleValue() > 0.0)
				{
					finalPrice = mrpPrice;
				}
				if (mrpPrice != null && mrpPrice.doubleValue() > 0.0)
				{
					finalMrpPrice = mrpPrice;
				}
				//priceData.put("mop", new PriceValue("INR", finalPrice.doubleValue(), false));
				//priceData.put("mrp", new PriceValue("INR", finalMrpPrice.doubleValue(), false));
			}

		}
		return new Tuple2(new PriceValue("INR", finalPrice.doubleValue(), false),
				new PriceValue("INR", finalMrpPrice.doubleValue(), false));
	}
}