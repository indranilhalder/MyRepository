import React from "react";
import styles from "./TextWithUnderLine.css";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";

export default class TextWithUnderLine extends React.Component {
  onClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }

  render() {
    return (
      <div className={styles.base}>
        {this.props.borderBox === false ? (
          <div className={styles.textBorder}>
            <div className={styles.headingText}>{this.props.heading}</div>
          </div>
        ) : (
          <div className={styles.headingText}>{this.props.heading}</div>
        )}
        <div className={styles.button} onClick={() => this.onClick()}>
          <UnderLinedButton label={this.props.buttonLabel} />
        </div>
      </div>
    );
  }
}
TextWithUnderLine.propTypes = {
  onClick: PropTypes.func,
  buttonLabel: PropTypes.string,
  heading: PropTypes.string
};
