import React from "react";
import Logo from "../../general/components/Logo";
import styles from "./JewelleryCertification.css";
import PropTypes from "prop-types";
import bis from "./img/bsi.png";
import caratLane from "./img/caratLane.png";
import egl from "./img/egl.png";
import gia from "./img/gia.png";
import gsi from "./img/gsi.png";
import igi from "./img/igi.png";
import orra from "./img/orra.png";
import platinum from "./img/platinum.png";
import sgl from "./img/sgl.png";
import tanishq from "./img/tanishq.png";

export default class JewelleryCertification extends React.Component {
  getCertificationImage = val => {
    let image = "";
    switch (val) {
      case "Orra":
        image = orra;
        break;
      case "Platinum":
        image = platinum;
        break;
      case "AGL":
        image = egl;
        break;
      case "Carat lane":
        image = caratLane;
        break;
      case "Tanishq":
        image = tanishq;
        break;
      case "IGI":
        image = igi;
        break;
      case "GSI":
        image = gsi;
        break;
      case "GIA":
        image = gia;
        break;
      case "BIS":
        image = bis;
        break;
      case "SGL":
        image = sgl;
        break;
      default:
        image = "";
        break;
    }
    return image;
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>Certified by -</div>
        <div className={styles.logo}>
          {this.props.certifications.map(val => {
            return (
              <div className={styles.logoHolder}>
                <Logo image={this.getCertificationImage(val)} />{" "}
              </div>
            );
          })}
        </div>
      </div>
    );
  }
}

JewelleryCertification.propTypes = {
  certifications: PropTypes.arrayOf(
    PropTypes.oneOf([
      "Orra",
      "Platinum",
      "EGL",
      "Carat Lane",
      "Tanishq",
      "IGI",
      "GSI",
      "GIA",
      "BSI",
      "SGL"
    ])
  )
};
