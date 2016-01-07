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

<!--[if lt IE 9]>
<script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<script src="//code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
<![endif]-->

<c:choose>
	<c:when test="${isMinificationEnabled}">
		<compressible:mplminjs/>
	</c:when>
	<c:otherwise>
		<compressible:mpljs/>
	</c:otherwise>
</c:choose>


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
