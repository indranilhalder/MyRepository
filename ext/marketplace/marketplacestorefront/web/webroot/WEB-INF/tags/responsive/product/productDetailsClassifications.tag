<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
.view-button, .hide-button {
   
    position: relative;
    text-align: center;
    background-color: #a9143c;
    color: #fff;
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

<c:if test="${not empty product.classifications}">
<div class="view-button">Check The Specs</div>
</c:if>
<!-- <div class="hide-button" style="display:none;">Hide Specifications</div> -->
<div class="product-classifications wrapper" >
	<c:if test="${not empty product.classifications}">
		<table class="stats-table">
			<tbody>
				<c:forEach items="${product.classifications}" var="classification" varStatus="outer">
				<tr style="background-color: #f0f4f5;"><td colspan='2' style="font-weight: 700;"><div class="headline">${classification.name}</div></td></tr>
					<c:forEach items="${classification.features}" var="feature" varStatus="inner">
						<tr style="border: 1px solid #f0f4f5;">
							<td style="border-right: 1px solid #f0f4f5;" class="title"><%-- ${outer.index} - ${inner.index} --%>  ${feature.name}</td>
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
		<table class="stats-table mobile">
			<tbody>
				<c:forEach items="${product.classifications}" var="classification">
					<tr style="background-color: #f0f4f5;"><td colspan=2 style="font-weight: 700;"><div class="headline">${classification.name}</div></td></tr>
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