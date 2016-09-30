/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.BulkPromotionCreationDao;
import com.tisl.mpl.model.EtailExcludeSellerSpecificRestrictionModel;
import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author TCS
 *
 */
public class ExSellerBulkLoadInterceptor implements PrepareInterceptor
{
	private static final Logger LOG = Logger.getLogger(ExSellerBulkLoadInterceptor.class);

	@Autowired
	private BulkPromotionCreationDao bulkPromotionCreationDao;

	/**
	 * For Bulk Upload of Seller Data in Seller Restriction
	 *
	 * @param arg0
	 * @param arg1
	 */
	@Override
	public void onPrepare(final Object arg0, final InterceptorContext arg1) throws InterceptorException
	{

		LOG.debug("Inside SellerBulkLoadInterceptor");

		if (arg0 instanceof EtailExcludeSellerSpecificRestrictionModel)
		{
			final EtailExcludeSellerSpecificRestrictionModel oModel = (EtailExcludeSellerSpecificRestrictionModel) arg0;

			if (StringUtils.isNotEmpty(oModel.getExSellerBulkList()))
			{
				final String sellerCodes = oModel.getExSellerBulkList();
				final List<SellerMasterModel> sellerlList = new ArrayList<SellerMasterModel>();

				final StringTokenizer sellerCodeTokens = new StringTokenizer(sellerCodes,
						MarketplacecommerceservicesConstants.PROMO_PRODUCT_UPLOAD_SEPARATOR);

				while (sellerCodeTokens.hasMoreTokens())
				{
					sellerlList.addAll(bulkPromotionCreationDao.fetchSellerMasterInfo(sellerCodeTokens.nextToken().trim()));
				}

				LOG.debug("Checking for Existing Data ");

				final Collection<SellerMasterModel> existingProductList = oModel.getSellerMasterList();
				final List<SellerMasterModel> finalSellerlList = new ArrayList<SellerMasterModel>();
				final Set<SellerMasterModel> sellerModelSet = new HashSet<SellerMasterModel>();

				if (CollectionUtils.isNotEmpty(existingProductList))
				{
					finalSellerlList.addAll(existingProductList);
				}

				finalSellerlList.addAll(sellerlList);
				sellerModelSet.addAll(finalSellerlList);

				finalSellerlList.clear();
				finalSellerlList.addAll(sellerModelSet);

				LOG.debug("Adding Data to Model");


				oModel.setSellerMasterList(finalSellerlList);
				oModel.setExSellerBulkList(MarketplacecommerceservicesConstants.EMPTY);
			}
		}


	}

}
