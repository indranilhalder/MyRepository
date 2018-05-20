import React from "react";
import searchIcon from "./img/Search.svg";
import Input2 from "./Input2";
import Icon from "../../xelpmoc-core/Icon";
import PropTypes from "prop-types";
import styles from "./SearchInput.css";

export default class SearchInput extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: null
    };
  }
  handleOnChange(val) {
    this.setState({ value: val }, () => {
      if (this.props.onChange) {
        this.props.onChange(val);
      }
    });
  }
  render() {
    return (
      <Input2
        height={35}
        placeholder={this.props.placeholder}
        onChange={val => this.handleOnChange(val)}
        value={this.state.value}
        type="search"
        rightChild={
          <div className={styles.searchIcon}>
            <Icon image={searchIcon} size={15} />
          </div>
        }
        rightChildSize={40}
      />
    );
  }
}

SearchInput.propTypes = {
  onChange: PropTypes.func,
  placeholder: PropTypes.string
};
