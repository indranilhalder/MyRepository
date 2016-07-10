;(function ($) {

"use strict";

$(document).ready(function () {

  $('.image-stretch').each(function () {
    var url = $(this).css('background-image').replace('url(','').replace(')','').replace(/"/g, '').replace(/'/g, '');
    $(this).backstretch(url);
  });

});

}(jQuery))