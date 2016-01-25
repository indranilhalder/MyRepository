package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.pmd.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.api.InputElement;

import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceCancellationController;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceReturnsController;
import com.tisl.mpl.wsdto.ReturnLogistics;
import com.tisl.mpl.xml.pojo.OrderLineDataResponse;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.impl.PropertyColumnConfiguration;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.models.ListWidgetModel;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.CockpitUiConfigLoader;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.comparators.TypedObjectOrderEntryNumberComparator;
import de.hybris.platform.cscockpit.widgets.controllers.ReturnsController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.ReturnRequestCreateWidgetRenderer;
import de.hybris.platform.returns.model.ReturnRequestModel;

public class MarketplaceReturnRequestCreateWidgetRenderer extends
		ReturnRequestCreateWidgetRenderer {

	private static final Logger LOG = Logger
			.getLogger(ReturnRequestCreateWidgetRenderer.class);
	private transient List<ObjectValueContainer> returnObjectValueContainers;

	private static final String REVERSE_LOGISTICS_NOTAVAILABLE = "reverselogisticnotavailable";
	private static final String REVERSE_LOGISTICS_AVAILABLE = "reverselogisticavailable";
	private static final String REVERSE_LOGISTICS_PARTIALAVAILABLE = "reverselogisticpartialavailable";
	private static final String NO_RESPONSE_FROM_SERVER = "noResponseFromServer";

	@Override
	protected HtmlBasedComponent createContentInternal(InputWidget widget,
			HtmlBasedComponent rootContainer) {
		Div content = new Div();
		returnObjectValueContainers = new ArrayList();
		if (((ReturnsController) widget.getWidgetController()).canReturn()) {
			Div returnableContainer = new Div();
			returnableContainer.setSclass("csListboxContainer");
			returnableContainer.setParent(content);
			InputElement hiddenInput = new Textbox();
			hiddenInput.setVisible(false);
			hiddenInput.setFocus(true);
			hiddenInput.setParent(returnableContainer);
			Listbox listBox = new Listbox();
			listBox.setParent(returnableContainer);
			listBox.setVflex(false);
			listBox.setFixedLayout(true);
			listBox.setSclass("csWidgetListbox");
			renderListbox(listBox, widget);
			Div requestCreationContent = new Div();
			requestCreationContent.setSclass("csReturnRequestActions");
			requestCreationContent.setParent(content);
			Button createButton = new Button(LabelUtils.getLabel(widget,
					"createButton", new Object[0]));
			createButton.setParent(requestCreationContent);
			createButton.addEventListener(
					"onClick",
					createReturnRequestCreateEventListener(widget,
							returnObjectValueContainers));
		} else {
			Label dummyLabel = new Label(LabelUtils.getLabel(widget,
					"cantReturn", new Object[0]));
			dummyLabel.setSclass("csCantReturn");
			dummyLabel.setParent(content);
		}
		return content;
	}

	@Override
	protected void renderListbox(
			Listbox listBox,
			InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget) {
		ListWidgetModel widgetModel = (ListWidgetModel) widget.getWidgetModel();
		if (widgetModel == null)
			return;
		Map returnableOrderEntries = ((ReturnsController) widget
				.getWidgetController()).getReturnableOrderEntries();
		if ((returnableOrderEntries == null)
				|| (returnableOrderEntries.isEmpty()))
			return;
		try {
			List<ColumnConfiguration> columns = getColumnConfigurations(
					getListConfigurationCode(), getListConfigurationType());
			if (!(CollectionUtils.isNotEmpty(columns)))
				return;
			Listhead headRow = new Listhead();
			headRow.setParent(listBox);

			Listheader colEntryNoHeader = new Listheader(LabelUtils.getLabel(
					widget, "entryNumber", new Object[0]));
			colEntryNoHeader.setWidth("10px");
			colEntryNoHeader.setParent(headRow);

			Listheader colTxnIdHeader = new Listheader(LabelUtils.getLabel(
					widget, "txnId", new Object[0]));
			// colTxnIdHeader.setWidth("50px");
			colTxnIdHeader.setParent(headRow);

			Listheader colProductHeader = new Listheader(LabelUtils.getLabel(
					widget, "productInfo", new Object[0]));
			colProductHeader.setWidth("100px");
			colProductHeader.setParent(headRow);

			/*
			 * Listheader colUSSIdHeader = new Listheader(LabelUtils.getLabel(
			 * widget, "ussId", new Object[0])); //
			 * colUSSIdHeader.setWidth("50px");
			 * colUSSIdHeader.setParent(headRow);
			 */

			Listheader colSellerNameHeader = new Listheader(
					LabelUtils.getLabel(widget, "sellerName", new Object[0]));
			// colSellerNameHeader.setWidth("50px");
			colSellerNameHeader.setParent(headRow);

			Listheader colSerialNumberHeader = new Listheader(
					LabelUtils.getLabel(widget, "serialNumer", new Object[0]));
			// colSerialNumberHeader.setWidth("50px");
			colSerialNumberHeader.setParent(headRow);

			/*
			 * Listheader colMaxQtyHeader = new Listheader(LabelUtils.getLabel(
			 * widget, "maxQty", new Object[0]));
			 * colMaxQtyHeader.setWidth("50px");
			 * colMaxQtyHeader.setParent(headRow);
			 */

			for (ColumnConfiguration col : columns) {
				Listheader colHeader = new Listheader(
						getPropertyRendererHelper().getPropertyDescriptorName(
								col));
				colHeader.setParent(headRow);
				colHeader.setTooltiptext(col.getName());
			}

			EditorConfiguration editorConf = CockpitUiConfigLoader
					.getEditorConfiguration(UISessionUtils.getCurrentSession(),
							getListEditorConfigurationCode(),
							getListConfigurationType());

			List editorMapping = getPropertyEditorHelper()
					.getAllEditorRowConfigurations(editorConf);

			List<TypedObject> orderEntries = new ArrayList(
					returnableOrderEntries.keySet());
			Collections.sort(orderEntries,
					TypedObjectOrderEntryNumberComparator.INSTANCE);

			for (TypedObject item : orderEntries) {
				Listitem row = new Listitem();
				row.setSclass("listbox-row-item");
				row.setParent(listBox);

				Listcell entryNoCell = new Listcell();
				entryNoCell.setParent(row);
				Div entryNoDiv = new Div();
				entryNoDiv.setParent(entryNoCell);
				entryNoDiv.setSclass("editorWidgetEditor");
				Label entryNoLabel = new Label(
						String.valueOf(((AbstractOrderEntryModel) item
								.getObject()).getEntryNumber()));
				entryNoLabel.setParent(entryNoDiv);

				Listcell txnIdCell = new Listcell();
				txnIdCell.setParent(row);
				Div txnIdDiv = new Div();
				txnIdDiv.setParent(txnIdCell);
				txnIdDiv.setSclass("editorWidgetEditor");
				Label txnIdLabel = new Label(
						((AbstractOrderEntryModel) item.getObject())
								.getTransactionID());
				txnIdLabel.setParent(txnIdDiv);

				Listcell productCell = new Listcell();
				productCell.setParent(row);
				Div productDiv = new Div();
				productDiv.setParent(productCell);
				productDiv.setSclass("editorWidgetEditor");
				List columnsOrderEntry = getColumnConfigurations(
						getProductInfoConfigurationCode(), item.getType()
								.getCode());
				getPropertyRendererHelper()
						.buildPropertyValuesFromColumnConfigs(item,
								columnsOrderEntry, productDiv);

				/*
				 * Listcell USSIdCell = new Listcell();
				 * USSIdCell.setParent(row); Div USSIdDiv = new Div();
				 * USSIdDiv.setParent(USSIdCell);
				 * USSIdDiv.setSclass("editorWidgetEditor"); Label USSIdLabel =
				 * new Label( String.valueOf(((AbstractOrderEntryModel) item
				 * .getObject()).getSelectedUSSID()));
				 * USSIdLabel.setParent(USSIdDiv);
				 */

				Listcell sellerNameCell = new Listcell();
				sellerNameCell.setParent(row);
				Div sellerNameDiv = new Div();
				sellerNameDiv.setParent(sellerNameCell);
				sellerNameDiv.setSclass("editorWidgetEditor");
				String sellerName = ((MarketPlaceReturnsController) widget
						.getWidgetController()).getSellerName(String
						.valueOf(((AbstractOrderEntryModel) item.getObject())
								.getSelectedUSSID()));
				Label sellerNameLabel = new Label(sellerName);
				sellerNameLabel.setParent(sellerNameDiv);

				Listcell serialNumberCell = new Listcell();
				serialNumberCell.setParent(row);
				Div serialNumberDiv = new Div();
				serialNumberDiv.setParent(serialNumberCell);
				serialNumberDiv.setSclass("editorWidgetEditor");
				String serialNumber = "";
				if (null != (((AbstractOrderEntryModel) item.getObject())
						.getImeiDetail())) {
					for (Iterator iterator = ((AbstractOrderEntryModel) item
							.getObject()).getImeiDetail().getIdentifiers()
							.iterator(); iterator.hasNext();) {
						if (StringUtil.isNotEmpty(serialNumber)) {
							serialNumber = serialNumber + "-"
									+ (String) iterator.next();
						} else {
							serialNumber = (String) iterator.next();
						}
					}
					if (StringUtil.isNotEmpty(serialNumber)) {
						serialNumber = serialNumber
								+ "/"
								+ ((AbstractOrderEntryModel) item.getObject())
										.getImeiDetail().getSerialNum();
					} else {
						serialNumber = ((AbstractOrderEntryModel) item
								.getObject()).getImeiDetail().getSerialNum();
					}
				}
				Label serialNumberLabel = new Label(serialNumber);
				serialNumberLabel.setParent(serialNumberDiv);

				/*
				 * Listcell maxQtyCell = new Listcell();
				 * maxQtyCell.setParent(row); Div maxQtyDiv = new Div();
				 * maxQtyDiv.setParent(maxQtyCell);
				 * maxQtyDiv.setSclass("editorWidgetEditor"); Label maxQtyLabel
				 * = new Label(
				 * String.valueOf(returnableOrderEntries.get(item)));
				 * maxQtyLabel.setParent(maxQtyDiv);
				 */

				TypedObject returnEntryObject = null;
				ObjectType returnEntryType = getCockpitTypeService()
						.getObjectType(getListConfigurationType());
				ObjectValueContainer returnEntryValueContainer = buildReturnEntryValueContainer(
						item, returnEntryType,
						returnEntryType.getPropertyDescriptors(),
						getSystemService().getAvailableLanguageIsos());
				this.returnObjectValueContainers.add(returnEntryValueContainer);

				for (ColumnConfiguration column : columns) {
					if (!(column instanceof PropertyColumnConfiguration))
						continue;
					PropertyColumnConfiguration col = (PropertyColumnConfiguration) column;

					PropertyDescriptor propertyDescriptor = col
							.getPropertyDescriptor();
					EditorRowConfiguration editorRowConfiguration = getPropertyEditorHelper()
							.findConfigurationByPropertyDescriptor(
									editorMapping, propertyDescriptor);
					if (editorRowConfiguration == null)
						continue;
					Listcell cell = new Listcell();
					cell.setParent(row);
					Div editorDiv = new Div();
					editorDiv.setParent(cell);
					editorDiv.setSclass("editorWidgetEditor");
					renderEditor(editorDiv, editorRowConfiguration,
							returnEntryObject, returnEntryValueContainer,
							propertyDescriptor, widget);
				}
			}
		} catch (Exception e) {
			LOG.error("failed to render return entries list", e);
		}
	}

	@Override
	protected ObjectValueContainer buildReturnEntryValueContainer(
			TypedObject orderEntry, ObjectType returnEntryType,
			Set<PropertyDescriptor> returnEntryPropertyDescriptors,
			Set<String> langISOs) {
		ObjectValueContainer currentObjectValues = new ObjectValueContainer(
				returnEntryType, orderEntry);
		for (PropertyDescriptor pd : returnEntryPropertyDescriptors) {
			if (pd.isLocalized()) {
				for (String langIso : langISOs) {
					currentObjectValues.addValue(pd, langIso,
							getDefaultValue(pd));
				}
			} else {
				Object currentValue = getDefaultValue(pd);
				currentObjectValues.addValue(pd, null, currentValue);
			}
		}
		return currentObjectValues;
	}

	@Override
	protected void handleReturnRequestCreateEvent(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			Event event,
			final List<ObjectValueContainer> returnObjectValueContainers)
			throws Exception {
		try {
			List<ReturnLogistics> returnLogisticsList = ((MarketPlaceReturnsController) widget
					.getWidgetController()).getReturnLogisticsList(widget,
					returnObjectValueContainers);

			final Map<Boolean, List<OrderLineDataResponse>> responseMap = ((MarketPlaceReturnsController) widget
					.getWidgetController())
					.validateReverseLogistics(returnLogisticsList);

			if ("RefundEntry".equalsIgnoreCase(getListConfigurationType())) {
				if (((ReturnsController) widget.getWidgetController())
						.validateCreateRefundRequest(returnObjectValueContainers)) {

					if (((MarketPlaceReturnsController) widget
							.getWidgetController())
							.isFreebieAvaialble(returnObjectValueContainers)) {
						Messagebox.show(LabelUtils.getLabel(widget,
								"freeBieAvaialble", new Object[0]), LabelUtils
								.getLabel(widget, "freeBieAvaialbleTitle",
										new Object[0]), Messagebox.OK
								| Messagebox.CANCEL, Messagebox.INFORMATION,
								new EventListener() {

									@Override
									public void onEvent(Event event)
											throws Exception {
										if (!event.getName().equals("onOK")) {
											return;
										} else {
											proceedToReturn(
													widget,
													returnObjectValueContainers,
													responseMap);
										}
									}
								});
					} else {
						proceedToReturn(widget, returnObjectValueContainers,
								responseMap);
						return;
					}
				}
/*				Messagebox.show(LabelUtils.getLabel(widget, "failedToValidate",
						new Object[0]), LabelUtils.getLabel(widget,
						"failedToValidate", new Object[0]), 1,
						"z-msgbox z-msgbox-error");*/
				return;
			}
			if ("ReplacementEntry".equalsIgnoreCase(getListConfigurationType())) {
				if (((ReturnsController) widget.getWidgetController())
						.validateCreateReplacementRequest(returnObjectValueContainers)) {
					if (((MarketPlaceReturnsController) widget
							.getWidgetController())
							.isFreebieAvaialble(returnObjectValueContainers)) {
						Messagebox.show(LabelUtils.getLabel(widget,
								"freeBieAvaialble", new Object[0]), LabelUtils
								.getLabel(widget, "freeBieAvaialbleTitle",
										new Object[0]), Messagebox.OK
								| Messagebox.CANCEL, Messagebox.INFORMATION,
								new EventListener() {

									@Override
									public void onEvent(Event event)
											throws Exception {
										if (!event.getName().equals("onOK")) {
											return;
										} else {
											proceedToReplacement(
													widget,
													returnObjectValueContainers,
													responseMap);
										}
									}
								});
					} else {

						proceedToReplacement(widget,
								returnObjectValueContainers, responseMap);
						return;
					}
				}
			/*	Messagebox.show(LabelUtils.getLabel(widget, "failedToValidate",
						new Object[0]), LabelUtils.getLabel(widget,
						"failedToValidate", new Object[0]), 1,
						"z-msgbox z-msgbox-error");*/
				return;
			}
			throw new IllegalStateException("Unsupported return entry type ["
					+ getListConfigurationType() + "]");
		} catch (ValidationException e) {
			Messagebox.show(e.getMessage()
					+ ((e.getCause() == null) ? "" : new StringBuilder(" - ")
							.append(e.getCause().getMessage()).toString()),
					LabelUtils.getLabel(widget, "failedToValidate",
							new Object[0]), 1, "z-msgbox z-msgbox-error");
		}
	}

	private void proceedToReplacement(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<ObjectValueContainer> returnObjectValueContainers,
			final Map<Boolean, List<OrderLineDataResponse>> responseMap)
			throws InterruptedException {
		if (MapUtils.isNotEmpty(responseMap)) {
			if (CollectionUtils.isEmpty(responseMap.get(Boolean.FALSE))) {
				Messagebox.show(LabelUtils.getLabel(widget,
						REVERSE_LOGISTICS_AVAILABLE, new Object[0]),
						"Question", Messagebox.OK | Messagebox.CANCEL,
						Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() {
							public void onEvent(Event e)
									throws InterruptedException {
								if (e.getName().equals("onOK")) {
									replacementOK(widget,
											returnObjectValueContainers);
									return;
								} else {
									return;
								}
							}
						});
			} else if (CollectionUtils.isEmpty(responseMap.get(Boolean.TRUE))) {
				Messagebox.show(LabelUtils.getLabel(widget,
						REVERSE_LOGISTICS_NOTAVAILABLE, new Object[0]),
						"Question", Messagebox.OK | Messagebox.CANCEL,
						Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() {
							public void onEvent(Event e)
									throws InterruptedException {
								if (e.getName().equals("onOK")) {
									replacementOK(widget,
											returnObjectValueContainers);
									return;
								} else {
									return;
								}
							}
						});
			} else {
				String notAvailabletransactionList = null;
				if (CollectionUtils.isNotEmpty(responseMap.get(Boolean.FALSE))) {
					for (OrderLineDataResponse orderEntry : responseMap
							.get(Boolean.FALSE)) {
						if (StringUtils.isNotEmpty(notAvailabletransactionList))
							notAvailabletransactionList = notAvailabletransactionList
									+ System.getProperty("line.separator")
									+ orderEntry.getTransactionId();
						else
							notAvailabletransactionList = orderEntry
									.getTransactionId();
					}
				}
				String availabletransactionList = null;
				if (CollectionUtils.isNotEmpty(responseMap.get(Boolean.TRUE))) {
					for (OrderLineDataResponse orderEntry : responseMap
							.get(Boolean.TRUE)) {
						if (StringUtils.isNotEmpty(availabletransactionList))
							availabletransactionList = availabletransactionList
									+ System.getProperty("line.separator")
									+ orderEntry.getTransactionId();
						else
							availabletransactionList = orderEntry
									.getTransactionId();
					}
				}

				String finalMessage = LabelUtils.getLabel(widget,
						REVERSE_LOGISTICS_PARTIALAVAILABLE);
				if (null != availabletransactionList)
					finalMessage = finalMessage
							+ System.getProperty("line.separator")
							+ "The Transaction Id's for which Reverse Logisitic is available: "
							+ System.getProperty("line.separator")
							+ availabletransactionList;
				if (null != notAvailabletransactionList)
					finalMessage = finalMessage
							+ System.getProperty("line.separator")
							+ "The Transaction Id's for which Reverse Logisitic is not available: "
							+ System.getProperty("line.separator")
							+ notAvailabletransactionList;

				Messagebox.show(finalMessage, "Error", Messagebox.OK,
						Messagebox.ERROR,
						new org.zkoss.zk.ui.event.EventListener() {
							public void onEvent(Event e)
									throws InterruptedException {
								if (e.getName().equals("onOK")) {
									return;
								}
							}
						});
			}
		} else {
			Messagebox.show(LabelUtils.getLabel(widget,
					NO_RESPONSE_FROM_SERVER, new Object[0]), "Error",
					Messagebox.OK, Messagebox.ERROR,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event e)
								throws InterruptedException {
							if (e.getName().equals("onOK")) {
								return;
							}
						}
					});
		}
		return;
	}

	private void proceedToReturn(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<ObjectValueContainer> returnObjectValueContainers,
			Map<Boolean, List<OrderLineDataResponse>> responseMap)
			throws InterruptedException {
		if (MapUtils.isNotEmpty(responseMap)) {
			if (CollectionUtils.isEmpty(responseMap.get(Boolean.FALSE))) {
				Messagebox.show(LabelUtils.getLabel(widget,
						REVERSE_LOGISTICS_AVAILABLE, new Object[0]),
						"Question", Messagebox.OK | Messagebox.CANCEL,
						Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() {
							public void onEvent(Event e)
									throws InterruptedException {
								if (e.getName().equals("onOK")) {
									refundOK(widget,
											returnObjectValueContainers);
									return;
								} else {
									return;
								}
							}
						});
			} else if (CollectionUtils.isEmpty(responseMap.get(Boolean.TRUE))) {
				Messagebox.show(LabelUtils.getLabel(widget,
						REVERSE_LOGISTICS_NOTAVAILABLE, new Object[0]),
						"Question", Messagebox.OK | Messagebox.CANCEL,
						Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() {
							public void onEvent(Event e)
									throws InterruptedException {
								if (e.getName().equals("onOK")) {
									refundOK(widget,
											returnObjectValueContainers);
									return;
								} else {
									return;
								}
							}
						});
			} else {
				String notAvailabletransactionList = null;
				if (CollectionUtils.isNotEmpty(responseMap.get(Boolean.FALSE))) {
					for (OrderLineDataResponse orderEntry : responseMap
							.get(Boolean.FALSE)) {
						if (StringUtils.isNotEmpty(notAvailabletransactionList))
							notAvailabletransactionList = notAvailabletransactionList
									+ System.getProperty("line.separator")
									+ orderEntry.getTransactionId();
						else
							notAvailabletransactionList = orderEntry
									.getTransactionId();
					}
				}
				String availabletransactionList = null;
				if (CollectionUtils.isNotEmpty(responseMap.get(Boolean.TRUE))) {
					for (OrderLineDataResponse orderEntry : responseMap
							.get(Boolean.TRUE)) {
						if (StringUtils.isNotEmpty(availabletransactionList))
							availabletransactionList = availabletransactionList
									+ System.getProperty("line.separator")
									+ orderEntry.getTransactionId();
						else
							availabletransactionList = orderEntry
									.getTransactionId();
					}
				}

				String finalMessage = LabelUtils.getLabel(widget,
						REVERSE_LOGISTICS_PARTIALAVAILABLE);
				if (null != availabletransactionList)
					finalMessage = finalMessage
							+ System.getProperty("line.separator")
							+ "The Transaction Id's for which Reverse Logisitic is available: "
							+ System.getProperty("line.separator")
							+ availabletransactionList;
				if (null != notAvailabletransactionList)
					finalMessage = finalMessage
							+ System.getProperty("line.separator")
							+ "The Transaction Id's for which Reverse Logisitic is not available: "
							+ System.getProperty("line.separator")
							+ notAvailabletransactionList;

				Messagebox.show(finalMessage, "Error", Messagebox.OK,
						Messagebox.ERROR,
						new org.zkoss.zk.ui.event.EventListener() {
							public void onEvent(Event e)
									throws InterruptedException {
								if (e.getName().equals("onOK")) {
									return;
								}
							}
						});
			}
		} else {
			Messagebox.show(LabelUtils.getLabel(widget,
					NO_RESPONSE_FROM_SERVER, new Object[0]), "Error",
					Messagebox.OK, Messagebox.ERROR,
					new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event e)
								throws InterruptedException {
							if (e.getName().equals("onOK")) {
								return;
							}
						}
					});
		}
		return;
	}

	@Override
	protected EventListener createReturnRequestCreateEventListener(
			InputWidget widget, List returnObjectValueContainers) {
		return new ReturnRequestCreateEventListener(widget,
				returnObjectValueContainers);
	}

	private void replacementOK(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<ObjectValueContainer> returnObjectValueContainers)
			throws InterruptedException {
		TypedObject returnRequest = ((MarketPlaceReturnsController) widget
				.getWidgetController())
				.createReplacementRequest(returnObjectValueContainers);
		if (returnRequest != null) {
			getPopupWidgetHelper().dismissCurrentPopup();
			ReturnRequestModel returnRequestModel = (ReturnRequestModel) returnRequest
					.getObject();
			Messagebox.show(LabelUtils.getLabel(widget, "rmaNumber",
					new Object[] { returnRequestModel.getRMA() }), LabelUtils
					.getLabel(widget, "rmaNumberTitle", new Object[0]), 1,
					"z-msgbox z-msgbox-information");
			((ReturnsController) widget.getWidgetController()).dispatchEvent(
					null, widget, null);
			return;
		}
		Messagebox.show(LabelUtils.getLabel(widget, "error", new Object[0]),
				LabelUtils.getLabel(widget, "failed", new Object[0]), 1,
				"z-msgbox z-msgbox-error");
		return;
	}

	private void refundOK(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<ObjectValueContainer> returnObjectValueContainers) {
		TypedObject refundOrder = ((MarketPlaceReturnsController) widget
				.getWidgetController())
				.createRefundOrderPreview(returnObjectValueContainers);
		if (refundOrder == null)
			return;
		createRefundConfirmationPopupWindow(widget, getPopupWidgetHelper()
				.getCurrentPopup().getParent());
		return;
	}

	protected ObjectValueContainer.ObjectValueHolder getPropertyValue(
			ObjectValueContainer ovc, String propertyQualifier) {
		PropertyDescriptor pdReason = getPropertyDescriptor(
				ovc.getPropertyDescriptors(), propertyQualifier);
		if (pdReason != null) {
			return ovc.getValue(pdReason, null);
		}
		return null;
	}

	protected PropertyDescriptor getPropertyDescriptor(
			Set<PropertyDescriptor> properties, String qualifier) {
		if (properties != null) {
			for (PropertyDescriptor pd : properties) {
				if (pd.getQualifier().equals(qualifier)) {
					return pd;
				}
			}
		}
		return null;
	}

}