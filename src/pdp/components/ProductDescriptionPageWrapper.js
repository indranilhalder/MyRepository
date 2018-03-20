import React from "react";
import PdpElectronics from "./PdpElectronics";
import PdpApparel from "./PdpApparel";
import PdpJewellery from "./PdpJewellery";
import styles from "./ProductDescriptionPageWrapper.css";
import ProductDescriptionPage from "./ProductDescriptionPage";
import MDSpinner from "react-md-spinner";
import {
  PRODUCT_DESCRIPTION_PRODUCT_CODE,
  PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE,
  UPDATE_PDP_REDUCER_FOR_DELIVERY_OPTION,
  DEFAULT_PIN_CODE_LOCAL_STORAGE
} from "../../lib/constants";
// prettier-ignore
const typeComponentMapping = {
  "Electronics": props => <PdpElectronics {...props} />,
  "FashionJewellery":props => <PdpJewellery {...props} />,
  "Clothing":props => <PdpApparel {...props} />
};

const defaultPinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);

export default class ProductDescriptionPageWrapper extends React.Component {
  componentDidMount() {
    if (this.props.match.path === PRODUCT_DESCRIPTION_PRODUCT_CODE) {
      this.props.getProductDescription(this.props.match.params[1]);
      this.props.getMsdRequest(this.props.match.params[1]);
      this.props.pdpAboutBrand(this.props.match.params[1]);
      if (defaultPinCode) {
        this.props.getProductPinCode(
          defaultPinCode,
          this.props.match.params[1]
        );
      }
    } else if (
      this.props.match.path === PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE
    ) {
      this.props.getProductDescription(this.props.match.params[2]);
      this.props.getMsdRequest(this.props.match.params[2]);
      this.props.pdpAboutBrand(this.props.match.params[2]);
      if (defaultPinCode) {
        this.props.getProductPinCode(
          defaultPinCode,
          this.props.match.params[2]
        );
      }
    } else {
      //need to show error page
    }
  }

  componentDidUpdate(prevProps, prevState) {
    if (prevProps.location.pathname !== this.props.location.pathname) {
      if (this.props.match.path === PRODUCT_DESCRIPTION_PRODUCT_CODE) {
        this.props.getProductDescription(this.props.match.params[1]);
        this.props.getMsdRequest(this.props.match.params[1]);
        if (defaultPinCode) {
          this.props.getProductPinCode(
            defaultPinCode,
            this.props.match.params[1]
          );
        }
      } else if (
        this.props.match.path === PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE
      ) {
        this.props.getProductDescription(this.props.match.params[2]);
        this.props.getMsdRequest(this.props.match.params[2]);
        if (defaultPinCode) {
          this.props.getProductPinCode(
            defaultPinCode,
            this.props.match.params[2]
          );
        }
      } else {
        //need to show error page
      }
    }
  }

  renderRootCategory = datumType => {
    return (
      <React.Fragment>
        {typeComponentMapping[datumType] &&
          typeComponentMapping[datumType]({ ...this.props })}
      </React.Fragment>
    );
  };
  renderLoader() {
    return (
      <div className={styles.loadingIndicator}>
        <MDSpinner />
      </div>
    );
  }

  render() {
    if (!this.props.loading && this.props.productDetails) {
      return (
        <div>
          {this.renderRootCategory(this.props.productDetails.rootCategory)}
        </div>
      );
    } else {
      return this.renderLoader();
    }
  }
}
