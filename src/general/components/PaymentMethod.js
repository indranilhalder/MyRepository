import React from "react";
import styles from "./PaymentMethod.css";
import PropTypes from "prop-types";
import MenuDetails from "./MenuDetails.js";
import CreditCard from "./CreditCard.js";

export default class PaymentMethod extends React.Component {
  render() {
    const data = this.props.data;
    return (
      <div className={styles.base}>
        {data.map((datum, i) => {
          return (
            <MenuDetails data={data} key={i} text={datum.title}>
              <CreditCard />
            </MenuDetails>
          );
        })}
      </div>
    );
  }
}
PaymentMethod.propTypes = {
  title: PropTypes.string
};
