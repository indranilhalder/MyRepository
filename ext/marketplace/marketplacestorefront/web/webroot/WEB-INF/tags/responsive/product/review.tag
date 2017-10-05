<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('marketplace.static.resource.host')" var="staticHost"/>

<c:url value="${product.url}" var="productUrl" />
         <li class="product-item"><ycommerce:testId
	                                      	code="product_wholeProduct">
		                   <div class="product-tile">
		                 	<div class="image">
			
			            	 <c:if test="${product.isProductNew eq true}">
					<div style="z-index: 1;" class="new">
					<img class="brush-strokes-sprite sprite-New"
					src="//${staticHost}/_ui/responsive/common/images/transparent.png"><span>New</span>
					</div>  </c:if> <a
					class="thumb" href="${productUrl}" title="${product.productTitle}"> <product:productPrimaryImage
						product="${product}" format="searchPage" lazyLoad="false" /> <%-- 	<product:productSearchPrimaryImage product="${product}" format="searchPage" index="1"/> --%>

				</a>
				</div>
				<div class="short-info">
				<div class="brand">${product.mobileBrandName}</div>
               <p class="company">${product.brand.brandname}</p>		 
				<ycommerce:testId code="product_productName">
					<div>
				
							                  
						<h3 class="product-name">
							<a class="name" href="${productUrl}">${product.productTitle}</a>

						</h3>
					</div>
				</ycommerce:testId>
				</div>
				</div>

	</ycommerce:testId></li>
