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
          onClick={this.props.onClick}
          loading={this.props.loading}
          textStyle={{
            color: "#FFF",
            fontSize: 14,
            fontFamily: "Rubik Medium"
          }}
        />
      );
    } else if (this.props.type === "secondary") {
      return (
        <Button
          backgroundColor={"#ffffff"}
          borderColor={"#181818"}
          label={this.props.label}
          width={this.props.width}
          height={this.props.height}
          borderRadius={this.props.height / 2}
          onClick={this.props.onClick}
          loading={this.props.loading}
          textStyle={{
            color: "#181818",
            fontSize: 14,
            fontFamily: "Rubik Medium"
          }}
        />
      );
    } else if (this.props.type === "tertiary") {
      return (
        <Button
          backgroundColor={"transparent"}
          borderColor={"#8D8D8D"}
          label={this.props.label}
          width={this.props.width}
          height={this.props.height}
          borderRadius={this.props.height / 2}
          onClick={this.props.onClick}
          loading={this.props.loading}
          textStyle={{
            color: "#8D8D8D",
            fontSize: 14,
            fontFamily: "Rubik Medium"
          }}
        />
      );
    } else if (this.props.type === "hollow") {
      return (
        <Button
          backgroundColor={"transparent"}
          borderColor={"#FFFFFF"}
          label={this.props.label}
          width={this.props.width}
          height={this.props.height}
          borderRadius={this.props.height / 2}
          onClick={this.props.onClick}
          loading={this.props.loading}
          textStyle={{
            color: "#FFFFFF",
            fontSize: 14,
            fontFamily: "Rubik Medium"
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
