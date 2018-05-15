import React from "react";
import Loadable from "react-loadable";

import styles from "./ProductDescriptionPageWrapper.css";
import SecondaryLoader from "../../general/components/SecondaryLoader";
import {
  PRODUCT_DESCRIPTION_PRODUCT_CODE,
  PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE,
  DEFAULT_PIN_CODE_LOCAL_STORAGE
} from "../../lib/constants";
import {
  renderMetaTags,
  renderMetaTagsWithoutSeoObject
} from "../../lib/seoUtils.js";
import PdpElectronics from "./PdpElectronics";
import PdpJewellery from "./PdpJewellery";
import PdpApparel from "./PdpApparel";
import PdpHome from "./PdpHome";
// prettier-ignore

const PiqPageForPdp = Loadable({
  loader: () => import("./PiqPageForPdp"),
  loading() {
    return <div className={styles.loadingIndicator}><Loader /></div>
  }
})

const typeComponentMapping = {
  Electronics: props => <PdpElectronics {...props} />,
  Watches: props => <PdpElectronics {...props} />,
  FashionJewellery: props => <PdpJewellery {...props} />,
  Clothing: props => <PdpApparel {...props} />,
  Footwear: props => <PdpApparel {...props} />,
  HomeFurnishing: props => <PdpHome {...props} />,
  FineJewellery: props => <PdpJewellery {...props} />
};

const Loader = () => {
  return (
    <div>
      <SecondaryLoader />
    </div>
  );
};

const defaultPinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);

export default class ProductDescriptionPageWrapper extends React.Component {
  constructor(props) {
    super(props);
    this.state = { showPiqPage: false };
  }
  componentDidMount = async () => {
    if (this.props.match.path === PRODUCT_DESCRIPTION_PRODUCT_CODE) {
      setTimeout(() => {
        window.scrollTo(0, 0);
      }, 0);
      await this.props.getProductDescription(this.props.match.params[0]);
    } else if (
      this.props.match.path === PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE
    ) {
      setTimeout(() => {
        window.scrollTo(0, 0);
      }, 0);
      this.props.getProductDescription(this.props.match.params[1]);
    } else {
      //need to show error page
    }
  };

  componentDidUpdate(prevProps, prevState) {
    if (this.props.productDetails && this.props.productDetails !== "null") {
      this.props.setHeaderText(this.props.productDetails.productName);
    }

    if (prevProps.location.pathname !== this.props.location.pathname) {
      setTimeout(() => {
        window.scrollTo(0, 0);
      }, 0);

      if (this.props.match.path === PRODUCT_DESCRIPTION_PRODUCT_CODE) {
        this.props.getProductDescription(this.props.match.params[0]);
        // this.props.getMsdRequest(this.props.match.params[0]);
      } else if (
        this.props.match.path === PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE
      ) {
        setTimeout(() => {
          window.scrollTo(0, 0);
        }, 0);
        this.props.getProductDescription(this.props.match.params[1]);
        // this.props.getMsdRequest(this.props.match.params[1]);
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

  render() {
    if (this.props.loading) {
      this.showLoader();
    } else {
      this.hideLoader();
    }
    if (this.props.productDetails) {
      if (!this.props.showPiqPage) {
        return (
          <div itemScope itemType="http://schema.org/Product">
            {this.props.productDetails.seo
              ? renderMetaTags(this.props.productDetails)
              : renderMetaTagsWithoutSeoObject(this.props.productDetails)}
            {this.renderRootCategory(this.props.productDetails.rootCategory)}
          </div>
        );
      } else {
        return (
          <PiqPageForPdp
            loadingForCliqAndPiq={this.props.loadingForCliqAndPiq}
            productDetails={this.props.productDetails}
            stores={this.props.stores}
            displayToast={this.props.displayToast}
            getAllStoresForCliqAndPiq={this.props.getAllStoresForCliqAndPiq}
            removeCliqAndPiq={() => this.removeCliqAndPiq()}
            hidePdpPiqPage={this.props.hidePdpPiqPage}
          />
        );
      }
    } else {
      return this.renderLoader();
    }
  }
}
