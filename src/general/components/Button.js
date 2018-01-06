import React from "react";
import { Button } from "xelpmoc-core";
import PropTypes from "prop-types";
export default class ButtonType extends React.Component {
  renderButton() {
    if (this.props.type === "primary") {
      return (
        <Button
          backgroundColor={"#FF1744"}
          label={this.props.label}
          width={this.props.width}
          height={this.props.height}
          borderRadius={this.props.height / 2}
          onClick={this.onClick}
          loading={this.props.loading}
          textStyle={{ color: "#FFF", fontSize: 14, fontFamily: "regular" }}
        />
      );
    } else if (this.props.type === "secondary") {
      return (
        <Button
          backgroundColor={"#ffffff"}
          borderColor={"#000000"}
          label={this.props.label}
          width={this.props.width}
          height={this.props.height}
          borderRadius={this.props.height / 2}
          onClick={this.onClick}
          loading={this.props.loading}
          textStyle={{ color: "#000000", fontSize: 14, fontFamily: "regular" }}
        />
      );
    } else if (this.props.type === "tertiary") {
      return (
        <Button
          backgroundColor={"transparent"}
          borderColor={"rgba(0,0,0,0.5)"}
          label={this.props.label}
          width={this.props.width}
          height={this.props.height}
          borderRadius={this.props.height / 2}
          onClick={this.onClick}
          loading={this.props.loading}
          textStyle={{
            color: "rgba(0,0,0,0.5)",
            fontSize: 14,
            fontFamily: "regular"
          }}
        />
      );
    } else {
      return <Button {...this.props} />;
    }
  }
  render() {
    return <div>{this.renderButton()}</div>;
  }
}

ButtonType.propTypes = {
  type: PropTypes.oneOf(["primary", "secondary", "tertiary"])
};

ButtonType.defaultProps = {
  height: 40
};
