/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.core.model.MplSellerPriorityModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplSellerPriorityDao;


/**
 * @author TCS
 *
 */
public class SellerPriorityInterceptor implements ValidateInterceptor
{


	private static final String ERROR_SAME_CATEGORY = "There is already an entry active for the same category id";
	/**
	 *
	 */
	private static final String ERROR_SAME_SKU = "There is already an entry for the same product id";
	private static final String SELLERIDBLANK = "seller Id cannot be blank";
	private static final String CATANDLISTBLANK = "Category Id and Product Id both cannot be blank";
	private static final String STARTDATEBLANK = "start date cannot be blank";
	private static final String ENDDATEBLANK = "end date cannot be blank";
	private static final String ENDDATEBEFORESTARTDATE = "end date cannot be before start date";
	private static final String CATIDPRODIDNONEDITABLE = "cannot modify category id or product id";
	@Resource(name = "mplSellerPriorityDao")
	private MplSellerPriorityDao mplSellerPriorityDao;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SellerPriorityInterceptor.class.getName());

	/**
	 * @Description : The Method checks the Promotion Priority prior to its creation from HMCR
	 * @param: object
	 * @param: arg1
	 * @return: void
	 */
	@Override
	public void onValidate(final Object model, final InterceptorContext arg) throws InterceptorException
	{

		// YTODO Auto-generated method stub
		if (model instanceof MplSellerPriorityModel)
		{
			final MplSellerPriorityModel priority = (MplSellerPriorityModel) model;
			String categoryId = null;
			String listingId = null;

			LOG.debug("modified row  *********** categoryId : " + categoryId + " ************** listingId" + listingId);

			if (null != priority.getCategoryId())
			{
				categoryId = priority.getCategoryId().getCode();
			}
			if (null != priority.getListingId())
			{
				listingId = priority.getListingId().getCode();
			}
			if (null == priority.getSellerId())
			{
				throw new InterceptorException(SELLERIDBLANK);
			}
			if (null == priority.getCategoryId() && null == priority.getListingId())
			{
				throw new InterceptorException(CATANDLISTBLANK);
			}
			if (null == priority.getPriorityStartDate())
			{
				throw new InterceptorException(STARTDATEBLANK);
			}
			if (null == priority.getPriorityEndDate())
			{
				throw new InterceptorException(ENDDATEBLANK);
			}
			if (priority.getPriorityEndDate().before(priority.getPriorityStartDate()))
			{
				throw new InterceptorException(ENDDATEBEFORESTARTDATE);
			}

			final List<String> categoryList = new ArrayList<String>();
			final List<String> skuIdList = new ArrayList<String>();
			//			final Map<String, String> sellerCat = new HashMap<String, String>();
			//			final Map<String, String> sellerProd = new HashMap<String, String>();
			if (!(mplSellerPriorityDao.getAllSellerPriorities().isEmpty()))
			{
				LOG.debug("*********** Priority table size" + mplSellerPriorityDao.getAllSellerPriorities().size());

				for (final MplSellerPriorityModel priorityValue : mplSellerPriorityDao.getAllSellerPriorities())
				{
					// Cannot modify Category ID or Listing ID
					if (arg.isModified(priorityValue, MplSellerPriorityModel.CATEGORYID)
							|| arg.isModified(priorityValue, MplSellerPriorityModel.LISTINGID)
							|| arg.isModified(priorityValue, MplSellerPriorityModel.LISTINGID))
					{
						throw new InterceptorException(CATIDPRODIDNONEDITABLE);
					}
					else
					{
						// Addding Category id and listing id into a list for the rows not modified
						if (!arg.isModified(priorityValue, MplSellerPriorityModel.PRIORITYSTARTDATE)
								&& !arg.isModified(priorityValue, MplSellerPriorityModel.PRIORITYENDDATE)
								&& !arg.isModified(priorityValue, MplSellerPriorityModel.ISACTIVE))
						{
							LOG.debug("no modification *********** categoryId : " + priorityValue.getCategoryId()
									+ " **************   listingId" + priorityValue.getListingId());
							if (null != priorityValue.getIsActive() && priorityValue.getIsActive().booleanValue())
							{
								if (null != priorityValue.getCategoryId())
								{
									categoryList.add(priorityValue.getCategoryId().getCode());
								}
								if (null != priorityValue.getListingId())
								{
									skuIdList.add(priorityValue.getListingId().getCode());
								}
							}
						}
					}
				}
				// if new value already	 exist throw error
				if (null != categoryId && categoryList.contains(categoryId))
				{
					throw new InterceptorException(ERROR_SAME_CATEGORY);
				}
				if (null != listingId && skuIdList.contains(listingId))
				{
					throw new InterceptorException(ERROR_SAME_SKU);
				}
			}
		}
	}
}