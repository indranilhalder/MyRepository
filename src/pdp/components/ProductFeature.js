import React from "react";
import PropTypes from "prop-types";
import Accordion from "../../general/components/Accordion.js";
import styles from "./ProductFeatures.css";
export default class ProductFeature extends React.Component {
  render() {
    return (
      <Accordion
        text={this.props.heading}
        headerFontSize={16}
        isOpen={this.props.isOpen}
      >
        <div className={styles.holder}>
          <div className={styles.content}>
            <div className={styles.description}>{this.props.content}</div>
          </div>
        </div>
      </Accordion>
    );
  }
}
ProductFeature.propTypes = {
  isOpen: PropTypes.bool,
  heading: PropTypes.string,
  content: PropTypes.string
};
