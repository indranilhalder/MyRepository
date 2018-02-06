import React from "react";
import styles from "./Accordion.css";
import PropTypes from "prop-types";
import { Collapse } from "react-collapse";
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
          <div
            className={activeheader}
            style={{ fontSize: this.props.headerFontSize }}
          >
            {this.props.text}
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
  headerFontSize: PropTypes.number,
  searchImageURL: PropTypes.string
};

Accordion.defaultProps = {
  headerFontSize: 14
};
