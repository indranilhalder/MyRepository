/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.ExchangeCouponValueModel;
import com.tisl.mpl.core.model.ExchangeModel;
import com.tisl.mpl.core.model.ExchangeTransactionModel;
import com.tisl.mpl.marketplacecommerceservices.daos.ExchangeGuideDao;
import com.tisl.mpl.marketplacecommerceservices.daos.SizeGuideDao;
import com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService;


/**
 * @author TCS
 *
 */
public class ExchangeGuideServiceImpl implements ExchangeGuideService
{
	@Resource
	private ProductService productService;

	@Resource
	private ClassificationService classificationService;

	@Resource(name = "sizeGuideDao")
	private SizeGuideDao sizeGuideDao;

	@Autowired
	private PersistentKeyGenerator temporaryExchangeId;


	@Autowired
	private PersistentKeyGenerator EXCHANGEREQUESTID;

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

	private ModelService modelService;

	/**
	 * @return the exchangeGuideDao
	 */
	public ExchangeGuideDao getExchangeGuideDao()
	{
		return exchangeGuideDao;
	}

	/**
	 * @param exchangeGuideDao
	 *           the exchangeGuideDao to set
	 */
	public void setExchangeGuideDao(final ExchangeGuideDao exchangeGuideDao)
	{
		this.exchangeGuideDao = exchangeGuideDao;
	}

	private ExchangeGuideDao exchangeGuideDao;

	private static final Logger LOG = Logger.getLogger(ExchangeGuideServiceImpl.class);
	@Autowired
	private ConfigurationService configurationService;

	/*
	 * @Javadoc
	 * 
	 * @returns All L4 for which Exchange is Applicable
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService#getDistinctL4()
	 */
	@Override
	public boolean isExchangable(final String categoryCode)
	{
		return exchangeGuideDao.isExchangable(categoryCode);
	}


	/*
	 * @Javadoc
	 * 
	 * @param String categoryCode
	 * 
	 * @param ExchangeCouponValueModel pricematrix
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService#getExchangeGuideList(java.lang.String)
	 */
	@Override
	public List<ExchangeCouponValueModel> getExchangeGuideList(final String categoryCode) throws ModelNotFoundException
	{
		return exchangeGuideDao.getExchangeOptionforCategorycode(categoryCode);

	}

	/**
	 * @param l3code
	 * @param pincode
	 * @param l4
	 * @param isWorking
	 * @param brand
	 * @param pincode
	 * @return String ExchangeId
	 * @description Saves Exchange Transaction and returns Exchange Id before Add to Cart
	 */

	@Override
	public String getExchangeID(final String l3code, final String l4, final String isWorking, final String brand,
			final String pincode, final String guid, final String productCode, final String ussid)
	{
		String exID = "";
		final List<ExchangeCouponValueModel> exVal = getPriceMatrix(l3code, l4, isWorking);
		if (CollectionUtils.isNotEmpty(exVal))
		{
			final ExchangeTransactionModel exTrax = new ExchangeTransactionModel();
			exTrax.setBrandName(brand);
			exTrax.setCartguid(guid);
			exTrax.setPincode(pincode);
			exTrax.setExchangeValue(exVal.get(0));
			exTrax.setProductId(productCode);
			exTrax.setUssid(ussid);
			exID = getTemporaryExchangeId().generate().toString();
			exTrax.setExchangeId(exID);
			modelService.save(exTrax);
		}

		return exID;
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService#getPriceMatrix(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public List<ExchangeCouponValueModel> getPriceMatrix(final String l3code, final String l4, final String isWorking)
	{
		return exchangeGuideDao.getPriceMatrix(l3code, l4, isWorking);
	}


	@Override
	public boolean isBackwardServiceble(final String pincode)
	{
		return exchangeGuideDao.isBackwardServiceble(pincode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService#changePincode(java.lang.String)
	 */
	@Override
	public boolean changePincode(final String pincode, final String exchangeId)
	{
		boolean isSaved = false;
		final List<ExchangeTransactionModel> exList = getTeporaryExchangeModelforId(exchangeId);
		final List<ExchangeTransactionModel> exListToSave = new ArrayList<>();
		for (final ExchangeTransactionModel ex : exList)
		{
			ex.setPincode(pincode);
			exListToSave.add(ex);
		}
		modelService.saveAll(exListToSave);
		isSaved = true;
		// YTODO Auto-generated method stub
		return isSaved;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService#removeFromTransactionTable(java.lang.String)
	 */
	@Override
	public boolean removeFromTransactionTable(final String exchangeId, final String reason, final CartModel cart)
	{
		boolean isSaved = false;
		final List<ExchangeTransactionModel> exList = getTeporaryExchangeModelforId(exchangeId);
		final String id = getExchangeRequestID(exList, true, reason);
		if (StringUtils.isNotBlank(id))
		{
			isSaved = true;
			boolean hasExchangeinCart = false;
			if (cart != null && cart.getExchangeAppliedCart() != null && cart.getExchangeAppliedCart().booleanValue())
			{
				hasExchangeinCart = true;

			}
			if (!hasExchangeinCart && cart != null)
			{
				cart.setExchangeAppliedCart(Boolean.FALSE);
				modelService.save(cart);
			}
		}


		return isSaved;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService#getTeporaryExchangeModelforId(java.lang.
	 * String)
	 */
	@Override
	public List<ExchangeTransactionModel> getTeporaryExchangeModelforId(final String exId)
	{

		return exchangeGuideDao.getTeporaryExchangeModelforId(exId);
	}




	/**
	 *
	 * @return String ExchangeId
	 * @description Saves Exchange Transaction and returns Exchange Id before Add to Cart
	 */

	@Override
	public String getExchangeRequestID(final OrderModel order)
	{
		final List<ExchangeModel> exModList = new ArrayList<>();
		final List<OrderModel> childModfList = new ArrayList();
		final List<AbstractOrderEntryModel> parentEntryList = new ArrayList<>();
		final List<AbstractOrderEntryModel> childOrderEntryList = new ArrayList<>();
		final List<ExchangeTransactionModel> exTraxRemovList = new ArrayList();
		String exReqId = "";
		for (final OrderModel child : order.getChildOrders())
		{
			boolean changeInChild = false;
			final List<AbstractOrderEntryModel> entryDetails = child.getEntries();
			for (final AbstractOrderEntryModel entry : entryDetails)
			{
				if (entry != null && StringUtils.isNotEmpty(entry.getExchangeId()))
				{
					final List<ExchangeTransactionModel> exTraxList = getTeporaryExchangeModelforId(entry.getExchangeId());
					for (final ExchangeTransactionModel exTrax : exTraxList)
					{
						final ExchangeModel exMod = modelService.create(ExchangeModel.class);
						exReqId = getEXCHANGEREQUESTID().generate().toString();
						exMod.setBrandName(exTrax.getBrandName());
						exMod.setExchangeRequestId(exReqId);
						exMod.setL4categoryCode(exTrax.getExchangeValue().getL4categoryCode());
						exMod.setL4categoryName(exTrax.getExchangeValue().getL4categoryName());
						exMod.setThirdLevelCategory(exTrax.getExchangeValue().getThirdLevelCategory());
						exMod.setCouponValue(exTrax.getExchangeValue().getCouponValue());
						exMod.setIsWorking(exTrax.getExchangeValue().isIsWorking());
						exMod.setL4categoryCode(exTrax.getExchangeValue().getL4categoryCode());

						exMod.setOrderID(child.getParentReference().getCode());
						exMod.setPincode(exTrax.getPincode());
						exMod.setProductId(exTrax.getProductId());
						exMod.setSellerOrderID(child.getCode());
						exMod.setTransactiondId(entry.getTransactionID());
						exMod.setUssid(exTrax.getUssid());
						exModList.add(exMod);
						exTraxRemovList.add(exTrax);
						entry.setExchangeId(exReqId);
						changeInChild = true;
						childOrderEntryList.add(entry);
					}
				}
			}

			for (final AbstractOrderEntryModel entry : child.getParentReference().getEntries())
			{
				if (entry != null && StringUtils.isNotEmpty(entry.getExchangeId()))
				{
					entry.setExchangeId(exReqId);
				}
				parentEntryList.add(entry);
			}
			childModfList.add(child);

		}

		modelService.saveAll(exModList);
		modelService.saveAll(childOrderEntryList);
		modelService.saveAll(childModfList);
		modelService.saveAll(parentEntryList);
		modelService.removeAll(exTraxRemovList);

		return exReqId;
	}

	@Override
	public String getExchangeRequestID(final List<ExchangeTransactionModel> exTraxList, final boolean isInternal,
			final String reason)
	{
		final List<ExchangeModel> exModList = new ArrayList<>();
		final List<ExchangeTransactionModel> exTraxRemovList = new ArrayList();
		String exReqId = "";
		for (final ExchangeTransactionModel exTrax : exTraxList)
		{
			final ExchangeModel exMod = new ExchangeModel();
			exReqId = getEXCHANGEREQUESTID().generate().toString();
			exMod.setBrandName(exTrax.getBrandName());
			exMod.setExchangeRequestId(exReqId);
			exMod.setL4categoryCode(exTrax.getExchangeValue().getL4categoryCode());
			exMod.setL4categoryName(exTrax.getExchangeValue().getL4categoryName());
			exMod.setThirdLevelCategory(exTrax.getExchangeValue().getThirdLevelCategory());
			exMod.setCouponValue(exTrax.getExchangeValue().getCouponValue());
			exMod.setIsWorking(exTrax.getExchangeValue().isIsWorking());
			exMod.setL4categoryCode(exTrax.getExchangeValue().getL4categoryCode());
			exMod.setOrderID(exTrax.getCartguid());
			exMod.setPincode(exTrax.getPincode());
			exMod.setProductId(exTrax.getProductId());
			exMod.setSellerOrderID(exTrax.getCartguid());
			exMod.setTransactiondId(exTrax.getCartguid());
			exMod.setUssid(exTrax.getUssid());
			if (StringUtils.isNotEmpty(reason))
			{
				exMod.setExchangeRemovalReason(reason);
			}
			else
			{
				exMod.setExchangeRemovalReason("Exchange Removed from Cart/Delivery Page due to Pincode Servicability");
			}
			exModList.add(exMod);
			exTraxRemovList.add(exTrax);

		}

		modelService.saveAll(exModList);
		modelService.removeAll(exTraxRemovList);

		return exReqId;
	}

	@Override
	public void removeExchangefromCart(final CartModel cartModel)
	{
		String removeExchangeIdList = null;
		final List<AbstractOrderEntryModel> entryUpdate = new ArrayList();
		for (final AbstractOrderEntryModel entry : cartModel.getEntries())
		{
			if (StringUtils.isEmpty(removeExchangeIdList) && StringUtils.isNotEmpty(entry.getExchangeId()))
			{
				removeExchangeIdList = entry.getExchangeId();
				entry.setExchangeId("");
				entryUpdate.add(entry);
			}
			else if (StringUtils.isNotEmpty(removeExchangeIdList) && StringUtils.isNotEmpty(entry.getExchangeId()))
			{
				removeExchangeIdList += entry.getExchangeId();
				entry.setExchangeId("");
				entryUpdate.add(entry);
			}

		}
		modelService.saveAll(entryUpdate);

		removeFromTransactionTable(removeExchangeIdList, null, cartModel);

	}

	@Override
	public void changeGuidforCartMerge(final CartModel cart)
	{
		String exIdStringList = null;
		final List<ExchangeTransactionModel> listToSave = new ArrayList<>();
		for (final AbstractOrderEntryModel entry : cart.getEntries())
		{
			if (StringUtils.isNotEmpty(entry.getExchangeId()))
			{
				if (StringUtils.isEmpty(exIdStringList))
				{
					exIdStringList = entry.getExchangeId();
				}
				else
				{
					exIdStringList = exIdStringList + "," + entry.getExchangeId();
				}

			}
		}
		final List<ExchangeTransactionModel> exTraxList = getTeporaryExchangeModelforId(exIdStringList);
		for (final ExchangeTransactionModel exTrax : exTraxList)
		{
			exTrax.setCartguid(cart.getGuid());
			listToSave.add(exTrax);
		}

		modelService.saveAll(listToSave);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService#addToExchangeTable(com.tisl.mpl.core.model
	 * .ExchangeTransactionModel)
	 */
	@Override
	public boolean addToExchangeTable(final ExchangeTransactionModel ex)
	{
		// YTODO Auto-generated method stub
		return false;
	}

	/**
	 * @return the temporaryExchangeId
	 */
	public PersistentKeyGenerator getTemporaryExchangeId()
	{
		return temporaryExchangeId;
	}

	/**
	 * @param temporaryExchangeId
	 *           the temporaryExchangeId to set
	 */
	public void setTemporaryExchangeId(final PersistentKeyGenerator temporaryExchangeId)
	{
		this.temporaryExchangeId = temporaryExchangeId;
	}

	/**
	 * @return the eXCHANGEREQUESTID
	 */
	public PersistentKeyGenerator getEXCHANGEREQUESTID()
	{
		return EXCHANGEREQUESTID;
	}

	/**
	 * @param eXCHANGEREQUESTID
	 *           the eXCHANGEREQUESTID to set
	 */
	public void setEXCHANGEREQUESTID(final PersistentKeyGenerator eXCHANGEREQUESTID)
	{
		EXCHANGEREQUESTID = eXCHANGEREQUESTID;
	}




}