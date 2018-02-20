import React from "react";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import styles from "./SavedCard.css";
export default class SavedCard extends React.Component {
  onChangeCvv(val) {
    if (this.props.onChangeCvv) {
      this.props.onChangeCvv(val);
    }
  }
  render() {
    let classCircle = styles.circleDesign;
    return (
      <div className={styles.base}>
        <div className={styles.cardNumber}>
          <div className={classCircle} />
          <div className={classCircle} />
          <div className={classCircle} />
          <div className={classCircle} />
          <div className={styles.circleTransparent} />
          <div className={classCircle} />
          <div className={classCircle} />
          <div className={classCircle} />
          <div className={classCircle} />
          <div className={styles.circleTransparent} />
          <div className={classCircle} />
          <div className={classCircle} />
          <div className={classCircle} />
          <div className={classCircle} />
          <div className={styles.cardLastFourDigit}>
            {this.props.cardNumber}
          </div>
        </div>
        <div className={styles.cardNameAndCvv}>
          <div className={styles.cardsSection}>
            <div className={styles.cardIconHolder}>
              <Icon image={this.props.cardImage} size={40} />
            </div>
          </div>
          <div className={styles.cvvInput}>
            <Input2
              placeholder="Cvv"
              height={33}
              type="number"
              textStyle={{
                color: "#000",
                fontSize: 13
              }}
              onChange={val => this.onChangeCvv(val)}
            />
          </div>
        </div>
      </div>
    );
  }
}
SavedCard.propTypes = {
  onChangeCvv: PropTypes.func,
  cardImage: PropTypes.string,
  cardNumber: PropTypes.string
};
