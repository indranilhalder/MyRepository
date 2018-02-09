import React from "react";
import styles from "./ColourSelector.css";
import ColourSelect from "./ColourSelect";
import CarouselWithSelect from "../../general/components/CarouselWithSelect";
import PropTypes from "prop-types";
export default class ColourSelector extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      displayColour: this.props.selected[0]
    };
  }
  updateColour(val) {
    this.setState({ displayColour: val }, () => {
      if (this.props.updateColour) {
        this.props.updateColour(this.state.displayColour);
      }
    });
  }
  render() {
    let data = this.props.data;

    return (
      <div className={styles.base}>
        <CarouselWithSelect
          elementWidthMobile={19}
          limit={1}
          selected={this.props.selected}
          headerComponent={
            <div className={styles.header}>
              Colour -{" "}
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
                colour={datum.hexCode}
                value={datum.color}
                onSelect={val => this.updateColour(val)}
              />
            );
          })}
        </CarouselWithSelect>
      </div>
    );
  }
}
ColourSelector.propTypes = {
  onSelect: PropTypes.func,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      color: PropTypes.string,
      hexCode: PropTypes.string
    })
  )
};
