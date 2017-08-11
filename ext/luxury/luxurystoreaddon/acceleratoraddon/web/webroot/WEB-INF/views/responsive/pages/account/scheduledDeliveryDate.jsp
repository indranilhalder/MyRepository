<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty stringMessage}">
${stringMessage}
</c:if>
<c:if test="${empty stringMessage}">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="sec"
		uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>
<input type="hidden" id="scheduledDeliveryOrderCode"
		value="${orderDetail.code}" />
	<div id="showOrderDetails">
		<div class="modal-dialog">
			<div class="modal-content">
				<form class="form-horizontal">
					<div class="modal-body">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">�</span>
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
							<div class="row  clearfix">
								<c:set var="txnId" value="0" scope="page"></c:set>
								<c:forEach var="txnScheduleData"
									items="${txnScheduleData.entries}">
										<div class="row scheduleDates" style="min-height: 200px; overflow-y: scroll;vertical-align: bottom;">
									<div class="col-md-6 col-sm-6 leftBlockSchedule clearfix">
									<c:forEach var="entry"
									items="${orderDetail.entries}">
									<c:if test="${entry.product.code eq txnScheduleData.key}">
									<b><spring:theme
								code="text.scheduledDeliveryDate.productName" /></b>
									${entry.brandName}<br /> 
									<!-- R2.3 TISRLUAT-1043 start -->
									<%-- <b>Seller ID:</b> # 
									${entry.selectedSellerInformation.sellerID} <br /> --%>  
									<!-- R2.3 TISRLUAT-1043 end -->
									<b>Quantity :
									</b>
									${entry.quantity}<br />
									 <span><b> Price :</b>
										&nbsp;</span>
									<format:price priceData="${entry.totalPrice}"
										displayFreeForZero="true" />
									<br />
									</c:if>
									</c:forEach>
								</div>
								<div class="hidden-sm" style="height: 20px;"></div>
									<c:set var="txnId" value="${txnId + 1}" scope="page"></c:set>
									<div class="col-md-6 col-sm-6 scheduleRadio clearfix">
										<div class="sideheadsch"><spring:theme
								code="text.order.deliveryDate" /></div>
										<div class="row">
											<c:set var="dateSlotId" value="0" scope="page"></c:set>

											<c:forEach var="txnDateData" items="${txnScheduleData.value}">
												<c:set var="dateSlotId" value="${dateSlotId + 1}"
													scope="page"></c:set>
												<div class="col-md-4 col-sm-4 col-xs-4 scheduleDate">
													<c:choose>
														<c:when test="${dateSlotId ==1}">
														<input type="radio" data-name="dateRadio"
														name="date${txnId}" class="timeTribhuvan"
														value="${txnDateData.key}"  > <span>${txnDateData.key}</span>
															<div class="timeDelivery" data-timeBlock="${dateSlotId}">
																<div class="sideheadsch"><spring:theme code="text.order.deliveryTime" /></div>
																<c:set var="timeSlotId" value="0" scope="page"></c:set>
																<c:forEach var="txnTimeData"
																	items="${txnDateData.value}">
																	<c:set var="timeSlotId" value="${timeSlotId + 1}"
																		scope="page"></c:set>

																	<div class="row" 
																		data-txn="${txnId}">
																		<div class="col-md-4 col-sm-4 col-xs-4">
																			<input type="radio" data-name="timeRadio"
																				class="timeTribhuvan" name="time${txnId}${dateSlotId}"
																				
																				data-date="${txnDateData.key}" 
																				data-txnId="${txnScheduleData.key}"
																				value="${txnTimeData}"> ${txnTimeData}
																		</div>
																	</div>
																</c:forEach>
															</div>
														</c:when>
														<c:otherwise>
														<input type="radio" data-name="dateRadio"
														name="date${txnId}" class="timeTribhuvan"
														value="${txnDateData.key}"  > <span>${txnDateData.key}</span>
															<div class="timeDelivery display"
																data-timeBlock="${dateSlotId}">
																<div class="sideheadsch"><spring:theme
								code="text.order.deliveryTime" /></div>
																<c:set var="timeSlotId" value="0" scope="page"></c:set>
																<c:forEach var="txnTimeData"
																	items="${txnDateData.value}">
																	<c:set var="timeSlotId" value="${timeSlotId + 1}"
																		scope="page"></c:set>
																	<div class="row" 
																		data-txn="${txnId}">
																		<div class="col-md-4 col-sm-4 col-xs-4">
																			<input type="radio" data-name="timeRadio"
																				name="time${txnId}${dateSlotId}" class="timeTribhuvan"
																				data-date="${txnDateData.key}"
																				data-txnId="${txnScheduleData.key}"
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
									</div>
								</c:forEach>
							</div>
					</div>
					<p style="clear: both;"></p>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary submitSchedule"
							id="reschedule"><spring:theme
								code="text.order.reSchedule" /></button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<style>
	.scheduleDate input[type="radio"]{
	position: static;
	overflow: auto;
	border: 1px solid #CCC;
	display: block !important;
	cursor: pointer;
	}
	
.display {
	display: none;
}

.timeDelivery {
	margin-top:20px;
	width: 300%;
	position: absolute;
}

.submitSchedule, .submitSchedule:hover, .submitSchedule:active {
	background: #a9143c;
	color: #fff;
	font-weight: bold;
	padding-left: 10px;
	padding-right: 10px;
}

.sideheadsch {
	font-weight: bold;
}
</style>
<script>
		$(document).ready(function() {
			
			$('.scheduleRadio').each(function(){
				
				 $(this).find(".scheduleDate:first > input").attr("data-picked","yes");
				$(this).find(".scheduleDate:first > input").prop("checked",true);
				$(this).find(".scheduleDate .timeDelivery input[data-name=timeRadio]:first").prop("checked",true);
				$(this).find(".scheduleDate .timeDelivery input[data-name=timeRadio]:first").attr("data-picked","yes");
				})
			
			 var date;	var time; var productid;
							$(".close,.wrapBG").click(function() {
								$("#changeAddressPopup").hide();
								$("#showOrderDetails").hide();
								$(".wrapBG").hide();
							});
							
							$(".timeTribhuvan").click(function() {

												var selectedElement = $(this);
												var selectedParent = selectedElement.closest(".scheduleDates");
												var selectedGrandParent = selectedElement.closest(".scheduleRadio");
												var elem = selectedElement.next().next();
												selectedGrandParent.find(".timeTribhuvan").attr('data-picked',"");	
												if (selectedElement.attr('data-name') == 'dateRadio') {
													
													selectedParent.find(".scheduleDate .timeDelivery").addClass("display");
													if (elem.attr('data-timeBlock') == 1) {
														selectedElement.attr("data-picked","yes");
														elem.find(".timeTribhuvan").first().attr("data-picked","yes");
														elem.find(".timeTribhuvan").first().prop("checked","checked");
														elem.css({'left' : '0%','position' : 'relative'});
													}
													if (elem.attr('data-timeBlock') == 2) {
														elem.find(".timeTribhuvan").first().attr("data-picked","yes");
														selectedElement.attr("data-picked","yes");
														elem.find(".timeTribhuvan").first().prop("checked","checked");
														elem.css({'left' : '-100%','position' : 'relative'});
													} else if (elem.attr('data-timeBlock') == 3) {
														elem.find(".timeTribhuvan").first().attr("data-picked","yes");
														selectedElement.attr("data-picked","yes");
														elem.find(".timeTribhuvan").first().prop("checked","checked");
														elem.css({'left' : '-200%','position' : 'relative'});
													}
													 /* date = elem.find("input.timeTribhuvan[type=radio]:checked").attr("data-date");
													 time = elem.find("input.timeTribhuvan[type=radio]:checked").val();
													 productid = elem.find("input.timeTribhuvan[type=radio]:checked").attr("data-txnId"); */
													 
												} else {
													
													selectedElement.attr("data-picked","yes");
													/* time = selectedElement.val();
													productid = selectedElement.attr("data-txnId"); */
												}
												
												elem.removeClass("display");
												

											});

					$("#reschedule").click(function(){  
			             var rescheduleData = {};
						 var json = '{"rescheduleDataList" : [';
						 var jsonLeg = json.length;
						 $("input.timeTribhuvan[data-name='timeRadio'][data-picked='yes']").each(function(){
							 
								  var selectedDate = $(this).attr('data-date');
								  var selectedTxnId = $(this).attr('data-txnId');
								  var selectedTime = $(this).val();
								    rescheduleData['productCode'] = selectedTxnId;
									rescheduleData['date'] = selectedDate;
									rescheduleData['time'] = selectedTime;
									json = json.concat(JSON.stringify(rescheduleData));
									json = json.concat(',');	
																
							});								
							if (json.length > jsonLeg) {
								json = json.substring(0,json.length - 1);
							}
							json = json.concat(']}');
							var orderCode = $('#scheduledDeliveryOrderCode').val();
							var orderId = orderCode;
						$.ajax({
								type : "GET",
								url : ACC.config.encodedContextPath+ "/my-account/"
								                + orderCode
												+ "/reScheduledDeliveryDate",
								data : "entryData="+ json,
								contentType : "html/text",
								success : function(response){
								           	          $("#changeAddressPopup").hide();
								 	                  $("#showOrderDetails").hide();
									                  $("#otpPopup").empty().html(response).show();
										  },	
										failure : function(data) {
										}
									}); 
						});
	});
	</script>

</c:if>