<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
.view-button, .hide-button {
	position: relative;
	text-align: center;
	background-color: #a9143c;
	color: #fff;
	/*  background: -webkit-linear-gradient(91deg, #3f90e4 0%, #21f7ff 99%), url('../images/header-grad.png');
    background: linear-gradient(91deg, #3f90e4 0%, #21f7ff 99%), url('../images/header-grad.png'); */
	text-transform: uppercase;
	display: block;
	line-height: 30px;
	width: 300px;
	padding-top: 5px;
	padding-bottom: 5px;
	margin: 20px auto;
	font-weight: 600;
	-webkit-transition: top 0.15s 0.3s;
	-moz-transition: top 0.15s 0.3s;
	transition: top 0.15s 0.3s;
}
</style>
<script>
	/* $(document).ready(function(){
	 $(".view-button").click(function(){
	 $(".hide-button").show();
	 $(".view-button").hide();
	 $(".product-classifications").fadeToggle();
	 });
	 $(".hide-button").click(function(){
	 $(".hide-button").hide();
	 $(".view-button").show();
	 $(".product-classifications").fadeToggle();
	 });
	 });
	 */
</script>
<c:choose>
<c:when test="${product.rootCategory!='FashionJewellery' and product.rootCategory=='FineJewellery'}">		
		<%-- <div class="about-pro">
		    <p>${product.articleDescription}</p>
		</div> --%>
		<div class="accordin">      
			<c:forEach items="${product.classifications}" var="classification" varStatus="outer">
		    	<div class="item">
		        	<div class="title">
		            	<a href="">${classification.name}</a>
		            </div>
			        <div class="detail" style="display: block;">
			        	<ul>
			            	<c:forEach items="${classification.features}" var="feature" varStatus="inner">
			                	<li>${feature.name} 
			                		<span>
			                			<c:forEach items="${feature.featureValues}" var="value" varStatus="status">
												${value.value}
											<c:choose>
												<c:when test="${feature.range}">
													${not status.last ? '-' : feature.featureUnit.symbol}
												</c:when>
												<c:otherwise>
													${feature.featureUnit.symbol}
													${not status.last ? '<br/>' : ''}
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</span>
								</li>
			               </c:forEach>
						</ul>
					</div>
		         </div>
			 </c:forEach>
		</div>
		<div id = "showPrice">
		 <p id = "show" class="pricebreakup-link"> </p>
		</div>
		
	    <table id="showPriceBreakup" class="price-breakuplist clearfix" style="display:none"></table> 
	</c:when>
<c:when test="${product.rootCategory=='Watches'}">
<c:if test="${not empty product.classifications}">
	 <div class="view-button">Check The Specs</div>
</c:if>
<!-- <div class="hide-button" style="display:none;">Hide Specifications</div> -->
 <div class="product-classifications wrapper">
	<c:if test="${not empty product.classifications}">
		<table class="stats-table">
			<tbody>
						<tr style="background-color: #f0f4f5;">
						<td colspan='2' style="font-weight: 700;"><div
								class="headline">Functions and Features</div></td>
					</tr>
						<c:if test="${not empty mapConfigurableAttributes }">
							<c:forEach var="classification"
								items="${mapConfigurableAttributes}">
								<tr style="border: 1px solid #f0f4f5;">
									<td style="border-right: 1px solid #f0f4f5;" class="title">
										<%-- ${outer.index} - ${inner.index} --%>
										${classification.key}
									</td>


									<td><c:choose>
											<c:when test="${not empty classification.value }">
												<c:forEach var="classValue" items="${classification.value }">
   						 			${classValue.key} &nbsp;&nbsp;${classValue.value}
   						 		 </c:forEach>
											</c:when>
											<c:otherwise>
   						 ${classification.key}
   						</c:otherwise>
										</c:choose></td>
								</tr>
							</c:forEach>
						</c:if>
					
					<%-- <c:otherwise>
						<c:forEach items="${product.classifications}" var="classification"
							varStatus="outer">

								<tr style="background-color: #f0f4f5;">
									<td colspan='2' style="font-weight: 700;"><div
											class="headline">${classification.name}</div></td>
								</tr>

							<c:forEach items="${classification.features}" var="feature"
								varStatus="inner">
								<tr style="border: 1px solid #f0f4f5;">
									<td style="border-right: 1px solid #f0f4f5;" class="title">
										${outer.index} - ${inner.index} ${feature.name}
									</td>
									<td><c:forEach items="${feature.featureValues}"
											var="value" varStatus="status">
										${value.value}
										<c:choose>
												<c:when test="${feature.range}">
												${not status.last ? '-' : feature.featureUnit.symbol}
											</c:when>
												<c:otherwise>
												${feature.featureUnit.symbol}
												${not status.last ? '<br/>' : ''}
											</c:otherwise>
											</c:choose>
										</c:forEach></td>
								</tr>
							</c:forEach>
						</c:forEach>

					</c:otherwise> --%>
				


			</tbody>
		</table>
		<table class="stats-table mobile">
			<tbody>
				<c:forEach items="${product.classifications}" var="classification">
					<tr style="background-color: #f0f4f5;">
						<td colspan=2 style="font-weight: 700;"><div class="headline">${classification.name}</div></td>
					</tr>
					<c:forEach items="${classification.features}" var="feature">
						<tr>
							<td style="border-right: 1px solid #f0f4f5;" class="title">${feature.name}</td>
							<td><c:forEach items="${feature.featureValues}" var="value"
									varStatus="status">
										${value.value}
										<c:choose>
										<c:when test="${feature.range}">
												${not status.last ? '-' : feature.featureUnit.symbol}
											</c:when>
										<c:otherwise>
												${feature.featureUnit.symbol}
												${not status.last ? '<br/>' : ''}
											</c:otherwise>
									</c:choose>
								</c:forEach></td>
						</tr>
					</c:forEach>
				</c:forEach>
			</tbody>
		</table> 
	</c:if>
</div>
</c:when> 
<%--  <c:when test="${product.rootCategory ne 'Watches'}"> --%>
<c:otherwise>
<!-- TPR-792 changes start -->
<c:if test="${not empty product.classifications}">
<div class="SpecWrap">
  <div class="Padd">
    <h2>Specifications</h2>
    <div class="tabs-block">
    <%-- <c:when test="${product.rootCategory=='Watches'}">
      <div class="nav-wrapper">
	  <span></span>
        <ul class="nav pdp specNav">
           <li>Functions and Features</li>
         </ul>
         </div>
         <ul class="tabs pdp specTabs">
         <c:if test="${not empty mapConfigurableAttributes }">
             <li>
			<div class="tab-details">
			<ul>
         <c:forEach var="classification"	items="${mapConfigurableAttributes}">
					<li>
					<span>
						${outer.index} - ${inner.index}
						${classification.key}
					</span>
					<span>
					<c:choose>
					<c:when test="${not empty classification.value }">
					<c:forEach var="classValue" items="${classification.value }">
					  ${classValue.key} &nbsp;&nbsp;${classValue.value}
   					</c:forEach>
					</c:when>
					<c:otherwise>
   					  ${classification.key}
   					</c:otherwise>
					</c:choose>
					</span>
					</li>
         </c:forEach>
         </ul>
		</div>
		</li>
         </c:if>
         </ul>
    </c:when> --%>
   
      <div class="nav-wrapper">
	  <span></span>
        <ul class="nav pdp specNav">
      <c:forEach items="${product.classifications}" var="classification" varStatus="outer">
      <li>${classification.name}</li>
      </c:forEach>
        </ul>
         </div>
         <ul class="tabs pdp specTabs">
        		<c:forEach items="${product.classifications}" var="classification" varStatus="outer">
					
					<li>
					<div class="tab-details">
					<ul>
					<c:forEach items="${classification.features}" var="feature" varStatus="inner">
					<c:forEach items="${feature.featureValues}" var="value"
									varStatus="status">
             			 <li>
							<span><%-- ${outer.index} - ${inner.index} --%>  ${feature.name}</span>
							<span>
										${value.value}
										<c:choose>
										<c:when test="${feature.range}">
												${not status.last ? '-' : feature.featureUnit.symbol}
											</c:when>
										<c:otherwise>
												${feature.featureUnit.symbol}
												${not status.last ? '<br/>' : ''}
											</c:otherwise>
									</c:choose>
								</span>
							</li>
							</c:forEach>
								</c:forEach>
							</ul>
								</div>
						</li>
				
				</c:forEach>
         
         </ul>
     
      </div>
  </div>
</div>
 </c:if>
</c:otherwise>
</c:choose>
<!-- TPR-792 changes end -->