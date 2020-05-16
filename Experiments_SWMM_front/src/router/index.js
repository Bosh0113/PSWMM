import Vue from 'vue'
import Router from 'vue-router'

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
      component: resolve => (require(["@/components/Parameter/RunParameters"], resolve))
    },
    { path: '/collaborativeEditor', 
      name: 'CollaborativeEditor', 
      component: resolve => (require(["@/components/DataProcess/CollaborativeEditor"], resolve))
    },
    { path: '/tableChart', 
      name: 'TableChart', 
      component: resolve => (require(["@/components/Visualization/TableChart"], resolve))
    },
    { path: '/timeSeries', 
      name: 'TimeSeries', 
      component: resolve => (require(["@/components/Visualization/TimeSeries"], resolve))
    },
    { path: '/floodingNodes', 
      name: 'floodingNodes', 
      component: resolve => (require(["@/components/Evaluation/floodingNodes"], resolve))
    }
  ],
  mode: 'history'
})
