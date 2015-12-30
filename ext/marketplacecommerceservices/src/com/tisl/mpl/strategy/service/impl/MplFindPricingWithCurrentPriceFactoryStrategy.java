/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.strategy.service.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.FindDiscountValuesStrategy;
import de.hybris.platform.order.strategies.calculation.FindPriceStrategy;
import de.hybris.platform.order.strategies.calculation.FindTaxValuesStrategy;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.PriceValue;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.marketplacecommerceservices.daos.BuyBoxDao;


/**
 * Default implementation of price, taxes and discounts resolver strategies ({@link FindPriceStrategy},
 * {@link FindDiscountValuesStrategy}, {@link FindTaxValuesStrategy}) that resolves values for calculation from current
 * session's price factory. If no session price factory is set it uses {@link OrderManager#getPriceFactory()} which will
 * retrieve the default one according to system settings.
 */
public class MplFindPricingWithCurrentPriceFactoryStrategy extends AbstractBusinessService implements FindPriceStrategy,
		FindDiscountValuesStrategy, FindTaxValuesStrategy
{

	protected static final Logger LOG = Logger.getLogger(MplFindPricingWithCurrentPriceFactoryStrategy.class);
	@Autowired
	BuyBoxDao buyBoxDao;

	@Override
	public Collection findTaxValues(final AbstractOrderEntryModel entry) throws CalculationException
	{
		final AbstractOrderEntry entryItem = getModelService().getSource(entry);
		try
		{
			return getCurrentPriceFactory().getTaxValues(entryItem);
		}
		catch (final JaloPriceFactoryException e)
		{
			throw new CalculationException(e);
		}
	}

	@Override
	public PriceValue findBasePrice(final AbstractOrderEntryModel entry) throws CalculationException
	{
		//final AbstractOrderEntry entryItem = getModelService().getSource(entry);
		try
		{
			//return getCurrentPriceFactory().getBasePrice(entryItem);
			LOG.info("Inside MplFindPricingWithCurrentPriceFactoryStrategy " + entry.getSelectedUSSID());
			final String ussid = entry.getSelectedUSSID();
			final List<BuyBoxModel> buyBoxModelList = buyBoxDao.getBuyBoxPriceForUssId(ussid);

			Double finalPrice = Double.valueOf(0.0);
			if (buyBoxModelList != null)
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

			}
			return new PriceValue("INR", finalPrice.doubleValue(), false);
		}
		catch (final Exception e)
		{
			throw new CalculationException(e);
		}
	}

	@Override
	public List<DiscountValue> findDiscountValues(final AbstractOrderEntryModel entry) throws CalculationException
	{
		final AbstractOrderEntry entryItem = getModelService().getSource(entry);
		try
		{
			return getCurrentPriceFactory().getDiscountValues(entryItem);
		}
		catch (final JaloPriceFactoryException e)
		{
			throw new CalculationException(e);
		}
	}

	@Override
	public List<DiscountValue> findDiscountValues(final AbstractOrderModel order) throws CalculationException
	{
		final AbstractOrder orderItem = getModelService().getSource(order);
		try
		{
			return getCurrentPriceFactory().getDiscountValues(orderItem);
		}
		catch (final JaloPriceFactoryException e)
		{
			throw new CalculationException(e);
		}
	}

	public PriceFactory getCurrentPriceFactory()
	{
		// Actually OrderManager.getPriceFactory() implements default / session specific price
		// factory fetching. So no need to do it twice.
		return OrderManager.getInstance().getPriceFactory();
	}

	/**
	 * @return the buyBoxDao
	 */
	public BuyBoxDao getBuyBoxDao()
	{
		return buyBoxDao;
	}

	/**
	 * @param buyBoxDao
	 *           the buyBoxDao to set
	 */
	public void setBuyBoxDao(final BuyBoxDao buyBoxDao)
	{
		this.buyBoxDao = buyBoxDao;
	}

	/**
	 * @return the log
	 */
	public static Logger getLog()
	{
		return LOG;
	}
}
