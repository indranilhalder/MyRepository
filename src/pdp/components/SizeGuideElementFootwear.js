import React from "react";
import styles from "./SizeGuideElementFootwear.css";
import propTypes from "prop-types";
export default class SizeGuideElement extends React.Component {
  render() {
    const data = this.props.data;
    return (
      <div className={styles.base}>
        <div className={styles.uk}>
          <div className={styles.header}>IND/UK</div>
          <div className={styles.dimensionValue}>
            {data.map((datum, i) => {
              return (
                <div className={styles.dimensionValueList}>
                  {datum.dimensionSize}
                </div>
              );
            })}
          </div>
        </div>
        <div className={styles.us}>
          <div className={styles.header}>US</div>
          <div className={styles.dimensionValue}>
            {data.map((datum, i) => {
              return (
                <div className={styles.dimensionValueList}>{datum.usSize}</div>
              );
            })}
          </div>
        </div>
        <div className={styles.uk}>
          <div className={styles.header}>EURO</div>
          <div className={styles.dimensionValue}>
            {data.map((datum, i) => {
              return (
                <div className={styles.dimensionValueList}>
                  {datum.euroSize}
                </div>
              );
            })}
          </div>
        </div>
        <div className={styles.us}>
          <div className={styles.header}>FOOT LENGTH (CM)</div>
          <div className={styles.dimensionValues}>
            {data.map((datum, i) => {
              return (
                <div className={styles.dimensionValueList}>
                  {datum.footlength}
                </div>
              );
            })}
          </div>
        </div>
      </div>
    );
  }
}
SizeGuideElement.propTypes = {
  key: propTypes.string,
  value: propTypes.number
};
