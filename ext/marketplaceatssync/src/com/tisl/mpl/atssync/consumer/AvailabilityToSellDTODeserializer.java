package com.tisl.mpl.atssync.consumer;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybris.oms.export.api.ats.AvailabilityToSellDTO;


/**
 *
 * @author TECHOUTS
 *
 */
public class AvailabilityToSellDTODeserializer implements Deserializer<AvailabilityToSellDTO>
{

	@Override
	public AvailabilityToSellDTO deserialize(final String arg0, final byte[] arg1)
	{
		final ObjectMapper mapper = new ObjectMapper();
		AvailabilityToSellDTO user = null;
		try
		{
			user = mapper.readValue(arg1, AvailabilityToSellDTO.class);
		}
		catch (final Exception e)
		{

			e.printStackTrace();
		}
		return user;
	}


	@Override
	public void close()
	{
		// YTODO Auto-generated method stub

	}


	@Override
	public void configure(final Map<String, ?> arg0, final boolean arg1)
	{
		// YTODO Auto-generated method stub

	}

}
