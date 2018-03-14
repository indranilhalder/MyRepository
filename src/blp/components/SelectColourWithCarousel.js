import React from "react";
import styles from "./SelectColourWithCarousel.css";
import PropTypes from "prop-types";
import ColourSelect from "../../pdp/components/ColourSelect";
import Carousel from "../../general/components/Carousel";
export default class SelectColourWithCarousel extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.headerHolder}>{this.props.heading}</div>
        <div className={styles.colourHolder}>
          <Carousel elementWidthMobile={20}>
            {this.props.multipleColour.map((datum, i) => {
              return (
                <ColourSelect
                  key={i}
                  colour={datum.colour}
                  selected={datum.selected}
                />
              );
            })}
          </Carousel>
        </div>
      </div>
    );
  }
}
SelectColourWithCarousel.propTypes = {
  multipleColour: PropTypes.arrayOf(
    PropTypes.shape({
      colour: PropTypes.string,
      selected: PropTypes.boolean
    })
  ),
  heading: PropTypes.string
};
