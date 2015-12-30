<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>	
<style>
.error{
color: red;
font-size: 12px;
}
</style>
<div class="click-to-chat modal fade" id="clicktoChatModal">
<div class="content" style="width:35%">
<button class="close" data-dismiss="modal"></button>
	<div class="click2chat-container">
	 <h1>
			<spring:theme code="popupc2c.click2chat.title" />
    </h1>
	<form method="post" action="#" id="chatForm">
					<label for="otp" > <spring:theme code="popupc2c.customer.name" />
					</label> <input type="text"
						value="${name}"
						name="customerName" maxlength="40">
					<label for="errorCustomerName" class="error"></label>
		
	
			<label for="email_address"><spring:theme code="popupc2c.customer.email" />
			</label> <input type="text"
				value="${emailId}"
			 name="emailId" maxlength="240">
			 <label for="errorCustomerEmail" class="error"></label>

		
			<label for="mobile_no" ><spring:theme code="popupc2c.customer.mobileNo" /></label> <input
				type="text"
				value="${mobileNo}"
				 name="contactNo" maxlength="10">
	
			<label for="errorCustomerMobileNo" class="error"></label>
		
		
			<label for="reason"><spring:theme code="popupc2c.customer.reason" /></label> 
			<select name="reason">
			<c:if test="${reasons ne null }">
				<c:forEach items="${reasons}" var="reason">
				<option value="${reason.key}">${reason.value}</option>
				</c:forEach>
			</c:if>
			</select>
	
		<span></span>
		<div class="button_fwd_wrapper actions">
			<button id="submitC2C" type="button"><spring:theme code="popupc2c.connect.chat" /></button>
			 <a class="close" href="" data-dismiss="modal"><spring:theme code="text.button.cancel" /></a>
		</div>
		</form>
	</div>
		
	</div>
		<div class="overlay" data-dismiss="modal"></div>	
	</div>
	<script>
		var chatUrl = '${chatUrl}';
	</script>
