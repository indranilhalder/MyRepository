<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>


<%-- <c:url value="/**/c-${brandCode}" var="searchUrl" /> --%>
<c:url value="/search/" var="searchUrl" />

<input type="hidden" id="brandCodeVal" value="${brandCode}"/>
<div class="ui-front active ">
	<form id="search_form_brand" name="search_form_brand" method="get"
		action="${searchUrl}">
		
		<select name="searchCategory">
  <option value="${brandCode}">${categoryName}</option>
  </select>
		
		<button type="submit" id="brandSearchButton"><%-- ${categoryName} --%></button>
		
		
			   
		
		<spring:theme code="search.placeholder" var="searchPlaceholder" />
		
		<ycommerce:testId code="header_search_input">
		<input type="hidden" name="q" value=""/>
			
			<input type="text" id="js-site-micrositesearch-input"
				class="form-control js-site-micrositesearch-input" name="text" value=""
				maxlength="100" placeholder="Search in ${categoryName}">
		</ycommerce:testId>
		<input type="hidden" name="blpLogo" />
	</form>
</div>
<script>
/* $("#search_form_brand").submit(function(){
	if($("#js-site-micrositesearch-input").val()){
		$("input[name=blpLogo]").val($(".common_logo_slot_wrapper").find(".image").attr("src"));
		return true;
	}else{
		return false;
	}
}); */

$("#search_form_brand").submit(function(){
	var brandVal=$("#brandCodeVal").val();
	//alert(brandVal);
	if($("#js-site-micrositesearch-input").val()){
		var q=$("#js-site-micrositesearch-input").val()+":relevance:brand:"+brandVal;
		$("input[name=blpLogo]").val($(".common_logo_slot_wrapper").find(".image").attr("src"));
		$("input[name=q]").val(q);
		return true;
	}else{
		return false;
	}
});

</script>