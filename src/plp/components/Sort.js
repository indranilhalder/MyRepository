import React from "react";
import SortTab from "./SortTab.js";
import PropTypes from "prop-types";
import styles from "./Sort.css";
import InformationHeader from "../../general/components/InformationHeader";

export default class Sort extends React.Component {
  onClick(val) {
    if (this.props.onClick) {
      this.props.onClick(val);
    }
  }
  handleCloseClick = () => {
    this.props.onCloseSort();
  };
  render() {
    let data = this.props.sortList;
    return (
      <div className={styles.base}>
        <InformationHeader text="Sort" onClick={this.handleCloseClick} />
        {this.props.sortList &&
          this.props.sortList.length > 0 &&
          data.map((datum, i) => {
            return (
              <SortTab
                label={datum.name}
                value={datum.code}
                key={i}
                onClick={() => {
                  this.onClick(datum.code);
                }}
              />
            );
          })}
      </div>
    );
  }
}

Sort.PropTypes = {
  sortList: PropTypes.shape({ name: PropTypes.string, code: PropTypes.string }),
  onClick: PropTypes.func
};
