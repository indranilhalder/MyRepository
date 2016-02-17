package com.tisl.mpl.cockpits.cscockpit.services;
 
 /*    */ 
	import java.util.Collections;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Map;
 import java.util.Set;

 	import org.apache.commons.lang.ObjectUtils;

	import de.hybris.platform.core.model.ItemModel;
 













 public class ItemModificationInfo
 {
   private final ItemModel model;
   private final Map<String, Object> originalValues = new HashMap();
   private final Map<String, Object> modifiedValues = new HashMap();
   private final Set<String> localizedAttributes = new HashSet();
   private boolean newFlag = false;

   public ItemModificationInfo(ItemModel model)
   {
     this.model = model;
   }

   public void addEntry(String attribute, boolean localized, Object originalValue, Object modifiedValue)
   {
     if (valuesEqual(originalValue, modifiedValue))
       return;
     this.originalValues.put(attribute, originalValue);
     this.modifiedValues.put(attribute, modifiedValue);
     if (!(localized))
       return;
     this.localizedAttributes.add(attribute);
   }



   public ItemModel getModel()
   {
     return this.model;
   }

   public Set<String> getModifiedAttributes()
   {
     return Collections.unmodifiableSet(this.originalValues.keySet());
   }

   public Object getOriginalValue(String attribute)
   {
     return this.originalValues.get(attribute);
   }

   public Object getModifiedValue(String attribute)
   {
     return this.modifiedValues.get(attribute);
   }

   public boolean isLocalized(String attribute)
   {
     return this.localizedAttributes.contains(attribute);
   }

   public boolean isNew()
   {
     return this.newFlag;
   }

   public void setNew(boolean isModelNew)
   {
     this.newFlag = isModelNew;
   }

   protected boolean valuesEqual(Object value1, Object value2)
   {
     return ObjectUtils.equals(value1, value2);
   }
 }
