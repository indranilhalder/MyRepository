/**
 *
 */
package com.tisl.mpl.storefront.web.forms;

import de.hybris.platform.category.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;


/**
 * @author TCS
 *
 */
public class MplPreferenceForm
{
	private String myInterest;
	private List<String> selectedCategory = new ArrayList<String>();
	private List<String> selectedBrand = new ArrayList<String>();

	private List<CategoryModel> selectedCategory1 = new ArrayList<CategoryModel>();
	private List<CategoryModel> selectedBrand1 = new ArrayList<CategoryModel>();

	private String selectedFrequency;
	private List<String> selectedfeedbackArea = new ArrayList<String>();

	/**
	 * @return the myInterest
	 */
	public String getMyInterest()
	{
		return myInterest;
	}

	/**
	 * @param myInterest
	 *           the myInterest to set
	 */
	public void setMyInterest(final String myInterest)
	{
		this.myInterest = myInterest;
	}

	/**
	 * @return the selectedCategory
	 */
	public List<String> getSelectedCategory()
	{
		return selectedCategory;
	}

	/**
	 * @param selectedCategory
	 *           the selectedCategory to set
	 */
	public void setSelectedCategory(final List<String> selectedCategory)
	{
		this.selectedCategory = selectedCategory;
	}

	/**
	 * @return the selectedBrand
	 */
	public List<String> getSelectedBrand()
	{
		return selectedBrand;
	}

	/**
	 * @param selectedBrand
	 *           the selectedBrand to set
	 */
	public void setSelectedBrand(final List<String> selectedBrand)
	{
		this.selectedBrand = selectedBrand;
	}

	/**
	 * @return the selectedFrequency
	 */
	public String getSelectedFrequency()
	{
		return selectedFrequency;
	}

	/**
	 * @param selectedFrequency
	 *           the selectedFrequency to set
	 */
	public void setSelectedFrequency(final String selectedFrequency)
	{
		this.selectedFrequency = selectedFrequency;
	}

	/**
	 * @return the selectedfeedbackArea
	 */
	public List<String> getSelectedfeedbackArea()
	{
		return selectedfeedbackArea;
	}

	/**
	 * @param selectedfeedbackArea
	 *           the selectedfeedbackArea to set
	 */
	public void setSelectedfeedbackArea(final List<String> selectedfeedbackArea)
	{
		this.selectedfeedbackArea = selectedfeedbackArea;
	}

	/**
	 * @param selectedBrand1
	 *           the selectedBrand1 to set
	 */
	public void setSelectedBrand1(final List<CategoryModel> selectedBrand1)
	{
		this.selectedBrand1 = selectedBrand1;
	}

	/**
	 * @return the selectedBrand1
	 */
	public List<CategoryModel> getSelectedBrand1()
	{
		return selectedBrand1;
	}

	/**
	 * @param selectedCategory1
	 *           the selectedCategory1 to set
	 */
	public void setSelectedCategory1(final List<CategoryModel> selectedCategory1)
	{
		this.selectedCategory1 = selectedCategory1;
	}

	/**
	 * @return the selectedCategory1
	 */
	public List<CategoryModel> getSelectedCategory1()
	{
		return selectedCategory1;
	}



}
