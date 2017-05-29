<!-- Added for TPR-198 -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/product" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="container plp-wrapper">
	<h4 class="categor-name text-center"></h4>
	<div class="row">
		 <div class="product-sort-wrapper mb-30 col-sm-12">
				<div class="breadcrumb pull-left">
					<!-- <ul>
						<li><a href="javascript:;">Home</a></li>
						<li><a href="javascript:;">Handbags</a></li>
					</ul> -->
				</div>	
				<div class="sort-by-fature pull-right">
					<select class="responsiveSort">
						<option  value="relevance">Relevance</option>
						<option  value="new">New</option>
						<option  value="discount">Discount</option>
						<option  value="low">Low to High</option>
						<option  value="high">High to Low</option>						
					</select>
				</div>
				<div class="sort-wrapper pull-right">
					<div class="btn-group" role="group">
						<button type="button" class="btn active plp-productimg-show">Product</button>
						<button type="button" class="btn plp-modelimg-show">Model</button>
					</div>
				</div>
				<div class="grid-wrapper pull-right">
					<div class="grid-seperator grid-count-two pull-left">
						<span></span>
						<span></span>
					</div>
					<div class="grid-seperator grid-count-three pull-left active">
						<span></span>
						<span></span>
						<span></span>
					</div>
				</div>
			</div><!-- product-sort-wrapper -->
		<div class="col-sm-3 leftbar col-xs-12">
			<div class="facetItem">
				<div class="row facet-desktop">
					<div class="col-xs-12">
						<div class="filterblocks le-checkbox plp-checkbox">
							<product:luxuryProductrefinementcomponent/>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-sm-9 col-xs-12 rightbar right-side-content text-right pull-right nopadding">
			<div class="product-list-wrapper col-sm-12 nopadding">
				<div class="product-list-wrapper-inner">
					<product:luxurySearchresultsgridcomponent/>
				</div>
			</div>
		</div>
	</div>
</div>
