<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
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
<c:set var="firstShippedItem" value="true"></c:set>


<c:if test="${hasShippedItems}">
<script>
	$(document).ready(function(){
		var tmp = false;
		/* $(".slotRadio").mouseup(function(){
			 Set Values For Ajax Call 
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
		}); */
    	
/*     	$(".reset").click(function(){
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
 		
		
	}); */
	function updateSlotForEntry(element)
	{
		if($(element).is(":checked"))
		{
			var mplconfigModel = $(element).attr("data-deliveryCost");
			var selectedUssId = $(element).attr("data-ussid");
			var date = $(element).attr("data-deliverySlotDate");
			var time = $(element).attr("data-deliverySlotTime");
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
		}
	}
	function resetSlotForEntry(element,radioElement)
	{
		$("input:radio[name="+radioElement+"]").prop('checked', false);
		var ussId=$(element).attr('data-ussid');
		var mplconfigModel= $(element).attr('data-deliveryCost');
		var dataString = 'deliverySlotCost='+mplconfigModel+'&ussId='+ussId;
		
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
	}
</script>
<div class="checkout-shipping-items">
	<div class="checkout-headers">
		<h1 class="title-name">
			<spring:theme code="checkout.single.deliverySlot.chooseDeliverySlotOption"></spring:theme>
		</h1>
	</div>
	<ul id="deliveryradioul" class="">
		<c:set var="scheduleIndex" value="0" scope="page"></c:set>
		<c:forEach items="${cartData.entries}" var="entry">
			<c:set var="scheduleIndex" value="${scheduleIndex + 1}" scope="page"></c:set>
			<c:url value="${entry.product.url}" var="productUrl" />
			<li class="item">
				<ul>
					<li class="deliverySlotProduct">
						<div>
							<div class="thumb product-img">
								<c:choose>
									<c:when test="${fn:toLowerCase(entry.product.luxIndicator)=='luxury'}">
										<a href="${productUrl}"><product:productPrimaryImage
												product="${entry.product}" format="luxuryCartIcon" /></a>
									</c:when>
									<c:otherwise>
										<a href="${productUrl}"><product:productPrimaryImage
												product="${entry.product}" format="thumbnail" /></a>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</li>
					<li class="deliverySlotRadio">
						<%-- <input type="hidden" id="mplconfigModel" name="mplconfigModel" value="${mplconfigModel}"/>
						<input type="hidden" id="selectedUssId" name="selectedUssId" value="${entry.selectedUssid}"/> --%>
						<div class="" id="content"  data-ajax="3bu1">
							<c:choose>
								<c:when test="${not empty entry.deliverySlotsTime}">
									<div class="">
										<c:forEach items="${entry.deliverySlotsTime}" var="dateSlots">
											<fmt:parseDate value="${dateSlots.key}" var="parseddeliveryDate" pattern="dd-MM-yyyy" />
											<div class="">
												<c:forEach items="${dateSlots.value}" var="timeSlots">
													<span>	
														<input type="radio" class="" name="date${scheduleIndex}" style="display:block;" data-ussid="${entry.selectedUssid}" data-deliveryCost="${mplconfigModel}" data-deliverySlotDate="${dateSlots.key}"  data-deliverySlotTime="${timeSlots}" value="" onclick="updateSlotForEntry(this);">
														<fmt:formatDate value="${parseddeliveryDate}" pattern="d  MMMM"/>
														<span class="dateTime1">&nbsp;(${timeSlots})</span>
														<c:choose>
															<c:when test="${empty mplconfigModel}">
																<span class="greyText">(Free)</span>
															</c:when>
															<c:otherwise>
																<span class="">(${currencySymbol} ${mplconfigModel})</span>
															</c:otherwise>
														</c:choose>
													</span>
												</c:forEach>
											</div>
										</c:forEach>
										<div class="" align="left">
											<button class="" type="button" data-ussid="${entry.selectedUssid}"  data-deliveryCost="${mplconfigModel}" disabled="disabled" onclick="resetSlotForEntry(this,'date${scheduleIndex}');">Reset</button>
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