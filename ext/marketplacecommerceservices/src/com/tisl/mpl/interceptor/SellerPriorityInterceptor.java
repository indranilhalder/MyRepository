/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.core.enums.SellerPriorityEnum;
import com.tisl.mpl.core.model.MplSellerPriorityModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplSellerPriorityDao;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
public class SellerPriorityInterceptor implements PrepareInterceptor
//ValidateInterceptor
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
	private static final String SELLER_NOT_MAPPED_CATEGORY = "There is no seller mapped against the particular category";
	private static final String SELLER_NOT_MAPPED_PRODUCT = "There is no seller mapped against the particular product";
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
	//	public void onValidate(final Object model, final InterceptorContext arg) throws InterceptorException
	public void onPrepare(final Object model, final InterceptorContext arg) throws InterceptorException
	{

		// YTODO Auto-generated method stub
		if (model instanceof MplSellerPriorityModel)
		{
			final MplSellerPriorityModel priority = (MplSellerPriorityModel) model;
			String categoryId = null;
			String listingId = null;

			LOG.debug("modified row  *********** categoryId : " + categoryId + " ************** listingId" + listingId);
			if (null == priority.getSellerId())
			{
				throw new InterceptorException(SELLERIDBLANK);
			}
			if (null != priority.getListingId() && null != priority.getCategoryId() && null != priority.getSellerId())
			{
				listingId = priority.getListingId().getCode();
				if (!checkSellerExistsForUssid(Collections.singletonList(priority.getListingId()), priority.getSellerId().getId()))
				{
					throw new InterceptorException(SELLER_NOT_MAPPED_PRODUCT);
				}
			}
			else if (null != priority.getListingId())
			{
				listingId = priority.getListingId().getCode();
				if (!checkSellerExistsForUssid(Collections.singletonList(priority.getListingId()), priority.getSellerId().getId()))
				{
					throw new InterceptorException(SELLER_NOT_MAPPED_PRODUCT);
				}
			}
			if (null != priority.getCategoryId() && null != priority.getSellerId())
			{
				categoryId = priority.getCategoryId().getCode();

				if (!checkSellerExistsForUssid(mplSellerPriorityDao.getProductListForCategory(priority.getCategoryId()), priority
						.getSellerId().getId()))
				{
					throw new InterceptorException(SELLER_NOT_MAPPED_CATEGORY);
				}
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
							|| arg.isModified(priorityValue, MplSellerPriorityModel.LISTINGID))
					{
						throw new InterceptorException(CATIDPRODIDNONEDITABLE);
					}
					else
					{
						// Addding Category id and listing id into a list for the rows not modified
						if (!arg.isModified(priorityValue, MplSellerPriorityModel.PRIORITYSTARTDATE)
								&& !arg.isModified(priorityValue, MplSellerPriorityModel.PRIORITYENDDATE)
								&& null != priorityValue.getIsActive() && priorityValue.getIsActive().booleanValue()
								&& !arg.isModified(priorityValue, MplSellerPriorityModel.ISACTIVE)
								&& !arg.isModified(priorityValue, MplSellerPriorityModel.SELLERID))
						{
							LOG.debug("no modification *********** categoryId : " + priorityValue.getCategoryId()
									+ " **************   listingId" + priorityValue.getListingId());

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
				// if new value already	 exist throw error
				if (SellerPriorityEnum.PROCESSING.equals(priority.getPriorityStatus()))
				{
					priority.setPriorityStatus(SellerPriorityEnum.PROCESSED);
				}
				else if (SellerPriorityEnum.ERROR.equals(priority.getPriorityStatus()))
				{
					priority.setPriorityStatus(SellerPriorityEnum.ERROR);
				}
				else
				{
					if (priority.getIsActive().booleanValue())

					{
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
	}

	/**
	 * @param productList
	 * @param sellerId
	 */
	private boolean checkSellerExistsForUssid(final List<ProductModel> productList, final String sellerId)
	{
		boolean isExits = false;
		// YTODO Auto-generated method stub
		for (final ProductModel product : productList)
		{
			for (final SellerInformationModel seller : product.getSellerInformationRelator())
			{
				if (seller.getSellerID().equals(sellerId))
				{
					isExits = true;
					break;
				}
			}
		}
		return isExits;
	}
}