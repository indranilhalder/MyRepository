/**
 *
 */
package com.tisl.mpl.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "MerchantMaster")
@XmlType(propOrder =
{ "merchantMasterDataList" })
public class MerchantMasterXMLData
{
	private List<MerchantTableXMLData> merchantMasterDataList;

	/**
	 * @return the merchantMasterDataList
	 */
	@XmlElement(name = "Merchant")
	public List<MerchantTableXMLData> getMerchantMasterDataList()
	{
		return merchantMasterDataList;
	}

	/**
	 * @param merchantMasterDataList
	 *           the merchantMasterDataList to set
	 */
	public void setMerchantMasterDataList(final List<MerchantTableXMLData> merchantMasterDataList)
	{
		this.merchantMasterDataList = merchantMasterDataList;
	}


}
