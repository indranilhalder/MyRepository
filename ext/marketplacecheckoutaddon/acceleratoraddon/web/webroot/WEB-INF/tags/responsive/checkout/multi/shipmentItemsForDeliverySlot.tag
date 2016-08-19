<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="defaultPincode" required="true" type="java.lang.String" %>
<%@ attribute name="showDeliveryAddress" required="true" type="java.lang.Boolean" %>
<%@ attribute name="mplconfigModel"  type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
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
		width: 30% !important;
	}
	
	.deliverySlotOptions .deliverySlotRadio {
		width: 30% !important;
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
}

.deliverySlotOptions .deliverySlotRadio input:focus, .deliverySlotOptions .deliverySlotRadio input:active, .deliverySlotOptions .deliverySlotRadio input:checked {
	background: #000;
}

.deliverySlotOptions .deliverySlotRadio label {
	margin: 0px;
	padding: 0px !important;
	font-size: 10px;
}

.deliverySlotOptions .deliverySlotRadio .heading {
	margin-bottom: 10px;
	margin-top: 15px;
	color: #333;
	font-size: 14px;
}

.deliverySlotOptions .deliverySlotRadio .col-md-4 {
	padding: 0px;
}


</style>
<script>
	$(document).ready(function(){
		$(".click-and-collect").addClass("click-collect");
	});
</script>
	<div class="checkout-shipping-items">
		<h1>
			
			<spring:theme code="checkout.multi.deliveryMethod.chooseDeliveryOption"></spring:theme></br>
			
			 
		</h1>
		<p><spring:theme code="checkout.multi.deliveryMethod.chooseDeliveryOption.showPincode" text="Showing delivery options for pincode "></spring:theme>&nbsp;<span>${defaultPinCode}</span></p>
		<span></span>
		
		<ul id="deliveryradioul" class="checkout-table product-block">
				<li class="header">
					<ul class="headline">
						<li style="width:40px" id="header2"><spring:theme code="text.product.information"/></li>
						<li class="delivery" id="header4"><spring:theme code="text.delivery.modes"/></li>
						<li class="delivery" id="header4">Delivery Slots</li>
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
											<div class="item-price delivery-price">
											<ycommerce:testId code="cart_totalProductPrice_label">
											<c:choose>
											<c:when test="${not empty entry.totalSalePrice}">
												<format:price priceData="${entry.totalSalePrice}"
													displayFreeForZero="true" />
													</c:when>
													<c:otherwise>
													<format:price priceData="${entry.totalPrice}"
													displayFreeForZero="true" />
													</c:otherwise>
													</c:choose>
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
										<button class="button reset pull-right" type="button" data-ussid="${entry.selectedUssid}" >Reset</button>
										<label class="heading" for="date">Preferred Date of Delivery</label>
										<div class="row"  id="content">
										
										
										<c:if test="${not empty entry.deliverySlotsTime}">
										<c:set var="dateTimeSlotId" value="0" scope="page"></c:set>
									<c:forEach items="${entry.deliverySlotsTime}" var="dateSlots">
									<c:set var="dateTimeSlotId" value="${dateTimeSlotId + 1}" scope="page"></c:set>
									
											<div class="col-md-4 col-xs-4 col-sm-4" >
												<div class="form-control scheduleDate">
													<input type="radio" class="timeTribhuvan" data-name="date" name="date${scheduleIndex}" value="${dateSlots.key}"> <br/>
													<fmt:parseDate value="${dateSlots.key}" var="parseddeliveryDate" pattern="dd-MM-yyyy" />
													<label for="date1"><fmt:formatDate value="${parseddeliveryDate}" pattern="MMM dd"/></label>
										<c:choose>
      										<c:when test="${dateTimeSlotId ==1}">
      										<div class="timeDelivery" id="${dateTimeSlotId}">
										<label class="heading" for="time">Preferred Time of Delivery</label>
										<c:set var="timeSlotId" value="0" scope="page"></c:set>
      										 <c:forEach items="${dateSlots.value}" var="timeSlots">
      										  <c:set var="timeSlotId" value="${timeSlotId + 1}" scope="page"></c:set>
										   <div class="row" id="${dateTimeSlotId}${timeSlotId}">
											<div class="col-md-4 col-xs-4 col-sm-4 " >
												<div class="form-control scheduleTime"  >
													<input type="radio" class="timeTribhuvan" data-Date='${dateSlots.key}' data-name="time"  name="time${scheduleIndex}${dateTimeSlotId}" value="${timeSlots}" disabled="disabled"> <br/>
													<label for="time1">${timeSlots}</label>
												</div>
											</div>
											</div>
											
											</c:forEach>
											</div>
      										</c:when>
      										<c:otherwise>
      										
      										 <div class="display timeDelivery" id="${dateTimeSlotId}">
										<label class="heading" for="time">Preferred Time of Delivery</label>
										<c:set var="timeSlotId" value="0" scope="page"></c:set>
      										 <c:forEach items="${dateSlots.value}" var="timeSlots">
      										
      										 <c:set var="timeSlotId" value="${timeSlotId + 1}" scope="page"></c:set>
										   <div class="row" id="${dateTimeSlotId}${timeSlotId}">
											<div class="col-md-4 col-xs-4 col-sm-4">
												<div class="form-control scheduleContent"  >
													<input type="radio" class="timeTribhuvan" data-Date='${dateSlots.key}' data-name="time" name="time${scheduleIndex}${dateTimeSlotId}" value="${timeSlots}" disabled="disabled"> <br/>
													<label for="time1">${timeSlots}</label>
												</div>
											</div>
											</div>
											
											</c:forEach>
      										</div>
      										</c:otherwise>
      										 </c:choose>
												</div>
											</div>
										</c:forEach>
										</c:if>
										</div>
									</li>
									</ul>
								</li>
							</c:forEach>
			</ul>
		
		
	</div>
	</c:if>
	
	<style>
		 .display{
			display: none;
		} 
		.timeDelivery{
			    width: 300%;
			    position: absolute;
			   
   
		}
		li.deliverySlotRadio .reset{margin: 0px auto !important;    height: 30px !important; line-height: 30px;} 
	</style>
	
	<script>
	$(document).ready(function() {
	    
	    	
	    	$(".reset").click(function(){
	    	
	    		$(this).parent().find(".scheduleDate input[type='radio']").prop('checked', false);
	    		var ussId=$(this).attr('data-ussid');
	    		var mplconfigModel= $('#mplconfigModel').val();
	    		
	    		var dataString = 'deliverySlotCost='+mplconfigModel+'&ussId='+ussId;
	    		$.ajax({                            
	    	 		url: ACC.config.encodedContextPath + "/checkout/multi/delivery-method/updateDeliverySlotCostForEd",
	    	 		data : dataString,
	    	 		success : function(response) {
	    	 			$("#totalWithConvField").empty().text(response+"0");
	    	 			//alert("response"+response);
	    	 		},
	    	 		error : function(error) {

	    	 		}
	    	 	});	
	    		
	    	});
    
	    	$(".timeTribhuvan").click(function(){
	    		var selectedElement = $(this);
	    		var selectedParent  = selectedElement.closest(".scheduleDate");
	    		var elem = selectedElement.next().next().next();
	    		var time; var temp; 
	    		var date;
	    		var mplconfigModel;
	    		var selectedUssId;
	    		var datanametimeall = selectedParent.find(".timeDelivery input[data-name='time']");
	    		var datanamedateall = selectedParent.find(".scheduleDate input[data-name='date']");
	    		var datanametimeallDates = selectedParent.closest(".row").find(".scheduleDate .timeDelivery input[data-name='time']");
	    		
	    		datanametimeallDates.each(function(){
	    			
	    			if(!$(this).prop('checked')){
	    			
	    			 mplconfigModel = $('#mplconfigModel').val();
	    			 selectedUssId = $('#selectedUssId').val();
	    			}else{
	    				temp = $(this).val();
	    				mplconfigModel=0;
	    				return false;
	    				
	    			}
	    		});
	    		
	    		
	    		
	    		
	    		if(selectedElement.attr('data-name')=='date'){
	    			date = selectedElement.val();
	    			datanamedateall.children('.timeDelivery').addClass("display");
	    			
	    			datanametimeall.prop('disabled',false);
	    			
	    			selectedParent.find(".timeDelivery input[data-name='time']").first().prop('checked','checked');
		    		    
		    			 if(elem.attr('id') == 1){
			    			
			    			elem.css({'left':'0px','position':'relative'});
			    		}
			    			
			    		else if(elem.attr('id') == 2){
			    			
			    			 elem.css({'left':'-74px','position':'relative'});
			    			
			    		}
			    		else if(elem.attr('id') == 3){
			    			
			    			 elem.css({'left':'-148px','position':'relative'});
			    		} 
		    		elem.removeClass("display");
		    		datanametimeall.each(function(){if($(this).prop('checked')){time =  $(this).val(); }});
	    		}else{
	    			time = selectedElement.val();
	    			date = selectedElement.attr('data-Date');
	    			
	    		}
	    		
    			//alert("time is "+time+" money is "+mplconfigModel + "date is "+date);
	    		var dataString = 'deliverySlotCost='+mplconfigModel+'&deliverySlotDate='+date+'&deliverySlotTime='+time+'&ussId='+selectedUssId;
	    		$.ajax({                            
	    	 		url: ACC.config.encodedContextPath + "/checkout/multi/delivery-method/deliverySlotCostForEd",
	    	 		data : dataString,
	    	 		success : function(response) {
	    	 			$("#totalWithConvField").empty().text(response+"0");
	    	 			//alert("response"+response);
	    	 		},
	    	 		error : function(error) {

	    	 		}
	    	 	});	
	    		
	    	});
	    	
	    	
	    	
	});
	</script>


