import React from "react";
import PdpElectronics from "./PdpElectronics";
import PdpApparel from "./PdpApparel";
import PdpJewellery from "./PdpJewellery";
import PdpHome from "./PdpHome";

import styles from "./ProductDescriptionPageWrapper.css";
import SecondaryLoader from "../../general/components/SecondaryLoader";
import MetaTags from "react-meta-tags";
import {
  PRODUCT_DESCRIPTION_PRODUCT_CODE,
  PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE,
  UPDATE_PDP_REDUCER_FOR_DELIVERY_OPTION,
  DEFAULT_PIN_CODE_LOCAL_STORAGE,
  GOOGLE_TAG_TITLE_DEFAULT,
  GOOGLE_TAG_IMAGE_DEFAULT,
  TWITTER_TAG_IMAGE_DEFAULT,
  TWITTER_TAG_TITLE_DEFAULT,
  FACEBOOK_TAG_IMAGE_DEFAULT
} from "../../lib/constants";
// prettier-ignore
const typeComponentMapping = {
  "Electronics": props => <PdpElectronics {...props} />,
  "Watches":props =><PdpElectronics {...props} />,
  "FashionJewellery":props => <PdpJewellery {...props} />,
  "Clothing":props => <PdpApparel {...props} />,
  "Footwear":props => <PdpApparel {...props} />,
  "HomeFurnishing":props => <PdpHome {...props} />,
  "FineJewellery": props => <PdpJewellery {...props} />,
};

const defaultPinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);

export default class ProductDescriptionPageWrapper extends React.Component {
  componentDidMount() {
    if (this.props.match.path === PRODUCT_DESCRIPTION_PRODUCT_CODE) {
      setTimeout(() => {
        window.scrollTo(0, 0);
      }, 0);
      this.props.getProductDescription(this.props.match.params[0]);
      this.props.getMsdRequest(this.props.match.params[0]);
      this.props.pdpAboutBrand(this.props.match.params[0]);
      if (defaultPinCode) {
        this.props.getProductPinCode(
          defaultPinCode,
          this.props.match.params[0]
        );
      }
    } else if (
      this.props.match.path === PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE
    ) {
      setTimeout(() => {
        window.scrollTo(0, 0);
      }, 0);
      this.props.getProductDescription(this.props.match.params[1]);
      this.props.getMsdRequest(this.props.match.params[1]);
      this.props.pdpAboutBrand(this.props.match.params[1]);
      if (defaultPinCode) {
        this.props.getProductPinCode(
          defaultPinCode,
          this.props.match.params[1]
        );
      }
    } else {
      //need to show error page
    }
  }

  componentDidUpdate(prevProps, prevState) {
    if (prevProps.location.pathname !== this.props.location.pathname) {
      setTimeout(() => {
        window.scrollTo(0, 0);
      }, 0);

      if (this.props.match.path === PRODUCT_DESCRIPTION_PRODUCT_CODE) {
        this.props.getProductDescription(this.props.match.params[0]);
        this.props.getMsdRequest(this.props.match.params[0]);
        if (defaultPinCode) {
          this.props.getProductPinCode(
            defaultPinCode,
            this.props.match.params[0]
          );
        }
      } else if (
        this.props.match.path === PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE
      ) {
        setTimeout(() => {
          window.scrollTo(0, 0);
        }, 0);
        this.props.getProductDescription(this.props.match.params[1]);
        this.props.getMsdRequest(this.props.match.params[1]);
        if (defaultPinCode) {
          this.props.getProductPinCode(
            defaultPinCode,
            this.props.match.params[1]
          );
        }
      } else {
        //need to show error page
      }
    }
  }
  showLoader = () => {
    this.props.showSecondaryLoader();
  };
  hideLoader = () => {
    this.props.hideSecondaryLoader();
  };
  renderRootCategory = datumType => {
    let pdpToRender = typeComponentMapping[datumType];
    if (!pdpToRender) {
      pdpToRender = typeComponentMapping["Clothing"];
    }

    return <React.Fragment>{pdpToRender({ ...this.props })}</React.Fragment>;
  };
  renderLoader() {
    return (
      <div className={styles.loadingIndicator}>
        <SecondaryLoader />
      </div>
    );
  }
  /*

  */

  renderOgTags = () => {
    const productDetails = this.props.productDetails;
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
  renderMetaTags = () => {
    const productDetails = this.props.productDetails;
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
        {this.renderOgTags()}
      </MetaTags>
    );
  };

  renderMetaTagsWithoutSeoObject = () => {
    return (
      <MetaTags>
        <link rel="canonical" href={window.location.href} hreflang="en-in" />
        <link rel="alternate" href={window.location.href} hreflang="en-in" />
        {this.renderOgTags()}
      </MetaTags>
    );
  };

  render() {
    if (this.props.loading) {
      this.showLoader();
    } else {
      this.hideLoader();
    }
    if (this.props.productDetails) {
      return (
        <div>
          {this.props.productDetails.seo
            ? this.renderMetaTags()
            : this.renderMetaTagsWithoutSeoObject()}
          {this.renderRootCategory(this.props.productDetails.rootCategory)}
        </div>
      );
    } else {
      return this.renderLoader();
    }
  }
}
