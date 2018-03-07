import React from "react";
import PropTypes from "prop-types";
import styles from "./BrandsToolTip.css";
import { Icon, Image } from "xelpmoc-core";
export default class BrandsToolTip extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      openPopup: false
    };
    this.setWrapperRef = this.setWrapperRef.bind(this);
    this.handleClickOutside = this.handleClickOutside.bind(this);
  }
  componentDidMount() {
    document.addEventListener("mousedown", this.handleClickOutside);
  }
  handleClickOutside(event) {
    if (this.wrapperRef && !this.wrapperRef.contains(event.target)) {
      this.setState({ openPopup: false });
    }
  }
  setWrapperRef(node) {
    this.wrapperRef = node;
  }
  handleClick() {
    this.setState({ openPopup: !this.state.openPopup });
  }
  render() {
    let className = styles.toolTip;

    return (
      <div
        className={styles.base}
        onClick={() => this.handleClick()}
        ref={this.setWrapperRef}
      >
        <div className={styles.brandsIconHolder}>
          <div
            className={styles.logoHolder}
            style={{
              backgroundImage: `url(${this.props.logo})`,
              backgroundSize: "80%",
              backgroundPosition: "center"
            }}
          />
        </div>
        <div className={this.state.openPopup ? styles.openToolTip : className}>
          <div className={styles.optionList}>Visit Brand</div>
          <div className={styles.optionList}>Remove Brand</div>
        </div>
      </div>
    );
  }
}
