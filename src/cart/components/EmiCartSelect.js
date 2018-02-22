import React from "react";
import CheckBox from "../../general/components/CheckBox";
import Button from "../../general/components/Button";
import EmiCard from "../../pdp/components/EmiCard";
import styles from "./EmiCartSelect.css";
export default class EmiAccordian extends React.Component {
  handleClick() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    return (
      <div className={this.props.selected ? styles.baseSelected : styles.base}>
        <div className={styles.header} onClick={() => this.handleClick()}>
          {this.props.title}
          <div className={styles.checkHolder}>
            <CheckBox selected={this.props.selected} />
          </div>
        </div>
        {this.props.selected && (
          <React.Fragment>
            <div className={styles.options}>
              <EmiCard options={this.props.options} />
            </div>
            <div className={styles.button}>
              <Button type="secondary" label="Select this plan" />
            </div>
          </React.Fragment>
        )}
      </div>
    );
  }
}
