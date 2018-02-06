import React from "react";
import PropTypes from "prop-types";
import Input2 from "./Input2.js";
import { CircleButton, Icon } from "xelpmoc-core";
import LocationIcon from "./img/GPS.svg";
import SearchIcon from "./img/Search.svg";
export default class SearchBox extends React.Component {
  render() {
    return (
      <Input2
        boxy="true"
        placeholder="Search by brands"
        rightchildsize={35}
        rightchild={
          <CircleButton
            size={35}
            color={"transparent"}
            icon={<Icon image={SearchIcon} size={15} />}
          />
        }
        {...this.props}
      />
    );
  }
}
