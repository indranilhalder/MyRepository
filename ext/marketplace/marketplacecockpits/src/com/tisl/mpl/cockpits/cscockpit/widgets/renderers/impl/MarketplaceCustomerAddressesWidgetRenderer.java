package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.impl.PropertyColumnConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.impl.DefaultListboxWidget;
import de.hybris.platform.cockpit.widgets.models.ListWidgetModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cscockpit.utils.CockpitUiConfigLoader;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.ObjectGetValueUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CustomerController;
import de.hybris.platform.cscockpit.widgets.controllers.dispatcher.ItemRemovalAppender;
import de.hybris.platform.cscockpit.widgets.models.impl.CustomerAddressesListWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.CustomerAddressesWidgetRenderer;

public class MarketplaceCustomerAddressesWidgetRenderer<E> extends CustomerAddressesWidgetRenderer {

	private static final Logger LOG = Logger.getLogger(MarketplaceCustomerAddressesWidgetRenderer.class);
    private ItemRemovalAppender<TypedObject> itemRemovalAppender;
	
    public ItemRemovalAppender<TypedObject> getItemRemovalAppender() {
		return itemRemovalAppender;
	}

	public void setItemRemovalAppender(
			ItemRemovalAppender<TypedObject> itemRemovalAppender) {
		this.itemRemovalAppender = itemRemovalAppender;
	}

	@Override
    protected HtmlBasedComponent createContentInternal(
            final DefaultListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget,
            final HtmlBasedComponent rootContainer){
        //copied from the 'grandparent class' instead of super.createContentInternal(widget, rootContainer) which was calling the method we have override
        
		return super.createContentInternal(widget, rootContainer);
		/*final Div container = new Div();

        final Listbox listBox = new Listbox();
        listBox.setParent(container);
        widget.setListBox(listBox);
        listBox.setVflex(false);
        listBox.setFixedLayout(true);
         
        //To create the headers and the textboxes
        renderListbox(listBox, widget, rootContainer);
        
         * Start of the code to get the header values correct - Street Number, Street Name, Action
         
        Collection headerList = listBox.getHeads();
        if (headerList.iterator().hasNext()) {
        	Listhead listHead = (Listhead) headerList.iterator().next();
            
            Collection headers = listHead.getChildren();

            for (Iterator iterator = headers.iterator(); iterator.hasNext();) {
            	Listheader header = (Listheader) iterator.next();
// The Action cell containing the delete button - delete address functionality cscockpit           	
            	if (StringUtils.isEmpty(header.getLabel())) {
           		header.setLabel("Action");
           		header.setWidth("80px");
           	}
            	
            	if (header.getLabel().equals("Line 1")) {
            		header.setLabel("Address Line 1");
            	}
            	
            	if (header.getLabel().equals("Line 2")) {
            		header.setLabel("Address Line 2");
            	}
            	if (header.getLabel().equals("[Address.addressLine3]")) {
            		header.setLabel("Address Line 3");
            	}
				if (header.getLabel().equals("Phone1")) {
            		header.setLabel("Mobile Number");
            	}
            }
        }
        
        
         * End of the code
         
        
        
        
        if (isLazyLoadingEnabled())
        {
            UITools.applyLazyload(listBox);
        }

        if (listBox.getItemCount() > 0 && listBox.getSelectedIndex() <= 0)
        {
            listBox.setSelectedIndex(0);
        }
        //end of super code

        final CustomerModel customerModel = getCustomer(widget);
        if (customerModel != null)
        {
            final CockpitEventAcceptor addressNotificationEventAcceptor = createAddressNotificationEventAcceptor(widget);
            final OrderedConfigurableBrowserArea browserArea = (OrderedConfigurableBrowserArea) UISessionUtils.getCurrentSession()
                    .getCurrentPerspective().getBrowserArea();
            browserArea.addNotificationListener(widget.getWidgetCode(), addressNotificationEventAcceptor);
            
            AddressModel defaultShipmentAddress = customerModel
					.getDefaultShipmentAddress();

			Div selectDefaultShipmentAddressContent = new Div();
			selectDefaultShipmentAddressContent
					.setSclass("defaultShipmentAddressBox");
			selectDefaultShipmentAddressContent.setParent(container);

			Label defaultShipmentAddressLabel = new Label(LabelUtils.getLabel(
					widget, "defaultShipmentAddress", new Object[0]));
			defaultShipmentAddressLabel
					.setParent(selectDefaultShipmentAddressContent);

			Listbox defaultShipmentAddressDropdown = new Listbox();
			defaultShipmentAddressDropdown
					.setParent(selectDefaultShipmentAddressContent);
			defaultShipmentAddressDropdown.setMold("select");
			defaultShipmentAddressDropdown.setRows(1);

			if (defaultShipmentAddress == null) {
				Listitem listItem = new Listitem("", null);
				listItem.setParent(defaultShipmentAddressDropdown);
				listItem.setSelected(true);
			}

			for (int i = 0; i < ((CustomerAddressesListWidgetModel) widget
					.getWidgetModel()).getItems().size(); ++i) {
				TypedObject address = (TypedObject) ((CustomerAddressesListWidgetModel) widget
						.getWidgetModel()).getItems().get(i);
				AddressModel addressModel = (AddressModel) address.getObject();
				String label = getAddressModelLabelProvider().getLabel(address);

				Listitem listItem = new Listitem(label, address);
				listItem.setParent(defaultShipmentAddressDropdown);

				listItem.setSelected((defaultShipmentAddress != null)
						&& (defaultShipmentAddress.getPk().equals(addressModel
								.getPk())));
			}

			defaultShipmentAddressDropdown.addEventListener("onSelect",
					createDefaultShipmentAddressChangedEventListener(widget));
        }
        else
        {
            final Div noResultsContainer = new Div();
            noResultsContainer.setParent(container);
            final Label noResults = new Label(LabelUtils.getLabel(widget, "noResults"));
            noResults.setParent(noResultsContainer);
        }

        Div addNewAddressContainer = new Div();
		addNewAddressContainer.setParent(container);
		Button addNewAddressButton = new Button(LabelUtils.getLabel(widget,
				"addBtn", new Object[0]));
		addNewAddressContainer.setClass("createNewAddress");
		addNewAddressButton.setParent(addNewAddressContainer);
		addNewAddressButton.addEventListener("onClick", onClickAddEventListener(
				widget, container)); 
        return container;
*/    }
    
    private EventListener onClickAddEventListener(
			DefaultListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget,
			Div container) {
		// TODO Auto-generated method stub
		return new MarketplaceOnClickAddEventListener(widget,container);
	}
    
    protected class MarketplaceOnClickAddEventListener implements EventListener {

    	private DefaultListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget;
		private Div container;
		public MarketplaceOnClickAddEventListener(
				DefaultListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget,
				Div container) {
			// TODO Auto-generated constructor stub
			this.widget=widget;
			this.container=container;
		}

		@Override
		public void onEvent(Event paramEvent) throws Exception {
			// TODO Auto-generated method stub
			handleAddClickEvent(widget,
					container);
			
		}
		
		/**
		 * It creates the pop up for address creation
		 * @param widget
		 * @param container
		 * @throws Exception
		 */
		protected void handleAddClickEvent(DefaultListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget,
				HtmlBasedComponent container)
				throws Exception {
			getPopupWidgetHelper().createPopupWidget(
					container,
					getAddressCreateWidgetConfigCode(),
					"csAddressCreateWidgetConfig-Popup",
					"csAddressCreatePopup",
					LabelUtils.getLabel(widget, "popup.addressCreateTitle",
							new Object[0]));
		}
    	
    }
    
    protected String getAddressCreateWidgetConfigCode() {
		return "csShippingAddressCreateWidgetConfig";
	}
    
    protected EventListener createDefaultShipmentAddressChangedEventListener(
			ListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget) {
		return new DefaultShipmentAddressChangedEventListener(widget);
	}

	/**
	 * It sets the default shipping address
	 * @param widget
	 * @param selectEvent
	 */
	protected void handleDefaultShipmentAddressChangedEvent(
			ListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget,
			SelectEvent selectEvent) {
		Set selectedItems = selectEvent.getSelectedItems();
		Listitem selectedItem = null;
		if ((selectedItems != null) && (!(selectedItems.isEmpty()))) {
			selectedItem = (Listitem) selectedItems.iterator().next();
		}

		if (selectedItem == null) {
			return;
		}
		boolean changed = ((CustomerController) widget.getWidgetController())
				.setDefaultShipmentAddress((TypedObject) selectedItem
						.getValue());
		if (!(changed)) {
			return;
		}
		((CustomerAddressesListWidgetModel) widget.getWidgetModel())
				.notifyListeners();
	}

	

	@Override
	protected void renderListbox(final Listbox listBox, final DefaultListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget, final HtmlBasedComponent rootContainer)
	{
		final ListWidgetModel<TypedObject> widgetModel = widget.getWidgetModel();
		if (widgetModel != null)
		{
			final List<TypedObject> items = widgetModel.getItems();
			if (CollectionUtils.isNotEmpty(items))
			{
				try
				{
					final List<ColumnConfiguration> columns = getColumnConfigurations();
					if (CollectionUtils.isNotEmpty(columns))
					{
						final Listhead headRow = new Listhead();
						headRow.setParent(listBox);

						// create proper headers
						populateHeaderRow(widget, headRow, columns);

						// add delete button header
						final Listheader colHeader = new Listheader("");
						colHeader.setWidth("65px");
						colHeader.setParent(headRow);

						final EditorConfiguration editorConf = CockpitUiConfigLoader.getEditorConfiguration(
								UISessionUtils.getCurrentSession(), getListEditorConfigurationCode(), getListConfigurationType());

						final List<EditorRowConfiguration> editorMapping = getPropertyEditorHelper().getAllEditorRowConfigurations(
								editorConf);

						//Processing Default shipping address
						
						for (final TypedObject item : items)
						{
							AddressModel address = (AddressModel) item.getObject();
							if(address != null)
							{
									createAddressRow(listBox,
											widget, columns, editorMapping, item, colHeader);
																		
							}
							
						}
						
						/*IT2379ADA - end*/
					}
				}
				catch (final Exception e)
				{
					LOG.error("failed to render address list", e);
				}
			}
		}
	}

	private EventListener onClickEventListener(
			DefaultListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget,
			TypedObject item) {
		// TODO Auto-generated method stub
		return new MarketplaceOnClickEventListener(widget,item);
	}
	
	protected class MarketplaceOnClickEventListener implements EventListener {
		private DefaultListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget;
	    private TypedObject item;
	    private Event event;
		public MarketplaceOnClickEventListener(
				DefaultListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget,
				TypedObject item) {
			// TODO Auto-generated constructor stub
			this.widget=widget;
			this.item=item;
		}
		@Override
		public void onEvent(Event paramEvent) throws Exception {
			// TODO Auto-generated method stub
			MarketplaceCustomerAddressesWidgetRenderer.this
						.handleDeleteClickEvent(this.widget, event,
								this.item);
			
		}
			/**
			 * It deletes the address row
			 * @param widget
			 * @param event
			 * @param item
			 * @throws Exception
			 */
			protected void handleDeleteClickEvent(Widget widget, Event event, TypedObject item)
					throws Exception {
				if (Messagebox.show(LabelUtils.getLabel(widget, "deleteConfirm.text",
						new Object[0]), LabelUtils.getLabel(widget,
						"deleteConfirm.title", new Object[0]), 48,
						"z-msgbox z-msgbox-question") != 16) {
					return;
				}
				getItemRemovalAppender().remove((TypedObject) item);
			}
			
		
	}

	/**
	 * It creates the address row
	 * @param listBox
	 * @param widget
	 * @param columns
	 * @param editorMapping
	 * @param item
	 * @param colHeader 
	 * @return
	 */
	private Listitem createAddressRow(
			final Listbox listBox,
			final DefaultListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget,
			final List<ColumnConfiguration> columns,
			final List<EditorRowConfiguration> editorMapping,
			final TypedObject item, Listheader colHeader) {
		
		final Listitem row = new Listitem();
		row.setSclass(CSS_LISTBOX_ROW_ITEM);
		row.setParent(listBox);

		for (final ColumnConfiguration column : columns)
		{
			if (column instanceof PropertyColumnConfiguration)
			{
				final PropertyColumnConfiguration col = (PropertyColumnConfiguration) column;
				final PropertyDescriptor propertyDescriptor = col.getPropertyDescriptor();
				final EditorRowConfiguration editorRowConfiguration = getPropertyEditorHelper()
						.findConfigurationByPropertyDescriptor(editorMapping, propertyDescriptor);
				if (editorRowConfiguration != null)
				{
					final Listcell cell = new Listcell();
					cell.setParent(row);
					final Div editorDiv = new Div();
					editorDiv.setParent(cell);
					editorDiv.setSclass(CSS_EDITOR_WIDGET_EDITOR);
					renderEditor(editorDiv, editorRowConfiguration, item, propertyDescriptor, widget);
				}
			}
			else
			{
				// Not editable!
				final String value = ObjectGetValueUtils.getValue(column.getValueHandler(), item);
				final Listcell cell = new Listcell(value);
				cell.setParent(row);
			}
		}
		Listcell cell = new Listcell();
		cell.setParent(row);

		if ((isDeletable(widget, item))){
		Button deleteButton = new Button(LabelUtils.getLabel(widget,
				"deleteBtn", new Object[0]));
		deleteButton.setParent(cell);
		deleteButton.addEventListener("onClick", onClickEventListener(
				widget,item));
		}
		return row;
	}
	
	protected class DefaultShipmentAddressChangedEventListener implements
	EventListener {
    private final transient ListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget;

    public DefaultShipmentAddressChangedEventListener(
			ListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget) {
	this.widget = widget;
      }

public void onEvent(Event event) throws Exception {
	if (!(event instanceof SelectEvent)) {
		return;
	}
	MarketplaceCustomerAddressesWidgetRenderer.this
			.handleDefaultShipmentAddressChangedEvent(this.widget,
					(SelectEvent) event);
}
    }
}
	

