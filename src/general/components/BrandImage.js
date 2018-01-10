import React from "react";
import styles from "./BrandImage.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
export default class BrandImage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selected: this.props.selected
    };
  }
  onSelected = () => {
    this.setState({ selected: !this.state.selected });
  };
  render() {
    return (
      <div className={styles.base} onClick={this.onSelected}>
        <div className={this.state.selected ? styles.active : styles.inactive}>
          <div className={styles.imageHolder}>
            <Image image={this.props.source} />
          </div>
        </div>
      </div>
    );
  }
}
BrandImage.propTypes = {
  source: PropTypes.string,
  selected: PropTypes.bool
};
BrandImage.defaultProps = {
  source: "",
  selected: false
};
