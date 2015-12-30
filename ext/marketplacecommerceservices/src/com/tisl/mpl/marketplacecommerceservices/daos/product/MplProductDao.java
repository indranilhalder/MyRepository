/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.product;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;

import java.util.List;


/**
 *
 * @author TCS
 *
 */
public interface MplProductDao extends ProductDao
{

	@Override
	public List<ProductModel> findProductsByCode(final String code);

	/**
	 * @param code
	 * @return List<ProductModel>
	 */
	List<ProductModel> findProductsByCodeNew(String code);
}
