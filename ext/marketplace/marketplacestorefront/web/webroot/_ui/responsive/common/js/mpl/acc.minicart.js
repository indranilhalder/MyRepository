ACC.minicart = {

	_autoload : [ "bindMiniCart", "removeFromMyBag" ],

	/**
	 * using this function for click & hover over the 'MyBag' Link
	 */
	bindMiniCart : function() {

		$(document).on(
				"mouseenter",
				".mini-cart-link",
				function(e) {
					var url = $(this).data("miniCartUrl") + "?stamp="
							+ (new Date()).getTime();
					$.get(url, function(html) {
						$(".mini-bag").html(html);

					});

				})

		$(document).on("click", ".js-mini-cart-close-button", function(e) {
			e.preventDefault();
			ACC.colorbox.close();
		})
		$(document).on("click", "#cboxClose, #cboxOverlay", function() {
			$(".myBag-sticky").css({
				"z-index" : 1001
			});
		})
	},

	updateMiniCartDisplay : function() {
		var miniCartRefreshUrl = $(".js-mini-cart-link").data(
				"miniCartRefreshUrl");
		$
				.get(
						miniCartRefreshUrl,
						function(data) {
							var data = $.parseJSON(data);
							$(
									".js-mini-cart-link .js-mini-cart-count,span.responsive-bag-count")
									.html(data.miniCartCount);
							$(".js-mini-cart-link .js-mini-cart-price").html(
									data.miniCartPrice);
						})
	},
	removeFromMyBag : function() {
		$(document)
				.on(
						"click",
						".removeFromCart",
						function(e) {
							var entryNo = $(this).attr("data-entry-no");
							var entryUssid = $(this).attr("data-ussid");
							$
									.ajax({
										url : ACC.config.encodedContextPath
												+ "/cart/removeFromMinicart?entryNumber="
												+ entryNo,
										type : 'GET',
										cache : false,
										success : function(response) {
											$
													.ajax({
														url : ACC.config.encodedContextPath
																+ "/cart/miniCart/TOTAL",
														returnType : "JSON",
														type : "GET",
														cache : false,
														success : function(
																cartData) {
															cartData = $
																	.parseJSON(cartData);
															quantity = parseInt(cartData.masterMiniCartCount);
															$(
																	"span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count")
																	.text(
																			quantity);
															if (quantity == 0) {
																$(
																		"ul.my-bag-ul")
																		.remove();
															} else {
																// console.log($(e.target).parents().find("li.item"));
																var url = window.location.href;
																$(e.target)
																		.parents()
																		.find(
																				"li.item")
																		.remove();
																if (url
																		.indexOf("cart") != -1) {
																	window.location
																			.reload();
																}
															}
															ACC.product
																	.addToBagFromWl(
																			entryUssid,
																			false);
															//ACC.track.trackAddToCart(productCode, quantity, cartResult.cartData);
														}
													});
										},
										error : function(resp) {
											console.log(resp);
										}
									});
						});
	}
};
