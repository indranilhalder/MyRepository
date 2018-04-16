import React from "react";
import PropTypes from "prop-types";
import Icon from "../../xelpmoc-core/Icon";
import styles from "./BrandsToolTip.css";
import deleteIcon from "../../general/components/img/delete.svg";
export default class BrandsToolTip extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    let className = styles.delete;
    return (
      <div className={styles.base}>
        <div className={styles.iconAndToolTip}>
          <div className={this.props.onDelete ? styles.onDelete : className}>
            <Icon image={deleteIcon} size={18} />
          </div>
          <div className={styles.brandsIconHolder}>
            <div
              className={styles.logoHolder}
              onClick={() => this.handleClick()}
              style={{
                backgroundImage: `url(${this.props.logo})`,
                backgroundSize: "60%",
                backgroundPosition: "center"
              }}
            />
          </div>
        </div>
      </div>
    );
  }
}
BrandsToolTip.propTypes = {
  onClick: PropTypes.func,
  logo: PropTypes.string
};
