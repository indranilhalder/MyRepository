import React from "react";
import styles from "./SizeSelectFilter.css";
import SizeAdd from "./SizeSelect";
import Carousel from "./Carousel";
import PropTypes from "prop-types";

export default class SizeSelectFilter extends React.Component {
  render() {
    let data = this.props.data;
    return (
      <div className={styles.base}>
        <Carousel elementWidthMobile={18}>
          {data.map((datum, i) => {
            return (
              <SizeAdd
                key={i}
                selected={this.props.selected}
                size={datum.size}
              />
            );
          })}
        </Carousel>
      </div>
    );
  }
}

SizeSelectFilter.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      size: PropTypes.string,
      selected: PropTypes.bool
    })
  )
};
