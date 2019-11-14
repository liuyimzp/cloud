export function isObject (val) {
  return val !== null && typeof val === 'object'
}
export function setStore (key, val, type = 'localStorage') {
  if (isObject(val)) window[type].setItem(key, JSON.stringify(val))
  else window[type].setItem(key, val)
}
export function getStore (key, type = 'localStorage') {
  var val = window[type].getItem(key)
  try {
    return JSON.parse(val)
  } catch (e) {
    return val
  }
}
export function deleteStore (key, type = 'localStorage') {
  window[type].removeItem(key)
}
export function setSessionStore (key, val) {
  setStore(key, val, 'sessionStorage')
}
export function getSessionStore (key) {
  return getStore(key)
}
// 默认30minute
export function setExpireStore (key, val, ex = 1.8e6) {
  setStore(key, {v: val, t: Date.now() + ex})
}
export function getExpireStore (key) {
  var obj = getStore(key)
  if (obj.t > Date.now()) return obj.v
  else return false
}

export default {
  isObject,
  setStore,
  getStore,
  deleteStore,
  setSessionStore,
  getSessionStore,
  setExpireStore,
  getExpireStore
}
