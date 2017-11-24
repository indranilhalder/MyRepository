<!-- PDP changes start -->
<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Details of style notes tab -->
<div class="tab-details">
	<%-- <ycommerce:testId code="productDetails_content_label"> --%> 
	<ycommerce:testId code="productStyleNotes_content_label">
		<ul style="list-style:none; padding-left:0;">
		<c:choose>
					<c:when test="${product.rootCategory=='FineJewellery' || product.rootCategory=='FashionJewellery'}">
						<div class="product-desc">
							<span class="key-label">
							 <c:forEach var="classification" items="${mapConfigurableAttributes}">
							 <c:if test="${not empty classification.value }">
   						 		<c:forEach var="classValue" items="${classification.value }">
   						 			${classValue.key} &nbsp;&nbsp;${classValue.value}
   						 			 </c:forEach>
							  </c:if> 
								</c:forEach>
								<li class="stylenote" itemprop="description">${product.articleDescription}</li>
							</span> 
						</div>
					</c:when>
					<c:otherwise>
					<li class="stylenote" itemprop="description">${product.articleDescription}</li></c:otherwise>
				</c:choose>
              
              <!-- TPR-1996 START  -->
  		 <c:choose>
          <c:when test="${product.rootCategory=='Watches'}">
          <c:if test = "${not empty functionality}">
       		<div class="tabs_warranty">
             <ul> 
              <li>
  					<spring:theme code="text.product.feature.functionality"/>  - 
                    <c:forEach items="${functionality}" var="function">
  					    ${function} &nbsp
  			        </c:forEach>
  			 </li>
  		    </ul> 
          </div>
     	 </c:if>
     	 
     	  <c:if test = "${not empty movementList}">
          <div class="tabs_warranty">
             <ul> 
              <li>
  					<spring:theme code="text.product.feature.movement"/> - 
                    <c:forEach items="${movementList}" var="movement">
  					    ${movement} &nbsp
  			        </c:forEach>
  			 </li>
  		    </ul> 
          </div>
          </c:if>
          
          <c:if test = "${not empty specialFeatures}">
          <div class="tabs_warranty">
             <ul> 
              <li>
  					<spring:theme code="text.product.feature.specialFeatures"/> -
                    <c:forEach items="${specialFeatures}" var="features">
  					    ${features} &nbsp
  			        </c:forEach>
  			 </li>
  		    </ul> 
          </div>
         </c:if>
           <c:if test = "${not empty Warranty}">
            <div class="tabs_warranty">
             <ul> 
              <li>
  					<spring:theme code="text.product.feature.warranty"/> - 
                    <c:forEach items="${Warranty}" var="warranty">
  					    ${warranty} &nbsp
  			        </c:forEach>
  			 </li>
  		    </ul> 
          </div>
          </c:if>
          </c:when>
          </c:choose>


      <!-- TPR-1996 END  -->
			
  		</ul>
	</ycommerce:testId>
</div>

<!-- PDP changes end -->

