export const SHOW_LOADER = "SHOW_LOADER";
export const HIDE_LOADER = "HIDE_LOADER";
export function showSecondaryLoader() {
  return {
    type: SHOW_LOADER
  };
}

export function hideSecondaryLoader() {
  return {
    type: HIDE_LOADER
  };
}
