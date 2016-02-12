package com.tisl.mpl.core.jobs.converter;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.core.model.FeedBackStoringModel;


/**
 * Converts customer model into data fed into customer feed job csv.
 */
public class FeedBackStoringModelToStringListConverter implements
		Converter<List<FeedBackStoringModel>, List<Map<Integer, String>>>
{


	@SuppressWarnings("boxing")
	@Override
	public List<Map<Integer, String>> convert(final List<FeedBackStoringModel> source) throws ConversionException
	{
		if (source == null || source.size() <= 0)
		{
			return null;
		}
		final List<Map<Integer, String>> target = new ArrayList<Map<Integer, String>>();
		for (final FeedBackStoringModel feedback : source)
		{
			final Map<Integer, String> lineData = new HashMap<Integer, String>();
			int count = 0;

			//Customer Email
			if (feedback.getCustomerEmail() != null)
			{
				lineData.put(Integer.valueOf(count++),
						StringUtils.isNotBlank(feedback.getCustomerEmail()) ? feedback.getCustomerEmail() : "");
			}
			else
			{
				lineData.put(Integer.valueOf(count++), "");
			}
			// Category
			if (feedback.getCategory() != null)
			{
				lineData.put(Integer.valueOf(count++), StringUtils.isNotBlank(feedback.getCategory().getCode()) ? feedback
						.getCategory().getCode() : "");
			}
			else
			{
				lineData.put(Integer.valueOf(count++), "");
			}
			// FeedBack for Search
			if (feedback.getFeedbackComment() != null)
			{
				lineData.put(Integer.valueOf(count++),
						StringUtils.isNotBlank(feedback.getFeedbackComment()) ? feedback.getFeedbackComment() : "");


			}
			else
			{
				lineData.put(Integer.valueOf(count++), "");
			}
			// FeedBack for FeedBack Date Time
			if (feedback.getFeedbackComment() != null)
			{
				final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm");
				lineData.put(Integer.valueOf(count++),
						StringUtils.isNotBlank(sdf.format(feedback.getCreationtime())) ? sdf.format(feedback.getCreationtime()) : "");


			}
			else
			{
				lineData.put(Integer.valueOf(count++), "");
			}

			// FeedBack for Search Term,Search Category

			if (feedback.getFeedbackComment() != null)
			{
				lineData.put(Integer.valueOf(count++), StringUtils.isNotBlank(feedback.getSearchTerm()) ? feedback.getSearchTerm()
						: "");


			}
			else
			{
				lineData.put(Integer.valueOf(count++), "");
			}

			// FeedBack for Search Category

			if (feedback.getFeedbackComment() != null)
			{
				lineData.put(Integer.valueOf(count++),
						StringUtils.isNotBlank(feedback.getSearchCategory()) ? feedback.getSearchCategory() : "");


			}
			else
			{
				lineData.put(Integer.valueOf(count++), "");
			}
			target.add(lineData);
		}
		return target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.dto.converter.Converter#convert(java.lang.Object, java.lang.Object)
	 */
	@Override
	public List<Map<Integer, String>> convert(final List<FeedBackStoringModel> source, final List<Map<Integer, String>> target)
			throws ConversionException
	{
		// YTODO Auto-generated method stub
		return null;
	}




}
