import React from "react";
import styles from "./SizeGuideElementBelt.css";
import propTypes from "prop-types";
export default class SizeGuideElementBelt extends React.Component {
  render() {
    const data = this.props.data;
    return (
      <div className={styles.base}>
        <div className={styles.grey}>
          <div className={styles.header}>SIZE</div>
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
        <div className={styles.white}>
          <div className={styles.header}>BELT SIZE (CM)</div>
          <div className={styles.dimensionValue}>
            {data.map((datum, i) => {
              return (
                <div className={styles.dimensionValueList}>
                  {datum.cmsBeltSize}
                </div>
              );
            })}
          </div>
        </div>
        <div className={styles.grey}>
          <div className={styles.header}>BELT SIZE (INCH)</div>
          <div className={styles.dimensionValue}>
            {data.map((datum, i) => {
              return (
                <div className={styles.dimensionValueList}>
                  {datum.inchesBeltSize}
                </div>
              );
            })}
          </div>
        </div>
        <div className={styles.white}>
          <div className={styles.header}>WAIST SIZE (INCH)</div>
          <div className={styles.dimensionValue}>
            {data.map((datum, i) => {
              return (
                <div className={styles.dimensionValueList}>
                  {datum.inchesWaistSize}
                </div>
              );
            })}
          </div>
        </div>
        <div className={styles.grey}>
          <div className={styles.header}>BELT LENGTH (INCH)</div>
          <div className={styles.dimensionValue}>
            {data.map((datum, i) => {
              return (
                <div className={styles.dimensionValueList}>
                  {datum.inchesBeltLength}
                </div>
              );
            })}
          </div>
        </div>
      </div>
    );
  }
}
SizeGuideElementBelt.propTypes = {
  key: propTypes.string,
  value: propTypes.number
};
