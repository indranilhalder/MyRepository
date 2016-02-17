package com.tisl.mpl.cockpits.cscockpit.editors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zk.ui.HtmlBasedComponent;

import com.tisl.mpl.marketplacecommerceservices.daos.AccountAddressDao;
import com.tisl.mpl.model.StateModel;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.DefaultSelectUIEditor;
import de.hybris.platform.core.Registry;

public class MarketPlaceStateListEditor extends DefaultSelectUIEditor {
	
	
	private AccountAddressDao accountAddressDao;

	@Override
	public HtmlBasedComponent createViewComponent(final Object initialValue,
			final Map<String, ? extends Object> parameters,
			final EditorListener listener) {
		super.setAvailableValues(getAllStates());
		return super.createViewComponent(initialValue, parameters, listener);
	}
	
	private List<String> getAllStates(){
		accountAddressDao=(AccountAddressDao) Registry.getApplicationContext().getBean("accountAddressDao");
		List<StateModel> states= accountAddressDao.getStates();
		if(CollectionUtils.isNotEmpty(states)){
			List<String> results=new ArrayList<>();
			for(StateModel state:states){
				results.add(state.getDescription());
			}
			return results;
		}
		return Collections.EMPTY_LIST;
	}

}
