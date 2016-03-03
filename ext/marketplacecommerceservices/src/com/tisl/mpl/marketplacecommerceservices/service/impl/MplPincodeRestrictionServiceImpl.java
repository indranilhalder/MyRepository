package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPincodeRestrictionDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplPincodeRestrictionService;
import com.tisl.mpl.model.RestrictionsetupModel;


/**
 * @author TCS
 */

public class MplPincodeRestrictionServiceImpl implements MplPincodeRestrictionService
{

	private static final String EXPRESS_DELIVERY = "Express Delivery";
	private static final String HOME_DELIVERY = "Home Delivery";
	private static final String ED = "ED";
	private static final String HD = "HD";
	private static final String N = "N";
	private static final String Y = "Y";
	protected static final String CATALOG_ID = "mplProductCatalog";
	protected static final String VERSION_ONLINE = "Online";

	private static final Logger LOG = Logger.getLogger(MplPincodeRestrictionServiceImpl.class);

	@Autowired
	private MplPincodeRestrictionDao mplPincodeRestrictionDao;


	@Autowired
	private CatalogVersionService catalogVersionService;

	@Resource(name = "productService")
	private ProductService productService;


	@Resource(name = "defaultPromotionManager")
	private DefaultPromotionManager defaultPromotionManager;

	//	@Autowired
	//	private CMSSiteService cmsSiteService;

	/**
	 * @return the mplPincodeRestrictionDao
	 */
	public MplPincodeRestrictionDao getMplPincodeRestrictionDao()
	{
		return mplPincodeRestrictionDao;
	}


	/**
	 *
	 * @param mplPincodeRestrictionDao
	 */
	public void setMplPincodeRestrictionDao(final MplPincodeRestrictionDao mplPincodeRestrictionDao)
	{
		this.mplPincodeRestrictionDao = mplPincodeRestrictionDao;
	}

	@Autowired
	private ModelService modelService;


	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}



	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}





	/**
	 * @description this method checks the restriction list and calls pincode service accordingly
	 *
	 * @param articleSKUID
	 * @param sellerIdList
	 * @param pincode
	 * @return listingId
	 * @throws EtailNonBusinessExceptions
	 */

	@Override
	public List<PincodeServiceData> getRestrictedPincode(final List<String> articleSKUID, final List<String> sellerIdList,
			final String listingID, final String pincode, final List<PincodeServiceData> reqData) throws EtailNonBusinessExceptions
	{
		CopyOnWriteArrayList<PincodeServiceData> reqDataList = new CopyOnWriteArrayList<PincodeServiceData>(reqData);

		final List<RestrictionsetupModel> restrictionsetupModelList = mplPincodeRestrictionDao.getRestrictedPincode(pincode,
				articleSKUID, sellerIdList, listingID, getCategoryCodeList(listingID));
		final List<String> listingIdList = new ArrayList<String>();
		listingIdList.add(listingID);

		reqDataList = checkPincodeRestriction(sellerIdList, listingIdList, reqData, restrictionsetupModelList);

		return reqDataList;
	}

	private CopyOnWriteArrayList<PincodeServiceData> checkPincodeRestriction(final List<String> sellerIdList,
			final List<String> listingIdList, final List<PincodeServiceData> reqData,
			final List<RestrictionsetupModel> restrictionsetupModelList)
	{

		boolean block = false;
		CopyOnWriteArrayList<PincodeServiceData> reqDataList = new CopyOnWriteArrayList<PincodeServiceData>(reqData);
		final Map<String, Boolean> ussidMap = new HashMap<String, Boolean>();
		final Map<String, Boolean> sellerIdMap = new HashMap<String, Boolean>();
		boolean booleanTshipCODRestricted = false;

		final Map<String, List<String>> restrictedDeliveryModeMap = new HashMap<String, List<String>>();
		List<String> deliveryModeList = null;
		for (final RestrictionsetupModel r : restrictionsetupModelList)
		{
			deliveryModeList = getRestrictedDeliveryModes(r.getDeliveryMode());
			//For Sship,seler level restriction
			if (sellerIdList.contains(r.getSellerId()))
			{
				if (null != r.getUSSID())
				{
					//check for restricted deliverymodes if present for the ussid

					restrictedDeliveryModeMap.put(r.getUSSID(), deliveryModeList);
					if (r.getCodRestricted().equalsIgnoreCase(Y) && r.getPrepaidRestricted().equalsIgnoreCase(Y)
							&& checkFullfillmentType(reqData, r.getShipmentMode(), r.getUSSID(), null)) //Defect fix - TISST-12903
					{

						ussidMap.put(r.getUSSID(), Boolean.TRUE);
						LOG.debug("cod & preapaid restricted skuIds" + r.getUSSID());
					}
					else
					{
						if (r.getCodRestricted().equalsIgnoreCase(Y))
						{
							ussidMap.put(r.getUSSID(), Boolean.FALSE);
							LOG.debug("Only cod restricted skuIds" + r.getUSSID());
						}
					}
				}
				if (null != r.getPrimaryCatID() && getCategoryCodeListCart(listingIdList).contains(r.getPrimaryCatID()))
				{
					//check for restricted deliverymodes if present for the sellerid
					deliveryModeList = getRestrictedDeliveryModes(r.getDeliveryMode());
					restrictedDeliveryModeMap.put(r.getSellerId(), deliveryModeList);
					if (r.getCodRestricted().equalsIgnoreCase(Y) && r.getPrepaidRestricted().equalsIgnoreCase(Y)
							&& checkFullfillmentType(reqData, r.getShipmentMode(), null, r.getSellerId())) //Defect fix - TISST-12903
					{
						sellerIdMap.put(r.getSellerId(), Boolean.TRUE);
						LOG.debug("cod & preapaid restricted skuIds" + r.getSellerId());
					}
					else
					{
						if (r.getCodRestricted().equalsIgnoreCase(Y))
						{
							sellerIdMap.put(r.getSellerId(), Boolean.FALSE);
							LOG.debug("Only cod restricted skuIds" + r.getSellerId());
						}
					}
				}
			}
			else
			//For Tship,independent of seller
			{
				if (null != r.getPrimaryCatID() && getCategoryCodeListCart(listingIdList).contains(r.getPrimaryCatID()))
				{
					if (r.getCodRestricted().equalsIgnoreCase(Y) && r.getPrepaidRestricted().equalsIgnoreCase(Y))
					{
						LOG.debug("Restricted Category Id" + r.getPrimaryCatID());
						block = true;
						break;
					}
					else
					{
						if (r.getCodRestricted().equalsIgnoreCase(Y))
						{
							LOG.debug("Cod Restricted Category Id" + r.getPrimaryCatID());
							restrictedDeliveryModeMap.put(r.getPrimaryCatID(), deliveryModeList);
							booleanTshipCODRestricted = true;
						}
					}
				}
				if (null != r.getListingID() && listingIdList.contains(r.getListingID()))
				{
					if (r.getCodRestricted().equalsIgnoreCase(Y) && r.getPrepaidRestricted().equalsIgnoreCase(Y))
					{
						LOG.debug("Restricted Listing Id" + r.getListingID());
						block = true;
						break;
					}
					else
					{
						if (r.getCodRestricted().equalsIgnoreCase(Y))
						{
							LOG.debug("Cod Restricted Listing Id" + r.getListingID());
							restrictedDeliveryModeMap.put(r.getListingID(), deliveryModeList);
							booleanTshipCODRestricted = true;
						}
					}
				}
			}
		}
		if (!(restrictionsetupModelList.isEmpty()))
		{

			if (!block)
			{
				reqDataList = populateRequestDataList(ussidMap, sellerIdMap, restrictedDeliveryModeMap, reqDataList,
						booleanTshipCODRestricted);
			}
			else
			//blocked in listingId or categoryId level
			{
				reqDataList = null;
			}
		}

		return reqDataList;
	}

	/*
	 * This method will check whether fulfillment type is matching or not.
	 *
	 * @param reqData
	 *
	 * @param fullfillmentType
	 *
	 * @param ussId
	 *
	 * @param selleId
	 *
	 * @return flag
	 */
	private boolean checkFullfillmentType(final List<PincodeServiceData> reqData, final String fullfillmentType,
			final String ussId, final String selleId)
	{
		boolean flag = false;
		if (null == fullfillmentType || fullfillmentType.isEmpty())
		{
			return true;
		}
		for (final PincodeServiceData pincodeData : reqData)
		{
			if ((pincodeData.getUssid().equalsIgnoreCase(ussId) || pincodeData.getSellerId().equalsIgnoreCase(selleId))
					&& fullfillmentType.equalsIgnoreCase(pincodeData.getFullFillmentType()))
			{
				flag = true;
			}
		}

		return flag;
	}


	@Override
	public List<PincodeServiceData> getRestrictedPincodeCart(final String pincode, final List<PincodeServiceData> requestData)
			throws EtailNonBusinessExceptions
	{
		final List<String> ussidList = new ArrayList<String>();
		final List<String> sellerIdList = new ArrayList<String>();
		final List<String> productCodeList = new ArrayList<String>();
		for (final PincodeServiceData reqData : requestData)
		{
			ussidList.add(reqData.getUssid());
			sellerIdList.add(reqData.getSellerId());
			productCodeList.add(reqData.getProductCode());
		}
		//boolean block = false;
		CopyOnWriteArrayList<PincodeServiceData> reqDataList = new CopyOnWriteArrayList<PincodeServiceData>(requestData);

		final List<RestrictionsetupModel> restrictionsetupModelList = mplPincodeRestrictionDao.getRestrictedPincodeCart(pincode,
				ussidList, sellerIdList, productCodeList, getCategoryCodeListCart(productCodeList));
		reqDataList = checkPincodeRestriction(sellerIdList, productCodeList, requestData, restrictionsetupModelList);

		return reqDataList;
	}

	/**
	 * @param productCodeList
	 * @return List<String>
	 */
	private List<String> getCategoryCodeListCart(final List<String> productCodeList)
	{
		final List<String> categoryList = new ArrayList<String>();
		for (final String listingID : productCodeList)
		{
			if (listingID != null)
			{
				LOG.debug("Listing ID of the product" + listingID);

				final ProductModel productModel = productService.getProductForCode(
						catalogVersionService.getCatalogVersion("mplProductCatalog", "Online"), listingID);
				//TISEE-6376
				if (productModel != null)
				{
					final List<CategoryModel> categoryModelList = defaultPromotionManager.getcategoryData(productModel);
					//TISEE-6376
					if (CollectionUtils.isNotEmpty(categoryModelList))
					{
						for (final CategoryModel categoryModel : categoryModelList)
						{
							//TISEE-6376
							if (categoryModel != null && categoryModel.getCode() != null)
							{
								categoryList.add(categoryModel.getCode());
							}
						}
					}
				}
			}
		}
		return categoryList;
	}

	/**
	 * populating request data based on restriction list
	 *
	 * @param ussidMap
	 * @param sellerIdMap
	 * @param restrictedDeliveryModeMap
	 * @param reqDataList
	 * @param booleanTshipCODRestricted
	 * @return CopyOnWriteArrayList<PincodeServiceData>
	 */
	private CopyOnWriteArrayList<PincodeServiceData> populateRequestDataList(final Map<String, Boolean> ussidMap,
			final Map<String, Boolean> sellerIdMap, final Map<String, List<String>> restrictedDeliveryModeMap,
			CopyOnWriteArrayList<PincodeServiceData> reqDataList, final boolean booleanTshipCODRestricted)
	{
		for (final PincodeServiceData pincodeServiceData : reqDataList)
		{
			if (booleanTshipCODRestricted)
			{

				populateValidDeliveryModes(pincodeServiceData, restrictedDeliveryModeMap);
				//checking if any deliverymodes present after restriction,otherwise remove the ussid
				if (!(pincodeServiceData.getDeliveryModes().isEmpty()))
				{
					pincodeServiceData.setIsCOD(N);
				}
				else
				{
					reqDataList.remove(pincodeServiceData);
				}

			}
			else
			{
				if (null != ussidMap.get(pincodeServiceData.getUssid()))
				{
					if (ussidMap.get(pincodeServiceData.getUssid()).booleanValue())
					{
						reqDataList.remove(pincodeServiceData);
					}
					else
					{
						if (null != restrictedDeliveryModeMap.get(pincodeServiceData.getUssid()))
						{

							populateValidDeliveryModes(pincodeServiceData, restrictedDeliveryModeMap);

						}
						//checking if any deliverymodes present after restriction,otherwise remove the ussid
						if (!(pincodeServiceData.getDeliveryModes().isEmpty()))
						{
							pincodeServiceData.setIsCOD(N);
						}
						else
						{
							reqDataList.remove(pincodeServiceData);
						}
					}
				}
				if (null != sellerIdMap.get(pincodeServiceData.getSellerId()))
				{
					if (sellerIdMap.get(pincodeServiceData.getSellerId()).booleanValue())
					{
						reqDataList.remove(pincodeServiceData);
					}
					else
					{

						if (null != restrictedDeliveryModeMap.get(pincodeServiceData.getSellerId()))
						{
							populateValidDeliveryModes(pincodeServiceData, restrictedDeliveryModeMap);
						}
						//checking if any deliverymodes present after restriction,otherwise remove the ussid
						if (!(pincodeServiceData.getDeliveryModes().isEmpty()))
						{
							pincodeServiceData.setIsCOD(N);
						}
						else
						{
							reqDataList.remove(pincodeServiceData);
						}

					}
				}
			}
		}
		if (reqDataList.isEmpty())
		{
			reqDataList = null;
		}
		return reqDataList;

	}

	/**
	 * populating unrestricted deliverymodes for aussid againgst a pincode
	 *
	 * @param pincodeServiceData
	 * @param restricteddeliveryModeMap
	 */
	private void populateValidDeliveryModes(final PincodeServiceData pincodeServiceData,
			final Map<String, List<String>> restricteddeliveryModeMap)
	{
		final List<MarketplaceDeliveryModeData> deliveryModes = new ArrayList<MarketplaceDeliveryModeData>(
				pincodeServiceData.getDeliveryModes());
		for (final MarketplaceDeliveryModeData deliveryModeData : deliveryModes)
		{
			if (!(restricteddeliveryModeMap.isEmpty()))
			{

				if (null != restricteddeliveryModeMap.get(pincodeServiceData.getUssid())
						&& restricteddeliveryModeMap.get(pincodeServiceData.getUssid()).contains(deliveryModeData.getName()))
				{
					pincodeServiceData.getDeliveryModes().remove(deliveryModeData);
					break;
				}
				if (null != restricteddeliveryModeMap.get(pincodeServiceData.getSellerId())
						&& restricteddeliveryModeMap.get(pincodeServiceData.getSellerId()).contains(deliveryModeData.getName()))
				{
					pincodeServiceData.getDeliveryModes().remove(deliveryModeData);
					break;
				}
				//Start - Code additon TISPRO-167
				// added for Category Id
				final List<String> categoryList = getCategoryCodeList(pincodeServiceData.getProductCode());
				if (CollectionUtils.isNotEmpty(categoryList))
				{
					for (final Map.Entry<String, List<String>> entry : restricteddeliveryModeMap.entrySet())
					{
						if (entry.getValue().contains(deliveryModeData.getName()) && categoryList.contains(entry.getKey()))
						{
							pincodeServiceData.getDeliveryModes().remove(deliveryModeData);
							break;
						}
					}
				}
				// added for listing id check
				for (final Map.Entry<String, List<String>> entry : restricteddeliveryModeMap.entrySet())
				{
					if (entry.getValue().contains(deliveryModeData.getName())
							&& pincodeServiceData.getProductCode().equalsIgnoreCase(entry.getKey()))
					{
						pincodeServiceData.getDeliveryModes().remove(deliveryModeData);
						break;
					}
				}
				//End - Code additon TISPRO-167
				/*
				 * else {
				 *
				 * for (final Map.Entry<String, List<String>> entry : restricteddeliveryModeMap.entrySet()) { if
				 * (entry.getValue().contains(deliveryModeData.getName())) {
				 * pincodeServiceData.getDeliveryModes().remove(deliveryModeData); break; }
				 *
				 * }
				 *
				 *
				 * }
				 */

			}
		}

	}

	/**
	 * getting category hierarchy list for a product
	 *
	 * @param listingID
	 * @return categoryList
	 */

	private List<String> getCategoryCodeList(final String listingID)
	{


		final List<String> categoryList = new ArrayList<String>();
		final JaloSession session = JaloSession.getCurrentSession();
		session.createLocalSessionContext();
		try
		{

			Collection<CatalogVersion> vers = null;

			final Collection<CatalogVersion> cvs = (Collection<CatalogVersion>) session
					.getAttribute(CatalogConstants.SESSION_CATALOG_VERSIONS);

			for (final CatalogVersion ver : cvs)
			{
				if (VERSION_ONLINE.equals(ver.getVersion()) && CATALOG_ID.equals(ver.getCatalog().getId()))
				{
					vers = Collections.singleton(ver);
					break;
				}
			}
			session.setAttribute(CatalogConstants.SESSION_CATALOG_VERSIONS, vers);

			final ProductModel productModel = productService.getProductForCode(listingID);
			for (final CategoryModel c : defaultPromotionManager.getcategoryData(productModel))
			{
				categoryList.add(c.getCode());
			}
		}
		finally
		{
			session.removeLocalSessionContext();
		}
		return categoryList;



	}

	/**
	 * get restricted deliverymodes for a pincode
	 *
	 * @param deliveryMode
	 * @return deliveryModes
	 */
	private List<String> getRestrictedDeliveryModes(final String deliveryMode)
	{
		// YTODO Auto-generated method stub
		final List<String> deliveryModes = new ArrayList<String>();

		if (deliveryMode.equals(HD))
		{
			deliveryModes.add(HOME_DELIVERY);
			LOG.debug("Restricted deliveryModes is" + deliveryMode);
		}
		if ((deliveryMode.equals(ED)))
		{
			deliveryModes.add(EXPRESS_DELIVERY);
			LOG.debug("Restricted deliveryModes is" + deliveryMode);
		}

		return deliveryModes;
	}




}
