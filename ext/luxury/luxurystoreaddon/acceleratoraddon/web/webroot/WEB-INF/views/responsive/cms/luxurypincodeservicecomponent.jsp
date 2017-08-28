<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


    <span id="deliveryPretext" style="display:none;"><spring:theme code="mpl.pdp.delivery.pretext"/></span>
    <span id="deliveryPosttext" style="display:none;"><spring:theme code="mpl.pdp.delivery.posttext"/></span>
	<ul class="delivery-block">
	<li><h3><spring:theme code="pdp.delivery.options"/></h3><%-- <p id="pdpPinCodeAvailable"><spring:theme code="product.pincode"/></p> --%>
	
		<a class="luxuyChangepincode" title="Change pincode">Change pincode</a>
		<div class="inline-form mt-10">
		 <c:choose>
		 <c:when test="${not empty pincode}">
		 	<input id="pinCodeAvailable" type="hidden" value="${true}"/>		 	
			<input id="pin" type="text" value="${pincode}" maxlength="6" onkeypress="return isNum(event)" style="display: none;"/>
			<button class="orange submit" id="pdpPincodeCheck" style="display: none;"><spring:theme code="text.submit"/></button>
		    </c:when>
		    <c:otherwise>
		    	<input id="pinCodeAvailable" type="hidden" value="${false }"/>
		    	<input id="pin" type="text" placeholder="Pincode" maxlength="6" onkeypress="return isNum(event)"/>
		    	 <!-- TISEE-6552 fix  -->
				<button class="orange submit" id="pdpPincodeCheck"><spring:theme code="text.submit"/></button>
				
				<button class="gray submitDList" id="pdpPincodeCheckDList" style="display:none;"><spring:theme code="text.submit"/></button>
		    </c:otherwise>
		 </c:choose>
		</div>
		
			<span id="unsevisablePin" style="display:none;color:#ff1c47"><spring:theme code="pincode.unsevisable"/></span>
			<%--Tpr-634 - added for scope of improvement --%>
			<span class="pincodeErrorMsg">
				<span id="emptyPin" style="display:none;color:#ff1c47"><spring:theme code="product.empty.pincode"/></span>
				<span id="wrongPin" style="display:none;color:#ff1c47"><spring:theme code="pincode.invalid"/></span>
				<span id="unsevisablePin" style="display:none;color:#ff1c47"><spring:theme code="pincode.unsevisable"/></span>
				<span id="serviceablePin" style="display:none;color:#00994d"><spring:theme code="pincode.serviceable"/></span> <!-- Changes for TISPRM-20,65 -->
				<span id="unableprocessPin" style="display:none;color:#ff1c47"><spring:theme code="pincode.unableprocess"/></span>
	
			</span>
		
		</li>
		
		
	   <c:forEach var="entry" items="${deliveryModeMap}">
		<%-- Key: <c:out value="${entry.key}"/> --%>
		<c:if test="${entry.key eq 'home-delivery'}">
		<li class="do selected" id="homeli"> <p><spring:theme code="text.home.delivery"/></p> 
		 <c:forEach var="homeEntry" items="${entry.value}">
			 <c:if test="${homeEntry.key eq 'startForHome'}">
			 <input type="hidden" value="${homeEntry.value}" id="homeStartId"/>
			 </c:if>
			
			  <c:if test="${homeEntry.key eq 'endForHome'}">
			  <input type="hidden" value="${homeEntry.value}" id="homeEndId"/>
		     </c:if>
		    </c:forEach>
		  <span id="homeDate"></span>
			<%-- <li><a  id="home" class="HomeDelivery  home deliveryDisabled" style="display: block"> <span><spring:theme code="text.home.delivery"/></span> <span><c:out value="${entry.value}" /></span> --%>
			
		</li>
		  </c:if>
		  <c:if test="${entry.key eq 'express-delivery'}">
			<li class="do selected" id="expressli"> <p><spring:theme code="text.express.shipping"/></p> 
			 <c:forEach var="expressEntry" items="${entry.value}">
			 <c:if test="${expressEntry.key eq 'startForExpress'}">
			 <input type="hidden" value="${expressEntry.value}" id="expressStartId"/>
			 </c:if>
			 
			  <c:if test="${expressEntry.key eq 'endForExpress'}">
			  <input type="hidden" value="${expressEntry.value}" id="expressEndId"/>
		     </c:if>
		    </c:forEach>
			<span id="expressDate"> <c:out value="${entry.value}" /></span>
			</li>	
		</c:if> 
		
		<c:if test="${entry.key eq 'click-and-collect'}">
		    <c:set var="clickCollect" value="${entry }"/>
		</c:if> 
	</c:forEach>
	
	
	<!-- <li><a href="#" class="collect"><span>Click and
					Collect</span><span>Buy online, collect in-store</span></a></li> -->
	</ul>
	<ul class="delivery-block right-delivery-block">
		<li id="collectli" class="do selected"><p><spring:theme code="text.clickandcollect.shipping"/></p>
		<span style="display: block;">Buy online, collect in-store</span>
		 <c:forEach var="clickEntry" items="${clickCollect.value}">
	
			 <c:if test="${clickEntry.key eq 'startForClick'}">
			 <input type="hidden" value="${clickEntry.value}" id="clickStartId"/>
			 </c:if>
			 
			  <c:if test="${clickEntry.key eq 'endForClick'}">
			  <input type="hidden" value="${clickEntry.value}" id="clickEndId"/>
		     </c:if>
		    </c:forEach>
		    <span id="clickDate"><%-- <c:out value="${entry.value}" /> --%></span>
		    </li>
	</ul>

	
