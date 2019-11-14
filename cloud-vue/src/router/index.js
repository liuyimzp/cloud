import Vue from 'vue'
import Router from 'vue-router'

/*首页*/
import welcome from '@/page/index/welcome'
import index from '@/page/index/index'
import console from '@/page/index/console'

/*容器集群管理*/
import addCluster from '@/page/container/addCluster'
import nodesManage from '@/page/container/nodesManage'
import saveManage from '@/page/container/saveManage'
import cloudSaveManage from '@/page/container/cloudSaveManage'
import addCephCluster from '@/page/container/addCephCluster'
import cephCluster from '@/page/container/cephCluster'
import cephPool from '@/page/container/cephPool'
/*应用管理*/
import appManage from '@/page/application/appManage'
import nameSpace from '@/page/application/nameSpace'
import podInfo from '@/page/application/podInfo'
import runInfo from '@/page/application/runInfo'
import configMapManage from '@/page/application/configMapManage'
import envConfig from '@/page/application/envConfig'
import appSteps from '@/page/application/appSteps'
import resourceConfig from '@/page/application/resourceConfig'
import changeImage from '@/page/application/changeImage'
import envParamConfig from '@/page/application/envParamConfig'
import configMap from '@/page/application/configMap'
import serviceConfig from '@/page/application/serviceConfig'
import success from '@/page/application/success'
/*应用仓库*/
import repoManage from '@/page/repository/repoManage'
import jenkins from '@/page/repository/jenkins'
import repoImage from '@/page/repository/repoImage'
import jenkinsIntegration from '@/page/repository/jenkinsIntegration'
/*系统设置*/
import scriptManage from '@/page/setting/scriptManage'
import scriptDetail from '@/page/setting/scriptDetail'
import author from '@/page/setting/author'
import systemproperty from '@/page/setting/systemproperty'

/*监控*/
import monitorCluster from '@/page/monitor/monitorCluster'
import monitorNode from '@/page/monitor/monitorNode'
import monitorApp from '@/page/monitor/monitorApp'
import monitorConfig from '@/page/monitor/monitorConfig'

Vue.use(Router);

export default new Router({
  mode:'hash',
  routes: [
    {
      path: '/',
      component: welcome,
      name: 'welcome',
    },
    {
      path: '/index',
      component: index,
      children: [
        {
          path: '/',
          name: 'console',
          component: console,
          meta:{
            keepAlive:false
          }
        },
        /*容器集群管理*/
        {
          path: '/addCluster',
          name: 'addCluster',
          component: addCluster,
          meta:{
            keepAlive:false
          }
        },
        {
          path: '/nodesManage',
          name: 'nodesManage',
          component: nodesManage,
          meta:{
            keepAlive:false
          }
        },
        {
          path: '/saveManage',
          name: 'saveManage',
          component: saveManage,
          meta:{
            keepAlive:false
          }
        },
        {
          path: '/cloudSaveManage',
          name: 'cloudSaveManage',
          component: cloudSaveManage,
          meta:{
            keepAlive:false
          }
        },
        {
          path: '/cephCluster',
          name: 'cephCluster',
          component: cephCluster,
          meta:{
            keepAlive:false
          }
        },
        {
          path: '/cephPool',
          name: 'cephPool',
          component: cephPool,
          meta:{
            keepAlive:false
          }
        },
        {
          path: '/addCephCluster',
          name: 'addCephCluster',
          component: addCephCluster,
          meta:{
            keepAlive:false
          }
        },
        /*应用管理*/
        {
          path: '/appManage',
          name: 'appManage',
          component: appManage,
          meta:{
            keepAlive:false,
          }
        },
        {
          path: '/nameSpace',
          name: 'nameSpace',
          component: nameSpace,
          meta:{
            keepAlive:false,
          }
        },
        {
          path: 'podInfo',
          name: 'podInfo',
          component: podInfo,
          meta:{
            parent:'/appManage',
            keepAlive:false,
          }
        },
        {
          path: 'runInfo',
          name: 'runInfo',
          component: runInfo,
          meta:{
            parent:'/appManage',
            keepAlive:false,
          }
        },
        {
          path: '/appSteps',
          component: appSteps,
          meta:{
            parent:'/appManage'
          },
          children: [
            {
              path: '/',
              component: resourceConfig,
              meta:{
                parent:'/appManage',
                keepAlive:false,
              }
            },
            {
              path: 'resourceConfig',
              name: 'resourceConfig',
              component: resourceConfig,
              meta:{
                parent:'/appManage',
                keepAlive:false,
              }
            },
            {
              path: 'envParamConfig',
              name: 'envParamConfig',
              component: envParamConfig,
              meta:{
                parent:'/appManage',
                keepAlive:false,
              }
            },
            {
              path: 'configMap',
              name: 'configMap',
              component: configMap,
              meta:{
                parent:'/appManage',
                keepAlive:false,
              }
            },
            {
              path: 'serviceConfig',
              name: 'serviceConfig',
              component: serviceConfig,
              meta:{
                parent:'/appManage',
                keepAlive:false,
              }
            },
            {
              path: 'success',
              name: 'success',
              component: success,
              meta:{
                parent:'/appManage',
                keepAlive:false,
              }
            }
          ]
        },
        {
          path: '/changeImage',
          name: 'changeImage',
          component: changeImage,
          meta:{
            parent:'/appManage',
            keepAlive:false,
          }
        },
        {
         path: 'configMapManage',
         name: 'configMapManage',
         component: configMapManage,
         meta:{
         parent:'/appManage',
         keepAlive:false,
         }
         },
        {
          path: '/envConfig',
          name: 'envConfig',
          component: envConfig,
        },
        /*镜像仓库管理*/
        {
          path: '/repoManage',
          name: 'repoManage',
          component: repoManage,
          meta:{
            keepAlive:false
          }
        },
        /*jenkins管理*/
        {
          path: '/jenkins',
          name: 'jenkins',
          component: jenkins,
          meta:{
            keepAlive:false
          }
        },
        {
          path: '/repoImage',
          name: 'repoImage',
          component: repoImage,
          meta:{
            parent:'/repoManage',
            keepAlive:false
          }
        },
        /*系统设置*/
        {
          path: '/scriptManage',
          name: 'scriptManage',
          component: scriptManage,
        },
        {
          path: '/scriptDetail',
          name: 'scriptDetail',
          component: scriptDetail,
          meta:{
            keepAlive:false
          }
        },
        /*监控*/
        {
          path: '/monitorCluster',
          name: 'monitorCluster',
          component: monitorCluster,
          meta:{
            keepAlive:false
          }
        },
        {
          path: '/monitorApp',
          name: 'monitorApp',
          component: monitorApp,
          meta:{
            keepAlive:false
          }
        },
        {
          path: '/monitorNode',
          name: 'monitorNode',
          component: monitorNode,
          meta:{
            keepAlive:false
          }
        },
        {
          path: '/monitorConfig',
          name: 'monitorConfig',
          component: monitorConfig,
          meta:{
            keepAlive:false
          }
        },
        /*人员及权限管理*/
        {
          path: '/author',
          name: 'author',
          component: author,
          meta:{
            keepAlive:false
          }
        },
        /*全局变量管理*/
        {
          path: '/systemproperty',
          name: 'systemproperty',
          component: systemproperty,
          meta:{
            keepAlive:false
          }
        },
        /*全局变量管理*/
        {
          path: '/jenkinsIntegration',
          name: 'jenkinsIntegration',
          component: jenkinsIntegration,
          meta:{
            keepAlive:false
          }
        }
      ]
    }
  ]
})
