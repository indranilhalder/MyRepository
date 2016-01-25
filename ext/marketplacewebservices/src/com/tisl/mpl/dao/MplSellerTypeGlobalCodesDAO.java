/**
 *
 */
package com.tisl.mpl.dao;

import java.util.List;

import com.tisl.mpl.model.SellerTypeGlobalCodesModel;


/**
 * @author TCS
 *
 */
public interface MplSellerTypeGlobalCodesDAO
{
	List<SellerTypeGlobalCodesModel> findSellerTypeDescriptionByCode(String code);

}
