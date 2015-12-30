/**
 *
 */
package com.tisl.mpl.dao;

import java.util.List;

import com.tisl.mpl.model.SellerTypeGlobalCodesModel;


/**
 * @author 589733
 *
 */
public interface SellerTypeGlobalCodesDAO
{
	List<SellerTypeGlobalCodesModel> findSellerTypeDescriptionByCode(String code);

}
