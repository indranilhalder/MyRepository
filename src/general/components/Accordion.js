import React from "react";
import styles from "./Accordion.css";
import PropTypes from "prop-types";
import { Collapse } from "react-collapse";
export default class Accordion extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isOpen: this.props.isOpen ? this.props.isOpen : false
    };
  }
  openMenu() {
    this.setState(prevState => ({
      isOpen: !prevState.isOpen
    }));
  }
  componentWillReceiveProps(props) {
    if (this.state.isOpen !== props.isOpen) {
      this.setState({ isOpen: props.isOpen });
    }
  }
  render() {
    let iconActive = styles.icon;
    let activeheader = styles.textBox;
    let background = "";
    if (this.state.isOpen) {
      iconActive = styles.iconup;
      activeheader = styles.textBoxActive;
      background = this.props.activeBackground;
    }

    return (
      <div
        className={styles.base}
        style={{
          padding: `0 ${this.props.offset}px`,
          backgroundColor: `${background}`
        }}
      >
        <div
          className={styles.holder}
          onClick={() => {
            this.openMenu();
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
        <Collapse isOpened={this.state.isOpen}>{this.props.children}</Collapse>
      </div>
    );
  }
}
Accordion.propTypes = {
  text: PropTypes.string,
  iconImageURL: PropTypes.string,
  headerFontSize: PropTypes.number,
  offset: PropTypes.number,
  searchImageURL: PropTypes.string,
  activeBackground: PropTypes.string
};

Accordion.defaultProps = {
  headerFontSize: 14,
  offset: 0
};
