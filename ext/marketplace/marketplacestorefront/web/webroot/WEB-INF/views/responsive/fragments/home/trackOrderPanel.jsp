<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<c:if test="${empty notificationMessagelist}">
	
		<li><spring:theme
				code="default.notification.message.trackOrder" /></li>
				
</c:if>
<c:if test="${isSignedInUser eq 'yes' }">
	
<script type="text/javascript">
	//  function countread(count)
	//  {		
	// 	var span = $('<span id="notificationCount"/>').attr({'class':'badge' }).html(count);
	// 	 $('#tracklink').prepend(span);
	//  }

	//  function updateCount(count)
	//  {
	// 	 $('#tracklink span').text(count);	 
	//  }

	$(function() {
		$(".trackorder-dropdown").children("li").each(
				function() {
					$(this).click(
							function() {
								
								$(this).css("background-Color", "#c0c0c0");
								//var current = $('.badge').text();
								var current = '${notificationCount}';
								
								if (current > 0) {
									current = current - 1;
									markAsRead(this.id,$(this).attr('data-name'), $(this).attr('data-status'));
									$('#tracklink span').text(current)
								}
							});
					$(this).mouseout(function() {
						$(this).css("background-Color", "#FFF");
					});
				});
	});

	function markAsRead(currentId, consignmentNo, shopperstatus) {
     
		var consignmentNo = consignmentNo;
		var currentId = currentId;
		var shopperstatus = shopperstatus
		var contentData = '';
		$.ajax({
			url : ACC.config.encodedContextPath
					+ "/headerTrackOrder/markAsRead",
			data : {
				'currentId' : currentId,
				'consignmentNo' : consignmentNo,
				'shopperStatus' : shopperstatus
			},
			type : "GET",
			cache : false,
			success : function(data) {
			

			},
			error : function(resp) {
				alert("Sorry We Could Not Connect to Database");
			}
		});

	}
</script>

	

	<c:forEach items="${notificationMessagelist}" var="notifylist">
		<c:choose>
		

			<c:when test="${empty notifylist.transactionID and notifylist.orderNumber ne null}">

				<c:set var="orderId" value="${notifylist.orderNumber}" />
				<c:set var="cstatus"
					value="${notifylist.notificationCustomerStatus}" />

				<li id="${notifylist.orderNumber}"
				  data-name="${notifylist.transactionID }"
					data-order="${notifylist.transactionID}"
					 data-status="${notifylist.notificationCustomerStatus}"> 
					<a href="/store/mpl/en/my-account/order/?orderCode=${notifylist.orderNumber}">${fn:replace(cstatus, "@", orderId)}
				</a>
					<div id="track_footer" style="float: right;">
						<h4>
							<c:set var="notify" value="${notifylist.notificationCreationDate}" />
							
									<jsp:useBean id="now1" class="java.util.Date" scope="request"/>
						<fmt:parseNumber
			    			value="${ now1.time / (1000*60*60*24) }"
			    			integerOnly="true" var="nowDays" scope="request"/>
			
						<fmt:parseNumber
			    			value="${ notify.time / (1000*60*60*24) }"
			    			integerOnly="true" var="otherDays" scope="page"/>

						<c:set value="${nowDays - otherDays}" var="dateDiff"/>
   			
   						<c:choose>
			    			<c:when test="${dateDiff eq 0}">TODAY <fmt:formatDate type="both" pattern="HH:mm" value="${notify}" /></c:when>
			    			<%-- <c:when test="${dateDiff eq 1}">yesterday</c:when> --%>
			    			<c:otherwise>
			    				<fmt:formatDate type="both" pattern="dd/MM/yyyy HH:mm" value="${notify}" />
			    			</c:otherwise>	
			 			</c:choose>
						</h4>

					</div></li>
	
			</c:when>
		
			<c:otherwise>
			<c:if test="${ notifylist.couponCode eq null}">
		
				<c:set var="orderId" value="${notifylist.orderNumber}" />
				<c:set var="cstatus"
					value="${notifylist.notificationCustomerStatus}" />

				<li id="${notifylist.orderNumber}"
				 data-name="${notifylist.transactionID }"
					data-order="${notifylist.transactionID}"
					data-status="${notifylist.notificationCustomerStatus}"><a
					href="/store/mpl/en/my-account/order/?orderCode=${notifylist.orderNumber}">${fn:replace(cstatus, "@", orderId)}</a>
					<div id="track_footer" style="float: right;">
						<h4>
						
							<c:set var="notify" value="${notifylist.notificationCreationDate}" />
									<jsp:useBean id="now2" class="java.util.Date" scope="request"/>
						<fmt:parseNumber
			    			value="${ now2.time / (1000*60*60*24) }"
			    			integerOnly="true" var="nowDays" scope="request"/>
			
						<fmt:parseNumber
			    			value="${ notify.time / (1000*60*60*24) }"
			    			integerOnly="true" var="otherDays" scope="page"/>

						<c:set value="${nowDays - otherDays}" var="dateDiff"/>
   			
   						<c:choose>
			    			<c:when test="${dateDiff eq 0}">TODAY <fmt:formatDate type="both" pattern="HH:mm" value="${notify}" /></c:when>
			    			<%-- <c:when test="${dateDiff eq 1}">yesterday</c:when> --%>
			    			<c:otherwise>
			    				<fmt:formatDate type="both" pattern="dd/MM/yyyy HH:mm" value="${notify}" />
			    			</c:otherwise>	
			 			</c:choose>
					
   						
						</h4>

					</div></li>
              </c:if>
              
             <c:if test= "${ notifylist.couponCode  ne null}">
		
					<c:set var="coupon" value="${notifylist.couponCode}" />
					  <c:url var="productUrl" value="${notifylist.productUrl}"></c:url>
					 
					
				<c:set var="couponStatus"
					value="${notifylist.notificationCustomerStatus}" />
		       
		         
				<li id="${notifylist.couponCode}"
					 data-status="${couponStatus}"> 
					<a href="${productUrl}">${fn:replace(couponStatus, "@", coupon)}
				</a>
				<div id="track_footer" style="float: right;">
						<h4>
							<c:set var="notify" value="${notifylist.notificationCreationDate}" />
						
							<jsp:useBean id="now" class="java.util.Date" scope="request"/>
							
								
						<fmt:parseNumber
			    			value="${ now.time / (1000*60*60*24) }"
			    			integerOnly="true" var="nowDays" scope="request"/>
			
						<fmt:parseNumber
			    			value="${ notify.time / (1000*60*60*24) }"
			    			integerOnly="true" var="otherDays" scope="page"/>


						<c:set value="${nowDays - otherDays}" var="dateDiff"/>
						
					
   			
   						<c:choose>
			    			<c:when test="${dateDiff eq 0}">TODAY <fmt:formatDate type="both" pattern="HH:mm" value="${notify}" /></c:when>
			    			<%-- <c:when test="${dateDiff eq 1}">yesterday</c:when> --%>
			    			<c:otherwise>
			    				<fmt:formatDate type="both" pattern="dd/MM/yyyy HH:mm" value="${notify}" />
			    			</c:otherwise>	
			 			</c:choose>
				
						</h4>

					</div>
					</li>
					
			</c:if>
		
			</c:otherwise>
		</c:choose>
	</c:forEach>
</c:if>
