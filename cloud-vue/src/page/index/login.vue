<template>
  <el-card class="login-box" shadow="hover" :style="{'top': top +'px'}">
    <div class="title">银海容器云登录</div>
    <el-button type="text" icon="el-icon-close" class="btn-close" @click="clickClose"></el-button>
    <el-form :model="formInfo" ref="formInfo" :rules="rules" label-width="0px">
      <el-form-item prop="userName">
        <el-input prefix-icon="icon-user" ref="user" class="input-style" v-model="formInfo.userName" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" prefix-icon="icon-password" ref="password"  class="input-style" v-model="formInfo.password" placeholder="请输入密码" @keyup.enter.native="fnLogin('formInfo')"></el-input>
      </el-form-item>
      <div style="text-align: center; padding-top: 10px;">
        <el-button type="primary" style="padding: 9px 40px" size="small" @click="fnLogin('formInfo')" :disabled="loading">登录</el-button>
      </div>
    </el-form>
  </el-card>
</template>
<script>
  import {login} from '../../axios/api'
  export default {
    name: 'login',
    props:['top','showLogin'],
    data() {
      return {
        loading : false,
        formInfo:{
          userName : '',
          password : '',
        },
        rules:{
          userName: [ { required: true, message: '请输入用户名', trigger: 'blur' } ],
          password: [ { required: true, message: '请输入密码', trigger: 'blur' } ],
        },
      }
    },
    watch:{
      showLogin(value){
        if(value === false){
          this.$refs['formInfo'].resetFields();
          this.$refs.user.blur();
          this.$refs.password.blur();
          this.$refs['formInfo'].clearValidate();
        }
      }
    },
    methods: {
      fnLogin(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.loading = true;
            login(this.formInfo).then((res)=>{
              if(res.success){
                this.clickClose();
                this.$router.push({name: 'console'});
              }else {
                this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
              }
              this.loading = false;
            }).catch(()=>{
              this.$message({ showClose: true, message: "登录失败", type: 'error' });
              this.loading = false;
            })
          }
        })
      },
      clickClose(){
        this.$refs['formInfo'].resetFields();
        this.$emit('closeEvent');
      }
    }
  }
</script>
<style lang="scss" type="text/scss">
  .login-box {
    .el-card__body { padding: 40px 50px 0; }
    &.el-card { border: none; }
    .input-style {
      .el-input__inner { padding-left: 40px; }
      .el-input__prefix { left: 12px; }
      .el-input__icon { width: 18px; display: inline-block; }
      .icon-user { @include bgimgCover($src:"img/user.png",$size: contain,$position:center); }
      .icon-password { @include bgimgCover($src:"img/password.png",$size: contain,$position:center); }
    }
  }
</style>
<style lang="scss" scoped type="text/scss">
  .title { font-size: 26px; color: $text-l0-color; text-align: center; padding-bottom: 20px;}
  .btn-close { position: absolute; right: 20px; top: 7px; }
  .login-box { width: 400px; height: 300px; position: absolute; z-index: 2; @include transition($time: 0.2s,$style:ease-in-out); margin: 0 0 0 -200px; left: 50%; background-color: rgba(255, 255, 255, 0.8); background-color: white\0; }
</style>
