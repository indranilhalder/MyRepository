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
								var current = $('.badge').text();
								if (current > 0) {
									current = current - 1;
									markAsRead(this.id, $(this).attr(
											'data-name'), $(this).attr(
											'data-status'));

									$('.badge').text(current)
								}
							});
					$(this).mouseout(function() {
						$(this).css("background-Color", "#FFF");
					});
				});
	});

	function markAsRead(orderNo, creationDate, shopperstatus) {

		var consignmentNo = creationDate;
		var orderNo = orderNo;
		var status = shopperstatus
		var contentData = '';
		$.ajax({
			url : ACC.config.encodedContextPath
					+ "/view/TrackOrderHeaderComponentController/markAsRead",
			data : {
				'consignmentNo' : consignmentNo,
				'orderId' : orderNo,
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


	<c:if test="${empty notificationMessagelist}">
		<li class="trackOrder_message"><spring:theme
				code="default.notification.message.trackOrder" /></li>
	</c:if>

	<c:forEach items="${notificationMessagelist}" var="notifylist">
		<c:choose>

			<c:when test="${empty notifylist.transactionID}">

				<c:set var="orderId" value="${notifylist.orderNumber}" />
				<c:set var="cstatus"
					value="${notifylist.notificationCustomerStatus}" />


				<li id="${notifylist.orderNumber}"
					data-order="${notifylist.transactionID}"
					data-status="${notifylist.notificationCustomerStatus}"><a
					href="/store/mpl/en/my-account/order/?orderCode=${notifylist.orderNumber}">${fn:replace(cstatus, "@", orderId)}
				</a>
					<div id="track_footer" style="float: right;">
						<h4>
							<c:set var="now" value="${notifylist.notificationCreationDate}" />
							<fmt:formatDate type="date" value="${now}" />
						</h4>

					</div></li>


			</c:when>
			<c:otherwise>
				<c:set var="orderId" value="${notifylist.orderNumber}" />
				<c:set var="cstatus"
					value="${notifylist.notificationCustomerStatus}" />

				<li id="${notifylist.orderNumber}"
					data-order="${notifylist.transactionID}"
					data-status="${notifylist.notificationCustomerStatus}"><a
					href="/store/mpl/en/my-account/order/?orderCode=${notifylist.orderNumber}">${fn:replace(cstatus, "@", orderId)}</a>
					<div id="track_footer" style="float: right;">
						<h4>
							<c:set var="now" value="${notifylist.notificationCreationDate}" />
							<fmt:formatDate type="date" value="${now}" />
						</h4>

					</div></li>

			</c:otherwise>
		</c:choose>

	</c:forEach>
</c:if>
