<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="facetData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.FacetData" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<script>
function navigateToPage(queryString,textString)
{
	var urlString=ACC.config.encodedContextPath+"/search/helpmeshop?q="+encodeURIComponent(queryString)+"&text="+textString;
	//alert(urlString);
	window.open(urlString,"_self");
}
</script>
<c:if test="${not empty facetData.values}">
<c:if test="${facetData.code ne 'category'}">
<c:if test="${facetData.code ne 'snsCategory' && facetData.code ne 'micrositeSnsCategory'}">
<c:if test="${facetData.code ne 'deptType'}">
<c:if test="${facetData.code ne 'sellerId'}">
<c:if test="${facetData.code ne 'allMobilePromotions'}">
<c:if test="${facetData.code ne 'allCategories'}">
<!--  fixed for TISSTRT-615-Fixed -->
<c:if test="${facetData.code ne 'vouchers'}">
<!-- End  fixed for TISSTRT-615-Fixed -->

<c:if test="${not empty facetData.values && facetData.code == 'inStockFlag'}">

	<c:set var="facetValuesForStock" value="${facetData.values}" />
	
	<c:set var="facetStockSize" value="${fn:length(facetValuesForStock)}" />
	
</c:if>

<ycommerce:testId code="facetNav_title_${facetData.name}">
<c:if test="${facetData.values.size()>0}">
	<li class="facet js-facet ${facetData.name}">
		<div class="facet-name js-facet-name">
		
		<c:choose>
	
			<c:when test="${facetData.code == 'inStockFlag'}">
			<c:if test="${facetStockSize=='2'}">
				<h4 class="${facetData.genericFilter}">${facetData.name}</h4>
			</c:if>		 
			</c:when> 
			<c:when test="${facetData.code == 'price'}">
				<h4 class="true">${facetData.name}</h4>
			</c:when>
			<c:otherwise>
				<h4 class="${facetData.genericFilter}">${facetData.name}</h4>
			</c:otherwise>
	   </c:choose>
				
				
		<c:if test="${facetData.code eq 'brand'}">
		<div class="brandSelectAllMain search ">
		<form class="brandSearchForm" action="#" id="brandNoFormSubmit">
		
		<button></button>
		        <spring:theme code="text.brandSearch.placeholder" var="brandSearchPlaceholder" />
				<input class="brandSearchTxt" type="text" placeholder="${brandSearchPlaceholder}">
			</form>
			
		<input type="checkbox" id="brandSelectAll" data-url="">
		<c:choose>
		
		<c:when test="${param.selectAllBrand eq 'true' }">
		<label class="brandSelectAll" for="brandSelectAll">Uncheck All</label>
		</c:when>
		
		<c:otherwise>
		<label class="brandSelectAll" for="brandSelectAll">Check All</label>
		</c:otherwise>
		
		
		</c:choose>
			
				</div>
		</c:if>
		</div>

		<div class="facet-values js-facet-values js-facet-form ">
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
								<form action="#" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="submit" value="" style="background:url('${commonResourcePath}/images/multi.jpg'); height:36px; width:36px;">
								</form>
								<%-- <a   onclick="navigateToPage('${facetValue.query.query.value}','${searchPageData.freeTextSearch}')" >
							<!-- 	<a href="onclick="navigateToPage('${facetValue.query.query.value}','${searchPageData.freeTextSearch}')"> -->
						     		<a href="${facetValueQueryUrl}&amp;text=${searchPageData.freeTextSearch}&amp;searchCategory=${searchCategory}">
						     			<img src="${commonResourcePath}/images/multi.jpg" height="36" width="36" title="Multicolor" />
						     		</a>--%>
								</c:when> 
								<c:otherwise>
								<c:set var="colorHexCode" value="#${colorAry[1]}" />
								 <form action="#" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								
								<input type="submit" title="${facetValue.name}" value="" style="background-color:${colorHexCode}; border:1px solid rgb(204, 211, 217); height: 36px;    padding: 13px 17px;"  />
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
								 <form action="#" method="get"> 
									<input type="hidden" name="offer" value="${offer}"/>
									<input type="hidden" name="searchCategory" value="${searchCategory}"/>
									<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
									<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
									<label>
										<input type="checkbox" ${facetValue.selected ? 'checked="checked"' : ''}  class="facet-checkbox js-facet-checkbox sr-only" />
										<span class="facet-label">
											<span class="facet-mark"></span>
											<span class="facet-text">
												${facetValue.name}
												<%-- <ycommerce:testId code="facetNav_count">
													<span class="facet-value-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
												</ycommerce:testId> --%>
											</span>
										</span>
									</label>
								</form>
							</c:if>
							<c:if test="${not facetData.multiSelect}">
								<c:url value="${facetValue.query.url}" var="facetValueQueryUrl"/>
								<span class="facet-text">
								<form action="#" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								
								<input type="submit" value="${facetValue.name}"  />
								</form>
								
								<%-- <a href="#">${facetValue.name}</a> --%>
									<%-- <a href="${facetValueQueryUrl}&amp;text=${searchPageData.freeTextSearch}">${facetValue.name}</a> --%>&nbsp;
									<%-- <ycommerce:testId code="facetNav_count">
										<span class="facet-value-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
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
								<form action="#" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="submit" value="" style="background:url('${commonResourcePath}/images/multi.jpg'); height:36px; width:36px;">
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
							    <form action="#" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								
								<input type="submit" title="${facetValue.name}" value="" style="background-color:${colorHexCode}; border:1px solid rgb(204, 211, 217); height: 36px;    padding: 13px 17px;"  />
									<%-- <c:if test="${facetData.code == 'inStockFlag'}">
									<c:if test="${facetValue.code == 'true' && facetStockSize=='2'}">
										<span class="facet-label">
										<span class="facet-mark"></span>
										<span class="facet-text">
											<spring:theme code="text.exclude.outOfStock"/>&nbsp;
											<ycommerce:testId code="facetNav_count">
												<span class="facet-value-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
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
													<span class="facet-value-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
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
						<c:when test="${facetData.name eq 'size'}">
							   <form action="#" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="submit" value="${facetValue.name}"  />
								</form>
						<%-- <a href="#">${facetValue.name}</a> --%>
							<%-- <a href="${facetValueQueryUrl}&amp;text=${searchPageData.freeTextSearch}">${facetValue.name}</a> --%>
						</c:when>
						
						<c:otherwise>
							
						
					
						<c:if test="${facetData.multiSelect}">
							<ycommerce:testId code="facetNav_selectForm"> 
							 <form action="#" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<label>
									<input type="checkbox" ${facetValue.selected ? 'checked="checked"' : ''}  class="facet-checkbox js-facet-checkbox sr-only" />
									<c:if test="${facetData.code == 'inStockFlag'}">
									<c:if test="${facetValue.code == 'true' && facetStockSize=='2'}">
										<span class="facet-label">
										<span class="facet-mark"></span>
										<span class="facet-text">
											<spring:theme code="text.exclude.outOfStock"/>&nbsp;
											<%-- <ycommerce:testId code="facetNav_count">
												<span class="facet-value-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
											</ycommerce:testId> --%>
										</span>
									</span>
									</c:if>
									</c:if>
									<c:if test="${facetData.code ne 'inStockFlag'}">
										<span class="facet-label">
										<c:if test="${not empty facetValue.name}">
											<span class="facet-mark"></span>
										</c:if>	
											<span class="facet-text">
												${facetValue.name}&nbsp;
												<%-- <ycommerce:testId code="facetNav_count">
													<span class="facet-value-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
												</ycommerce:testId> --%>
											</span>
										</span>
									</c:if>
								</label>
							</form>
							</ycommerce:testId>
						</c:if>
						<c:if test="${not facetData.multiSelect}">
							<c:url value="${facetValue.query.url}" var="facetValueQueryUrl"/>
							<span class="facet-text">
								<form action="#" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								
								<input type="submit" value="${facetValue.name}"  />
								</form>	
							<%-- <a href="#">${facetValue.name}</a>	 --%>					
								<%-- <a href="${facetValueQueryUrl}">${facetValue.name}</a> --%>
								<%-- <ycommerce:testId code="facetNav_count">
									<span class="facet-value-count"><spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/></span>
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
	
		    <c:set var="remainingFacetValuesSize" value="${fn:length(remainingFacetValues)-6}" />
		    
			<div class="more-lessFacetLinks active">
				<div class="more js-more-facet-values checkbox-menu">
					<a href="#" class="js-more-facet-values-link more" > ${remainingFacetValuesSize} &nbsp;<spring:theme code="search.nav.facetShowMore_${facetData.code}" /></a>
				</div>
				<div class="less js-less-facet-values checkbox-menu">
				    	<form action="#" method="get"> 
								<input type="hidden" name="offer" value="${offer}"/>
								<input type="hidden" name="searchCategory" value="${searchCategory}"/>
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<input type="submit" value="<spring:theme code="search.nav.facetShowLess_${facetData.code}" />" class="js-less-facet-values-link"  />
								</form>
				
					<%-- <a href="#" class="js-less-facet-values-link"><spring:theme code="search.nav.facetShowLess_${facetData.code}" /></a> --%>
				</div>
				</div>
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
<script>
	
$(document).ready(function(){
	
	$( "#brandNoFormSubmit" ).submit(function() {
		  event.preventDefault();
		});
	 $(".facet-name.js-facet-name h4").each(function(){
		if($(this).hasClass("true")){
			$(this).addClass("active");
			$(this).parent().siblings('.facet-values.js-facet-values.js-facet-form').addClass("active");
	    	$(this).siblings('.brandSelectAllMain').addClass("active");
	    	$(this).parent().siblings('#searchPageDeptHierTreeForm').find("#searchPageDeptHierTree").addClass("active");
	    	$(this).parent().siblings('#categoryPageDeptHierTreeForm').find("#categoryPageDeptHierTree").addClass("active");
			
		}
}); 
	 
	 
});

	
</script>

