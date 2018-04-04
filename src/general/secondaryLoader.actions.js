export const SHOW_LOADER = "SHOW_LOADER";
export const HIDE_LOADER = "HIDE_LOADER";
export function showSecondaryLoader() {
  console.log("show called");
  return {
    type: SHOW_LOADER
  };
}

export function hideSecondaryLoader() {
  console.log("hide called");
  return {
    type: HIDE_LOADER
  };
}
