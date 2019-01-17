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
  export default {
    name: 'Invites'
  }
</script>

<style scoped>

</style>
