/**
 * 
 */
package com.tisl.mpl.wsdto;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author TECH
 *
 */
@XStreamAlias("SellerSlave")
public class SellerSlaveDTO
{
	@XStreamImplicit(itemFieldName = "SlaveInfo")
	private List<SlaveInfoDTO> slaveInfo;

	/**
	 * @return the slaveInfo
	 */
	public List<SlaveInfoDTO> getSlaveInfo()
	{
		return slaveInfo;
	}

	/**
	 * @param slaveInfo the slaveInfo to set
	 */
	public void setSlaveInfo(List<SlaveInfoDTO> slaveInfo)
	{
		this.slaveInfo = slaveInfo;
	}
}
