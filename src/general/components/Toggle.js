import React from "react";
import PropTypes from "prop-types";
import styles from "./Toggle.css";
export default class Toggle extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      active: this.props.active
    };
  }
  handleToggle() {
    if (!this.props.disabled) {
      this.setState({ active: !this.state.active }, () => {
        if (this.props.onToggle) {
          this.props.onToggle(this.state.active);
        }
      });
    }
  }
  componentWillReceiveProps(nextProps) {
    if (nextProps.active !== this.state.active) {
      this.setState({ active: nextProps.active });
    }
  }
  render() {
    let base = styles.base;
    let className = styles.toggleInActive;
    if (this.state.active) {
      className = styles.toggleActive;
    }
    if (this.props.disabled) {
      base = styles.toggleDisabled;
    }
    return (
      <div className={base} onClick={() => this.handleToggle()}>
        <div className={className} />
      </div>
    );
  }
}
Toggle.propTypes = {
  onToggle: PropTypes.func,
  disabled: PropTypes.bool,
  active: PropTypes.bool
};

Toggle.defaultProps = {
  disabled: false
};
