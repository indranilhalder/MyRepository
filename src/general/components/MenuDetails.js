import React from "react";
import styles from "./MenuDetails.css";
import PropTypes from "prop-types";
import { Collapse } from "react-collapse";
import { Icon } from "xelpmoc-core";
import couponIcon from "./img/credit-card.svg";

export default class ManueDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isOpen: false
    };
  }
  openMenu() {
    this.setState(prevState => ({
      isOpen: !prevState.isOpen
    }));
  }
  render() {
    let iconActive = styles.icon;
    if (this.state.isOpen) {
      iconActive = styles.iconup;
    }
    return (
      <div className={styles.base}>
        <div
          className={styles.holder}
          onClick={() => {
            this.openMenu();
          }}
        >
          <div className={styles.debitCardIcon}>
            <Icon image={this.props.icon} size={25} />
          </div>
          <div className={styles.textBox}>
            {this.props.text}
            <div className={iconActive} />
          </div>
        </div>
        <Collapse isOpened={this.state.isOpen}>{this.props.children}</Collapse>
      </div>
    );
  }
}
ManueDetails.propTypes = {
  text: PropTypes.string,
  icon: PropTypes.string
};

ManueDetails.defaultProps = {
  icon: couponIcon
};
