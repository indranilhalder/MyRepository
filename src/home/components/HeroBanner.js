import React from "react";
import BannerImage from "../../general/components/BannerImage";
import Banner from "../../general/components/Banner";
import PropTypes, { instanceOf } from "prop-types";
import styles from "./HeroBanner.css";
export default class HeroBanner extends React.Component {
  renderBanner = () => {
    const { feedComponentData, ...rest } = this.props;
    if (!this.props.loading) {
      if (feedComponentData.items.length > 1) {
        return (
          <Banner>
            {feedComponentData.items &&
              feedComponentData.items.map &&
              feedComponentData.items.map((datum, i) => {
                return (
                  <BannerImage
                    logo={datum.brandLogo}
                    title={datum.title}
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
      return <div className={styles.dummy}>HERO BANNER GOES HERE</div>;
    }
  };
  render() {
    if (
      !this.props.loading &&
      this.props.feedComponentData.items &&
      this.props.feedComponentData.items.length === 0
    ) {
      return <div className={styles.dummy}>HERO BANNER GOES HERE</div>;
    }
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
