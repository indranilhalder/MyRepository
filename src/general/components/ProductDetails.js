import React from "react";
import styles from "./ProductDetails.css";
import PropTypes from "prop-types";
import Accordion from "./Accordion.js";
export default class ProductDetails extends React.Component {
  render() {
    const data = this.props.data;
    return (
      <div className={styles.base}>
        {data.map((datum, i) => {
          return (
            <Accordion data={data} key={i} text={datum.title}>
              <div className={styles.productDetails}>
                <div className={styles.content}>{datum.details}</div>
              </div>
            </Accordion>
          );
        })}
      </div>
    );
  }
}
ProductDetails.propTypes = {
  title: PropTypes.string,
  details: PropTypes.string
};
