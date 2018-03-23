import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import ReturnToStore from "../components/ReturnToStore";
const mapData = [
  {
    active: "Y",
    address: {
      city: "Delh",
      country: {
        isocode: "IN"
      },
      defaultAddress: false,
      id: "8801080377367",
      line1: "Mountroad",
      line2: "Delhi",
      postalCode: "110022"
    },
    clicknCollect: "Y",
    displayName: "Tamilselvan",
    email0: "tamilselvan.mp@tcs.com",
    footFall: "100",
    geoPoint: {
      latitude: 28.564058,
      longitude: 77.1736968
    },
    isReturnable: "Y",
    location: "mall",
    managerName: "Tamil",
    managerPhone: "9896545646",
    mplClosingTime: "22:00",
    mplOpeningTime: "10:00",
    mplWorkingDays: "1,2,3,4,5,6",
    name: "Tamilselvan",
    normalRetailSalesOfStore: "200000",
    orderAcceptanceTAT: 0,
    orderProcessingTAT: 0,
    ownerShip: "franchise",
    parkingAvailable: "Y",
    phoneNo0: "09986955456",
    returnAddress1: "Mountroad",
    returnAddress2: "Delhi",
    returnCity: "Delh",
    returnPin: "110022",
    returnState: "30",
    returnstoreID: "800112-789654",
    sellerId: "800112",
    slaveId: "800112-789654",
    storeContactNumber: "09665869545",
    storeSize: "1750"
  },
  {
    active: "Y",
    address: {
      city: "NEW DELHI",
      country: {
        isocode: "IN"
      },
      defaultAddress: false,
      id: "8802784804887",
      line1: "tower 2, ebs, kurla",
      line2: "delhi",
      postalCode: "110049"
    },
    clicknCollect: "Y",
    displayName: "BestSeller New Delhi South Ext",
    email0: "appstuat4@gmail.com",
    geoPoint: {
      latitude: 28.5666032,
      longitude: 77.2201326
    },
    isReturnable: "N",
    location: "mall",
    managerName: "Bestseller New Delhi South Ext",
    mplClosingTime: "21:00",
    mplOpeningTime: "11:00",
    mplWorkingDays: "1,2,3,4,5,6,0",
    name: "800059-851059",
    orderAcceptanceTAT: 0,
    orderProcessingTAT: 0,
    parkingAvailable: "Y",
    phoneNo0: "98209070004",
    returnAddress1: "tower 5, ebs, kurla",
    returnAddress2: "mumbai",
    returnCity: "MUMBAI",
    returnPin: "400076",
    returnState: "13",
    returnstoreID: "800059-840059",
    sellerId: "800059",
    slaveId: "800059-851059",
    storeContactNumber: "09222222522"
  },
  {
    active: "Y",
    address: {
      city: "NDE ",
      country: {
        isocode: "IN"
      },
      defaultAddress: false,
      id: "8801042333719",
      line1: "Highway",
      line2: "Delhi",
      postalCode: "110022"
    },
    clicknCollect: "Y",
    displayName: "Ranjith",
    email0: "ranjith.arumugam@tcs.com",
    footFall: "100",
    geoPoint: {
      latitude: 28.564058,
      longitude: 77.1736968
    },
    isReturnable: "Y",
    location: "standalone",
    managerName: "Ranjith",
    managerPhone: "9855472565",
    mplClosingTime: "22:00",
    mplOpeningTime: "09:00",
    mplWorkingDays: "1,2,3,4,5,6",
    name: "Ranjith",
    normalRetailSalesOfStore: "200000",
    orderAcceptanceTAT: 0,
    orderProcessingTAT: 0,
    parkingAvailable: "Y",
    phoneNo0: "09632871456",
    returnAddress1: "Highway",
    returnAddress2: "Delhi",
    returnCity: "NDE ",
    returnPin: "110022",
    returnState: "30",
    returnstoreID: "100112-523698",
    sellerId: "100112",
    slaveId: "100112-523698",
    storeContactNumber: "05642624164",
    storeSize: "1200"
  },
  {
    active: "Y",
    address: {
      city: "NEW DELHI",
      country: {
        isocode: "IN"
      },
      defaultAddress: false,
      id: "8802172960791",
      line1: "Janakpuri - New Delhi",
      line2: "Janakpuri - New Delhi",
      postalCode: "110058"
    },
    clicknCollect: "Y",
    displayName: "Janakpuri - New Delhi",
    email0: "techstuat4@gmail.com",
    geoPoint: {
      latitude: 28.631704,
      longitude: 77.084033
    },
    isReturnable: "Y",
    managerName: "JanakpuriNew Delhi",
    mplClosingTime: "19:30",
    mplOpeningTime: "11:00",
    mplWorkingDays: "1,2,4,5,6,0",
    name: "800058-860058",
    orderAcceptanceTAT: 0,
    orderProcessingTAT: 0,
    phoneNo0: "09158458434",
    returnAddress1: "Janakpuri - New Delhi",
    returnAddress2: "Janakpuri - New Delhi",
    returnCity: "NEW DELHI",
    returnPin: "110058",
    returnState: "30",
    returnstoreID: "800058-860058",
    sellerId: "800058",
    slaveId: "800058-860058",
    storeContactNumber: "09158458434"
  },
  {
    active: "Y",
    address: {
      city: "NEW DELHI",
      country: {
        isocode: "IN"
      },
      defaultAddress: false,
      id: "8801709981719",
      line1: "Saket - New Delhi",
      line2: "Saket - New Delhi",
      postalCode: "110017"
    },
    clicknCollect: "Y",
    displayName: "Saket - New Delhi",
    geoPoint: {
      latitude: 28.52918,
      longitude: 77.2197
    },
    isReturnable: "Y",
    managerName: "SaketNew Delhi",
    mplClosingTime: "20:30",
    mplOpeningTime: "11:00",
    mplWorkingDays: "1,2,3,4,5,6,0",
    name: "Saket - New Delhi",
    orderAcceptanceTAT: 0,
    orderProcessingTAT: 0,
    phoneNo0: "09158458434",
    returnAddress1: "Saket - New Delhi",
    returnAddress2: "Saket - New Delhi",
    returnCity: "NEW DELHI",
    returnPin: "110017",
    returnState: "30",
    returnstoreID: "800058-870058",
    sellerId: "800058",
    slaveId: "800058-870058",
    storeContactNumber: "09158458434"
  },
  {
    active: "Y",
    address: {
      city: "Mumbai",
      country: {
        isocode: "IN"
      },
      defaultAddress: false,
      id: "8801711325207",
      line1: "Rameshwar",
      line2: "Gunthralu",
      postalCode: "400709"
    },
    clicknCollect: "Y",
    displayName: "Ramu",
    geoPoint: {
      latitude: 26.8539518,
      longitude: 75.7612568
    },
    isReturnable: "Y",
    managerName: "hjjhhjk",
    mplClosingTime: "18:00",
    mplOpeningTime: "04:00",
    mplWorkingDays: "1,2,3,4,5,6,0",
    name: "Ramu",
    orderAcceptanceTAT: 0,
    orderProcessingTAT: 0,
    phoneNo0: "00000000000",
    returnAddress1: "Rameshwar",
    returnAddress2: "Gunthralu",
    returnCity: "Mumbai",
    returnPin: "400709",
    returnState: "13",
    returnstoreID: "100058-1424789",
    sellerId: "100058",
    slaveId: "100058-1424789",
    storeContactNumber: "56471852266"
  },
  {
    active: "Y",
    address: {
      city: "NEW DELHI",
      country: {
        isocode: "IN"
      },
      defaultAddress: false,
      id: "8803339829271",
      line1: "South Ext",
      line2: "New Delhi",
      postalCode: "110049"
    },
    clicknCollect: "Y",
    displayName: "Bestseller New Delhi South Ext",
    email0: "appstuat4@yahoo.com",
    geoPoint: {
      latitude: 28.5666032,
      longitude: 77.2201326
    },
    isReturnable: "N",
    location: "mall",
    managerName: "Bestseller New Delhi South Ext",
    mplClosingTime: "21:00",
    mplOpeningTime: "11:00",
    mplWorkingDays: "1,2,3,4,5,6,0",
    name: "800059-850059",
    orderAcceptanceTAT: 0,
    orderProcessingTAT: 0,
    parkingAvailable: "Y",
    phoneNo0: "09222552222",
    returnAddress1: "tower 5, ebs, kurla",
    returnAddress2: "mumbai",
    returnCity: "MUMBAI",
    returnPin: "400076",
    returnState: "13",
    returnstoreID: "800059-840059",
    sellerId: "800059",
    slaveId: "800059-850059",
    storeContactNumber: "09222222522",
    storeSize: "5000"
  },
  {
    active: "Y",
    address: {
      city: "New Delhi",
      country: {
        isocode: "IN"
      },
      defaultAddress: false,
      id: "8804810686487",
      line1: "dsafsadf",
      line2: "sadfasdf",
      postalCode: "110001"
    },
    clicknCollect: "N",
    displayName: "CRL",
    geoPoint: {
      latitude: 28.63,
      longitude: 77.21
    },
    isReturnable: "Y",
    managerName: "Kamlesh",
    mplClosingTime: "10:00",
    mplOpeningTime: "09:00",
    mplWorkingDays: "1,2,3,4,5,6,0",
    name: "100002-CRL2",
    orderAcceptanceTAT: 0,
    orderProcessingTAT: 0,
    phoneNo0: "22888888883",
    returnAddress1: "dsafsadf",
    returnAddress2: "sadfasdf",
    returnCity: "New Delhi",
    returnPin: "110001",
    returnState: "30",
    returnstoreID: "100002-CRL2",
    sellerId: "100002",
    slaveId: "100002-CRL2",
    storeContactNumber: "22222222222"
  },
  {
    active: "Y",
    address: {
      city: "New Delhi",
      country: {
        isocode: "IN"
      },
      defaultAddress: false,
      id: "8807693025303",
      line1: "delhi",
      line2: "delhi",
      postalCode: "110003"
    },
    clicknCollect: "Y",
    displayName: "virat",
    email0: "virat@outlook.com",
    geoPoint: {
      latitude: 28.5916468,
      longitude: 77.2317863
    },
    isReturnable: "Y",
    location: "mall",
    managerName: "virat",
    mplClosingTime: "21:00",
    mplOpeningTime: "11:00",
    mplWorkingDays: "1,2,3,4,5,6,0",
    name: "273544-110003",
    orderAcceptanceTAT: 0,
    orderProcessingTAT: 0,
    parkingAvailable: "Y",
    phoneNo0: "04485236974",
    returnAddress1: "delhi",
    returnAddress2: "delhi",
    returnCity: "New Delhi",
    returnPin: "110003",
    returnState: "30",
    returnstoreID: "273544-110003",
    sellerId: "273544",
    slaveId: "273544-110003",
    storeContactNumber: "0448696969695"
  },
  {
    active: "Y",
    address: {
      city: "New Delhi",
      country: {
        isocode: "IN"
      },
      defaultAddress: false,
      id: "8811100798999",
      line1: "Mark Road streettt",
      line2: "Easat street",
      postalCode: "110001"
    },
    clicknCollect: "Y",
    displayName: "Bamboo",
    geoPoint: {
      latitude: 28.6327,
      longitude: 77.2196
    },
    isReturnable: "Y",
    managerName: "Bamboo",
    mplClosingTime: "21:00",
    mplOpeningTime: "11:00",
    mplWorkingDays: "1,2,3,4,5,6,0",
    name: "273570-273572",
    orderAcceptanceTAT: 0,
    orderCutoffTimeED: "20:00",
    orderCutoffTimeHD: "16:00",
    orderProcessingTAT: 0,
    parkingAvailable: "Y",
    phoneNo0: "04421465464",
    returnAddress1: "Mark Road streettt",
    returnAddress2: "Easat street",
    returnCity: "New Delhi",
    returnPin: "110001",
    returnState: "30",
    returnstoreID: "273570-273572",
    sellerId: "273570",
    slaveId: "273570-273572",
    storeContactNumber: "04425525451"
  },
  {
    active: "Y",
    address: {
      city: "New Delhi",
      country: {
        isocode: "IN"
      },
      defaultAddress: false,
      id: "8808479457303",
      line1: "M J ROAD",
      line2: "MAHAPE",
      postalCode: "110001"
    },
    clicknCollect: "Y",
    displayName: "QAslave01",
    geoPoint: {
      latitude: 28.6327,
      longitude: 77.2196
    },
    isReturnable: "Y",
    managerName: "TEST",
    mplClosingTime: "21:00",
    mplOpeningTime: "11:00",
    mplWorkingDays: "1,2,3,4,5,6,0",
    name: "273564-QASLAVE01",
    orderAcceptanceTAT: 0,
    orderCutoffTimeED: "16:00",
    orderCutoffTimeHD: "16:00",
    orderProcessingTAT: 0,
    phoneNo0: "5485878787878748",
    returnAddress1: "M J ROAD",
    returnAddress2: "MAHAPE",
    returnCity: "New Delhi",
    returnPin: "110001",
    returnState: "30",
    returnstoreID: "273564-QASLAVE01",
    sellerId: "273564",
    slaveId: "273564-QASLAVE01",
    storeContactNumber: "98998897788787"
  }
];

const mapDispatchToProps = dispatch => {
  return {
    getAllStores: () => {
      return false;
    }
  };
};

const mapStateToProps = state => {
  return {
    state,
    availableStores: mapData,
    numberOfStores: mapData.length
  };
};

const ReturnToStoreContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ReturnToStore)
);
export default ReturnToStoreContainer;
