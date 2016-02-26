/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.regioncache.region.CacheRegion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tisl.mpl.facade.brand.BrandFacade;
import com.tisl.mpl.facade.brand.impl.BrandComponentCacheKey;
import com.tisl.mpl.facade.brand.impl.BrandComponentCacheValueLoader;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author 354634
 *
 */
@Controller
@Scope("tenant")
@RequestMapping(value = RequestMappingUrlConstants.A_Z_BRANDS)
public class AtoZBrandController
{
	private static final Logger LOG = Logger.getLogger(AtoZBrandController.class);

	@Resource(name = "brandFacade")
	private BrandFacade brandFacade;

	private static final String CODE = "MBH1";

	@Resource(name = "brandCompCacheRegion")
	private CacheRegion brandCompCacheRegion;

	@Resource(name = "brandCompCacheValueLoader")
	private BrandComponentCacheValueLoader brandCompCacheValueLoader;


	@RequestMapping(method = RequestMethod.GET)
	public String get(final Model model)
	{

		Map<Character, List<CategoryModel>> sortedMap = null;

		final int groupSize = 5;
		final Map<Character, Character> groupedAlphabets = new HashMap();

		//Create group range of alphabets like A-G,H-N

		final char[] alpha = new char[26];
		int alphaCount = 0;

		for (int index = 0; index < 26; index++)
		{
			alpha[index] = (char) (65 + (alphaCount++));
		}


		char startAlphabet = 0;
		char endAlphabet;
		int count = 1;
		for (int index = 0; index < 26; index++)
		{

			if (count == 1)
			{
				startAlphabet = alpha[index];

			}

			if (count == groupSize || index > 20)
			{
				endAlphabet = alpha[index];

				groupedAlphabets.put(startAlphabet, endAlphabet);
				if (count == groupSize)
				{
					count = 1;
				}
			}
			else
			{
				count++;
			}

		}
		try
		{
			final BrandComponentCacheKey key = new BrandComponentCacheKey(CODE);

			sortedMap = (Map<Character, List<CategoryModel>>) brandCompCacheRegion.getWithLoader(key, brandCompCacheValueLoader);

			if (null == sortedMap)
			{
				sortedMap = brandFacade.getAllBrandsInAplhabeticalOrder(CODE);
			}
			//sortedMap = brandFacade.getAllBrandsInAplhabeticalOrder(CODE);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());

		}



		//Sorted the map based on the keys


		Map<Character, List<CategoryModel>> GroupBrandsAToE = new TreeMap<Character, List<CategoryModel>>();
		Map<Character, List<CategoryModel>> GroupBrandsFToJ = new TreeMap<Character, List<CategoryModel>>();
		Map<Character, List<CategoryModel>> GroupBrandsKToO = new TreeMap<Character, List<CategoryModel>>();
		Map<Character, List<CategoryModel>> GroupBrandsPToT = new TreeMap<Character, List<CategoryModel>>();
		Map<Character, List<CategoryModel>> GroupBrandsUToZ = new TreeMap<Character, List<CategoryModel>>();

		GroupBrandsAToE = getBrandsForRange('A', 'E', sortedMap);
		GroupBrandsFToJ = getBrandsForRange('F', 'J', sortedMap);
		GroupBrandsKToO = getBrandsForRange('K', 'O', sortedMap);
		GroupBrandsPToT = getBrandsForRange('P', 'T', sortedMap);
		GroupBrandsUToZ = getBrandsForRange('U', 'Z', sortedMap);


		model.addAttribute(ModelAttributetConstants.A_E_Brands, GroupBrandsAToE);
		model.addAttribute(ModelAttributetConstants.F_J_Brands, GroupBrandsFToJ);
		model.addAttribute(ModelAttributetConstants.K_O_Brands, GroupBrandsKToO);
		model.addAttribute(ModelAttributetConstants.P_T_Brands, GroupBrandsPToT);
		model.addAttribute(ModelAttributetConstants.U_Z_Brands, GroupBrandsUToZ);






		//if (GroupBrandsAToE.size() == 0 || GroupBrandsFToJ.size() == 0)
		if (MapUtils.isEmpty(GroupBrandsAToE) || MapUtils.isEmpty(GroupBrandsFToJ))
		{

			startAlphabet = 'A';
			endAlphabet = 'J';
			groupedAlphabets.put(startAlphabet, endAlphabet);
			final Map<Character, List<CategoryModel>> GroupBrandsAToJ = getBrandsForRange('A', 'J', sortedMap);
			model.addAttribute(ModelAttributetConstants.A_J_Brands, GroupBrandsAToJ);

			groupedAlphabets.remove('F');



		}


		//else if (GroupBrandsKToO.size() == 0)
		else if (MapUtils.isEmpty(GroupBrandsKToO))
		{

			startAlphabet = 'F';
			endAlphabet = 'O';
			groupedAlphabets.put(startAlphabet, endAlphabet);
			final Map<Character, List<CategoryModel>> GroupBrandsFToO = getBrandsForRange('F', 'O', sortedMap);
			model.addAttribute(ModelAttributetConstants.F_O_Brands, GroupBrandsFToO);

			groupedAlphabets.remove('K');

		}

		//else if (GroupBrandsPToT.size() == 0)
		else if (MapUtils.isEmpty(GroupBrandsPToT))
		{

			startAlphabet = 'K';
			endAlphabet = 'T';
			groupedAlphabets.put(startAlphabet, endAlphabet);
			final Map<Character, List<CategoryModel>> GroupBrandsKToT = getBrandsForRange('K', 'T', sortedMap);
			model.addAttribute(ModelAttributetConstants.K_T_Brands, GroupBrandsKToT);

			groupedAlphabets.remove('P');

		}

		//else if (GroupBrandsUToZ.size() == 0)
		else if (MapUtils.isEmpty(GroupBrandsUToZ))
		{

			startAlphabet = 'P';
			endAlphabet = 'Z';
			groupedAlphabets.put(startAlphabet, endAlphabet);
			final Map<Character, List<CategoryModel>> GroupBrandsPToZ = getBrandsForRange('P', 'Z', sortedMap);

			model.addAttribute(ModelAttributetConstants.P_Z_Brands, GroupBrandsPToZ);

			groupedAlphabets.remove('U');

		}

		//Sorted the map based on the keys
		final Map<Character, Character> sortedGroupedAlphabetsMap = new TreeMap<Character, Character>(groupedAlphabets);
		model.addAttribute(ModelAttributetConstants.GROUPED_ALPHABETS, sortedGroupedAlphabetsMap);

		return ControllerConstants.Views.Fragments.Home.AtoZBrand;
	}

	/**
	 * This method returns the list of brands for a particular range of characters like A-G or H-N
	 *
	 * @param startCharacter
	 * @param endCharacter
	 * @return Map<String, List<CMSSubbrandModel>>
	 */
	@SuppressWarnings("boxing")
	private Map<Character, List<CategoryModel>> getBrandsForRange(final Character startCharacter, final Character endCharacter,
			final Map<Character, List<CategoryModel>> sortedMap)
	{
		final Map<Character, List<CategoryModel>> brandsByRange = new HashMap();

		for (final Entry<Character, List<CategoryModel>> entry : sortedMap.entrySet())
		{ //ASCII Value of entry key
			final int entryKeyCharacterASCII = entry.getKey();

			//ASCII value of startCharacter
			final int startCharacterASCII = startCharacter;

			//ASCII value of endCharacter
			final int endCharacterASCII = endCharacter;

			if (entryKeyCharacterASCII >= startCharacterASCII && entryKeyCharacterASCII <= endCharacterASCII)
			{
				brandsByRange.put(entry.getKey(), entry.getValue());
			}

		}

		return new TreeMap<Character, List<CategoryModel>>(brandsByRange);
	}
}
