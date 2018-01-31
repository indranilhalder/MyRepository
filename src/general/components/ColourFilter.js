import React from "react";
import styles from "./ColourFilter.css";
import ColourAdd from "./ColourAdd";
import Carousel from "./Carousel";
import PropTypes from "prop-types";

export default class ColourFilter extends React.Component {
  render() {
    let data = this.props.data;
    return (
      <div className={styles.base}>
        <Carousel elementWidthMobile={18}>
          {data.map((datum, i) => {
            return (
              <ColourAdd
                key={i}
                backgroundColor={datum.colour}
                selected={this.props.selected}
              />
            );
          })}
        </Carousel>
      </div>
    );
  }
}
ColourFilter.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      backgroundColor: PropTypes.string,
      selected: PropTypes.bool
    })
  )
};
