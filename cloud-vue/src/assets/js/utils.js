/*中文*/
export function isChinese(str) {
  const reg = /[\u4e00-\u9fa5]/;
  return reg.test(str)
}
/*英文*/
export function isEnglish(str) {
  const reg = /^[A-Za-z]+$/;
  return reg.test(str)
}
/*小写英文*/
export function islowerEnglish(str) {
  const reg = /^[a-z]+$/;
  return reg.test(str)
}
/*ip*/
export function isIP(str) {
  const reg = /^((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))$/;
  return reg.test(str)
}
/*数字*/
export function isNumber(str) {
  const reg = /^[0-9]+$/;
  return reg.test(str)
}
/*以 / 开头*/
export function isSlash(str) {
  if(str.indexOf("/") === 0){
    return true;
  }else {
    return false;
  }
}

/*英文、减号、数字*/
export function isMinus(str) {
  const reg = /^[a-z0-9A-Z](([-a-zA-Z0-9]*[a-zA-Z0-9])+)?$/;
  return reg.test(str)
}
/*小写英文、减号、数字*/
export function isLowercaseMinus(str) {
  const reg = /^[a-z0-9]([-a-z0-9]*[a-z0-9])+$/;
  return reg.test(str)
}
/*小写英文、下划线*/
export function isUnderlines(str) {
  const reg = /^(?!_)(?!.*?_$)[a-z_]+$/;
  return reg.test(str)
}
/*镜像版本号 1.0.beta*/
export function isVersion(str) {
  const reg = /^\d+\.\d+(\.[a-zA-Z]+)?$/;
  return reg.test(str)
}
