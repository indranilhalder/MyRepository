import React from "react";
import styles from "./ProductFeatures.css";
import PropTypes from "prop-types";
import Accordion from "../../general/components/Accordion.js";
export default class ProductDetails extends React.Component {
  render() {
    const data = this.props.data;
    return (
      <Accordion text="Product Details" headerFontSize={16}>
        {data.map(val => {
          return (
            <div className={styles.content}>
              <div className={styles.header}>{val.key}</div>
              <div className={styles.description}>{val.value}</div>
            </div>
          );
        })}
      </Accordion>
    );
  }
}
ProductDetails.propTypes = {
  title: PropTypes.string,
  details: PropTypes.string
};
