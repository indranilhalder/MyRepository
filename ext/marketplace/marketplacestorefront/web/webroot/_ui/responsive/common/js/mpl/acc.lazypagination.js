var pageNoPagination = 1;
var totalNoOfPages = 0;
var innerRecordSize = 7;
var loadMoreCount = 71;
var initPageLoad = true;
var serpURL;
var ajaxUrl = '';

function innerLazyLoad(options) {
    //get the 8 items from the array and render // TODO: identify place holder done
    var gridHTML = '';
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
    if(initPageLoad){//TODO: duplicate loading prevention
    	$('ul.product-listing.product-grid.lazy-grid').html(gridHTML).hide().fadeIn(500);
    	initPageLoad = false;
    }else{
    	$('ul.product-listing.product-grid.lazy-grid').append(gridHTML).hide().fadeIn(500);
    }
    
    
    deleteArraySet(productItemArray);
}

function deleteArraySet(productItemArray) {
    if (productItemArray.length != 0) {
        for (i = 0; i <= innerRecordSize; i++) {
            productItemArray.shift();
        }
    }
    console.log('Availabe blocks ' + productItemArray.length);
}

function getProductSetData() {
	
	
	var pathName = window.location.pathname; 
	var query = window.location.search;
	
    	//url with page no occourance found.
    	if(/page-[0-9]/.test(pathName)){
    		var currentPageNo = pathName.match(/page-[0-9]/);
    		currentPageNo = currentPageNo[0].split("-");
    		currentPageNo = currentPageNo[1];
    		
    		if(currentPageNo <= totalNoOfPages){
    			ajaxUrl = pathName.replace(/page-[0-9]/,'page-'+currentPageNo);
    			if(query){
    				ajaxUrl = ajaxUrl + query;
    			}
    			currentPageNo++;
    		}
    	}else{ // if no url with page no occourance found.
    		
    		if (pageNoPagination <= totalNoOfPages) {
    			ajaxUrl = pathName + '/page-' + pageNoPagination;
    			pageNoPagination++;
    			if (query) {
    				ajaxUrl = ajaxUrl + query;
                }
    		}
    	}
    		   $.ajax({
    	            url: ajaxUrl,
    	            data:{pageSize:24,q:''},
    	            success: function(x) {
    	                var filtered = $.parseHTML(x);
    	                var ulProduct = $(filtered).find('ul.product-listing.product-grid');
    	                productItemArray = [];
    	                $(ulProduct).find('li.product-item').each(function() {
    	                    productItemArray.push($(this))
    	                });
    	            },
    	            complete: function() {
    	                //$('#loadMorePagination').val('LOAD MORE');
    	                innerLazyLoad();
    	            }
    	        });
	
/*	pageNoPagination++;
    if (pageNoPagination <= totalNoOfPages) {
        var urlBrowser = '';
        var query = window.location.search;
        var protocol = window.location.protocol;
        var hostname = window.location.hostname;

        //TODO: need to implement URL page matching 
        if(serpURL){
        	urlBrowser = serpURL;
        	
        	if(/page-[0-9]/.test(serpURL)){
        		var currentPageNo = serpURL.match(/page-[0-9]/);
        		currentPageNo = currentPageNo.split("-");
        		currentPageNo = currentPageNo[1];
        	}
        }else{
        	if (/page/.test(window.location.pathname)) {
                urlBrowser = window.location.pathname + '/page-' + pageNoPagination;
            } else {
                urlBrowser = window.location.pathname + '/page-' + pageNoPagination;
            }
            if (query) {
                urlBrowser + '?' + query;
            }
        }
        
        //urlBrowser = protocol+hostname+urlBrowser;

        $.ajax({
            url: urlBrowser,
            success: function(x) {
                var filtered = $.parseHTML(x);
                var ulProduct = $(filtered).find('ul.product-listing.product-grid');
                productItemArray = [];
                $(ulProduct).find('li.product-item').each(function() {
                    productItemArray.push($(this))
                });
            },
            complete: function() {
                //$('#loadMorePagination').val('LOAD MORE');
                innerLazyLoad();
            }
        });
    }*/

}
$(document).ready(function() {
    //set the total no of pages 
    totalNoOfPages = $('input[name=noOfPages]').val();
    totalNoOfPages == '' ? 0 : parseInt(totalNoOfPages);
    getProductSetData();
    $(window).on('scroll', function() {
        if ($('.lazy-reached').length != 0) {
            var hT = $('.lazy-reached').offset().top,
                hH = $('.lazy-reached').outerHeight(),
                wH = $(window).height(),
                wS = $(this).scrollTop();
            // console.log((hT - wH), wS);
            if (wS > (hT + hH - wH)) {
                if (productItemArray.length === 0) { //TODO: check if category page 
                	//window.history.replaceState({},"",ajaxUrl);
                	getProductSetData();
                }
                $('.lazy-reached').attr('class', 'product-item');
                innerLazyLoad({
                    effect: true
                });
            }
        }

    });
});