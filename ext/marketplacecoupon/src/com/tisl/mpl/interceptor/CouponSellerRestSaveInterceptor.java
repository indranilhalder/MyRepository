/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerMasterService;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.model.SellerRestrictionModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class CouponSellerRestSaveInterceptor implements PrepareInterceptor
{

	private static final String BLANK = "";
	@Resource(name = "mplSellerMaster")
	private MplSellerMasterService mplSellerMasterService;
	private static final Logger LOG = Logger.getLogger(CouponSellerRestSaveInterceptor.class);

	/**
	 * @Description : This Method is evaluated when seller restriction is created(changes incorporated for TPR-715)
	 * @param: sellerModel
	 * @param: paramInterceptorContext
	 */
	@Override
	public void onPrepare(final Object rModel, final InterceptorContext arg1) throws InterceptorException
	{
		try
		{
			if (rModel instanceof SellerRestrictionModel)
			{
				LOG.debug("seller restriction interceptor is getting invoked.........");
				final SellerRestrictionModel sellerModel = (SellerRestrictionModel) rModel;
				if (StringUtils.isNotEmpty(sellerModel.getSellerCodeList()))
				{
					final String sellerString = sellerModel.getSellerCodeList(); //List of sellers codes separated by commas
					final Set<SellerMasterModel> newsellerList = new HashSet<SellerMasterModel>();

					final StringTokenizer newSellerTokens = new StringTokenizer(sellerString,
							MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);
					//getting tokens for seller ids and checking its presence in seller master
					while (newSellerTokens.hasMoreTokens())
					{
						newsellerList.add(mplSellerMasterService.getSellerMaster(newSellerTokens.nextToken().trim()));
					}
					final List<SellerMasterModel> existingSellerList = Lists.newArrayList(sellerModel.getSeller());
					if (CollectionUtils.isNotEmpty(existingSellerList))
					{
						newsellerList.addAll(existingSellerList);
						//removing if any null values exist in the final seller list
						CollectionUtils.filter(newsellerList, PredicateUtils.notNullPredicate());
					}
					if (CollectionUtils.isNotEmpty(sellerModel.getSeller()))
					{
						existingSellerList.clear();
					}
					sellerModel.setSeller(newsellerList);
				}

				sellerModel.setSellerCodeList(BLANK);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
		}
	}
}