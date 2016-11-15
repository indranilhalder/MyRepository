/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.ProductOfferDetailDao;
import com.tisl.mpl.marketplacecommerceservices.service.ProductOfferDetailService;


/**
 * @author TCS
 *
 */

//Added for displaying Non HMC configurable offer messages , TPR-589

public class ProductOfferDetailServiceImpl implements ProductOfferDetailService
{

	@Resource(name = "prodOfferDetDao")
	private ProductOfferDetailDao prodOfferDetDao;
	public static final int MIN_OFFER_LENGTH = 200;

	public static final String OFFER_REGEX = "(^[a-zA-Z0-9][\\$#\\+{}:\\?\\.^*()_+=,%&'!;|<>-~@\"a-zA-Z0-9 ]+$)";

	/**
	 * @Description This method is used to fetch message from OfferDetail for a product
	 * @param productCode
	 * @return message
	 */

	@Override
	public Map<String, Map<String, String>> showOfferMessage(final String productCode)
	{
		final SearchResult<List<Object>> result = prodOfferDetDao.showOfferMessage(productCode);

		final Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String>>();

		if (null != result && CollectionUtils.isNotEmpty(result.getResult()))
		{
			for (final List<Object> row : result.getResult())
			{
				final Map<String, String> offerDetMap = new HashMap<String, String>();
				String sellerIdQry = null;
				String offerMessage = null;
				String offerMessageDet = null;
				String offerStartDate = null;
				String offerEndDate = null;
				if (!row.isEmpty())
				{
					sellerIdQry = (String) row.get(0);
					offerMessage = (String) row.get(1);
					offerMessageDet = (String) row.get(2);
					offerStartDate = (String) row.get(3);
					offerEndDate = (String) row.get(4);
				}
				if (null != sellerIdQry)
				{
					if (StringUtils.isNotEmpty(offerMessage))
					{
						offerDetMap.put(MarketplacecommerceservicesConstants.MESSAGE, offerMessage);
					}
					if (StringUtils.isNotEmpty(offerMessageDet) && offerMessageDet.length() <= MIN_OFFER_LENGTH
							&& validateOffer(offerMessageDet))
					{
						offerDetMap.put(MarketplacecommerceservicesConstants.MESSAGEDET, offerMessageDet);
					}
					if (StringUtils.isNotEmpty(offerStartDate))
					{
						offerDetMap.put(MarketplacecommerceservicesConstants.MESSAGESTARTDATE, offerStartDate);
					}
					if (StringUtils.isNotEmpty(offerEndDate))
					{
						offerDetMap.put(MarketplacecommerceservicesConstants.MESSAGEENDDATE, offerEndDate);
					}
					resultMap.put(sellerIdQry, offerDetMap);
				}
			}
		}
		return resultMap;
	}

	/**
	 * validate the pattern of the message
	 *
	 * @param offerMessageDet
	 * @return boolean
	 */
	public boolean validateOffer(final String offerMessageDet)
	{
		final Pattern pattern = Pattern.compile(OFFER_REGEX);
		final Matcher matcher = pattern.matcher(offerMessageDet);
		return matcher.matches();
	}

	//update the message for Freebie product TPR-1754
	/**
	 * Return Map
	 */
	@Override
	public Map<String, Map<String, String>> showFreebieMessage(final String ussId)
	{
		final SearchResult<List<Object>> result = prodOfferDetDao.showFreebieMessage(ussId);
		final Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String>>();
		if (null != result && CollectionUtils.isNotEmpty(result.getResult()))
		{
			for (final List<Object> row : result.getResult())
			{
				final Map<String, String> freebieDetMap = new HashMap<String, String>();
				String ussIdQry = null;
				String freebieMessage = null;
				String freebieStartDate = null;
				String freebieEndDate = null;

				if (!row.isEmpty())
				{
					ussIdQry = (String) row.get(0);
					freebieMessage = (String) row.get(1);
					freebieStartDate = (String) row.get(2);
					freebieEndDate = (String) row.get(3);
				}
				if (null != ussIdQry)
				{
					if (StringUtils.isNotEmpty(freebieMessage))
					{
						freebieDetMap.put(MarketplacecommerceservicesConstants.FREEBIEMSG, freebieMessage);
					}

					if (StringUtils.isNotEmpty(freebieStartDate))
					{
						freebieDetMap.put(MarketplacecommerceservicesConstants.MESSAGESTARTDATE, freebieStartDate);
					}
					if (StringUtils.isNotEmpty(freebieEndDate))
					{
						freebieDetMap.put(MarketplacecommerceservicesConstants.MESSAGEENDDATE, freebieEndDate);
					}

					resultMap.put(ussIdQry, freebieDetMap);
				}
			}
		}
		return resultMap;
	}
}
