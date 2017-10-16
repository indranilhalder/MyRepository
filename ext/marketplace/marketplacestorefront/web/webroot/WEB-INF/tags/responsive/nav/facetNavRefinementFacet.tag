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
<div class="facet_desktop">
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
<!--  TPR-1283 CHANGES Starts-->
 <c:set var="isCatPage" value="false" />
<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'], '/c')}">
  <c:set var="isCatPage" value="true" />
</c:if>
<!--  TPR-1283 CHANGES Ends-->
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

<ycommerce:testId code="facetNav_title_${facetData.code}">
<c:if test="${facetData.values.size()>0}">
	<c:choose>
		<c:when test="${fn:contains(facetData.name, 'Colour')}">
			<c:set var="facetNameClass" value="${facetData.name}"></c:set>
		</c:when>
		<c:otherwise>
			<c:set var="facetNameClass" value="${fn:replace(facetData.name,' ','_')}"></c:set>
		</c:otherwise>
	</c:choose>
	<li class="facet js-facet ${facetNameClass}">
		<div class="facet-name js-facet-name">
		
		<c:choose>
	
			<c:when test="${facetData.code == 'inStockFlag'}">
			<c:if test="${facetStockSize=='2'}">
				<h3 class="${facetData.genericFilter}">${facetData.name}</h3>
			</c:if>		 
			</c:when> 
			
			<%-- 
			//Removed  strap color block for INC_11113
			<c:when test="${facetData.code eq 'strapcolor'}">
				<h4 class="true">Strap Colour</h4>
			</c:when> --%>
			<%-- <c:when test="${facetData.code == 'price'}">
				<h4 class="true">${facetData.name}</h4>
			</c:when> --%>
			<c:otherwise>
				<h3 class="true">${facetData.name}</h3>
			</c:otherwise>
	   </c:choose>
		<c:if test="${facetData.code eq 'brand'}">
		<div class="brandSelectAllMain search ">
		<form class="brandSearchForm" action="#" id="brandNoFormSubmit">
		
		<!-- <button></button> -->
		        <spring:theme code="text.brandSearch.placeholder" var="brandSearchPlaceholder" />
				<input class="brandSearchTxt" type="text" placeholder="${brandSearchPlaceholder}">
			</form>
			
		<!-- <input type="checkbox" id="brandSelectAll" data-url=""> -->
				<%-- <label class="applyBrandFilters" style="float: right;margin-right: 12%;margin-top: -1%;padding-bottom: 4%;">Apply</label>
				Below currentQueryParams input tag is for (brand facet) apply option in SERT page
				<input type="hidden" name="currentQueryParams" value="${searchPageData.currentQuery.query.value}" class="currentQueryParamsApply"/>
							<form action="${url}" method="get" id="brandApply"> 
									<input type="hidden" name="searchCategory" value="${searchCategory}"/>
									<input type="hidden" name="q" value="" class="qValueForApply"/>
									<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
									<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>
							</form> --%>
		<%-- <c:choose>
		
		<c:when test="${param.selectAllBrand eq 'true' }">
		<label class="brandSelectAll" for="brandSelectAll">Uncheck All</label>
		</c:when>
			<c:otherwise>
		<label class="brandSelectAll" for="brandSelectAll">Check All</label>
		</c:otherwise>
		</c:choose> --%>
			
				</div>
		</c:if>
		</div>

		<div class="facet-values js-facet-values js-facet-form ">
			<c:if test="${not empty facetData.topValues}">
				<ul class="facet-list js-facet-top-values active">
					<c:forEach items="${facetData.topValues}" var="facetValue">
 							<%-- 			<li class="filter-${facetData.code}">    --%>
						
						<c:url value="${facetValue.query.url}" var="facetValueQueryUrl"/>
						<c:choose>
						<%-- INC_12606  check added for dialColour-classification--%>
						<c:when test="${(facetData.code eq 'colour'  || facetData.code  eq 'dialColourWatches' || facetData.code eq 'dialColour-classification'|| facetData.code  eq 'colorfamilytrlg')&& not empty facetValue.name}">	<!-- For colorfamilytrlg checking-->
						<li class="filter-colour">
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
								<%-- <input type="submit" value="" style="background:url('${commonResourcePath}/images/multi.jpg');border:1px solid rgb(204, 211, 217);height:36px;padding: 13px 17px; width:36px;background-size:100%;"> --%>
  								<input type="button" class="js-facet-colourbutton" value="" style="background:url('${commonResourcePath}/images/multi.jpg');border:1px solid rgb(204, 211, 217);height:36px;padding: 13px 17px; width:36px;background-size:100%;">
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
							</li>
						</c:when>
						
						<c:otherwise>
						<li class="filter-${facetData.code}">
							<c:if test="${facetData.multiSelect}">
							<c:choose>
								<c:when test="${facetData.code eq 'brand'}">
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
										<c:if test="${not empty facetValue.name}">
											<span class="facet-mark"></span>
											</c:if>
											<!--  TPR-1283 CHANGE Starts--><!-- TISSTRT-1519 fix starts-->
										  <div class="facet-text">
											<span class="facet-text">
											  <c:set var="brandCode" value="${facetValue.code}"/>
											  <c:set var="urls" value=" "/>
											  <c:set var="brandName" value="${fn:replace(facetValue.name, ' ', '-')}" />
											  <c:set var="urls" value="/${catName}-${fn:toLowerCase(brandName)}/c-${catCode}/b-"/>
											   <c:choose>
											   <%-- PRDI-547 fix --%>
											   <c:when test="${isCatPage=='true' && fn:length(catCode) > 5  && fn:containsIgnoreCase(catCode, 'MBH')=='false'}">
											   <a class="brandFacetRequire" href="${urls}${fn:toLowerCase(brandCode)}">
											   ${facetValue.name}</a>	
											   </c:when >
											   <c:otherwise>
											   ${facetValue.name}
											   </c:otherwise>
											   </c:choose>   											 
										  </span>
										  <!--  TPR-1283 CHANGES Ends--><!-- TISSTRT-1519 fix ends-->
											<%-- <div class="facet-text">
											<span class="facet-text">
												${facetValue.name}												 
											</span> --%>
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
								</c:otherwise>
								</c:choose>
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
							</li>
							</c:otherwise>
						</c:choose>
<%-- 						</li> --%>
					</c:forEach>
				</ul>
			</c:if>
			
			<ul class="facet-list js-facet-list <c:if test="${not empty facetData.topValues}">facet-list-hidden js-facet-list-hidden</c:if>">
				<c:forEach items="${facetData.values}" var="facetValue">		
				<!-- TPR-4722 -->
					<c:if test="${facetValue.name== 'Exclude out of stock'}">
				            <input type="hidden" id="out_of_stock_count" value="${facetValue.count}"/>
				     	 </c:if>
				  <!-- TPR-4722 -->   	 
							
				  <c:url value="${facetValue.query.url}" var="facetValueQueryUrl"/>
<%-- 					<li class="filter-${facetData.code}"> --%>

					<c:choose>

						<%--  INC_12606  check added for dialColour-classification --%>
						<c:when test="${(facetData.code eq 'colour' || facetData.code  eq 'dialColourWatches' || facetData.code eq 'dialColour-classification'|| facetData.code  eq 'colorfamilytrlg') && not empty facetValue.name }">		<!-- For TPR-3955, colorfamilytrlg checking-->					
						<li class="filter-colour">	

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
								<%-- <input type="submit" value="" style="background:url('${commonResourcePath}/images/multi.jpg'); border:1px solid rgb(204, 211, 217);height:36px;padding: 13px 17px; width:36px;background-size:100%;"> --%>
  								<input type="button" class="js-facet-colourbutton" value="" style="background:url('${commonResourcePath}/images/multi.jpg'); border:1px solid rgb(204, 211, 217);height:36px;padding: 13px 17px; width:36px;background-size:100%;">
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
								<input type="button" class="js-facet-colourbutton" style="background-color:${colorHexCode}; border:1px solid rgb(204, 211, 217); height: 36px;border-radius: 50%;    padding: 13px 17px;">
								<%-- <input type="button" class="js-facet-colourbutton" style="background-color:${colorHexCode}; border:1px solid rgb(204, 211, 217); height: 36px;    padding: 13px 17px;"  />
								<input type="button" class="js-facet-colourbutton" style="background-color:${colorHexCode}; border:1px solid rgb(204, 211, 217); height: 36px;    padding: 13px 17px;"> --%>
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
							</li>
						</c:when>
						<%-- <c:when test="${facetData.name eq 'size'}"> --%>	
						
						
						
						<c:when test="${facetData.code eq 'size' && not empty facetValue.name}">
						<li class="filter-${facetData.code}">					
							  <form action="${url}" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>
								<input type="hidden" name="isFacet" value="true"/>
								<input type="hidden" name="facetValue" value="${facetValue.code}"/>									
								<c:if test="${not empty facetValue.name && facetValue.name ne 'NO SIZE'}">
									<label>
										<input type="checkbox" ${facetValue.selected ? 'checked="checked"' : ''}  class="facet-checkbox js-facet-checkbox sr-only" value="${facetValue.code}"/>
										<span class="facet-label">
												<span class="facet-mark"></span>
												<div class="facet-text">
												<span class="facet-text">
													${facetValue.name}&nbsp;											
												</span>
												 <ycommerce:testId code="facetNav_count">
														<span class="facet-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
												</ycommerce:testId>
												</div>
											</span>
									</label>
								</c:if>
								</form>
						<%-- <a href="#">${facetValue.name}</a> --%>
							<%-- <a href="${facetValueQueryUrl}&amp;text=${searchPageData.freeTextSearch}">${facetValue.name}</a> --%>
							</li>
						</c:when>						
						
						<c:otherwise>
							
						<li class="filter-${facetData.code}">
					
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
								<c:when test="${facetData.code eq 'brand'}">
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
										<c:if test="${not empty facetValue.name}">
												<span class="facet-mark"></span>
											</c:if>	
											<!--  TPR-1283 CHANGE Starts-->
										  <div class="facet-text">
											<span class="facet-text">
											  <c:set var="brandCode" value="${facetValue.code}"/>
											  <c:set var="urls" value=" "/>
											  <c:set var="brandName" value="${fn:replace(facetValue.name, ' ', '-')}" />
											  <c:set var="urls" value="/${catName}-${fn:toLowerCase(brandName)}/c-${catCode}/b-"/>
											   <c:choose>
											   <%-- PRDI-547 fix --%>
											   <c:when test="${isCatPage=='true' && fn:length(catCode) > 5  && fn:containsIgnoreCase(catCode, 'MBH')=='false'}">
											   <a class="brandFacetRequire" href="${urls}${fn:toLowerCase(brandCode)}">
											   ${facetValue.name}</a>	
											   </c:when >
											   <c:otherwise>
											   ${facetValue.name}
											   </c:otherwise>
											   </c:choose>   											 
										  </span>
										  <!--  TPR-1283 CHANGES Ends-->
											<%-- <div class="facet-text">
												<span class="facet-text">
												${facetValue.name}&nbsp;
												   
												</span> --%>
												<ycommerce:testId code="facetNav_count">
													<span class="facet-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
												</ycommerce:testId>
											<!-- </div> -->
											</div>
										</span>									
								</label>
							</form>
								</c:when>
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
						</li>
							</c:otherwise>

					</c:choose>
<!-- 					</li> -->
				</c:forEach>
			</ul>

			<c:if test="${facetData.code eq 'brand'}">
				<input type="hidden" id="facetTopValuesCnt" value="${fn:length(facetData.topValues)}"/>
		    	<input type="hidden" id="remainingFacetValuesCnt" value="${fn:length(facetData.values)}"/>
			</c:if>
			
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
				<c:when test="${facetData.code eq 'brand'}" >
					<a href="#" class="js-more-facet-values-link more" >+&nbsp;<span>${remainingFacetValuesSize}</span>&nbsp;<spring:theme code="search.nav.facetShowMore_${facetData.code}" text="more" /></a>
				</c:when>
				<c:otherwise>
					<a href="#" class="js-more-facet-values-link more" >+&nbsp;${remainingFacetValuesSize}&nbsp;<spring:theme code="search.nav.facetShowMore_${facetData.code}" text="more" /></a>
				</c:otherwise>
				</c:choose>
				
				</div>
				
				<div class="less js-less-facet-values checkbox-menu">
				    	<form action="${url}" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>
								<input type="hidden" name="facetValue" value="${facetValue.code}"/>
								<input type="hidden" name="isFacet" value="true"/>
								<input type="submit" value="<spring:theme code="search.nav.facetShowLess_${facetData.code}" text="less..."/>" class="js-less-facet-values-link"  />
								</form>
				
					<%-- <a href="#" class="js-less-facet-values-link"><spring:theme code="search.nav.facetShowLess_${facetData.code}" /></a> --%>
				</div>
				</div>
			</c:if>
		
		
		
		
		<c:if test="${facetData.code eq 'price'}">
		    <div class="priceBucketExpand" style="display:none">		    				
				<c:url value="${removeQueryUrlForPriceValue}" var="removeQueryUrl"/>
				<%-- SDI-1034 fix--%>
				<a href="${fn:replace(removeQueryUrl, 
                                '{pageNo}', '1')}" ><span class="any_price">Any Price</span></a>
			</div>		  
			<h3 class="customPriceRange">Price Range</h3>
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
							    <input class="minPriceSearchTxt" type="text" id="customMinPrice" name="customMinPrice" width="30" height="20" placeholder="${minPriceSearchPlaceholder}" onkeypress="return isNumber(event)">							
							    <spring:theme code="text.maxPriceSearch.placeholder" var="maxPriceSearchPlaceholder" />
							    <span>-</span>
							    <input class="maxPriceSearchTxt" type="text" id="customMaxPrice" name="customMaxPrice" width="30" height="20" placeholder="${maxPriceSearchPlaceholder}" onkeypress="return isNumber(event)">
							   
								<input type="button" name ="submitPriceFilter" id ="applyCustomPriceFilter"	value="GO"/>
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
<script>
	


	
</script>
