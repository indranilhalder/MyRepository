<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user" %>
<input type="hidden" id="scheduledDeliveryOrderCode"
	value="${orderDetail.code}" />
<div id="showOrderDetails">
	<div class="modal-dialog">
		<div class="modal-content">
			<form class="form-horizontal">
				<div class="modal-body">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">×</span>
					</button>
					<h4>
						<spring:theme
							code="text.accountOrderDetails.scheduledDeliveryDate.dateAndTime" />
					</h4>

					<spring:theme
						code="text.accountOrderDetails.scheduledDeliveryDate.msg" />
					<br /> <br /> <span class="orderReference"> <spring:theme
							code="text.accountOrderDetails.scheduledDeliveryDate.orderId" />
						<span id="#"># ${orderDetail.code}</span></span>
					<c:forEach items="${orderDetail.sellerOrderList}" var="sellerOrder"
						varStatus="status">
						<c:forEach items="${sellerOrder.entries}" var="entry"
							varStatus="entryStatus">
							<c:if test="${entry.mplDeliveryMode.code eq 'home-delivery'}">
								<div class="row scheduleDates">
									<div class="col-md-6 col-sm-6 leftBlockSchedule">
										<spring:theme code="text.orderHistory.transactionID" />
										# ${entry.transactionId}<br /> <b>Product Name:</b>
										${entry.brandName}<br /> <b>Seller ID:</b> #
										${entry.selectedSellerInformation.sellerID} <br /> <span>Price
											&nbsp;</span>
										<format:price priceData="${entry.amountAfterAllDisc}"
											displayFreeForZero="true" />
										<br />

										<p>
											<c:forEach items="${fullfillmentData}" var="fullfillmentData">
												<span>Fulfilled By:</span>
												<c:if test="${fullfillmentData.key == entry.transactionId}">
													<c:set var="fulfilmentValue"
														value="${fullfillmentData.value}">
													</c:set>
													<c:choose>
														<c:when test="${fulfilmentValue eq 'tship'}">
															<span><spring:theme
																	code="product.default.fulfillmentType" /></span>
														</c:when>
														<c:otherwise>
															<span>${entry.selectedSellerInformation.sellername}</span>
														</c:otherwise>
													</c:choose>
												</c:if>
											</c:forEach>
										</p>
									</div>
									<c:forEach var="txnScheduleData"
										items="${txnScheduleData.entries}">
										<c:if test="${txnScheduleData.key eq  entry.transactionId}">

											<div class="col-md-6 col-sm-6 scheduleRadio">
												<div class="sideheadsch">Preferred date of delivery</div>
												<div class="row">
													<c:set var="dateSlotId" value="0" scope="page"></c:set>

													<c:forEach var="txnDateData"
														items="${txnScheduleData.value}">
														<c:set var="dateSlotId" value="${dateSlotId + 1}"
															scope="page"></c:set>

														<div class="col-md-4 col-sm-4 col-xs-4">
															<input type="radio" name="dateRadio"
																class="scheduleDateRadio" value="${txnDateData.key}">
															<span>${txnDateData.key}</span>



															<c:choose>
																<c:when test="${dateSlotId ==1}">
																	<div class="timeDelivery" id="${dateSlotId}">
																		<div class="sideheadsch">Preferred time of
																			delivery</div>
																		<c:set var="timeSlotId" value="0" scope="page"></c:set>
																		<c:forEach var="txnTimeData"
																			items="${txnDateData.value}">
																			<c:set var="timeSlotId" value="${timeSlotId + 1}"
																				scope="page"></c:set>

																			<div class="row" id="${dateSlotId}${timeSlotId}">
																				<div class="col-md-4 col-sm-4 col-xs-4">
																					<input type="radio" name="timeRadio"
																						class="scheduleTimeRadio" disabled="disabled"
																						id="${dateSlotId}${timeSlotId}time"
																						data-date="${txnDateData.key}"
																						data-txnId="${txnScheduleData.key}"
																						value="${txnTimeData}"> ${txnTimeData}
																				</div>

																			</div>


																		</c:forEach>
																	</div>
																</c:when>
																<c:otherwise>
																	<div class="timeDelivery display" id="${dateSlotId}">
																		<div class="sideheadsch">Preferred time of
																			delivery</div>
																		<c:set var="timeSlotId" value="0" scope="page"></c:set>
																		<c:forEach var="txnTimeData"
																			items="${txnDateData.value}">
																			<c:set var="timeSlotId" value="${timeSlotId + 1}"
																				scope="page"></c:set>

																			<div class="row" id="${dateSlotId}${timeSlotId}">
																				<div class="col-md-4 col-sm-4 col-xs-4">
																					<input type="radio" name="timeRadio"
																						class="scheduleTimeRadio" disabled="disabled"
																						id="${dateSlotId}${timeSlotId}time"
																						value="${txnTimeData}"> ${txnTimeData}
																				</div>

																			</div>


																		</c:forEach>
																	</div>
																</c:otherwise>

															</c:choose>

														</div>

													</c:forEach>


												</div>


											</div>

										</c:if>


									</c:forEach>
								</div>
							</c:if>
						</c:forEach>
					</c:forEach>

				</div>
				<p style="clear: both;"></p>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary submitSchedule"
						id="reschedule">Re-Schedule</button>
				</div>
			</form>
		</div>
	</div>
</div>

<style>
.display {
	display: none;
}

.timeDelivery {
	width: 300%;
	position: absolute;
}
</style>

<script>
 $(document).ready(function() {
      $(".scheduleDateRadio").click(function(){
      
       
       //alert("hi");
       $(".scheduleDateRadio").next().next().addClass("display");
       
       var elem = $(this).next().next();
        //alert(elem.attr('class'));
       if(elem.attr('id') == 1){
    	   $("#"+elem.attr('id')+" .scheduleTimeRadio").prop('disabled',false);

    	   $("#"+elem.children().next().attr('id')+"time").prop('checked','checked');

         elem.css({'left':'0px','position':'relative'});
         
        }
         
        else if(elem.attr('id') == 2){
        	$("#"+elem.attr('id')+" .scheduleTimeRadio").prop('disabled',false);
        	//alert("#"+elem.children().next().next().attr('id')+"time");
        	$("#"+elem.children().next().attr('id')+"time").prop('checked','checked');
          elem.css({'left':'-76px','position':'relative'});
         
        }
        else if(elem.attr('id') == 3){
        	$("#"+elem.attr('id')+" .scheduleTimeRadio").prop('disabled',false);
        	$("#"+elem.children().next().attr('id')+"time").prop('checked','checked');
          elem.css({'left':'-150px','position':'relative'});
        }  
       elem.removeClass("display");
        
       
      });
      $("#reschedule").click(function(){ 
    	  var rescheduleData = {};
    	  var json='{"rescheduleDataList" : [';
    	  var jsonLeg=json.length;
    	  
    	  $( ".scheduleTimeRadio" ).each(function( index ) {
    			 if($(this).prop("checked")){
    	   				
    	   				var selectedDate = $(this).attr('data-date');
    	   				var selectedTxnId = $(this).attr('data-txnId');
    	   				var selectedTime = $(this).val();

   						alert(selectedTime+"date is "+selectedDate+" txn id is "+ selectedTxnId);
   						
   						rescheduleData['transactionId'] = selectedTxnId;
   						
   						rescheduleData['date'] = selectedDate;
   						
   						rescheduleData['time'] = selectedTime;
                      
   						json=json.concat(JSON.stringify(rescheduleData));
                        json=json.concat(',');
   					
    	   			}
    			
     	  });	
    	  if(json.length > jsonLeg){
    	  json = json.substring(0, json.length-1);
    	                        }
    	  json=json.concat(']}');
  			console.log(rescheduleData);
  			var orderCode = $('#scheduledDeliveryOrderCode').val();
  			var orderId=orderCode;
  			debugger;
  			 $.ajax({
 				type : "GET",
 				url : ACC.config.encodedContextPath +"/my-account/" +orderCode+"/reScheduledDeliveryDate",
 				data : "entryData=" + json,
 				contentType: "html/text",
 				success : function(response) {
 					$("#changeAddressPopup").empty().html(result).show();
 				},
  			 failure:function(data){
  			 }
 			}); 
  			});
 });
 
 </script>