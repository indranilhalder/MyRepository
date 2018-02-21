import React from "react";
import CheckBox from "../../general/components/CheckBox";
import EmiCard from "../../pdp/components/EmiCard";
import styles from "./EmiAccordian.css";
export default class EmiAccordian extends React.Component {
  render() {
    return (
      <div className={this.props.selected ? styles.baseSelected : styles.base}>
        <div className={styles.header}>
          {this.props.title}
          <div className={styles.check}>
            <CheckBox />
          </div>
        </div>
        {/* {this.props.selected && <EmiCard options={this.props.options} />} */}
        <EmiCard options={this.props.options} />
      </div>
    );
  }
}
