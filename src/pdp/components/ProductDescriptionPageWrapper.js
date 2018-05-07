import React from "react";
import PdpElectronics from "./PdpElectronics";
import PdpApparel from "./PdpApparel";
import PdpJewellery from "./PdpJewellery";
import PdpHome from "./PdpHome";
import PiqPage from "../../cart/components/PiqPage";
import InformationHeader from "../../general/components/InformationHeader";
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
  constructor(props) {
    super(props);
    this.state = { showPiqPage: false };
  }
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
    if (this.props.productDetails && this.props.productDetails !== "null") {
      this.props.setHeaderText(this.props.productDetails.productName);
    }

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

  renderCliqAndPiq() {
    // let currentSelectedProduct = this.props.cart.cartDetailsCNC.products.find(
    //   product => {
    //     return product.USSID === this.state.selectedProductsUssIdForCliqAndPiq;
    //   }
    // );
    console.log(this.props.getAllStoresForCliqAndPiq);
    const firstSlaveData = this.props.productDetails.slaveData;

    console.log(firstSlaveData);
    const someData = firstSlaveData
      .map(slaves => {
        return (
          slaves.CNCServiceableSlavesData &&
          slaves.CNCServiceableSlavesData.map(slave => {
            return (
              slave &&
              slave.serviceableSlaves.map(serviceableSlave => {
                return serviceableSlave;
              })
            );
          })
        );
      })
      .map(val => {
        return (
          val &&
          val.map(v => {
            return v;
          })
        );
      });

    const allStoreIds = [].concat
      .apply([], [].concat.apply([], someData))
      .map(store => {
        return store && store.slaveId;
      });
    const availableStores = this.props.stores
      ? this.props.stores.filter(val => {
          return allStoreIds.includes(val.slaveId);
        })
      : [];
    console.log(availableStores);
    //console.log(availableStores[0].geoPoint);
    return (
      <div className={styles.piqPageHolder}>
        <div className={styles.piqHeaderHolder}>
          <InformationHeader
            goBack={() => {
              this.props.hidePdpPiqPage();
            }}
            text="CLiQ & PiQ"
          />
        </div>
        <PiqPage
          availableStores={availableStores}
          selectedSlaveId={this.state.selectedSlaveId}
          numberOfStores={availableStores.length}
          showPickupPerson={false}
          productName={this.props.productDetails.productName}
          // initailLatitude={availableStores[0].geoPoint.latitude}
          // initialLongitude={availableStores[0].geoPoint.longitude}
          // hidePickupPersonDetail={() => this.togglePickupPersonForm()}
          //addStoreCNC={slavesId => this.addStoreCNC(slavesId)}
          // addPickupPersonCNC={(mobile, name) =>
          //   this.addPickupPersonCNC(mobile, name, currentSelectedProduct)
          // }
          canSelectStore={false}
          changePincode={pincode =>
            this.props.getAllStoresForCliqAndPiq(pincode)
          }
          goBack={() => this.removeCliqAndPiq()}
          // getUserDetails={() => this.getUserDetails()}
          // userDetails={this.props.userDetails}
        />
      </div>
    );
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
    console.log(this.props);
    if (this.props.loading) {
      this.showLoader();
    } else {
      this.hideLoader();
    }
    if (this.props.productDetails) {
      if (!this.props.showPiqPage) {
        return (
          <div itemscope itemtype="http://schema.org/Product">
            {this.props.productDetails.seo
              ? renderMetaTags(this.props.productDetails)
              : renderMetaTagsWithoutSeoObject(this.props.productDetails)}
            {this.renderRootCategory(this.props.productDetails.rootCategory)}
          </div>
        );
      } else {
        return this.renderCliqAndPiq();
      }
    } else {
      return this.renderLoader();
    }
  }
}
