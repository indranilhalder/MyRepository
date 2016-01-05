package com.tisl.mpl.cockpits.cscockpit.services.impl;



import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.services.search.generic.query.AbstractCsFlexibleSearchQueryBuilder;
import de.hybris.platform.cscockpit.services.search.impl.DefaultCsTextSearchCommand;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

public class MarketplaceCustomerSearchQueryBuilder extends AbstractCsFlexibleSearchQueryBuilder<DefaultCsTextSearchCommand> {

	protected static final Logger LOG = Logger.getLogger(MarketplaceCustomerSearchQueryBuilder.class);

	/**
	 * The Enum TextFields.
	 */
	public enum TextFields {

		/** The First name. */
		FirstName,

		/** The Last name. */
		LastName,

		/** The Email. */
		Email,

		/** The Phone Number. */
		Phone,




	}



	@Override
	protected FlexibleSearchQuery buildFlexibleSearchQuery(final DefaultCsTextSearchCommand command)
	{
		final String firstName = command.getText(TextFields.FirstName);
		final String lastName = command.getText(TextFields.LastName);
		final String email = command.getText(TextFields.Email);
		final String phone = command.getText(TextFields.Phone);


		final StringBuilder query = new StringBuilder(300);

		// Select the PK, FirstName, LastName, Email, DateOfBirth and UID. LastName, FirstName and UID are sort columns
		query.append("SELECT  {c:" + CustomerModel.PK + "} ");

		// from the customer table
		query.append("FROM {" + CustomerModel._TYPECODE + " AS c } " );

		query.append("WHERE ({c:" + CustomerModel.NAME + "}!='anonymous' OR {c:" + CustomerModel.NAME + "} IS NULL) ");

		if (StringUtils.isNotEmpty(firstName))
		{
			if (isCaseInsensitive())
			{
				// Search the customer firstname field case-insensitively
				query.append("AND LOWER({c:" + CustomerModel.FIRSTNAME + "}) like LOWER(?firstName) ");
			}
			else
			{
				// Search the customer firstname field case-sensitively
				query.append("AND {c:" + CustomerModel.FIRSTNAME + "} like ?firstName ");
			}
		}

		if (StringUtils.isNotEmpty(lastName))
		{
			if (isCaseInsensitive())
			{
				// Search the joined addresses lastname fields case-insensitively
				query.append("AND LOWER({c:" + CustomerModel.LASTNAME + "}) like LOWER(?lastName) ");
			}
			else
			{
				// Search the joined addresses lastname fields case-sensitively
				query.append("AND {c:" + CustomerModel.LASTNAME + "} like ?lastName ");
			}
		}
		if (StringUtils.isNotEmpty(email))
		{
			if (isCaseInsensitive())
			{
				// Search the customer email fields case-insensitively
				query.append("AND LOWER({c:" + CustomerModel.ORIGINALUID + "}) like LOWER(?email) ");
			}
			else
			{
				// Search the customer email fields case-insensitively
				query.append("AND {c:" + CustomerModel.ORIGINALUID + "} like ?email ");
			}
		}

		if (StringUtils.isNotEmpty(phone))
		{
			query.append("AND {c:" + CustomerModel.MOBILENUMBER + "} like ?phone ");
		}

		query.append(" ORDER BY LOWER({c:" + CustomerModel.FIRSTNAME + "}) ");


		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());
		
		
		// Add the search parameters


		if (StringUtils.isNotEmpty(firstName) || StringUtils.isNotEmpty(lastName)|| StringUtils.isNotEmpty(email)|| StringUtils.isNotEmpty(phone)) {
		if (StringUtils.isNotEmpty(firstName))
		{
			searchQuery.addQueryParameter("firstName", "%" + firstName.trim() + "%");
		}
		if (StringUtils.isNotEmpty(lastName))
		{
			searchQuery.addQueryParameter("lastName", "%" + lastName.trim() + "%");
		}
		if (StringUtils.isNotEmpty(email))
		{
			searchQuery.addQueryParameter("email", "%" + email.trim() + "%");
		}
		if (StringUtils.isNotEmpty(phone))
		{
			searchQuery.addQueryParameter("phone", "%" + phone.trim() + "%");
		}

		return searchQuery;
		}
		return null;
	}
}
