/**
 *
 */
package com.tisl.mpl.facade.stw.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
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

	private static final Logger LOG = Logger.getLogger(STWWidgetFacadeImpl.class);//sonar fix
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
	public List<STWJsonRecomendationData> getSTWWidgetFinalData(final Map<String, String[]> stwParamsMap)
	{
		String stwjsonAsString = null;//"{\"status\":0,\"statusMessage\":\"Success\",\"recommendations\":[{\"listingId\":\"987654321\",\"productName\":\"Ambrane P-1111 10000 mAh Power Bank (Black)\",\"categoryL1\":\"Electronics\",\"productBrand\":\"Ambrane\",\"imageUrl\":\"https://assetstmppprd.tataunistore.com/medias/sys_master/images/9234671370270.jpg\",\"availableSize\":null,\"availableColor\":\"Black\",\"mrp\":1799,\"mop\":629,\"productUrl\":\"https://www.tatacliq.com/ambrane-p-1111-10000-mah-power-bank-black/p-mp000000000076452\"},{\"listingId\":\"987654322\",\"productName\":\"Vivo V5 4G Dual Sim 32GB (Gold)\",\"categoryL1\":\"Electronics\",\"productBrand\":\"Vivo\",\"imageUrl\":\"https://assetstmppprd.tataunistore.com/medias/sys_master/images/9234671337502.jpg\",\"availableSize\":null,\"availableColor\":\"Gold\",\"mrp\":18980,\"mop\":16145,\"productUrl\":\"https://www.tatacliq.com/vivo-v5-4g-dual-sim-32gb-gold/p-mp000000000734559\"},{\"listingId\":\"987654323\",\"productName\":\"Vivo V5 4G Dual Sim 32GB (Gold)\",\"categoryL1\":\"Electronics\",\"productBrand\":\"Vivo\",\"imageUrl\":\"//assetstmppprd.tataunistore.com/medias/sys_master/images/9234671304734.jpg\",\"availableSize\":null,\"availableColor\":\"Gold\",\"mrp\":1880,\"mop\":1645,\"productUrl\":\"https://www.tatacliq.com/vivo-v5-4g-dual-sim-32gb-gold/p-mp000000000734559\"},{\"listingId\":\"987654324\",\"productName\":\"Vivo V5 4G Dual Sim 32GB (Gold)\",\"categoryL1\":\"Electronics\",\"productBrand\":\"Vivo\",\"imageUrl\":\"https://assetstmppprd.tataunistore.com/medias/sys_master/images/9234671468574.jpg\",\"availableSize\":null,\"availableColor\":\"Gold\",\"mrp\":1898,\"mop\":1615,\"productUrl\":\"https://www.tatacliq.com/vivo-v5-4g-dual-sim-32gb-gold/p-mp000000000734559\"},{\"listingId\":\"987654321\",\"productName\":\"Ambrane P-1111 10000 mAh Power Bank (Black)\",\"categoryL1\":\"Electronics\",\"productBrand\":\"Ambrane\",\"imageUrl\":\"https://assetstmppprd.tataunistore.com/medias/sys_master/images/9234671370270.jpg\",\"availableSize\":null,\"availableColor\":\"Black\",\"mrp\":1799,\"mop\":629,\"productUrl\":\"https://www.tatacliq.com/ambrane-p-1111-10000-mah-power-bank-black/p-mp000000000076452\"},{\"listingId\":\"987654322\",\"productName\":\"Vivo V5 4G Dual Sim 32GB (Gold)\",\"categoryL1\":\"Electronics\",\"productBrand\":\"Vivo\",\"imageUrl\":\"https://assetstmppprd.tataunistore.com/medias/sys_master/images/9234671337502.jpg\",\"availableSize\":null,\"availableColor\":\"Gold\",\"mrp\":18980,\"mop\":16145,\"productUrl\":\"https://www.tatacliq.com/vivo-v5-4g-dual-sim-32gb-gold/p-mp000000000734559\"},{\"listingId\":\"987654323\",\"productName\":\"Vivo V5 4G Dual Sim 32GB (Gold)\",\"categoryL1\":\"Electronics\",\"productBrand\":\"Vivo\",\"imageUrl\":\"//assetstmppprd.tataunistore.com/medias/sys_master/images/9234671304734.jpg\",\"availableSize\":null,\"availableColor\":\"Gold\",\"mrp\":1880,\"mop\":1645,\"productUrl\":\"https://www.tatacliq.com/vivo-v5-4g-dual-sim-32gb-gold/p-mp000000000734559\"},{\"listingId\":\"987654324\",\"productName\":\"Vivo V5 4G Dual Sim 32GB (Gold)\",\"categoryL1\":\"Electronics\",\"productBrand\":\"Vivo\",\"imageUrl\":\"https://assetstmppprd.tataunistore.com/medias/sys_master/images/9234671468574.jpg\",\"availableSize\":null,\"availableColor\":\"Gold\",\"mrp\":1898,\"mop\":1615,\"productUrl\":\"https://www.tatacliq.com/vivo-v5-4g-dual-sim-32gb-gold/p-mp000000000734559\"}]}";
		List<STWJsonRecomendationData> stwFinalAfterBuyBox = new ArrayList<STWJsonRecomendationData>();
		try
		{
			stwjsonAsString = stwWidgetService.callSTWService(stwParamsMap);
			if (null != stwjsonAsString)
			{
				final ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

				final List<String> productCodes = new ArrayList<String>();
				List<BuyBoxModel> buyBoxModelList = new ArrayList<BuyBoxModel>();

				final STWJsonData sTWJsonData = mapper.readValue(stwjsonAsString, STWJsonData.class);
				final List<STWJsonRecomendationData> stwRecomendationData = sTWJsonData.getRecommendations();
				for (final STWJsonRecomendationData recData : stwRecomendationData)
				{
					if (StringUtils.isNotEmpty(recData.getListingId()))
					{
						productCodes.add(recData.getListingId().toUpperCase());
					}
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
			}
		}
		catch (final IOException | ClassCastException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			LOG.error("getSTWWidgetFinalData Exception occoured  ", e);
		}
		return stwFinalAfterBuyBox;
	}

	/**
	 *
	 * @param stwRecomendationData
	 * @param buyBoxModelList
	 * @throws ArrayIndexOutOfBoundsException
	 * @throws ClassCastException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public List<STWJsonRecomendationData> compareStwDataWithBuyBox(final List<STWJsonRecomendationData> stwRecomendationData,
			final List<BuyBoxModel> buyBoxModelList) throws ArrayIndexOutOfBoundsException, ClassCastException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		final List<STWJsonRecomendationData> finalStwRecomendationData = new ArrayList<STWJsonRecomendationData>();
		STWJsonRecomendationData stwPojo = null;
		final List<String> listingIds = (List<String>) CollectionUtils.collect(stwRecomendationData,
				new BeanToPropertyValueTransformer("listingId"));

		for (final BuyBoxModel buyBoxModel : buyBoxModelList)
		{
			final String buyBoxListingIdLwrCase = buyBoxModel.getProduct().toLowerCase();
			if (listingIds.contains(buyBoxListingIdLwrCase))
			{
				stwPojo = (STWJsonRecomendationData) CollectionUtils.find(stwRecomendationData, new Predicate()
				{
					@Override
					public boolean evaluate(final Object obj)
					{
						final STWJsonRecomendationData finder = (STWJsonRecomendationData) obj;
						return finder.getListingId().equals(buyBoxListingIdLwrCase);
					}
				});
				if (buyBoxModel.getSpecialPrice() != null && Double.compare(buyBoxModel.getSpecialPrice().doubleValue(), 0.0) > 0)
				{
					stwPojo.setMop(buyBoxModel.getSpecialPrice().toString());
				}
				else
				{
					stwPojo.setMop(buyBoxModel.getPrice().toString());
				}
				stwPojo.setMrp(buyBoxModel.getMrp().toString());
				finalStwRecomendationData.add(stwPojo);
			}
		}
		return finalStwRecomendationData;
	}
}
