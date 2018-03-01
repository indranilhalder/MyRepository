import React from "react";
import PropTypes from "prop-types";
import Accordion from "../../general/components/Accordion.js";
import styles from "./ProductFeature.css";
export default class ProductFeatures extends React.Component {
  render() {
    const data = this.props.data;
    return (
      <div className={styles.base}>
        {data.map((datum, i) => {
          return (
            <Accordion data={data} key={i} text={datum.title}>
              {datum.map(val => {
                return (
                  <div className={styles.content}>
                    <div className={styles.header}>{datum.feature}</div>
                    <div className={styles.header}>
                      {datum.featureDescription}
                    </div>
                  </div>
                );
              })}
            </Accordion>
          );
        })}
      </div>
    );
  }
}
ProductFeatures.propTypes = {
  title: PropTypes.string,
  details: PropTypes.string
};
