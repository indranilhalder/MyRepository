<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="defaultPincode" required="true" type="java.lang.String" %>
<%@ attribute name="showDeliveryAddress" required="true" type="java.lang.Boolean" %>
<%@ attribute name="mplconfigModel"  type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/product" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="hasShippedItems" value="${cartData.deliveryItemsQuantity > 0}" />
<c:set var="deliveryAddress" value="${cartData.deliveryAddress}"/>
<c:set var="firstShippedItem" value="true"></c:set>
<c:set var="defaultPinCode" value="${defaultPincode}"></c:set>


<c:if test="${hasShippedItems}">
<style>

@media (min-width: 650px) {
	.deliverySlotOptions {
		width: 100% !important;
	}

	.deliverySlotOptions .deliverySlotProduct {
		width: 40% !important;
	}
	
	.deliverySlotOptions .deliverySlotType {
		width: 20% !important;
	}
	
	.deliverySlotOptions .deliverySlotRadio {
		width: 40% !important;
		padding-right: 0px !important;
		padding-left: 0px !important;
	}
}

.deliverySlotOptions .deliverySlotRadio {
	text-align: center;
}

.deliverySlotOptions .deliverySlotRadio .form-control {
	text-align: center;
}

.deliverySlotOptions .deliverySlotRadio input {
	display: block;
	width: 15px;
	height:15px;
	border-radius: 50%;
	border: 2px solid #ccc;
	padding: 0px;
	cursor: pointer;
	margin: 0 auto;
	position: static;
    overflow: auto; 
}

.deliverySlotOptions .deliverySlotRadio input:focus, .deliverySlotOptions .deliverySlotRadio input:active, .deliverySlotOptions .deliverySlotRadio input:checked {
	background: #000;
}

.deliverySlotOptions .deliverySlotRadio label {
	margin: 0px;
	padding: 0px !important;
	font-size: 10px;
	display: block;
    text-align: left;
}

.deliverySlotOptions .deliverySlotRadio .heading {
	margin-bottom: 10px;
	color: #333;
	font-size: 14px;
	margin-left: 10px;
}

.deliverySlotOptions .deliverySlotRadio .col-md-4 {
	padding: 0px;
}

.pardhuBlock {
	text-align: left;
}

.radioPardhu {
	display: inline-block !important;
}

.greyText {
	font-size: 10px;
	color: #333;
	padding: 5px 0px;
}

.pardhuBlock span.dateTime {
	font-size: 12px;
}

.pardhuBlock {
	margin-bottom: 10px;
	margin-left: 5px;	
	display: block;
}

.timeSlotsLat span {
	font-size: 10px;
	color: #000;
}

.workingTimeslots {
	width: 100%;
}

.workingTimeslots li {
	float: left;
	display: inline-blok;
	width: auto !important;
	padding-right: 5px;
	padding-top: 5px;
}

span.selectTime {
	padding: 5px 0px;
	font-size: 12px;
	color: #333;
	display: block;
}

div.displayClick {
	display: none;
	margin-left: 20px;
}
 .display{
			display: none;
		} 
.timeDelivery{
			    width: 300%;
			    position: absolute;
		}
li.deliverySlotRadio .reset{margin: 3px 0px !important;    height: 30px !important; line-height: 30px;} 
.fixHeaderButton{top: 35px; right: 75px;}
.w100 {height: 40px !important;
    width: 175px !important;
    font-size: 10px !important;font-weight: normal !important; line-height: 20px !important;}
    
    .checkout.wrapper .product-block li.header > ul li.delivery, .checkout.wrapper .product-block li.item > ul li.delivery{
	    width: 31%;
	    /* padding: 20px 0 0; */
	    float: right;
	    text-align:center;
}
 /* R2.3 TISRLREG-2465 Author Tribhuvan Start*/
@media (max-width: 767px){
	 
 .checkout.wrapper .product-block li.item > ul li.delivery{
		    clear: both;
    float: none;
    margin-top: 20px;
    width: 100%;
    text-align: left;
 }
	}
	 /* R2.3 TISRLREG-2465 Author Tribhuvan end*/
</style>
<script>
	$(document).ready(function(){
		$(".step-1").addClass("step-done");
		$(".step-2").addClass("active");
		$(".step-3").addClass("in-active");
		$(".progress-barg .step").addClass("step2");
		$("#deliveryMethodSubmitUp").addClass("fixHeaderButton");
		$(".click-and-collect").addClass("click-collect");
		$(".radioClickDate").click(function(){
			$(this).parent().parent().find("div.displayClick").hide();
			if($(this).next().next().css("display") == "none") {
				$(this).next().next().slideToggle().find("input").filter(':input:visible:first').prop("checked", true);
			}
			$(this).parent().parent().parent().find("button").removeAttr("disabled");
			
			
		});
		var tmp = false;
		$(".radioPardhu").mouseup(function(){
			/* Set Values For Ajax Call */
			var mplconfigModel = $('#mplconfigModel').val();
			var selectedUssId;
			var date;
			var time;
			var radioTribhuvanLen = $(".radioPardhu:checked").length;
			if(($(this).attr("data-name") == "date") && $(this).closest(".deliverySlotRadio .tribhuvanClosest").attr('data-ajax') == '3bu1') {
				//alert(mplconfigModel);
				//alert($(this).closest(".deliverySlotRadio .tribhuvanClosest").attr('id'));
				$(this).closest(".deliverySlotRadio .tribhuvanClosest").attr('data-ajax','shiva');
				mplconfigModel = $('#mplconfigModel').val();
				date = $(this).val();
				time = $(this).next().next().find("input.timeSlots:first").val();
				selectedUssId = $(this).attr('data-ussid');
				
			} else {
				
				mplconfigModel = "0";
				//alert(mplconfigModel);
				date = $(this).parent().parent().parent().parent().parent().find(".radioClickDate").val();
				time = $(this).val();
				selectedUssId = $(this).attr('data-ussid');
			}
			var dataString = 'deliverySlotCost='+mplconfigModel+'&deliverySlotDate='+date+'&deliverySlotTime='+time+'&ussId='+selectedUssId;
    		//alert(dataString);
    		
    		$.ajax({                            
    	 		url: ACC.config.encodedContextPath + "/checkout/multi/delivery-method/deliverySlotCostForEd",
    	 		data : dataString,
    	 		success : function(response) {
    	 			var result = response.split("-");
    	 			if(response == '-'){
    	 				
    	 			}else{
    	 			$("#deliveryCostSpanId").empty().text(result[0]);
    	 			$("#totalWithConvField").empty().text(result[1]);
    	 		}
    	 		},
    	 		error : function(error) {
    	 			
    	 		}
    	 	});
		});
    	
    	$(".reset").click(function(){
    		var currentReset = $(this);
    		$(this).parent().parent().find(".pardhuBlock input[type='radio']").prop('checked', false);
    		$(this).parent().parent().find(".pardhuBlock input[data-name='time']").prop('checked', false);
    		var ussId=$(this).attr('data-ussid');
    		var mplconfigModel= $('#mplconfigModel').val();
    		$(this).parent().parent().find(".displayClick").hide();
    		$(this).closest(".deliverySlotRadio .tribhuvanClosest").attr('data-ajax','3bu1');
    		var dataString = 'deliverySlotCost='+mplconfigModel+'&ussId='+ussId;
    		//alert(dataString);
    		 $.ajax({                            
    	 		url: ACC.config.encodedContextPath + "/checkout/multi/delivery-method/updateDeliverySlotCostForEd",
    	 		data : dataString,
    	 		success : function(response) {
    	 			
    	 			currentReset.prop('disabled','disabled');
    	 			var result = response.split("-");
    	 			$("#deliveryCostSpanId").empty().text(result[0]);
    	 			$("#totalWithConvField").empty().text(result[1]);
    	 		},
    	 		error : function(error) {

    	 		}
    	 	}); 
    		
    	});
		
		
		
	});
</script>
	<div class="checkout-shipping-items">
		<h1>
			
			<spring:theme code="checkout.multi.deliveryMethod.chooseDeliveryOption"></spring:theme></br>
			
			 
		</h1>
		<p><spring:theme code="checkout.multi.deliveryMethod.chooseDeliveryOption.showPincode" text="Showing delivery options for pincode "></spring:theme>&nbsp;<span>${defaultPinCode}</span></p>
		<span></span>
		
		<ul id="deliveryradioul" class="checkout-table product-block">
				<li class="headers">
					<ul class="headline">
					
						<li class="delivery" id="header4"><spring:theme code="text.delivery.modes"/></li>
						<li class="delivery" id="header4">Delivery Slots</li>
						<li  id="header2" class="delivery"><spring:theme code="text.product.information"/></li>
					
					
					</ul>
				</li>
				 			<c:set var="scheduleIndex" value="0" scope="page"></c:set>
						<c:forEach items="${cartData.entries}" var="entry">
						<c:set var="scheduleIndex" value="${scheduleIndex + 1}" scope="page"></c:set>

								<c:url value="${entry.product.url}" var="productUrl" />
				
								<li class="item delivery_options deliverySlotOptions">
								<ul>
									<li class="deliverySlotProduct">
										<div >
											<div class="thumb product-img">
												<a href="${productUrl}"><product:productPrimaryImage
														product="${entry.product}" format="thumbnail" /></a>
											</div>
													   
													   
											<div class="details product" >
												<h3 class="product-brand-name"><a href="${entryProductUrl}">${entry.product.brand.brandname}</a></h3>
												<ycommerce:testId code="cart_product_name">
													<a href="${productUrl}"><div class="name product-name">${entry.product.name}</div></a>
												</ycommerce:testId>
												
												<!-- start TISEE-4631 TISUAT-4229 -->
												
												 <c:if test="${fn:toUpperCase(entry.product.rootCategory) != 'ELECTRONICS'}">
												 	
												 	<ycommerce:testId code="cart_product_size">
														<div class="size"><spring:theme code="text.size"/>${entry.product.size}</div>
													</ycommerce:testId>
													<ycommerce:testId code="cart_product_colour">
																<div class="colour"><spring:theme code="text.colour"/>${entry.product.colour}</div>
													</ycommerce:testId>
												 </c:if>
												<!-- end TISEE-4631 TISUAT-4229 -->
												
												<ycommerce:testId code="cart_product_colour">
													<div class="colour"><spring:theme code="text.seller.name"/>	${entry.selectedSellerInformation.sellername}</div>
												</ycommerce:testId>
												
												<c:forEach items="${fullfillmentData}" var="fullfillmentData">
													<c:if test="${fullfillmentData.key == entry.entryNumber}">
														<c:set var="fulfilmentValue" value="${fn:toLowerCase(fullfillmentData.value)}"> </c:set>
														<c:choose>
															<c:when test="${fulfilmentValue eq 'tship'}">
																	<div class="colour">
																		Fulfilled By : <spring:theme code="product.default.fulfillmentType"></spring:theme>
																	</div>
															</c:when>
															<c:otherwise>
																	<div class="colour">
																		Fulfilled By : ${entry.selectedSellerInformation.sellername} 
																	</div>	
															</c:otherwise>
														</c:choose>
													</c:if>
												</c:forEach>
												
												<ycommerce:testId code="cart_product_colour">
												<div class="colour"><spring:theme code="text.qty"/>${entry.quantity}</div>
												</ycommerce:testId>
															
												<c:if
													test="${ycommerce:doesPotentialPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
													<c:forEach items="${cartData.potentialProductPromotions}"
														var="promotion">
														<c:set var="displayed" value="false" />
														<c:forEach items="${promotion.consumedEntries}"
															var="consumedEntry">
															<c:if
																test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber && not empty promotion.description}">
															</c:if>
														</c:forEach>
													</c:forEach>
												</c:if>
												<c:if
													test="${ycommerce:doesAppliedPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
													<c:forEach items="${cartData.appliedProductPromotions}"
														var="promotion">
														<c:set var="displayed" value="false" />
														<c:forEach items="${promotion.consumedEntries}"
															var="consumedEntry">
															<c:if
																test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
																<c:set var="displayed" value="true" />
															</c:if>
														</c:forEach>
													</c:forEach>
												</c:if>
		
												<c:set var="entryStock"
													value="${entry.product.stock.stockLevelStatus.code}" />
		
												<c:forEach items="${entry.product.baseOptions}" var="option">
													<c:if
														test="${not empty option.selected and option.selected.url eq entry.product.url}">
														<c:forEach
															items="${option.selected.variantOptionQualifiers}"
															var="selectedOption">
															<div>
																<strong>${selectedOption.name}:</strong> <span>${selectedOption.value}</span>
															</div>
															<c:set var="entryStock"
																value="${option.selected.stock.stockLevelStatus.code}" />
														</c:forEach>
													</c:if>
												</c:forEach>
		
											<c:if
												test="${not empty sellerInfoMap}">
												<c:forEach items="${sellerInfoMap}"
													var="sellerInfoMap">
													<c:if
														test="${sellerInfoMap.key == entry.entryNumber}">
														<c:forEach items="${sellerInfoMap.value}"
															var="sellerInfoMapValue"> 
															<div><span><spring:theme code="text.seller.name"/> </span>${sellerInfoMapValue}</div>
														</c:forEach>
														
														<div class="size"><spring:theme code="text.size"/>${entry.product.size}</div>
												<div class="colour"><spring:theme code="text.colour"/>${entry.product.colour}</div>
													</c:if>
												</c:forEach>
											</c:if>
											<div class="item-price delivery-price"style="line-height: 0px;">
											<ycommerce:testId code="cart_totalProductPrice_label">
											<%-- <c:choose>
												<c:when test="${not empty entry.totalPrice}">
													<format:price priceData="${entry.totalPrice}"
														displayFreeForZero="true" />
												</c:when>
												<c:otherwise>
													<format:price priceData="${entry.totalSalePrice}"
														displayFreeForZero="true" />
													</c:otherwise>
											</c:choose> --%>
											</ycommerce:testId>
											
										</div> 

											</div>
											
										</div>
										
									</li>
								
									<li class="delivery deliverySlotType">
										<ul >
										<c:if test="${not empty entry.selectedDeliveryModeForUssId}">
													${entry.selectedDeliveryModeForUssId}
												</c:if>
										</ul>
									</li>
									
									
									<li class="deliverySlotRadio">
									   <input type="hidden" id="mplconfigModel" name="mplconfigModel" value="${mplconfigModel}"/>
									    <input type="hidden" id="selectedUssId" name="selectedUssId" value="${entry.selectedUssid}"/>
										
										<div class="row tribhuvanClosest" id="content"  data-ajax="3bu1">
										<c:choose>
										<c:when test="${not empty entry.deliverySlotsTime}">
										<label class="heading" for="date">Preferred Date of Delivery</label>
										<div class="col-md-8">
										<c:set var="dateTimeSlotId" value="0" scope="page"></c:set>
											<c:forEach items="${entry.deliverySlotsTime}" var="dateSlots">
												<c:set var="dateTimeSlotId" value="${dateTimeSlotId + 1}" scope="page"></c:set>
												<fmt:parseDate value="${dateSlots.key}" var="parseddeliveryDate" pattern="dd-MM-yyyy" />
												<div class="col-md-12 NOP pardhuBlock">
													<input type="radio" class="radioPardhu radioClickDate" data-name="date" name="date${scheduleIndex}" data-ussid="${entry.selectedUssid}" value="${dateSlots.key}"> <span class="dateTime"><fmt:formatDate value="${parseddeliveryDate}" pattern="'ON' d  MMMM, yyyy"/></span>
													<c:choose>
		      										<c:when test="${dateTimeSlotId ==1}">
														<div class="displayClick" id="${dateTimeSlotId}">
														
															<%-- <div class="greyText">(Shipped at INR ${mplconfigModel} per order)</div> --%>
															<c:choose>
																	<c:when test="${empty mplconfigModel}">
																		<div class="greyText">(Shipped at INR 0.00 per order)</div>
																	</c:when>
																	<c:otherwise>
																		<div class="greyText">(Shipped at INR ${mplconfigModel} per order)</div>
																	</c:otherwise>
															</c:choose>
															<div class="timeSlotsLat">
																<span class="selectTime">Select a time slot</span>
																<ul class="workingTimeslots">
																	<c:set var="timeSlotId" value="0" scope="page"></c:set>
					      										 	<c:forEach items="${dateSlots.value}" var="timeSlots">
					      										  	<c:set var="timeSlotId" value="${timeSlotId + 1}" scope="page"></c:set>
																		<li id="${dateTimeSlotId}${timeSlotId}"><input type="radio" class="radioPardhu timeSlots" data-ussid="${entry.selectedUssid}" data-Date='${dateSlots.key}' data-name="time"  name="time${scheduleIndex}${dateTimeSlotId}" value="${timeSlots}"> <span class="dateTime1">${timeSlots}</span></li>
																	</c:forEach>
																</ul>
															</div>
														</div>
													</c:when>
													<c:otherwise>
														<div class="displayClick" id="${dateTimeSlotId}">
															<%-- <div class="greyText">(Shipped at INR ${mplconfigModel} per order)</div> --%>
															<c:choose>
																	<c:when test="${empty mplconfigModel}">
																		<div class="greyText">(Shipped at INR 0.00 per order)</div>
																	</c:when>
																	<c:otherwise>
																		<div class="greyText">(Shipped at INR ${mplconfigModel} per order)</div>
																	</c:otherwise>
															</c:choose>
															<div class="timeSlotsLat">
																<span class="selectTime">Select a time slot</span>
																<ul class="workingTimeslots">
																	<c:set var="timeSlotId" value="0" scope="page"></c:set>
					      										 	<c:forEach items="${dateSlots.value}" var="timeSlots">
					      										  	<c:set var="timeSlotId" value="${timeSlotId + 1}" scope="page"></c:set>
																		<li id="${dateTimeSlotId}${timeSlotId}"><input type="radio" class="radioPardhu timeSlots" data-ussid="${entry.selectedUssid}" data-Date='${dateSlots.key}' data-name="time"  name="time${scheduleIndex}${dateTimeSlotId}" value="${timeSlots}"> <span class="dateTime1">${timeSlots}</span></li>
																	</c:forEach>
																</ul>
															</div>
														</div>
													</c:otherwise>
												</c:choose>
												</div>
											</c:forEach>
										<div class="col-md-12 NOP pardhuBlock" align="left">
											<button class="button w100 reset" type="button" data-ussid="${entry.selectedUssid}" disabled="disabled">Reset</button>
											 
											</div>
											</div>
											
										</c:when>
										<c:otherwise>
										<div class="">Not Applicable</div>
										
										</c:otherwise>
										</c:choose>
										</div>
									</li>
									</ul>
								</li>
							</c:forEach>
			</ul>
		
		
	</div>
	</c:if>