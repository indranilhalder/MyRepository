import React from "react";
import styles from "./Accordion.css";
import PropTypes from "prop-types";
import { Collapse } from "react-collapse";
import iconImageURL from "./img/makefg.png";
import searchImageURL from "./img/down-arrow.svg";
import SizeGuide from "./SizeGuide.js";

export default class Accordion extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isOpen: false
    };
  }
  changeIcon() {
    this.setState(prevState => ({
      isOpen: !prevState.isOpen
    }));
  }
  render() {
    let iconActive = styles.icon;
    let activeheader = styles.textBox;
    if (this.state.isOpen) {
      iconActive = styles.iconup;
    }
    if (this.state.isOpen) {
      activeheader = styles.textBoxActive;
    }
    return (
      <div className={styles.base}>
        <div
          className={styles.holder}
          onClick={() => {
            this.changeIcon();
          }}
        >
          <div className={activeheader}>
            {this.props.text}
            {this.props.size && (
              <span className={styles.span}>({this.props.size})</span>
            )}
            <div className={iconActive} />
          </div>
        </div>
        <Collapse isOpened={this.state.isOpen}>
          <SizeGuide data={this.props.data} />
        </Collapse>
      </div>
    );
  }
}
Accordion.propTypes = {
  text: PropTypes.string,
  iconImageURL: PropTypes.string,
  size: PropTypes.string,
  searchImageURL: PropTypes.string
};
