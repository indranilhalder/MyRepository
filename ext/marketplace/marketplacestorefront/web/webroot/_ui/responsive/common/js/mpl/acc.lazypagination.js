var pageNoPagination = 1;
var totalNoOfPages = 0;
var innerRecordSize = 7;
var loadMoreCount = 72;
var initPageLoad = true;
var directPaginatedLoad = true;
var lazyPagePush = true;
var serpURL;
var ajaxUrl = '';
var recordsLoadedCount = 0;
var pageType = $('#pageType').val();
var isSerp = false;

$(document).ready(function(){
	
$(window).on('scroll', function() {
	var productItemArrayLength = $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,ul.product-listing.product-grid.lazy-grid-normal,ul.product-listing.product-grid.custom-sku').find('li.product-item').length;
	
    if(productItemArrayLength > 16){
    	lazyPushInitalPage();
    }
	innerLazyLoad({effect: true});
});

$(document).on('click', '.pageNo', function() {
    var clickedPageNo = parseInt($(this).text());
    getProductSetData(clickedPageNo);
});
});

		  
function lazyPushInitalPage(){
	
	var pathName = window.location.pathname;
	var pageNoExists = /page-[0-9]+/.test(pathName);

	if(lazyPagePush && !pageNoExists){
		var query = window.location.search;
		var initialUrl = pathName.replace(/[/]$/,"") + '/page-1';
		if(pageType == 'productsearch'){//for serp initial page 
		initialUrl = initialUrl + '?'+ $('#searchPageDeptHierTreeForm').serialize();
		}else if(query){
		initialUrl = initialUrl + query;
		}
		window.history.replaceState({}, "", initialUrl);
		lazyPagePush = false;
	}
}

function innerLazyLoad(options) {
	if(typeof(options)!='undefined' && options.isSerp){
    	totalNoOfPages = $('input[name=noOfPages]').val();
        totalNoOfPages == '' ? 0 : parseInt(totalNoOfPages);
        isSerp = true;
    }
    //get the 8 items from the array and render // TODO: identify place holder done
    var gridHTML = '';
    //if total no of pages is 1 then load all 24 products at once.
    if(1 == totalNoOfPages){
    	$.each(productItemArray, function(index, element) {
            gridHTML += '<li class="product-item">' + $(element).html() + '</li>';
        });
    }else{
    	 $.each(productItemArray, function(index, element) {
     	    gridHTML += '<li class="product-item">' + $(element).html() + '</li>';
     	 });
    }
   
    if (initPageLoad) { //TODO: duplicate loading prevention
        $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,ul.product-listing.product-grid.lazy-grid-normal,ul.product-listing.product-grid.custom-sku').html(gridHTML).hide().fadeIn(500);
        initPageLoad = false;
        //TISSPTXI-21
        $("img.lazy").lazyload();
    } else {
    	$('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,ul.product-listing.product-grid.lazy-grid-normal,ul.product-listing.product-grid.custom-sku').html(gridHTML);
        $("img.lazy").lazyload();
    }
}

function getProductSetData(pageNoPagination) {

    	 var pathName = window.location.pathname;
    	    var query = window.location.search;
    	    
    	    //INC144316143
    	    if ($('#pageType').val() == 'productsearch' || $('#pageType').val() == 'category') {

    	        window.localStorage.setItem('lastUrlpathName',encodeURI(pathName));
    	        window.localStorage.setItem('lastUrlquery',encodeURI(query));

    	    }
        
        if(pageNoPagination <= totalNoOfPages){
        	
        	if(facetAjaxUrl){
        		ajaxUrl = facetAjaxUrl.replace(/page-[0-9]+/, 'page-' + pageNoPagination);
        		var sort = findGetParameter('sort');
        		if(sort){
        			ajaxUrl = ajaxUrl + '&sort='+ sort;
        		}
        		window.history.replaceState({}, "", ajaxUrl);
        	}else{
        		ajaxUrl = pathName.replace(/page-[0-9]+/, 'page-' + pageNoPagination);
        		var nextPaginatedAjaxUrl = pathName.replace(/page-[0-9]+/, 'page-' + pageNoPagination);
                if (query) {
                    ajaxUrl = ajaxUrl + query;
                    nextPaginatedAjaxUrl = nextPaginatedAjaxUrl + query;
                }
                window.history.replaceState({}, "", nextPaginatedAjaxUrl);
        	}
        	
        	ajaxPLPLoad(ajaxUrl);
        }
}

function ajaxPLPLoad(ajaxUrl){
	
    $.ajax({
        url: ajaxUrl,
        data: {
            pageSize: 24,
            /* q: '', */
            lazyInterface:'Y'
        },
        beforeSend: function() {
            var staticHost = $('#staticHost').val();
            $('ul.product-listing.product-grid.lazy-grid,ul.product-list').after('<p class="lazyLoadPagination" style="text-align: center;margin: 5px 0;font-size: 18px;">Loading...<span class="loaderDiv" style="vertical-align:middle;"><img src="' + staticHost + '/_ui/responsive/common/images/red_loader.gif" class="spinner"></span></p>');
            
        },
        success: function(x) {
            var filtered = $.parseHTML(x);
            var ulProduct = null;
            //for serp
            if($('ul.product-listing.product-grid').length==0){
            	 ulProduct = $(filtered).find('ul.product-list');


            }
            else if($('input[name=customSku]').length == 1){
           	// ulProduct = $(filtered).find('ul.product-listing.product-grid.custom-sku');
            	ulProduct = $(filtered).find('ul.product-list');
            }
            else{
            	 ulProduct = $(filtered).find('ul.product-listing.product-grid.lazy-grid,.product-listing.product-grid.lazy-grid-normal');
            }
          //Add to bag and quick view ui fixes starts here
            $(".product-tile .image .item.quickview").each(function(){
            	if($(this).find(".addtocart-component").length == 1){
            		$(this).addClass("quick-bag-both");
            	}
            });
          //Add to bag and quick view ui fixes ends here
            productItemArray = [];
            
            $(ulProduct).find('li.product-item').each(function() {
                productItemArray.push($(this))
            });
            ACC.quickview.bindToUiCarouselLink();
        },
        complete: function() {
            $('.lazyLoadPagination').remove();
            innerLazyLoad();
            //TPR-4720 first 5 product display
			if($('#pageType').val() == "productsearch"){
				populateFirstFiveProductsSerp();
				dtmSearchTags();
			}
			
			if($('#pageType').val() == "category" || $('#pageType').val() == "electronics"){
				populateFirstFiveProductsPlp();
				dtmSearchTags();
			}
            //ACC.quickview.bindToUiCarouselLink();
        }
    });
}

function sort(this_data,drop_down){
	var item = $(this_data).attr('data-name');
	$('.sort').removeAttr('style');
	if(!drop_down){
		$(this_data).css('color', '#a5173c');
	}
	var pathName = window.location.pathname;
	pathName = pathName.replace(/page-[0-9]+/, 'page-1');
	
	var url = '';
	switch (item) {
	case 'relevance':
		if(pageType == 'productsearch'){
			var url = $('#searchPageDeptHierTreeForm').serialize();
			url = url+'&sort=relevance';
		}
		//SDI-810
		else if(pageType == 'product'){
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			if(url.indexOf('&') > -1){
				url = url+'&sort=relevance';
			} else {
				url = url+'sort=relevance';
			}
		}
		else{
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			url = url+'&sort=relevance';
		}
		ajaxPLPLoad(pathName +'?'+url);
		sortReplaceState(pathName +'?'+url); 
		initPageLoad = true;
		break;
	case 'new':
		if(pageType == 'productsearch'){
			var url = $('#searchPageDeptHierTreeForm').serialize();
			url = url+'&sort=isProductNew';
		}
		//SDI-810
		else if(pageType == 'product'){
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			if(url.indexOf('&') > -1){
				url = url+'&sort=isProductNew';
			} else {
				url = url+'sort=isProductNew';
			}
		}
		else{
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			url = url+'&sort=isProductNew';
		}
		ajaxPLPLoad(pathName +'?'+url);
		sortReplaceState(pathName +'?'+url); 
		initPageLoad = true;
		break;
	case 'discount':
		if(pageType == 'productsearch'){
			var url = $('#searchPageDeptHierTreeForm').serialize();
			url = url+'&sort=isDiscountedPrice';
		}
		//SDI-810
		else if(pageType == 'product'){
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			if(url.indexOf('&') > -1){
				url = url+'&sort=isDiscountedPrice';
			} else {
				url = url+'sort=isDiscountedPrice';
			}
		}
		else{
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			url = url+'&sort=isDiscountedPrice';
		}
		ajaxPLPLoad(pathName +'?'+url);
		sortReplaceState(pathName +'?'+url); 
		initPageLoad = true;
		break;
	case 'low':
		if(pageType == 'productsearch'){
			var url = $('#searchPageDeptHierTreeForm').serialize();
			url = url+'&sort=price-asc';
		}
		//SDI-810
		else if(pageType == 'product'){
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			if(url.indexOf('&') > -1){
				url = url+'&sort=price-asc';
			} else {
				url = url+'sort=price-asc';
			}
		}
		else{
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			url = url+'&sort=price-asc';
		}
		ajaxPLPLoad(pathName +'?'+url);
		sortReplaceState(pathName +'?'+url); 
		initPageLoad = true;
		break;
	case 'high':
		if(pageType == 'productsearch'){
			var url = $('#searchPageDeptHierTreeForm').serialize();
			url = url+'&sort=price-desc';
		}
		//SDI-810
		else if(pageType == 'product'){
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			if(url.indexOf('&') > -1){
				url = url+'&sort=price-desc';
			} else {
				url = url+'sort=price-desc';
			}
		}
		else{
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			url = url+'&sort=price-desc';
		}
		ajaxPLPLoad(pathName +'?'+url);
		sortReplaceState(pathName +'?'+url); 
		initPageLoad = true;
		break;
	default:
		break;
	}
}