<template>
    <div class="events">
      <table>
        <tr>
          <th>Event Name</th>
          <th>Is public?</th>
          <th>Info</th>
          <th>Place</th>
          <th>Time</th>
          <th>Invites expire</th>
        </tr>
        <tr v-for="ev in events" @click.stop="handleRow">
          <td><input type="text" v-model="ev.eventName" placeholder="Event Name"></td>
          <td><input type="checkbox" v-model="ev.isPublic" placeholder="Public?"></td>
          <td><input type="text" v-model="ev.eventInfo" placeholder="Info"></td>
          <td><input type="text" v-model="ev.eventPlace" placeholder="Place"></td>
          <td><input type="text" v-model="ev.eventTime" :placeholder="timeFormat"></td>
          <td><input type="text" v-model="ev.inviteExpire" :placeholder="timeFormat"></td>
          <td><button>X</button></td>
          {{getTime(ev.eventTime)}}
          {{getTime(ev.inviteExpire)}}
        </tr>
      </table>
      <br>
      {{this.events}}
    </div>
</template>

<script>
import axios from '@/client'
import moment from 'moment'

export default {
  name: 'EventsView',
  components: {

  },
  data: function () {
    return {
      timeFormat: 'DD.MM.YYYY HH:mm',
      events: [
        {
          isPublic: false,
          eventName: 'Keikka',
          info: 'Tulge kohale',
          eventPlace: 'Ituk',
          eventTime: '17.10.2019 05:35',
          inviteExpire: '17.10.2019 03:35'
        }
      ]
    }
  },
  methods: {
    getTime: function (time){
      return moment(time, this.timeFormat).unix()
    },
    handleRow: function (event) {
      let row = event.target.parentElement.parentElement
      let table = row.parentElement

      if (event.target.nodeName !== 'BUTTON') {
        if (table.lastElementChild === row && event.target.type !== "checkbox") {
          this.events.push({
            isPublic: false,
            eventName: '',
            info: '',
            eventPlace: '',
            eventTime: '',
            inviteExpire: ''
          })
        }
      } else  {
        if (this.events.length > 1) {
          this.events.splice(row.rowIndex - 1, 1)
        }
      }
    },
    onSubmit: function () {
      axios.post('/events/', {
        id: 0,
        creatorId: "Tha Man",
        isPublic: this.isPublic,
        eventName: this.eventName,
        info: this.eventInfo,
        eventPlace: this.eventPlace,
        //eventTime: this.computeTime(this.eventTime),
        //inviteExpire: this.computeTime(this.inviteExpire)
      }).then(response => {
        console.log(response.data)
      }).catch(err => {
        console.log(err.response.status)
        console.log(err.response.data)
      })
    }
  }
}
</script>

<style scoped>

</style>
