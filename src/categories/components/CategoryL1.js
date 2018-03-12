import React from "react";
import ColourButton from "../../general/components/ColourButton";
import styles from "./CategoryL1.css";
export default class CategoryL1 extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isOpen: false
    };
  }
  handleViewAll() {
    if (this.props.viewAll) {
      this.props.viewAll();
    }
  }
  openItem() {
    this.setState({ isOpen: true });
  }
  closeItem() {
    this.setState({ isOpen: false });
  }
  render() {
    return (
      <div className={styles.base}>
        {!this.state.isOpen && (
          <div
            className={styles.closedHeader}
            onClick={() => {
              this.openItem();
            }}
          >
            {this.props.label}
            <div className={styles.arrow} />
          </div>
        )}
        {this.state.isOpen && (
          <div className={styles.openHeader}>
            <div
              className={styles.backArrow}
              onClick={() => {
                this.closeItem();
              }}
            />
            {this.props.label}
            <div className={styles.viewButton}>
              <ColourButton
                label="View all"
                color="#ff1744"
                onClick={() => {
                  this.handleViewAll();
                }}
              />
            </div>
          </div>
        )}
        {this.state.isOpen && (
          <div className={styles.content}>{this.props.children}</div>
        )}
      </div>
    );
  }
}
