<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<style>
.compareSection {
	display: none;
	background-color: #f5f5f5;
	border: 1px solid #ddd;
	font-size: 11px;
	margin: 0;
	overflow: visible;
	height: auto;
	position: fixed;
	bottom: 0;
	left: 0;
	width: 100%;
	padding: 15px;
	text-align: center;
	font-family: "Avenir Next";
	z-index: 3;
}

.compareSection .compare-titleContent {
	text-align: left;
	margin-top: 11px;
	font-size: 13px;
}

.compare-wrapper {
	width: 20%;
	text-align: left;
}

#compareProducts {
	width: 80%;
	float: left;
}

#compareProducts .compare-item {
	float: left;
	width: 24%;
	margin-right: 1%;
}

#compareProducts .compare-item img {
	width: 40%;
	float: left;
}

#compareProducts .compare-item ul {
	width: 60%;
	float: left;
	font-size: 15px;
	padding-left: 10px;
	text-align: left;
	padding-bottom:25px;
}

#compareProducts .compare-item ul li:nth-child(2) {
	text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
}

.compare-item .content li {
	line-height: 22px;
}

.compare-item .content li:last-child {
	margin-top: 10px;
}

.compareBtn {
	color: white;
	font-size: 11px;
	margin: 15px 0;
	height: 40px;
	line-height: 15px;
	font-weight: normal;
	text-align: left;
}

.compareBtn.enabled:hover {
	color: #A9143C;
	border: 2px solid #A9143C;
	background-color: #fff;
}

.compareBtn.disabled {
	background: #A9143C;
	opacity: 0.5;
	cursor: default;
	color: #fff;
}

.compareBtn.enabled {
	background: #A9143C;;
}

.compareError {
	color: red;
	font-size: 16px;
	margin-bottom: 15px;
	font-weight: 500;
	letter-spacing: 1px;
}

.closeLink {
	position: absolute;
	top: 6px;
	right: 5px;
	color: #000;
	font-size: 17px;
	font-weight: 600;
}

.compareRemoveLink {
	color: #cc3827;
	text-decoration: underline;
	margin-top: 15px;
}

.compareRemoveLink:hover {
	color: #cc3827;
}

.compare-wrapper, .compareProducts {
	float: left;
}

.comapreProducts {
	font-size: 25px;
	font-weight: 600;
	text-align:
}

.compare-selectedProducts {
	display: none;
}

@media ( max-width :650px) {
	#compareSection {
		min-height: 100px;
	}
	#compareProducts {
		display: none;
	}
	.compareBtn {
		float: right;
		position: absolute;
		right: 0px;
		bottom: -10px;
		height: 35px;
		line-height: 1;
	}
	.compare-wrapper {
		width: 100%;
		position: relative;
	}
	.closeLink {
		right: 0px;
	}
	.compare-selectedProducts {
		display: block;
		font-size: 13px;
		text-decoration: underline;
		font-weight: normal;
		margin-top: 10px;
		cursor: pointer;
	}
	#compareProducts {
		width: 100%;
		padding: 20px 0;
	}
	#compareProducts .compare-item {
		width: 48%;
		float: none;
    	display: inline-block;
    	vertical-align: top;
	}
	.hideSelections {
		display: none;
	}
	.showSelections {
		display: block;
	}
}

@media ( min-width :651px) and (max-width:1050px) {
	.compare-wrapper {
		width: 20%;
		text-align: left;
	}
	#compareProducts {
		width: 80%;
		float: left;
	}
	#compareProducts .compare-item {
		float: left;
		margin-right: 1%;
	}
}
</style>
<script>
	$(document).ready(function() {
		$(".compare-selectedProducts").click(function() {
			$("#compareProducts").toggle();
			$(".compare-selectedProducts span").toggleClass("hideSelections");
			$(".compare-selectedProducts span").toggleClass("showSelections");
		});

	});
</script>
<div id="compareSection" class="compareSection">
	<div id="compareError" class="compareError"></div>
	<div class="compare-wrapper">
		<h2 class="comapreProducts">Compare Products</h2>
		<p class="compare-titleContent">Compare upto 4 products</p>
		<a href="#" class="closeLink">Close</a>
		<p class="compare-selectedProducts">
			<span class="showSelections">See Selections</span> <span
				class="hideSelections">Hide Selections</span>
		</p>
		<c:choose>
			<c:when test="${empty sessionScope.compareList }">
				<button class="compareBtn" id="compareBtn" disabled="disabled">Compare
					Now</button>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${fn:length(sessionScope.compareList)>1}">
						<button class="compareBtn enabled" id="compareBtn">Compare
							Now</button>
					</c:when>
					<c:otherwise>
						<button class="compareBtn disabled" id="compareBtn"
							disabled="disabled">Compare Now</button>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</div>
	<div id="compareProducts">

		<c:if test="${not empty sessionScope.compareList}">
			<input type="hidden" value="${sessionScope.compareList}"
				id="compareList">
			<c:forEach items="${sessionScope.compareList}" var="compareItem">
				<div class="compare-item" id="compare-id${compareItem.code}">
					<product:productPrimaryImage product="${compareItem}"
						format="product" />
					<ul class="content">
						<li>${compareItem.brand.brandname}</li>
						<li>${compareItem.name}</li>

						<li class="compare-price">
							<div class="price">

								<c:choose>
									<c:when
										test="${compareItem.productMRP.formattedValue != compareItem.productMOP.formattedValue}">
										<p class="old">
											<format:fromPrice priceData="${compareItem.productMRP}" />
										</p>
										<p class="sale">
											<format:fromPrice priceData="${compareItem.productMOP}" />
										</p>
									</c:when>
									<c:otherwise>
										<format:fromPrice priceData="${compareItem.productMOP}" />
									</c:otherwise>
								</c:choose>
							</div>
						</li>

						<li><a id="${compareItem.code}" class="compareRemoveLink">Remove</a></li>
					</ul>
				</div>
			</c:forEach>
		</c:if>
	</div>


</div>


