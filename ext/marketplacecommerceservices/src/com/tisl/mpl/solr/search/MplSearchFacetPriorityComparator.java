/**
 *
 */
package com.tisl.mpl.solr.search;

import java.util.Comparator;

import com.tisl.mpl.wsdto.FacetDataWsDTO;


/**
 * @author TCS
 *
 */
public class MplSearchFacetPriorityComparator
{
	FacetDataWsDTO facetDataWsDTO;
	int priority;


	public static final Comparator<MplSearchFacetPriorityComparator> searchFacetByPriority = new Comparator<MplSearchFacetPriorityComparator>()
	{

		@Override
		public int compare(final MplSearchFacetPriorityComparator facet1, final MplSearchFacetPriorityComparator facet2)
		{
			final int rank1 = facet1.getPriority();
			final int rank2 = facet2.getPriority();

			if (rank1 == rank2)
			{
				return 0;
			}
			else if (rank1 < rank2)
			{
				return 1;
			}
			else
			{
				return -1;
			}

		}
	};


	/**
	 * @return the facetDataWsDTO
	 */
	public FacetDataWsDTO getFacetDataWsDTO()
	{
		return facetDataWsDTO;
	}


	/**
	 * @param facetDataWsDTO
	 *           the facetDataWsDTO to set
	 */
	public void setFacetDataWsDTO(final FacetDataWsDTO facetDataWsDTO)
	{
		this.facetDataWsDTO = facetDataWsDTO;
	}


	/**
	 * @return the priority
	 */
	public int getPriority()
	{
		return priority;
	}


	/**
	 * @param priority
	 *           the priority to set
	 */
	public void setPriority(final int priority)
	{
		this.priority = priority;
	}
}
