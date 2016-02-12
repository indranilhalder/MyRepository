package com.tisl.mpl.cockpits.cscockpit.services;




import java.util.List;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.hmc.model.SavedValuesModel;

public  interface ItemModificationHistoryService
{
  public abstract ItemModificationInfo createModificationInfo(ItemModel paramItemModel);

  public abstract void logItemModification(ItemModificationInfo paramItemModificationInfo);

  public abstract List<SavedValuesModel> getSavedValues(ItemModel paramItemModel);
}
