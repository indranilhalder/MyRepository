<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="video-wrapper">
    <div class="video-container">
        <video width="100%" height="100%" controls autoplay poster="${previewImageUrl}">
            <source src="${videoUrl}" type="video/mp4">
        </video>
    </div>
</div>



