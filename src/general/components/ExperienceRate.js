import React from "react";
import styles from "./ExperienceRate.css";
import { Icon } from "xelpmoc-core";
import badIcon from "./img/bad.svg";
import activeBadIcon from "./img/Bad_Red.svg";
import PropTypes from "prop-types";
export default class ExperienceRate extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    let activeIcon = styles.hiddenIcon;
    let hiddenIconM = styles.active;
    if (this.props.selected) {
      activeIcon = styles.active;
    }
    return (
      <div className={styles.base}>
        <div className={styles.retingIconHolder}>
          <div className={styles.experienceRateingHolder}>
            <div className={styles.iconHolder} onClick={this.handleClick}>
              <div className={hiddenIconM}>
                <Icon image={badIcon} size={36} />
              </div>
              <div className={activeIcon}>
                <Icon image={activeBadIcon} size={36} />
              </div>
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
  selectedItem: PropTypes.string
};
ExperienceRate.defaultProps = {
  selected: true,
  value: "bad",
  selectedItem: "one"
};
