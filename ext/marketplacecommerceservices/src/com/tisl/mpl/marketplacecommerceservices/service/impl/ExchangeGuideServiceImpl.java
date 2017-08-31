/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.ExchangeCouponValueModel;
import com.tisl.mpl.core.model.ExchangeModel;
import com.tisl.mpl.core.model.ExchangeTransactionModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.ExchangeGuideDao;
import com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService;


/**
 * @author TCS
 *
 */
public class ExchangeGuideServiceImpl implements ExchangeGuideService
{
	//SONAR FIX JEWELLERY
	protected static final Logger LOG = Logger.getLogger(ExchangeGuideServiceImpl.class);
	private static final String ERROR_OCCURED = "Error Occured At ";
	private static final String WHILE_SAVING = "While Saving";

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

	//SONAR  FIX JEWELLERY
	//private static final Logger LOG = Logger.getLogger(ExchangeGuideServiceImpl.class);
	//SONAR  FIX JEWELLERY
	//	@Autowired
	//	private ConfigurationService configurationService;

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

		try
		{
			return exchangeGuideDao.isExchangable(categoryCode);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(ERROR_OCCURED + "isExchangable");
			return false;
		}

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
		try
		{
			return exchangeGuideDao.getExchangeOptionforCategorycode(categoryCode);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(ERROR_OCCURED + "getExchangeGuideList");
			return null;
		}

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
		String exID = MarketplacecommerceservicesConstants.EMPTYSPACE;
		try
		{
			final List<ExchangeCouponValueModel> exVal = getPriceMatrix(l3code, l4, isWorking);
			if (CollectionUtils.isNotEmpty(exVal))
			{
				final ExchangeTransactionModel exTrax = modelService.create(ExchangeTransactionModel.class);
				exTrax.setBrandName(brand);
				exTrax.setCartguid(guid);
				exTrax.setPincode(pincode);
				exTrax.setExchangeValue(exVal.get(0));
				exTrax.setProductId(productCode);
				exTrax.setUssid(ussid);
				exID = getTemporaryExchangeId().generate().toString();
				exTrax.setExchangeId(exID);
				modelService.save(exTrax);
				//Added for EQA Comments
				modelService.refresh(exTrax);
			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error(ERROR_OCCURED + WHILE_SAVING + "getExchangeID");
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(ERROR_OCCURED + "getExchangeID");

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
		try
		{
			return exchangeGuideDao.getPriceMatrix(l3code, l4, isWorking);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(ERROR_OCCURED + "getPriceMatrix");
			return null;

		}
	}


	@Override
	public boolean isBackwardServiceble(final String pincode)
	{
		try
		{
			return exchangeGuideDao.isBackwardServiceble(pincode);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(ERROR_OCCURED + "isBackwardServiceble");
			return false;

		}
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
		try
		{
			final List<ExchangeTransactionModel> exList = getTeporaryExchangeModelforId(exchangeId);
			final List<ExchangeTransactionModel> exListToSave = new ArrayList<>();
			for (final ExchangeTransactionModel ex : exList)
			{
				ex.setPincode(pincode);
				exListToSave.add(ex);
			}
			modelService.saveAll(exListToSave);
			isSaved = true;
		}
		catch (final ModelSavingException e)
		{
			LOG.error(ERROR_OCCURED + WHILE_SAVING + "changePincode");
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(ERROR_OCCURED + "changePincode");

		}
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
		try
		{
			final List<ExchangeTransactionModel> exList = getTeporaryExchangeModelforId(exchangeId);
			final String id = getExchangeRequestID(exList, true, reason);
			if (StringUtils.isNotBlank(id))
			{
				isSaved = true;
				boolean hasExchangeinCart = false;
				if (cart != null)
				{
					for (final AbstractOrderEntryModel entry : cart.getEntries())
					{
						if (StringUtils.isNotEmpty(entry.getExchangeId()))
						{
							hasExchangeinCart = true;
							break;
						}
					}
				}
				if (!hasExchangeinCart && cart != null)
				{
					cart.setExchangeAppliedCart(Boolean.FALSE);
					modelService.save(cart);
					//Added for EQA Comments
					modelService.refresh(cart);
				}
			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error(ERROR_OCCURED + WHILE_SAVING + "removeFromTransactionTable");
		}

		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(ERROR_OCCURED + "removeFromTransactionTable");

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
		try
		{
			return exchangeGuideDao.getTeporaryExchangeModelforId(exId);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(ERROR_OCCURED + "getTeporaryExchangeModelforId");
			return null;
		}

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
		String exReqId = MarketplacecommerceservicesConstants.EMPTYSPACE;
		final Map<String, String> productExcId = new HashMap<>();

		try
		{
			for (final OrderModel child : order.getChildOrders())
			{
				//	boolean changeInChild = false; SONAR FIX JEWELLERY
				final List<AbstractOrderEntryModel> entryDetails = child.getEntries();
				for (final AbstractOrderEntryModel entry : entryDetails)
				{
					if (entry != null && StringUtils.isNotEmpty(entry.getExchangeId()))
					{
						final List<ExchangeTransactionModel> exTraxList = getTeporaryExchangeModelforId(entry.getExchangeId());
						if (CollectionUtils.isNotEmpty(exTraxList))
						{
							for (final ExchangeTransactionModel exTrax : exTraxList)
							{
								final ExchangeModel exMod = modelService.create(ExchangeModel.class);
								exReqId = getEXCHANGEREQUESTID().generate().toString();
								if (exTrax.getBrandName() != null)
								{
									exMod.setBrandName(exTrax.getBrandName());
								}
								exMod.setExchangeRequestId(exReqId);
								if (exTrax.getExchangeValue() != null && exTrax.getExchangeValue().getL4categoryCode() != null)
								{
									exMod.setL4categoryCode(exTrax.getExchangeValue().getL4categoryCode());

								}
								if (exTrax.getExchangeValue() != null && exTrax.getExchangeValue().getL4categoryName() != null)
								{
									exMod.setL4categoryName(exTrax.getExchangeValue().getL4categoryName());
								}
								if (exTrax.getExchangeValue() != null && exTrax.getExchangeValue().getThirdLevelCategory() != null)
								{
									exMod.setThirdLevelCategory(exTrax.getExchangeValue().getThirdLevelCategory());
								}
								if (exTrax.getExchangeValue() != null && exTrax.getExchangeValue().getCouponValue() != null)
								{
									exMod.setCouponValue(exTrax.getExchangeValue().getCouponValue());
								}
								if (exTrax.getExchangeValue() != null)
								{
									exMod.setIsWorking(exTrax.getExchangeValue().isIsWorking());
								}
								if (exTrax.getExchangeValue() != null && exTrax.getExchangeValue().getL4categoryCode() != null)
								{
									exMod.setL4categoryCode(exTrax.getExchangeValue().getL4categoryCode());
								}
								if (child.getParentReference() != null && child.getParentReference().getCode() != null)
								{
									exMod.setOrderID(child.getParentReference().getCode());
								}
								if (exTrax.getPincode() != null)
								{
									exMod.setPincode(exTrax.getPincode());
								}
								if (exTrax.getProductId() != null)
								{
									exMod.setProductId(exTrax.getProductId());
									productExcId.put(exTrax.getProductId(), exReqId);
								}
								if (child.getCode() != null)
								{
									exMod.setSellerOrderID(child.getCode());
								}
								if (entry.getTransactionID() != null)
								{
									exMod.setTransactiondId(entry.getTransactionID());
								}
								if (exTrax.getUssid() != null)
								{
									exMod.setUssid(exTrax.getUssid());
								}

								exModList.add(exMod);
								exTraxRemovList.add(exTrax);
								entry.setExchangeId(exReqId);
								//	changeInChild = true; SONAR FIX JEWELLERY
								childOrderEntryList.add(entry);
								childModfList.add(child);
							}
						}
					}
				}

			}
			if (MapUtils.isNotEmpty(productExcId))
			{
				for (final AbstractOrderEntryModel entry : order.getEntries())
				{
					ProductModel product = null;
					if (entry != null && StringUtils.isNotEmpty(entry.getExchangeId()))
					{
						product = entry.getProduct();
					}
					if (product != null && StringUtils.isNotEmpty(product.getCode()) && productExcId.containsKey(product.getCode()))
					{
						entry.setExchangeId(exReqId);
					}
					parentEntryList.add(entry);
				}
			}

			if (CollectionUtils.isNotEmpty(exModList))
			{
				modelService.saveAll(exModList);
			}

			if (CollectionUtils.isNotEmpty(childOrderEntryList))
			{
				modelService.saveAll(childOrderEntryList);
			}

			if (CollectionUtils.isNotEmpty(childModfList))
			{

				modelService.saveAll(childModfList);
			}

			if (CollectionUtils.isNotEmpty(parentEntryList))
			{

				modelService.saveAll(parentEntryList);
			}

			if (CollectionUtils.isNotEmpty(exTraxRemovList))
			{

				modelService.removeAll(exTraxRemovList);
			}

		}
		catch (final ModelSavingException e)
		{
			LOG.error(ERROR_OCCURED + WHILE_SAVING + "getExchangeRequestID2");
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(ERROR_OCCURED + "getExchangeRequestID2");

		}
		return exReqId;
	}

	@Override
	public String getExchangeRequestID(final List<ExchangeTransactionModel> exTraxList, final boolean isInternal,
			final String reason)
	{
		final List<ExchangeModel> exModList = new ArrayList<>();
		final List<ExchangeTransactionModel> exTraxRemovList = new ArrayList();
		String exReqId = MarketplacecommerceservicesConstants.EMPTYSPACE;
		try
		{
			if (CollectionUtils.isNotEmpty(exTraxList))
			{
				for (final ExchangeTransactionModel exTrax : exTraxList)
				{
					final ExchangeModel exMod = new ExchangeModel();
					exReqId = getEXCHANGEREQUESTID().generate().toString();
					if (exTrax.getBrandName() != null)
					{
						exMod.setBrandName(exTrax.getBrandName());
					}
					exMod.setExchangeRequestId(exReqId);
					if (exTrax.getExchangeValue() != null && exTrax.getExchangeValue().getL4categoryCode() != null)
					{
						exMod.setL4categoryCode(exTrax.getExchangeValue().getL4categoryCode());

					}
					if (exTrax.getExchangeValue() != null && exTrax.getExchangeValue().getL4categoryName() != null)
					{
						exMod.setL4categoryName(exTrax.getExchangeValue().getL4categoryName());
					}
					if (exTrax.getExchangeValue() != null && exTrax.getExchangeValue().getThirdLevelCategory() != null)
					{
						exMod.setThirdLevelCategory(exTrax.getExchangeValue().getThirdLevelCategory());
					}
					if (exTrax.getExchangeValue() != null && exTrax.getExchangeValue().getCouponValue() != null)
					{
						exMod.setCouponValue(exTrax.getExchangeValue().getCouponValue());
					}

					if (exTrax.getExchangeValue() != null)
					{
						exMod.setIsWorking(exTrax.getExchangeValue().isIsWorking());
					}
					if (exTrax.getExchangeValue() != null && exTrax.getExchangeValue().getL4categoryCode() != null)
					{
						exMod.setL4categoryCode(exTrax.getExchangeValue().getL4categoryCode());
					}
					exMod.setOrderID(MarketplacecommerceservicesConstants.NOT_APPLICABLE);
					if (exTrax.getPincode() != null)
					{
						exMod.setPincode(exTrax.getPincode());
					}
					if (exTrax.getProductId() != null)
					{
						exMod.setProductId(exTrax.getProductId());
					}
					exMod.setSellerOrderID(MarketplacecommerceservicesConstants.NOT_APPLICABLE);
					exMod.setTransactiondId(MarketplacecommerceservicesConstants.NOT_APPLICABLE);
					if (exTrax.getUssid() != null)
					{
						exMod.setUssid(exTrax.getUssid());
					}
					if (StringUtils.isNotEmpty(reason))
					{
						exMod.setExchangeRemovalReason(reason);
					}
					else
					{
						exMod.setExchangeRemovalReason(MarketplacecommerceservicesConstants.EXCHANGE_REMOVAL_REASON);
					}
					exModList.add(exMod);
					//Added for EQA Comments
					modelService.refresh(exMod);
					exTraxRemovList.add(exTrax);
					//Added for EQA Comments
					modelService.refresh(exTrax);

				}
			}

			if (CollectionUtils.isNotEmpty(exModList))
			{
				modelService.saveAll(exModList);
			}
			if (CollectionUtils.isNotEmpty(exTraxRemovList))
			{
				modelService.removeAll(exTraxRemovList);
			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error(ERROR_OCCURED + WHILE_SAVING + "getExchangeRequestID");
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(ERROR_OCCURED + "getExchangeRequestID");

		}
		return exReqId;
	}

	@Override
	public void removeExchangefromCart(final CartModel cartModel)
	{
		try
		{
			String removeExchangeIdList = null;
			final List<AbstractOrderEntryModel> entryUpdate = new ArrayList();
			for (final AbstractOrderEntryModel entry : cartModel.getEntries())
			{
				if (StringUtils.isEmpty(removeExchangeIdList) && StringUtils.isNotEmpty(entry.getExchangeId()))
				{
					removeExchangeIdList = entry.getExchangeId();
					entry.setExchangeId(MarketplacecommerceservicesConstants.EMPTYSPACE);
					entryUpdate.add(entry);
				}
				else if (StringUtils.isNotEmpty(removeExchangeIdList) && StringUtils.isNotEmpty(entry.getExchangeId()))
				{
					removeExchangeIdList += entry.getExchangeId();
					entry.setExchangeId(MarketplacecommerceservicesConstants.EMPTYSPACE);
					entryUpdate.add(entry);
				}

			}
			if (CollectionUtils.isNotEmpty(entryUpdate))
			{
				modelService.saveAll(entryUpdate);
			}


			removeFromTransactionTable(removeExchangeIdList, null, cartModel);
		}
		catch (final ModelSavingException e)
		{
			LOG.error(ERROR_OCCURED + WHILE_SAVING + "removeExchangefromCart");
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(ERROR_OCCURED + "removeExchangefromCart");

		}

	}

	@Override
	public void changeGuidforCartMerge(final CartModel cart)
	{
		try
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
						exIdStringList = exIdStringList + MarketplacecommerceservicesConstants.COMMA + entry.getExchangeId();
					}

				}
			}
			final List<ExchangeTransactionModel> exTraxList = getTeporaryExchangeModelforId(exIdStringList);
			for (final ExchangeTransactionModel exTrax : exTraxList)
			{
				exTrax.setCartguid(cart.getGuid());
				listToSave.add(exTrax);
			}

			if (CollectionUtils.isNotEmpty(listToSave))
			{
				modelService.saveAll(listToSave);

			}


		}
		catch (final ModelSavingException e)
		{
			LOG.error(ERROR_OCCURED + WHILE_SAVING + "changeGuidforCartMerge");
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(ERROR_OCCURED + "changeGuidforCartMerge");

		}
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