<template>
  <div class="user-info">
    <div class="user-head"></div>
    <div>{{hello}}{{userName}}</div>
    <div class="logout" @click="fnLogout">
      <i class="cloud-icon-exit"></i>
      <span class="logout-word">退出登录</span>
    </div>
  </div>
</template>
<script>
  import { logout, getUser } from '../../axios/api'
  import { getStore, setStore, deleteStore } from '../../assets/js/storage'
  export default {
    name: 'userInfo',
    data(){
      return{
        userName:'',
        positionId:0,
      }
    },
    computed:{
      hello(){
        let now = new Date(), hour = now.getHours(), hello = '';
        if(hour < 6){ hello = "凌晨好，"}
        else if (hour < 9){ hello = "早上好，"}
        else if (hour < 12){ hello = "上午好，"}
        else if (hour < 14){ hello = "中午好，"}
        else if (hour < 17){ hello = "下午好，"}
        else if (hour < 19){ hello = "傍晚好，"}
        else if (hour < 22){ hello = "晚上好，"}
        else { hello = "夜里好，"}
        return hello;
      }
    },
    mounted(){
      this.fnGetUser();
      this.userName = getStore('userName');
    },
    methods:{
      fnGetUser(){
        getUser().then((res)=>{
          if(res.success){
            if(res.data.positionId!=1){
              document.getElementById("5").style.display="none";
            }
            if(getStore('userName')){
              if(getStore('userName') != res.data.name){
                setStore('userName',res.data.name);
              }
            }else {
              setStore('userName',res.data.name);
              this.userName = getStore('userName');
            }
          }else {
            this.$router.push({name: 'welcome'});
          }
        }).catch(() => {
          this.$router.push({name: 'welcome'});
        });
      },
      fnLogout(){
        logout().then((res)=>{
          if(res.success){
            deleteStore('userName');
            this.$router.push({name: 'welcome'});
          }
        });
      }
    }
  }
</script>
<style lang="scss" scoped type="text/scss">
  @import "../../assets/icon/iconfont.css";
  .user-info { height: $header-height; line-height: $header-height; float: right; color: $main-ft-color; @include flexbox-display-flex(row);  }
  .user-head { width: $user-height; height: $user-height; @include bgimgCover($src:"../../assets/image/head.png",$size:contain,$position:center); margin-top: ( $header-height - $user-height ) / 2 ; margin-right: 10px; box-sizing: border-box; border-radius: 100%; }
  .logout { @include flexbox-display-flex(row); cursor: pointer; line-height: $header-height; margin-left: 10px;  padding-right: 10px; overflow: hidden;
    .cloud-icon-exit { margin-right: 5px; }
    .logout-word { display: inline-block; width: 0; height: 0; opacity: 0; @include transition(); }
    &:hover{ .logout-word { width: 60px; opacity: 1; } }
  }
</style>
