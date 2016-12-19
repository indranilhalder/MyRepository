<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="compressible" tagdir="/WEB-INF/tags/responsive/template/compressible" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<template:javaScriptVariables />

<script>
var loginStatus = '${sessionScope.loginSuccess}';

$(document).on("click", ".header-myAccountSignOut", function() {
	window.localStorage.removeItem("eventFired");
});

//TPR-565
/* $(document).on("click","form .pagination_a_link",function(e){
	e.preventDefault();
	var hrefurl = $(this).attr('href');
	$("#paginationForm").attr("action", hrefurl);
	$(this).closest('form').submit();
 });  
 $(document).on("click","form .pagination_a_link",function(e){
		e.preventDefault();
		var hrefurl = $(this).attr('href');
		$("#paginationFormBottom").attr("action", hrefurl);
		$(this).closest('form').submit();
	 });  */

//TISPRO-183 -- Firing Tealium event only after successful user login
if(loginStatus){
	if (localStorage.getItem("eventFired")==null || window.localStorage.getItem("eventFired")!="true") {
		localStorage.setItem("eventFired","true");
	//	console.log("Login Success!!!");
		if(typeof utag == "undefined"){
			console.log("Utag is undefined")
		}
		else{
			console.log("Firing Tealium Event")
			utag.link({ "event_type" : "Login", "link_name" : "Login" });
		}
		
		//fireTealiumEvent();
		
		
		
	}  
}
</script>

<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/plugins/jquery-ui-1.11.2.custom.min.js"></script> --%>
<%-- bootstrap --%>
<script type="text/javascript"
	src="${commonResourcePath}/bootstrap/dist/js/bootstrap.min.js"></script>

<!-- R2.3: START FL04 -->
<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/address-book') or
		fn:contains(requestScope['javax.servlet.forward.request_uri'],'/new-address') or
		fn:contains(requestScope['javax.servlet.forward.request_uri'],'/edit-address') or
		fn:contains(requestScope['javax.servlet.forward.request_uri'],'/my-account')}">
	<script type="text/javascript" src="${commonResourcePath}/js/addresslandmark.js"></script><!-- R2.3: One line -->
</c:if>
<!-- R2.3: END FL04 -->
<!-- LW-230 Start -->
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('luxury.resource.host')" var="luxuryHost"/>
<c:set var="headerWidgetJsSource" value="${luxuryHost}/header-widget.js"/>
<c:choose>
<c:when test="${(param.isLux ne null and param.isLux eq true) and ((not empty isLuxury and isLuxury != 'false') or (empty isLuxury))}">
<script type="text/javascript" src="${headerWidgetJsSource}"></script>
</c:when>
<c:otherwise>
<c:if test="${not empty isLuxury and isLuxury == 'true'}">
<script type="text/javascript" src="${headerWidgetJsSource}"></script>
</c:if>
</c:otherwise>
</c:choose> 
 <%-- <c:set var="headerWidgetJsSource" value="${luxuryHost}/header-widget.js"/> 
 <c:if test="${(param.isLux ne null and param.isLux eq true) and ((not empty isLuxury and isLuxury != 'false') or (empty isLuxury))}">
	<script type="text/javascript" src="${headerWidgetJsSource}"></script>
</c:if>

<c:if test="${not empty isLuxury and isLuxury == 'true'}">
	<script type="text/javascript" src="${headerWidgetJsSource}"></script>
</c:if>  --%>

<!-- LW-230 End -->
<c:choose>
	<c:when test="${isMinificationEnabled}">
		<compressible:mplminjs/>

	</c:when>
	<c:otherwise>
		<compressible:mpljs/>
	</c:otherwise>
</c:choose>



<%-- <c:if test="${isIAEnabled}">
<script type="text/javascript"
	src="${commonResourcePath}/js/ia_plugin_general.js" defer="defer"></script>
<script type="text/javascript" src="${commonResourcePath}/js/tataia.js" defer="defer"></script>
</c:if>
 --%>


<!--- START: INSERTED for MSD --->
<c:if test="${isMSDEnabled}">
	<c:choose>
	<c:when test="${product.rootCategory=='Clothing'}">
	<c:set var="MSDRESTURL" scope="request" value="${msdRESTURL}"/>
	<c:forEach items="${product.categories}" var="categoryForMSD">
			<c:if test="${fn:startsWith(categoryForMSD.code, 'MSH')}">
			<input type="hidden" value="${categoryForMSD.code}" name="salesHierarchyCategoryMSD" />   
		</c:if>
	</c:forEach>
	<input type="hidden" name="productCodeMSD" class="cartMSD"	value="${product.code}" />
	<script type="text/javascript">
	$(window).on('load',function(){
		$.getScript('${msdjsURL}').done(function(){
			$.getScript('${commonResourcePath}/js/minified/acc.moreMADness.min.js?v=${buildNumber}').done(function(){
				var productCodeMSD =  $("input[name=productCodeMSD]").val();
				  var salesHierarchyCategoryMSD =  $("input[name=salesHierarchyCategoryMSD]").val();				  
				  var msdRESTURL = '<c:out value="${MSDRESTURL}"/>';				  
		          callMSD(productCodeMSD,salesHierarchyCategoryMSD,msdRESTURL); 
		          jQuery($("#view-similar-items")[0]).resize();
			});
		});
	});
	</script>
	</c:when>
	</c:choose>
</c:if>


<style type="text/css">
div.blockMsg {
    text-align: center;
    background-color: transparent !important;
    border:0px solid transparent !important;
    padding: 15px;
    color: #fff;
    margin-left:20px;
}
/* div.blockUI.blockOverlay{
	opacity : .09 !important;
	top : 100px !important;
} */

</style> 

<!--- END:MSD --->
