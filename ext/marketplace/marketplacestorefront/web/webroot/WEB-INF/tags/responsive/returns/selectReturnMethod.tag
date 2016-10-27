<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="return" tagdir="/WEB-INF/tags/responsive/returns"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- Accordtion Panel 3 For Select Return Method -->
	
<div class="accordtionTataCliq thirdTataCliq col-md-12">
	<div class="accHeading"><spring:theme code="text.order.returns.selectreturnmethod"/></div>
	<div class="accContents returnMethod col-md-12">
		<div class="col-md-3 col-sm-6">
			<b><spring:theme code="text.order.returns.returnmethod"/> </b> <br />
			<div class="selectReturnMethod quick col-md-12 col-sm-12">
			<c:if test="${productRichAttrOfQuickDrop eq 'yes' &&  sellerRichAttrOfQuickDrop eq 'yes' }">
				<div class="selectRadio quickDropRadio col-md-2 col-sm-2 col-xs-2">
					<!-- <input id="QuickDrop" onclick="changeRadioColor('quick')"
						class="radioButton" type="radio" value="quick" name="returnMethod" /> -->
						<form:radiobutton id="QuickDrop" onclick="changeRadioColor('quick')"
						class="radioButton"  value="quickDrop" name="returnMethod" path="returnMethod" />
				</div>
				
				<div class="col-md-10 col-sm-10 col-xs-10">
					<b><spring:theme code="text.order.returns.quickdrop"/></b> <br />
					 <span><spring:theme code="text.order.returns.droppackagenearbystore"/></span>
				</div>
				</c:if>
			</div>
			<div class="selectReturnMethod scheduled col-md-12 col-sm-12">
				<div class="selectRadio col-md-2 col-sm-2 col-xs-2">
						<form:radiobutton id="QuickDrop" onclick="changeRadioColor('scheduled')"
						class="radioButton" value="schedule" name="returnMethod" path="returnMethod" />
				</div>
				<div class="col-md-10 col-sm-10 col-xs-10">
					<b><spring:theme code="text.order.returns.schedulepickup"/></b> <br /> 
					<span><spring:theme code="text.order.returns.pickedupfromaddress"/></span>
				</div>
			</div>
			<div class="selectReturnMethod self col-md-12 col-sm-12">
				<div class="selectRadio col-md-2 col-sm-2 col-xs-2">

						<form:radiobutton id="QuickDrop" onclick="changeRadioColor('self')"
						class="radioButton" value="selfShipment" name="returnMethod" path="returnMethod" />
				</div>
				<div class="col-md-10 col-sm-10 col-xs-10">
					<b><spring:theme code="text.order.returns.selfcourier"/></b> <br /> <span><spring:theme code="text.order.returns.sendthepackageback"/></span>
				</div>
			</div>
		</div>
		

		<c:if test="${productRichAttrOfQuickDrop eq 'yes' &&  sellerRichAttrOfQuickDrop eq 'yes' }"> 
		<!-- Quick Drop -->
		<div class="col-md-9 quickDrop">
			<div class="col-md-4 col-sm-6">
				<b><spring:theme code="text.order.returns.quickdrop"/> : </b> <br /> <span><spring:theme code="text.order.returns.listofstores"/></span>
				<div class="quickDropArea" data-loaded="false">

					 <c:forEach var="returnableStore" varStatus="i" items="${returnableSlaves}">
					 
				<div class="selectquickDrop quickDropRadio${i.index} col-md-12">
						<div class="selectRadio col-md-2 col-sm-2 col-xs-2">
							<form:checkbox path="storeIds" 
								onclick="updatePointersNew('quickDropRadio${i.index}', '${returnableStore.geoPoint.latitude}', '${returnableStore.geoPoint.longitude}', '${returnableStore.displayName}, ${returnableStore.address.town},${returnableStore.address.postalCode}')"
								class="checkButton" value="${returnableStore.slaveId}"
								/>
						</div>
						<div class="col-md-10 col-sm-10 col-xs-10">
							<b>${returnableStore.displayName}</b> <br /> <span>${returnableStore.address.line1 }, ${returnableStore.address.line2},${returnableStore.address.line3},
								 ${returnableStore.address.town},${returnableStore.address.postalCode}</span>
						</div>
					</div>
				</c:forEach> 
				
				<%-- Radios Code 
				
				<c:forEach var="returnableStore" varStatus="i" items="${returnableSlaves}">
				<div class="selectquickDrop quickDropRadio1 col-md-12">
						<div class="selectRadio col-md-2 col-sm-2 col-xs-2">
							<input
								onclick="updateMap('quickDropRadio${i.index}', '${returnableStore.geoPoint.latitude}', '${returnableStore.geoPoint.longitude}', 'This Map Pointer Click Text')"
								class="radioButton" type="radio" value="replace"
								name="QuickDrop" checked />
						</div>
						<div class="col-md-10 col-sm-10 col-xs-10">
							<b>${returnableStore.displayName}</b> <br /> <span>${returnableStore.address.line1 }, ${returnableStore.address.line2},${returnableStore.address.line3},
								 ${returnableStore.address.town},${returnableStore.address.postalCode}</span>
						</div>
					</div>
				</c:forEach> --%>
				
				</div>
					<div class="quickDropAreaPincode">
					<c:choose>
					
					<c:when test="${subOrderEntry.mplDeliveryMode.code eq 'click-and-collect'}">
					<div class="row clearfix">
						<div class="col-md-6 col-sm-6 col-xs-6">
							<div class="form-group">
								 <input type="text" class="form-control" id="pin" maxlength="6" placeholder="Enter Pincode" value="${subOrderEntry.deliveryPointOfService.address.postalCode}" onkeypress="return isNum(event)" />
							</div>
						</div>
						<div class="col-md-6 col-sm-6 col-xs-6">
							<button id="subOrderEntry" type="button" class="btn btn-primary pincodeSubmit">Submit</button>
						</div>
					</div>
					<%-- <span>Pincode :</span><input id="pin" type="text" value="${subOrderEntry.deliveryPointOfService.address.postalCode}" placeholder="Enter Pincode" maxlength="6" onkeypress="return isNum(event)"/>
					<span><button type="button" class="light-blue" value="SUBMIT" id="mplDeliveryMode"></button></span> --%>
					</c:when>
					<c:otherwise>
					<div class="row clearfix">
						<div class="col-md-6 col-sm-6 col-xs-6">
							<div class="form-group">
								 <input type="text" class="form-control" id="pin" maxlength="6" placeholder="Enter Pincode" value="${subOrder.deliveryAddress.postalCode } " onkeypress="return isNum(event)" />
							</div>
						</div>
						<div class="col-md-6 col-sm-6 col-xs-6">
							<button id="subOrder" type="button" class="btn btn-primary pincodeSubmit">Submit</button>
						</div>
					</div>
					</c:otherwise>
					
					</c:choose>
							
					</div>
			</div>
			<div class="col-md-8 col-sm-12 mapArea">
				<div id="map-canvas"></div>
				<!-- <div class="mapDistance">This store is 8 KM away from your
					current location.</div> -->
			</div>
		</div>
		</c:if> 
		<!-- Scheduled Pickup -->

		<div class="col-md-9 scheduledPickup">
			<div class="col-md-6 col-sm-6 col-xs-12">
				<div class="col-md-12 col-xs-12">
					<%-- <b><spring:theme code="text.order.returns.schedulepickup"/> : </b> <br /> <span><spring:theme code="text.order.returns.choosepickupaddress"/></span> <span style=""><a href="#" class="addNewAddressPopup">Add New Address</a></span> --%>
					<div class="scheduledPickupArea">
						<c:forEach var="address" items="${addressData}" varStatus="i">

							<div
								class="selectScheduledPickup  address${i.count} col-md-12 col-sm-12 col-xs-12">
								<div class="selectRadio col-md-2 col-sm-2 col-xs-2">
									<input onclick="showPickupTimeDate('address${i.count}')"
										class="radioButton" type="radio" value="schedule"
										name="selectScheduledPickup" />
								</div>
								<div class="col-md-6 col-sm-6 col-xs-6 updateaddress${i.count}">
									<ul >
										<li>${fn:escapeXml(address.title)}&nbsp;<span class="firstName">${fn:escapeXml(address.firstName)}</span>&nbsp;<span class="lastName">${fn:escapeXml(address.lastName)}</span></li>
										<li><span class="addressline1">${fn:escapeXml(address.line1)}</span></li>
										<li><span class="addressline2">${fn:escapeXml(address.line2)}</span></li>
										<li><span class="addressTown">${fn:escapeXml(address.town)}</span></li>
										<li><span class="state">${fn:escapeXml(address.state)}</span></li>
										<li><span class="postalCode">${fn:escapeXml(address.postalCode)}</span></li>
										<li><span class="country">${fn:escapeXml(address.country.name)}</span></li>
										<li><span class="phoneNumber">${fn:escapeXml(address.phone)}</span></li>
										<li style="display: none" id="addressUniqueId">${address.id}</li>
									</ul>
								</div>
								<div class="col-md-4 col-sm-4 col-xs-4 editAddress">
									<a href="#" class="changeAddressLinkPop" data-class="address${i.count}"><spring:theme code="text.order.returns.editaddress"/></a>
								</div>		
							</div> 
						</c:forEach>
						
					</div>
				</div>
			</div>
			<div class="col-md-6 col-sm-12 col-xs-12">
				<b><spring:theme code="text.order.returns.selectpickupdate"/></b><br /> <br />
				<div class="row col-md-12 col-sm-6 col-xs-12 selectDateTime">
					<div class="sideHead"><spring:theme code="text.order.returns.preferreddate"/></div>
					<br />
					<c:forEach var="returnDate" items="${returnDates}" varStatus="i">
					<div class="selectRadio date col-md-4 col-sm-4 col-xs-4">
						<form:radiobutton class="radioButton selectRadioDate${i.index}" value="${returnDate}"
							name="selectDate" path="scheduleReturnDate" /> <br />
						<p style="clear: both"></p>
						<fmt:parseDate value="${returnDate}" var="scheduleReturnDate" pattern="dd-MM-yyyy" />
						<div><fmt:formatDate value="${scheduleReturnDate}" pattern="MMM dd"/></div> 
						<%-- <div>${returnDate}</div> --%>
						
					</div>
					</c:forEach>
					<br /> <br /> <br />
				</div>


				<div class="row col-md-12 col-sm-6  col-xs-12 selectDateTime">
					<div class="sideHead"><spring:theme code="text.order.returns.preferredtime"/></div>
					<br />

					<c:forEach var="timeSlot" items="${timeSlots}">
						<div class="selectRadio time col-md-4 col-sm-4 col-xs-4">
							<form:radiobutton  class="radioButton" value="${timeSlot}" path="scheduleReturnTime"
								name="scheduleReturnTime" /> <br />
							<p style="clear: both"></p>

							<div>${timeSlot}</div>
						</div>
					</c:forEach>


				</div>
			</div>
		</div>


		<!-- self Courier -->

		<div class="col-md-9 selfCourier">
			<b><spring:theme code="text.order.returns.selfcourier"/> </b><br/><br />
			<div class="col-md-12 selfCourierArea">
				<div class="col-md-2 col-xs-12 col-sm-2">
					<b><spring:theme code="text.order.returns.step1"/></b> <br /><spring:theme code="text.order.returns.dispatchproduct"/>
				</div>
				<div class="col-md-1 col-xs-1 hidden-xs col-sm-1">
					<span class="fa-angle-right"></span>
				</div>
				<div class="col-md-2 col-xs-12 col-sm-2">
					<b><spring:theme code="text.order.returns.step2"/></b> <br /><spring:theme code="text.order.returns.downloaddocuments"/> <br /> 
					
					<a href=""><spring:theme code="text.order.returns.document1"/></a><br />
					<a href=""><spring:theme code="text.order.returns.document2"/></a>
				</div>
				<div class="col-md-1 col-xs-1 hidden-xs col-sm-1">
					<span class="fa-angle-right"></span>
				</div>
				<div class="col-md-2 col-xs-12 col-sm-2">
					<b><spring:theme code="text.order.returns.step3"/></b> <br /> <spring:theme code="text.order.returns.provideawbnumber"/>
				</div>
			</div>

		</div>
		<p style="clear: both"></p>
		<div class="button_holder">
			<button type="button" onclick="goBackToSecondTataCliq()"
				class="light-blue newRequest"><spring:theme code="text.order.returns.backbutton"/></button>
			<button type="submit" id="saveBlockData" class="btn btn-primary">	
<spring:theme code="text.returRequest.continueButton"/></button>
				</div>
		</div>
	</div>

<div id="hiddenFields">
<form:hidden path="firstName"  id="firstName"/>
<form:hidden path="addressType"  />
<form:hidden path="lastName"  id="lastName"/>
<form:hidden path="addrLine1"  id="addressLine1"/>
<form:hidden path="addrLine2"  id="addressLine2"/>
<form:hidden path="addrLine3" />
<form:hidden path="landMark" /> 
<form:hidden path="state" id="stateListBox"/> 
<form:hidden path="pincode"  id="pincode"/>
<form:hidden path="phoneNumber"  id="phoneNumber"/>
<form:hidden path="city"  id="city"/>
<form:hidden path="country"   id="country"/>
<form:hidden path="isDefault" />
<form:hidden path="orderCode" value="${orderCode}"/>
<form:hidden path="ussid"  value="${subOrderEntry.selectedUssid}" />
<form:hidden path="transactionId" value="${subOrderEntry.transactionId}" />
	<c:choose>
		<c:when test="${subOrder.mplPaymentInfo.paymentOption eq 'COD'}">
			<form:hidden path="isCODorder" value="Y" />
		</c:when>
		<c:otherwise>
			<form:hidden path="isCODorder" value="N" />
		</c:otherwise>

	</c:choose>

</div>
<style>
.firstTataCliq span{
	margin-bottom: -4px !important;
    display: block !important;
}
.secondTataCliq select{
	width: 100%;
}
</style>




<script>
$(document).ready(function(){
	$("#subOrder").click(function() {
		var pincode=$("#pin").val();
	    var ussid=$("#ussid").val();
	    var dataString = 'pin=' + pincode + '&ussid=' + ussid;
		  $.ajax({
			  url: ACC.config.encodedContextPath+"/my-account/returns/pincodeServiceCheck",
			  data : dataString,
			  contentType : "application/json; charset=utf-8",
			  success: function(data) {
				  if (data == "" || data == []
					|| data == null) {
					  $('.quickDropArea').html("<div>Stores Not Avalible</div>");
				  }else{
					 // alert("Stores are :"+data); 
					  $('.quickDropArea').empty();
					  for(var i=0; i<data.length; i++){
						 
						  var tempHtml = "<div class='selectquickDrop quickDropRadio"+i+" col-md-12'>"
						  +"<div class='selectRadio col-md-2 col-sm-2 col-xs-2'>"	
						  +"<input id='storeIds"+i+"' name='storeIds' onclick='updatePointersNew('quickDropRadio"+i+"','"+ data[i].geoPoint.latitude+"','"+ data[i].geoPoint.longitude+"','"+ data[i].displayName+","+data[i].address.town+","+ data[i].address.postalCode+"')' class='checkButton' type='checkbox' value = '"+data[i].slaveId+"' /></div>"
								+"<div class='col-md-10 col-sm-10 col-xs-10'>"
							+"<b>"+data[i].displayName+"</b> <br /> <span>"+ data[i].address.line1+","+ data[i].address.line2+","+ data[i].address.line3+"," 
								 +data[i].address.town+","+data[i].address.postalCode+"</span></div></div>";
								// console.log(tempHtml);
						  $('.quickDropArea').append(tempHtml);
						  }
				  }
			  
			  },
			  error:function(data){
				  $('.quickDropArea').html("<div>Oops Some thing wrong.Please try other pincode</div>");
			  }
		  });
	});
});



</script>
<style>
.pincodeSubmit, .pincodeSubmit:hover, .pincodeSubmit:active, .pincodeSubmit:visited, .pincodeSubmit:focus {
	background: #a9143c;
	color: #fff;
	font-weight: bold;
	height: 40px;
}
</style>