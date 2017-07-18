<!-- PDP changes start -->
<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Details for Product Description tab -->

<div class="tab-details">
	<ycommerce:testId code="productStyleNotes_content_label">
	   <c:if test="${not empty product.articleDescription}">
	   <%-- INC144316941 Start --%>
	   <ul class="item-desc">
	   <li itemprop="description">
  		${product.articleDescription}
  		</li>
  		</ul>
  		<%-- INC144316941 End --%>
  		
  		<!-- TPR-1996 START  -->
  		 <c:if test = "${not empty Warranty}">
         <div class="tabs_warranty">
             <ul> 
              <li>
  					<spring:theme code="text.product.feature.warranty"/> - 
                    <c:forEach items="${Warranty}" var="warranty">
  					    ${warranty}
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
  					    ${features}
  			        </c:forEach>
  			 </li>
  		    </ul> 
          </div>
           </c:if>
           
           
           <c:if test = "${not empty movementList}">
          <div class="tabs_warranty">
             <ul> 
              <li>
  					<spring:theme code="text.product.feature.movement"/> 
                    <c:forEach items="${movementList}" var="movement">
  					    ${movement}
  			        </c:forEach>
  			 </li>
  		    </ul> 
          </div>
          </c:if>
          
          
          <c:if test = "${not empty functionality}">
       		<div class="tabs_warranty">
             <ul> 
              <li>
  					<spring:theme code="text.product.feature.functionality"/>  - 
                    <c:forEach items="${functionality}" var="function">
  					    ${function}
  			        </c:forEach>
  			 </li>
  		    </ul> 
          </div>
     	 </c:if>
          
      <!-- TPR-1996 END  -->
  		</c:if>
  		 
  		<ul>
  		
  			<c:forEach var="classification" items="${mapConfigurableAttribute}">
				   <li>${classification.value}</li>
			</c:forEach>
  			<%-- <li><spring:theme code="product.listing.id"></spring:theme>${product.code}</li> --%>
  		</ul>
  		
	</ycommerce:testId>
</div>
<!-- PDP changes end -->

