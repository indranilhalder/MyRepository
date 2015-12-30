package com.tisl.mpl.core.search.provider.helper;


/**
 * The Interface MplCacheMBean.
 */
/**
 * @author TCS
 *
 */
//This is a Standard MBean interface, every method in this interface defines either an attribute or an operation in the MplCache
public interface MplCacheMBean
{

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	long getSize();

	/**
	 * Gets the hits.
	 *
	 * @return the hits
	 */
	long getHits();

	/**
	 * Gets the misses.
	 *
	 * @return the misses
	 */
	long getMisses();

	/**
	 * Clear.
	 */
	void clear();
}
