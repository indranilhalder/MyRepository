/**
 *
 */
package com.tisl.mpl.pojo;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author TCS
 * @param <ListDelist>
 *
 */
@XmlRootElement(name = "SellerUSSIDDelisting")
public class ProductDelistingDTO
{
	private final List<RecordsetDTO> recordset = new ArrayList();

	/**
	 * @return the listdelist
	 */
	@XmlElement(name = "ListDelist")
	public List<RecordsetDTO> getRecordset()
	{
		return recordset;
	}
}
