import React from "react";
import styles from "./InformationHeader.css";
// import { Button } from "xelpmoc-core";
import { CircleButton, Icon } from "xelpmoc-core";
import propTypes from "prop-types";
import iconImageURL from "./img/makefg.png";

export default class InformationHeader extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
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
              onClick={() => this.handleClick(console.log("check count"))}
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
  text: propTypes.string,
  iconImageURL: propTypes.string,
  count: propTypes.number
};
InformationHeader.defaultProps = {
  text: "Sort by",
  iconImageURL: iconImageURL,
  count: 49
};
