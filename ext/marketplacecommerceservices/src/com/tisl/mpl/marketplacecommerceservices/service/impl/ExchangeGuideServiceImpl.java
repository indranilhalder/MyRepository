/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
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

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.ExchangeCouponValueModel;
import com.tisl.mpl.core.model.ExchangeModel;
import com.tisl.mpl.core.model.ExchangeTransactionModel;
import com.tisl.mpl.core.model.SizeGuideModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
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

	/**
	 * @description It will take the customer id from the customer model
	 * @param productCode
	 * @return List<SizeGuideModel>
	 *
	 */
	public List<SizeGuideModel> getProductSizeGuideList(final String productCode) throws CMSItemNotFoundException
	{
		List<SizeGuideModel> sizeModels = null;
		try
		{
			final ProductModel productModel = productService.getProductForCode(productCode);
			final String sizeGuideCode = sizeChartCode(productModel);
			LOG.info("**********product code: " + productModel.getCode() + " sizeGuideCode:" + sizeGuideCode);

			sizeModels = sizeGuideDao.getsizeGuideByCode(sizeGuideCode);
			//sizeModels = new ArrayList(productModel.getSizeGuide());
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0018);//MarketplacecommerceservicesConstants.E0000);
		}
		return sizeModels;
	}

	private String sizeChartCode(final ProductModel product)
	{
		String sizeGuideCode = null;
		//get size chart feature
		final FeatureList featureList = getClassificationService().getFeatures(product);
		try
		{
			for (final Feature feature : featureList)
			{
				final String featureName = feature.getName().replaceAll("\\s+", "");

				final String sizeChart = configurationService.getConfiguration().getString("product.sizechart.value")
						.replaceAll("\\s+", "");
				if (featureName.equalsIgnoreCase(sizeChart))
				{
					if (null != feature.getValue())
					{
						final FeatureValue sizeGuidefeatureVal = feature.getValue();
						sizeGuideCode = String.valueOf(sizeGuidefeatureVal.getValue());
						break;
					}
				}
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return sizeGuideCode;
	}





	/**
	 * @return the classificationService
	 */
	public ClassificationService getClassificationService()
	{
		return classificationService;
	}





	/**
	 * @param classificationService
	 *           the classificationService to set
	 */
	public void setClassificationService(final ClassificationService classificationService)
	{
		this.classificationService = classificationService;
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
		final ExchangeTransactionModel ex = getTeporaryExchangeModelforId(exchangeId);

		ex.setPincode(pincode);

		modelService.save(ex);
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
	public boolean removeFromTransactionTable(final String exchangeId)
	{
		final boolean isSaved = false;
		final ExchangeTransactionModel ex = getTeporaryExchangeModelforId(exchangeId);
		modelService.remove(ex);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService#getTeporaryExchangeModelforId(java.lang.
	 * String)
	 */
	@Override
	public ExchangeTransactionModel getTeporaryExchangeModelforId(final String exId)
	{
		// YTODO Auto-generated method stub
		return exchangeGuideDao.getTeporaryExchangeModelforId(exId);
	}




	/**
	 *
	 * @return String ExchangeId
	 * @description Saves Exchange Transaction and returns Exchange Id before Add to Cart
	 */

	@Override
	public String getExchangeRequestID(final List<OrderModel> childOrders)
	{
		final List<ExchangeModel> exModList = new ArrayList<>();
		final List<OrderModel> childModfList = new ArrayList();
		final List<ExchangeTransactionModel> exTraxRemovList = new ArrayList();
		String exReqId = "";
		for (final OrderModel child : childOrders)
		{
			boolean changeInChild = false;
			final List<AbstractOrderEntryModel> entryDetails = child.getEntries();
			for (final AbstractOrderEntryModel entry : entryDetails)
			{
				if (entry != null && StringUtils.isNotEmpty(entry.getExchangeTempId()))
				{
					final ExchangeTransactionModel exTrax = getTeporaryExchangeModelforId(entry.getExchangeTempId());
					if (exTrax != null)
					{
						final ExchangeModel exMod = new ExchangeModel();
						exReqId = getEXCHANGEREQUESTID().generate().toString();
						exMod.setBrandName(exTrax.getBrandName());
						exMod.setExchangeRequestId(exReqId);
						exMod.setExchangeValue(exTrax.getExchangeValue());
						exMod.setOrderID(child.getParentReference().getCode());
						exMod.setPincode(exTrax.getPincode());
						exMod.setProductId(exTrax.getProductId());
						exMod.setSellerOrderID(child.getCode());
						exMod.setTransactiondId(entry.getTransactionID());
						exMod.setUssid(exTrax.getUssid());
						exModList.add(exMod);
						exTraxRemovList.add(exTrax);
						entry.setExchangeTempId(exReqId);
						changeInChild = true;

					}
				}
			}
			if (changeInChild)
			{
				childModfList.add(child);
			}
		}

		modelService.saveAll(exModList);
		modelService.saveAll(childModfList);
		modelService.removeAll(exTraxRemovList);

		return exReqId;
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