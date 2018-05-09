import React from "react";
import MetaTags from "react-meta-tags";
import {
  GOOGLE_TAG_TITLE_DEFAULT,
  GOOGLE_TAG_IMAGE_DEFAULT,
  TWITTER_TAG_IMAGE_DEFAULT,
  TWITTER_TAG_TITLE_DEFAULT,
  FACEBOOK_TAG_IMAGE_DEFAULT,
  TITLE_DEFAULT
} from "./constants";
import { URL_ROOT } from "./apiRequest";

export const getPdpSchemaMetaTags = productDetails => {
  return (
    <MetaTags>
      <meta itemprop="priceCurrency" content="INR" />
      <meta itemprop="itemCondition" content="http://schema.org/NewCondition" />
    </MetaTags>
  );
};

export const renderMetaTags = (productDetails, isReviewPage: false) => {
  let canonicalUrl = productDetails.seo.canonicalURL
    ? productDetails.seo.canonicalURL
    : window.location.href;
  let alternateUrl = productDetails.seo.alternateURL
    ? productDetails.seo.alternateURL
    : window.location.href;
  let title = productDetails.seo.title
    ? productDetails.seo.title
    : TITLE_DEFAULT;
  if (title.length === 0) {
    title = TITLE_DEFAULT;
  }

  if (canonicalUrl.length === 0) {
    canonicalUrl = window.location.href;
  }

  if (alternateUrl.length === 0) {
    alternateUrl = window.location.href;
  }

  let description = productDetails.seo.description;
  if (isReviewPage) {
    description = `${productDetails.productName} Review - Check ${
      productDetails.productName
    } reviews, rating & other specifications.`;
    title = `${productDetails.productName} Reviews & Ratings - Tata CLiQ`;
  }

  return (
    <MetaTags>
      <title> {title}</title>
      <meta name="description" content={description} />
      <meta name="keywords" content={productDetails.seo.keywords} />
      <link
        rel="canonical"
        href={`${URL_ROOT}${canonicalUrl}`}
        hreflang="en-in"
      />
      <link
        rel="alternate"
        href={`${URL_ROOT}${alternateUrl}`}
        hreflang="en-in"
      />
      {renderOgTags(productDetails, isReviewPage)}
    </MetaTags>
  );
};

export const renderMetaTagsWithoutSeoObject = () => {
  let description = "some default description";
  return (
    <MetaTags>
      <title> {TITLE_DEFAULT}</title>
      <meta name="description" content={description} />

      <link rel="canonical" href={window.location.href} hreflang="en-in" />
      <link rel="alternate" href={window.location.href} hreflang="en-in" />
      {renderOgTags()}
    </MetaTags>
  );
};

export const renderOgTags = (productDetails, isReviewPage: false) => {
  let googleTitle = GOOGLE_TAG_TITLE_DEFAULT;
  let googleDescription = null;
  let googleImageUrl = GOOGLE_TAG_IMAGE_DEFAULT;
  let twitterTitle = TWITTER_TAG_TITLE_DEFAULT;
  let twitterImageUrl = TWITTER_TAG_IMAGE_DEFAULT;
  let twitterDescription = null;
  let facebookDescription = null;
  let facebookUrl = window.location.href;
  let facebookImageUrl = FACEBOOK_TAG_IMAGE_DEFAULT;
  let facebookTitle = null;
  if (productDetails && productDetails.seo) {
    googleTitle = productDetails.seo.title;
    googleDescription = productDetails.seo.description;
    googleImageUrl = productDetails.seo.imageURL;
    twitterTitle = productDetails.seo.title;
    twitterImageUrl = productDetails.seo.imageURL;
    twitterDescription = productDetails.seo.description;
    facebookDescription = productDetails.seo.description;
    facebookUrl = window.location.href;
    facebookTitle = productDetails.seo.title;
    facebookImageUrl = productDetails.seo.imageURL;
    if (isReviewPage) {
      googleTitle = `${
        productDetails.productName
      } Reviews & Ratings - Tata CLiQ`;
      twitterTitle = `${
        productDetails.productName
      } Reviews & Ratings - Tata CLiQ`;
      facebookTitle = `${
        productDetails.productName
      } Reviews & Ratings - Tata CLiQ`;
      googleDescription = `${productDetails.productName} Review - Check ${
        productDetails.productName
      } reviews, rating & other specifications.`;
      facebookDescription = `${productDetails.productName} Review - Check ${
        productDetails.productName
      } reviews, rating & other specifications.`;
      twitterDescription = `${productDetails.productName} Review - Check ${
        productDetails.productName
      } reviews, rating & other specifications.`;
    }
  }

  return (
    <React.Fragment>
      <meta itemprop="name" content={googleTitle} />
      {googleDescription && (
        <meta itemprop="description" content={googleDescription} />
      )}
      <meta itemprop="image" content={googleImageUrl} />
      <meta name="twitter:card" content="Website" />
      <meta name="twitter:site" content="@tatacliq" />
      <meta name="twitter:title" content={twitterTitle} />
      {twitterDescription && (
        <meta name="twitter:description" content={twitterDescription} />
      )}
      <meta name="twitter:image:src" content={twitterImageUrl} />
      <meta property="og:site_name" content="Tata CliQ" />
      <meta property="og:url" content={facebookUrl} />
      {facebookTitle && <meta property="og:title" content={facebookTitle} />}

      {facebookDescription && (
        <meta property="og:description" content={facebookDescription} />
      )}
      <meta property="og:image" content={facebookImageUrl} />
    </React.Fragment>
  );
};
