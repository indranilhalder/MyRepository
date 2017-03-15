<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="facetData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.FacetData" %>
<%@ attribute name="pageFacetData" required="true" type="String" %>
<%@ attribute name="removeQueryUrlForPriceValue" required="true" type="String" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script>

/* $('form').submit(function(){

    var action = this.action;
 //   alert("hello"+action);
    $('form').attr("action",action.replaceAll("page-/[^0-9]/g" ));
	$('form').submit();  
   

    }); */
//}); 
function navigateToPage(queryString,textString)
{
	
	var urlString=ACC.config.encodedContextPath+"/search/helpmeshop?q="+encodeURIComponent(queryString)+"&text="+textString;
	//alert(urlString);
	window.open(urlString,"_self");
}
</script>
<div class="facet_mobile">
<c:if test="${not empty facetData.values}">
<c:if test="${facetData.code ne 'category'}">
<c:if test="${facetData.code ne 'snsCategory' && facetData.code ne 'micrositeSnsCategory'}">
<c:if test="${facetData.code ne 'deptType'}">
<c:if test="${facetData.code ne 'sellerId'}">
<c:if test="${facetData.code ne 'allMobilePromotions'}">
<c:if test="${facetData.code ne 'allCategories'}">
<c:if test="${facetData.code ne 'isOffersExisting'}">
<c:if test="${facetData.code ne 'promotedProduct'}">
<!--  fixed for TISSTRT-615-Fixed -->
<c:if test="${facetData.code ne 'vouchers'}">
<!-- End  fixed for TISSTRT-615-Fixed -->
<!-- Change for CAR-245 -->
<c:if test="${facetData.code ne 'categoryNameCodeMapping'}">
<!-- End Change for CAR-245 -->

<c:if test="${not empty facetData.values && facetData.code == 'inStockFlag'}">

	<c:set var="facetValuesForStock" value="${facetData.values}" />
	<c:set var="facetStockSize" value="${fn:length(facetValuesForStock)}" />
</c:if>
<input type="hidden" id="facetStockSize" value="${facetStockSize}"/>
<!-- //display url -->
<c:set var="url" value="${requestScope['javax.servlet.forward.request_uri']}"/>
<c:if test="${fn:contains(url, 'page')}">
 <c:set var="subStringIndex" value="${fn:indexOf(url,'page-')}"/>
<c:set var="url" value="${fn:substring(url, 0, subStringIndex)}" />
</c:if>

<ycommerce:testId code="facetNav_title_${facetData.name}">
<c:if test="${facetData.values.size()>0}">
	<li class="facet js-facet ${facetData.name}">
		<div class="facet-name js-facet-name">
		
		<c:choose>
	
			<c:when test="${facetData.code == 'inStockFlag'}">
			<c:if test="${facetStockSize=='2'}">
				<h3 class="${facetData.genericFilter}"><span class="filter-nav">${facetData.name}</span><span class="category-icons"><span></span></span></h3>
			</c:if>		 
			</c:when> 
			<%-- <c:when test="${facetData.code == 'price'}">
				<h4 class="true">${facetData.name}</h4>
			</c:when> --%>
			<c:otherwise>
				<h3 class="true"><span class="filter-nav">${facetData.name}</span><span class="category-icons"><span></span></span></h3>
			</c:otherwise>
	   </c:choose>
		
                                	
		</div>
		<div class="facet-values js-facet-values js-facet-form ">
		<p class="filter-name">${facetData.name}</p>
			<c:if test="${not empty facetData.topValues}">
				<ul class="facet-list js-facet-top-values active">
					<c:forEach items="${facetData.topValues}" var="facetValue">
						<li class="filter-${facetData.code}">
						
						<c:url value="${facetValue.query.url}" var="facetValueQueryUrl"/>
						<c:choose>
						
						<c:when test="${facetData.code eq 'colour' && not empty facetValue.name}">
							<c:set var="colorAry" value="${fn:split(facetValue.code, '_')}" />
							<c:choose>
								<c:when test="${colorAry[0]=='Multi' || colorAry[0]=='multi'}">
								<form action="${url}" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>
								<input type="hidden" name="isFacet" value="true"/>
								<input type="hidden" name="facetValue" value="${facetValue.code}"/>
								<input type="submit" value="" style="background:url('${commonResourcePath}/images/multi.jpg');border:1px solid rgb(204, 211, 217);height:36px;padding: 13px 17px; width:36px;background-size:100%;">
								<span><span>All Color</span></span>
								</form>
								<%-- <a   onclick="navigateToPage('${facetValue.query.query.value}','${searchPageData.freeTextSearch}')" >
							<!-- 	<a href="onclick="navigateToPage('${facetValue.query.query.value}','${searchPageData.freeTextSearch}')"> -->
						     		<a href="${facetValueQueryUrl}&amp;text=${searchPageData.freeTextSearch}&amp;searchCategory=${searchCategory}">
						     			<img src="${commonResourcePath}/images/multi.jpg" height="36" width="36" title="Multicolor" />
						     		</a>--%>
								</c:when> 
								<c:otherwise>
								<c:set var="colorHexCode" value="#${colorAry[1]}" />
								<form action="${url}" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>							
								<input type="hidden" name="isFacet" value="true"/>
								<input type="hidden" name="facetValue" value="${facetValue.code}"/>
								<input type="button" class="js-facet-colourbutton" style="background-color:${colorHexCode}; border:1px solid rgb(204, 211, 217); height: 36px;    padding: 13px 17px;"  />
								<%-- <input type="submit" value="" style="background-color:${colorHexCode}; border:1px solid rgb(204, 211, 217); height: 36px;    padding: 13px 17px;"  /> --%>
								<span><span>${facetValue.name}</span></span>
								</form>
									<%-- 
									<a  title="${facetValue.name}" onclick="navigateToPage('${facetValue.query.query.value}','${searchPageData.freeTextSearch}')" style="background-color:${colorHexCode}; border: 1px solid rgb(204, 211, 217)"></a>
									 <a href="#" title="${facetValue.name}" style="background-color:${colorHexCode}; border: 1px solid rgb(204, 211, 217)"></a> 
									<a href="${facetValueQueryUrl}&amp;text=${searchPageData.freeTextSearch}&amp;searchCategory=${searchCategory}" title="${facetValue.name}" style="background-color:${colorHexCode}; border: 1px solid rgb(204, 211, 217)"></a> --%>
								</c:otherwise>
							</c:choose>
						</c:when>
						
						<c:otherwise>
							<c:if test="${facetData.multiSelect}">
								<form action="${url}" method="get"> 
									<input type="hidden" name="offer" value="${offer}"/>
									<input type="hidden" name="searchCategory" value="${searchCategory}"/>
									<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
									<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
									<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>
									<input type="hidden" name="isFacet" value="true"/>
									<input type="hidden" name="facetValue" value="${facetValue.code}"/>
									<label>
										<input type="checkbox" ${facetValue.selected ? 'checked="checked"' : ''}  class="facet-checkbox js-facet-checkbox sr-only" />
										<span class="facet-label">
											<span class="facet-mark"></span>
											<div class="facet-text">
											<span class="facet-text">
												${facetValue.name}												
												</span>
												<ycommerce:testId code="facetNav_count">
													<span class="facet-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
												</ycommerce:testId>																							
											</div>
										</span>
									</label>
								</form>
							</c:if>
							<c:if test="${not facetData.multiSelect}">
								<c:url value="${facetValue.query.url}" var="facetValueQueryUrl"/>
								<span class="facet-text">
							   <form action="${url}" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>
								<input type="hidden" name="isFacet" value="true"/>
								<input type="hidden" name="facetValue" value="${facetValue.code}"/>
								<input type="submit" value="${facetValue.name}"  />
								</form>
								
								<%-- <a href="#">${facetValue.name}</a> --%>
									<%-- <a href="${facetValueQueryUrl}&amp;text=${searchPageData.freeTextSearch}">${facetValue.name}</a> --%>&nbsp;
									<%-- <ycommerce:testId code="facetNav_count">
										<span class="facet-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
									</ycommerce:testId> --%>
								</span>
							</c:if>
							</c:otherwise>
						</c:choose>
						</li>
					</c:forEach>
				</ul>
			</c:if>
			<ul class="facet-list js-facet-list  <c:if test="${not empty facetData.topValues}">facet-list-hidden js-facet-list-hidden</c:if>">

				<c:forEach items="${facetData.values}" var="facetValue">					
				  <c:url value="${facetValue.query.url}" var="facetValueQueryUrl"/>
					<li class="filter-${facetData.code}">

					<c:choose>
						<c:when test="${facetData.code eq 'colour' && not empty facetValue.name}">						
							<c:set var="colorAry" value="${fn:split(facetValue.code, '_')}" />
							<c:choose>
								<c:when test="${colorAry[0]=='Multi' || colorAry[0]=='multi'}">
							<form action="${url}" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>
								<input type="hidden" name="isFacet" value="true"/>
								<input type="hidden" name="facetValue" value="${facetValue.code}"/>
								<input type="submit" value="" style="background:url('${commonResourcePath}/images/multi.jpg'); border:1px solid rgb(204, 211, 217);height:36px;padding: 13px 17px; width:36px;background-size:100%;">
								<span><span>All Color</span></span>
								</form>
								<%-- <a href="#">
								<a href="/search/helpmeshop&amp;text=${searchPageData.freeTextSearch}&amp;q=${facetValue.query.query.value}">
						     		<a href="${facetValueQueryUrl}&amp;text=${searchPageData.freeTextSearch}&amp;searchCategory=${searchCategory}">
						     			<img src="${commonResourcePath}/images/multi.jpg" height="36" width="36" title="Multicolor" />
						     		</a> --%>
								</c:when>
								<c:otherwise>
								<c:set var="colorHexCode" value="#${colorAry[1]}" />
								<!-- <a href="#" ></a> --> 
							    <form action="${url}" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="hidden" name="isFacet" value="true"/>
								<input type="hidden" name="facetValue" value="${facetValue.code}"/>
								<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>
								<!-- <input type="submit" value="" style="background-color:${colorHexCode}; border:1px solid rgb(204, 211, 217); height: 36px;    padding: 13px 17px;"  />-->
								<input type="button" class="js-facet-colourbutton" style="background-color:${colorHexCode}; border:1px solid rgb(204, 211, 217); height: 36px;    padding: 13px 17px;">
								<span><span>${facetValue.name}</span></span>
									<%-- <c:if test="${facetData.code == 'inStockFlag'}">
									<c:if test="${facetValue.code == 'true' && facetStockSize=='2'}">
										<span class="facet-label">
										<span class="facet-mark"></span>
										<span class="facet-text">
											<spring:theme code="text.exclude.outOfStock"/>&nbsp;
											<ycommerce:testId code="facetNav_count">
												<span class="facet-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
											</ycommerce:testId>
										</span>
									</span>
									</c:if>
									</c:if> --%>
									<%-- <c:if test="${facetData.code ne 'inStockFlag'}">
										<span class="facet-label">
										<c:if test="${not empty facetValue.name}">
											<span class="facet-mark"></span>
										</c:if>	
											<span class="facet-text">
												${facetValue.name}&nbsp;
												<ycommerce:testId code="facetNav_count">
													<span class="facet-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
												</ycommerce:testId>
											</span>
										</span>
									</c:if> --%>
					
							</form>
									 
									 
									<%-- <a title="${facetValue.name}" onclick="navigateToPage('${facetValue.query.query.value}','${searchPageData.freeTextSearch}')" style="background-color:${colorHexCode}; border: 1px solid rgb(204, 211, 217)"></a> --%>
									<%-- <a href="/search/helpmeshop?q=${facetValue.query.query.value}&amp;text=${searchPageData.freeTextSearch}" title="${facetValue.name}" style="background-color:${colorHexCode}; border: 1px solid rgb(204, 211, 217)"></a> --%>
									<%-- <a href="${facetValueQueryUrl}&amp;text=${searchPageData.freeTextSearch}&amp;searchCategory=${searchCategory}" title="${facetValue.name}" style="background-color:${colorHexCode}; border: 1px solid rgb(204, 211, 217)"></a> --%>
								</c:otherwise>
							</c:choose>
						</c:when>
						<%-- <c:when test="${facetData.name eq 'size'}"> --%>	
						<c:when test="${facetData.code eq 'size' && not empty facetValue.name}">					
							  <form action="${url}" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>
								<input type="hidden" name="isFacet" value="true"/>
								<input type="hidden" name="facetValue" value="${facetValue.code}"/>
								<input type="button" class="js-facet-sizebutton" value="${facetValue.name}"/>
								</form>
						<%-- <a href="#">${facetValue.name}</a> --%>
							<%-- <a href="${facetValueQueryUrl}&amp;text=${searchPageData.freeTextSearch}">${facetValue.name}</a> --%>
						</c:when>						
						
						<c:otherwise>
							
						
					
						<c:if test="${facetData.multiSelect}">											
							<ycommerce:testId code="facetNav_selectForm"> 
							<!-- Added for TISPRO-490 Start here -->							
							<c:if test="${facetData.code eq 'dialColour'}">
							<form action="#" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="hidden" name="isFacet" value="true"/>								
								<input type="submit" title="${facetValue.name}" value="" style="background-color:${facetValue.name}; border:1px solid rgb(204, 211, 217); height: 36px;    padding: 13px 17px;" />														
								<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>
								<input type="hidden" name="facetValue" value="${facetValue.code}"/>
							</form>							
							</c:if>
							<!-- Added for TISPRO-490 End here -->
							<c:choose>
								<c:when test="${facetData.code eq 'price'}">
								<form action="${url}" method="get"> 
									<input type="hidden" name="offer" value="${offer}"/>
									<input type="hidden" name="searchCategory" value="${searchCategory}"/>
									<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
									<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
									<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>
									<input type="hidden" name="isFacet" value="true"/>
									<input type="hidden" name="facetValue" value="${facetValue.code}"/>
									<label>
										<input type="checkbox" ${facetValue.selected ? 'checked="checked"' : ''}  class="facet-checkbox js-facet-checkbox-price sr-only" />
										<span class="facet-label">
											<span class="facet-mark"></span>
											<div class="facet-text">
											<span class="facet-text">
												${facetValue.name}												 
											</span>
											 <ycommerce:testId code="facetNav_count">
													<span class="facet-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
												</ycommerce:testId>
											</div>
										</span>
									</label>
								</form>
								</c:when>

								<c:otherwise>
								<form action="${url}" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>
								<input type="hidden" name="isFacet" value="true"/>
								<input type="hidden" name="facetValue" value="${facetValue.code}"/>
								<c:if test="${facetData.code ne 'dialColour'}"> <!-- Added for TISPRO-490  -->		
								<label>
									<input type="checkbox" ${facetValue.selected ? 'checked="checked"' : ''}  class="facet-checkbox js-facet-checkbox sr-only" />																		
									<c:if test="${facetData.code == 'inStockFlag'}">
									<c:if test="${facetValue.code == 'true' && facetStockSize=='2'}">
										<span class="facet-label">
										<span class="facet-mark"></span>
										<div class="facet-text">
										<span class="facet-text">
											<spring:theme code="text.exclude.outOfStock"/>&nbsp;											

										</span>

										 <ycommerce:testId code="facetNav_count">
												<span class="facet-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
										</ycommerce:testId>
										</div>
									</span>
									</c:if>
									</c:if>
									<c:if test="${facetData.code ne 'inStockFlag'}">
										<span class="facet-label">
										<c:if test="${not empty facetValue.name}">
											<span class="facet-mark"></span>
										</c:if>	
										<div class="facet-text">
											<span class="facet-text">
												${facetValue.name}												 
												</span>
												<ycommerce:testId code="facetNav_count">
													<span class="facet-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
												</ycommerce:testId>												
											</div>
											
										</span>
									</c:if>
								</label>
								</c:if>
							</form>
							
							 </c:otherwise> 
							</c:choose>
							
							</ycommerce:testId>
						</c:if>
						<c:if test="${not facetData.multiSelect}">
							<c:url value="${facetValue.query.url}" var="facetValueQueryUrl"/>
							<span class="facet-text">
								<form action="${url}" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>
								<input type="hidden" name="facetValue" value="${facetValue.code}"/>
								<input type="hidden" name="isFacet" value="true"/>
								<input type="submit" value="${facetValue.name}"  />
								</form>	
							<%-- <a href="#">${facetValue.name}</a>	 --%>					
								<%-- <a href="${facetValueQueryUrl}">${facetValue.name}</a> --%>
								 <%-- <ycommerce:testId code="facetNav_count">

									<span class="facet-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>

								</ycommerce:testId> --%> 
							</span>
						</c:if>
							</c:otherwise>

					</c:choose>
					</li>
				</c:forEach>
			</ul>

			<c:if test="${not empty facetData.topValues}">
			
			<c:set var="remainingFacetValues" value="${facetData.values}" />
			
	        <%-- <spring:eval expression="T(de.hybris.platform.util.Config).getParameter('search.Facet.topValue')" var="facetTopValue"/> 
	        <c:set var="remainingFacetValuesSize" value="${fn:length(remainingFacetValues)-facetTopValue}" />--%>
	        
	        <c:set var="facetTopValues" value="${facetData.topValues}" />
	        
		    <c:set var="remainingFacetValuesSize" value="${fn:length(remainingFacetValues)-fn:length(facetTopValues)}" />	    
		    
			<div class="more-lessFacetLinks active">
				<div class="more js-more-facet-values checkbox-menu">
				
				<c:choose>
				<c:when test="${facetData.code eq 'colour'}" >
				<a href="#" class="js-more-facet-values-link more" >+&nbsp;${remainingFacetValuesSize}&nbsp;<spring:theme code="search.nav.facetShowMore_${facetData.code}" /></a>
				
				</c:when>
				<c:otherwise>
					<a href="#" class="js-more-facet-values-link more" >+&nbsp;${remainingFacetValuesSize}&nbsp;<spring:theme code="search.nav.facetShowMore_${facetData.code}" text="more" /></a>
				</c:otherwise>
				</c:choose>
				
				</div>
				
				<div class="less js-less-facet-values checkbox-menu">
				    <%-- 	<form action="${url}" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>
								<input type="hidden" name="facetValue" value="${facetValue.code}"/>
								<input type="hidden" name="isFacet" value="true"/> --%>
								<input type="submit" value="<spring:theme code="search.nav.facetShowLess_${facetData.code}" text="less..."/>" class="js-less-facet-values-link"  />
				<!-- 				</form> -->
				
					<%-- <a href="#" class="js-less-facet-values-link"><spring:theme code="search.nav.facetShowLess_${facetData.code}" /></a> --%>
				</div>
				</div>
			</c:if>
		
		
		
		
		<c:if test="${facetData.code eq 'price'}">
		    <div class="priceBucketExpand" style="display:none">		    				
				<c:url value="${removeQueryUrlForPriceValue}" var="removeQueryUrl"/>
				<a href="${removeQueryUrl}" ><span class="any_price_mobile">Any Price</span></a>
			</div>		  
			<h4 class="customPriceRange">Price Range</h4>
							<input type="hidden" name="currentPriceQueryParams" value="${searchPageData.currentQuery.query.value}" class="currentPriceQueryParams"/>					  
							 <form action="${url}" method="get" id="customPriceFilter">
							    <input type="hidden" name="offer" value="${offer}"/>
							    <input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="" class="qValueForCustomPrice"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>		
								<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>	
								<input type="hidden" name="isFacet" value="true"/>							
								<input type="hidden" id="facetValue" name="facetValue" value="${facetValue.code}"/>						
								<spring:theme code="text.minPriceSearch.placeholder" var="minPriceSearchPlaceholder" />
							    <input class="minPriceSearchTxt" type="text" id="customMinPriceMob" name="customMinPrice" width="30" height="20" placeholder="${minPriceSearchPlaceholder}" onkeypress="return isNumber(event)">							
							    <spring:theme code="text.maxPriceSearch.placeholder" var="maxPriceSearchPlaceholder" />
							    <span>-</span>
							    <input class="maxPriceSearchTxt" type="text" id="customMaxPriceMob" name="customMaxPrice" width="30" height="20" placeholder="${maxPriceSearchPlaceholder}" onkeypress="return isNumber(event)">
							   
								<input type="button" name ="submitPriceFilter" id ="applyCustomPriceFilterMob"	value="GO"/>
						</form>							
		</c:if>
		</div>
		
		
		
	</li> </c:if> 
			</ycommerce:testId>
			</c:if>
			</c:if>
			</c:if>
			</c:if>
			</c:if>
			</c:if>
			</c:if>
			</c:if>
			</c:if>
</c:if>
</c:if>
</div>

