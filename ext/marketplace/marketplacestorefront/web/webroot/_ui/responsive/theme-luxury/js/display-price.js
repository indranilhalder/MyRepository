function dispPrice(mrp, mop, spPrice, savingsOnProduct) {
    if ($("#mrpPriceId").html(""), $("#mopPriceId").html(""), $("#spPriceId").html(""), 
    $("#savingsOnProductId").html(""), void 0 === savingsOnProduct && (null != mrp && null != spPrice ? (savingPriceCal = mrp.doubleValue - spPrice.doubleValue, 
    savingPriceCalPer = savingPriceCal / mrp.doubleValue * 100, savingsOnProduct = Math.round(100 * savingPriceCalPer / 100)) : null != mrp && null != mop && (savingPriceCal = mrp.doubleValue - mop.doubleValue, 
    savingPriceCalPer = savingPriceCal / mrp.doubleValue * 100, savingsOnProduct = Math.round(100 * savingPriceCalPer / 100))), 
    null != mrp && $("#mrpPriceId").append(mrp.formattedValueNoDecimal), null != mop && $("#mopPriceId").html('<span>Price:</span><span>'+mop.formattedValueNoDecimal+'</span>'), 
    null != spPrice && $("#spPriceId").append(spPrice.formattedValueNoDecimal), null != savingsOnProduct && $("#savingsOnProductId").append("(-" + savingsOnProduct + " %)"), 
    null != savingsOnProduct && 0 != savingsOnProduct && $("#savingsOnProductId").show(), 
    "" == mrp.value) $("#mrpPriceId").hide(), $("#savingsOnProductId").hide(), $("#addToCartButton-wrong").attr("disable", !0), 
    $("#addToCartButton-wrong").show(), $("#addToCartButton").hide(), $("#buyNowButton").attr("disabled", !0); else if (null != spPrice && 0 != spPrice) mop.value, 
    mrp.value, $("#mrpPriceId").css("text-decoration", "line-through"), $("#mrpPriceId").show(), 
    $("#spPriceId").show(), $("#mopPriceId").hide(), $("#mrpPriceId").removeClass("sale").addClass("old"); else {
        var freebiePriceThresVal = $("#freebiePriceThreshId").val();
        if (null != mop && 0 != mop.value && mop.value > freebiePriceThresVal) mop.value == mrp.value ? ($("#mrpPriceId").removeClass("old").addClass("sale"), 
        $("#mrpPriceId").show(), $("#mrpPriceId").css("text-decoration", ""),$(".price-feature").addClass("nonsale"), $("#mopPriceId").hide(),
        $("#spPriceId").hide()) : ($("#mrpPriceId").css("text-decoration", "line-through"), 
        $("#mrpPriceId").show(), $("#mopPriceId").show(), $("#spPriceId").hide(), $("#mrpPriceId").removeClass("sale").addClass("old")); else if (0 != mop.value && mop.value <= freebiePriceThresVal) {
            $(".size").hide(), $(".color-swatch").hide(), $(".reviews").hide(), $("#addToCartButton-wrong").attr("disable", !0), 
            $("#addToCartButton-wrong").show(), $("#addToCartButton").hide(), $("#otherSellerInfoId").hide(), 
            $(".wish-share").hide(), $(".fullfilled-by").hide(), $("#pdpPincodeCheck").hide(), 
            $("#pin").attr("disabled", !0), $("#pdpPincodeCheckDList").show(), $("#buyNowButton").attr("disabled", !0), 
            $("#mopPriceId").hide(), $("#mrpPriceId").hide(), $("#savingsOnProductId").hide(), 
            $(".delivery-block").hide(), $(".seller").hide(), $(".star-review").hide(), $("#spPriceId").hide();
            var prodCode = $("#productCodePost").val(), freebieproductMsg = ($("#ussid").val(), 
            populateFreebieMsg(prodCode));
            $.isEmptyObject(freebieproductMsg) ? $("#freebieProductMsgId").show() : ($("#freebieProductMsgId").html(freebieMsg), 
            $("#freebieProductMsgId").show());
        }else{$(".price-feature").addClass("nonsale"); $("#mrpPriceId").show();}
    }
    void 0 != spPrice || null != spPrice ? ($("#prodPrice").val(spPrice.value), $("#price-for-mad").val(spPrice.value)) : void 0 != mop || null != mop ? ($("#prodPrice").val(mop.value), 
    $("#price-for-mad").val(mop.value)) : ($("#prodPrice").val(mrp.value), $("#price-for-mad").val(mrp.value)), 
    parseInt($("#prodPrice").val()) > emiCuttOffAmount.value ? $("#emiStickerId").show() : $("#emiStickerId").hide();
}