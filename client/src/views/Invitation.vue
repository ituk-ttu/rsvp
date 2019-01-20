<template>
  <div>
    <p>Dear {{invite.name}}</p>
    <p>{{invite.info}}</p>
    <p>---------------------------------------------</p>
    <p>What: {{event.eventName}}</p>
    <p>Where: {{event.eventPlace}}</p>
    <p>When: {{formatTime}}</p>
    <textarea v-model="event.info" readonly></textarea><br>
    <p>You in?</p>
    <button @click="sendAnswer(true)">Yay</button>
    <button @click="sendAnswer(false)">Nay</button> <br>
  </div>
</template>

<script>
import axios from '@/client'
import moment from 'moment'

export default {
  name: 'Invitation',
  data () {
    return {
      _timeFormat: 'DD.MM.YYYY HH:mm',
      id: 0,
      invite: '',
      event: ''
    }
  },
  created () {
    this.id = this.$route.params.id

    axios.post('/invites/get/' + this.id, {})
      .then(res => {
        this.invite = res.data.invite
        this.event = res.data.event
      }).catch(err => {
        console.log(err)
      })
  },
  computed: {
    formatTime () {
      return moment(this.event.eventTime, 'X').format(this.$data._timeFormat)
    }
  },
  methods: {
    sendAnswer (answer) {
      this.invite.isComing = answer
      axios.put('/invites/' + this.id, this.invite)
        .then(res => {
          console.log('Timmis')
        }).catch(err => {
          console.log(err.response)
        })
    }
  }
}
</script>

<style scoped>

</style>
