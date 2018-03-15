import React from "react";
import PropTypes from "prop-types";
import Accordian from "../../general/components/Accordion";
import styles from "./CategoryL2.css";
export default class CategoryL2 extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick(this.props.url);
    }
  }
  render() {
    return (
      <div className={this.props.base}>
        {this.props.children && (
          <Accordian text={this.props.label} headerFontSize={16}>
            <div className={styles.base}>{this.props.children}</div>
          </Accordian>
        )}
        {!this.props.children && (
          <div
            className={styles.link}
            onClick={() => {
              this.handleClick();
            }}
          >
            {this.props.label}
            <div className={styles.icon} />
          </div>
        )}
      </div>
    );
  }
}
CategoryL2.propTypes = {
  label: PropTypes.string,
  onClick: PropTypes.func,
  url: PropTypes.string
};
