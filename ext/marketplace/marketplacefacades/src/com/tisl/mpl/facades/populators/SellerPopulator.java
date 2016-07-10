/**
 * @author TCS
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.core.enums.PaymentModesEnum;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.enums.SellerAssociationStatusEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPriceRowService;
import com.tisl.mpl.model.SellerInformationModel;



/**
 *
 * @author TCS
 *
 * @param <SOURCE>
 * @param <TARGET>
 */
public class SellerPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		AbstractProductPopulator<SOURCE, TARGET>
{

	protected static final Logger LOG = Logger.getLogger(SellerPopulator.class);


	@Resource
	private MplPriceRowService mplPriceRowService;

	@Resource
	private ConfigurationService configurationService;

	private PriceDataFactory priceDataFactory;

	@Resource(name = "mplDeliveryCostService")
	private MplDeliveryCostService mplDeliveryCostService;

	@Resource(name = "productDetailsHelper")
	private ProductDetailsHelper productDetailsHelper;

	/**
	 * @return the mplPriceRowService
	 */
	public MplPriceRowService getMplPriceRowService()
	{
		return mplPriceRowService;
	}

	/**
	 * @param mplPriceRowService
	 *           the mplPriceRowService to set
	 */
	public void setMplPriceRowService(final MplPriceRowService mplPriceRowService)
	{
		this.mplPriceRowService = mplPriceRowService;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the priceDataFactory
	 */
	public PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	/**
	 * @param priceDataFactory
	 *           the priceDataFactory to set
	 */
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	/**
	 * @return the mplDeliveryCostService
	 */
	public MplDeliveryCostService getMplDeliveryCostService()
	{
		return mplDeliveryCostService;
	}

	/**
	 * @param mplDeliveryCostService
	 *           the mplDeliveryCostService to set
	 */
	public void setMplDeliveryCostService(final MplDeliveryCostService mplDeliveryCostService)
	{
		this.mplDeliveryCostService = mplDeliveryCostService;
	}

	/**
	 * @return the productDetailsHelper
	 */
	public ProductDetailsHelper getProductDetailsHelper()
	{
		return productDetailsHelper;
	}

	/**
	 * @param productDetailsHelper
	 *           the productDetailsHelper to set
	 */
	public void setProductDetailsHelper(final ProductDetailsHelper productDetailsHelper)
	{
		this.productDetailsHelper = productDetailsHelper;
	}


	/**
	 * @description method is to populate basic product details in pdp
	 * @param productModel
	 * @param productData
	 * @throws ConversionException
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException,
			EtailNonBusinessExceptions
	{
		if (null != productModel.getSellerInformationRelator())
		{
			final List<SellerInformationModel> sellerList = new ArrayList<SellerInformationModel>(
					productModel.getSellerInformationRelator());

			final List<SellerInformationData> sellerDataList = new ArrayList<SellerInformationData>();
			for (final SellerInformationModel sellerInformationModel : sellerList)
			{
				if ((sellerInformationModel.getSellerAssociationStatus() == null || sellerInformationModel
						.getSellerAssociationStatus().equals(SellerAssociationStatusEnum.YES))
						&& (null != sellerInformationModel.getStartDate() && new Date().after(sellerInformationModel.getStartDate())
								&& null != sellerInformationModel.getEndDate() && new Date().before(sellerInformationModel.getEndDate())))
				{
					final SellerInformationData sellerData = new SellerInformationData();
					sellerData.setUssid(sellerInformationModel.getSellerArticleSKU());
					sellerData.setSellerAssociationstatus(SellerAssociationStatusEnum.YES.toString());
					for (final RichAttributeModel rm : sellerInformationModel.getRichAttribute())
					{

						sellerData.setDeliveryModes(productDetailsHelper.getDeliveryModeLlist(rm,
								sellerInformationModel.getSellerArticleSKU()));

						if ((null != rm.getPaymentModes() && rm.getPaymentModes().equals(PaymentModesEnum.BOTH) || (null != rm
								.getPaymentModes() && rm.getPaymentModes().equals(PaymentModesEnum.COD))))
						{
							sellerData.setIsCod(MarketplaceFacadesConstants.Y);
						}
						else
						{
							sellerData.setIsCod(MarketplaceFacadesConstants.N);
						}
						/*
						 * if (rm.getDeliveryFulfillModes().equals(DeliveryFulfillModesEnum.TSHIP)) {
						 * sellerData.setFullfillment(configurationService.getConfiguration().getString("tship.sellerName"));
						 * } else { sellerData.setFullfillment(sellerInformationModel.getSellerName()); }
						 */
						if (null != rm.getDeliveryFulfillModes())
						{
							sellerData.setFullfillment(rm.getDeliveryFulfillModes().getCode());
						}
						sellerData.setSellername(sellerInformationModel.getSellerName());

						if (null != rm.getShippingModes())
						{
							sellerData.setShippingMode(rm.getShippingModes().getCode());
						}

						if (null != rm.getReturnWindow())
						{
							sellerData.setReturnPolicy(rm.getReturnWindow());
						}
						if (null != rm.getReplacementWindow())
						{
							sellerData.setReplacement(rm.getReplacementWindow());
						}
						if (null != rm.getCancellationWindow())
						{
							sellerData.setCancelPolicy(rm.getCancellationWindow());
						}
						if (null != rm.getExchangeAllowedWindow())
						{
							sellerData.setExchangePolicy(rm.getExchangeAllowedWindow());
						}
					}

					sellerData.setSellerID(sellerInformationModel.getSellerID());
					sellerDataList.add(sellerData);
				}
			}
			productData.setSeller(sellerDataList);
		}



	}

	protected boolean isApproved(final SOURCE productModel)
	{
		final ArticleApprovalStatus approvalStatus = productModel.getApprovalStatus();
		return ArticleApprovalStatus.APPROVED.equals(approvalStatus);
	}

}
