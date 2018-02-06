import React from "react";
import styles from "./IconicFilter.css";
import CenterAdd from "./CenterAdd";
import Carousel from "../../general/components/Carousel";
import PropTypes from "prop-types";

export default class IconicFilter extends React.Component {
  render() {
    let data = this.props.data;
    return (
      <div
        className={styles.base}
        style={{
          backgroundimage: `url(${data.backgroundimageURL})`
        }}
      >
        <Carousel
          elementWidthMobile={18}
          headerComponent={
            <div className={styles.header}>{this.props.title}</div>
          }
        >
          {data.map((datum, i) => {
            return <CenterAdd key={i} text={datum.name} />;
          })}
        </Carousel>
      </div>
    );
  }
}

IconicFilter.propTypes = {
  title: PropTypes.string,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      name: PropTypes.string
    })
  )
};
