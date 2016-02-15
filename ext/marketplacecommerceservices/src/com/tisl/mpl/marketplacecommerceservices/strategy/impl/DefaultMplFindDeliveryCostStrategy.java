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
package com.tisl.mpl.marketplacecommerceservices.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.strategies.calculation.FindDeliveryCostStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.PriceValue;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.enums.DeliveryFulfillModesEnum;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.strategy.MplFindDeliveryCostStrategy;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * Default implementation of {@link FindDeliveryCostStrategy}.
 */
public class DefaultMplFindDeliveryCostStrategy extends AbstractBusinessService implements MplFindDeliveryCostStrategy
{

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private ModelService modelService;

	private static final Logger LOG = Logger.getLogger(DefaultMplFindDeliveryCostStrategy.class);

	@Autowired
	private MplDeliveryCostService deliveryCostService;

	@Autowired
	private MplCommerceCartService mplCommerceCartService;




	@Override
	public PriceValue getDeliveryCost(final AbstractOrderModel order)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("order", order);
		try
		{
			final String tshipThresholdValue = configurationService.getConfiguration().getString("tship.item.threshold.value");

			MplZoneDeliveryModeValueModel valueModel = null;
			getModelService().save(order);

			double delCost = 0.0d;
			double finalCost = 0.0d;
			double finaldelCost = 0.0d;

			finalCost = getTotalCostForTSHIP(order);

			for (final AbstractOrderEntryModel entry : order.getEntries())
			{
				try
				{
					valueModel = deliveryCostService.getDeliveryCost(entry.getMplDeliveryMode().getDeliveryMode().getCode(), order
							.getCurrency().getIsocode(), entry.getSelectedUSSID());

					if (isTShip(entry.getSelectedUSSID()))
					{

						if (finalCost > Double.parseDouble(tshipThresholdValue))
						{
							delCost = 0.0d;
							entry.setCurrDelCharge(Double.valueOf(0.0));
							LOG.warn("skipping deliveryCost for Threshold [" + entry.getSelectedUSSID() + "] due to Threshold ");
						}
					}
					else
					{
						if (entry.getGiveAway() != null && !entry.getGiveAway().booleanValue())
						{
							delCost = (valueModel.getValue().doubleValue() * entry.getQuantity().intValue());
							entry.setCurrDelCharge(Double.valueOf(delCost));
						}
						else
						{
							delCost = 0.0d;
							entry.setCurrDelCharge(Double.valueOf(delCost));
							LOG.warn("skipping deliveryCost for freebee [" + entry.getSelectedUSSID() + "] due to freebee ");
						}
					}
					modelService.save(entry);
					modelService.refresh(entry);

					finaldelCost = finaldelCost + entry.getCurrDelCharge().doubleValue();

				}
				catch (final Exception e)
				{
					LOG.warn("Could not find deliveryCost for order [" + order.getCode() + "] due to : " + e.getMessage()
							+ "... skipping!");
				}
			}

			return new PriceValue(order.getCurrency().getIsocode(), finaldelCost, order.getNet().booleanValue());
		}
		catch (final Exception e)
		{
			LOG.warn("Could not find deliveryCost for order [" + order.getCode() + "] due to : " + e.getMessage() + "... skipping!");
			return new PriceValue(order.getCurrency().getIsocode(), 0.0, order.getNet().booleanValue());
		}
	}

	private double getTotalCostForTSHIP(final AbstractOrderModel order)
	{
		double finalCost = 0.0d;
		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			if (null != entry.getMplDeliveryMode())
			{
				if (isTShip(entry.getSelectedUSSID()))
				{
					finalCost = finalCost + entry.getTotalPrice().doubleValue();
				}
			}
		}
		return finalCost;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the modelService
	 */
	@Override
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the deliveryCostService
	 */
	public MplDeliveryCostService getDeliveryCostService()
	{
		return deliveryCostService;
	}

	/**
	 * @param deliveryCostService
	 *           the deliveryCostService to set
	 */
	public void setDeliveryCostService(final MplDeliveryCostService deliveryCostService)
	{
		this.deliveryCostService = deliveryCostService;
	}


	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	@Override
	public String findDeliveryFulfillMode(final String selectedUssid)
	{
		LOG.debug("MplDefaultFindDeliveryFulfillModeStrategy" + selectedUssid);
		try
		{

			final SellerInformationModel seller = mplSellerInformationService.getSellerDetail(selectedUssid);
			return seller.getRichAttribute().iterator().next().getDeliveryFulfillModes().getCode();

		}
		catch (final Exception ex)
		{
			LOG.error(ex);
		}
		return StringUtils.EMPTY;
	}

	@Override
	public boolean isTShip(final String selectedUssid)
	{
		LOG.debug("MplDefaultFindDeliveryFulfillModeStrategy" + selectedUssid);
		return DeliveryFulfillModesEnum.TSHIP.getCode().equalsIgnoreCase(findDeliveryFulfillMode(selectedUssid));
	}


	@Override
	public String getDeliveryModeDesc(final MplZoneDeliveryModeValueModel deliveryEntry, final String selectedUssid)
	{
		LOG.debug("MplDefaultFindDeliveryFulfillModeStrategy :: getDeliveryModeDesc () " + selectedUssid);

		try
		{

			//TISEE-950
			final String startValue = String.valueOf(deliveryEntry.getDeliveryMode().getStart());

			final String endValue = String.valueOf(deliveryEntry.getDeliveryMode().getEnd());

			return mplCommerceCartService.getDeliveryModeDescription(selectedUssid, deliveryEntry.getDeliveryMode().getCode(),
					startValue, endValue);
		}
		catch (final Exception ex)
		{
			LOG.error("MplDefaultFindDeliveryFulfillModeStrategy :: getDeliveryModeDesc () " + ex);
		}
		return null;

	}






}