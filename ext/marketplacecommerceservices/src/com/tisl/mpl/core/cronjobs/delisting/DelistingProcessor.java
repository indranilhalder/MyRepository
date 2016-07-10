/**
 *
 */
package com.tisl.mpl.core.cronjobs.delisting;

import java.util.Map;


/**
 * @author TCS
 *
 */
//TISPRD-207 Changes
public interface DelistingProcessor
{


	public void process();

	public boolean processDatatoModelSeller(final Map<Integer, String> line);

	public boolean processDatatoModelUssid(final Map<Integer, String> line);

}
