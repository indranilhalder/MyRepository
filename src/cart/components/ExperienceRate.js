import React from "react";
import styles from "./ExperienceRate.css";
import Icon from "../../xelpmoc-core/Icon";
import PropTypes from "prop-types";
export default class ExperienceRate extends React.Component {
  onSelect() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    let activeIcon = styles.hiddenIcon;
    let hiddenIconM = styles.active;
    if (this.props.selected) {
      activeIcon = styles.active;
    }
    return (
      <div className={styles.base} onClick={() => this.onSelect()}>
        <div className={styles.iconBox}>
          <div className={styles.iconHolder}>
            <div className={hiddenIconM}>
              <Icon image={this.props.defaultImage} size={36} />
            </div>
            <div className={activeIcon}>
              <Icon image={this.props.activeImage} size={36} />
            </div>
          </div>
        </div>
      </div>
    );
  }
}
ExperienceRate.propTypes = {
  value: PropTypes.string,
  selected: PropTypes.bool,
  onClick: PropTypes.func,
  selectedItem: PropTypes.func,
  defaultImage: PropTypes.string,
  activeImage: PropTypes.string
};
