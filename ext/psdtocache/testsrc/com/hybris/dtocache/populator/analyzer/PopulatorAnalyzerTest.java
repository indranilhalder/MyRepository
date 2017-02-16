/**
 *
 */
package com.hybris.dtocache.populator.analyzer;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.hybris.dtocache.exception.DtoCacheException;
import com.hybris.dtocache.exception.DtoCacheRuntimeException;
import com.hybris.dtocache.populator.AnotherTestPopulator;
import com.hybris.dtocache.populator.CustomTestPopulator;

import de.hybris.platform.core.model.ItemModel;


/**
 * 
 * @author giovanni.ganassin@sap.com
 * Jan 19, 2016 10:52:04 AM
 *
 */
public class PopulatorAnalyzerTest
{
	public static int CALL_COUNT = 0;

	@Test
	public void test1() throws DtoCacheException {
		try
		{
			PopulatorAnalyzer.analyse(this.getClass().getName());
			fail("should throw a com.hybris.dtocache.exception.DtoCacheException");			
		} catch (DtoCacheException e)
		{
			assertEquals(true, true);
		}
	}

	@Test
	public void test2() throws DtoCacheException {
		try
		{
			Method m = PopulatorAnalyzer.getPopulateMethod(AnotherTestPopulator.class.getName());
		} catch (DtoCacheException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void test3() throws DtoCacheException {
		List<String> props = new ArrayList<String>();
		try
		{
			props = PopulatorAnalyzer.analyse(AnotherTestPopulator.class.getName());
			System.out.println(props);
		} catch (DtoCacheException e)
		{
			fail(e.getMessage());
		}

		assertEquals(6, props.size());
	}

	@Test
	public void test4() throws DtoCacheException {
		try
		{
			PopulatorAnalyzer.analyse("giovanni.unexisting.class");
			fail("should throw a com.hybris.dtocache.exception.DtoCacheException");
		} catch (DtoCacheException e)
		{
			//NOOP
			assertEquals(true, true);
		}
	}

	@Test
	public void test5() throws DtoCacheException {
		Method m = PopulatorAnalyzer.getPopulateMethod(CustomTestPopulator.class.getName());
		assertNotNull("should not be null", m);
		final Class<? extends ItemModel> itemModelClass = PopulatorAnalyzer.getItemModelClassFromPopulateMethod(m);
		assertNotNull("should not be null", itemModelClass);
		try
		{
			PopulatorAnalyzer.scanForPossibleChildren(itemModelClass);
			fail("it should have failed due to parameter types missmatch");
		} catch (DtoCacheRuntimeException e)
		{
			assertEquals(true, true);
		}
	}


	@Test
	public void test6() throws DtoCacheException {
		try
		{
			PopulatorAnalyzer.analyse(CustomTestPopulator.class.getName(), true);
			fail("should throw a DtoCacheRuntimeException because it checks for ItemModel class conflicts");	
		} catch (DtoCacheRuntimeException e)
		{
			assertEquals(true, true);
		}
	}
	
}
