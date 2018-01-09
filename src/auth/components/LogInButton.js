import React from "react";
import { Button } from "xelpmoc-core";
import MediaQuery from "react-responsive";
import PropTypes from "prop-types";
export default class LogInButton extends React.Component {
  onClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    return (
      <div>
        <MediaQuery query="(min-device-width: 1024px)">
          <Button
            label={"Sign in"}
            width={150}
            height={40}
            borderRadius={20}
            backgroundColor={"#ff1744"}
            onClick={this.onClick}
            loading={this.props.loading}
            textStyle={{ color: "#FFF", fontSize: 14 }}
          />
        </MediaQuery>
        <MediaQuery query="(max-device-width:1023px)">
          <Button
            backgroundColor={"#FF1744"}
            label={"Login"}
            width={150}
            height={45}
            borderRadius={22.5}
            onClick={this.onClick}
            loading={this.props.loading}
            textStyle={{ color: "#FFF", fontSize: 14 }}
          />
        </MediaQuery>
      </div>
    );
  }
}

LogInButton.propTypes = {
  onClick: PropTypes.func,
  loading: PropTypes.bool
};

LogInButton.defaultProps = {
  loading: false
};
