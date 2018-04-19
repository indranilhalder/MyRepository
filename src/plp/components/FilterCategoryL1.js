import React from "react";
import styles from "./FilterCategoryL1.css";
export default class FilterCategoryL1 extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isOpen: false
    };
  }
  toggleOpen() {
    this.setState({ isOpen: !this.state.isOpen });
    this.onClick();
  }

  onClick = () => {
    if (this.props.onClick) {
      this.props.onClick(this.props.value);
    }
  };

  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header} onClick={() => this.toggleOpen()}>
          {this.props.name}
          <div className={styles.count}>{this.props.count}</div>
        </div>
        {this.props.children && (
          <div className={styles.content}>{this.props.children}</div>
        )}
      </div>
    );
  }
}
