/**
 *
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "UploadImage")
public class UploadImage
{
	private String imagePath;

	/**
	 * @return the imagePath
	 */
	@XmlElement(name = "ImagePath")
	public String getImagePath()
	{
		return imagePath;
	}

	/**
	 * @param imagePath
	 *           the imagePath to set
	 */
	public void setImagePath(final String imagePath)
	{
		this.imagePath = imagePath;
	}
}
