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
  toggle() {
    if (!this.props.disabled) {
      this.setState({ active: !this.state.active }, () => {
        if (this.props.onToggle) {
          this.props.onToggle(this.state.active);
        }
      });
    }
  }
  render() {
    let base = styles.base;
    let className = styles.toggleInActive;
    if (this.state.active) {
      className = styles.toggleActive;
    }
    if (this.props.disabled) {
      base = styles.toggleDissabled;
    }
    return (
      <div className={base} onClick={() => this.toggle()}>
        <div className={className} />
      </div>
    );
  }
}
Toggle.propTypes = {
  onToggle: PropTypes.string,
  disabled: PropTypes.bool,
  active: PropTypes.bool
};

Toggle.defaultProps = {
  disabled: false
};
