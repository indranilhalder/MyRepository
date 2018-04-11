export const SET_ICID = "SET_ICID";
export const CLEAR_ICID = "CLEAR_ICID";

export function setIcid(icid) {
  return {
    type: SET_ICID,
    icid
  };
}

export function clearIcid() {
  return {
    type: CLEAR_ICID
  };
}
