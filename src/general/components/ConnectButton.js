import React from "react";
import PropTypes from "prop-types";
import MediaQuery from "react-responsive";
import styles from "./ConnectButton.css";
import Icon from "../../xelpmoc-core/Icon";
import CircleButton from "../../xelpmoc-core/CircleButton";
import iconImageURL from "../../home/components/img/Connect_Small.svg";
export default class ConnectButton extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    return (
      <div className={styles.connectButtonHolder}>
        <MediaQuery query="(min-device-width: 1025px)">
          <CircleButton
            color={"transparent"}
            size={40}
            icon={<Icon image={iconImageURL} size={25} />}
            onClick={this.handleClick}
          />
        </MediaQuery>
        <MediaQuery query="(max-device-width: 1024px)">
          <CircleButton
            color={"transparent"}
            size={50}
            icon={<Icon image={iconImageURL} size={30} />}
            onClick={this.handleClick}
          />
        </MediaQuery>
      </div>
    );
  }
}
ConnectButton.propTypes = {
  onClick: PropTypes.func
};
