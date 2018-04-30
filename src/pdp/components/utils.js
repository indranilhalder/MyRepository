import React from "react";
import MetaTags from "react-meta-tags";
import {
  GOOGLE_TAG_TITLE_DEFAULT,
  GOOGLE_TAG_IMAGE_DEFAULT,
  TWITTER_TAG_IMAGE_DEFAULT,
  TWITTER_TAG_TITLE_DEFAULT,
  FACEBOOK_TAG_IMAGE_DEFAULT
} from "../../lib/constants";
export const getPdpSchemaMetaTags = productDetails => {};

export const renderMetaTags = productDetails => {
  const canonicalUrl = productDetails.seo.canonicalURL
    ? productDetails.seo.canonicalURL
    : window.location.href;
  const alternateUrl = productDetails.seo.alternateURL
    ? productDetails.seo.alternateURL
    : window.location.href;

  return (
    <MetaTags>
      <title> {productDetails.seo.title}</title>
      <meta name="description" content={productDetails.seo.description} />
      <meta name="keywords" content={productDetails.seo.keywords} />
      <link rel="canonical" href={canonicalUrl} hreflang="en-in" />
      <link rel="alternate" href={alternateUrl} hreflang="en-in" />
      {renderOgTags(productDetails)}
    </MetaTags>
  );
};

export const renderMetaTagsWithoutSeoObject = () => {
  return (
    <MetaTags>
      <link rel="canonical" href={window.location.href} hreflang="en-in" />
      <link rel="alternate" href={window.location.href} hreflang="en-in" />
      {renderOgTags()}
    </MetaTags>
  );
};

export const renderOgTags = productDetails => {
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
  if (productDetails.seo) {
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
