
        if ($('#compareProducts .compare-item').length > 0) {

            $('#compareSection').show();
            $('.compare-item').each(function() {
                var item = $(this).attr('id');
                var id = item.substring(10);
                $('#' + id).prop('checked', true);
                $('#compare' + id).html('Compare Now');
            });
        }

        var selected = [];
		var tealiumCompare=[];
        $(document).on('click','.compare input[type=checkbox]',function(){
                    var checkboxId = $(this).attr('id');
                    //Added
                    var screenwidth = $(window).width();
                    var items = 4;
                    if (screenwidth < 650) {
                        var items = 2;
                    }
                    //End
					
                    if ($(this).prop("checked") == true) {
                        // AJAX CALL
                        $
                            .ajax({
                                type: "GET",
                                dataType: "json",
                                url: ACC.config.encodedContextPath +
                                    "/compare/add/?productCode=" +
                                    checkboxId + "&maximumSize=" + items,

                                success: function(response) {
                                    //
                                    setTimeout(function() {
                                        // Just wait
                                        $('#compareError').empty();
                                    }, 3000);
                                    $('#compareProducts').empty();
                                    $('#compareSection').show();

                                    var compareProductCount = 0;
                                    var comparableProductCode = [];
                                    $
                                        .each(
                                            response,
                                            function(k, v) {
                                                console
                                                    .log(v.productPriceSpecial)
                                                compareProductCount++;
                                                var renderHtml = "<div class='compare-item' id='compare-id" +
                                                    v.productCode +
                                                    "'><img src='" +
                                                    v.productImageUrl +
                                                    "'/><ul class='content'><li>" +
                                                    v.brand +
                                                    "</li><li>" +
                                                    v.productName +
                                                    "</li>";
                                                if (v.productPrice != v.productPriceSpecial) {

                                                    renderHtml += "<li><p class='old'>" +
                                                        v.productPrice +
                                                        "<p></li><li><p class='sale'>" +
                                                        v.productPriceSpecial +
                                                        "</p></li><br/>";
                                                } else {
                                                    renderHtml += "<li>" +
                                                        v.productPrice +
                                                        "</li><br/>";
                                                }

                                                renderHtml += "<li><a id='" +
                                                    v.productCode +
                                                    "'class='compareRemoveLink'>Remove</li></ul></div>";
                                                $(
                                                        '#compareProducts')
                                                    .append(
                                                        renderHtml);

                                                comparableProductCode
                                                    .push(v.productCode);
                                              //TPR-4727 | add to compare 1st part
    	                       					if(typeof utag !="undefined"){
    	                       						utag.link({ link_text : "add_to_compare_clicked" , event_type : "add_to_compare_clicked" });
    	                       					}
                                                tealiumCompare=comparableProductCode;
                                                //TPR-6290
                                                if(typeof _satellite !="undefined"){
    	                       						_satellite.track('product_comparison');
    	                       					}
                                                dtmAddToCompare(v.productCode,v.categoryType);
                                            });
                                    // if (compareProductCount <
                                    // selected.length) {

                                    for (index = 0; index < selected.length; ++index) {
                                        if (comparableProductCode
                                            .indexOf(selected[index]) == -1) {
                                            $(
                                                    "input:checkbox[id=" +
                                                    selected[index] +
                                                    "]")
                                                .prop(
                                                    'checked',
                                                    false);
                                            var compareText = 'compare' +
                                                selected[index];
                                            var divId = '#' +
                                                compareText;
                                            $(divId)
                                                .html(
                                                    "Add to compare");
                                            $('#compareError')
                                                .html(
                                                    "Sorry! We can only compare similar products.");
											//TPR-4727 | Add to compare | serp
											if(typeof utag !="undefined"){
												   utag.link({error_type : 'comparison_error'});
												}
                                        }
                                    }

                                    // }

                                    if (compareProductCount > 1) {
                                        $('#compareBtn')
                                            .removeClass(
                                                'disabled');
                                        $('#compareBtn').addClass(
                                            'enabled');
                                        $('#compareBtn')
                                            .removeAttr(
                                                "disabled");
                                    } else {
                                        $('#compareBtn').attr(
                                            "disabled",
                                            "disabled");
                                        $('#compareBtn')
                                            .removeClass(
                                                'enabled');
                                        $('#compareBtn').addClass(
                                            'disabled');
                                    }

                                },
                                error: function() {
                                    globalErrorPopup('Sorry we are unable to add your product to compare. Please try after sometime');
									//TPR-4727 | Add to compare | serp
									if(typeof utag !="undefined"){
										   utag.link({error_type : 'comparison_error'});
										}
                                }
                            });
                        // END
                    } else {

                        // AJAX CALL
                        $
                            .ajax({
                                type: "GET",
                                dataType: "json",
                                url: ACC.config.encodedContextPath +
                                    "/compare/remove/?productCode=" +
                                    checkboxId,

                                success: function(response) {
                                    var divId = '#compare-id' +
                                        checkboxId;
                                    $(divId).remove();
                                    /*TPR-4715 |add to compare */
                                    if(typeof utag !="undefined"){
                                  		 utag.link({ link_text : "add_to_compare_removed" , event_type : "add_to_compare_removed" });
                                  	 }
                                    var compareProdLen = $('#compareSection .compare-item').length;
                                    $(
                                        "input:checkbox[id=" +
                                        checkboxId +
                                        "]").prop(
                                        'checked', false);
                                    $('#compare' + checkboxId)
                                        .html('Add to Compare');
                                    //												var count = 0;
                                    //												$(
                                    //														'.compare input[type=checkbox]')
                                    //														.each(
                                    //																function() {
                                    //																	if ($(this)
                                    //																			.is(
                                    //																					":checked")) {
                                    //																		count++;
                                    //
                                    //																	}
                                    //																});
                                    if (compareProdLen <= 1) {
                                        $('#compareBtn').attr(
                                            "disabled",
                                            "disabled");
                                        $('#compareBtn')
                                            .removeClass(
                                                'enabled');
                                        $('#compareBtn').addClass(
                                            'disabled');
                                    }

                                    if ($('#compareProducts .compare-item').length == 0) {
                                        $('#compareSection').hide();
                                    }

                                },
                                error: function() {
                                    globalErrorPopup('Sorry we are unable to add your product to compare. Please try after sometime');
									//TPR-4727 | Add to compare | serp
									if(typeof utag !="undefined"){
										   utag.link({error_type : 'comparison_error'});
										}
                                }
                            });
                        // END

                    }

                    $('.compare input[type=checkbox]')
                        .each(
                            function() {
                                var checkboxId = '#' +
                                    $(this).attr('id');
                                var checked = $(checkboxId).is(
                                    ":checked");
                                if (checked) {
                                    var productCode = $(this)
                                        .attr('id');
                                    var compareText = 'compare' +
                                        productCode;
                                    var divId = '#' +
                                        compareText;
                                    $(divId)
                                        .html("Compare Now");
                                    var present = false;
                                    for (index = 0; index < selected.length; ++index) {
                                        if (productCode == selected[index]) {
                                            present = true;
                                        }
                                    }
                                    if (!present) {
                                        var screenwidth = $(window).width();
                                        var items = 4;
                                        if (screenwidth < 650) {
                                            var items = 2;
                                        }

                                        if (selected.length < items){
                                            selected.push(productCode);

                                        }	
                                        else {
					    //INC144317579
                                            $('#compare' + productCode).html('Add to Compare');
                                            $('#compareError')
                                                .html(
                                                    "You can add maximum " + items + " products to compare");

                                            $(this).prop(
                                                'checked',
                                                false);
											//TPR-4727 | Add to compare | serp
											if(typeof utag !="undefined"){
												   utag.link({error_type : 'comparison_error'});
												}
		                                }
                                    }

                                } else {
                                    var productCode = $(this)
                                        .attr('id');
                                    var compareText = 'compare' +
                                        productCode;
                                    var divId = '#' +
                                        compareText;
                                    $(divId).html(
                                        "Add to compare");
                                    for (index = 0; index < selected.length; ++index) {
                                        if (productCode == selected[index]) {
                                            selected
                                                .splice(
                                                    $
                                                    .inArray(
                                                        productCode,
                                                        selected),
                                                    1);
                                        }
                                    }

                                }

                            });

                });


        if ($(window).resize().width() > 790) {
            $(window)
                .on(
                    "scroll",
                    function() {
                        var nav = $(".comparison-table .short-info,.wrapper.compare ");
                        if (nav.length) {
                            if ($(window).scrollTop() > $(
                                    ".comparison-table .short-info")
                                .offset().top -
                                $("header .bottom.active")
                                .height()) {
                                $(".comparison-table.stats")
                                    .first()
                                    .css(
                                        "margin-top",
                                        $(
                                            ".comparison-table.products-compareTable")
                                        .height() +
                                        $(
                                            "header .bottom.active")
                                        .height());
                                $(".comparison-table").first()
                                    .addClass("active");

                            }
                            if ($(window).scrollTop() < $(
                                    ".wrapper.compare").offset().top -
                                10 -
                                $("header .bottom.active")
                                .height() -
                                $(".comparison-table.active")
                                .height()) {
                                $(".comparison-table.stats").first()
                                    .css("margin-top", "0px");
                                $(".comparison-table").first()
                                    .removeClass("active");

                            }
                            $(".comparison-table.active")
                                .css({
                                    "width": $(
                                            ".comparison-table.stats")
                                        .width()
                                })
                        }
                    });

        }
        $(window).on("load resize", function() {
            $(".comparison-table.active").css({
                "width": $(".comparison-table.stats").width()
            })
        });
        $(document).on('click','.compareBtn',function(){
        	//TPR-4727 | add to compare 4th part
			if(typeof utag !="undefined"){
				utag.link({ link_text : "compare_product_id_final" ,  compare_product_id_final : tealiumCompare });
			}
			//TPR-6290
			if (typeof _satellite != "undefined") {
				 _satellite.track('compare_now');
			    }	
			dtmAddToComparedList(tealiumCompare);
			window.location.href = ACC.config.encodedContextPath + "/compare"
		})
		$(document).on('click','.closeLink',function(){
			$("#compareSection").hide();
            $(".compare input[type=checkbox]").each(function() {
                if ($(this).is(":checked")) {
                    $(this).prop("checked", !1);
                    var a = $(this).attr("id");
                    $("#compare" + a).html("Add to compare")
                }
            });
            $.ajax({
                type: "GET",
                dataType: "json",
                url: ACC.config.encodedContextPath + "/compare/removeAll",
                success: function(a) {},
                error: function() {
                    globalErrorPopup("Sorry we are unable to add your product to compare. Please try after sometime");
					//TPR-4727 | Add to compare | serp
					if(typeof utag !="undefined"){
						   utag.link({error_type : 'comparison_error'});
						}
                }
            })
		})

        $(document)
            .on(
                "click",
                ".compareRemoveLink",
                function() {
                    $('#compareError').empty();
                    var productCode = $(this).attr('id');

                    // AJAX CALL
                    $
                        .ajax({
                            type: "GET",
                            dataType: "json",
                            url: ACC.config.encodedContextPath +
                                "/compare/remove/?productCode=" +
                                productCode,

                            success: function(response) {

                                var divId = '#compare-id' +
                                    productCode;
                                $(divId).remove();
                                $(
                                        "input:checkbox[id=" +
                                        productCode + "]")
                                    .prop('checked', false);
                                $('#compare' + productCode).html(
                                    'Add to Compare');
                                var compareProdLen1 = $('#compareSection .compare-item').length;
                                //											var count = 0;
                                //											$('.compare input[type=checkbox]')
                                //													.each(
                                //															function() {
                                //																if ($(this)
                                //																		.is(
                                //																				":checked")) {
                                //																	count++;
                                //
                                //																}
                                //															});
                                if (compareProdLen1 <= 1) {
                                    $('#compareBtn').attr(
                                        "disabled", "disabled");
                                    $('#compareBtn').removeClass(
                                        'enabled');
                                    $('#compareBtn').addClass(
                                        'disabled');
                                }

                                if ($('#compareProducts .compare-item').length == 0) {
                                    $('#compareSection').hide();
                                }

                            },
                            error: function() {
                                globalErrorPopup('Sorry we are unable to add your product to compare. Please try after sometime');
								//TPR-4727 | Add to compare | serp
								if(typeof utag !="undefined"){
									   utag.link({error_type : 'comparison_error'});
									}
                            }
                        });
                    // END

                })

    
