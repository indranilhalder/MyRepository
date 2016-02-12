ACC.comparenow = {

	_autoload : [ "bindCompareNow" ],

	bindCompareNow : function() {

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
		$('.compare input[type=checkbox]')
				.click(
						function() {
							var checkboxId = $(this).attr('id');

							if ($(this).prop("checked") == true) {
								// AJAX CALL
								$
										.ajax({
											type : "GET",
											dataType : "json",
											url : ACC.config.encodedContextPath
													+ "/compare/add/?productCode="
													+ checkboxId,

											success : function(response) {
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
																	var renderHtml = "<div class='compare-item' id='compare-id"
																			+ v.productCode
																			+ "'><img src='"
																			+ v.productImageUrl
																			+ "'/><ul class='content'><li>"
																			+ v.brand
																			+ "</li><li>"
																			+ v.productName
																			+ "</li>";
																	if (v.productPrice != v.productPriceSpecial) {

																		renderHtml += "<li><p class='old'>"
																				+ v.productPrice
																				+ "<p></li><li><p class='sale'>"
																				+ v.productPriceSpecial
																				+ "</p></li><br/>";
																	} else {
																		renderHtml += "<li>"
																				+ v.productPrice
																				+ "</li><br/>";
																	}

																	renderHtml += "<li><a id='"
																			+ v.productCode
																			+ "'class='compareRemoveLink'>Remove</li></ul></div>";
																	$(
																			'#compareProducts')
																			.append(
																					renderHtml);

																	comparableProductCode
																			.push(v.productCode);
																});
												// if (compareProductCount <
												// selected.length) {

												for (index = 0; index < selected.length; ++index) {
													if (comparableProductCode
															.indexOf(selected[index]) == -1) {
														$(
																"input:checkbox[id="
																		+ selected[index]
																		+ "]")
																.prop(
																		'checked',
																		false);
														var compareText = 'compare'
																+ selected[index];
														var divId = '#'
																+ compareText;
														$(divId)
																.html(
																		"Add to compare");
														$('#compareError')
																.html(
																		"Sorry! We can only compare similar products.");

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
											error : function() {
												globalErrorPopup('Sorry we are unable to add your product to compare. Please try after sometime');
											}
										});
								// END
							} else {

								// AJAX CALL
								$
										.ajax({
											type : "GET",
											dataType : "json",
											url : ACC.config.encodedContextPath
													+ "/compare/remove/?productCode="
													+ checkboxId,

											success : function(response) {

												var divId = '#compare-id'
														+ checkboxId;
												$(divId).remove();
												$(
														"input:checkbox[id="
																+ checkboxId
																+ "]").prop(
														'checked', false);
												$('#compare' + checkboxId)
														.html('Add to Compare');
												var count = 0;
												$(
														'.compare input[type=checkbox]')
														.each(
																function() {
																	if ($(this)
																			.is(
																					":checked")) {
																		count++;

																	}
																});
												if (count <= 1) {
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
											error : function() {
												globalErrorPopup('Sorry we are unable to add your product to compare. Please try after sometime');
											}
										});
								// END

							}

							$('.compare input[type=checkbox]')
									.each(
											function() {
												var checkboxId = '#'
														+ $(this).attr('id');
												var checked = $(checkboxId).is(
														":checked");
												if (checked) {
													var productCode = $(this)
															.attr('id');
													var compareText = 'compare'
															+ productCode;
													var divId = '#'
															+ compareText;
													$(divId)
															.html("Compare Now");
													var present = false;
													for (index = 0; index < selected.length; ++index) {
														if (productCode == selected[index]) {
															present = true;
														}
													}
													if (!present) {
														if (selected.length < 4)
															selected
																	.push(productCode);
														else {
															$('#compareError')
																	.html(
																			"You can add maximum 4 products to compare");

															$(this).prop(
																	'checked',
																	false);

														}
													}

												} else {
													var productCode = $(this)
															.attr('id');
													var compareText = 'compare'
															+ productCode;
													var divId = '#'
															+ compareText;
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

		/*
		 * $(document).on("click", ".compareNowLink", function() {
		 * 
		 * param=selected;
		 * window.location.href=ACC.config.encodedContextPath+"/compare/?productCodes="+selected;
		 * });
		 */

		/*
		 * $(window).on( "scroll", function() { if ($(window).scrollTop() >
		 * $(".comparison-table .short-info").offset().top - $("header
		 * .bottom.active").height()) {
		 * $(".comparison-table").first().addClass("active"); }
		 * if($(window).scrollTop() < $(".wrapper.compare").offset().top -10-
		 * $("header .bottom.active").height() -
		 * $(".comparison-table.active").height()){
		 * $(".comparison-table").first().removeClass("active"); } });
		 */

		if ($(window).resize().width() > 790) {
			$(window)
					.on(
							"scroll",
							function() {
								var nav = $(".comparison-table .short-info,.wrapper.compare ");
								if (nav.length) {
									if ($(window).scrollTop() > $(
											".comparison-table .short-info")
											.offset().top
											- $("header .bottom.active")
													.height()) {
										$(".comparison-table.stats")
												.first()
												.css(
														"margin-top",
														$(
																".comparison-table.products-compareTable")
																.height()
																+ $(
																		"header .bottom.active")
																		.height());
										$(".comparison-table").first()
												.addClass("active");

									}
									if ($(window).scrollTop() < $(
											".wrapper.compare").offset().top
											- 10
											- $("header .bottom.active")
													.height()
											- $(".comparison-table.active")
													.height()) {
										$(".comparison-table.stats").first()
												.css("margin-top", "0px");
										$(".comparison-table").first()
												.removeClass("active");

									}
									$(".comparison-table.active")
											.css(
													{
														"width" : $(
																".comparison-table.stats")
																.width()
													})
								}
							});

		}
		$(window).on("load resize", function() {
			$(".comparison-table.active").css({
				"width" : $(".comparison-table.stats").width()
			})
		});
		$(".compareBtn").click(function() {
			window.location.href = ACC.config.encodedContextPath + "/compare";
		});

		$(".closeLink")
				.click(
						function() {
							$('#compareSection').hide();

							$('.compare input[type=checkbox]').each(function() {
								if ($(this).is(":checked")) {
									$(this).prop('checked', false);
									var id = $(this).attr('id');
									$('#compare' + id).html('Add to compare');
								}
							});

							// AJAX CALL
							$
									.ajax({
										type : "GET",
										dataType : "json",
										url : ACC.config.encodedContextPath
												+ "/compare/removeAll",

										success : function(response) {

										},
										error : function() {
											globalErrorPopup('Sorry we are unable to add your product to compare. Please try after sometime');
										}
									});

						});

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
										type : "GET",
										dataType : "json",
										url : ACC.config.encodedContextPath
												+ "/compare/remove/?productCode="
												+ productCode,

										success : function(response) {

											var divId = '#compare-id'
													+ productCode;
											$(divId).remove();
											$(
													"input:checkbox[id="
															+ productCode + "]")
													.prop('checked', false);
											$('#compare' + productCode).html(
													'Add to Compare');
											var count = 0;
											$('.compare input[type=checkbox]')
													.each(
															function() {
																if ($(this)
																		.is(
																				":checked")) {
																	count++;

																}
															});
											if (count <= 1) {
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
										error : function() {
											globalErrorPopup('Sorry we are unable to add your product to compare. Please try after sometime');
										}
									});
							// END

						})

	}
};
