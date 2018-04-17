import React from "react";
import styles from "./NoCostEmi.css";
import PropTypes from "prop-types";
import { Collapse } from "react-collapse";
export default class NoCostEmi extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isOpen: false
    };
  }

  openMenu() {
    this.setState(
      prevState => ({
        isOpen: !prevState.isOpen
      }),
      () => {
        if (this.props.onOpenMenu) {
          this.props.onOpenMenu(this.state.isOpen);
        }
      }
    );
  }

  render() {
    let rotateIcon = styles.iconHolder;
    if (this.state.isOpen) {
      rotateIcon = styles.rot;
    }
    return (
      <div className={styles.base}>
        <div
          className={styles.holder}
          onClick={() => {
            this.openMenu();
          }}
        >
          <div className={rotateIcon} />
          <div className={styles.textHolder}>{this.props.text}</div>
        </div>
        <Collapse isOpened={this.state.isOpen}>{this.props.children}</Collapse>
      </div>
    );
  }
}
NoCostEmi.propTypes = {
  text: PropTypes.string,
  onOpenMenu: PropTypes.func
};
