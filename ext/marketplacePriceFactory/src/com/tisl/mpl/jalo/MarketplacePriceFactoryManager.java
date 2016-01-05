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
 * Author: TCS
 */
package com.tisl.mpl.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.PriceValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacePriceFactoryConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.BuyBoxDao;
import com.tisl.mpl.util.ExceptionUtil;



/**
 * This is the extension manager of the MarketplacePriceFactory extension.
 */
public class MarketplacePriceFactoryManager extends GeneratedMarketplacePriceFactoryManager
{
	/** Edit the local|project.properties to change logging behavior (properties 'log4j.*'). */
	private static final Logger LOG = Logger.getLogger(MarketplacePriceFactoryManager.class.getName());
	private static final String SELLER_ARTICLE_SKU = "USSID";
	@Autowired
	private ModelService modelService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private BuyBoxDao buyBoxDao;

	/*
	 * Some important tips for development:
	 *
	 * Do NEVER use the default constructor of manager's or items. => If you want to do something whenever the manger is
	 * created use the init() or destroy() methods described below
	 *
	 * Do NEVER use STATIC fields in your manager or items! => If you want to cache anything in a "static" way, use an
	 * instance variable in your manager, the manager is created only once in the lifetime of a "deployment" or tenant.
	 */


	/**
	 * Get the valid instance of this manager.
	 *
	 * @return the current instance of this manager
	 */
	public static MarketplacePriceFactoryManager getInstance()
	{
		return (MarketplacePriceFactoryManager) Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
				.getExtension(MarketplacePriceFactoryConstants.EXTENSIONNAME);
	}


	/**
	 * Never call the constructor of any manager directly, call getInstance() You can place your business logic here -
	 * like registering a jalo session listener. Each manager is created once for each tenant.
	 */
	public MarketplacePriceFactoryManager() // NOPMD
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("constructor of MarketplacePriceFactoryManager called.");
		}
	}

	/**
	 * Use this method to do some basic work only ONCE in the lifetime of a tenant resp. "deployment". This method is
	 * called after manager creation (for example within startup of a tenant). Note that if you have more than one tenant
	 * you have a manager instance for each tenant.
	 */
	@Override
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("init() of MarketplacePriceFactoryManager called. " + getTenant().getTenantID());
		}
	}

	/**
	 * Use this method as a callback when the manager instance is being destroyed (this happens before system
	 * initialization, at redeployment or if you shutdown your VM). Note that if you have more than one tenant you have a
	 * manager instance for each tenant.
	 */
	@Override
	public void destroy()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("destroy() of MarketplacePriceFactoryManager called, current tenant: " + getTenant().getTenantID());
		}
	}

	/**
	 * @Description : Return Price Row For a USSID
	 * @param :
	 *           List<PriceRow> priceRows
	 * @return : List<PriceRow>
	 */
	//Filtering Price Rows for Seller Article SKU from Product Page Controller.Price is displayed as per the USSID.
	@Override
	protected List<PriceRow> filterPriceRows(final List<PriceRow> priceRows)
	{
		if (priceRows.isEmpty())
		{
			return Collections.EMPTY_LIST;
		}

		Unit lastUnit = null;
		long lastMin = -1L;
		final List<PriceRow> finalList = new ArrayList<PriceRow>();
		final ArrayList ret = new ArrayList(priceRows);
		for (final ListIterator it = ret.listIterator(); it.hasNext();)
		{
			final PriceRow row = (PriceRow) it.next();

			final long min = row.getMinQuantity();
			final Unit unit = row.getUnit();
			try
			{
				//USSID For a Price Row model is compared to USSID retrieved from session.
				//If same then return the Price Row for the particular USSID.
				final PriceRowModel priceRowModel = (PriceRowModel) modelService.get(row.getPK());
				final String ussid = priceRowModel.getSellerArticleSKU();
				final String USSID = sessionService.getAttribute(SELLER_ARTICLE_SKU);
				if (USSID != null && USSID.equals(ussid))
				{
					finalList.add(row);
					//Return final list of Price Row as per USSID.
					return finalList;

				}

				if ((lastUnit != null) && (lastUnit.equals(unit)) && (lastMin == min))
				{
					it.remove();
				}
				else
				{
					lastUnit = unit;
					lastMin = min;
				}
			}
			catch (final EtailBusinessExceptions e)
			{
				ExceptionUtil.etailBusinessExceptionHandler(e, null);
			}
			catch (final EtailNonBusinessExceptions e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
			}
			catch (final Exception e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			}
		}
		return ret;
	}


	@Override
	public PriceValue getBasePrice(final AbstractOrderEntry entry) throws JaloPriceFactoryException
	{
		String ussid = "";
		final Object ussidObj = entry.getProperty("selectedUSSID");
		if (null != ussidObj)
		{
			ussid = ussidObj.toString();

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

		return super.getBasePrice(entry);
	}

}
