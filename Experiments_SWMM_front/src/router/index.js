import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component:  resolve => (require(["@/components/HelloWorld"], resolve)),
    },
    { path: '/runParameters', 
      name: 'RunParameters', 
      component: resolve => (require(["@/components/RunParameters"], resolve)) }
  ],
  mode: 'history'
})
