import React from "react";
import Accordion from "../../general/components/Accordion";
import PropTypes from "prop-types";
import styles from "./ProductFeatures.css";
export default class PriceBreakUp extends React.Component {
  render() {
    return (
      <Accordion
        text="Price breakup"
        headerFontSize={16}
        isOpen={this.props.isOpen}
      >
        <div className={styles.holder}>
          {this.props.data.map(val => {
            return (
              <div className={styles.content}>
                <div className={styles.header}>{val.name}</div>
                {val.price && (
                  <div className={styles.description}>
                    {val.price.formattedValue}
                  </div>
                )}
              </div>
            );
          })}
        </div>
      </Accordion>
    );
  }
}
PriceBreakUp.propTypes = {
  isOpen: PropTypes.bool,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      name: PropTypes.string,
      price: PropTypes.shape({ formattedValue: PropTypes.string })
    })
  )
};
