/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.SellerPriorityEnum;
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
		final boolean isUpadated = true;
		List<MplSellerPriorityModel> sellerPriorityModels = null;
		final List<MplSellerPriorityModel> priorityModelList = new ArrayList<MplSellerPriorityModel>();
		try
		{

			sellerPriorityModels = mplSellerPriorityDao.getAllSellerPriorities();
			final Map<String, MplSellerPriorityLevelModel> priorityMap = new HashMap<String, MplSellerPriorityLevelModel>();
			Map<String, List<Integer>> validSellerPriorityMap = new HashMap<String, List<Integer>>();
			final List<String> priorityMapList = new ArrayList<String>();
			if (CollectionUtils.isEmpty(priorityModelList))
			{
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
						sellerPriority.setPriorityStatus(SellerPriorityEnum.PROCESSED);
						final int count = 1;
						priorityLevel = findCategoryLevel(sellerPriority.getCategoryId(), count);
						ussidList = getUssidsFromSellers(sellerPriority.getCategoryId(), sellerPriority.getSellerId());
						ussidList.removeAll(Collections.singletonList(null));
						if (isValid)
						{
							priorityMapList.addAll(ussidList);
						}
						validSellerPriorityMap = getValidPrioritiesAgainstUssid(validSellerPriorityMap, ussidList, priorityLevel,
								isValid);
						priorityMap.putAll(getPriorityLevelData(ussidList, priorityLevel, sellerPriority.getIsActive().booleanValue(),
								priorityMap, priorityMapList));

						//	final int productPriorityLevel =
						priorityLevel = Integer.parseInt(MarketplacecommerceservicesConstants.PRODUCT_PRIORITY);
						ussidList = new ArrayList<String>(Arrays.asList(getUssidFromSkuId(sellerPriority.getListingId(),
								sellerPriority.getSellerId())));
						if (isValid)
						{
							priorityMapList.addAll(ussidList);
						}
						validSellerPriorityMap = getValidPrioritiesAgainstUssid(validSellerPriorityMap, ussidList, priorityLevel,
								isValid);
						priorityMap.putAll(getPriorityLevelData(ussidList, priorityLevel, isValid, priorityMap, priorityMapList));
						priorityModelList.add(sellerPriority);

					}
					else
					{
						//if only category level priority exist
						if (null != sellerPriority.getCategoryId())
						{
							sellerPriority.setPriorityStatus(SellerPriorityEnum.PROCESSED);
							final int count = 1;
							priorityLevel = findCategoryLevel(sellerPriority.getCategoryId(), count);
							ussidList = getUssidsFromSellers(sellerPriority.getCategoryId(), sellerPriority.getSellerId());
							log.info(new StringBuilder("###########ussid for category level").append(ussidList).append("prioritylevel")
									.append(priorityLevel).toString());
							if (isValid)
							{
								priorityMapList.addAll(ussidList);
							}
							if (null != ussidList && ussidList.contains(null) || ussidList == null || ussidList.isEmpty())
							{
								sellerPriority.setPriorityStatus(SellerPriorityEnum.ERROR);
							}
							if (ussidList != null)
							{
								ussidList.removeAll(Collections.singletonList(null));
							}
							priorityModelList.add(sellerPriority);
							//chceking for current valid priorities
							if (isValid)
							{
								for (final String ussid : ussidList)
								{
									if (validSellerPriorityMap.isEmpty())
									{
										validSellerPriorityMap.put(ussid, Collections.singletonList(Integer.valueOf(priorityLevel)));
									}
									else
									{

										if (validSellerPriorityMap.containsKey(ussid))
										{
											final List<Integer> validPriorities = new ArrayList<Integer>(validSellerPriorityMap.get(ussid));
											if (CollectionUtils.isNotEmpty(validPriorities))
											{
												validPriorities.add(Integer.valueOf(priorityLevel));
												validSellerPriorityMap.put(ussid, validPriorities);
											}
											else
											{
												validSellerPriorityMap.put(ussid, Collections.singletonList(Integer.valueOf(priorityLevel)));
											}

										}
										else
										{
											validSellerPriorityMap.put(ussid, Collections.singletonList(Integer.valueOf(priorityLevel)));
										}
									}
								}
							}
							//chceking for current valid priorities
						}
						//if only listing id level priority exist
						else if (null != sellerPriority.getListingId())
						{
							sellerPriority.setPriorityStatus(SellerPriorityEnum.PROCESSED);
							priorityLevel = Integer.parseInt(MarketplacecommerceservicesConstants.PRODUCT_PRIORITY);
							if (getUssidFromSkuId(sellerPriority.getListingId(), sellerPriority.getSellerId()) != null)
							{
								ussidList = new ArrayList<String>(Arrays.asList(getUssidFromSkuId(sellerPriority.getListingId(),
										sellerPriority.getSellerId())));
								if (isValid)
								{
									priorityMapList.addAll(ussidList);
								}
								log.info(new StringBuilder("***************ussid for product level").append(ussidList)
										.append("prioritylevel").append(priorityLevel).toString());
							}
							if (null != ussidList && ussidList.contains(null) || ussidList == null || ussidList.isEmpty())
							{
								sellerPriority.setPriorityStatus(SellerPriorityEnum.ERROR);
							}
							priorityModelList.add(sellerPriority);
							//chceking for current valid priorities
							if (isValid)
							{
								for (final String ussid : ussidList)
								{
									if (validSellerPriorityMap.isEmpty())
									{
										validSellerPriorityMap.put(ussid, Collections.singletonList(Integer.valueOf(priorityLevel)));
									}
									else
									{

										if (validSellerPriorityMap.containsKey(ussid))
										{
											final List<Integer> validPriorities = new ArrayList<Integer>(validSellerPriorityMap.get(ussid));
											if (CollectionUtils.isNotEmpty(validPriorities))
											{
												validPriorities.add(Integer.valueOf(priorityLevel));
												validSellerPriorityMap.put(ussid, validPriorities);
											}
											else
											{
												validSellerPriorityMap.put(ussid, Collections.singletonList(Integer.valueOf(priorityLevel)));
											}

										}
										else
										{
											validSellerPriorityMap.put(ussid, Collections.singletonList(Integer.valueOf(priorityLevel)));
										}
									}
								}
							}
							//chceking for current valid priorities
						}
						if (ussidList != null)
						{
							priorityMap.putAll(getPriorityLevelData(ussidList, priorityLevel, isValid, priorityMap, priorityMapList));
						}
					}

				}

				//	modelService.saveAll(priorityModelList);
				updateNonExistingPriorities(priorityMap, validSellerPriorityMap);
				modelService.saveAll(new ArrayList(priorityMap.values()));
			}
		}
		catch (final Exception ex)
		{
			//updateNonProcessedPriorities(priorityModelList, sellerPriorityModels);
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);

		}
		return isUpadated;
	}

	/**
	 * @param validSellerPriorityMap
	 * @param ussidList
	 * @param priorityLevel
	 * @param isValid
	 * @return
	 */
	private Map<String, List<Integer>> getValidPrioritiesAgainstUssid(final Map<String, List<Integer>> validSellerPriorityMap,
			final List<String> ussidList, final int priorityLevel, final boolean isValid)
	{
		if (isValid)
		{
			for (final String ussid : ussidList)
			{
				if (validSellerPriorityMap.isEmpty())
				{
					validSellerPriorityMap.put(ussid, Collections.singletonList(Integer.valueOf(priorityLevel)));
				}
				else
				{

					if (validSellerPriorityMap.containsKey(ussid))
					{
						final List<Integer> validPriorities = new ArrayList<Integer>(validSellerPriorityMap.get(ussid));
						if (CollectionUtils.isNotEmpty(validPriorities))
						{
							validPriorities.add(Integer.valueOf(priorityLevel));
							validSellerPriorityMap.put(ussid, validPriorities);
						}
						else
						{
							validSellerPriorityMap.put(ussid, Collections.singletonList(Integer.valueOf(priorityLevel)));
						}

					}
					else
					{
						validSellerPriorityMap.put(ussid, Collections.singletonList(Integer.valueOf(priorityLevel)));
					}
				}
			}
		}
		return validSellerPriorityMap;
	}

	/**
	 * @param priorityMap
	 * @param validSellerPriorityMap
	 */
	private void updateNonExistingPriorities(final Map<String, MplSellerPriorityLevelModel> priorityMap,
			final Map<String, List<Integer>> validSellerPriorityMap)
	{
		// YTODO Auto-generated method stub
		//for (final Map.Entry<String, MplSellerPriorityLevelModel> priority : priorityMap.entrySet())
		final List<MplSellerPriorityLevelModel> sellerPriorityList = new ArrayList<MplSellerPriorityLevelModel>();
		for (final String ussid : priorityMap.keySet())
		{
			if (!mplSellerPriorityDao.loadExistingUssid(ussid).isEmpty() && !(validSellerPriorityMap.containsKey(ussid)))
			{
				final MplSellerPriorityLevelModel sellerPriority = priorityMap.get(ussid);
				sellerPriority.setL1Priority(Integer.valueOf(0));
				sellerPriority.setL2Priority(Integer.valueOf(0));
				sellerPriority.setL3Priority(Integer.valueOf(0));
				sellerPriority.setL4Priority(Integer.valueOf(0));
				sellerPriority.setProductPriority(Integer.valueOf(0));
				sellerPriority.setIsValidPriority(Boolean.FALSE);
			}
			else if (!mplSellerPriorityDao.loadExistingUssid(ussid).isEmpty() && validSellerPriorityMap.containsKey(ussid))
			{
				final MplSellerPriorityLevelModel sellerPriority = priorityMap.get(ussid);
				sellerPriorityList.add(sellerPriority);
				final List<Integer> priorities = validSellerPriorityMap.get(ussid);
				if (!priorities.contains(sellerPriority.getL1Priority()))
				{
					sellerPriority.setL1Priority(Integer.valueOf(0));
				}
				if (!priorities.contains(sellerPriority.getL2Priority()))
				{
					sellerPriority.setL2Priority(Integer.valueOf(0));
				}
				if (!priorities.contains(sellerPriority.getL3Priority()))
				{
					sellerPriority.setL3Priority(Integer.valueOf(0));
				}
				if (!priorities.contains(sellerPriority.getL4Priority()))
				{
					sellerPriority.setL4Priority(Integer.valueOf(0));
				}
				if (!priorities.contains(sellerPriority.getProductPriority()))
				{
					sellerPriority.setProductPriority(Integer.valueOf(0));
				}
			}
		}
		final List<MplSellerPriorityLevelModel> existingUssidList = mplSellerPriorityDao.loadExistingUssids();
		final List<MplSellerPriorityLevelModel> nonValidUssidList = new ArrayList<MplSellerPriorityLevelModel>(
				CollectionUtils.disjunction(existingUssidList, sellerPriorityList));
		if (CollectionUtils.isNotEmpty(nonValidUssidList))
		{
			for (final MplSellerPriorityLevelModel nonValidPriority : nonValidUssidList)
			{
				nonValidPriority.setL1Priority(Integer.valueOf(0));
				nonValidPriority.setL2Priority(Integer.valueOf(0));
				nonValidPriority.setL3Priority(Integer.valueOf(0));
				nonValidPriority.setL4Priority(Integer.valueOf(0));
				nonValidPriority.setProductPriority(Integer.valueOf(0));
				nonValidPriority.setIsValidPriority(Boolean.FALSE);
				priorityMap.put(nonValidPriority.getUssid(), nonValidPriority);
			}
		}
	}

	/**
	 * @param priorityModelList
	 * @param sellerPriorityModels
	 */
	private void updateNonProcessedPriorities(final List<MplSellerPriorityModel> priorityModelList,
			final List<MplSellerPriorityModel> sellerPriorityModels)
	{
		final List<MplSellerPriorityModel> sellerPriorities = new ArrayList<MplSellerPriorityModel>();
		if (CollectionUtils.isEmpty(priorityModelList) && CollectionUtils.isEmpty(sellerPriorityModels))
		{
			try
			{
				for (final MplSellerPriorityModel priority : priorityModelList)
				{
					if (!(sellerPriorityModels.contains(priority)))
					{
						priority.setPriorityStatus(SellerPriorityEnum.ERROR);
						sellerPriorities.add(priority);
					}
				}
				modelService.saveAll(sellerPriorities);
			}
			catch (final ModelSavingException e)
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
			}
		}
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
		final List<ProductModel> productList = mplSellerPriorityDao.getProductListForCategory(category);
		for (final ProductModel product : productList)
		{
			//			if (product.getCatalogVersion().equals(getCatalogVersion()))
			//			{
			for (final SellerInformationModel seller : product.getSellerInformationRelator())
			{
				if (seller.getSellerID().equals(sellerMasterModel.getId()))
				{
					ussidList.add(seller.getSellerArticleSKU());
					//					isExits = true;
					//					break;
				}
			}
			//}
		}

		//		final List<String> ussidList = new ArrayList<String>();
		//		try
		//		{
		//			if (!(category.getProducts().isEmpty()))
		//			{
		//				for (final ProductModel productList : category.getProducts())
		//				{
		//					for (final SellerInformationModel seller : productList.getSellerInformationRelator())
		//					{
		//						if (seller.getSellerID().equals(sellerMasterModel.getId()))
		//						{
		//							ussidList.add(seller.getSellerArticleSKU());
		//							break;
		//						}
		//					}
		//				}
		//			}
		//			else
		//			{
		//				if (!(category.getCategories().isEmpty()))
		//				{
		//					for (final CategoryModel cat : category.getCategories())
		//					{
		//						final List<String> ussids = findUssidsByRecursion(cat, sellerMasterModel);
		//						ussidList.addAll(ussids);
		//					}
		//				}
		//				else
		//				{
		//					final List<String> ussids = findUssidsByRecursion(category, sellerMasterModel);
		//					ussidList.addAll(ussids);
		//				}
		//			}
		//		}
		//		catch (final Exception e)
		//		{
		//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		//		}
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
				for (final ProductModel products : category.getProducts())
				{
					for (final SellerInformationModel seller : products.getSellerInformationRelator())
					{
						if (seller.getSellerID().equals(sellerMasterModel.getId()))
						{

							//System.out.println("***************#######ussid for product in category" + ussidList + "product" + products+ "category" + category.getCode());
							log.debug("***************#######ussid for product in category" + ussidList + "product" + products
									+ "category" + category.getCode());


							ussidList.add(seller.getSellerArticleSKU());
							break;
						}
					}
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
	 * @param priorityMapList
	 */
	private Map<String, MplSellerPriorityLevelModel> getPriorityLevelData(final List<String> ussidList, final int priorityLevel,
			final boolean isActive, final Map<String, MplSellerPriorityLevelModel> priorityLevelMap,
			final List<String> priorityMapList)
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
					final List<Integer> priorityList = new ArrayList<Integer>();
					priorityList.add(sellerPriorityLevel.getL1Priority());
					priorityList.add(sellerPriorityLevel.getL2Priority());
					priorityList.add(sellerPriorityLevel.getL3Priority());
					priorityList.add(sellerPriorityLevel.getL4Priority());
					priorityList.add(sellerPriorityLevel.getProductPriority());
					if (!isActive && !(priorityMapList.contains(ussid)))
					{
						updateInvalidPriorityLevel(priorityLevel, sellerPriorityLevel);
						sellerPriorityLevel.setIsValidPriority(Boolean.FALSE);
					}
					else if (!isActive && priorityMapList.contains(ussid))
					{
						if (!priorityList.contains(Integer.valueOf(priorityLevel)))
						{
							updateInvalidPriorityLevel(priorityLevel, sellerPriorityLevel);
						}
					}
					else if (isActive)
					{
						setPriorityLevel(priorityLevel, sellerPriorityLevel);
						sellerPriorityLevel.setIsValidPriority(Boolean.valueOf(isActive));
					}

					priorityLevelMap.put(ussid, sellerPriorityLevel);

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
				// if (!(isNewEntry && !isActive))
				if (isNewEntry && isActive)
				{
					setPriorityLevel(priorityLevel, sellerPriorityLevel);
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
