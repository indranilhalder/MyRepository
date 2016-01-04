/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplSellerPriorityLevelModel;
import com.tisl.mpl.core.model.MplSellerPriorityModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplSellerPriorityDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerPriorityService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author TCS
 *
 */
public class MplSellerPriorityServiceImpl implements MplSellerPriorityService
{


	@Resource(name = "mplSellerPriorityDao")
	private MplSellerPriorityDao mplSellerPriorityDao;
	@Autowired
	private ModelService modelService;
	private static final Logger log = Logger.getLogger(MplSellerPriorityServiceImpl.class.getName());

	/**
	 * updating intermediate priority tables and setting priority levels against ussid to be read from buybox
	 *
	 * @return isUpadated
	 */
	@Override
	public boolean updateSellerPriorityDetails()
	{
		boolean isUpadated = true;
		try
		{

			final List<MplSellerPriorityModel> sellerPriorityModels = mplSellerPriorityDao.getAllSellerPriorities();
			final Map<String, MplSellerPriorityLevelModel> priorityMap = new HashMap<String, MplSellerPriorityLevelModel>();
			for (final MplSellerPriorityModel sellerPriority : sellerPriorityModels)
			{
				int priorityLevel = 0;
				boolean isValid = false;
				List<String> ussidList = null;
				if ((null != sellerPriority.getIsActive() && sellerPriority.getIsActive().booleanValue())
						&& (new Date().after(sellerPriority.getPriorityStartDate()) && new Date().before(sellerPriority
								.getPriorityEndDate())))
				{
					isValid = true;
				}
				if (null != sellerPriority.getCategoryId() && null != sellerPriority.getListingId())
				{
					final int count = 1;
					priorityLevel = findCategoryLevel(sellerPriority.getCategoryId(), count);
					ussidList = getUssidsFromSellers(sellerPriority.getCategoryId(), sellerPriority.getSellerId());
					priorityMap.putAll(getPriorityLevelData(ussidList, priorityLevel, sellerPriority.getIsActive().booleanValue(),
							priorityMap));
					final int productPriorityLevel = Integer.parseInt(MarketplacecommerceservicesConstants.PRODUCT_PRIORITY);
					ussidList = new ArrayList<String>(Arrays.asList(getUssidFromSkuId(sellerPriority.getListingId(),
							sellerPriority.getSellerId())));

					priorityMap.putAll(getPriorityLevelData(ussidList, productPriorityLevel, isValid, priorityMap));
					log.debug(new StringBuilder("###########ussid present in both category and product level").append(ussidList)
							.append("prioritylevel").append(priorityLevel).toString());
				}
				else
				{
					//if only category level priority exist
					if (null != sellerPriority.getCategoryId())
					{

						final int count = 1;
						priorityLevel = findCategoryLevel(sellerPriority.getCategoryId(), count);
						ussidList = getUssidsFromSellers(sellerPriority.getCategoryId(), sellerPriority.getSellerId());
						log.debug(new StringBuilder("###########ussid for category level").append(ussidList).append("prioritylevel")
								.append(priorityLevel).toString());
					}
					//if only listing id level priority exist
					else if (null != sellerPriority.getListingId())
					{
						priorityLevel = Integer.parseInt(MarketplacecommerceservicesConstants.PRODUCT_PRIORITY);
						if (getUssidFromSkuId(sellerPriority.getListingId(), sellerPriority.getSellerId()) != null)
						{
							ussidList = new ArrayList<String>(Arrays.asList(getUssidFromSkuId(sellerPriority.getListingId(),
									sellerPriority.getSellerId())));
							log.debug(new StringBuilder("***************ussid for product level").append(ussidList)
									.append("prioritylevel").append(priorityLevel).toString());
						}
					}
					if (ussidList != null)
					{
						priorityMap.putAll(getPriorityLevelData(ussidList, priorityLevel, isValid, priorityMap));
					}
				}

			}
			modelService.saveAll(new ArrayList(priorityMap.values()));
		}
		catch (final Exception e)
		{
			isUpadated = false;
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);

		}
		return isUpadated;
	}

	/**
	 * get ussids corresponding to listing id/sku id
	 *
	 * @param listingId
	 * @param sellerMasterModel
	 * @return ussid
	 */
	private String getUssidFromSkuId(final ProductModel listingId, final SellerMasterModel sellerMasterModel)
	{
		String ussid = null;
		try
		{
			for (final SellerInformationModel seller : listingId.getSellerInformationRelator())
			{
				if (seller.getSellerID().equals(sellerMasterModel.getId()))
				{
					ussid = seller.getSellerArticleSKU();
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return ussid;
	}

	/**
	 * get ussids corresponding to the seller ids and category
	 *
	 * @param sellerMasterModel
	 * @param category
	 * @return skuList
	 */



	private List<String> getUssidsFromSellers(final CategoryModel category, final SellerMasterModel sellerMasterModel)
	{

		final List<String> ussidList = new ArrayList<String>();
		try
		{
			if (!(category.getCategories().isEmpty()))
			{
				for (final CategoryModel cat : category.getCategories())
				{
					final List<String> ussids = findUssidsByRecursion(cat, sellerMasterModel);
					ussidList.addAll(ussids);
				}
			}
			else
			{
				final List<String> ussids = findUssidsByRecursion(category, sellerMasterModel);
				ussidList.addAll(ussids);
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return ussidList;
	}

	/**
	 * finding a category level corresponding to a category id
	 *
	 * @param categoryId
	 * @param count
	 * @return count
	 */
	private int findCategoryLevel(final CategoryModel categoryId, int count)
	{

		int finalCount = 0;
		try
		{
			if (categoryId.getSupercategories().isEmpty())
			{
				finalCount = count;
			}
			else
			{
				for (final CategoryModel superCategory : categoryId.getSupercategories())
				{
					count++;
					if (count == finalCount)
					{
						break;
					}
					else
					{
						return findCategoryLevel(superCategory, count);
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return finalCount;
	}


	/**
	 * find usssid corresponding to sellerid and categoryid
	 *
	 * @param category
	 * @param sellerMasterModel
	 * @return product
	 */
	private List<String> findUssidsByRecursion(final CategoryModel category, final SellerMasterModel sellerMasterModel)
	{
		final List<String> ussidList = new ArrayList<String>();
		try
		{
			if (!(category.getProducts().isEmpty()))
			{
				for (final ProductModel p : category.getProducts())
				{
					for (final SellerInformationModel seller : p.getSellerInformationRelator())
					{
						if (seller.getSellerID().equals(sellerMasterModel.getId()))
						{
							ussidList.add(seller.getSellerArticleSKU());
							break;
						}
					}

					//				if (p.getSellerInformationRelator().contains(sellerInformationModel))
					//				{
					//					product.add(p);
					//				}

				}
			}
			for (final CategoryModel subCategories : category.getCategories())
			{
				if (!(findUssidsByRecursion(subCategories, sellerMasterModel).isEmpty()))
				{
					return findUssidsByRecursion(subCategories, sellerMasterModel);
				}

			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return ussidList;
	}

	/**
	 * insert priority level entries nad modify the priority levels of the existing ones
	 *
	 * @param ussidList
	 * @param priorityLevel
	 * @param isActive
	 * @param priorityLevelMap
	 */
	private Map<String, MplSellerPriorityLevelModel> getPriorityLevelData(final List<String> ussidList, final int priorityLevel,
			final boolean isActive, final Map<String, MplSellerPriorityLevelModel> priorityLevelMap)
	{
		MplSellerPriorityLevelModel sellerPriorityLevel = null;
		boolean isNewEntry = false;
		try
		{
			for (final String ussid : ussidList)
			{
				//load alraedy existing ussid and update
				if (!(mplSellerPriorityDao.loadExistingUssid(ussid).isEmpty()))
				{
					sellerPriorityLevel = mplSellerPriorityDao.loadExistingUssid(ussid).get(0);
					if (!isActive)
					{
						updateInvalidPriorityLevel(priorityLevel, sellerPriorityLevel);
						sellerPriorityLevel.setIsValidPriority(Boolean.valueOf(isActive));
					}
					else
					{
						setPriorityLevel(priorityLevel, sellerPriorityLevel);
					}
				}
				//creating a new entry
				else if (priorityLevelMap.get(ussid) != null)
				{
					isNewEntry = true;
					sellerPriorityLevel = priorityLevelMap.get(ussid);
				}
				else
				{
					isNewEntry = true;
					if (isActive)
					{
						sellerPriorityLevel = modelService.create(MplSellerPriorityLevelModel.class);
						setPriorityLevel(priorityLevel, sellerPriorityLevel);
					}
				}
				if (!(isNewEntry && !isActive))
				{
					sellerPriorityLevel.setUssid(ussid);
					sellerPriorityLevel.setIsValidPriority(Boolean.valueOf(isActive));
					priorityLevelMap.put(ussid, sellerPriorityLevel);
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return priorityLevelMap;
	}

	/**
	 * set priority levels against a particular ussid
	 *
	 * @param priorityLevel
	 * @param sellerPriorityLevel
	 */
	private void setPriorityLevel(final int priorityLevel, final MplSellerPriorityLevelModel sellerPriorityLevel)
	{
		try
		{
			switch (priorityLevel)
			{
				case 1:
					sellerPriorityLevel.setL1Priority(Integer.valueOf(priorityLevel));
					break;
				case 2:
					sellerPriorityLevel.setL2Priority(Integer.valueOf(priorityLevel));
					break;
				case 3:
					sellerPriorityLevel.setL3Priority(Integer.valueOf(priorityLevel));
					break;
				case 4:
					sellerPriorityLevel.setL4Priority(Integer.valueOf(priorityLevel));
					break;
				case 5:
					sellerPriorityLevel.setProductPriority(Integer.valueOf(priorityLevel));
					break;
				case 6:
					sellerPriorityLevel.setProductPriority(Integer.valueOf(priorityLevel));
					break;
				case 7:
					sellerPriorityLevel.setProductPriority(Integer.valueOf(priorityLevel));
					break;
				default:
					sellerPriorityLevel.setProductPriority(Integer.valueOf(0));
					break;
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * update priorities for a ussid,when the priority for the ussid is no longer valid
	 *
	 * @param priorityLevel
	 * @param sellerPriorityLevel
	 */
	private void updateInvalidPriorityLevel(final int priorityLevel, final MplSellerPriorityLevelModel sellerPriorityLevel)
	{
		try
		{
			switch (priorityLevel)
			{
				case 1:
					sellerPriorityLevel.setL1Priority(Integer.valueOf(0));
					break;
				case 2:
					sellerPriorityLevel.setL2Priority(Integer.valueOf(0));
					break;
				case 3:
					sellerPriorityLevel.setL3Priority(Integer.valueOf(0));
					break;
				case 4:
					sellerPriorityLevel.setL4Priority(Integer.valueOf(0));
					break;
				case 5:
					sellerPriorityLevel.setProductPriority(Integer.valueOf(0));
					break;
				case 6:
					sellerPriorityLevel.setProductPriority(Integer.valueOf(0));
					break;
				case 7:
					sellerPriorityLevel.setProductPriority(Integer.valueOf(0));
					break;
				default:
					sellerPriorityLevel.setProductPriority(Integer.valueOf(0));
					break;
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


}
