/**
 *
 */
package com.tisl.mpl.facade.stw.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.facade.stw.STWWidgetFacade;
import com.tisl.mpl.facades.data.STWJsonData;
import com.tisl.mpl.facades.data.STWJsonRecomendationData;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.service.STWWidgetService;



/**
 * @author TCS
 *
 */

public class STWWidgetFacadeImpl implements STWWidgetFacade
{

	Logger LOG = Logger.getLogger(this.getClass().getName());

	private STWWidgetService stwWidgetService;
	private BuyBoxService buyBoxService;

	/**
	 * @return the buyBoxService
	 */
	public BuyBoxService getBuyBoxService()
	{
		return buyBoxService;
	}

	/**
	 * @param buyBoxService
	 *           the buyBoxService to set
	 */
	public void setBuyBoxService(final BuyBoxService buyBoxService)
	{
		this.buyBoxService = buyBoxService;
	}

	/**
	 * @return the stwWidgetService
	 */
	public STWWidgetService getStwWidgetService()
	{
		return stwWidgetService;
	}

	/**
	 * @param stwWidgetService
	 *           the stwWidgetService to set
	 */
	public void setStwWidgetService(final STWWidgetService stwWidgetService)
	{
		this.stwWidgetService = stwWidgetService;
	}

	/**
	 *
	 */
	@Override
	public List<STWJsonRecomendationData> getSTWWidgetFinalData()
	{
		final String stwjsonAsString = "{\"status\":0,\"statusMessage\":\"Success\",\"recommendations\":[{\"listingId\":\"987654321\",\"productName\":\"Ambrane P-1111 10000 mAh Power Bank (Black)\",\"categoryL1\":\"Electronics\",\"productBrand\":\"Ambrane\",\"imageUrl\":\"https://img.tatacliq.com/images/437Wx649H/MP000000000076452_437Wx649H_20160327190811.jpeg\",\"availableSize\":null,\"availableColor\":\"Black\",\"mrp\":1799,\"mop\":629,\"productUrl\":\"https://www.tatacliq.com/ambrane-p-1111-10000-mah-power-bank-black/p-mp000000000076452\"},{\"listingId\":\"987654322\",\"productName\":\"Vivo V5 4G Dual Sim 32GB (Gold)\",\"categoryL1\":\"Electronics\",\"productBrand\":\"Vivo\",\"imageUrl\":\"https://img.tatacliq.com/images/437Wx649H/MP000000000734559_437Wx649H_20161123185445.jpeg\",\"availableSize\":null,\"availableColor\":\"Gold\",\"mrp\":18980,\"mop\":16145,\"productUrl\":\"https://www.tatacliq.com/vivo-v5-4g-dual-sim-32gb-gold/p-mp000000000734559\"}]}";
		//StringBuffer sbs = new StringBuffer();
		//sbs.append('{"status":0,"statusMessage":"Success","recommendations":"');

		//stwWidgetService.callSTWService();
		List<STWJsonRecomendationData> stwFinalAfterBuyBox = null;
		if (null != stwjsonAsString)
		{
			final ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			try
			{
				final List<String> productCodes = new ArrayList<String>();
				List<BuyBoxModel> buyBoxModelList = new ArrayList<BuyBoxModel>();

				final STWJsonData sTWJsonData = mapper.readValue(stwjsonAsString, STWJsonData.class);
				final List<STWJsonRecomendationData> stwRecomendationData = sTWJsonData.getRecommendations();
				for (final STWJsonRecomendationData recData : stwRecomendationData)
				{
					productCodes.add(recData.getListingId());
				}
				if (CollectionUtils.isNotEmpty(productCodes))
				{
					final String commaSepartedProductCodes = StringUtils.join(productCodes, ",");
					buyBoxModelList = buyBoxService.buyboxPrice(commaSepartedProductCodes);
				}

				if (CollectionUtils.isNotEmpty(buyBoxModelList))
				{
					stwFinalAfterBuyBox = this.compareStwDataWithBuyBox(stwRecomendationData, buyBoxModelList);
				}
				System.out.println(sTWJsonData.getRecommendations().size());

			}
			catch (final IOException e)
			{
				LOG.error("getSTWWidgetFinalData unable to map JSON data on ", e);
			}
		}
		return stwFinalAfterBuyBox;


	}

	/**
	 *
	 * @param stwRecomendationData
	 * @param buyBoxModelList
	 * @throws ArrayIndexOutOfBoundsException
	 * @throws ClassCastException
	 */
	public List<STWJsonRecomendationData> compareStwDataWithBuyBox(final List<STWJsonRecomendationData> stwRecomendationData,
			final List<BuyBoxModel> buyBoxModelList) throws ArrayIndexOutOfBoundsException, ClassCastException
	{
		for (final BuyBoxModel buyBoxModel : buyBoxModelList)
		{
			final STWJsonRecomendationData stwPojo = new STWJsonRecomendationData();
			stwPojo.setListingId(buyBoxModel.getProduct());
			final int index = Collections.binarySearch(stwRecomendationData, stwPojo, new StwDataCompatetor());
			if (index < 0)
			{
				stwRecomendationData.remove(index);
			}
			else
			{
				stwRecomendationData.get(index).setMop(buyBoxModel.getSpecialPrice().toString());
				stwRecomendationData.get(index).setMrp(buyBoxModel.getMrp().toString());
			}
		}
		return stwRecomendationData;
	}

	/**
	 *
	 * @author TCS
	 *
	 */
	public class StwDataCompatetor implements Comparator<STWJsonRecomendationData>
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(final STWJsonRecomendationData paramT1, final STWJsonRecomendationData paramT2)
		{
			// YTODO Auto-generated method stub
			return 0;
		}
		/*
		 * @Override public int compare(final STWJsonRecomendationData o1, final STWJsonRecomendationData o2) { //return
		 * o1.getLISTING_ID().compareTo(o2.getLISTING_ID()); }
		 */

	}

}
