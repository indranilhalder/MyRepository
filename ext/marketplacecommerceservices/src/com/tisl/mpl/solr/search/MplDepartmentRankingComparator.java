/**
 *
 */
package com.tisl.mpl.solr.search;

import java.util.Comparator;


/**
 * @author 360641
 *
 */
public class MplDepartmentRankingComparator
{
	String departmentHierarchy;
	int rankingValue;
	int count;


	/* Comparator for sorting the department hierarchy by ranking */
	public static Comparator<MplDepartmentRankingComparator> departmentRankingByRank = new Comparator<MplDepartmentRankingComparator>()
	{

		@Override
		public int compare(final MplDepartmentRankingComparator departmentRanking1,
				final MplDepartmentRankingComparator departmentRanking2)
		{

			final int rank1 = departmentRanking1.getRankingValue();
			final int rank2 = departmentRanking2.getRankingValue();

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

	/* Comparator for sorting the department hierarchy by count */
	public static Comparator<MplDepartmentRankingComparator> departmentRankingByCount = new Comparator<MplDepartmentRankingComparator>()
	{

		@Override
		public int compare(final MplDepartmentRankingComparator departmentRanking1,
				final MplDepartmentRankingComparator departmentRanking2)
		{

			final int count1 = departmentRanking1.getCount();
			final int count2 = departmentRanking2.getCount();

			if (count1 == count2)
			{
				return 0;
			}
			else if (count1 < count2)
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
	 * @return the departmentHierarchy
	 */
	public String getDepartmentHierarchy()
	{
		return departmentHierarchy;
	}

	/**
	 * @param departmentHierarchy
	 *           the departmentHierarchy to set
	 */
	public void setDepartmentHierarchy(final String departmentHierarchy)
	{
		this.departmentHierarchy = departmentHierarchy;
	}

	/**
	 * @return the rankingValue
	 */
	public int getRankingValue()
	{
		return rankingValue;
	}

	/**
	 * @param rankingValue
	 *           the rankingValue to set
	 */
	public void setRankingValue(final int rankingValue)
	{
		this.rankingValue = rankingValue;
	}

	/**
	 * @return the count
	 */
	public int getCount()
	{
		return count;
	}

	/**
	 * @param count
	 *           the count to set
	 */
	public void setCount(final int count)
	{
		this.count = count;
	}
}