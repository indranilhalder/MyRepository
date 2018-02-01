import React from "react";
import styles from "./InsuranceCopy.css";
import { Icon } from "xelpmoc-core";
import propTypes from "prop-types";

export default class InsuranceCopy extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    let data = this.props.data;
    return (
      <div className={styles.base}>
        <div className={styles.insuranceHolder}>
          <div className={styles.header}>{this.props.text}</div>
          <div className={styles.textHolder}>
            <ul className={styles.ulClass}>
              {data.map((datum, i) => {
                return (
                  <li className={styles.liClass} key={i}>
                    {datum.lable}
                  </li>
                );
              })}
            </ul>
          </div>
          <div className={styles.knowMore}>
            <div
              className={styles.button}
              onClick={() => this.handleClick(console.log("click"))}
            >
              {this.props.knowMore}
            </div>
          </div>
        </div>
      </div>
    );
  }
}
InsuranceCopy.propTypes = {
  text: propTypes.string,
  label: propTypes.string,
  knowMore: propTypes.string,
  onClick: propTypes.func
};
// InsuranceCopy.defaultProps = {
//   text: 'OneAssist Accidental & Liquid Damage Protection Plan for Mobile & Tablets from Rs 8001 to Rs 12000'
// }
