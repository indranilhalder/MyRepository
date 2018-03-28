import React from "react";
import styles from "./ColourSelector.css";
import ColourSelect from "./ColourSelect";
import Carousel from "../../general/components/Carousel";
import PropTypes from "prop-types";
export default class ColourSelector extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      displayColour: this.props.selected ? this.props.selected[0] : [""]
    };
  }
  updateColour(productUrl) {
    this.props.history.push(productUrl);
  }
  render() {
    let data = this.props.data;

    return (
      <div
        className={this.props.noBackground ? styles.noBackground : styles.base}
      >
        <Carousel
          elementWidthMobile="auto"
          limit={1}
          headerComponent={
            <div className={styles.header}>
              Colour{" "}
              <span className={styles.colourName}>
                {this.state.displayColour}
              </span>
            </div>
          }
        >
          {data.map((datum, i) => {
            return (
              <ColourSelect
                key={i}
                colour={datum.colorHexCode}
                value={datum.color}
                selected={datum.selected}
                onSelect={() => this.updateColour(datum.colorurl)}
              />
            );
          })}
        </Carousel>
      </div>
    );
  }
}
ColourSelector.propTypes = {
  noBackground: PropTypes.bool,
  onSelect: PropTypes.func,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      color: PropTypes.string,
      hexCode: PropTypes.string
    })
  )
};
