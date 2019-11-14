import axios from 'axios'

// axios 配置
// axios.defaults.timeout = 5000;
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';
axios.defaults.withCredentials=true;
//返回状态判断
axios.interceptors.response.use((res) =>{
  // if(!res.data.success){
  //   return Promise.reject(res);
  // }
  return res;
}, (error) => {
  return Promise.reject(error);
});

export function fetchPost(url, params, config){
  return new Promise((resolve, reject) => {
    axios.post(url, params, config)
      .then(response => {
        resolve(response.data);
      })
      .catch((error) => {
        reject(error)
      })
  })
}

export function fetchGet(url, params){
  return new Promise((resolve, reject) => {
    axios.get(url, {params: params})
      .then(response => {
        resolve(response.data);
      })
      .catch((error) => {
        reject(error)
      })
  })
}
