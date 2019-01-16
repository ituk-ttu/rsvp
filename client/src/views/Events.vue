<template>
    <div class="events">
      <form v-on:submit.prevent="onSubmit">
        <input type="text" v-model="eventName" placeholder="Event Name">
        <input type="checkbox" v-model="isPublic" placeholder="Public?">
        <input type="text" v-model="eventInfo" placeholder="Info">
        <input type="text" v-model="eventPlace" placeholder="Place">
        <input type="text" v-model="eventTime" :placeholder="this.timeFormat">
        <input type="text" v-model="inviteExpire" :placeholder="this.timeFormat">
        <button>Create</button>
      </form>
    </div>
</template>

<script>
import moment from 'moment'
import axios from '@/client'

export default {
  name: 'EventsView',
  data: function () {
    return {
      timeFormat: 'DD.MM.YYYY HH:mm',
      isPublic: false,
      eventName: 'Keikka',
      eventInfo: 'Tulge kohale',
      eventPlace: 'Ituk',
      eventTime: '17.01.2019 05:35',
      inviteExpire: '17.01.2019 03:35'
    }
  },
  methods: {
    computeTime: function(datetime) {
      return moment(datetime, this.timeFormat).unix()
    },
    onSubmit: function () {
      axios.post('/events/', {
        id: 0,
        creatorId: "Man",
        isPublic: this.isPublic,
        eventName: this.eventName,
        info: this.eventInfo,
        eventPlace: this.eventPlace,
        eventTime: this.computeTime(this.eventTime),
        inviteExpire: this.computeTime(this.inviteExpire)
      }).catch(err => {
        console.log(err)
      })
    }
  }
}
</script>

<style scoped>

</style>
