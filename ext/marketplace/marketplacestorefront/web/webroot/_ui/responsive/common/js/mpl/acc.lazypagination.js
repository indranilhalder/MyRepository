var pageNoPagination = 1;
var totalNoOfPages = 0;
var innerRecordSize = 7;
var loadMoreCount = 72;
var initPageLoad = true;
var directPaginatedLoad = true;
var serpURL;
var ajaxUrl = '';
var recordsLoadedCount = 0;

function innerLazyLoad(options) {
    //get the 8 items from the array and render // TODO: identify place holder done
    var gridHTML = '';
    $.each(productItemArray, function(index, element) {
        if (index <= innerRecordSize) {
            if (index == innerRecordSize && pageNoPagination != totalNoOfPages) {
                //productsLoaded+= index;
                gridHTML += '<li class="product-item lazy-reached">' + $(element).html() + '</li>';
                //$('ul.product-listing.product-grid').eq(1).append('<li class="product-item lazy-reached">' + $(element).html() + '</li>');
            } else {
                gridHTML += '<li class="product-item">' + $(element).html() + '</li>';
                //$('ul.product-listing.product-grid').eq(1).append('<li class="product-item">' + $(element).html() + '</li>');
            }
        }
    });
    if(initPageLoad && !/page-[0-9]+/.test(window.location.pathname)){//TODO: duplicate loading prevention
    	//$('ul.product-listing.product-grid').eq(2).html(gridHTML).hide().fadeIn(500);
		$('ul.product-listing.product-grid.lazy-grid').html(gridHTML);
    	initPageLoad = false;
    }else{
    	//$('ul.product-listing.product-grid').eq(2).append(gridHTML);
		$('ul.product-listing.product-grid.lazy-grid').append(gridHTML);
    }
    
    
    deleteArraySet(productItemArray);
}

function deleteArraySet(productItemArray) {
    if (productItemArray.length != 0) {
        for (i = 0; i <= innerRecordSize; i++) {
            productItemArray.shift();
			recordsLoadedCount++;
        }
    }
    console.log('Availabe blocks ' + productItemArray.length +' Record count == '+recordsLoadedCount);
}

function getProductSetData() {
	
	
	var pathName = window.location.pathname; 
	var query = window.location.search;
	if (pageNoPagination <= totalNoOfPages) {
		
		//url with page no occourance found.
    	if(/page-[0-9]+/.test(pathName)){
    		var currentPageNo = pathName.match(/page-[0-9]+/);
			
    		currentPageNo = currentPageNo[0].split("-");
			console.log(currentPageNo);
    		currentPageNo = parseInt(currentPageNo[1]);
    		if(directPaginatedLoad){
				directPaginatedLoad = false;
			}else{
				currentPageNo++; // TODO: replace the state 
			}
    		if(currentPageNo <= totalNoOfPages){
    			ajaxUrl = pathName.replace(/page-[0-9]+/,'page-'+currentPageNo);
				var nextPaginatedAjaxUrl = pathName.replace(/page-[0-9]+/,'page-'+currentPageNo);
    			if(query){
    				ajaxUrl = ajaxUrl + query;
					nextPaginatedAjaxUrl = nextPaginatedAjaxUrl + query;
    			}
    			window.history.replaceState({},"",nextPaginatedAjaxUrl);
    		}
    	}else{ // if no url with page no occourance found.
    		if (pageNoPagination <= totalNoOfPages) {
    			ajaxUrl = pathName + '/page-' + pageNoPagination;
    			var nextPaginatedAjaxUrl = pathName + '/page-' + pageNoPagination;
				pageNoPagination++; // TODO: replace the state 	
    			if (query) {
    				ajaxUrl = ajaxUrl + query;
					nextPaginatedAjaxUrl = nextPaginatedAjaxUrl + query;
                }
				window.history.replaceState({},"",nextPaginatedAjaxUrl);
    		}
    	}
		
		   $.ajax({
    	            url: ajaxUrl,
    	            data:{pageSize:24,q:''},
					beforeSend: function(){
						$('ul.product-listing.product-grid.lazy-grid').after('<p class="loadingLazyPagination" style="color:red;text-align:center;"><strong>Loading...</strong></p>');
					},
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
						$('.loadingLazyPagination').remove();
    	            }
    	        });
	}
    	
}
$(document).ready(function() {
//js hide will be changed after jstl changes
$('.pageSizeForm').remove();
$('.bottom-pagination').remove();
$('.paginationForm').remove();
    //set the total no of pages 
    totalNoOfPages = $('input[name=noOfPages]').val();
    totalNoOfPages == '' ? 0 : parseInt(totalNoOfPages);
	
    if($("#isCategoryPage").val() == 'true'){
		getProductSetData();
    $(window).on('scroll', function() {
        if ($('.lazy-reached').length != 0) {
            var hT = $('.lazy-reached').offset().top,
                hH = $('.lazy-reached').outerHeight(),
                wH = $(window).height(),
                wS = $(this).scrollTop();
            // console.log((hT - wH), wS);
            if (wS > (hT + hH - wH)) {
				$('.lazy-reached').attr('class', 'product-item');
				if((recordsLoadedCount % loadMoreCount)==0){
					$('.loadMorePageButton').remove();
					$('ul.product-listing.product-grid.lazy-grid').after('<a href="#nogo" class="loadMorePageButton"><div class="list_title"><h1>Load More</h1></div></a>');
				}else{
                if (productItemArray.length == 0 ) { //TODO: check if category page 
                	//window.history.replaceState({},"",ajaxUrl);
                	getProductSetData();
                }else{
					innerLazyLoad({
                    effect: true
                });
				}
				}
            }
        }
    });
	// listner for load more
	$(document).on('click','.loadMorePageButton',function(){
		getProductSetData();
		$('.loadMorePageButton').remove();
	});
	}
	
});
