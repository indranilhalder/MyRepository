<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>	
<script type="text/javascript" src="/_ui/responsive/common/js/jquery-2.1.1.min.js"></script>
<style>
.error{
color: #ff1c47;
font-size: 12px;
text-align: left;
margin-top:5px;
}
</style>
<div class="click-to-chat modal fade" id="clicktoChatModal">
<div class="content">
<div class="close-btns">
    <a href="#" class="cminimize" data-dismiss="modal"></a>
    <a href="#" class="cclose" data-dismiss="modal"></a>  
  </div>
	<div class="click2chat-container">
    <h2 class="cchat-ico"><spring:theme code="popupc2c.click2chat.title" /></h2>
	<form method="post" action="#" id="chatForm">
					<div class="input-box">
					 <input type="text" value="${name}"	name="customerName" maxlength="40" id="customerNameId">
						<label for="customerNameId" class="clabel" > <spring:theme code="popupc2c.customer.name" />
					</label>
					<label for="errorCustomerName" class="error"></label>
		</div>
	<div class="input-box">
			 <input type="text" value="${emailId}" name="emailId" maxlength="240" id="emailIdChat">
			 <label for="emailIdChat" class="clabel"><spring:theme code="popupc2c.customer.email" />
			</label>
			 <label for="errorCustomerEmail" class="error"></label>
</div>
		<div class="input-box">
			 <input type="text" value="${mobileNo}" name="contactNo" maxlength="10" id="contactNoId">
	<label for="contactNoId" class="clabel"><spring:theme code="popupc2c.customer.mobileNo" /></label>
			<label for="errorCustomerMobileNo" class="error"></label>
			</div>
		
		
			<div class="input-box">
			<select name="reason" id="reasonIdChat">
			<c:if test="${reasons ne null }">
				<c:forEach items="${reasons}" var="reason">
				<option value="${reason.key}">${reason.value}</option>
				</c:forEach>
			</c:if>
			</select>
	<label for="reasonIdChat" class="clabel"><spring:theme code="popupc2c.customer.reason" /></label> 
		<span></span>
		</div>
		<div class="button_fwd_wrapper actions">
			<button id="submitC2C" type="button" class="bsubmit"><spring:theme code="popupc2c.connect.chat" /></button>
			 <a class="close bcancel" href="" data-dismiss="modal" id="chat"><spring:theme code="text.button.cancel" /></a>
		</div>
		</form>
	</div>
		
	</div>
		<div class="overlay" data-dismiss="modal"></div>	
	</div>
	<script>
		var chatUrl = '${chatUrl}';
		$(document).ready(function(){
			window.setTimeout(function() {
				if($('.click-to-chat').length){	
					$(".input-box input").each(function(){
						if( $(this).val() != ""){
							$(this).addClass("used");
						}
						else {
							$(this).removeClass("used");
						}	
					});
					
					$(".input-box select").each(function(){
						if( $(this).val() != ""){
							$(this).addClass("used");
						}
						else {
							$(this).removeClass("used");
						}	
					});
				}
			},500);
			$(document).on("click",".cminimize",function(){	
				$("#up").addClass("minimize");
			});
			$(document).on("click",".cclose",function(){	
				$("#up").removeClass("minimize");
			});
		});
	</script>
