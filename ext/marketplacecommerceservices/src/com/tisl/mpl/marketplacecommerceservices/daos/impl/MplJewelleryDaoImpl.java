/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.JewellerySellerDetailsModel;
import de.hybris.platform.core.model.JwlryRevSealInfoModel;
import de.hybris.platform.core.model.PancardInformationModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplJewelleryDao;


/**
 * @author TCS
 *
 */
public class MplJewelleryDaoImpl implements MplJewelleryDao
{

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplJewelleryDao#getJewelleryUssid(java.lang.String)
	 */
	@Override
	public List<JewelleryInformationModel> getJewelleryUssid(final String productCode)
	{
		try
		{
			final String classAttrquery = "select {pk} from {JewelleryInformation} " + "where {productCode}" + "	IN ('"
					+ productCode + "')";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(classAttrquery);
			return flexibleSearchService.<JewelleryInformationModel> search(query).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplJewelleryDao#getJewelleryInfoByUssid(java.lang.String)
	 */
	@Override
	public List<JewelleryInformationModel> getJewelleryInfoByUssid(final String ussid)
	{
		try
		{
			final String classAttrquery = "select {pk} from {JewelleryInformation} " + "where {ussid} " + "		IN ('" + ussid + "')";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(classAttrquery);
			return flexibleSearchService.<JewelleryInformationModel> search(query).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplJewelleryDao#getWeightVarientUssid(java.lang.String)
	 */
	@Override
	public List<String> getWeightVarientUssid(final String ussid)
	{
		// YTODO Auto-generated method stub
		try
		{
			final String classAttrquery = "select distinct{ussid} from {JewelleryInformation} where {pcmussid} IN({{select distinct{pcmussid} from {JewelleryInformation} where {ussid} = ?mainUssid}})";
			//	final FlexibleSearchQuery weightVariantQuery = new FlexibleSearchQuery(classAttrquery.toString());//SONAR FIX JEWELLERY
			final FlexibleSearchQuery weightVariantQuery = new FlexibleSearchQuery(classAttrquery);
			final List resultClassList = new ArrayList();
			resultClassList.add(String.class);
			weightVariantQuery.setResultClassList(resultClassList);
			weightVariantQuery.addQueryParameter("mainUssid", ussid);

			return flexibleSearchService.<String> search(weightVariantQuery).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}

	@Override
	public List<BuyBoxModel> getAllWeightVariant(final String ussid)
	{
		// YTODO Auto-generated method stub
		try
		{
			final String classAttrquery = "select {pk} from {Buybox as bb} where {bb.PUSSID} IN({{select distinct {b.PUSSID} from {Buybox as b} where {b.sellerArticleSKU}= ?mainUssid}})";
			final FlexibleSearchQuery weightVariantQuery = new FlexibleSearchQuery(classAttrquery);
			//	final List resultClassList = new ArrayList();
			//	resultClassList.add(String.class);
			//		weightVariantQuery.setResultClassList(resultClassList);
			weightVariantQuery.addQueryParameter("mainUssid", ussid);

			return flexibleSearchService.<BuyBoxModel> search(weightVariantQuery).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplJewelleryDao#getPanCardStatus(java.lang.String)
	 */
	//CKD:TPR-3809
	@Override
	public String getPanCardStatus(final String orderLineId)
	{
		List<PancardInformationModel> panCardInfo = null;
		String status = null;
		final String query = "select {pan.pk} from {PancardInformation as pan} where {pan.transactionid}=" + orderLineId;

		try
		{
			//	panCardInfo = flexibleSearchService.<PancardInformationModel> search(query.toString()).getResult();//SONAR FIX JEWELLERY
			panCardInfo = flexibleSearchService.<PancardInformationModel> search(query).getResult();
			if (CollectionUtils.isNotEmpty(panCardInfo))
			{
				status = panCardInfo.get(0).getStatus();
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
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplJewelleryDao#getSellerMsgForRetRefTab(java.lang.String)
	 */
	@Override
	public List<JewellerySellerDetailsModel> getSellerMsgForRetRefTab(final String sellerId)
	{
		// YTODO Auto-generated method stub
		try
		{
			final String query = "select pk from {" + JewellerySellerDetailsModel._TYPECODE + "} where {"
					+ JewellerySellerDetailsModel.SELLERID + "} =?sellerId order by {" + JewellerySellerDetailsModel.INDEX + "}";
			final FlexibleSearchQuery retRefQuery = new FlexibleSearchQuery(query);
			retRefQuery.addQueryParameter("sellerId", sellerId);
			return flexibleSearchService.<JewellerySellerDetailsModel> search(retRefQuery).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplJewelleryDao#getSealInfo(java.lang.String)
	 */
	@Override
	public List<JwlryRevSealInfoModel> getSealInfo(final String sellerId)
	{
		try
		{
			final String query = "select pk from {" + JwlryRevSealInfoModel._TYPECODE + "} where {" + JwlryRevSealInfoModel.SELLERID
					+ "} =?sellerId";
			final FlexibleSearchQuery sealInfoQuery = new FlexibleSearchQuery(query);
			sealInfoQuery.addQueryParameter("sellerId", sellerId);
			return flexibleSearchService.<JwlryRevSealInfoModel> search(sealInfoQuery).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplJewelleryDao#getAllWeightVariantByPussid(java.lang.String)
	 */
	@Override
	public List<BuyBoxModel> getAllWeightVariantByPussid(final String pUssid)
	{
		// YTODO Auto-generated method stub
		try
		{
			final String classAttrquery = "select {pk} from {Buybox as bb} where {bb.PUSSID} = ?pUssid";
			final FlexibleSearchQuery weightVariantQuery = new FlexibleSearchQuery(classAttrquery);
			//	final List resultClassList = new ArrayList();
			//	resultClassList.add(String.class);
			//		weightVariantQuery.setResultClassList(resultClassList);
			weightVariantQuery.addQueryParameter("pUssid", pUssid);

			return flexibleSearchService.<BuyBoxModel> search(weightVariantQuery).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}
}
