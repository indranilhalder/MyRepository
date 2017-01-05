<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- <link rel="stylesheet" type="text/css" href="style.css"> -->
<c:url value="/brands" var="authenticAndExclusiveUrl" />
<form method="get" action="${authenticAndExclusiveUrl}">

<div class="brands-page">
<div class="brands-left">

<h2><spring:theme code="text.branslist.brands.by.category"/></h2>

<select name="test" class="menu-select" id="mySelect">
<c:forEach items="${subcategoryList}" var="subCategoryList">
				<c:set var="brandCode" value="${subCategoryList.code}" />
				<option id="${brandCode}" class="brandCategory">
				
				${subCategoryList.name}
				</option>
				
				</c:forEach>
<c:forEach items="${brandComponentCollection}" var="brandCollection">
			<c:set var="uid" value="${ brandCollection.uid}" />
<option id="${uid}" class="cmsManagedBrands">
${brandCollection.masterBrandName}
</option>
</c:forEach>
</select> 

	<!--<spring:theme code="home.authenticExclusive.showAllBrands"/>-->

			<div class="toggle">
			
				<a href="">${subCategoryList.name}</a>
			</div> 
			
			<ul>
			<c:forEach items="${subcategoryList}" var="subCategoryList">

				<c:set var="brandCode" value="${subCategoryList.code}" />
				
				<li>

				<a data-id="${brandCode}"  id="${fn:toLowerCase(brandCode)}"  class="brandCategory">${subCategoryList.name}</a>
			
				</li>
               
			</c:forEach>
			
			<c:forEach items="${brandComponentCollection}" var="brandCollection">
			<c:set var="uid" value="${ brandCollection.uid}" />
						<li><a  data-id="${uid}" id="${fn:toLowerCase(uid)}"   class="cmsManagedBrands">${brandCollection.masterBrandName}</a></li>
			</c:forEach>
			</ul>
			</div>
			
			<!--  START : This section is for CMS Managed Section MULTI BRANDS AND OUR FAVOURITES-->
			<ul>
			<c:forEach items="${brandComponentCollection}" var="brandCollection">
			<c:set var="uid" value="${ brandCollection.uid}" />
						<li id="ToBeHidden"><a id="${uid}"  class="cmsManagedBrands">${brandCollection.masterBrandName}</a></li>
						  <li data-id="${uid}" id = "cmsManaged-${uid}" class="subBrands cmsBrands  left-list" >
						
						 <h1>${brandCollection.masterBrandName}</h1>
						 <p><spring:theme code="brand.A-Z.description"/></p> 
						  
						<div class="desktop">
						<div class="nav-wrapper">
						
						<ul class="nav">
        				<li class="active" id="a-g-set"><spring:theme code="text.branslist.a-g"/></li>
        				<li id="h-n-set"><spring:theme code="text.branslist.h-n"/></li>
        				<li id="o-u-set"><spring:theme code="text.branslist.o-u"/></li>
        				<li id="v-z-set"><spring:theme code="text.branslist.v-z"/></li>
      					</ul>
      					</div>
      					
						<ul class="list_custom">
						    <c:if test="${uid eq 'A-ZBrands'}">
						  <c:forEach items="${sortedMapForAToZ}" var="entry">
							<li>
								
								<span class="letter">${entry.key}</span>
								 <c:forEach items="${entry.value}" var="itemValue1">
								 <c:set var="catName" value="${fn:split(itemValue1.name, '||')}" />
								 <c:url var="brandlistUrl" value="/${catName[1]}/c-${fn:toLowerCase(itemValue1.code)}"></c:url>
								 <a href ="${brandlistUrl}">${catName[0]}</a> 
									
									
									</c:forEach>
									
									</li>
						  </c:forEach>
						  </c:if>
						  </ul>
						   <div class="nav-wrapper">
						
						<ul class="nav">
        				<li class="active" id="a-g-set"><spring:theme code="text.branslist.a-g"/></li>
        				<li id="h-n-set"><spring:theme code="text.branslist.h-n"/></li>
        				<li id="o-u-set"><spring:theme code="text.branslist.o-u"/></li>
        				<li id="v-z-set"><spring:theme code="text.branslist.v-z"/></li>
      					</ul>
      					</div>
						</div>
						  
						   </li>
			  </c:forEach>
			  
						    
						    </ul>
						    
						   <ul>
						     <c:forEach items="${mapForCmsBrands}" var="entry">
				
						      <li id = "cmsMultiManaged-${entry.key}" class="cmsMultiBrands left-list" >	
                             
                              <div class="desktop">
                              
                              <div class="nav-wrapper">
						
								<ul class="nav">
        						<li class="active" id="a-g-set"><spring:theme code="text.branslist.a-g"/></li>
        						<li id="h-n-set"><spring:theme code="text.branslist.h-n"/></li>
        						<li id="o-u-set"><spring:theme code="text.branslist.o-u"/></li>
        						<li id="v-z-set"><spring:theme code="text.branslist.v-z"/></li>
      							</ul>
      							</div> 
                            
                            <ul class="list_custom">
							  
								 <c:forEach items="${entry.value}" var="itemValue1">
								 <li>
								 <span class="letter">${itemValue1.key}</span>
								 
								  <c:forEach items="${itemValue1.value}" var="item">
								  <c:set var="catName1" value="${fn:split(item.name, '||')}" />
								   <c:url var="brandlistUrl" value="/${catName1[1]}/c-${fn:toLowerCase(item.code)}"></c:url>
								 
								 <a
									href ="${brandlistUrl}">${catName1[0]}</a> 
									
									</c:forEach>
									</li>
									</c:forEach>
									
									</ul>
									 <div class="nav-wrapper">
						
						<ul class="nav">
        				<li class="active" id="a-g-set"><spring:theme code="text.branslist.a-g"/></li>
        				<li id="h-n-set"><spring:theme code="text.branslist.h-n"/></li>
        				<li id="o-u-set"><spring:theme code="text.branslist.o-u"/></li>
        				<li id="v-z-set"><spring:theme code="text.branslist.v-z"/></li>
      					</ul>
      					</div>
									</div>
									
									</li>
					
						  </c:forEach> 
				   	</ul>
						
		
			 <!--  END : This section is for CMS Managed Section MULTI BRANDS AND OUR FAVOURITES-->
			
			
			
		<ul>
			<c:forEach items="${allBrandListMapForCode}" var="entry">
				<li id="subcategory-${entry.key}" class="subBrands brands-main left-list"
					style="display:none;">
					
						<c:forEach items="${subcategoryList}" var="subCategoryList">
						<c:if test="${subCategoryList.code eq entry.key }">
						<h1>${subCategoryList.name}</h1>
						<p><spring:theme code=""/></p>
						</c:if>
						</c:forEach>
						
						<div class="desktop">
						<div class="nav-wrapper">
						
						<ul class="nav">
        				<li class="active" id="a-g-set"><spring:theme code="text.branslist.a-g"/></li>
        				<li id="h-n-set"><spring:theme code="text.branslist.h-n"/></li>
        				<li id="o-u-set"><spring:theme code="text.branslist.o-u"/></li>
        				<li id="v-z-set"><spring:theme code="text.branslist.v-z"/></li>
      					</ul>
      					</div> 
							<c:set var="values" value="${entry.value}" />
							
							
							<ul class="list_custom">
							<c:forEach items="${values}" var="item">
						
								<li>
								
								<span class="letter">${item.key}</span>
								 <c:forEach items="${item.value}" var="itemValue">
								 <c:set var="catName2" value="${fn:split(itemValue.name, '||')}" />
								   <c:url var="brandlistUrl" value="/${catName2[1]}/c-${fn:toLowerCase(itemValue.code)}"></c:url>
								 
								 <a
									class ="hello" href="${brandlistUrl}">${catName2[0]}</a> 
									</c:forEach>
									
									</li>
					
							</c:forEach>
							
							</ul>
					 	<div class="nav-wrapper">
						
						<ul class="nav">
        				<li class="active" id="a-g-set"><spring:theme code="text.branslist.a-g"/></li>
        				<li id="h-n-set"><spring:theme code="text.branslist.h-n"/></li>
        				<li id="o-u-set"><spring:theme code="text.branslist.o-u"/></li>
        				<li id="v-z-set"><spring:theme code="text.branslist.v-z"/></li>
      					</ul>
      					</div>
					</div>
					
	
				</li>
			</c:forEach>
			</ul>
</div>

</form>
<script>

$(document).ready(function(){
	//var cat = '${param.cat}';
	
	//TPR-1287 Start
	var cat = (window.location.pathname).split("-");
	cat = cat[cat.length-1];
	cat = cat.toLowerCase();
	
	//TPR-1287 end
	
	//alert(cat);
	if(null!= cat ){
		 window.setTimeout(function(){
			//alert("Fire");
			$("#"+cat).click();
			$("a[data-id="+cat+"]").parent().addClass("active");
		},1000); 
		
	}
});
</script>










