import React from "react";
import styles from "./EntireBrandsType.css";
import BrandsType from "./BrandsType.js";
import BrandsTypeList from "./BrandsTypeList.js";
import PropTypes from "prop-types";
export default class EntireBrandsType extends React.Component {
  render() {
    console.log(this.props);
    return (
      <div className={styles.base}>
        <div className={styles.headerShopAddress}>{this.props.shopeName}</div>
        <div className={styles.typeListHolder}>
          {this.props.brandsTypeList &&
            this.props.brandsTypeList.map((val, i) => {
              return (
                <BrandsType title={val.brandsType} key={i}>
                  {val.typeList &&
                    val.typeList.map((data, i) => {
                      return <BrandsTypeList list={data.subList} key={i} />;
                    })}
                </BrandsType>
              );
            })}
        </div>
      </div>
    );
  }
}
EntireBrandsType.propTypes = {
  shopeName: PropTypes.func,
  brandsTypeList: PropTypes.arrayOf(
    PropTypes.shape({
      brandsType: PropTypes.string,
      subList: PropTypes.string
    })
  )
};
