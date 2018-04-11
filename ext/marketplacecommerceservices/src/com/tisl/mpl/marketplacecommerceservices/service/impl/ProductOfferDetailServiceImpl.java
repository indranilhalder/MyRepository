/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.FreebieDetailModel;
import com.tisl.mpl.core.model.ProductFreebieDetailModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.ProductOfferDetailDao;
import com.tisl.mpl.marketplacecommerceservices.service.ProductOfferDetailService;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;


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
	public Map<String, Map<String, String>> showOfferMessage(final String productCode, final Boolean isPwa)
	{
		SearchResult<List<Object>> result = null;
		final Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String>>();

		if (null != isPwa && isPwa.booleanValue())
		{
			result = prodOfferDetDao.showOfferMessage(productCode, isPwa);

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
					//no cost emi
					String offerStartDate1 = null;
					String offerEndDate1 = null;
					Boolean isNoCostEmi = null;
					if (!row.isEmpty())
					{
						sellerIdQry = (String) row.get(0);
						offerMessage = (String) row.get(1);
						offerMessageDet = (String) row.get(2);
						offerStartDate = (String) row.get(3);
						offerEndDate = (String) row.get(4);
						//no cost emi
						offerStartDate1 = (String) row.get(5);
						offerEndDate1 = (String) row.get(6);
						isNoCostEmi = (Boolean) row.get(7);
					}
					if (null != sellerIdQry)
					{
						if (StringUtils.isNotEmpty(offerMessage) && offerMessage.length() <= MIN_OFFER_LENGTH)
						{
							offerDetMap.put(MarketplacecommerceservicesConstants.MESSAGE, offerMessage);
						}

						// validateOffer method call for messageDet removed for INC_11105
						if (StringUtils.isNotEmpty(offerMessageDet) && offerMessageDet.length() <= MIN_OFFER_LENGTH)

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
						//no cost emi
						if (StringUtils.isNotEmpty(offerStartDate1))
						{
							offerDetMap.put(MarketplacecommerceservicesConstants.OFFERSTARTDATE, offerStartDate1);
						}
						if (StringUtils.isNotEmpty(offerEndDate1))
						{
							offerDetMap.put(MarketplacecommerceservicesConstants.OFFERENDDATE, offerEndDate1);
						}
						if (null != isNoCostEmi)
						{
							if (isNoCostEmi.booleanValue())
							{
								offerDetMap.put(MarketplacecommerceservicesConstants.ISNOCOSTEMI,
										MarketplacecommerceservicesConstants.TRUE.toLowerCase());
							}
							else
							{
								offerDetMap.put(MarketplacecommerceservicesConstants.ISNOCOSTEMI,
										MarketplacecommerceservicesConstants.FALSE.toLowerCase());
							}
						}
						resultMap.put(sellerIdQry, offerDetMap);
					}
				}
			}
		}
		else
		{
			result = prodOfferDetDao.showOfferMessage(productCode, null);

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
						if (StringUtils.isNotEmpty(offerMessage) && offerMessage.length() <= MIN_OFFER_LENGTH)
						{
							offerDetMap.put(MarketplacecommerceservicesConstants.MESSAGE, offerMessage);
						}

						// validateOffer method call for messageDet removed for INC_11105
						if (StringUtils.isNotEmpty(offerMessageDet) && offerMessageDet.length() <= MIN_OFFER_LENGTH)

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
	 * @Description Added for displaying freebie messages other than default freebie message
	 * @param ussId
	 * @return freebie message
	 */
	@Override
	public Map<String, String> showFreebieMessage(final String ussId) throws EtailNonBusinessExceptions, FlexibleSearchException,
			UnknownIdentifierException
	{
		final SearchResult<List<Object>> result = prodOfferDetDao.showFreebieMessage(ussId);
		final Map<String, String> resultMap = new HashMap<String, String>();

		try
		{
			if (null != result && CollectionUtils.isNotEmpty(result.getResult()))
			{
				for (final List<Object> row : result.getResult())
				{
					final ProductFreebieDetailModel prddetails = (ProductFreebieDetailModel) row.get(0);
					if (null != prddetails && new Date().after(prddetails.getStartDate())
							&& new Date().before(prddetails.getEndDate()))
					{
						final FreebieDetailModel freebiedet = (FreebieDetailModel) row.get(1);

						if (null != freebiedet && null != freebiedet.getFreebieId() && null != prddetails.getOffer()
								&& null != prddetails.getOffer().getFreebieId()
								&& prddetails.getOffer().getFreebieId().equals(freebiedet.getFreebieId()))
						{
							resultMap.put(prddetails.getUssId(), freebiedet.getFreebieMsg());
						}

					}
				}
			}
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return resultMap;
	}
}
