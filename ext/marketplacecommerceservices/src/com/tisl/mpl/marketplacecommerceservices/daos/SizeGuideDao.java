/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.List;

import com.tisl.mpl.core.model.SizeGuideModel;


/**
 * @author TCS
 *
 */
public interface SizeGuideDao
{
	List<SizeGuideModel> getsizeGuideByCode(String sizeGuideCode);


}
