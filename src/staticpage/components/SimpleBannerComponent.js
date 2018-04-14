import React from "react";
import styles from "./SimpleBannerComponent.css";
import PropTypes from "prop-types";
import Image from "../../xelpmoc-core/Image";
export default class SimpleBannerComponent extends React.Component {
  handleClick(urlLink) {
    this.props.history.push(urlLink);
  }
  render() {
    return (
      <div
        className={styles.base}
        onClick={() => this.handleClick(this.props.urlLink)}
      >
        <div className={styles.imageHolder}>
          <Image image={this.props.media} fit="cover" />
          {this.props.title && (
            <div className={styles.displayTitle}>{this.props.title}</div>
          )}
        </div>
      </div>
    );
  }
}
SimpleBannerComponent.propTypes = {
  media: PropTypes.string,
  urlLink: PropTypes.string,
  title: PropTypes.string
};
