import React from "react";
import styles from "./SearchBrand.css";
import PropTypes from "prop-types";
import Input2 from "./Input2.js";
import { Icon, CircleButton } from "xelpmoc-core";
import informationIcon from "./img/Search.svg";
export default class SearchBrand extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.content}>
          <Input2
            placeholder="Search your brand"
            textStyle={{ fontSize: 14 }}
            height={40}
            rightChildSize={35}
            onChange={this.props.onChange}
            value={this.props.value}
            rightChild={
              <CircleButton
                onClick={this.handleClick}
                size={35}
                color={"transparent"}
                icon={<Icon image={informationIcon} size={16} />}
              />
            }
          />
        </div>
      </div>
    );
  }
}
SearchBrand.propTypes = {
  onClick: PropTypes.func,
  placeHolder: PropTypes.string,
  onChange: PropTypes.func
};
