/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.marketplacecommerceservices.daos.BulkPromotionCreationDao;
import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author TCS
 *
 */
@Component(value = "bulkPromotionCreationDao")
public class DefaultBulkPromotionCreationDaoImpl implements BulkPromotionCreationDao
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultBulkPromotionCreationDaoImpl.class);

	@Autowired
	private FlexibleSearchService flexibleSearchService;
	private static final String SELECT_CLASS = "SELECT {p:";
	private static final String FROM_CLASS = "FROM {";
	private static final String P_CLASS = "{p.";
	private static final String CODE_CLASS = "} = ?code";
	private static final String WHERE = " AS p } where";
	private static final String CODE = "code";

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}


	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}


	/**
	 * @Description : To fetch existing Promotion Details
	 * @return : List<ProductPromotionModel>
	 */
	@Override
	public AbstractPromotionModel fetchExistingPromotionData(final String code)
	{
		final String queryString = //
		SELECT_CLASS + AbstractPromotionModel.PK + "} "//
				+ FROM_CLASS + AbstractPromotionModel._TYPECODE + WHERE + P_CLASS + AbstractPromotionModel.CODE + CODE_CLASS;
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(CODE, code);
		return flexibleSearchService.<AbstractPromotionModel> searchUnique(query);
	}


	/**
	 * @Description: Fetch Promotion Group
	 * @return : PromotionGroupModel
	 */
	@Override
	public PromotionGroupModel fetchPromotionGroup(final String promoGroup)
	{
		final String queryString = //
		SELECT_CLASS + PromotionGroupModel.PK + "} "//
				+ FROM_CLASS + PromotionGroupModel._TYPECODE + WHERE + P_CLASS + PromotionGroupModel.IDENTIFIER + "} = ?promoGroup";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("promoGroup", promoGroup);
		return flexibleSearchService.<PromotionGroupModel> searchUnique(query);
	}


	/**
	 * @Description: Fetch Currency Details
	 * @return : CurrencyModel
	 */
	@Override
	public CurrencyModel fetchCurrencyData(final String currency)
	{
		final String queryString = //
		SELECT_CLASS + CurrencyModel.PK + "} "//
				+ FROM_CLASS + CurrencyModel._TYPECODE + WHERE + P_CLASS + CurrencyModel.ISOCODE + CODE_CLASS;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(CODE, currency);
		return flexibleSearchService.<CurrencyModel> searchUnique(query);
	}


	/**
	 * @Description: Fetch Product Details
	 * @return : ProductModel
	 */
	@Override
	public ProductModel fetchProductData(final CatalogVersionModel catalog, final String code)
	{
		final String queryString = //
		SELECT_CLASS + ProductModel.PK
				+ "} "//
				+ FROM_CLASS + ProductModel._TYPECODE + WHERE + P_CLASS + ProductModel.CODE + "} = ?code and " + P_CLASS
				+ ProductModel.CATALOGVERSION + "} = ?catalog";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(CODE, code);
		query.addQueryParameter("catalog", catalog);
		return flexibleSearchService.<ProductModel> searchUnique(query);
	}


	/**
	 * @Description: Fetch Category Details
	 * @return : CategoryModel
	 */
	@Override
	public CategoryModel fetchCategoryData(final CatalogVersionModel catalog, final String code)
	{
		final String queryString = //
		SELECT_CLASS + CategoryModel.PK
				+ "} "//
				+ FROM_CLASS + CategoryModel._TYPECODE + WHERE + P_CLASS + CategoryModel.CODE + "} = ?code and " + P_CLASS
				+ CategoryModel.CATALOGVERSION + "} = ?catalog";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(CODE, code);
		query.addQueryParameter("catalog", catalog);
		return flexibleSearchService.<CategoryModel> searchUnique(query);
	}


	/**
	 * @Description: Fetch Seller Information based on Seller USSID
	 * @param : code, oModel
	 * @return List<SellerInformationModel>
	 */
	@Override
	public List<SellerInformationModel> fetchSellerInformation(final CatalogVersionModel catalogVersionModel, final String string)
	{
		LOG.debug("Fetching Seller Data");
		final String queryString = //
		SELECT_CLASS + SellerInformationModel.PK
				+ "} "//
				+ FROM_CLASS + SellerInformationModel._TYPECODE + WHERE + P_CLASS + SellerInformationModel.SELLERID
				+ "} = ?code and " + P_CLASS + SellerInformationModel.CATALOGVERSION + "} = ?oModel";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(CODE, string);
		query.addQueryParameter("oModel", catalogVersionModel);
		return flexibleSearchService.<SellerInformationModel> search(query).getResult();
	}

	/**
	 * @Description: Fetch Delivery Mode Details
	 */
	@Override
	public DeliveryModeModel fetchDeliveryModeDetails(final String deliveryMode)
	{
		final String queryString = //
		SELECT_CLASS + DeliveryModeModel.PK + "} "//
				+ FROM_CLASS + DeliveryModeModel._TYPECODE + WHERE + P_CLASS + DeliveryModeModel.CODE + CODE_CLASS;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(CODE, deliveryMode);
		return flexibleSearchService.<DeliveryModeModel> searchUnique(query);
	}


	/**
	 * @Description: Fetch Payment Mode Details
	 */
	@Override
	public PaymentTypeModel fetchPaymentModeDetails(final String paymentMode)
	{
		final String queryString = //
		SELECT_CLASS + PaymentTypeModel.PK + "} "//
				+ FROM_CLASS + PaymentTypeModel._TYPECODE + WHERE + P_CLASS + PaymentTypeModel.MODE + CODE_CLASS;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(CODE, paymentMode);
		return flexibleSearchService.<PaymentTypeModel> searchUnique(query);
	}


	/**
	 * @Description: Fetch Bank Details
	 */
	@Override
	public BankModel fetchBankDetails(final String bankCode)
	{
		final String queryString = //
		SELECT_CLASS + BankModel.PK + "} "//
				+ FROM_CLASS + BankModel._TYPECODE + WHERE + P_CLASS + BankModel.BANKCODE + CODE_CLASS;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(CODE, bankCode);
		return flexibleSearchService.<BankModel> searchUnique(query);
	}


	/**
	 * The Method is used to fetch Seller Details
	 *
	 */
	@Override
	public List<SellerMasterModel> fetchSellerMasterInfo(final String sellerId)
	{
		LOG.debug("Fetching Seller Data");
		final String queryString = //
		SELECT_CLASS + SellerMasterModel.PK + "} "//
				+ FROM_CLASS + SellerMasterModel._TYPECODE + WHERE + P_CLASS + SellerMasterModel.ID + "} = ?code ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(CODE, sellerId);
		return flexibleSearchService.<SellerMasterModel> search(query).getResult();
	}
}
