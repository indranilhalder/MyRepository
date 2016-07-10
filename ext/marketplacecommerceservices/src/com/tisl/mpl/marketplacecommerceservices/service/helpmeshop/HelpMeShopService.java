/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.helpmeshop;

import java.util.List;

import com.tisl.mpl.data.GenderOrTitleData;
import com.tisl.mpl.data.ReasonOrEventData;
import com.tisl.mpl.data.TypeOfProductData;


/**
 * @author TCS
 *
 */
public interface HelpMeShopService
{


	List<GenderOrTitleData> getGenderOrTitle();

	List<ReasonOrEventData> getReasonOrEvent();

	List<TypeOfProductData> getTypeOfProduct();


}
