import Vue from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { library } from '@fortawesome/fontawesome-svg-core'

import { faCheckCircle, faTimesCircle } from '@fortawesome/free-solid-svg-icons'

library.add(faTimesCircle)
library.add(faCheckCircle)

Vue.component('font-awesome-icon', FontAwesomeIcon)
