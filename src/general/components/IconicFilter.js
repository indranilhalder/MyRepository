import React from "react";
import styles from "./IconicFilter.css";
import CenterAdd from "./CenterAdd";
import Carousel from "./Carousel";

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
            return <CenterAdd key={i} text={datum.text} />;
          })}
        </Carousel>
      </div>
    );
  }
}
