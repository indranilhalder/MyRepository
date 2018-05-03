import React from "react";
import styles from "./InformationHeader.css";
import CircleButton from "../../xelpmoc-core/CircleButton";
import Icon from "../../xelpmoc-core/Icon";
import PropTypes from "prop-types";
import iconImageURL from "./img/arrowBack.svg";
import crossIcon from "./img/cancel.svg";
export default class InformationHeader extends React.Component {
  handleClick() {
    if (this.props.goBack) {
      this.props.goBack();
    }
  }

  render() {
    return (
      <div className={styles.base}>
        <div className={styles.holder}>
          <div className={styles.buttonHolder}>
            {this.props.hasBackButton && (
              <CircleButton
                color={"rgba(0,0,0,0)"}
                size={50}
                onClick={() => this.handleClick()}
                icon={<Icon image={iconImageURL} size={16} />}
              />
            )}
            {this.props.hasCrossButton && (
              <CircleButton
                color={"rgba(0,0,0,0)"}
                size={50}
                onClick={() => this.handleClick()}
                icon={<Icon image={crossIcon} size={16} />}
              />
            )}
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
  count: PropTypes.number,
  hasBackButton: PropTypes.bool,
  onClick: PropTypes.func,
  hasCrossButton: PropTypes.bool
};
InformationHeader.defaultProps = {
  hasBackButton: true,
  hasCrossButton: false
};
