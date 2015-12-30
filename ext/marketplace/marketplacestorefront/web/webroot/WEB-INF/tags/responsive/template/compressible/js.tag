<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="compressible" tagdir="/WEB-INF/tags/responsive/template/compressible" %>

<template:javaScriptVariables />

<script type="text/javascript"
	src="${commonResourcePath}/js/jquery-ui-1.11.2.custom.min.js"></script>
<%-- bootstrap --%>
<script type="text/javascript"
	src="${commonResourcePath}/bootstrap/dist/js/bootstrap.min.js"></script>


<c:choose>
	<c:when test="${isMinificationEnabled}">
		<compressible:mplminjs/>
		<%-- <compressible:mpljs/> --%>
	</c:when>
	<c:otherwise>
		<compressible:mpljs/>
	</c:otherwise>
</c:choose>
<script type="text/javascript"
	src="${commonResourcePath}/js/feedback.js"></script>

<!--[if lt IE 9]>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/main-ie8.css"/>
<script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/html5shiv.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/respond.js"></script>
<script src="//code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.backstretch.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/background-size-shim.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.html5-placeholder-shim.js"></script>
<![endif]-->

<!-- Search feed back End -->

<c:if test="${isIAEnabled}">
<script type="text/javascript"
	src="${commonResourcePath}/js/ia_plugin_general.js" defer="defer"></script>
<script type="text/javascript" src="${commonResourcePath}/js/tataia.js" defer="defer"></script>
</c:if>



<!--- START: INSERTED for MSD --->
<c:if test="${isMSDEnabled}">
	<c:choose>
	<c:when test="${product.rootCategory=='Clothing'}">
		<script type="text/javascript"	src="${msdjsURL}" defer="defer"></script>
		<script type="text/javascript"	src="${commonResourcePath}/js/moreMADness.js" defer="defer"></script>
		
		 <c:set var="MSDRESTURL" scope="request" value="${msdRESTURL}"/>
		  
		<c:forEach items="${product.categories}" var="categoryForMSD">
			<c:if test="${fn:startsWith(categoryForMSD.code, 'MSH')}">
			<input type="hidden" value="${categoryForMSD.code}" name="salesHierarchyCategoryMSD" />   
		</c:if>
		</c:forEach>
		<input type="hidden" name="productCodeMSD" class="cartMSD"	value="${product.code}" />	   
		<script type="text/javascript" defer="defer">
				  var productCodeMSD =  $("input[name=productCodeMSD]").val();
				  var salesHierarchyCategoryMSD =  $("input[name=salesHierarchyCategoryMSD]").val();				  
				  var msdRESTURL = '<c:out value="${MSDRESTURL}"/>';				  
		          callMSD(productCodeMSD,salesHierarchyCategoryMSD,msdRESTURL);        
		          $(window).on('load', function(){
		        	  jQuery($("#view-similar-items")[0]).resize();}
		          );  
		</script>  
	</c:when>
	</c:choose>
</c:if>
<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/address-book') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/populateAddressDetail') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/addNewAddress') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/editAddress') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/set-default-address/*') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/remove-address/*') or

fn:contains(requestScope['javax.servlet.forward.request_uri'],'/orders') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/order/*') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/returnRequest') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/returnSuccess') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/cancelSuccess')}">
	<script type="text/javascript"
		src="${commonResourcePath}/js/acc.accountpagination.js"></script>
</c:if>

<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/login') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/register') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/login/*')}">
<script type="text/javascript"
		src="${commonResourcePath}/js/acc.accountaddress.js"></script>
<script type="text/javascript"
		src="${commonResourcePath}/js/acc.forgottenpassword.js"></script>
</c:if>

<script>
function globalErrorPopup(msg) {
	$("body").append('<div class="modal fade" id="globalErrorPopupMsg"><div class="content" style="padding: 10px;"><span style="display: block; margin: 7px 16px;line-height: 18px;">'+msg+'</span><button class="close" data-dismiss="modal"></button></div><div class="overlay" data-dismiss="modal"></div></div>');
	$("#globalErrorPopupMsg").modal('show');
} 
$(document).on('hide.bs.modal', function () {
    $("#globalErrorPopupMsg").remove();
}); 
</script>

<style type="text/css">
div.blockMsg {
    text-align: center;
    background-color: transparent !important;
    border:0px solid transparent !important;
    padding: 15px;
    color: #fff;
    margin-left:20px;
}
div.blockUI.blockOverlay{
	opacity : .09 !important;
	top : 100px !important;
}
</style> 

<!--- END:MSD --->
