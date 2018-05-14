import React from "react";
import BannerImage from "../../general/components/BannerImage";
import Banner from "../../general/components/Banner";
import PropTypes from "prop-types";
import HeroBannerSkeleton from "../../general/components/HeroBannerSkeleton";
import HomeSkeleton from "../../general/components/HomeSkeleton.js";
import styles from "./HeroBanner.css";
export default class HeroBanner extends React.Component {
  renderBanner = () => {
    const { feedComponentData, ...rest } = this.props;
    if (!this.props.loading) {
      if (feedComponentData.items && feedComponentData.items.length > 1) {
        return (
          <Banner>
            {feedComponentData.items &&
              feedComponentData.items.map &&
              feedComponentData.items.map((datum, i) => {
                return (
                  <BannerImage
                    logo={datum.brandLogo}
                    title={datum.title}
                    subTitle={datum.subTitle}
                    image={datum.imageURL}
                    key={i}
                    url={datum.webURL}
                    {...rest}
                  />
                );
              })}
          </Banner>
        );
      } else {
        return (
          <React.Fragment>
            {feedComponentData.items &&
              feedComponentData.items.map &&
              feedComponentData.items.map((datum, i) => {
                return (
                  <BannerImage
                    logo={datum.brandLogo}
                    title={datum.title}
                    image={datum.imageURL}
                    subTitle={datum.subTitle}
                    key={i}
                    url={datum.webURL}
                    {...rest}
                  />
                );
              })}
          </React.Fragment>
        );
      }
    } else {
      return <HomeSkeleton />;
    }
  };
  render() {
    return <div className={styles.base}>{this.renderBanner()}</div>;
  }
}
HeroBanner.propTypes = {
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
