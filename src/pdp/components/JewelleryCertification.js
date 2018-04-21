import React from "react";
import Logo from "../../general/components/Logo";
import styles from "./JewelleryCertification.css";
import PropTypes from "prop-types";
import caratLane from "./img/cartlane.png";
import egl from "./img/egl.png";
import gia from "./img/gia.png";
import gsi from "./img/gsi.png";
import igi from "./img/igi.png";
import orra from "./img/orra.png";
import platinum from "./img/platinum.png";
import sgl from "./img/sgl.png";
import tanishq from "./img/tanishq.png";
import ibja from "./img/ibja.png";
import igl from "./img/igl.png";
import agl from "./img/agl.png";
import ags from "./img/ags.png";
import agta from "./img/agta.png";
import dgla from "./img/dgla.png";
import gii from "./img/gii.png";
import gubelin from "./img/gubelin.png";
import hrd from "./img/hrd.png";
import pgi from "./img/pgi.png";
import bis from "./img/bis.png";
import idl from "./img/idl.png";
import jagdamba from "./img/jagdamba.png";
import nibr from "./img/nibr.png";

export default class JewelleryCertification extends React.Component {
  getCertificationImage = val => {
    let image = "";
    switch (val) {
      case "AGL":
        image = agl;
        break;
      case "AGS":
        image = ags;
        break;
      case "AGTA":
        image = agta;
        break;
      case "BIS":
        image = bis;
        break;
      case "DGLA":
        image = dgla;
        break;
      case "EGL":
        image = egl;
        break;
      case "GIA":
        image = gia;
        break;
      case "GII":
        image = gii;
        break;
      case "GSI":
        image = gsi;
        break;
      case "Gubelin":
        image = gubelin;
        break;
      case "HRD":
        image = hrd;
        break;
      case "IBJA":
        image = ibja;
        break;
      case "IDL":
        image = idl;
        break;
      case "IGI":
        image = igi;
        break;
      case "IGL":
        image = igl;
        break;
      case "NIBR":
        image = nibr;
        break;
      case "ORRA":
        image = orra;
        break;
      case "PGI":
        image = pgi;
        break;
      case "SGL":
        image = sgl;
        break;
      case "Tanishq":
        image = tanishq;
        break;
      case "Sri Jagdamba Pearls":
        image = jagdamba;
        break;
      case "CaratLane":
        image = caratLane;
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
      "AGL",
      "AGS",
      "AGTA",
      "BIS",
      "DGLA",
      "EGL",
      "GSI",
      "GIA",
      "GII",
      "Gubelin",
      "HRD",
      "IBJA",
      "IGI",
      "IGL",
      "ORRA",
      "PGI",
      "SGL",
      "CaratLane",
      "Tanishq",
      "Sri Jagdamba Pearls",
      "NIBR",
      "IDL"
    ])
  )
};
