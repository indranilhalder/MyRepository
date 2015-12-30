<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>



<button type="button" class="close pull-right" data-dismiss="modal" aria-hidden="true"></button>
<div class="sizes">
	
	<h3><spring:theme code="product.variants.size.guide"/></h3>
	<c:choose>
	<c:when test="${not empty sizeguideData}">	
		
		<div class="tables">
			<div>
				<%-- <h2>Top</h2> --%>
					<ul>
						<li class="header">
							<ul>
								<li><spring:theme code="product.variants.size"/></li>
								<c:forEach items="${sizeguideData}" var="sizeGuide" varStatus="sizeGuideIndex" end="0">
									<c:forEach items="${sizeGuide.value}" var="sizeGuideValue" varStatus="sizeIndex" >
										<c:if test="${sizeIndex.index eq 0}">
											<c:set var="imageURL" value="${sizeGuideValue.imageURL}"></c:set>
										</c:if>
										
									
									</c:forEach>
								</c:forEach>
								<c:forEach items="${sizeguideHeader}" var="sizeGuide" >
										<li>${sizeGuide}</li>
									</c:forEach>	
								
							</ul>
						</li>
						<c:forEach items="${sizeguideData}" var="sizeGuide" >
							<c:set var="count" value="${4 - fn:length(sizeGuide.value) }"></c:set>	
													
							<li class="item">
								<ul>
									<li>${sizeGuide.key}</li>
									<c:forEach items="${sizeGuide.value}" var="sizeGuideValue">
										<li>${sizeGuideValue.dimensionValue}
											<c:choose>
											<c:when test="${fn:containsIgnoreCase(sizeGuideValue.dimensionUnit , 'inch')}">"</c:when> 
											<c:otherwise>${sizeGuideValue.dimensionUnit}</c:otherwise>
											</c:choose>
										</li>
									</c:forEach>
									<c:if test="${count gt 1 }">
									<c:forEach begin="0" end="${count-1}">
										<li>&nbsp;</li>
									</c:forEach>
									</c:if>
								</ul>
							</li>	
							
						</c:forEach>
						
						
					</ul>
			</div>
				
		</div>
		<div class="img">
			<img src="${imageURL}" alt="sizeGuideImage" />
		</div>
	</c:when>
	<c:otherwise>
		<p><spring:theme code="product.variants.size.guide.notavail"/></p>
	</c:otherwise>
	</c:choose>	
</div>
<script>
$(document).ready(function(){

	var numLi= $(".modal.size-guide .sizes .tables li.header > ul").children().length;
	
	var sizeWidth= 88/(numLi-1) + "%";

	$(".modal.size-guide .sizes .tables li > ul > li").css("width",sizeWidth);
	$(".modal.size-guide .sizes .tables li > ul > li:first-child").css("width","12%");

});

</script> 	
			