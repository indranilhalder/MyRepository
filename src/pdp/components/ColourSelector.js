import React from "react";
import styles from "./ColourSelector.css";
import ColourSelect from "./ColourSelect";
import Carousel from "../../general/components/Carousel";
import { PRODUCT_DESCRIPTION_ROUTER } from "../../lib/constants";
import PropTypes from "prop-types";
export default class ColourSelector extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      displayColour: this.props.selected ? this.props.selected[0] : [""]
    };
  }
  updateColour(productUrl) {
    console.log(productUrl);
    console.log(PRODUCT_DESCRIPTION_ROUTER);
    // this.setState({ displayColour: val }, () => {
    //   if (this.props.updateColour) {
    //     this.props.getProductSpecification(productUrl);
    //   }
    // });
    // this.props.history.push(`${PRODUCT_DESCRIPTION_ROUTER}/${productUrl}`);
    this.props.history.push(`${productUrl}`);
  }
  render() {
    let data = this.props.data;
    console.log(data);
    return (
      <div className={styles.base}>
        <Carousel
          elementWidthMobile={22}
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
            console.log(datum);
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
  onSelect: PropTypes.func,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      color: PropTypes.string,
      hexCode: PropTypes.string
    })
  )
};
