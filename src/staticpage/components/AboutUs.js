import React from "react";
import styles from "./AboutUs.css";
import PropTypes from "prop-types";
import SimpleBannerComponent from "./SimpleBannerComponent.js";
import { ABOUTUS } from "../../lib/constants";
export default class AboutUs extends React.Component {
  componentDidMount() {
    this.props.getAboutUsDetails();
  }
  componentDidUpdate() {
    this.props.setHeaderText(ABOUTUS);
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.aboutUsDataHolder}>
          {this.props.aboutUs &&
            this.props.aboutUs.items &&
            this.props.aboutUs.items.map((val, i) => {
              return (
                <div className={styles.dataHolder}>
                  {val.componentName === "simpleBannerComponent" &&
                    val.simpleBannerComponent && (
                      <div className={styles.bannerHolder}>
                        <SimpleBannerComponent
                          media={val.simpleBannerComponent.media}
                          title={val.simpleBannerComponent.title}
                        />
                      </div>
                    )}
                  {val.componentName === "cmsParagraphComponent" &&
                    val.cmsParagraphComponent && (
                      <div
                        className={styles.contentHolder}
                        dangerouslySetInnerHTML={{
                          __html: val.cmsParagraphComponent.content
                        }}
                      />
                    )}
                </div>
              );
            })}
        </div>
      </div>
    );
  }
}
AboutUs.propTypes = {
  items: PropTypes.arrayOf(
    PropTypes.shape({
      componentName: PropTypes.string,
      simpleBannerComponent: PropTypes.shape({
        description: PropTypes.string,
        media: PropTypes.string,
        title: PropTypes.string,
        urlLink: PropTypes.string
      }),
      cmsParagraphComponent: PropTypes.shape({
        content: PropTypes.string,
        componentName: PropTypes.string
      })
    })
  )
};
