<template>
  <div class="eventform">
    <input type="text" v-model="eventName" placeholder="Event Name">
    <input type="checkbox" v-model="isPublic" placeholder="Public?">
    <input type="text" v-model="eventInfo" placeholder="Info">
    <input type="text" v-model="eventPlace" placeholder="Place">
    <input type="text" v-model="$data._eventTime" :placeholder="this.timeFormat">
    <input type="text" v-model="$data._inviteExpire" :placeholder="this.timeFormat">
  </div>
</template>

<script>
  import moment from 'moment'

  export default {
    name: "EventForm",
  data: function () {
    return {
      timeFormat: 'DD.MM.YYYY HH:mm',
      isPublic: false,
      eventName: 'Keikka',
      eventInfo: 'Tulge kohale',
      eventPlace: 'Ituk',
      _eventTime: '17.10.2019 05:35',
      _inviteExpire: '17.10.2019 03:35'
    }
  },
  computed: {
    eventTime: function () {
      return moment(this.$data._eventTime, this.timeFormat).unix()
    },
    inviteExpire: function () {
      return moment(this.$data._inviteExpire, this.timeFormat).unix()
    }
  }
}
</script>

<style scoped>
  .error {
    border: red 2px solid;
  }
</style>
