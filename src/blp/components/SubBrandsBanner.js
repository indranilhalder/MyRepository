import React from "react";
import styles from "./SubBrandsBanner.css";
import ProductImageAndLogo from "./ProductImageAndLogo.js";
import Carousel from "../../general/components/Carousel";
import PropTypes from "prop-types";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
export default class SubBrandsBanner extends React.Component {
  handleClick(webURL) {
    let urlSuffix = webURL.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
    if (this.props.setClickedElementId) {
      this.props.setClickedElementId();
    }
  }
  render() {
    return (
      <div
        className={
          this.props.positionInFeed === 1 ? styles.firstItemBase : styles.base
        }
      >
        <Carousel
          header={
            this.props.feedComponentData && this.props.feedComponentData.title
          }
        >
          {this.props.feedComponentData &&
            this.props.feedComponentData.items &&
            this.props.feedComponentData.items.map &&
            this.props.feedComponentData.items.map((datum, i) => {
              return (
                <ProductImageAndLogo
                  key={i}
                  imageUrl={datum.imageURL}
                  logo={datum.brandLogo}
                  onClick={() => this.handleClick(datum.webURL)}
                />
              );
            })}
        </Carousel>
      </div>
    );
  }
}
SubBrandsBanner.propTypes = {
  feedComponentData: PropTypes.arrayOf(
    PropTypes.shape({
      title: PropTypes.string,
      items: PropTypes.arrayOf(
        PropTypes.shape({
          brandLogo: PropTypes.string,
          imageURL: PropTypes.string,
          webURL: PropTypes.string
        })
      )
    })
  )
};
