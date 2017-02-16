/**
 * 
 */
package com.hybris.yps.nodewarmer;

/**
 * @author brendan.dobbs
 * 
 */
public class NodeStartupStateHolder
{
	public enum StartupState
	{
		STARTING, FAILED, STARTED
	}

	private static NodeStartupStateHolder state;
	private StartupState startupState = StartupState.STARTING;

	/**
	 * @return the startupState
	 */
	public StartupState getStartupState()
	{
		return startupState;
	}

	public static NodeStartupStateHolder getInstance()
	{
		if (state == null)
		{
			synchronized (NodeStartupStateHolder.class)
			{
				if (state == null)
				{
					state = new NodeStartupStateHolder();
				}
			}
		}
		return state;
	}

	/**
	 * @param startupState
	 *           the startupState to set
	 */
	public void setStartupState(final StartupState startupState)
	{
		this.startupState = startupState;
	}
}
