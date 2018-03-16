import { connect } from "react-redux";
import { withRouter } from "react-router";
import PiqPage from "../components/PiqPage";
import {
  getAllStoresCNC,
  addStoreCNC,
  addPickupPersonCNC
} from "../actions/cart.actions";

let pinCodeResponse = {
  cod: "N",
  exchangeServiceable: false,
  isCODLimitFailed: "Y",
  isPrepaidEligible: "Y",
  isServicable: "Y",
  ussid: "273544ASB001",
  validDeliveryModes: [
    {
      CNCServiceableSlavesData: [
        {
          fulfillmentType: "TSHIP",
          qty: 986,
          serviceableSlaves: [
            {
              priority: "1",
              slaveId: "273544-110003"
            }
          ],
          storeId: "273544-110003"
        }
      ],
      inventory: "986",
      isCOD: false,
      type: "CNC"
    },
    {
      CNCServiceableSlavesData: [
        {
          fulfillmentType: "TSHIP",
          qty: 986,
          serviceableSlaves: [
            {
              priority: "1",
              slaveId: "800059-850059"
            }
          ],
          storeId: "800059-850059"
        }
      ],
      inventory: "986",
      isCOD: false,
      type: "CNC"
    },
    {
      CNCServiceableSlavesData: [
        {
          fulfillmentType: "TSHIP",
          qty: 986,
          serviceableSlaves: [
            {
              priority: "1",
              slaveId: "100112-523698"
            }
          ],
          storeId: "100112-523698"
        }
      ],
      inventory: "986",
      isCOD: false,
      type: "CNC"
    }
  ]
};
const mapDispatchToProps = dispatch => {
  return {
    getAllStoresCNC: pincode => {
      dispatch(getAllStoresCNC(pincode));
    },
    addStoreCNC: (ussId, slaveId) => {
      dispatch(addStoreCNC(ussId, slaveId));
    },
    addPickupPersonCNC: (personMobile, personName) => {
      dispatch(addPickupPersonCNC(personMobile, personName));
    }
  };
};
const mapStateToProps = state => {
  const firstSlaveData = pinCodeResponse.validDeliveryModes;
  const someData = firstSlaveData
    .map(slaves => {
      return slaves.CNCServiceableSlavesData.map(slave => {
        return slave.serviceableSlaves.map(serviceableSlave => {
          return serviceableSlave;
        });
      });
    })
    .map(val => {
      return val.map(v => {
        return v;
      });
    });

  const allStoreIds = [].concat
    .apply([], [].concat.apply([], someData))
    .map(store => {
      return store.slaveId;
    });
  const availableStores = state.cart.storeDetails.filter(val => {
    return allStoreIds.includes(val.slaveId);
  });
  return {
    availableStores,
    numberOfStores: availableStores.length,
    productName: "data.products[0].productName",
    productColour: "data.products[0].color"
  };
};
const PiqPageContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(PiqPage)
);
export default PiqPageContainer;
