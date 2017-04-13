/**
 * 
 */
package com.tisl.mpl.xml.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author TECHOUTS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType( propOrder ={ "success"})
@XmlRootElement(name = "ReturnInfoResponse")
public class RTSAndRSSReturnInfoResponse
{
@XmlElement(name = "Success")	
private String success;

/**
 * @return the success
 */
public String getSuccess()
{
	return success;
}

/**
 * @param success the success to set
 */
public void setSuccess(String success)
{
	this.success = success;
}


}
