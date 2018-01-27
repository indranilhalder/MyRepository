import React from "react";
import BannerImage from "../../general/components/BannerImage";
import Banner from "../../general/components/Banner";
import PropTypes from "prop-types";
import styles from "./HeroBanner.css";
export default class HeroBanner extends React.Component {
  renderBanner = () => {
    const feedComponentData = this.props.feedComponentData;
    if (!this.props.loading && feedComponentData.data.items) {
      return (
        <Banner>
          {feedComponentData.data.items &&
            feedComponentData.data.items.map((datum, i) => {
              return (
                <BannerImage
                  logo={datum.brandLogo}
                  title={datum.title}
                  image={datum.imageURL}
                  key={i}
                />
              );
            })}
        </Banner>
      );
    } else {
      return null;
    }
  };
  render() {
    return <div className={styles.base}>{this.renderBanner()}</div>;
  }
}
HeroBanner.PropTypes = {
  loading: PropTypes.bool,
  feedComponentData: PropTypes.shape({
    data: PropTypes.shape({
      items: PropTypes.arrayOf(
        PropTypes.shape({
          brandLogo: PropTypes.string,
          title: PropTypes.string,
          imageURL: PropTypes.string
        })
      )
    })
  })
};
