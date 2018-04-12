import React from "react";
import styles from "./AboutUs.css";
import PropTypes from "prop-types";
import SimpleBannerComponent from "./SimpleBannerComponent.js";
export default class AboutUs extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.aboutUsDataHolder}>
          {this.props.items &&
            this.props.items.map((val, i) => {
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
