import React from "react";
import PropTypes from "prop-types";
import Accordion from "../../general/components/Accordion.js";
import styles from "./ProductFeatures.css";
export default class ProductFeatures extends React.Component {
  render() {
    const data = this.props.data;
    return (
      <div className={styles.base}>
        {this.props.features.map((datum, i) => {
          return (
            <Accordion
              data={data}
              key={i}
              text={datum.title}
              headerFontSize={16}
            >
              <div className={styles.holder}>
                {datum.features.map(val => {
                  return (
                    <div className={styles.content}>
                      <div className={styles.header}>{val.feature}</div>
                      <div className={styles.description}>
                        {val.description}
                      </div>
                    </div>
                  );
                })}
              </div>
            </Accordion>
          );
        })}
      </div>
    );
  }
}
ProductFeatures.propTypes = {
  features: PropTypes.arrayOf(
    PropTypes.shape({
      feature: PropTypes.string,
      description: PropTypes.string
    })
  )
};
