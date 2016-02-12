package com.tisl.mpl.core.cronjobs.delisting;


import de.hybris.platform.util.CSVWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * @author TCS
 *
 */
public interface DelistingProcessor
{
	public void processFile(InputStream input, OutputStream output, boolean flag) throws IOException;

	CSVWriter getCSVWriter(final OutputStream output) throws java.io.UnsupportedEncodingException;

}
