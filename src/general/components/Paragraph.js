import React from "react";
import PropTypes from "prop-types";

export default class Paragraph extends React.Component {
  render() {
    return <p style={{ ...this.props.textStyle }}> {this.props.text} </p>;
  }
}

Paragraph.propTypes = {
  text: PropTypes.string,
  textStyle: PropTypes.shape({
    padding: PropTypes.number,
    margin: PropTypes.number,
    color: PropTypes.string,
    fontSize: PropTypes.number,
    fontFamily: PropTypes.string,
    fontWeight: PropTypes.string,
    lineHeight: PropTypes.number
  })
};

Paragraph.defaultProps = {
  textStyle: {
    padding: 0,
    margin: 0,
    color: "#212121",
    fontSize: 14,
    lineHeight: "20px",
    fontFamily: "regular",
    fontWeight: "normal"
  }
};
