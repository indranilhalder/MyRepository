import React from "react";
import styles from "./AllBrandTypes.css";
import BrandsType from "./BrandsType.js";
import ShopByBrandLists from "./ShopByBrandLists";
import BrandsTypeList from "./BrandsTypeList";
import Grid from "../../general/components/GridSelect";
import PropTypes from "prop-types";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class AllBrandTypes extends React.Component {
  selectItem(val) {
    if (this.props.selectItem) {
      this.props.selectItem(val);
    }
  }
  handleSelect(val) {
    const urlSuffix = val[0].replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
    if (this.props.setClickedElementId) {
      this.props.setClickedElementId();
    }
  }
  render() {
    let feedComponentData = this.props.feedComponentData;
    return (
      <div className={styles.base}>
        <div className={styles.headerShopAddress}>
          {feedComponentData.title}
        </div>
        <div className={styles.typeListHolder}>
          {feedComponentData.items &&
            feedComponentData.items.map((val, i) => {
              return val.items ? (
                <BrandsType title={val.title} key={i}>
                  <Grid
                    limit={1}
                    offset={0}
                    elementWidthMobile={100}
                    onSelect={val => this.handleSelect(val)}
                  >
                    {val.items.map((data, i) => {
                      return (
                        <BrandsTypeList
                          list={data.title}
                          key={i}
                          value={data.webURL}
                        />
                      );
                    })}
                  </Grid>
                </BrandsType>
              ) : (
                <ShopByBrandLists
                  key={i}
                  brandList={val.title}
                  onClick={() => this.handleSelect([val.webURL])}
                />
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
