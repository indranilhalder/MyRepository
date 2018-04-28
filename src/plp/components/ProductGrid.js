import React from "react";
import Grid from "../../general/components/Grid";
import ProductModule from "../../general/components/ProductModule";
import IconicFilter from "./IconicFilter";
import PlpAds from "./PlpAds";
import PlpComponent from "./PlpComponent";
import Icon from "../../xelpmoc-core/Icon";
import styles from "./ProductGrid.css";
import gridImage from "./img/grid.svg";
import listImage from "./img/list.svg";
import {
  PRODUCT_DESCRIPTION_ROUTER,
  IS_OFFER_EXISTING
} from "../../lib/constants";
import { setDataLayerForPlpDirectCalls } from "../../lib/adobeUtils";
const LIST = "list";
const GRID = "grid";
const PRODUCT = "product";
export const PLPAD = "plpAd";
export const ICONICFILTER = "iconicFilter";
export default class ProductGrid extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      view: GRID
    };
  }

  switchView() {
    if (this.state.view === LIST) {
      this.setState({ view: GRID });
    } else {
      this.setState({ view: LIST });
    }
  }
  changeAddress() {
    if (this.props.changeAddress) {
      this.props.changeAddress();
    }
  }

  goToProductDescription = (url, productObj) => {
    setDataLayerForPlpDirectCalls(productObj);
    this.props.history.push(url);
  };

  renderComponent = data => {
    // if (data.type === PRODUCT) {
    console.log(data);
    return (
      <ProductModule
        productImage={data.imageURL}
        title={data.brandname}
        price={data.price.mrpPrice.formattedValueNoDecimal}
        discountPrice={data.price.sellingPrice.formattedValueNoDecimal}
        description={data.productname}
        discountPercent={data.discountPercent}
        isOfferExisting={data.isOfferExisting}
        onlineExclusive={data.onlineExclusive}
        outOfStock={!data.cumulativeStockLevel}
        onOffer={data.isOfferExisting}
        newProduct={data.newProduct}
        averageRating={data.averageRating}
        totalNoOfReviews={data.totalNoOfReviews}
        view={this.state.view}
        onClick={url => this.goToProductDescription(url, data)}
        productCategory={data.productCategoryType}
        productId={data.productId}
        showWishListButton={false}
      />
    );
    // } else if (data.type === PLPAD) {
    //   return <PlpAds imageURL={data.imageURL} />;
    // } else if (data.type === ICONICFILTER) {
    //   return <IconicFilter data={data.filterValue} title={data.filterTitle} />;
    // } else {
    //   return null;
    // }
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          <div className={styles.product}>
            {this.props.totalResults ? this.props.totalResults : 0} Products
          </div>

          {/* <div className={styles.area}>{this.props.area}</div> */}
          {/* <div
            className={styles.areaChange}
            onClick={() => this.changeAddress()}
          >
            Change
          </div> */}
          <div className={styles.icon} onClick={() => this.switchView()}>
            {this.state.view === LIST && <Icon image={gridImage} size={20} />}
            {this.state.view === GRID && <Icon image={listImage} size={20} />}
          </div>
        </div>
        <div className={styles.content}>
          <Grid
            search={this.props.search}
            offset={0}
            elementWidthMobile={this.state.view === LIST ? 100 : 50}
          >
            {this.props.data &&
              this.props.data.map((datum, i) => {
                // if (
                //   datum.type === PRODUCT ||
                //   datum.type === PLPAD ||
                //   datum.type === ICONICFILTER
                // ) {
                let widthMobile = false;
                // if (datum.type === PLPAD || datum.type === ICONICFILTER) {
                //   widthMobile = 100;
                // }
                return (
                  <PlpComponent
                    key={i}
                    gridWidthMobile={widthMobile}
                    view={this.state.view}
                    type={datum && datum.type}
                  >
                    {this.renderComponent(datum)}
                  </PlpComponent>
                );
                // } else {
                //   return null;
                // }
              })}
          </Grid>
        </div>
      </div>
    );
  }
}

ProductGrid.defaultProps = {
  area: "Delhi - 560345"
};
