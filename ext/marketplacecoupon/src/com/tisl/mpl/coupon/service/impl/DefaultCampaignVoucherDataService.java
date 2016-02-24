/**
 *
 */
package com.tisl.mpl.coupon.service.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.ProductCategoryRestrictionModel;
import de.hybris.platform.voucher.model.ProductRestrictionModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.coupon.constants.MarketplacecouponConstants;
import com.tisl.mpl.coupon.dao.MplCouponDao;
import com.tisl.mpl.coupon.pojo.CampaignVoucherData;
import com.tisl.mpl.coupon.service.CampaignVoucherDataService;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * @author TCS
 *
 */
public class DefaultCampaignVoucherDataService implements CampaignVoucherDataService
{
	private final static Logger LOG = Logger.getLogger(DefaultCampaignVoucherDataService.class.getName());

	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "mplCouponDao")
	private MplCouponDao mplCouponDao;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "campaignVoucherData")
	private CampaignVoucherData campaignVoucherData;



	/**
	 *
	 * The Method is used to generate the .csv file data for the Campaign Team
	 *
	 */
	@Override
	public void generateCSV()
	{
		final List<VoucherModel> voucherList = getMplCouponDao().findVoucher();
		List<CampaignVoucherData> campaignDataList = new ArrayList<CampaignVoucherData>();
		if (CollectionUtils.isNotEmpty(voucherList))
		{
			LOG.debug("The Required Voucher Data  is Fetched");
			campaignDataList = populateCampaignData(voucherList);

			if (CollectionUtils.isNotEmpty(campaignDataList))
			{
				generateCSVFile(campaignDataList);
			}
		}
	}



	/**
	 * The Method is used to populate the Data Class with the Voucher Data
	 *
	 * @param voucherList
	 * @return List<CampaignVoucherData>
	 */
	private List<CampaignVoucherData> populateCampaignData(final List<VoucherModel> voucherList)
	{
		final List<CampaignVoucherData> campaignDataList = new ArrayList<CampaignVoucherData>();
		for (final VoucherModel voucher : voucherList)
		{
			campaignDataList.add(voucherCampaignData(voucher));
		}
		return campaignDataList;
	}



	/**
	 * The Method is used to populate the Data Class with the Voucher Data
	 *
	 * @param voucher
	 * @return data
	 */
	private CampaignVoucherData voucherCampaignData(final VoucherModel voucher)
	{
		CampaignVoucherData data = new CampaignVoucherData();

		data = populateDefaultVal(data); //Setting up default values
		if (voucher instanceof PromotionVoucherModel)
		{
			final PromotionVoucherModel promoVoucher = (PromotionVoucherModel) voucher;
			//Setting voucher details to data
			data.setIdentifier(voucher.getCode());

			if (StringUtils.isNotEmpty(promoVoucher.getVoucherCode()))
			{
				data.setVoucherCode(promoVoucher.getVoucherCode());
			}

			if (null == voucher.getCurrency() && null != promoVoucher.getMaxDiscountValue()
					&& promoVoucher.getMaxDiscountValue().doubleValue() > 0)
			{
				data.setMaxDiscount(promoVoucher.getMaxDiscountValue().toString());
			}

			if (StringUtils.isNotEmpty(voucher.getName()))
			{
				final StringBuilder dataBuilder = new StringBuilder(50);
				data.setName(dataBuilder.append("\"").append(voucher.getName()).append("\"").toString());
			}

			if (StringUtils.isNotEmpty(voucher.getDescription()))
			{
				final StringBuilder dataBuilder = new StringBuilder(50);
				data.setPromoText(dataBuilder.append("\"").append(voucher.getDescription()).append("\"").toString());
			}

			if (null != voucher.getValue())
			{
				data.setValue(voucher.getValue().toString());
			}

			if (null != voucher.getCurrency() && null != voucher.getCurrency().getIsocode())
			{
				data.setCurrency(voucher.getCurrency().getIsocode());
			}

			if (null != promoVoucher.getRedemptionQuantityLimit() && promoVoucher.getRedemptionQuantityLimit().intValue() >= 0)
			{
				data.setRedeemLimit(promoVoucher.getRedemptionQuantityLimit().toString());
			}

			if (null != promoVoucher.getRedemptionQuantityLimitPerUser()
					&& promoVoucher.getRedemptionQuantityLimitPerUser().intValue() >= 0)
			{
				data.setRedeemLimitUsr(promoVoucher.getRedemptionQuantityLimitPerUser().toString());
			}

			if (CollectionUtils.isNotEmpty(promoVoucher.getRestrictions()))
			{
				data = populateRestrictionData(promoVoucher.getRestrictions(), data);
			}
		}
		return data;
	}


	/**
	 * Populate data from Voucher Restriction to Data Class
	 *
	 * @param restrictionList
	 * @param data
	 * @return campaignData
	 */
	private CampaignVoucherData populateRestrictionData(final Set<RestrictionModel> restrictionList, final CampaignVoucherData data)
	{
		final CampaignVoucherData campaignData = data;

		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		for (final RestrictionModel restriction : restrictionList)
		{
			if (restriction instanceof DateRestrictionModel)
			{
				final DateRestrictionModel dateModel = (DateRestrictionModel) restriction;
				campaignData.setStartDate(formatter.format(dateModel.getStartDate()));
				campaignData.setEndDate(formatter.format(dateModel.getEndDate()));
			}
			else if ((restriction instanceof ProductRestrictionModel) && !(restriction instanceof ProductCategoryRestrictionModel))
			{
				final ProductRestrictionModel productModel = (ProductRestrictionModel) restriction;
				campaignData.setProducts(populateProductData(productModel));
				campaignData.setForSelProducts(populatePositiveCheck(restriction));
			}
			else if ((restriction instanceof ProductRestrictionModel) && (restriction instanceof ProductCategoryRestrictionModel))
			{
				final ProductCategoryRestrictionModel oModel = (ProductCategoryRestrictionModel) restriction;
				campaignData.setCategories(populateCategoryData(oModel));
				campaignData.setForSelCategories(populatePositiveCheck(restriction));
			}
			else if (restriction instanceof UserRestrictionModel)
			{
				final UserRestrictionModel userModel = (UserRestrictionModel) restriction;
				campaignData.setUserGrp(populateUserGrp(userModel));
				campaignData.setUsers(populateUser(userModel));
				campaignData.setForSelUsers(populatePositiveCheck(restriction));
			}
		}
		return campaignData;
	}





	/**
	 * The Method is used to populate User Data
	 *
	 * @param userModel
	 * @return String
	 */
	private String populateUser(final UserRestrictionModel userModel)
	{
		String userdata = MarketplacecouponConstants.EMPTYSPACE;
		final List<UserModel> userDataList = new ArrayList<UserModel>();

		if (CollectionUtils.isNotEmpty(userModel.getUsers()))
		{
			for (final PrincipalModel oModel : userModel.getUsers())
			{
				if (oModel instanceof UserModel)
				{
					final UserModel user = (UserModel) oModel;
					userDataList.add(user);
				}
			}
			if (CollectionUtils.isNotEmpty(userDataList))
			{
				userdata = populateUsrData(userDataList);
			}
		}

		return userdata;
	}



	/**
	 * The Method is used to populate User Data
	 *
	 * @param userDataList
	 * @return String
	 */
	private String populateUsrData(final List<UserModel> userDataList)
	{
		String userdata = MarketplacecouponConstants.EMPTYSPACE;

		for (int i = 0; i < userDataList.size(); i++)
		{
			if ((i != (userDataList.size() - 1)))
			{
				userdata = userdata + userDataList.get(i).getUid()
						+ MarketplacecommerceservicesConstants.CAMPAIGN_MULTIDATA_SEPERATOR;
			}
			else
			{
				userdata = userdata + userDataList.get(i).getUid();
			}
		}

		return userdata;
	}



	/**
	 * The Method is used to populate User Grp Data
	 *
	 * @param userModel
	 * @return String
	 */
	private String populateUserGrp(final UserRestrictionModel userModel)
	{
		String userdata = MarketplacecouponConstants.EMPTYSPACE;
		final List<UserGroupModel> userDataList = new ArrayList<UserGroupModel>();
		if (CollectionUtils.isNotEmpty(userModel.getUsers()))
		{
			for (final PrincipalModel oModel : userModel.getUsers())
			{
				if (oModel instanceof UserGroupModel)
				{
					final UserGroupModel userGrp = (UserGroupModel) oModel;
					userDataList.add(userGrp);
				}
			}


			if (CollectionUtils.isNotEmpty(userDataList))
			{
				userdata = populateUsrGrpData(userDataList);
			}

		}
		return userdata;
	}



	/**
	 * The Method is used to populate User Grp Data
	 *
	 * @param userDataList
	 * @return String
	 */
	private String populateUsrGrpData(final List<UserGroupModel> userDataList)
	{
		String userdata = MarketplacecouponConstants.EMPTYSPACE;

		for (int i = 0; i < userDataList.size(); i++)
		{
			if ((i != (userDataList.size() - 1)))
			{
				userdata = userdata
						+ (userDataList.get(i).getName() == null ? userDataList.get(i).getUid() : userDataList.get(i).getName())
						+ MarketplacecommerceservicesConstants.CAMPAIGN_MULTIDATA_SEPERATOR;
			}
			else
			{
				userdata = userdata
						+ (userDataList.get(i).getName() == null ? userDataList.get(i).getUid() : userDataList.get(i).getName());
			}
		}

		return userdata;
	}



	/**
	 * Populate Product Data from Restriction
	 *
	 * @param oModel
	 * @return String
	 */
	private String populateCategoryData(final ProductCategoryRestrictionModel oModel)
	{
		String categorydata = MarketplacecouponConstants.EMPTYSPACE;
		if (CollectionUtils.isNotEmpty(oModel.getCategories()))
		{
			final List<CategoryModel> categoryList = new ArrayList<CategoryModel>(oModel.getCategories());

			for (int i = 0; i < categoryList.size(); i++)
			{
				if ((i != (categoryList.size() - 1)))
				{
					categorydata = categorydata + (categoryList.get(i).getCode())
							+ MarketplacecommerceservicesConstants.CAMPAIGN_MULTIDATA_SEPERATOR;
				}
				else
				{
					categorydata = categorydata + (categoryList.get(i).getCode());
				}
			}

		}
		return categorydata;

	}



	/**
	 * Populate Product Data from Restriction
	 *
	 * @param productModel
	 * @return String
	 */
	private String populateProductData(final ProductRestrictionModel productModel)
	{
		String productdata = MarketplacecouponConstants.EMPTYSPACE;
		if (CollectionUtils.isNotEmpty(productModel.getProducts()))
		{
			final List<ProductModel> productList = new ArrayList<ProductModel>(productModel.getProducts());

			for (int i = 0; i < productList.size(); i++)
			{
				if ((i != (productList.size() - 1)))
				{
					productdata = productdata + (productList.get(i).getCode())
							+ MarketplacecommerceservicesConstants.CAMPAIGN_MULTIDATA_SEPERATOR;
				}
				else
				{
					productdata = productdata + (productList.get(i).getCode());
				}
			}

		}
		return productdata;
	}



	/**
	 * Set Default Value
	 *
	 * @param data
	 * @return CampaignVoucherData
	 */
	private CampaignVoucherData populateDefaultVal(final CampaignVoucherData data)
	{
		data.setIdentifier(MarketplacecouponConstants.EMPTYSPACE);
		data.setVoucherCode(MarketplacecouponConstants.EMPTYSPACE);
		data.setValue(MarketplacecouponConstants.EMPTYSPACE);
		data.setCurrency(MarketplacecouponConstants.EMPTYSPACE);
		data.setRedeemLimit(MarketplacecouponConstants.EMPTYSPACE);
		data.setRedeemLimitUsr(MarketplacecouponConstants.EMPTYSPACE);
		data.setName(MarketplacecouponConstants.EMPTYSPACE);
		data.setPromoText(MarketplacecouponConstants.EMPTYSPACE);
		data.setStartDate(MarketplacecouponConstants.EMPTYSPACE);
		data.setEndDate(MarketplacecouponConstants.EMPTYSPACE);
		data.setProducts(MarketplacecouponConstants.EMPTYSPACE);
		data.setCategories(MarketplacecouponConstants.EMPTYSPACE);
		data.setUserGrp(MarketplacecouponConstants.EMPTYSPACE);
		data.setUsers(MarketplacecouponConstants.EMPTYSPACE);
		data.setMaxDiscount(MarketplacecouponConstants.EMPTYSPACE);
		data.setForSelUsers(MarketplacecouponConstants.EMPTYSPACE);
		data.setForSelCategories(MarketplacecouponConstants.EMPTYSPACE);
		data.setForSelProducts(MarketplacecouponConstants.EMPTYSPACE);

		return data;
	}



	/**
	 * This method checks whether the product/Category/Brand restriction is for the selected
	 * products/categories/users/userGroups
	 *
	 * @param restriction
	 * @return String
	 */
	private String populatePositiveCheck(final RestrictionModel restriction)
	{
		String restCheckdata = MarketplacecouponConstants.EMPTYSPACE;
		if (null != restriction)
		{
			final Boolean check = restriction.getPositive();
			if (null != check)
			{
				restCheckdata = check.toString();
			}
		}

		return restCheckdata;
	}


	/**
	 * The Method is used to generate the .csv file at a specified location
	 *
	 * @param campaignDataList
	 */
	private void generateCSVFile(final List<CampaignVoucherData> campaignDataList)
	{
		FileWriter fileWriter = null;

		String datePrefix = MarketplacecouponConstants.EMPTYSPACE;
		if (null != GenericUtilityMethods.convertSysDateToString(new Date()))
		{
			datePrefix = GenericUtilityMethods.convertSysDateToString(new Date());
		}

		final File rootFolder = new File(getConfigurationService().getConfiguration().getString(
				MarketplacecouponConstants.CAMPAIGN_FILE_LOCATION, MarketplacecommerceservicesConstants.CAMPAIGN_FILE_PATH),
				MarketplacecouponConstants.CAMPAIGN_FILE_NAME
						+ datePrefix
						+ getConfigurationService().getConfiguration().getString(MarketplacecouponConstants.VOUCHERCAMPAIGNJOBEXTN,
								MarketplacecouponConstants.DEFVOUCAMPAIGNJOBEXTN));

		try
		{
			fileWriter = new FileWriter(rootFolder);
			fileWriter.append(MarketplacecouponConstants.CAMPAIGN_HEADER);

			fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_NEW_LINE_SEPARATOR);

			for (final CampaignVoucherData data : campaignDataList)
			{
				fileWriter.append(data.getIdentifier());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getName());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getPromoText());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getVoucherCode());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getValue());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getCurrency());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getRedeemLimit());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getRedeemLimitUsr());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getMaxDiscount());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getStartDate());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getEndDate());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getProducts());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getForSelProducts());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getCategories());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getForSelCategories());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getUserGrp());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getUsers());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getForSelUsers());
				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_DELIMITTER);


				fileWriter.append(MarketplacecouponConstants.CAMPAIGN_FILE_NEW_LINE_SEPARATOR);
			}


		}
		catch (final IOException exception)
		{
			LOG.error("IO Exception", exception);
		}
		catch (final Exception exception)
		{
			LOG.error(exception.getMessage());
		}

		finally
		{
			try
			{
				fileWriter.flush();
				fileWriter.close();
			}
			catch (final IOException exception)
			{
				LOG.error("Error while flushing/closing fileWriter !!!" + exception.getMessage());
			}
		}
	}



	//For Sonars
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
	 * @return the mplCouponDao
	 */
	public MplCouponDao getMplCouponDao()
	{
		return mplCouponDao;
	}

	/**
	 * @param mplCouponDao
	 *           the mplCouponDao to set
	 */
	public void setMplCouponDao(final MplCouponDao mplCouponDao)
	{
		this.mplCouponDao = mplCouponDao;
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
	 * @return the campaignVoucherData
	 */
	public CampaignVoucherData getCampaignVoucherData()
	{
		return campaignVoucherData;
	}

	/**
	 * @param campaignVoucherData
	 *           the campaignVoucherData to set
	 */
	public void setCampaignVoucherData(final CampaignVoucherData campaignVoucherData)
	{
		this.campaignVoucherData = campaignVoucherData;
	}


}
