/**
 *
 */
package com.tisl.mpl.order.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.order.strategies.calculation.FindPaymentCostStrategy;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author TCS
 *
 */
public class MplDefaultCalculationService extends DefaultCalculationService
{

	private static final Logger LOG = Logger.getLogger(MplDefaultCalculationService.class);

	@Autowired
	private FindPaymentCostStrategy findPaymentCostStrategy;

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
}
