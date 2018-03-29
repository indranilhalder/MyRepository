import React from "react";
import Carousel from "../../general/components/Carousel";
import ProductCapsuleCircle from "../../general/components/ProductCapsuleCircle";
import PropTypes from "prop-types";
import styles from "./ProductCapsules.css";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import MDSpinner from "react-md-spinner";
export default class ProductCapsules extends React.Component {
  handleClick() {
    const urlSuffix = this.props.feedComponentData.webURL.replace(
      TATA_CLIQ_ROOT,
      "$1"
    );
    this.props.history.push(urlSuffix);
  }

  componentDidMount() {
    this.props.getProductCapsules(this.props.positionInFeed);
  }

  render() {
    if (this.props.productCapsulesLoading) {
      return <MDSpinner />;
    }

    if (!this.props.feedComponentData) {
      return null;
    }

    const productCapsulesData = this.props.feedComponentData;
    const data = this.props.feedComponentData.data;
    let subHeader;
    console.log("DATA");
    console.log(data);
    if (data && data.wishlistData) {
      subHeader = `You have ${
        data.wishlistData[0].items.length
      } products in your list`;
    }

    return (
      <div className={styles.base}>
        <Carousel
          header={productCapsulesData.title}
          subheader={subHeader}
          buttonText={productCapsulesData.btnText}
          seeAll={() => this.handleClick()}
          elementWidthMobile={30}
          withFooter={false}
        >
          {this.props.feedComponentData.data &&
            this.props.feedComponentData.data.wishlistData[0] &&
            this.props.feedComponentData.data.wishlistData[0].items &&
            this.props.feedComponentData.data.wishlistData[0].items.map(
              (datum, i) => {
                return (
                  <ProductCapsuleCircle
                    image={datum.imageURL}
                    label={datum.label}
                    key={i}
                  />
                );
              }
            )}
        </Carousel>
      </div>
    );
  }
}
ProductCapsules.propTypes = {
  header: PropTypes.string
};
ProductCapsules.defaultProps = {
  header: "Saved products"
};
