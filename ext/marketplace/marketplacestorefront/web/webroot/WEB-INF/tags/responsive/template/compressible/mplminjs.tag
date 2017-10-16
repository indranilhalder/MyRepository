<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>

<c:if test="${empty buildNumber}">
	<c:set var="buildNumber" value="100000" />
</c:if>

<!-- Luxury Specific JS Include Starts-->

<c:if test="${fn:contains(themeResourcePath,'theme-luxury')}">
	
	<script type="text/javascript" src="${themeResourcePath}/combined/luxury-main.js"></script>
	<script type="text/javascript" src="${themeResourcePath}/combined/luxury-tmpmain.min.js"></script>
	
	<script type="text/javascript" src="${commonResourcePath}/js/minified/plugins.min.js?v=${buildNumber}"></script>
	<c:if test="${isIAEnabled}">
		<script type="text/javascript" src="${commonResourcePath}/js/minified/ia.min.js?v=${buildNumber}"></script>
	</c:if>
	
</c:if>
<!-- Luxury Specific JS Include Ends-->

<!-- UF-439 -->
<c:if test="${!fn:contains(themeResourcePath,'theme-luxury')}">
	<c:choose>
		<c:when test="${fn:contains(pageBodyCssClasses, 'homepage') 
			or fn:contains(pageBodyCssClasses, 'newBrandLandingPageTemplate')
	        or fn:contains(pageBodyCssClasses, 'productDetails') 
	        or  fn:contains(pageBodyCssClasses, 'productGridPage') 
	        or fn:contains(pageBodyCssClasses, 'searchGridPage')
	        or fn:contains(pageBodyCssClasses, 'BrandLayoutPage')
	        or fn:contains(pageBodyCssClasses, 'apparelCategoryLandingPageV1')
	        or fn:contains(pageBodyCssClasses, 'FootwearBrandLandingPageTemplate')
	        or fn:contains(pageBodyCssClasses, 'FootwearCategoryLandingPageTemplate')
	        or fn:contains(pageBodyCssClasses, 'electronicsCategoryLandingPage')}">
			<template:onLoadJS/>
		</c:when>
		<c:otherwise>
			<script type="text/javascript"
				src="${commonResourcePath}/js/minified/plugins.min.js?v=${buildNumber}"></script>
			<script type="text/javascript"
				src="${commonResourcePath}/js/minified/tmpmain.min.js?v=${buildNumber}"></script>
			<c:if test="${isIAEnabled}">
				<script type="text/javascript"
					src="${commonResourcePath}/js/minified/ia.min.js?v=${buildNumber}"></script>
			</c:if>
		</c:otherwise>
	</c:choose>
</c:if>
<!--[if lt IE 9]>
<script type="text/javascript" src="${commonResourcePath}/js/minified/ie9.min.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script type="text/javascript" src="${commonResourcePath}/js/minified/ie10.min.js"></script>
<![endif]-->

<c:forEach items="${addOnJavaScriptPaths}" var="addOnJavaScript">
	<script type="text/javascript"
		src="${addOnJavaScript}?v=${buildNumber}"></script>
</c:forEach>
<c:if test="${fn:contains(pageBodyCssClasses, 'homepage')}">
<script>

/*--- Start of  Mobile view Left menu Sign In toggle---- */
$(window).on("load resize",function(e){
	//alert('here1');
	if($('#pageTemplateId').val() =='LandingPage2Template'){	
		//alert('here2');
		var style=null;
		if($(window).width() < 773) {
			  $(document).off("click","span#mobile-menu-toggle").on("click","span#mobile-menu-toggle",function() {
				$("a#tracklink").mouseover();
				$(this).parent('li').siblings().children('#mobile-menu-toggle').removeClass("menu-dropdown-arrow");
				$(this).parent('li').siblings().find('#mobile-menu-toggle + ul').slideUp();
				$(this).next().slideToggle();
				$(this).toggleClass("menu-dropdown-arrow");
			});
			  $(document).on("click","ul.words span#mobile-menu-toggle",function() {
				var id = $(this).parents('ul.words').siblings("div.departmenthover").attr("id"), ind = $(this).parent('li.short.words').index("."+id+" .short.words")
					$(".long.words").hide();
					div_container=$(this).parent(".short.words");
					if($(this).hasClass('menu-dropdown-arrow')){
						for(var i=1;i<$(".long.words").length;i++){
							if(div_container.next().hasClass("long")){
								div_container.next().show();
								div_container=div_container.next();
							}
							else
								break;
							}		
					} else {
						$(".long.words").hide();
					}
					longStyleContainer=$(this).parent(".short.words").next(".long.words").attr("style");
					$(this).parents("li.level1").nextAll("li.level1").find(".short").children("#mobile-menu-toggle").removeClass("menu-dropdown-arrow");
					$(this).parents("li.level1").prevAll("li.level1").find(".short").children("#mobile-menu-toggle").removeClass("menu-dropdown-arrow");
			  });
			/*--- Mobile view shop by brand and department ---*/ 
		}
		else {
			$("#mobile-menu-toggle").next().attr("style",style);
			$("li.short.words,li.long.words").next().attr("style",style); 
		}
		
		div_container=$(".productGrid-menu nav #mobile-menu-toggle.mainli.menu-dropdown-arrow").parent(".short.words");
		for(var i=1;i<$(".long.words").length;i++){
			if(div_container.next().hasClass("long")){
				div_container.next().attr("style",longStyleContainer);
				div_container=div_container.next();
			}
			else
				break;
			}
	}
	});
</script>
</c:if>
