<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


    <span id="deliveryPretext" style="display:none;"><spring:theme code="mpl.pdp.delivery.pretext"/></span>
    <span id="deliveryPosttext" style="display:none;"><spring:theme code="mpl.pdp.delivery.posttext"/></span>
	<ul class="delivery-block">
	<li><p><spring:theme code="product.pincode"/></p>
		<div class="inline-form">
		 <c:choose>
		 <c:when test="${not empty pincode}">
			<input id="pin" type="text" value="${pincode}" maxlength="6" onkeypress="return isNum(event)"/>
		    </c:when>
		    <c:otherwise>
		    	<input id="pin" type="text" placeholder="Pincode" maxlength="6" onkeypress="return isNum(event)"/>
		    </c:otherwise>
		 </c:choose>
			<button class="orange submit"><spring:theme code="text.submit"/></button>
		</div></li>
		
		<span>
		<span id="emptyPin" style="display:none;color:red"><spring:theme code="product.empty.pincode"/></span>
		<span id="wrongPin" style="display:none;color:red"><spring:theme code="pincode.invalid"/></span>
		<span id="unsevisablePin" style="display:none;color:red"><spring:theme code="pincode.unsevisable"/></span>
		<span id="unableprocessPin" style="display:none;color:red"><spring:theme code="pincode.unableprocess"/></span>
		</span>
	   <c:forEach var="entry" items="${deliveryModeMap}">
		<%-- Key: <c:out value="${entry.key}"/> --%>
		<c:if test="${entry.key eq 'home-delivery'}">
		<li><a  id="home" class="HomeDelivery home" style="display: none"> <span><spring:theme code="text.home.delivery"/></span> 
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
			</a>
			</li>
		  </c:if>
		  <c:if test="${entry.key eq 'express-delivery'}">
			<li><a id="express" class="ExpressDelivery express" style="display: none"> <span><spring:theme code="text.express.shipping"/></span> 
			 <c:forEach var="expressEntry" items="${entry.value}">
			 <c:if test="${expressEntry.key eq 'startForExpress'}">
			 <input type="hidden" value="${expressEntry.value}" id="expressStartId"/>
			 </c:if>
			 
			  <c:if test="${expressEntry.key eq 'endForExpress'}">
			  <input type="hidden" value="${expressEntry.value}" id="expressEndId"/>
		     </c:if>
		    </c:forEach>
			<span id="expressDate"><%-- <c:out value="${entry.value}" /> --%></span>
			</a></li>	
		</c:if> 
	</c:forEach>
	
	<!-- <li><a href="#" class="collect"><span>Click and
					Collect</span><span>Buy online, collect in-store</span></a></li> -->
	</ul>

	