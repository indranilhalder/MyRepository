import React from "react";
import styles from "./PaymentMethod.css";
import PropTypes from "prop-types";
import ManueDetails from "./ManueDetails.js";

export default class PaymentMethod extends React.Component {
  render() {
    const data = this.props.data;
    return (
      <div className={styles.base}>
        {data.map((datum, i) => {
          return <ManueDetails data={data} key={i} text={datum.title} />;
        })}
      </div>
    );
  }
}
PaymentMethod.propTypes = {
  title: PropTypes.string
};
