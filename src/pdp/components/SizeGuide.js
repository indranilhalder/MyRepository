import React from "react";
import styles from "./SizeGuide.css";
import propTypes from "prop-types";
export default class SizeGuide extends React.Component {
  render() {
    const data = this.props.data;
    return (
      <div className={styles.base}>
        {data.map((datum, i) => {
          return (
            <div className={styles.sizeBox} key={i}>
              <div className={styles.sizeText}>{datum.dimensionType}</div>
              <div className={styles.sizeNumber} value={datum.value}>
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
SizeGuide.propTypes = {
  key: propTypes.string,
  value: propTypes.number
};
