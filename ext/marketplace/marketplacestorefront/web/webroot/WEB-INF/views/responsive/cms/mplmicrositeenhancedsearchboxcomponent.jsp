<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<script language='javascript'>
	$(document).ready(function() {
		var mSellerName = '${mSellerName}';
		$(".micrositeSellerName").html(mSellerName);
	
		 var selectedItemText = $("#micrositeSearchCategory").find('option:selected').text();
		 $("#micrositesearchBoxSpan").html(selectedItemText);
		$('.select-view .select-list').hover(function(){
			 $(this).find('ul').slideDown();
		 });
		
			$(".micrositeDropdown li").click(function() {
			
				$("#micrositeSearchCategory").val(this.id);
				$(this).addClass("selected");
				$(this).parents('.select-list').find('ul').slideUp();
				$("#js-site-micrositesearch-input").focus(); 
			});
	
			
		$("#search_form_microsite").submit(function(event) {
			if($("#js-site-micrositesearch-input").val().trim()=="") {
			
				var actionText = ACC.config.contextPath;

				var dropdownValue = $("#micrositeSearchCategory").val();
				var dropdownName = $("#micrositeSearchCategory").find('option:selected').text();
				if(dropdownValue=="all"){
					return false;
				}
				else{
					
					if (dropdownValue.startsWith("category-")) {
						actionText = (actionText + '/Categories/' + dropdownName + '/c/' + dropdownValue.replace("category-",""));
					}
					if (dropdownValue.startsWith("brand-")) {
						actionText = (actionText + '/Categories/' + dropdownName + '/c/' + dropdownValue.replace("brand-",""));
					}
					
				}
				
				$("#search_form_microsite :input").prop("disabled", true);
				$('#search_form_microsite').attr('action',actionText);
			} 
		});
		
		/*------------Start of SNS auto complete for microsite page----------*/
		
		var style_microsite = null ;

		if(!($('body').hasClass("page-multiStepCheckoutSummaryPage") || $('body').hasClass("page-checkout-login")))
			{
				 if($(window).scrollTop() == 0){
					 window.setTimeout(function(){
						 style_microsite = "width: "+$("#js-site-micrositesearch-input").css("width")+"; left: "+$("#js-site-micrositesearch-input").offset().left+"px";
						 
						 $("form#search_form_microsite + ul.ui-autocomplete.ui-front#ui-id-2").attr("style",style_microsite);
					 },100);
					 
				 }	
				
				 style_microsite = "width: "+$('#js-site-micrositesearch-input').css('width')+"; left: "+$('#js-site-micrositesearch-input').offset().left+"px";
			}
		  $("form#search_form_microsite + ul.ui-autocomplete.ui-front#ui-id-2").attr("style",style_microsite);
		  
		  $("#js-site-micrositesearch-input").keypress(function(){
				$("form#search_form_microsite + .ui-autocomplete.ui-front.links.ui-menu").css("border","1px solid #dbeafa");
			});

/* -----------------END of SNS auto complete microsite page---------*/	
		
	});
</script>




<c:url value="/search/" var="searchUrl" />
<c:url value="/micrositeSearch/autocomplete/${component.uid}/sellerID/${mSellerID}"
	var="autocompleteUrl" />
	
<c:url value="/micrositeSearchCategory/" var="micrositeSearchCategory" />

<div class="ui-front active ">

	<form id="search_form_microsite" name="search_form_microsite" method="get" action="${searchUrl}">


				
		<input type="hidden" name="mSellerID" 
				value="${mSellerID}" /> 
		<%-- 
		<div class="control-group left">
			<spring:theme code="text.search" var="searchText"/>
			<label class="control-label skip" for="search">${searchText}</label>
			
			<div class="controls">							
				<spring:theme code="search.placeholder" var="searchPlaceholder"/>
				<ycommerce:testId code="header_search_input">
					<input id="search" class="siteSearchInput left" type="text" name="text" value="" maxlength="100" placeholder="${searchPlaceholder}" 
						data-options='{"autocompleteUrl" : "${autocompleteUrl}","minCharactersBeforeRequest" : "${component.minCharactersBeforeRequest}","waitTimeBeforeRequest" : "${component.waitTimeBeforeRequest}","displayProductImages" : ${component.displayProductImages}}'/>
				</ycommerce:testId>
				<ycommerce:testId code="header_search_button">
					<button class="siteSearchSubmit" type="submit"/></button>
				</ycommerce:testId>
			</div>
		</div> 
--%>

		<span> <ycommerce:testId code="header_search_button">
				<button></button>
			</ycommerce:testId>
		</span>

		<!-- search category List -->

		<div class="select-view">
			<select id="micrositeSearchCategory" class="select-view" name="micrositeSearchCategory">
				<option value="all" class="micrositeSellerName"></option>
				<option disabled>----------</option>

				<c:forEach items="${categoryList }" var="category">
					<option value="category-${category.code }"
						<c:if test="${category.code eq categoryCode }"> 
					selected = "selected"
					</c:if>
						<c:if test="${category.name eq dropDownText }"> 
						selected = "selected"
					</c:if>>${category.name }
					</option>
				</c:forEach>
				<option disabled>----------</option>
				<c:forEach items="${brands }" var="brand">
					<option value="brand-${brand.code }"
						<c:if test="${brand.code eq categoryCode }"> 
						selected = "selected"
					</c:if>
						<c:if test="${brand.code eq dropDownText }"> 
						selected = "selected"
					</c:if>>
						${brand.name}</option>
				</c:forEach>
				<option disabled>----------</option>
		
			</select>

			<div class="select-list">
				<span class="selected selected-dropdownText micrositeSellerName" id="micrositesearchBoxSpan"></span>

				<ul class="micrositeDropdown" label="All">
					<li id="all" class="micrositeSellerName"></li>
				</ul>
			   
				<ul class="micrositeDropdown" label="Departments">
					<c:forEach items="${categoryList }" var="category">
						<li id="category-${category.code }"
							<c:if test="${category.code eq categoryCode }"> 
					class = "selected"
					</c:if>
							<c:if test="${category.name eq dropDownText }"> 
						class = "selected"
					</c:if>>${category.name }</li>
					</c:forEach>
				</ul>
				
				<ul class="micrositeDropdown" label="Brands">
					<c:forEach items="${brands }" var="brand">
				
						<li id="brand-${brand.code }"
							<c:if test="${brand.code eq categoryCode }"> 
						class = "selected"
					</c:if>
							<c:if test="${brand.name eq dropDownText }"> 
						class = "selected"
					</c:if>>
							${brand.name}</li>
					</c:forEach>
				</ul>
		
			</div>
		</div>

		<!-- search category  List-->



		<!-- searchIcon -->


		<!-- searchIcon -->

		<spring:theme code="search.placeholder" var="searchPlaceholder" />

		<ycommerce:testId code="header_search_input">
			<input type="text" id="js-site-micrositesearch-input"
				class="form-control js-site-micrositesearch-input" name="text" value=""
				maxlength="100" placeholder="Search in ${mSellerName}"
				data-options='{"autocompleteUrl" : "${autocompleteUrl}","minCharactersBeforeRequest" : "${component.minCharactersBeforeRequest}","waitTimeBeforeRequest" : "${component.waitTimeBeforeRequest}","displayProductImages" : ${component.displayProductImages}}'>
		</ycommerce:testId>



	</form>

</div>
<%-- <div style="color:#333;position:absolute;top:6px;"><cms:pageSlot position="MiniCart" var="component">
									<cms:component component="${component}" />
								</cms:pageSlot></div> --%>

