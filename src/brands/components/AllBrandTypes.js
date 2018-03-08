import React from "react";
import styles from "./AllBrandTypes.css";
import BrandsType from "./BrandsType.js";
import BrandsTypeList from "./BrandsTypeList.js";
import GridSelect from "../../general/components/GridSelect";
import PropTypes from "prop-types";
export default class AllBrandTypes extends React.Component {
  selectItem(val) {
    if (this.props.selectItem) {
      this.props.selectItem(val);
    }
  }
  handleSelect(val) {
    if (this.props.brandsItem) {
      this.props.brandsItem(val);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.headerShopAddress}>{this.props.shopeName}</div>
        <div className={styles.typeListHolder}>
          {this.props.brandsTypeList &&
            this.props.brandsTypeList.map((val, i) => {
              return (
                <BrandsType title={val.brandsType} key={i}>
                  <GridSelect
                    limit={1}
                    offset={0}
                    elementWidthMobile={100}
                    onSelect={val => this.handleSelect(val)}
                  >
                    {val.typeList &&
                      val.typeList.map((data, i) => {
                        return (
                          <BrandsTypeList
                            list={data.subList}
                            key={i}
                            value={data.subList}
                          />
                        );
                      })}
                  </GridSelect>
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
  brandsItem: PropTypes.func,
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
