/**
 *
 */
package com.tisl.mpl.promotion.dao;

import java.util.List;

import com.tisl.mpl.core.model.BinErrorModel;


/**
 * @author TCS
 *
 */
public interface MplBinErrorDao
{
	List<BinErrorModel> getBinErrorDetails();
}