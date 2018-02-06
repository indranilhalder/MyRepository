import React from "react";
import styles from "./SizeGuide.css";
import propTypes from "prop-types";
export default class SizeGuide extends React.Component {
  render() {
    const data = this.props.data;
    console.log(data);
    return (
      <div className={styles.base}>
        <div className={styles.holder}>
          {data.map((datum, i) => {
            return (
              <div className={styles.sizeBox} key={i}>
                <div className={styles.sizeText} key={datum.key}>
                  {datum.key}
                </div>
                <div className={styles.sizeNumber} value={datum.value}>
                  {datum.value}
                </div>
              </div>
            );
          })}
        </div>
      </div>
    );
  }
}
SizeGuide.propTypes = {
  key: propTypes.string,
  value: propTypes.number
};
