import React from "react";
import styles from "./SizeGuideElement.css";
import propTypes from "prop-types";
export default class SizeGuideElement extends React.Component {
  render() {
    const data = this.props.data;
    return (
      <div className={styles.base}>
        {data.map((datum, i) => {
          return (
            <div className={styles.sizeBox} key={i}>
              <div className={styles.sizeText}>{datum.dimensionSize}</div>
              <div className={styles.sizeNumber}>
                {datum.dimensionValue}
                {datum.dimensionUnit}
              </div>
            </div>
          );
        })}
      </div>
    );
  }
}
SizeGuideElement.propTypes = {
  key: propTypes.string,
  value: propTypes.number
};
