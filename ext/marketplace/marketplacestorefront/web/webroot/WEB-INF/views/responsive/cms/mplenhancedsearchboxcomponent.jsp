<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script language='javascript'>
	$(document).ready(function() {
		 var selectedItemText = $("#searchCategory").find('option:selected').text();
		 $("#searchBoxSpan").html(selectedItemText);
		 
		 $('.select-view .select-list').hover(function(){
			 $(this).find('ul').slideDown();
		 });
		$(".dropdown li").click(function() {
			$("#searchCategory").val(this.id);
			//sessionStorage.setItem("selectedItemValue",$("#searchCategory").val());
			//sessionStorage.setItem("selectedItemText",$(this).html());
			$(this).addClass("selected");
			$(this).parents('.select-list').find('ul').slideUp();
			$("#js-site-search-input").focus(); 
		});
		
		$('#searchButton').click(function(e){

			if($("#js-site-search-input").val().trim()==""){
				if($("#searchCategory").val().startsWith("all")){
					e.preventDefault();
				}
				else{
					var actionText = ACC.config.contextPath;
					var dropdownValue = $("#searchCategory").val();
					var dropdownName = $("#searchCategory").find('option:selected').text();
					if (!String.prototype.startsWith) {
						  String.prototype.startsWith = function(searchString, position) {
						    position = position || 0;
						    return this.indexOf(searchString, position) === position;
						  };
						}
					if (dropdownValue.startsWith("MSH") || dropdownValue.startsWith("MBH")) {
						actionText = (actionText + '/Categories/' + dropdownName + '/c/' + dropdownValue);
					} else if (!dropdownValue.startsWith("all")) {
						actionText = (actionText + '/s/' + dropdownValue);
					}
					$('#search_form').attr('action',actionText);
				}
			}

		});
		/* $("#search_form").submit(function(event) {
			if($("#js-site-search-input").val().trim()=="") {
				var actionText = ACC.config.contextPath;
				var dropdownValue = $("#searchCategory").val();
				var dropdownName = $("#searchCategory").find('option:selected').text();

				if (!String.prototype.startsWith) {
					  String.prototype.startsWith = function(searchString, position) {
					    position = position || 0;
					    return this.indexOf(searchString, position) === position;
					  };
					}
				
				if (dropdownValue.startsWith("MSH") || dropdownValue.startsWith("MBH")) {
					actionText = (actionText + '/Categories/' + dropdownName + '/c/' + dropdownValue);
				} else if (!dropdownValue.startsWith("all")) {
					actionText = (actionText + '/s/' + dropdownValue);
				}
				$("#search_form :input").prop("disabled", true);
				$('#search_form').attr('action',actionText);
			} 
		}); */
	});
</script>

<c:url value="/search/autocomplete/${component.uid}"
	var="autocompleteUrl" />
<c:url value="/search/" var="searchUrl" />
<c:url value="/searchCategory/" var="searchCategory" />
<div class="ui-front active ">
	
	<c:if test="${not empty categoryList}">

		<c:set var="categoryList" value="${categoryList}" />
	
		<c:set var="categorySize" value="${fn:length(categoryList)}" />
		
	
	</c:if>
	
	<c:if test="${not empty brands}">

		<c:set var="brandsList" value="${brands}" />
	
		<c:set var="brandSize" value="${fn:length(brandsList)}" />
		
	
	</c:if>
	
	<c:if test="${not empty sellers}">

		<c:set var="sellerList" value="${sellers}" />
	
		<c:set var="sellerSize" value="${fn:length(sellerList)}" />
		
	
	</c:if>
	
	<form id="search_form" name="search_form" method="get" action="${searchUrl}">
		<span> <ycommerce:testId code="header_search_button">
				<button id='searchButton' tabindex="2"></button>
			</ycommerce:testId>
		</span>
		<!-- search category List -->
		<div class="select-view">
			<select id="searchCategory" name="searchCategory">
				<optgroup label="All">
					<option selected="selected" value="all"><spring:theme code="text.all" /></option>
				</optgroup>
					
				<c:if test="${categorySize >0}">
				
				<optgroup label="Departments">
					<c:forEach items="${categoryList }" var="category">
							<option value="${category.code}" ${searchCode == category.code ? 'selected' : '' }>${category.name}</option>
					</c:forEach>
				</optgroup>
				</c:if>	
				
				<c:if test="${brandSize >0}">
				
				<optgroup label="Brands">
					<c:forEach items="${brands }" var="brand">
						<option value="${brand.code}" ${searchCode == brand.code ? 'selected' : '' }>${brand.name}</option>
					</c:forEach>
				</optgroup>
				</c:if>
				
				<c:if test="${sellerSize >0}">
				<optgroup label="Sellers">
					<c:forEach items="${sellers}" var="seller">
							<option value="${seller.id}" ${searchCode == seller.id ? 'selected' : '' }>${seller.legalName}</option>
					</c:forEach>
				</optgroup>
				</c:if>
			</select>

			<div class="select-list">
				<span class="selected selected-dropdownText searchBox" id="searchBoxSpan"><spring:theme code="text.all"/></span>

				<ul class="dropdown" label="All">
					<li id="all"><spring:theme code="text.all"/></li>
				</ul>
				<ul class="dropdown" label="Departments">
					<c:forEach items="${categoryList }" var="category">
						<li id="${category.code }" ${searchCode == category.code ? 'class=\"selected\"' : '' }>${category.name }</li>
					</c:forEach>				</ul>
				<ul class="dropdown" label="Brands">
					<c:forEach items="${brands }" var="brand">
						<li id="${brand.code } ${searchCode == brand.code ? 'class=\"selected\"' : '' }">${brand.name}</li>
					</c:forEach>
				</ul>
				<ul class="dropdown" label="Sellers">
					<c:forEach items="${sellers }" var="seller">
						<li id="${seller.id }" ${searchCode == seller.id ? 'class=\"selected\"' : '' }>${seller.legalName }</li>
					</c:forEach>
				</ul>
			</div>
		</div>

		<spring:theme code="search.placeholder" var="searchPlaceholder" />

		<ycommerce:testId code="header_search_input">
			<input type="text" id="js-site-search-input" tabindex="1"
				class="form-control js-site-search-input" name="text" value=""
				maxlength="250" placeholder="${searchPlaceholder}"
				data-options='{"autocompleteUrl" : "${autocompleteUrl}","minCharactersBeforeRequest" : "${component.minCharactersBeforeRequest}","waitTimeBeforeRequest" : "${component.waitTimeBeforeRequest}","displayProductImages" : ${component.displayProductImages}}'>
		</ycommerce:testId>
	</form>
</div>
<%-- <div style="color:#333;position:absolute;top:6px;"><cms:pageSlot position="MiniCart" var="component">
									<cms:component component="${component}" />
								</cms:pageSlot></div> --%>

