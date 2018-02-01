import React from "react";
import styles from "./ColourFilter.css";
import ColourAdd from "./ColourSelect";
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
                colour={datum.colour}
                selected={this.props.selected}
                onSelect={this.props.onSelect}
              />
            );
          })}
        </Carousel>
      </div>
    );
  }
}
ColourFilter.propTypes = {
  onSelect: PropTypes.func,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      colour: PropTypes.string,
      selected: PropTypes.bool
    })
  )
};
