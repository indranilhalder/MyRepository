import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";

export const GET_BRAND_DETAIL_REQUEST = "GET_BRAND_DETAIL_REQUEST";
export const GET_BRAND_DETAIL_SUCCESS = "GET_BRAND_DETAIL_SUCCESS";
export const GET_BRAND_DETAIL_FAILURE = "GET_BRAND_DETAIL_FAILURE";

const mockBrandInformation = {
  categoryType: "Brand",
  heroSection: {
    backgroundImage:
      "/medias/sys_master/images/images/hf4/hc0/8798847139870/logoCroma.png",
    description:
      "We make everyday look good. Express your style and mood at anytime",
    logoImage:
      "/medias/sys_master/images/images/hf4/hc0/8798847139870/logoCroma.png",
    targetCode: "MSH10",
    title: "Westside"
  },
  pageType: "Content",
  rootCategory: "Apparel",
  sections: [
    {},
    {
      components: [
        {
          image:
            "/medias/sys_master/images/images/h56/hd6/8798847107102/Camera.png",
          link: "Shop Now",
          targetCode: "MSH1010"
        },
        {
          image:
            "/medias/sys_master/images/images/h56/hd6/8798847107102/Camera.png",
          link: "Shop Now",
          targetCode: "MSH1010"
        },
        {
          image:
            "/medias/sys_master/images/images/h56/hd6/8798847107102/Camera.png",
          link: "Shop Now",
          targetCode: "MSH1010"
        },
        {
          image:
            "/medias/sys_master/images/images/h56/hd6/8798847107102/Camera.png",
          link: "Shop Now",
          targetCode: "MSH1010"
        }
      ],
      title: "Welcome to Westside"
    },
    {
      components: [
        {
          image:
            "/medias/sys_master/images/images/h8d/hb0/8798847205406/Samsung.png",
          link: "Shop Now",
          targetCode: "MSH1011"
        }
      ],
      title: "Westside Welcomes you"
    },
    {
      components: [
        {
          image:
            "/medias/sys_master/images/images/h8d/hb0/8798847205406/Samsung.png",
          targetCode: "MSH1011"
        },
        {
          image:
            "/medias/sys_master/images/images/h8d/hb0/8798847205406/Samsung.png",
          targetCode: "MSH1011"
        },
        {
          image:
            "/medias/sys_master/images/images/h8d/hb0/8798847205406/Samsung.png",
          targetCode: "MSH1011"
        },
        {
          image:
            "/medias/sys_master/images/images/h8d/hb0/8798847205406/Samsung.png",
          targetCode: "MSH1011"
        },
        {
          image:
            "/medias/sys_master/images/images/h8d/hb0/8798847205406/Samsung.png",
          targetCode: "MSH1011"
        },
        {
          image:
            "/medias/sys_master/images/images/h8d/hb0/8798847205406/Samsung.png",
          targetCode: "MSH1011"
        }
      ]
    },
    {
      components: [
        {
          additionalDescription: "Westside Clothing",
          description: "Welcome Westside",
          image:
            "/medias/sys_master/images/images/h4a/h8c/8798847238174/Electronics.png",
          link: "Shop Now",
          targetCode: "MSH1011"
        }
      ]
    },
    {
      components: [
        {
          description: "Westside Clothing",
          image:
            "/medias/sys_master/images/images/h7f/had/8798847172638/ipad.png",
          targetCode: "MSH1011"
        }
      ],
      title: "Westside Clothing"
    }
  ]
};

export function getBrandDetailsRequest() {
  return {
    type: GET_BRAND_DETAIL_REQUEST,
    status: REQUESTING
  };
}
export function getBrandDetailsSuccess(brandDetails) {
  return {
    type: GET_BRAND_DETAIL_SUCCESS,
    status: SUCCESS,
    brandDetails
  };
}
export function getBrandDetailsFailure(error) {
  return {
    type: GET_BRAND_DETAIL_FAILURE,
    status: ERROR,
    error
  };
}
export function getBrandDetails(categoryId) {
  return async (dispatch, getState, { api }) => {
    dispatch(getBrandDetailsRequest());
    try {
      dispatch(getBrandDetailsSuccess(mockBrandInformation));
    } catch (e) {
      dispatch(getBrandDetailsFailure(e.message));
    }
  };
}
