import React, { Component } from "react";
import { Icon, CircleButton } from "xelpmoc-core";
import PropTypes from "prop-types";
import Header from "./Header";
import Para from "./Paragraph";
import styles from "./Description.css";
import eye from "../../general/components/img/hide_pwd.svg";

export default class Description extends Component {
  handleClick() {
    if (this.props.onIconPress) {
      this.props.onIconPress();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          <Header text={this.props.title} />
          <CircleButton
            size={20}
            color={"transparent"}
            icon={<Icon image={this.props.icon} />}
            onClick={() => this.handleClick()}
          />
        </div>
        <div className={styles.subHeader}>
          <Para text={this.props.description} />
          <Para text={this.props.subDescription} />
          <Para text={this.props.price} />
        </div>
      </div>
    );
  }
}

Description.propTypes = {
  title: PropTypes.string,
  description: PropTypes.string,
  subDescription: PropTypes.string,
  price: PropTypes.number,
  icon: PropTypes.string,
  onIconPress: PropTypes.func
};

Description.defaultProps = {
  title: "",
  icon: eye,
  description: "",
  subDescription: "",
  price: 0
};
