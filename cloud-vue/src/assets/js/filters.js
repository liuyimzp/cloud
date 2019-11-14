/*保留2位小数 四舍五入*/
export function twoNumFilter (val) {
  let realVal = Number(val).toFixed(2);
  return Number(realVal);
}
