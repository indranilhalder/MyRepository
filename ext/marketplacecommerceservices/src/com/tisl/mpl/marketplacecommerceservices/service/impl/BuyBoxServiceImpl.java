/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.BuyBoxDao;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;



public class BuyBoxServiceImpl implements BuyBoxService
{


	private BuyBoxDao buyBoxDao;

	@Autowired
	private UserService userService;
	private ConfigurationService configurationService;


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

	/*
	 * This service method will return buybox prices for product code
	 *
	 * @param - productCode
	 *
	 * @return- buyBoxList
	 */
	@Override
	//	public List<BuyBoxModel> buyboxPrice(final String productCode) throws EtailNonBusinessExceptions
	public List<BuyBoxModel> buyboxPrice(final String productCode) throws EtailNonBusinessExceptions, EtailBusinessExceptions
	{

		final List<BuyBoxModel> buyBoxList = buyBoxDao.buyBoxPrice(productCode);

		final String sellerId = isUserInRole(configurationService.getConfiguration().getString(
				"cscockpit.user.group.storemanageragentgroup"));
		if (sellerId != null && StringUtils.isNotEmpty(sellerId))
		{
			final List<BuyBoxModel> buyBoxListForAgent = new ArrayList<BuyBoxModel>();
			final List<BuyBoxModel> buyBoxListForNonAgent = new ArrayList<BuyBoxModel>();
			final Iterator<BuyBoxModel> itAgent = buyBoxList.iterator();
			while (itAgent.hasNext())
			{
				final BuyBoxModel buyBoxModel = itAgent.next();
				if (buyBoxModel.getSellerId().equalsIgnoreCase(sellerId))
				{
					buyBoxListForAgent.add(buyBoxModel);
				}
				else
				{
					buyBoxListForNonAgent.add(buyBoxModel);
				}
			}
			if (buyBoxListForAgent.size() > 0)
			{
				if (buyBoxListForNonAgent.size() > 0)
				{
					final Iterator<BuyBoxModel> itAgentHelper = buyBoxListForNonAgent.iterator();
					while (itAgentHelper.hasNext())
					{
						buyBoxListForAgent.add(itAgentHelper.next());
					}
				}

				return buyBoxListForAgent;
			}
			else
			{
				return buyBoxList;
			}
		}

		else
		{
			return buyBoxList;
		}
	}

	private String isUserInRole(final String agentGroup)
	{
		final String userId = (String) JaloSession.getCurrentSession().getAttribute("sellerId");
		if (userId != null && StringUtils.isNotEmpty(userId))
		{
			final UserModel user = userService.getUserForUID(userId);
			final Set<PrincipalGroupModel> userGroups = user.getAllGroups();

			for (final PrincipalGroupModel ug : userGroups)
			{
				if (ug.getUid().equalsIgnoreCase(agentGroup))
				{
					return userId;
				}
			}
		}
		return StringUtils.EMPTY;
	}

	/*
	 * This service method will return buybox prices for product code
	 *
	 * @param - productCode
	 *
	 * @return- List<BuyBoxModel>
	 */

	@Override
	public List<BuyBoxModel> getBuyboxPricesForSearch(final String productCode) throws EtailNonBusinessExceptions
	{

		return buyBoxDao.getBuyboxPricesForSearch(productCode);
	}

	/*
	 * This service method will return buybox inventory for product code
	 *
	 * @param - productCode
	 */
	@Override
	public Integer getBuyboxInventoryForSearch(final String productCode, final String productType)
	{

		return buyBoxDao.getBuyboxAvailableInventoryForSearch(productCode, productType);
	}

	/*
	 * This service method will inavalidating pks of the buyboxbox sellers
	 *
	 * @param - productCode buyBoxDao.invalidatePkofBuybox(currenttime)
	 */
	@Override
	public List<BuyBoxModel> invalidatePkofBuybox(final Date currenttime) throws EtailNonBusinessExceptions
	{
		return buyBoxDao.invalidatePkofBuybox(currenttime);
	}



	@Required
	public void setBuyBoxDao(final BuyBoxDao buyBoxDao)
	{
		this.buyBoxDao = buyBoxDao;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService#buyBoxPriceNoStock(java.lang.String)
	 */
	@Override
	public List<BuyBoxModel> buyBoxPriceNoStock(final String productCode) throws EtailNonBusinessExceptions
	{
		final List<BuyBoxModel> buyBoxList = buyBoxDao.buyBoxPriceNoStock(productCode);

		final String sellerId = isUserInRole(configurationService.getConfiguration().getString(
				"cscockpit.user.group.storemanageragentgroup"));
		if (sellerId != null && StringUtils.isNotEmpty(sellerId))
		{
			final List<BuyBoxModel> buyBoxListForAgent = new ArrayList<BuyBoxModel>();
			final Iterator<BuyBoxModel> itAgent = buyBoxList.iterator();
			while (itAgent.hasNext())
			{
				final BuyBoxModel buyBoxModel = itAgent.next();
				if (buyBoxModel.getSellerId().equalsIgnoreCase(sellerId))
				{
					buyBoxListForAgent.add(buyBoxModel);
				}
			}
			if (buyBoxListForAgent.size() > 0)
			{
				return buyBoxListForAgent;
			}
			else
			{
				return buyBoxList;
			}
		}

		else
		{
			return buyBoxList;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService#getRichAttributeData(java.lang.String)
	 */
	@Override
	public RichAttributeModel getRichAttributeData(final String ussid) throws EtailNonBusinessExceptions, EtailBusinessExceptions
	{
		return buyBoxDao.getRichAttributeData(ussid);
	}

	@Override
	public Set<Map<BuyBoxModel, RichAttributeModel>> getsellersDetails(final String productCode)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions
	{
		final Set<Map<BuyBoxModel, RichAttributeModel>> resultMap = buyBoxDao.getsellersDetails(productCode);
		return resultMap;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService#getpriceForUssid(java.lang.String)
	 */
	@Override
	public BuyBoxModel getpriceForUssid(final String ussid)
	{
		final BuyBoxModel buyBox = buyBoxDao.priceForUssid(ussid);



		return buyBox;
	}

	/**
	 * This service method will return buybox data for product code and seller id
	 *
	 * @param productCode
	 * @param sellerId
	 * @return
	 * @throws EtailNonBusinessExceptions
	 * @throws EtailBusinessExceptions
	 */
	@Override
	public BuyBoxModel buyboxForSizeGuide(final String productCode, final String sellerId) throws EtailNonBusinessExceptions,
			EtailBusinessExceptions
	{
		//Get Buybox data in respect of product code and seller id
		return buyBoxDao.buyBoxForSizeGuide(productCode, sellerId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService#buyBoxStockForSeller(java.lang.String)
	 */
	@Override
	public List<BuyBoxModel> buyBoxStockForSeller(final String sellerID)
	{
		// YTODO Auto-generated method stub
		return buyBoxDao.buyBoxStockForSeller(sellerID);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService#getClassAttrAssignmentsForCode(java.lang.String)
	 */
	@Override
	public List<ClassAttributeAssignmentModel> getClassAttrAssignmentsForCode(final String code)
	{
		// YTODO Auto-generated method stub
		return buyBoxDao.getClassAttrAssignmentsForCode(code);
	}

	//INC144315542_INC144314878_INC_11113
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService#getBuyboxPricesForSizeVariant(java.lang.String)
	 */
	@Override
	public List<BuyBoxModel> getBuyboxPricesForSizeVariant(final String productCode) throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		return buyBoxDao.getBuyboxPricesForSizeVariant(productCode);// INC144314878_INC_11113

	}

}
