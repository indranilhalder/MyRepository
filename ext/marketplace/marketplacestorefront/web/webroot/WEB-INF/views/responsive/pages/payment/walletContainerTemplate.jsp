<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/my-account/profile" var="profileUrl" />




<template:page pageTitle="${pageTitle}">
	
	
	
<script type="text/javascript">

$(document).ready(function() {
	
var modelAttributeValue = '${isWalletActive}';

if(modelAttributeValue !== false){	
		$.ajax({
			url : ACC.config.encodedContextPath + "/wallet/registerCustomerWallet",
			//data : dataString,
			type : "GET",
			cache : false,
			success : function(data) {
				
			if(data === "Success"){
				
				window.location.href = ACC.config.encodedContextPath+"/wallet";
			}

			},	
		   
			fail : function(fail){
			//alert("Sorry we are unable to connect to Click 2 Call service. Please try again later.");
		}	
		
});
}	
});

</script>
	
	
	<html>
<%-- 	<c:choose> --%>
<%-- 	<c:when test="${isWalletActive eq true}"> --%>
	
	<div class="account">
	<h1>Total Cliq Cash: random Number</h1>
	
	
	
	<button>Add gift Card</button> <!-- Make get call in controller to add money  -->
	<button>Cash Back</button>
	<button>Refund</button>
	<button>View Statement</button>
	
	</div>
	
	
 
</html>

</template:page>