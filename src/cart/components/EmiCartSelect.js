import React from "react";
import CheckBox from "../../general/components/CheckBox";
import Button from "../../general/components/Button";
import EmiCard from "../../pdp/components/EmiCard";
import styles from "./EmiCartSelect.css";
import sortBy from "lodash.sortby";
export default class EmiAccordian extends React.Component {
  handleClick() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  handlePlanSelect(val) {
    if (this.props.selectPlan) {
      this.props.selectPlan(val);
    }
  }
  handleConfirmPlan() {
    if (this.props.confirmPlan) {
      this.props.confirmPlan();
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
              <EmiCard
                options={
                  this.props.options &&
                  sortBy(this.props.options, bank => {
                    return parseInt(bank.term, 10);
                  })
                }
                onChange={val => this.handlePlanSelect(val)}
              />
            </div>
            <div className={styles.button}>
              <Button
                type="secondary"
                label="Select this plan"
                onClick={() => this.handleConfirmPlan()}
              />
            </div>
          </React.Fragment>
        )}
      </div>
    );
  }
}
