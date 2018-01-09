import React from "react";
import PropTypes from "prop-types";

export default class Header extends React.Component {
  render() {
    return <h3 style={{ ...this.props.textStyle }}> {this.props.text} </h3>;
  }
}

Header.propTypes = {
  text: PropTypes.string,
  textStyle: PropTypes.shape({
    padding: PropTypes.number,
    margin: PropTypes.number,
    color: PropTypes.string,
    fontSize: PropTypes.number,
    fontFamily: PropTypes.string,
    fontWeight: PropTypes.string
  })
};

Header.defaultProps = {
  textStyle: {
    padding: 0,
    margin: 0,
    color: "#212121",
    fontSize: 16,
    fontFamily: "semibold",
    fontWeight: "normal"
  }
};
