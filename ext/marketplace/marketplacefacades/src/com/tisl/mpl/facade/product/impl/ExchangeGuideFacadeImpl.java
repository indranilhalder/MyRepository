/**
 *
 */
package com.tisl.mpl.facade.product.impl;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.ExchangeCouponValueModel;
import com.tisl.mpl.core.model.ExchangeTransactionModel;
import com.tisl.mpl.core.model.SizeGuideModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.comparator.SizeGuideComparator;
import com.tisl.mpl.facade.product.ExchangeGuideFacade;
import com.tisl.mpl.facades.product.data.ExchangeGuideData;
import com.tisl.mpl.facades.product.data.SizeGuideData;
import com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService;
import com.tisl.mpl.marketplacecommerceservices.service.SizeGuideService;


/**
 * @author TCS
 *
 */
public class ExchangeGuideFacadeImpl implements ExchangeGuideFacade
{
	/**
	 * @return the exchangeGuideService
	 */
	public ExchangeGuideService getExchangeGuideService()
	{
		return exchangeGuideService;
	}

	/**
	 * @param exchangeGuideService
	 *           the exchangeGuideService to set
	 */
	public void setExchangeGuideService(final ExchangeGuideService exchangeGuideService)
	{
		this.exchangeGuideService = exchangeGuideService;
	}

	/**
	 * @return the exchangeGuideConverter
	 */
	public Converter<ExchangeCouponValueModel, ExchangeGuideData> getExchangeGuideConverter()
	{
		return exchangeGuideConverter;
	}

	/**
	 * @param exchangeGuideConverter
	 *           the exchangeGuideConverter to set
	 */
	public void setExchangeGuideConverter(final Converter<ExchangeCouponValueModel, ExchangeGuideData> exchangeGuideConverter)
	{
		this.exchangeGuideConverter = exchangeGuideConverter;
	}

	/**
	 *
	 */
	private static final String FOOTWEAR = "Footwear";

	/**
	 *
	 */
	private static final String CLOTHING = "Clothing";

	/**
	 * Added FashionAccessories START ::::
	 *
	 */
	private static final String ACCESSORIES = "Accessories";

	@Resource
	private SizeGuideService sizeGuideService;

	private ExchangeGuideService exchangeGuideService;

	@Resource
	private Converter<SizeGuideModel, SizeGuideData> sizeGuideConverter;

	@Resource
	private Converter<ExchangeCouponValueModel, ExchangeGuideData> exchangeGuideConverter;

	@Resource(name = "sizeGuideComparator")
	private SizeGuideComparator sizeGuideComparator;

	/* SONAR FIX JEWELLERY */
	//	@Resource(name = "productService")
	//	private ProductService productService;
	// SONAR FIX JEWELLERY
	//	@Resource(name = "accProductFacade")
	//	private ProductFacade productFacade;


	/*
	 * @Javadoc
	 * 
	 * @returns All L4 for which Exchange is Applicable
	 * 
	 * @see com.tisl.mpl.facade.product.ExchangeGuideFacade#getDistinctL4()
	 */
	@Override
	public boolean isExchangable(final String categoryCode)

	{
		return exchangeGuideService.isExchangable(categoryCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.product.ExchangeGuideFacade#getExchangeGuide(java.lang.String, java.lang.String)
	 */
	@Override
	public List<ExchangeGuideData> getExchangeGuide(final String categoryCode) throws CMSItemNotFoundException
	{
		final List<ExchangeCouponValueModel> exchangeGuideModels = exchangeGuideService.getExchangeGuideList(categoryCode);

		final List<ExchangeGuideData> exchangeGuideData = new ArrayList<>();
		for (final ExchangeCouponValueModel exchangeGuideModel : exchangeGuideModels)
		{
			exchangeGuideData.add(exchangeGuideConverter.convert(exchangeGuideModel));
		}
		//changed to null = sizeGuideService.getProductSizeGuideList(productCode);
		return exchangeGuideData;
	}

	@Override
	public String getTemporaryExchangeId(final String exchangeParam, final String cartguid, final String productCode,
			final String ussid)
	{
		final String paramList[] = exchangeParam.split(Pattern.quote("|"));
		if (paramList.length == 5 && StringUtils.isNotEmpty(cartguid))
		{
			return exchangeGuideService.getExchangeID(paramList[0], paramList[1], paramList[2], paramList[3], paramList[4],
					cartguid, productCode, ussid);
		}

		return "";
	}

	/**
	 * @description It is used for fetching all distinct Size Guides of an online product
	 * @param productCode
	 * @return list of SizeGuideData
	 */
	public Map<String, List<SizeGuideData>> getProductSizeguide(final String productCode, final String categoryType)
			throws CMSItemNotFoundException
	{
		List<SizeGuideModel> sizeGuideModels = null; //changed to null
		List<SizeGuideData> sizeDataValues = null;
		SizeGuideData sizeGuideData = null;
		final TreeMap<String, List<SizeGuideData>> sizeGuideDatas = new TreeMap<String, List<SizeGuideData>>();
		final TreeMap<String, List<SizeGuideData>> sizeGuideSortedDatas = new TreeMap<String, List<SizeGuideData>>();
		final List<SizeGuideData> sizeGuideDataListForFootwear = new ArrayList<SizeGuideData>();
		try
		{
			sizeGuideModels = sizeGuideService.getProductSizeGuideList(productCode);
			if (sizeGuideModels != null)
			{
				for (final SizeGuideModel sizeGuideModel : sizeGuideModels)
				{
					//convertor is added
					sizeGuideData = getSizeGuideConverter().convert(sizeGuideModel);
					if (categoryType.equalsIgnoreCase(CLOTHING))
					{
						addToMap(sizeGuideDatas, sizeGuideModel.getDimension(), sizeGuideData);
					}
					else if (categoryType.equalsIgnoreCase(FOOTWEAR) || categoryType.equalsIgnoreCase(ACCESSORIES))
					{
						sizeGuideDataListForFootwear.add(sizeGuideData);
					}
				}
			}
			/* sorting the Size guide map based on dimension */
			if (categoryType.equalsIgnoreCase(CLOTHING))
			{

				for (final String key : sizeGuideDatas.keySet())
				{
					sizeDataValues = sizeGuideDatas.get(key);
					Collections.sort(sizeDataValues, sizeGuideComparator);
					sizeGuideSortedDatas.put(key, sizeDataValues);
				}
			}
			else if (categoryType.equalsIgnoreCase(FOOTWEAR) || categoryType.equalsIgnoreCase(ACCESSORIES))
			{
				Collections.sort(sizeGuideDataListForFootwear, sizeGuideComparator);
				sizeGuideSortedDatas.put(productCode, sizeGuideDataListForFootwear);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return sizeGuideSortedDatas;
	}

	/**
	 * @description Duplicate Keys if inserted than list of inserted Item will be added as List with same key within Map
	 * @param map
	 *           Original map where keys to be added and returned
	 * @param key
	 *           key to be inserted
	 * @param value
	 *           Value to be inserted
	 */
	private void addToMap(final Map<String, List<SizeGuideData>> map, final String key, final SizeGuideData value)
	{
		if (!map.containsKey(key))
		{
			map.put(key, new ArrayList<SizeGuideData>());
		}
		map.get(key).add(value);
	}

	/**
	 * @return the sizeGuideService
	 */
	public SizeGuideService getSizeGuideService()
	{
		return sizeGuideService;
	}

	/**
	 * @param sizeGuideService
	 *           the sizeGuideService to set
	 */
	public void setSizeGuideService(final SizeGuideService sizeGuideService)
	{
		this.sizeGuideService = sizeGuideService;
	}

	/**
	 * @return the sizeGuideConverter
	 */
	public Converter<SizeGuideModel, SizeGuideData> getSizeGuideConverter()
	{
		return sizeGuideConverter;
	}

	/**
	 * @param sizeGuideConverter
	 *           the sizeGuideConverter to set
	 */
	public void setSizeGuideConverter(final Converter<SizeGuideModel, SizeGuideData> sizeGuideConverter)
	{
		this.sizeGuideConverter = sizeGuideConverter;
	}

	/**
	 * @return the sizeGuideComparator
	 */
	public SizeGuideComparator getSizeGuideComparator()
	{
		return sizeGuideComparator;
	}

	/**
	 * @param sizeGuideComparator
	 *           the sizeGuideComparator to set
	 */
	public void setSizeGuideComparator(final SizeGuideComparator sizeGuideComparator)
	{
		this.sizeGuideComparator = sizeGuideComparator;
	}



	@Override
	public boolean isBackwardServiceble(final String pincode)

	{
		return exchangeGuideService.isBackwardServiceble(pincode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.product.ExchangeGuideFacade#changePincode(java.lang.String)
	 */
	@Override
	public boolean changePincode(final String pincode, final String exchangeId)
	{
		return exchangeGuideService.changePincode(pincode, exchangeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.product.ExchangeGuideFacade#removeFromTransactionTable(java.lang.String)
	 */
	@Override
	public boolean removeFromTransactionTable(final String exchangeId, final String reason, final CartModel cart)
	{
		return exchangeGuideService.removeFromTransactionTable(exchangeId, reason, cart);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.product.ExchangeGuideFacade#getTeporaryExchangeModelforId(com.tisl.mpl.core.model.
	 * ExchangeTransactionModel)
	 */
	@Override
	public List<ExchangeTransactionModel> getTeporaryExchangeModelforId(final String exId)
	{

		return exchangeGuideService.getTeporaryExchangeModelforId(exId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.facade.product.ExchangeGuideFacade#addToExchangeTable(com.tisl.mpl.core.model.ExchangeTransactionModel
	 * )
	 */
	@Override
	public boolean addToExchangeTable(final ExchangeTransactionModel ex)
	{
		// YTODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.product.ExchangeGuideFacade#getExchangeRequestID(java.util.List)
	 */
	@Override
	public String getExchangeRequestID(final OrderModel orders)
	{
		return exchangeGuideService.getExchangeRequestID(orders);
	}

	@Override
	public void removeExchangefromCart(final CartModel cart)
	{
		exchangeGuideService.removeExchangefromCart(cart);
	}
}