<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="com.tisl.mpl.data.MplDepartmentHierarchyData"%>

<script>
/* $(function() {
	var inputArray = ${departmentHierarchyData.hierarchyList};
	if(inputArray!=""||inputArray!=[]){
	constructDepartmentHierarchy(inputArray);
	}
	}); */
	
	
$(document).ready(function(){
	 $(".facet-name.js-facet-name h3").each(function(){
		if($(this).hasClass("false")){
	    	$(this).parent().siblings('#searchPageDeptHierTreeForm').find("#searchPageDeptHierTree").hide(100);
	    	$(this).parent().siblings('#categoryPageDeptHierTreeForm').find("#categoryPageDeptHierTree").hide(100);
		}
	}); 
	 
	 
	 displayPriotrityBrand();
		
});
function displayPriotrityBrand(){
	//PRDI-540 fix
	if ('${PriorityBrandArray}'!=''){	
	 var x= '${PriorityBrandArray}';
	 x=$.parseJSON(x);
	 console.log("PriorityBrandArray"+x);
	 $.each(x.priorityBrands,function(index,brandCode){
		 if($('#facetTopValuesCnt').val()=="0"){
		//	 alert("1");
			 var elements =$('.facet_desktop .js-facet-list').find('input[value='+brandCode+']').parents('li.filter-brand');
			 $('.facet_desktop li.facet.js-facet.Brand').find('ul.facet-list.js-facet-list').prepend(elements);
		 }
		 else{
	//		 alert("2");
		 var elements =$('.facet_desktop .facet-list-hidden').find('input[value='+brandCode+']').parents('li.filter-brand');
		 var elementsN =$('.facet_desktop .js-facet-top-values').find('input[value='+brandCode+']').parents('li.filter-brand');
		 $('.facet_desktop li.facet.js-facet.Brand').find('ul.facet-list.js-facet-top-values,ul.facet-list.facet-list-hidden').prepend(elements);
		 $(elementsN).remove();
		 var facetTopValuesCnt=$("#facetTopValuesCnt").val();
		 var cnt=(facetTopValuesCnt-1);
		 var countLiInTopValues=$('.facet_desktop li.facet.js-facet.Brand').find('ul.facet-list.js-facet-top-values>li').length;
		 if(countLiInTopValues>facetTopValuesCnt)
		 	{
		 	$('.facet_desktop li.facet.js-facet.Brand').find('ul.facet-list.js-facet-top-values>li:gt('+cnt+')').hide();
			}
		 }
		 
	//	$(elements).remove(); 
	 });
	}
}
//
//alert("priorityarray");
$( document ).ajaxComplete(function(event, xhr, settings) {	
	if ( settings.url.includes("getFacetData") ) {
	displayPriotrityBrand(); 
	}
});

</script>

	<input type="hidden" name="isConceirge" id="isConceirge" value="${isConceirge}" />
	<input type="hidden" name="isCategoryPage" id="isCategoryPage" value="${isCategoryPage}" />

	<span class="toggle-filterSerp" onclick="toggleFilter()">Filter By</span>
	<div class="mob-filter-wrapper">
	<div class="filter-title">Filter <a href="#nogo" class="filter-close"></a> </div>
<ul id="product-facet " class="product-facet js-product-facet listing-leftmenu">


		<div class="facet-name js-facet-name facet_mobile">
				<c:if test="${empty hideDepartments}">
				<h3 class="true tree-dept"><span class="filter-nav"><spring:theme code="search.nav.facetTitle" arguments="Department"/></span><span class="category-icons"><span></span></span></h3>
				</c:if>
		</div>
		<div class="facet-name js-facet-name facet_desktop">
				<c:if test="${empty hideDepartments}">
				<h3 class="true active tree-dept"><spring:theme code="search.nav.facetTitle" arguments="Department"/></h3>
				
				</c:if>
		</div>
		<c:choose>
		<c:when test="${isCategoryPage}">
			<form id="categoryPageDeptHierTreeForm" name="categoryPageDeptHierTreeForm" method="get">
			<p class="filter-name facet_mobile"><spring:theme code="search.nav.facetTitle" arguments="Department"/></p>
				<input type="hidden" name="q" id="q" value="${searchPageData.currentQuery.query.value}"/>
				<input type="hidden" name="searchCategory" value="${searchCategory}" />
				<div id="categoryPageDeptHierTree"></div>
			</form>
		</c:when>
		<c:otherwise>
			<form id="searchPageDeptHierTreeForm" method="get">
			<p class="filter-name facet_mobile"><spring:theme code="search.nav.facetTitle" arguments="Department"/></p>
				<input type="hidden" name="q" id="q" />
				<input type="hidden" name="text" id="text" value="${searchPageData.freeTextSearch}"/>
				<input type="hidden" name="searchCategory" id="searchCategoryTree"/>
				<div id="searchPageDeptHierTree"></div>
			</form>
		</c:otherwise>
		</c:choose>


	    <nav:facetNavAppliedFilters pageData="${searchPageData}"/>
	    <nav:facetNavRefinements pageData="${searchPageData}"/>
</ul>
</div>
