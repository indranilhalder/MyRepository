/**
 *
 */
package com.tisl.mpl.search.feedback.facades;

/**
 * @author TCS
 *
 */
public interface UpdateFeedbackFacade
{
	/**
	 * @param email
	 * @param comment
	 * @param name
	 * @param category
	 * @return
	 */
	String updateFeedbackNo(String comment, String category, String customerEmail, String searchCategory, String searchText);

	/**
	 * @param email
	 * @param comment
	 * @param name
	 */
	String updateFeedbackYes(String email, String comment, String name);
}
