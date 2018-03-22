import React from "react";
import ShopByBrandLists from "../../blp/components/ShopByBrandLists";
import Button from "../../general/components/Button";
import ProfilePicture from "../../blp/components/ProfilePicture";
import * as styles from "./MyAccountBrands.css";
export default class MyAccountBrands extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder}>
          <ProfilePicture firstName="aakarsh" lastName="Yadav" edit={false} />
        </div>
        <div className={styles.latestBrandHolder}>
          <div className={styles.latestBrandText}>
            See the latest products from your favorite Brand.
          </div>
          <div className={styles.latestBrandButtonHolder}>
            <div className={styles.latestBrandButton}>
              <Button
                height={40}
                width={170}
                type="primary"
                label="More Brands"
              />
            </div>
          </div>
        </div>
        <div className={styles.followedBrandsTextHolder}>
          <div className={styles.followedBrandsTextHeader}>Follow Brands</div>
          <div className={styles.followedBrandsTextSubHeader}>
            (Long press to visit or remove brands)
          </div>
        </div>
        <div className={styles.followedBrandsHolder}>
          <ShopByBrandLists />
        </div>
      </div>
    );
  }
}
