/**
 *
 */
package com.tisl.mpl.promotion.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.model.BuyABFreePrecentageDiscountModel;
import com.tisl.mpl.model.BuyXItemsofproductAgetproductBforfreeModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.promotion.dao.SellerBasedPromotionDao;


/**
 * @author TCS
 *
 */
public class SellerBasedPromotionServiceImpl implements SellerBasedPromotionService
{

	private SellerBasedPromotionDao sellerBasedPromotionDao;

	@Resource(name = "modelService")
	private ModelService modelService;

	/**
	 * @return the sellerBasedPromotionDao
	 */
	public SellerBasedPromotionDao getSellerBasedPromotionDao()
	{
		return sellerBasedPromotionDao;
	}

	/**
	 * @param sellerBasedPromotionDao
	 *           the sellerBasedPromotionDao to set
	 */
	public void setSellerBasedPromotionDao(final SellerBasedPromotionDao sellerBasedPromotionDao)
	{
		this.sellerBasedPromotionDao = sellerBasedPromotionDao;
	}

	/**
	 * @Description : For Fetching the Seller Information Data Corresponding to Seller USSID
	 * @param : code,oModel
	 */
	@Override
	public List<SellerInformationModel> fetchSellerInformation(final String code, final CatalogVersionModel oModel)
	{
		return getSellerBasedPromotionDao().fetchSellerInformation(code, oModel);
	}

	/**
	 * @Description : For Fetching the Price Information Data Corresponding to Seller USSID
	 * @param : code,oModel
	 */
	@Override
	public List<PriceRowModel> fetchPriceInformation(final String code, final CatalogVersionModel oModel)
	{
		return getSellerBasedPromotionDao().fetchPriceInformation(code, oModel);
	}

	/**
	 * @Description : Fetch Promotion Details Corresponding to a code
	 * @param : code
	 */
	@Override
	public List<AbstractPromotionModel> fetchPromotionDetails(final String code)
	{
		return getSellerBasedPromotionDao().fetchPromotionDetails(code);
	}

	/**
	 * Modify the promotion Fired Message
	 *
	 * @param promoCode
	 */
	@Override
	public void modifyFiredMessage(final String promoCode)
	{
		final List<AbstractPromotionModel> promoList = fetchPromotionDetails(promoCode);
		List<AbstractPromotionModel> modifyList = null;
		String message = MarketplacecommerceservicesConstants.EMPTY;
		if (CollectionUtils.isNotEmpty(promoList))
		{
			modifyList = new ArrayList<AbstractPromotionModel>();
			for (final AbstractPromotionModel oModel : promoList)
			{
				if (null != oModel.getEnabled() && oModel.getEnabled().booleanValue())
				{
					message = populateMessage(oModel);
				}
				else
				{
					modifyList.add(oModel);
				}
			}


			if (CollectionUtils.isNotEmpty(modifyList) && StringUtils.isNotEmpty(message))
			{
				modifyMessage(modifyList, message);
			}

		}

	}

	/**
	 * Save the modified Message
	 *
	 * @param modifyList
	 * @param message
	 */
	private void modifyMessage(final List<AbstractPromotionModel> modifyList, final String message)
	{
		final List<BuyABFreePrecentageDiscountModel> promoList = new ArrayList<BuyABFreePrecentageDiscountModel>();
		final List<BuyXItemsofproductAgetproductBforfreeModel> freebiePromoList = new ArrayList<BuyXItemsofproductAgetproductBforfreeModel>();


		for (final AbstractPromotionModel oModel : modifyList)
		{
			if (oModel instanceof BuyABFreePrecentageDiscountModel)
			{
				final BuyABFreePrecentageDiscountModel model = (BuyABFreePrecentageDiscountModel) oModel;
				model.setMessageFired(message, Locale.ENGLISH);
				promoList.add(model);
			}
			else if (oModel instanceof BuyXItemsofproductAgetproductBforfreeModel)
			{
				final BuyXItemsofproductAgetproductBforfreeModel model = (BuyXItemsofproductAgetproductBforfreeModel) oModel;
				model.setMessageFired(message, Locale.ENGLISH);
				freebiePromoList.add(model);
			}
		}


		if (CollectionUtils.isNotEmpty(promoList))
		{
			getModelService().saveAll(promoList);
		}
		else if (CollectionUtils.isNotEmpty(freebiePromoList))
		{
			getModelService().saveAll(freebiePromoList);
		}

	}

	/**
	 * Populate the modified Message
	 *
	 * @param oModel
	 * @return message
	 */
	private String populateMessage(final AbstractPromotionModel oModel)
	{
		String message = MarketplacecommerceservicesConstants.EMPTY;
		if (oModel instanceof BuyABFreePrecentageDiscountModel)
		{
			final BuyABFreePrecentageDiscountModel model = (BuyABFreePrecentageDiscountModel) oModel;
			message = model.getMessageFired();
		}
		else if (oModel instanceof BuyXItemsofproductAgetproductBforfreeModel)
		{
			final BuyXItemsofproductAgetproductBforfreeModel model = (BuyXItemsofproductAgetproductBforfreeModel) oModel;
			message = model.getMessageFired();
		}

		return message;
	}

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

	/**
	 * Fetch promotion details which are active
	 */
	@Override
	public List<AbstractPromotionModel> getPromoDetails()
	{
		return getSellerBasedPromotionDao().getPromoDetails();
	}

}
