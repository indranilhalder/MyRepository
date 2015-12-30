/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;

import java.util.List;

import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author TCS
 *
 */
public interface BulkPromotionCreationDao
{

	AbstractPromotionModel fetchExistingPromotionData(final String code);

	PromotionGroupModel fetchPromotionGroup(String promoGroup);

	CurrencyModel fetchCurrencyData(String currency);

	ProductModel fetchProductData(CatalogVersionModel catalog, String code);

	CategoryModel fetchCategoryData(CatalogVersionModel catalog, String code);

	List<SellerInformationModel> fetchSellerInformation(CatalogVersionModel catalogVersionModel, String string);

	DeliveryModeModel fetchDeliveryModeDetails(final String deliveryMode);

	PaymentTypeModel fetchPaymentModeDetails(final String paymentMode);

	BankModel fetchBankDetails(final String bankCode);

	List<SellerMasterModel> fetchSellerMasterInfo(String sellerId);

}
