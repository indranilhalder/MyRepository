import React from "react";
import BrandHeader from "./BrandHeader";
import BrandsCategory from "./BrandsCategory";
import BrandsSubCategory from "./BrandsSubCategory";
import MobileFooter from "../../general/components/MobileFooter";
import styles from "./BrandsLandingPageDefault.css";
export default class BrandsLandingPageDefault extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      currentSelectedType: 0
    };
  }
  componentDidMount() {
    this.props.getAllBrandsStore();
  }
  renderBrandsList(catagories) {
    return catagories.map((val, i) => {
      return (
        <BrandsCategory index={val.index} catagory={val.catagory}>
          {val.subCatagory &&
            val.subCatagory.map((data, i) => {
              return (
                <BrandsSubCategory label={data.list} select={data.select} />
              );
            })}
        </BrandsCategory>
      );
    });
  }
  render() {
    console.log(this.props);
    return (
      <div className={styles.base}>
        {this.props.brandsStores &&
          this.props.brandsStores[this.props.brandsStores.componentName] && (
            //add brands header here and  hero banner
            // and also render renderBrandsList just passing selected
            // brand type
            <BrandHeader />
          )}
        <MobileFooter selected="brands" />
      </div>
    );
  }
}
