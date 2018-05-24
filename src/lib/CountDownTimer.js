let timer;
export function countdown(id, second) {
  let seconds = second;
  function tick() {
    seconds--;
    document.getElementById(id).innerHTML = seconds + " sec";
    if (seconds > 0) {
      timer = setTimeout(tick, 1000);
    }
  }
  tick();
}

export function clearInterVal() {
  clearTimeout(timer);
}
