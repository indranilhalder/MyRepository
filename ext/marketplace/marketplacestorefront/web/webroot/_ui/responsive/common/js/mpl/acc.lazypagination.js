var pageNoPagination = 1;
var totalNoOfPages = 0;
var innerRecordSize = 7;
var loadMoreCount = 72;
var initPageLoad = true;
var directPaginatedLoad = true;
var serpURL;
var ajaxUrl = '';
var recordsLoadedCount = 0;
var pageType = $('#pageType').val();
var isSerp = false;

function innerLazyLoad(options) {
    //get the 8 items from the array and render // TODO: identify place holder done
    var gridHTML = '';
    //if total no of pages is 1 then load all 24 products at once.
    if(1 == totalNoOfPages){
    	$.each(productItemArray, function(index, element) {
                    gridHTML += '<li class="product-item">' + $(element).html() + '</li>';
        });
    }else{
    	 $.each(productItemArray, function(index, element) {
    	        if (index <= innerRecordSize) {
    	            if (index == innerRecordSize) {
    	                //productsLoaded+= index;
    	                gridHTML += '<li class="product-item lazy-reached">' + $(element).html() + '</li>';
    	                //$('ul.product-listing.product-grid').eq(1).append('<li class="product-item lazy-reached">' + $(element).html() + '</li>');
    	            } else {
    	                gridHTML += '<li class="product-item">' + $(element).html() + '</li>';
    	                //$('ul.product-listing.product-grid').eq(1).append('<li class="product-item">' + $(element).html() + '</li>');
    	            }
    	        }
    	    });
    }
   
    if (initPageLoad) { //TODO: duplicate loading prevention
        //$('ul.product-listing.product-grid').eq(2).html(gridHTML).hide().fadeIn(500);
        $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list').html(gridHTML).hide().fadeIn(500);
        initPageLoad = false;
    } else {
        //$('ul.product-listing.product-grid').eq(2).append(gridHTML);
        $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list').append(gridHTML);
    }
    deleteArraySet(productItemArray);
    if(typeof(options)!='undefined' && options.isSerp){
    	totalNoOfPages = $('input[name=noOfPages]').val();
        totalNoOfPages == '' ? 0 : parseInt(totalNoOfPages);
        isSerp = true;
    }
	/*if(pageNoPagination == totalNoOfPages){
		$('li').removeClass('lazy-reached');
	}*/
}

function innerLazyStorageLoad(productItemArrayStore) {
    var gridHTML = '';
    $.each(productItemArrayStore, function(index, element) {
                gridHTML += '<li class="product-item">' + $(element).html() + '</li>';
    });
    $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list').html(gridHTML).hide().fadeIn(500);
}

function deleteArraySet(productItemArray) {
	
    if (productItemArray.length != 0) {
        for (i = 0; i <= innerRecordSize; i++) {
        	productItemArray.shift();
        }
        //for serp
        if($('ul.product-listing.product-grid').length==0){
        	recordsLoadedCount = $('.product-list').find('li.product-item').length;
        }else{
        	recordsLoadedCount = $('.product-listing.product-grid.lazy-grid').find('li.product-item').length;
        }
    }
    console.log('Availabe blocks ' + productItemArray.length + ' Record count == ' + recordsLoadedCount);
}

function getProductSetData() {

    var pathName = window.location.pathname;
    var query = window.location.search;
    if (pageNoPagination <= totalNoOfPages) {

        //url with page no occourance found.
        if (/page-[0-9]+/.test(pathName)) {
            var currentPageNo = pathName.match(/page-[0-9]+/);

            currentPageNo = currentPageNo[0].split("-");
            currentPageNo = parseInt(currentPageNo[1]);
            if (directPaginatedLoad) {
                directPaginatedLoad = false;
            } else {
                currentPageNo++; 
            }
            if (currentPageNo <= totalNoOfPages) {
            	if(facetAjaxUrl){
            		ajaxUrl = facetAjaxUrl.replace(/page-[0-9]+/, 'page-' + currentPageNo);
            		window.history.replaceState({}, "", ajaxUrl);
            	}else{
            		ajaxUrl = pathName.replace(/page-[0-9]+/, 'page-' + currentPageNo);
            		var nextPaginatedAjaxUrl = pathName.replace(/page-[0-9]+/, 'page-' + currentPageNo);
                    if (query) {
                        ajaxUrl = ajaxUrl + query;
                        nextPaginatedAjaxUrl = nextPaginatedAjaxUrl + query;
                    }
                    window.history.replaceState({}, "", nextPaginatedAjaxUrl);
            	}
            }
        } else { // if no url with page no occourance found.
            if (pageNoPagination <= totalNoOfPages) {
                ajaxUrl = pathName.replace(/[/]$/,"") + '/page-' + pageNoPagination;
                if($('ul.product-listing.product-grid').length==0){//for serp initial page 
                	ajaxUrl = ajaxUrl + '?q='+findGetParameter('text')+':relevance:isLuxuryProduct:false';
            	}else if(query){
            		ajaxUrl = ajaxUrl + query;
            	}
                var nextPaginatedAjaxUrl = ajaxUrl;
                window.history.replaceState({}, "", nextPaginatedAjaxUrl);
                directPaginatedLoad =false;
            }
        }
        ajaxPLPLoad(ajaxUrl);
    }
}

$(document).ready(function() {
    //set the total no of pages 
    totalNoOfPages = $('input[name=noOfPages]').val();
    totalNoOfPages == '' ? 0 : parseInt(totalNoOfPages);
    var lazyfrompdp = window.localStorage.getItem("lazyfrompdp");
    
    if (window.localStorage &&  (html = window.localStorage.getItem('productlazyarray')) && html != '' && lazyfrompdp != '' && lazyfrompdp == 'true' && ($('#pageType').val() == 'productsearch' || $('#pageType').val() == 'category')) {
    	
    		$('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list').html(decodeURI(html));
        	
        	//added for load more
            if($('ul.product-listing.product-grid').length==0){
            	recordsLoadedCount = $('.product-list').find('li.product-item').length;
            }else{
            	recordsLoadedCount = $('.product-listing.product-grid.lazy-grid').find('li.product-item').length;
            }
            if (recordsLoadedCount!=0 && (recordsLoadedCount % loadMoreCount) == 0) {
                $('.loadMorePageButton').remove();
                $('ul.product-listing.product-grid.lazy-grid,ul.product-list').after('<button class="loadMorePageButton" style="background: #a9143c;color: #fff;margin: 5px auto;font-size: 12px;height: 40px;padding: 9px 18px;width: 250px;">Load More</button>');
            }
            //end added for load more
            window.localStorage.setItem('lazyfrompdp','false');
    }else{
    	getProductSetData();
    }
        $(window).on('scroll', function() {
            if ($('.lazy-reached').length != 0) {
                var hT = $('.lazy-reached').offset().top,
                    hH = $('.lazy-reached').outerHeight(),
                    wH = $(window).height(),
                    wS = $(this).scrollTop();
                if (wS > (hT + hH - wH)) {
				console.log("Lazy Reached");
                    $('.product-item').removeClass('lazy-reached');
					//$('li').removeClass('lazy-reached');
                    if ((recordsLoadedCount % loadMoreCount) == 0) {
                        $('.loadMorePageButton').remove();
                        $('ul.product-listing.product-grid.lazy-grid,ul.product-list').after('<button class="loadMorePageButton" style="background: #a9143c;color: #fff;margin: 5px auto;font-size: 12px;height: 40px;padding: 9px 18px;width: 250px;">Load More</button>');
                    } else {
                        if (productItemArray.length == 0) { //TODO: check if category page 
                            //window.history.replaceState({},"",ajaxUrl);
                            getProductSetData();
                        } else {
                            innerLazyLoad({
                                effect: true
                            });
                        }
                    }
                }
            }
        });
        // listner for load more
        $(document).on('click', '.loadMorePageButton', function() {
            getProductSetData();
            $('.loadMorePageButton').remove();
        });
   // }
        $(window).on("load resize",function(){
        if($("body").hasClass("page-productGrid")){
        $("body.page-productGrid .list_title h1").css("margin-left",$(".right-block").offset().left+parseInt($(".right-block").css("padding-left")));
        }
        });
        
        $(document).on('click','.sort',function(){
        	sort($(this),false);
        });
        
        $('.responsiveSort').change(function(){
        	sort($(this).find(':selected'),true);
        });
        
        $(document).on('click','.product-item',function(){
        	if(window.localStorage){
        		var productGridHtml = $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list').html();
        		window.localStorage.setItem("productlazyarray",encodeURI(productGridHtml));
        		window.localStorage.setItem('lazyfrompdp','true');
        	}
        });
        
        $(window).keypress(function( event ) {
        	  if ( event.which == 13 ) {
        		window.localStorage.removeItem('lazyfrompdp');
          		window.localStorage.removeItem('productlazyarray');
        	  }
        });
        $(document).on('click','.words,.departmenthover,.searchButton',function(){
        	window.localStorage.removeItem('lazyfrompdp');
      		window.localStorage.removeItem('productlazyarray');
        });
        $(document).on('click','.remove_filter',function(){
        	getProductSetData();
        });
       
});

function findGetParameter(parameterName) {
    var result = null,
        tmp = [];
    location.search
    .substr(1)
        .split("&")
        .forEach(function (item) {
        tmp = item.split("=");
        if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
    });
    return result;
}
function findGetParameterUrl(parameterName,URL) {
    var result = null,
        tmp = [];
    URL
    .substr(1)
        .split("&")
        .forEach(function (item) {
        tmp = item.split("=");
        if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
    });
    return result;
}
function removeURLParameter(url, parameter) {
    var urlparts= url.split('?');   
    if (urlparts.length>=2) {
        var prefix= encodeURIComponent(parameter)+'=';
        var pars= urlparts[1].split(/[&;]/g);
        for (var i= pars.length; i-- > 0;) {    
            if (pars[i].lastIndexOf(prefix, 0) !== -1) {  
                pars.splice(i, 1);
            }
        }
        url= urlparts[0] + (pars.length > 0 ? '?' + pars.join('&') : "");
        return url;
    } else {
        return url;
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
            $('ul.product-listing.product-grid.lazy-grid,ul.product-list').after('<p class="lazyLoadPagination" style="text-align: center;margin: 5px 0;font-size: 18px;">Loading...<img src="' + staticHost + '/_ui/responsive/common/images/spinner.gif" class="spinner" style="margin-left:5px;"></p>');
            
        },
        success: function(x) {
            var filtered = $.parseHTML(x);
            var ulProduct = null;
            //for serp
            if($('ul.product-listing.product-grid').length==0){
            	 ulProduct = $(filtered).find('ul.product-list');
            }else{
            	 ulProduct = $(filtered).find('ul.product-listing.product-grid.lazy-grid');
            }
            productItemArray = [];
            
            $(ulProduct).find('li.product-item').each(function() {
                productItemArray.push($(this))
            });
            $(".product-tile .image .item.quickview").each(function(){
            	if($(this).find(".addtocart-component").length == 1){
            		$(this).addClass("quick-bag-both");
            	}
            });	
        },
        complete: function() {
            $('.lazyLoadPagination').remove();
            innerLazyLoad();
            
        }
    });
}

function sortReplaceState(url){
		var nextPaginatedAjaxUrl = url.replace(/page-[0-9]+/, 'page-1');
		 window.history.replaceState({}, '', nextPaginatedAjaxUrl);
}

function sort(this_data,drop_down){
	console.log(typeof(this_data));
	var item = $(this_data).attr('data-name');
	$('.sort').removeAttr('style');
	if(!drop_down){
		$(this_data).css('color', 'red');
	}
	var pathName = window.location.pathname;
	pathName = pathName.replace(/page-[0-9]+/, 'page-1');
	
	var url = '';
	switch (item) {
	case 'relevance':
		if(pageType == 'productsearch'){
			url = 'q='+$('#js-site-search-input').val()+':relevance:isLuxuryProduct:false';
		}else{
			url = 'sort=relevance';
		}
		if(facetAjaxUrl){
			var extractedFacetQuery = findGetParameterUrl('q',facetAjaxUrl);
			var removedQParam = removeURLParameter(facetAjaxUrl,'q');
			var finalUrl = removedQParam+'&'+url+':'+extractedFacetQuery
			console.log(finalUrl);
			ajaxPLPLoad(finalUrl);
			sortReplaceState(finalUrl); 
		}else{
			ajaxPLPLoad(pathName +'?'+url);
			sortReplaceState(pathName +'?'+url); 
		}
		initPageLoad = true;
		break;
	case 'new':
		if(pageType == 'productsearch'){
			url = 'q='+$('#js-site-search-input').val()+':isProductNew:isLuxuryProduct:false';
		}else{
			url = 'sort=isProductNew';
		}
		if(facetAjaxUrl){
			var extractedFacetQuery = findGetParameterUrl('q',facetAjaxUrl);
			var removedQParam = removeURLParameter(facetAjaxUrl,'q');
			var finalUrl = removedQParam+'&'+url+':'+extractedFacetQuery
			console.log(finalUrl);
			ajaxPLPLoad(finalUrl);
			sortReplaceState(finalUrl); 
		}else{
			ajaxPLPLoad(pathName +'?'+url);
			sortReplaceState(pathName +'?'+url); 
		} 
		initPageLoad = true;
		break;
	case 'discount':
		if(pageType == 'productsearch'){
			url = 'q='+$('#js-site-search-input').val()+':isDiscountedPrice:isLuxuryProduct:false';
		}else{
			url = 'sort=isDiscountedPrice';
		}
		if(facetAjaxUrl){
			var extractedFacetQuery = findGetParameterUrl('q',facetAjaxUrl);
			var removedQParam = removeURLParameter(facetAjaxUrl,'q');
			var finalUrl = removedQParam+'&'+url+':'+extractedFacetQuery
			console.log(finalUrl);
			ajaxPLPLoad(finalUrl);
			sortReplaceState(finalUrl); 
		}else{
			ajaxPLPLoad(pathName +'?'+url);
			sortReplaceState(pathName +'?'+url); 
		}
		initPageLoad = true;
		break;
	case 'low':
		if(pageType == 'productsearch'){
			url = 'q='+$('#js-site-search-input').val()+':price-asc:isLuxuryProduct:false';
		}else{
			url = 'sort=price-asc';
		}
		if(facetAjaxUrl){
			var extractedFacetQuery = findGetParameterUrl('q',facetAjaxUrl);
			var removedQParam = removeURLParameter(facetAjaxUrl,'q');
			var finalUrl = removedQParam+'&'+url+':'+extractedFacetQuery
			console.log(finalUrl);
			ajaxPLPLoad(finalUrl);
			sortReplaceState(finalUrl);
		}else{
			ajaxPLPLoad(pathName +'?'+url);
			sortReplaceState(pathName +'?'+url); 
		}
		initPageLoad = true;
		break;
	case 'high':
		if(pageType == 'productsearch'){
			url = 'q='+$('#js-site-search-input').val()+':price-desc:isLuxuryProduct:false';
		}else{
			url = 'sort=price-desc';
		}
		if(facetAjaxUrl){
			var extractedFacetQuery = findGetParameterUrl('q',facetAjaxUrl);
			var removedQParam = removeURLParameter(facetAjaxUrl,'q');
			var finalUrl = removedQParam+'&'+url+':'+extractedFacetQuery
			console.log(finalUrl);
			ajaxPLPLoad(finalUrl);
			sortReplaceState(finalUrl); 
		}else{
			ajaxPLPLoad(pathName +'?'+url);
			sortReplaceState(pathName +'?'+url);
		}
		initPageLoad = true;
		break;
	default:
		break;
	}
}
