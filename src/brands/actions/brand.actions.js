import { SUCCESS, REQUESTING, ERROR, FAILURE } from "../../lib/constants";

export const GET_ALL_BRANDS_STORE_REQUEST = "GET_ALL_BRANDS_STORE_REQUEST";
export const GET_ALL_BRANDS_STORE_SUCCESS = "GET_ALL_BRANDS_STORE_SUCCESS";
export const GET_ALL_BRANDS_STORE_FAILURE = "GET_ALL_BRANDS_STORE_FAILURE";
const USER_CATEGORY_PATH = "v2/mpl/catalogs";
let brandsStores = {
  brandsTabAZListComponent: {
    items: [
      {
        brands: [
          {
            brandName: "Nike",
            webURL: "https://www.tatacliq.com/nike/mbh-123"
          },
          {
            brandName: "Puma",
            webURL: "https://www.tatacliq.com/puma/mbh-123"
          }
        ],
        items: [
          {
            heroBannerComponent: {
              items: [
                {
                  brandLogo: "",
                  imageURL: "",
                  title: "HeroBannerEle title",
                  webURL: "www.tatacliq.com"
                }
              ],
              type: "Hero Banner Component"
            }
          }
        ],
        subType: "Men"
      },
      {
        brands: [
          {
            brandName: "Nike",
            webURL: "https://www.tatacliq.com/nike/mbh-123"
          },
          {
            brandName: "Puma",
            webURL: "https://www.tatacliq.com/puma/mbh-123"
          }
        ],
        items: [
          {
            heroBannerComponent: {
              items: [
                {
                  brandLogo: "",
                  imageURL: "",
                  title: "HeroBannerEle title",
                  webURL: "www.tatacliq.com"
                }
              ],
              type: "Hero Banner Component"
            }
          }
        ],
        subType: "Women"
      }
    ],
    type: "Brands Tab AZ List Component"
  },
  componentName: "brandsTabAZListComponent"
};
export function getAllBrandsRequest() {
  return {
    type: GET_ALL_BRANDS_STORE_REQUEST,
    status: REQUESTING
  };
}
export function getAllBrandsSuccess(brandsStores) {
  return {
    type: GET_ALL_BRANDS_STORE_SUCCESS,
    status: SUCCESS,
    brandsStores
  };
}

export function getAllBrandsFailure(error) {
  return {
    type: GET_ALL_BRANDS_STORE_FAILURE,
    status: ERROR,
    error
  };
}

export function getAllBrands(userId, accessToken, cartId) {
  return async (dispatch, getState, { api }) => {
    dispatch(getAllBrandsRequest());

    try {
      // const result = await api.get(
      //   `${USER_CATEGORY_PATH}/getAllCategorieshierarchy?`
      // );

      // const resultJson = await result.json();

      // if (resultJson.status === FAILURE) {
      //   throw new Error(resultJson.error);
      // }
      dispatch(getAllBrandsSuccess(brandsStores));
    } catch (e) {
      dispatch(getAllBrandsFailure(e.message));
    }
  };
}
