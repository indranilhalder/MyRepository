/**
 * All home page XHR that are lazy loaded are written here. mplminjs.tag is the file where the scroll event for binding is added UF-439
 */

// AJAX CALL BEST PICKS START

// 1st 
function getBestPicksAjaxCall() {
    var env = $("#previewVersion").val();
    if (env == "true") {
        var dataString = 'version=Staged';
    } else {
        var dataString = 'version=Online';
    }
    var autoplayTimeout = 5000;
    var slideBy = 1;
    var autoPlay = true;
    $.ajax({
        type: "GET",
        dataType: "json",
        url: ACC.config.encodedContextPath + "/getBestPicks",
        data: dataString,
        success: function(response) {

            //changes for TPR-1121
            autoplayTimeout = response.autoplayTimeout ? response.autoplayTimeout : autoplayTimeout;
            slideBy = response.slideBy ? response.slideBy : slideBy;
            autoPlay = response.autoPlay != null ? response.autoPlay : autoPlay;

            //TPR-559 Show/Hide Components and Sub-components
            if (response.hasOwnProperty("title") && response.hasOwnProperty("subItems")) {
                renderHtml = "<h2>" + response.title + "</h2>" +
                    "<div class='home-best-pick-carousel'>";
                $.each(response.subItems, function(k, v) {
                    if (v.url) {
                        renderHtml += "<a href='" +
                            appendIcid(v.url, v.icid) +
                            "' class='item'>";
                    }
                    if (v.imageUrl) {
                        renderHtml +=
                            "<div class='home-best-pick-carousel-img'> <img class='' src='" +
                            v.imageUrl + "'></img></div>";
                    }
                    if (v.text) {
                        renderHtml +=
                            "<div class='short-info'>" + v.text +
                            "</div>";
                    }
                    renderHtml += "</a>";
                });

                renderHtml +=
                    // "</div> <a href='/store/view-all-offers' class='view-cliq-offers'> View Cliq Offers </a>";
                    "</div> <a href='";
                if (typeof response.buttonLink !== "undefined") {
                    renderHtml += response.buttonLink + "'";
                } else {
                    renderHtml += ACC.config.encodedContextPath + "/offersPage'";
                }

                renderHtml += "class='view-cliq-offers'>";
                if (typeof response.buttonText !== "undefined") {
                    renderHtml += response.buttonText;
                } else {
                    renderHtml += " View Cliq Offers ";
                }
                renderHtml += "</a>";
                // "</div> <a href='/store/view-all-offers' class='view-cliq-offers'> View Cliq Offers </a>";
                //"</div> <a href='"+ACC.config.encodedContextPath+"/offersPage' class='view-cliq-offers'> View Cliq Offers </a>";
                $("#bestPicks").html(renderHtml);
                // console.log()
            }

        },
        error: function() {
            console.log("Error while getting best picks");
        },
        complete: function() {

            //TPR-559 Show/Hide Components and Sub-components
            if ($(".home-best-pick-carousel").length) {
                $(".home-best-pick-carousel").owlCarousel({
                    items: 5,
                    loop: true,
                    nav: true,
                    dots: false,
                    navText: [],
                    lazyLoad: false,
                    autoplay: autoPlay,
                    autoHeight: false,
                    autoplayTimeout: autoplayTimeout,
                    slideBy: slideBy,
                    responsive: {
                        // breakpoint from 0 up
                        0: {
                            items: 1,
                            stagePadding: 50,
                        },
                        // breakpoint from 480 up
                        480: {
                            items: 2,
                            stagePadding: 50,
                        },
                        // breakpoint from 768 up
                        768: {
                            items: 3,
                        },
                        // breakpoint from 768 up
                        1080: {
                            items: 5,
                        }
                    }
                    /*navigation: true,
                    navigationText: [],
                    pagination: false,
                    itemsDesktop: [5000, 5],
                    itemsDesktopSmall: [1400, 5],
                    itemsTablet: [650, 1],
                    itemsMobile: [480, 1],
                    rewindNav: false,
                    lazyLoad: true,
                    scrollPerPage: true*/
                });
            }

        }
    });

}

//2nd call
function getBrandsYouLoveAjaxCall() {
    var env = $("#previewVersion").val();
    var count = 0;
    var autoplayTimeout = 5000;
    var slideBy = 1;
    var autoPlay = true;

    if (env == "true") {
        var dataString = 'version=Staged';
    } else {
        var dataString = 'version=Online';
    }
    $.ajax({
        type: "GET",
        dataType: "json",
        url: ACC.config.encodedContextPath + "/getBrandsYouLove",
        data: dataString,
        success: function(response) {
            //changes for TPR-1121
            autoplayTimeout = response.autoplayTimeout ? response.autoplayTimeout : autoplayTimeout;
            slideBy = response.slideBy ? response.slideBy : slideBy;
            autoPlay = response.autoPlay != null ? response.autoPlay : autoPlay;

            //TPR-559 Show/Hide Components and Sub-components
            if (response.hasOwnProperty("title") && response.hasOwnProperty("subComponents") && response.subComponents.length) {
                //console.log(response.subComponents);
                defaultComponentId = "";
                renderHtml = "<h2>" + response.title + "</h2>" +
                    "<div class='home-brands-you-love-carousel'>";
                $.each(response.subComponents, function(k, v) {
                    //console.log(v.brandLogoUrl);

                    if (!v.showByDefault) {
                        renderHtml +=
                            "<div class='home-brands-you-love-carousel-brands item' data-count =" + count + " id='" +
                            v.compId + "'><img src='" + v.brandLogoUrl +
                            "' alt='" + v.brandLogoAltText + "'></img></div>";

                    } else {
                        renderHtml +=
                            "<div class='home-brands-you-love-carousel-brands item' data-count =" + count + " id='" +
                            v.compId + "'><img src='" + v.brandLogoUrl +
                            "' alt='" + v.brandLogoAltText + "'></img></div>";

                        defaultComponentId = v.compId;
                    }
                    count++;
                });
                renderHtml += "</div>";
                $('#brandsYouLove').html(renderHtml);
                getBrandsYouLoveContentAjaxCall(defaultComponentId);
            }
        },
        error: function() {
            // globalErrorPopup('Failure!!!');
            console.log("Error while getting brands you love");
        },
        complete: function() {

            //TPR-559 Show/Hide Components and Sub-components
            if ($(".home-brands-you-love-carousel").length) {
                $(".home-brands-you-love-carousel").owlCarousel({
                    items: 7,
                    loop: true,
                    nav: true,
                    center: true,
                    dots: false,
                    navText: [],
                    autoplay: autoPlay,
                    autoHeight: false,
                    autoplayTimeout: autoplayTimeout,
                    slideBy: slideBy,
                    responsive: {
                        // breakpoint from 0 up
                        0: {
                            items: 1,
                            stagePadding: 75,
                        },
                        // breakpoint from 480 up
                        480: {
                            items: 3,
                            stagePadding: 50,
                        },
                        // breakpoint from 768 up
                        768: {
                            items: 3,
                            stagePadding: 90,
                        },
                        // breakpoint from 768 up
                        1080: {
                            items: 7,
                        }
                    }
                });
                var bulId = $(".home-brands-you-love-carousel .owl-item.active.center").find(".home-brands-you-love-carousel-brands").attr("id");
                getBrandsYouLoveContentAjaxCall(bulId);
                /*	$(document).on('click', '.home-brands-you-love-carousel .owl-item.active', function () {
	                		
	                		$(".home-brands-you-love-carousel").trigger('to.owl.carousel', [$(this).find(".home-brands-you-love-carousel-brands").attr("data-count"), 500, true]);
	
	                	});*/
                $(".home-brands-you-love-carousel").on('changed.owl.carousel', function(event) {
                    setTimeout(function() {
                        //	console.log($(".home-brands-you-love-carousel .owl-item.active.center").index());
                        var bulId = $(".home-brands-you-love-carousel .owl-item.active.center").find(".home-brands-you-love-carousel-brands").attr("id");
                        getBrandsYouLoveContentAjaxCall(bulId);
                    }, 80);

                });

            }

        }
    });

}

//3rd call
//TPR-1672
function getBestOffersAjaxCall() {
  var env = $("#previewVersion").val();
  if (env == "true") {
      var dataString = 'version=Staged';
  } else {
      var dataString = 'version=Online';
  }
  var autoplayTimeout = 5000;
  var slideBy = 1;
  var autoPlay = true;
  $.ajax({
      type: "GET",
      dataType: "json",
      url: ACC.config.encodedContextPath + "/getBestOffers",
      data: dataString,
      success: function(response) {

          //changes for TPR-1121
          autoplayTimeout = response.autoplayTimeout ? response.autoplayTimeout : autoplayTimeout;
          slideBy = response.slideBy ? response.slideBy : slideBy;
          autoPlay = response.autoPlay != null ? response.autoPlay : autoPlay;
          //TPR-559 Show/Hide Components and Sub-components
          if (response.hasOwnProperty("title") && response.hasOwnProperty("subItems")) {
              renderHtml = "<h2>" + response.title + "</h2>" +
                  "<div class='home-best-offers-carousel'>";
              $.each(response.subItems, function(k, v) {
                  if (v.url) {
                      renderHtml += "<a href='" +
                          appendIcid(v.url, v.icid) +
                          "' class='item'>";
                  }
                  if (v.imageUrl) {
                      renderHtml +=
                          "<div class='home-best-Offers-carousel-img'> <img class='' src='" +
                          v.imageUrl + "'></img></div>";
                  }
                  if (v.text) {
                      renderHtml +=
                          "<div class='short-info-bestOffers'>" + v.text +
                          "</div>";
                  }
                  renderHtml += "</a>";
              });

              renderHtml +=
                  // "</div> <a href='/store/view-all-offers' class='view-cliq-offers'> View Cliq Offers </a>";
                  "</div> <a href='";
              if (typeof response.buttonLink !== "undefined") {
                  renderHtml += response.buttonLink + "'";
              }
              //            else{
              //            	renderHtml +=ACC.config.encodedContextPath+"/offersPage'";
              //            }

              renderHtml += "class='view-best-offers'>";
              if (typeof response.buttonText !== "undefined") {
                  renderHtml += response.buttonText;
              }
              //            else{
              //            	 renderHtml +=" View Best Offers ";
              //            }
              renderHtml += "</a>";
              // "</div> <a href='/store/view-all-offers' class='view-cliq-offers'> View Cliq Offers </a>";
              //"</div> <a href='"+ACC.config.encodedContextPath+"/offersPage' class='view-cliq-offers'> View Cliq Offers </a>";
              $("#bestOffers").html(renderHtml);
              // console.log()
          }
      },
      error: function() {
          console.log("Error while getting best picks");
      },
      complete: function() {
          //TPR-559 Show/Hide Components and Sub-components
          if ($(".home-best-offers-carousel").length) {
              $(".home-best-offers-carousel").owlCarousel({
                  items: 5,
                  loop: true,
                  nav: true,
                  dots: false,
                  navText: [],
                  lazyLoad: false,
                  autoplay: autoPlay,
                  autoHeight: false,
                  autoplayTimeout: autoplayTimeout,
                  slideBy: slideBy,
                  responsive: {
                      // breakpoint from 0 up
                      0: {
                          items: 1,
                          stagePadding: 50,
                      },
                      // breakpoint from 480 up
                      480: {
                          items: 2,
                          stagePadding: 50,
                      },
                      // breakpoint from 768 up
                      768: {
                          items: 3,
                      },
                      // breakpoint from 768 up
                      1280: {
                          items: 5,
                      }
                  }
                  /*navigation: true,
                  navigationText: [],
                  pagination: false,
                  itemsDesktop: [5000, 5],
                  itemsDesktopSmall: [1400, 5],
                  itemsTablet: [650, 1],
                  itemsMobile: [480, 1],
                  rewindNav: false,
                  lazyLoad: true,
                  scrollPerPage: true*/
              });
          }
      }
  });

}
//4th call 
/* Promotional Banner Section starts */
function getPromoBannerHomepage() {
    var env = $("#previewVersion").val();
    if (env == "true") {
        var dataString = 'version=Staged';
    } else {
        var dataString = 'version=Online';
    }
    $.ajax({
        type: "GET",
        dataType: "json",
        url: ACC.config.encodedContextPath +
            "/getPromoBannerHomepage",
        data: dataString,
        success: function(response) {
        	//TPR-559 Show/Hide Components and Sub-components
        	if (response.hasOwnProperty("allBannerJsonObject") && response.allBannerJsonObject.length) {
	        	var arr= new Array();
	        	$.each( response, function(key, obj){
	                    arr.push(key,obj);
	            });
	        	var finalArr = arr[1];
	        	var count = finalArr.length;
	            if(window.sessionStorage && (seqPromo = window.sessionStorage.getItem("PromoBannerHomepage"))) {
	                seqPromo = parseInt(seqPromo);
	            	if (seqPromo == '' || seqPromo >= count || seqPromo < 1) {
	            		seqPromo = 1;
	            	} else {
	            		seqPromo=seqPromo+1;
	            	}
	        		showPromoBanner(finalArr[seqPromo-1]);
	        		window.sessionStorage.setItem("PromoBannerHomepage", seqPromo);
	        	} else {
	        		showPromoBanner(finalArr[0]);
	        		if(window.sessionStorage) {
	        			window.sessionStorage.setItem("PromoBannerHomepage", 1);
	        		}
	        	}
        	}   
        },
        error: function() {
            console.log('Failure in Promo!!!');
        }
    });
}

function showPromoBanner(response){
	 //console.log(response.bannerImage);
  var defaultHtml = "";
  var bannerUrlLink = response.bannerUrlLink;
  var bannerImage = response.bannerImage;
  var bannerAltText = response.bannerAltText;
  var promoText1 = response.promoText1;
  var promoText2 = response.promoText2;
  var promoText3 = response.promoText3;
  var promoText4 = response.promoText4;
  //renderHtml = '<img src="' + response.bannerImage +'"/>';
  renderHtml = promoText1;
  $('#promobannerhomepage').html(renderHtml);
}


//AJAX CALL PRODUCTS YOU CARE START
//5th call 
function getProductsYouCareAjaxCall() {
	
    var env = $("#previewVersion").val();
    if (env == "true") {
        var dataString = 'version=Staged';
    } else {
        var dataString = 'version=Online';
    }

    var slideBy = 1;
    var autoplayTimeout = 5000;
    var autoPlay = true;
    $.ajax({
        type: "GET",
        dataType: "json",
        url: ACC.config.encodedContextPath + "/getProductsYouCare",
        data: dataString,
        success: function(response) {

            //changes for TPR-1121
            autoplayTimeout = response.autoplayTimeout ? response.autoplayTimeout : autoplayTimeout;
            slideBy = response.slideBy ? response.slideBy : slideBy;
            autoPlay = response.autoPlay != null ? response.autoPlay : autoPlay;

            //TPR-559 Show/Hide Components and Sub-components
            if (response.hasOwnProperty("title") && response.hasOwnProperty("categories") && response.title && response.categories.length) {
                renderHtml = "<h2>" + response.title + "</h2>";
                renderHtml +=
                    "<div class='home-product-you-care-carousel'>";
                $.each(response.categories, function(k, v) {
                    var URL = ACC.config.encodedContextPath +
                        /*"/Categories/" + v.categoryName*/
                        v.categoryPath + //TISPRD_2315
                        "/c-" + v.categoryCode.toLowerCase();

                    //for url
                    if (!v.imageURL) {
                        renderHtml += "<a href='" + appendIcid(
                                URL, v.icid) +
                            "' class='item'>";
                    } else {
                        renderHtml += "<a href='" + v.imageURL +
                            "' class='item'>";
                    }
                    //for image
                    renderHtml +=
                        "<div class='home-product-you-care-carousel-img'> <img class='' src='" +
                        v.mediaURL + "'></img></div>";
                    /*TPR-562 -start   */
                    if (v.imageName) {
                        renderHtml +=
                            "<div class='short-info'><h3 class='product-name'><span>" +
                            v.imageName +
                            "</span></h3></div>";
                        renderHtml += "</a>";

                    } else {
                        renderHtml +=
                            "<div class='short-info'><h3 class='product-name'><span>" +
                            v.categoryName +
                            "</span></h3></div>";
                        renderHtml += "</a>";
                    }
                    /*TPR-562 -ends   */
                });
                renderHtml += "</div>";
                $("#productYouCare").html(renderHtml);
            }

        },
        error: function() {
            console.log(
                'Error while getting getProductsYouCare');
        },

        complete: function() {
            //TPR-559 Show/Hide Components and Sub-components
            if ($(".home-product-you-care-carousel").length) {
                $(".home-product-you-care-carousel").owlCarousel({
                    items: 4,
                    loop: true,
                    nav: true,
                    dots: false,
                    navText: [],
                    lazyLoad: false,
                    autoplay: autoPlay,
                    autoHeight: false,
                    autoplayTimeout: autoplayTimeout,
                    slideBy: slideBy,
                    responsive: {
                        // breakpoint from 0 up
                        0: {
                            items: 1,
                            stagePadding: 50,
                        },
                        480: {
                            items: 2,
                            stagePadding: 50,
                        },
                        // breakpoint from 768 up
                        768: {
                            items: 3,
                        },
                        // breakpoint from 768 up
                        1080: {
                            items: 4,
                        }

                    }
                    /*
                     * navigation: true, navigationText: [], pagination: false,
                     * itemsDesktop: [5000, 4], itemsDesktopSmall: [1400, 4],
                     * itemsTablet: [650, 2], itemsMobile: [480, 2], rewindNav:
                     * false, lazyLoad: true, scrollPerPage: true
                     */
                });
            }
        }
    });
}
// AJAX CALL PRODUCTS YOU CARE END

//6th call
/* StayQued Section starts */
function getStayQuedHomepage() {
    var env = $("#previewVersion").val();
    if (env == "true") {
        var dataString = 'version=Staged';
    } else {
        var dataString = 'version=Online';
    }
    $.ajax({
        type: "GET",
        dataType: "json",
        url: ACC.config.encodedContextPath + "/getStayQuedHomepage",
        data: dataString,
        success: function(response) {
            //TPR-559 Show/Hide Components and Sub-components
            if (response.hasOwnProperty("allBannerJsonObject") && response.allBannerJsonObject.length) {
                var arr = new Array();
                $.each(response, function(key, obj) {
                    arr.push(key, obj);
                });
                var finalArr = arr[1];
                var count = finalArr.length;
                if (window.sessionStorage && (seqStay = window.sessionStorage.getItem("StayQuedHomepage"))) {
                    seqStay = parseInt(seqStay);
                    if (seqStay == '' || seqStay >= count || seqStay < 1) {
                        seqStay = 1;
                    } else {
                        seqStay = seqStay + 1;
                    }
                    showStayQued(finalArr[seqStay - 1]);
                    window.sessionStorage.setItem("StayQuedHomepage", seqStay);
                } else {
                    showStayQued(finalArr[0]);
                    if (window.sessionStorage) {
                        window.sessionStorage.setItem("StayQuedHomepage", 1);
                    }
                }
            }
        },
        error: function() {
            console.log('Failure in StayQued!!!');
        }
    });
}

function showStayQued(response) {
    //alert(response);
    var defaultHtml = "";
    var linkText = "";
    var bannerUrlLink = response.bannerUrlLink;
    var bannerImage = response.bannerImage;
    var bannerAltText = response.bannerAltText;
    var promoText1 = response.promoText1;
    var promoText2 = response.promoText2;
    var promoText3 = response.promoText3;
    var promoText4 = response.promoText4;
    if ($(promoText2).is('p')) {
        linkText = $(promoText2).text();
    } else {
        linkText = promoText2;
    }
    renderHtml =
        '<h2><span class="h1-qued">Stay Qued</span></h2><div class="qued-padding"><div class="qued-content">' + /* UF-249 */
        promoText1 + '<a href="' + bannerUrlLink +
        '" class="button maroon">' + linkText +
        '</a></div><div class="qued-image"><img class="lazy" src="' +
        bannerImage + '" class="img-responsive"></div></div>';
    $('#stayQued').html(renderHtml);
}
//7th call 
function getNewAndExclusiveAjaxCall() {
    var env = $("#previewVersion").val();
    if (env == "true") {
        var dataString = 'version=Staged';
    } else {
        var dataString = 'version=Online';
    }
    var slideBy = 1;
    var autoplayTimeout = 5000;
    var autoPlay = true;

    $.ajax({
        type: "GET",
        dataType: "json",
        url: ACC.config.encodedContextPath + "/getNewAndExclusive",
        data: dataString,
        success: function(response) {

            //changes for TPR-1121
            autoplayTimeout = response.autoplayTimeout ? response.autoplayTimeout : autoplayTimeout;
            slideBy = response.slideBy ? response.slideBy : slideBy;
            autoPlay = response.autoPlay != null ? response.autoPlay : autoPlay;

            //TPR-559 Show/Hide Components and Sub-components
            if (response.hasOwnProperty("title") && response.hasOwnProperty("newAndExclusiveProducts") && response.newAndExclusiveProducts.length) {
                var staticHost = $('#staticHost').val();
                var defaultHtml = "";
                renderHtml = "<h2>" + response.title + "</h2>" +
                    "<div class='js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference' id='new_exclusive'>";
                $.each(response.newAndExclusiveProducts, function(
                    key, value) {
                    if (value.isNew == 'Y') {
                        renderNewHtml = "<div style='z-index: 1;' class='new'><img class='brush-strokes-sprite sprite-New' src='" + staticHost + "/_ui/responsive/common/images/transparent.png'><span>New</span></div>";
                    } else {
                        renderNewHtml = '';
                    }
                    renderHtml +=
                        "<div class='item slide'><div class='newExclusiveElement'><a href='" +
                        ACC.config.encodedContextPath +
                        value.productUrl + "'>" + renderNewHtml + "<img class='' src='" +
                        value.productImageUrl +
                        "'></img><p class='New_Exclusive_title'>" +
                        value.productTitle +
                        "</p><p class='New_Exclusive_price'>";
			//UF-319
	                if (value.productPrice.strikePrice != "" && value.productPrice.dispPrice != value.productPrice.strikePrice) {
	                	renderHtml += "<span class='priceFormat' style='color: gray;text-decoration: line-through;padding-right: 5px;'>" +
	                    value.productPrice.strikePrice +
	                    "</span>";
	                }
	                renderHtml += "<span class='priceFormat'>" +
                    value.productPrice.dispPrice +
                    "</span></p></a></div></div>";
                });
                renderHtml += "</div><a href='" + ACC.config.encodedContextPath +
                    "/search/viewOnlineProducts' class='new_exclusive_viewAll'>View All</a>";
                $('#newAndExclusive').html(renderHtml);
            }

        },
        error: function() {
            console.log("Error while getting new and exclusive");
        },
        complete: function() {

            //TPR-559 Show/Hide Components and Sub-components
            if ($("#new_exclusive").length) {
                $("#new_exclusive").owlCarousel({
                    items: 3,
                    loop: true,
                    nav: true,
                    dots: false,
                    navText: [],
                    lazyLoad: false,
                    autoplay: autoPlay,
                    autoHeight: false,
                    autoplayTimeout: autoplayTimeout,
                    slideBy: slideBy,
                    responsive: {
                        // breakpoint from 0 up
                        0: {
                            items: 1,
                            stagePadding: 50,
                        },
                        480: {
                            items: 2,
                            stagePadding: 50,
                        },
                        // breakpoint from 768 up
                        768: {
                            items: 3,
                        }
                    }
                    /*navigation: true,
                    rewindNav: false,
                    navigationText: [],
                    pagination: false,
                    items: 2,
                    itemsDesktop: false,
                    itemsDesktopSmall: false,
                    itemsTablet: false,
                    itemsMobile: false,
                    scrollPerPage: true,
                    lazyLoad: true*/
                });
                setTimeout(function() {
                    /*if($(window).width() > 773) {
						$('#newAndExclusive').css('min-height',$('#newAndExclusive').parent().height()+'px');
					}*/
                    //alert($('#newAndExclusive').height() +"|||"+$('#stayQued').height())
                    if ($('#stayQued').children().length == 0) {
                        $('#stayQued').css('min-height', 'auto');
                    } else {
                        $('#stayQued').css('min-height',
                            $('#newAndExclusive').outerHeight() +
                            'px');
                    }
                }, 2500);
                $("#new_exclusive").on('changed.owl.carousel', function(event) {
                    setTimeout(function() {
                        var arrHtOwl = [],
                            diffHtOwl = 0;
                        var len = $("#newAndExclusive .owl-item.active").length;
                        for (var j = 0; j < len; j++) {
                            arrHtOwl.push($("#newAndExclusive .owl-item.active").eq(j).find(".New_Exclusive_title").height());
                        }
                        if ($(window).width() > 790) {
                            arrHtOwl.splice(-1, 1);
                        }
                        var max2 = Math.max.apply(Math, arrHtOwl);
                        for (var k = 0; k < arrHtOwl.length; k++) {
                            diffHtOwl = max2 - arrHtOwl[k];
                            $("#newAndExclusive .owl-item.active").eq(k).find(".New_Exclusive_title").css("padding-bottom", +diffHtOwl);
                        }
                    }, 80);
                });
            }

        }
    });
}

function showMobileShowCase() {
    var env = $("#previewVersion").val();
    if (env == "true") {
        var dataString = 'version=Staged';
    } else {
        var dataString = 'version=Online';
    }
    if (window.localStorage && (html = window.localStorage.getItem("showcaseContentMobile")) && html != "") {
        $('#showcaseMobile').html(decodeURI(html));
        //UF-420 starts
        $(".showcase-carousel").owlCarousel({
            items: 1,
            loop: $(".showcase-carousel img").length == 1 ? false : true,
            navText: [],
            nav: true,
            dots: false
        });
        //UF-420 ends
    } else {
        $.ajax({
            type: "GET",
            dataType: "json",
            url: ACC.config.encodedContextPath +
                "/getCollectionShowcase",
            data: dataString,
            success: function(response) {
                try {
                    var showCaseMobile = '<h2>' + response.title + '</h2>';
                    showCaseMobile += '<div class="owl-carousel showcase-carousel">';
                    $.each(response.subComponents, function(k, v) {
                        //UF-420 starts
                        //showCaseMobile+= getShowcaseMobileContentAjaxCall(v.compId);
                        showCaseMobile += '<div class="item showcase-section">';
                        showCaseMobile += '<div class="desc-section">' + v.details.text;
                        showCaseMobile += '<img class="lazy" src="' + v.details.bannerImageUrl + '">'
                        showCaseMobile += '</div>'
                        showCaseMobile += '</div>';
                        //UF-420 ends
                    });
                    showCaseMobile += '</div>';
                    $('#showcaseMobile').html(showCaseMobile);
                    window.localStorage.setItem("showcaseContentMobile", encodeURI(showCaseMobile));
                } catch (e) {
                    console.log(e);
                }
            },
            complete: function() {
                $(".showcase-carousel").owlCarousel({
                    items: 1,
                    loop: $(".showcase-carousel img").length == 1 ? false : true,
                    navText: [],
                    nav: true,
                    dots: false
                });
            },
            error: function() {
                console.log("Error while getting showcase");
            }
        });
    }
}

//8 th call
function getShowcaseMobileContentAjaxCall(id) {
    var showCaseMobile = '';
    $.ajax({
        type: "GET",
        dataType: "json",
        async: false,
        beforeSend: function() {
            var staticHost = $('#staticHost').val();
            /*$(".showcase-switch").css("margin-bottom",
                "80px");
            $("#showcase").append(
                "<div class='loaderDiv' style='z-index: 100000;position: absolute; top: 150px;left: 50%;margin-left: -50px;'><img src='"+staticHost+"/_ui/responsive/common/images/red_loader.gif'/></div>"
            );*/
        },
        url: ACC.config.encodedContextPath +
            "/getShowcaseContent",
        data: {
            "id": id
        },
        success: function(response) {

            showCaseMobile += '<div class="item showcase-section">';
            showCaseMobile += '<div class="desc-section">' + response.text;
            showCaseMobile += '<img class="lazy" src="' + response.bannerImageUrl + '">'
            showCaseMobile += '</div>'
            showCaseMobile += '</div>';
        },
        error: function() {
            console.log(
                "Error while getting showcase content");
            //$('#showcase .loaderDiv').remove();
            //$(".showcase-switch").css("margin-bottom",
            // "0px");
        }
    });
    return showCaseMobile;
}

function getShowcaseContentAjaxCall(id) {
    if (window.localStorage && (html = window.localStorage.getItem(
            "showcaseContent-" + id)) && html != "") {
        // console.log("Local");
        $('.about-one showcase-section').remove();
        $('#showcase').append(decodeURI(html));
        //LazyLoad();
    } else {
        $.ajax({
            type: "GET",
            dataType: "json",
            beforeSend: function() {
                var staticHost = $('#staticHost').val();
                /*$(".showcase-switch").css("margin-bottom",
                    "80px");
                $("#showcase").append(
                    "<div class='loaderDiv' style='background: transparent;z-index: 100000;position: absolute; top: 150px;left: 50%;margin-left: -50px;display:inline-block;width:100px;height:100px;'><img src='"+staticHost+"/_ui/desktop/theme-blue/images/loading.gif' style='width:100%;'/></div>"
                );*/
            },
            url: ACC.config.encodedContextPath +
                "/getShowcaseContent",
            data: {
                "id": id
            },
            success: function(response) {

                $('.about-one.showcase-section').remove();
                defaultHtml =
                    "<div class='about-one showcase-section'>";
                if (typeof response.bannerImageUrl !==
                    "undefined") {
                    defaultHtml += "<div class='desc-section'>";
                    if (typeof response.bannerUrl !==
                        "undefined") {
                        defaultHtml += "<a href='" + appendIcid(response.bannerUrl, response.icid) + "'>";
                    }
                    defaultHtml += "<img class='lazy' src='" + response.bannerImageUrl +
                        "'></img>";
                    if (typeof response.bannerUrl !==
                        "undefined") {
                        defaultHtml += "</a>";
                    }
                    defaultHtml += "</div>";
                }
                if (typeof response.text !== "undefined") {
                    defaultHtml += "<div class='desc-section'>" +
                        response.text + "</div>"
                }
                if (typeof response.firstProductImageUrl !==
                    "undefined") {
                    defaultHtml +=
                        " <div class='desc-section'><a href='" +
                        ACC.config.encodedContextPath +
                        response.firstProductUrl +
                        "'><img class='lazy' src='" + response.firstProductImageUrl +
                        "'></img>";
                    defaultHtml +=
                        "<div class='showcase-center'>";
                    if (typeof response.firstProductTitle !==
                        "undefined") {
                        defaultHtml +=
                            "<h2 class='product-name'>" +
                            response.firstProductTitle +
                            "</h2>";

                    }
                    if (typeof response.firstProductPrice !==
                        "undefined") {
                        defaultHtml +=
                            "<div class='price'><p class='normal'><span class='priceFormat'>" +
                            response.firstProductPrice +
                            "</span></p></div>";
                    }
                    defaultHtml += "</a></div>";
                }
                defaultHtml += "</div>";
                /*$('#showcase .loaderDiv').remove();
                $(".showcase-switch").css("margin-bottom",
                    "0px");*/
                $('#showcase').append(defaultHtml);
                window.localStorage.setItem("showcaseContent-" +
                    id, encodeURI(defaultHtml));
            },
            error: function() {
                console.log(
                    "Error while getting showcase content");
                /*$('#showcase .loaderDiv').remove();
                $(".showcase-switch").css("margin-bottom",
                    "0px");*/
            }
        });
    }
}

// AJAX call for Showcase
function getShowCaseAjaxCall() {
    var env = $("#previewVersion").val();
    if (env == "true") {
        var dataString = 'version=Staged';
    } else {
        var dataString = 'version=Online';
    }
    $.ajax({
        type: "GET",
        dataType: "json",
        url: ACC.config.encodedContextPath +
            "/getCollectionShowcase",
        data: dataString,
        success: function(response) {
            //TPR-559 Show/Hide Components and Sub-components
            if (response.hasOwnProperty("title") && response.hasOwnProperty("subComponents") && response.subComponents.length) {
                defaultComponentId = "";
                //UF-420
                var defaultHtml = '';
                renderHtml = "<h2>" + response.title + "</h2>" +
                    "<div class='MenuWrap'><div class='mobile selectmenu'></div> <div class='showcase-heading showcase-switch'>";
                $.each(response.subComponents, function(k, v) {
                    if (!v.showByDefault) {
                        renderHtml +=
                            "<div class='showcaseItem'><a id='" +
                            v.compId + "'>" + v.headerText +
                            "</a></div>";
                    } else {
                        renderHtml +=
                            "<div class='showcaseItem'><a id='" +
                            v.compId +
                            "' class='showcase-border'>" +
                            v.headerText + "</a></div>";
                        defaultComponentId = v.compId;
                        //UF-420 starts
                        defaultHtml =
                            "<div class='about-one showcase-section'>";
                        if (typeof v.details.bannerImageUrl !==
                            "undefined") {
                            defaultHtml += "<div class='desc-section'>";
                            if (typeof v.details.bannerUrl !==
                                "undefined") {
                                defaultHtml += "<a href='" + appendIcid(v.details.bannerUrl, v.details.icid) + "'>";
                            }
                            defaultHtml += "<img class='lazy' src='" + v.details.bannerImageUrl +
                                "'></img>";
                            if (typeof v.details.bannerUrl !==
                                "undefined") {
                                defaultHtml += "</a>";
                            }
                            defaultHtml += "</div>";
                        }
                        if (typeof v.details.text !== "undefined") {
                            defaultHtml += "<div class='desc-section'>" +
                                v.details.text + "</div>"
                        }
                        if (typeof v.details.firstProductImageUrl !==
                            "undefined") {
                            defaultHtml +=
                                " <div class='desc-section'><a href='" +
                                ACC.config.encodedContextPath +
                                v.details.firstProductUrl +
                                "'><img class='lazy' src='" + v.details.firstProductImageUrl +
                                "'></img>";
                            defaultHtml +=
                                "<div class='showcase-center'>";
                            if (typeof v.details.firstProductTitle !==
                                "undefined") {
                                defaultHtml +=
                                    "<h2 class='product-name'>" +
                                    v.details.firstProductTitle +
                                    "</h2>";

                            }
                            if (typeof v.details.firstProductPrice !==
                                "undefined") {
                                defaultHtml +=
                                    "<div class='price'><p class='normal'><span class='priceFormat'>" +
                                    v.details.firstProductPrice +
                                    "</span></p></div>";
                            }
                            defaultHtml += "</a></div>";
                        }
                        defaultHtml += "</div>";
                        //UF-420 ends
                    }
                });
                renderHtml += "</div></div>";
                $('#showcase').html(renderHtml);
                //UF-420 starts
                if (defaultHtml != '') {
                    $('#showcase').append(defaultHtml);
                    window.localStorage.setItem("showcaseContent-" +
                        defaultComponentId, encodeURI(defaultHtml));
                }
                //getShowcaseContentAjaxCall(defaultComponentId);
                //UF-420 ends
                $('.selectmenu').text($(".showcaseItem .showcase-border").text());
            }
            if ($(".showcaseItem").length == 1) {
                $(".showcaseItem").addClass("one_showcase");
            }
            if ($(".showcaseItem").length == 2) {
                $(".showcaseItem").addClass("two_showcase");
            }
        },
        error: function() {
            // globalErrorPopup('Failure!!!');
            console.log("Error while getting showcase");
        }
    });
}





$(document).on("click", ".showcaseItem", function() {
    var id = $(this).find('a').attr("id");
    $(".showcaseItem").find("a").removeClass("showcase-border");
    $(this).find('a').addClass('showcase-border');
    $('.about-one.showcase-section').remove();
    getShowcaseContentAjaxCall(id);
});


function getBrandsYouLoveContentAjaxCall(id) {

    if (window.localStorage && (html = window.localStorage.getItem(
            "brandContent-" + id)) && html != "") {
        // console.log("Local");
        $(".home-brands-you-love-carousel").css("margin-bottom", "33px"); /*UF-249*/
        //$('#brandsYouLove').append(defaultHtml);
        $('.home-brands-you-love-desc').remove();
        $('#brandsYouLove').append(decodeURI(html));
        //LazyLoad();
    } else {
        $.ajax({
            type: "GET",
            dataType: "json",
            beforeSend: function() {
                var staticHost = $('#staticHost').val();
            },
            url: ACC.config.encodedContextPath +
                "/getBrandsYouLoveContent",
            data: {
                "id": id
            },
            success: function(response) {
                $('.home-brands-you-love-desc').remove();
                defaultHtml =
                    "<div class='home-brands-you-love-desc'>";
                if (typeof response.text !== "undefined") {
                    defaultHtml += response.text;
                } /* UF-249*/
                if (typeof response.firstProductImageUrl !==
                    "undefined") {
                    defaultHtml +=
                        "<div class='home-brands-you-love-side-image left'><a href='" +
                        ACC.config.encodedContextPath +
                        response.firstProductUrl +
                        "'><img src='" + response.firstProductImageUrl +
                        "'></img>";
                    if (typeof response.firstProductTitle !==
                        "undefined") {
                        defaultHtml +=
                            "<p class='product-name'>" +
                            response.firstProductTitle + "</p>";
                    }
                    if (typeof response.firstProductPrice !==
                        "undefined") {
                        defaultHtml +=
                            "<p class='price normal'><span class='priceFormat'>" +
                            response.firstProductPrice +
                            "</span></p>";
                    }
                    defaultHtml += "</a></div>"
                }
                /******* changes for INC_11934 ***/
                defaultHtml +=
                    "<div class='home-brands-you-love-main-image'>";
                if (typeof response.bannerImageUrl !==
                    "undefined") {
                    defaultHtml +=
                        "<div class='home-brands-you-love-main-image-wrapper'>";
                    /*UF-249*/
                    if (typeof response.bannerUrl !== "undefined") {
                        defaultHtml += "<a href='" + response.bannerUrl + "'><img src='" + response.bannerImageUrl +
                            "'></img></a></div>";
                    } else {
                        defaultHtml += "<img src='" + response.bannerImageUrl +
                            "'></img></div>";
                    }
                    /* UF-249 start*/
                    if (typeof response.text !== "undefined") {
                        defaultHtml += response.text;
                    }
                    if (typeof response.bannerText !==
                        "undefined") {
                        defaultHtml +=
                            "<div class='visit-store-wrapper'>" +
                            response.bannerText + "</div>";
                    }
                    /* UF-249 end*/
                }
                defaultHtml += '</div>';

                if (typeof response.secondproductImageUrl !==
                    "undefined") {
                    defaultHtml +=
                        "<div class='home-brands-you-love-side-image right'><a href='" +
                        ACC.config.encodedContextPath +
                        response.secondProductUrl +
                        "'><img src='" + response.secondproductImageUrl +
                        "'></img>";
                    if (typeof response.secondProductTitle !==
                        "undefined") {
                        defaultHtml +=
                            "<p class='product-name'>" +
                            response.secondProductTitle +
                            "</p>";
                    }
                    if (typeof response.secondProductPrice !==
                        "undefined") {
                        defaultHtml +=
                            "<p class='normal price'><span class='priceFormat'>" +
                            response.secondProductPrice +
                            "</span></p>";
                    }
                    defaultHtml += "</a></div>"
                }
                defaultHtml += "</div>";
                $(".home-brands-you-love-carousel").css(
                    "margin-bottom", "33px"); /* UF-249 */
                $('#brandsYouLove').append(defaultHtml);
                window.localStorage.setItem("brandContent-" +
                    id, encodeURI(defaultHtml));
            },
            complete: function() {
                /*$('#brandsYouLove .loaderDiv').remove();*/
            },
            error: function() {
                console.log(
                    "Error while getting brands you love content"
                );
            }
        });
    }
}



var bulCount = $(".home-brands-you-love-carousel-brands.active").index() - 1;
$(document).on("click", ".home-brands-you-love-carousel-brands",
    function() {
    });
var bulIndex = 0;

$(document).on("click", ".home-brands-you-love-carousel .owl-item", function() {
    var brandCarousel = $(".home-brands-you-love-carousel").data('owlCarousel');
    var relIndex = brandCarousel.relative($(this).index());
    $('.home-brands-you-love-carousel').trigger("to.owl.carousel", [relIndex, 500, true]);
});
if ($('#ia_site_page_id').val() == 'homepage') {
}

$(document).on("mouseover touchend", "div.brandClass", function() {
    var componentUid = $(this).find('a').attr('id');
    if (!$.cookie("dept-list") && window.localStorage) {
        for (var key in localStorage) {
            if (key.indexOf("brandhtml") >= 0) {
                window.localStorage.removeItem(key);
                // console.log("Deleting.." + key);

            }
        }
    }
    if (window.localStorage && (html = window.localStorage.getItem("brandhtml-" + componentUid)) && html != "") {
        // console.log("Local");
        //$("ul#"+componentUid).html(decodeURI(html));
        $("ul[id='" + componentUid + "']").html(decodeURI(html));
    } else {

        $.ajax({
            url: ACC.config.encodedContextPath +
                "/shopbybrand",
            type: 'GET',
            data: {
                "compId": componentUid
            },
            success: function(html) {
                //$("ul#"+componentUid).html(html);
                $("ul[id='" + componentUid + "']").html(html);
                if (window.localStorage) {
                    var date = new Date();
                    var minutes = 600;
                    date.setTime(date.getTime() + (minutes * 60 * 1000));

                    $.cookie("dept-list", "true", {
                        expires: date,
                        path: "/"

                    });
                    window.localStorage.setItem(
                        "brandhtml-" + componentUid,
                        encodeURI(html));

                }

            }
        });

    }
});

//TISPT-290
//function lazyLoadDivs() {
    //Changes

    //$(window).on('scroll load', function() {
       // lazyLoadfunction();
    //});
    //End
    var ctrlKey = false;
    $(document).keydown(function(e) {
        if (e.keyCode == 17) ctrlKey = true;
    }).keyup(function(e) {
        if (e.keyCode == 17) ctrlKey = false;
    });

    $(document).keydown(function(e) {
        if (ctrlKey && (e.which == 109 || e.which == 107 || e.which == 189 || e.which == 187)) {
            lazyLoadfunction();
        }
    });


//}
//depricated
function lazyLoadfunction() {

    if ($(window).scrollTop() + $(window).height() >= $('#brandsYouLove').offset().top) {
        if (!$('#brandsYouLove').attr('loaded')) {
            //not in ajax.success due to multiple sroll events
            $('#brandsYouLove').attr('loaded', true);

            //ajax goes here
            //by theory, this code still may be called several times
            if ($('#brandsYouLove').children().length == 0 && $('#pageTemplateId').val() ==
                'LandingPage2Template') {
                if (window.localStorage) {
                    for (var key in localStorage) {
                        if (key.indexOf("brandContent") >= 0) {
                            window.localStorage.removeItem(key);
                            //console.log("Deleting.." + key);
                        }
                    }
                }
                getBrandsYouLoveAjaxCall();
            }
        }
    }

    //TPR-1672
    if ($(window).scrollTop() + $(window).height() >= $('#bestOffers').offset().top) {
        if (!$('#bestOffers').attr('loaded')) {
            //not in ajax.success due to multiple sroll events
            $('#bestOffers').attr('loaded', true);

            //ajax goes here
            //by theory, this code still may be called several times
            if ($('#bestOffers').children().length == 0 && $('#pageTemplateId').val() ==
                'LandingPage2Template') {
                getBestOffersAjaxCall();
            }
        }
    }

    if ($(window).scrollTop() + $(window).height() >= $('#promobannerhomepage').offset().top) {
        if (!$('#promobannerhomepage').attr('loaded')) {
            //not in ajax.success due to multiple sroll events
            $('#promobannerhomepage').attr('loaded', true);

            //ajax goes here
            //by theory, this code still may be called several times
            if ($('#promobannerhomepage').children().length == 0 && $('#pageTemplateId').val() ==
                'LandingPage2Template') {
                getPromoBannerHomepage();
            }
        }
    }
    if ($(window).scrollTop() + $(window).height() >= $('#bestPicks').offset().top) {
        if (!$('#bestPicks').attr('loaded')) {
            //not in ajax.success due to multiple sroll events
            $('#bestPicks').attr('loaded', true);

            //ajax goes here
            //by theory, this code still may be called several times
            if ($('#bestPicks').children().length == 0 && $('#pageTemplateId').val() ==
                'LandingPage2Template') {
                getBestPicksAjaxCall();
            }
        }
    }

    if ($(window).scrollTop() + $(window).height() >= $('#productYouCare').offset().top) {
        if (!$('#productYouCare').attr('loaded')) {
            //not in ajax.success due to multiple sroll events
            $('#productYouCare').attr('loaded', true);

            //ajax goes here
            //by theory, this code still may be called several times
            if ($('#productYouCare').children().length == 0 && $('#pageTemplateId').val() ==
                'LandingPage2Template') {
                getProductsYouCareAjaxCall();
            }
        }
    }
    if ($(window).scrollTop() + $(window).height() >= $('#newAndExclusive').offset().top) {
        if (!$('#newAndExclusive').attr('loaded')) {
            //not in ajax.success due to multiple sroll events
            $('#newAndExclusive').attr('loaded', true);

            //ajax goes here
            //by theory, this code still may be called several times
            if ($('#newAndExclusive').children().length == 0 && $('#pageTemplateId').val() ==
                'LandingPage2Template') {
                getNewAndExclusiveAjaxCall();
            }
        }
    }

    if ($(window).scrollTop() + $(window).height() >= $('#stayQued').offset().top) {
        if (!$('#stayQued').attr('loaded')) {
            //not in ajax.success due to multiple sroll events
            $('#stayQued').attr('loaded', true);

            //ajax goes here
            //by theory, this code still may be called several times
            if ($('#stayQued').children().length == 0 && $('#pageTemplateId').val() ==
                'LandingPage2Template') {
                getStayQuedHomepage();
            }
        }
    }

    if ($(window).scrollTop() + $(window).height() >= $('#showcase').offset().top && $(window).width() > 767) {
        if (!$('#showcase').attr('loaded')) {
            //not in ajax.success due to multiple sroll events
            $('#showcase').attr('loaded', true);

            //ajax goes here
            //by theory, this code still may be called several times
            if ($('#showcase').children().length == 0 && $('#pageTemplateId').val() ==
                'LandingPage2Template') {
                if (window.localStorage) {
                    for (var key in localStorage) {
                        if (key.indexOf("showcaseContent") >= 0) {
                            window.localStorage.removeItem(key);
                            //console.log("Deleting.." + key);
                        }
                    }
                }
                getShowCaseAjaxCall();
            }
        }
    }

    //UF-420 starts
    if ($(window).scrollTop() + $(window).height() >= $('#showcaseMobile').offset().top && $(window).width() <= 767) {
        if (!$('#showcaseMobile').attr('loaded')) {
            //not in ajax.success due to multiple sroll events
            $('#showcaseMobile').attr('loaded', true);

            if ($('#showcaseMobile').children().length == 0 && $('#pageTemplateId').val() == 'LandingPage2Template') {
                showMobileShowCase();
            }
        }
    }
    //UF-420 ends
}



$(document).ajaxComplete(function() {
    var arrHt = [],
        diffHt = 0,
        arrHtOwl = [],
        diffHtOwl = 0;
    $(".home-brands-you-love-wrapper .home-brands-you-love-desc p.product-name").each(function() {
        arrHt.push($(this).height());
    });
    //noprotect
    var max1 = Math.max.apply(Math, arrHt);
    for (var i = 0; i < arrHt.length; i++) {
        diffHt = max1 - arrHt[i];
        $("#brandsYouLove .home-brands-you-love-desc p.price").eq(i).css("margin-top", +diffHt);
    }
    var len = $("#newAndExclusive .owl-item.active").length;
    for (var j = 0; j < len; j++) {
        arrHtOwl.push($("#newAndExclusive .owl-item.active").eq(j).find(".New_Exclusive_title").height());
    }
    if ($(window).width() > 790) {
        arrHtOwl.splice(-1, 1);
    }
    var max2 = Math.max.apply(Math, arrHtOwl);
    for (var k = 0; k < arrHtOwl.length; k++) {
        diffHtOwl = max2 - arrHtOwl[k];
        $("#newAndExclusive .owl-item.active").eq(k).find(".New_Exclusive_title").css("padding-bottom", +diffHtOwl);
    }


});



$(window).on("resize", function() {
    var arrHt = [],
        diffHt = 0,
        arrHtOwl = [],
        diffHtOwl = 0;
    $(".home-brands-you-love-wrapper .home-brands-you-love-desc p.product-name").each(function() {
        arrHt.push($(this).height());
    });
    //noprotect
    var max1 = Math.max.apply(Math, arrHt);
    for (var i = 0; i < arrHt.length; i++) {
        diffHt = max1 - arrHt[i];
        $("#brandsYouLove .home-brands-you-love-desc p.price").eq(i).css("margin-top", +diffHt);
    }

    var len = $("#newAndExclusive .owl-item.active").length;
    for (var j = 0; j < len; j++) {
        arrHtOwl.push($("#newAndExclusive .owl-item.active").eq(j).find(".New_Exclusive_title").height());
    }
    if ($(window).width() > 790) {
        arrHtOwl.splice(-1, 1);
    }
    var max2 = Math.max.apply(Math, arrHtOwl);
    for (var k = 0; k < arrHtOwl.length; k++) {
        diffHtOwl = max2 - arrHtOwl[k];
        $("#newAndExclusive .owl-item.active").eq(k).find(".New_Exclusive_title").css("padding-bottom", +diffHtOwl);
    }
});
