import React from "react";
import styles from "./InformationHeader.css";
import { CircleButton, Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
import iconImageURL from "./img/arrowBack.svg";
export default class InformationHeader extends React.Component {
  handleClick() {
    if (this.props.onClick) {
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.holder}>
          <div className={styles.buttonHolder}>
            <CircleButton
              color={"rgba(0,0,0,0)"}
              size={50}
              onClick={() => this.handleClick()}
              icon={<Icon image={iconImageURL} size={22} />}
            />
          </div>
          <div className={styles.textBox}>
            {this.props.text}
            {this.props.count && (
              <span className={styles.span}>({this.props.count})</span>
            )}
          </div>
        </div>
      </div>
    );
  }
}
InformationHeader.propTypes = {
  text: PropTypes.string,
  count: PropTypes.number
};
