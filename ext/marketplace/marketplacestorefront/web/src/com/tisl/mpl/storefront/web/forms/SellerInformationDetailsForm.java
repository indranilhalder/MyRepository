/**
 *
 */
package com.tisl.mpl.storefront.web.forms;

import java.util.List;


/**
 * @author TCS
 *
 */


public class SellerInformationDetailsForm
{

	private List<String> sellersSkuListId;
	private List<String> sellerSkuId;
	private List<String> skuIdsWithNoStock;
	private List<String> skuIdForED;
	private List<String> skuIdForHD;
	private String skuIdForCod;
	private String selectedSizeVariant;
	private String stockDataArray;
	private String isPinCodeChecked;

	/**
	 * @return the selectedSizeVariant
	 */
	public String getSelectedSizeVariant()
	{
		return selectedSizeVariant;
	}

	/**
	 * @param selectedSizeVariant
	 *           the selectedSizeVariant to set
	 */
	public void setSelectedSizeVariant(final String selectedSizeVariant)
	{
		this.selectedSizeVariant = selectedSizeVariant;
	}

	/**
	 * @return the sellersSkuListId
	 */
	public List<String> getSellersSkuListId()
	{
		return sellersSkuListId;
	}

	/**
	 * @param sellersSkuListId
	 *           the sellersSkuListId to set
	 */
	public void setSellersSkuListId(final List<String> sellersSkuListId)
	{
		this.sellersSkuListId = sellersSkuListId;
	}

	/**
	 * @return the sellerSkuId
	 */
	public List<String> getSellerSkuId()
	{
		return sellerSkuId;
	}

	/**
	 * @param sellerSkuId
	 *           the sellerSkuId to set
	 */
	public void setSellerSkuId(final List<String> sellerSkuId)
	{
		this.sellerSkuId = sellerSkuId;
	}

	/**
	 * @return the skuIdsWithNoStock
	 */
	public List<String> getSkuIdsWithNoStock()
	{
		return skuIdsWithNoStock;
	}

	/**
	 * @param skuIdsWithNoStock
	 *           the skuIdsWithNoStock to set
	 */
	public void setSkuIdsWithNoStock(final List<String> skuIdsWithNoStock)
	{
		this.skuIdsWithNoStock = skuIdsWithNoStock;
	}

	/**
	 * @return the skuIdForED
	 */
	public List<String> getSkuIdForED()
	{
		return skuIdForED;
	}

	/**
	 * @param skuIdForED
	 *           the skuIdForED to set
	 */
	public void setSkuIdForED(final List<String> skuIdForED)
	{
		this.skuIdForED = skuIdForED;
	}

	/**
	 * @return the skuIdForHD
	 */
	public List<String> getSkuIdForHD()
	{
		return skuIdForHD;
	}

	/**
	 * @param skuIdForHD
	 *           the skuIdForHD to set
	 */
	public void setSkuIdForHD(final List<String> skuIdForHD)
	{
		this.skuIdForHD = skuIdForHD;
	}

	/**
	 * @return the skuIdForCod
	 */
	public String getSkuIdForCod()
	{
		return skuIdForCod;
	}

	/**
	 * @param skuIdForCod
	 *           the skuIdForCod to set
	 */
	public void setSkuIdForCod(final String skuIdForCod)
	{
		this.skuIdForCod = skuIdForCod;
	}

	/**
	 * @return the stockDataArray
	 */
	public String getStockDataArray()
	{
		return stockDataArray;
	}

	/**
	 * @param stockDataArray
	 *           the stockDataArray to set
	 */
	public void setStockDataArray(final String stockDataArray)
	{
		this.stockDataArray = stockDataArray;
	}

	/**
	 * @return the isPinCodeChecked
	 */
	public String getIsPinCodeChecked()
	{
		return isPinCodeChecked;
	}

	/**
	 * @param isPinCodeChecked
	 *           the isPinCodeChecked to set
	 */
	public void setIsPinCodeChecked(final String isPinCodeChecked)
	{
		this.isPinCodeChecked = isPinCodeChecked;
	}

	/**
	 * @return the nonServicableSkuList
	 */


}