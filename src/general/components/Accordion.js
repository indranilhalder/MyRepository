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
    if (!this.props.controlled) {
      this.setState(prevState => ({
        isOpen: !prevState.isOpen
      }));
    }
    if (this.props.onOpen) {
      this.props.onOpen();
    }
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
            {this.props.text && (
              <h2 className={styles.text}>{this.props.text}</h2>
            )}
            {this.props.headerElement && (
              <div
                className={styles.faqQuestion}
                dangerouslySetInnerHTML={{ __html: this.props.faqQuestion }}
              />
            )}
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
  activeBackground: PropTypes.string,
  controlled: PropTypes.bool,
  onOpen: PropTypes.func,
  isOpen: PropTypes.bool,
  headerElement: PropTypes.bool
};

Accordion.defaultProps = {
  headerFontSize: 14,
  controlled: false,
  offset: 0,
  headerElement: false
};
