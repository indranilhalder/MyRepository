import React from "react";
import styles from "./BrandsType.css";
import PropTypes from "prop-types";
import Accordion from "../../general/components/Accordion.js";
export default class BrandsType extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <Accordion text={this.props.title}>
          {this.props.children && (
            <div className={styles.subListHolder}>{this.props.children}</div>
          )}
        </Accordion>
      </div>
    );
  }
}
BrandsType.propTypes = {
  title: PropTypes.string
};
