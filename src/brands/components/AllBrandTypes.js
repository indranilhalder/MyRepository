import React from "react";
import styles from "./AllBrandTypes.css";
import BrandsType from "./BrandsType.js";
import BrandsTypeList from "./BrandsTypeList.js";
import PropTypes from "prop-types";
export default class AllBrandTypes extends React.Component {
  render() {
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
AllBrandTypes.propTypes = {
  shopeName: PropTypes.func,
  brandsTypeList: PropTypes.arrayOf(
    PropTypes.shape({
      brandsType: PropTypes.string,
      subList: PropTypes.arrayOf(
        PropTypes.shape({
          subList: PropTypes.string
        })
      )
    })
  )
};
