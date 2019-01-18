<template>
  <div class="container">
    <div class="overlay"></div>

    <form>
      <label for="select-invite">Select invite:</label>
      <select id="select-invite" v-model="selected_invite">
        <option :value="this.$data._emptyInvite">-Create new invite-</option>
        <option v-for="i in invites" :value="i">
          {{i.name}}
        </option>
      </select>

      <label for="name">Person's name:</label>
      <input type="text" id="name" v-model="selected_invite.name">

      <input type="checkbox" id="coming" v-model="selected_invite.isComing"><label class="light" for="coming">Coming</label>
      <br><br>


      <label for="info">Info:</label>
      <textarea id="info" v-model="selected_invite.info"></textarea>

      <label for="select-event">Select event:</label>
      <select id="select-event" v-model="extractEventId">
        <option v-for="i in events" :value="i">
          {{i.eventName}}
        </option>
      </select>

      <button class="submit" @click.prevent="onSubmit">Save</button>
      <button class="delete" @click.prevent="onDelete" v-if="selected_invite.id !== -1">Delete</button>
      <label>Form by <a target="_blank" href="https://codepen.io/matthu185/pen/myyvgr">Matthew Largent</a> </label>
    </form>
    <div class="clear-fix"></div>
  </div>
</template>

<script>
  import axios from '@/client'

  export default {
    name: 'InviteEdit',
    data() {
      return {
        selected_invite: '',
        selected_event: '',
        _emptyInvite: {
          id: -1,
          eventId: 0,
          name: '',
          info: '',
          isComing: false
        },
        invites: [],
        events: []
      }
    },
    methods: {
      getInviteRequestData() {
        return {
          id: 0,
          eventId: this.selected_invite.eventId,
          name: this.selected_invite.name,
          info: this.selected_invite.info,
          isComing: this.selected_invite.isComing
        }
      },
      onDelete() {
        if (confirm("Are you sure you want to delete this invite?")) {
          axios.delete("/invites/" + this.selected_invite.id)
            .then(res => {
              this.getInvites()
            })
            .catch(err => {
              console.log(err)
            })
        }
      },
      onSubmit() {
        if (this.selected_invite.id === -1) {
          axios.post("/invites/", this.getInviteRequestData())
            .then(res => {

            }).catch(err => {
            console.log(err.response)
          })
        } else {
          axios.put("/invites/" + this.selected_invite.id, this.getInviteRequestData())
            .then(res => {

            }).catch(err => {
            console.log(err.response)
          })
        }
      },
      getInvites() {
        this.selected_invite = this.$data._emptyInvite
        axios.get('/invites/')
          .then(res => {
            if (res.status === 200) {
              this.invites = res.data
            }
          })
      },
      getEvents() {
        this.selected_event = ''
        axios.get('/events/')
          .then(res => {
            if (res.status === 200) {
              this.events = res.data
            }
          })
      }
    },
    computed: {
      extractEventId: {
        get() {
          return this.events.find(x => x.id === this.selected_invite.eventId)
        },
        set(event) {
          this.selected_event = event
          this.selected_invite.eventId = event.id
        }
      }
    },
    mounted() {
      if (this.selected_invite === '') {
        this.selected_invite = this.$data._emptyInvite
      }
      this.getEvents()
      this.getInvites()
    }
  }
</script>

<style scoped>
  *, *:before, *:after {
    -moz-box-sizing: border-box;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
  }

  .container {
    font-family: 'Nunito', sans-serif;
    color: #384047;
    width: 460px;

    display: flex;
    position: relative;
  }

  .overlay {
    display: none;
    border-radius: 8px;
    background: rgba(139, 139, 139, 0.5);
    z-index: 1;

    flex: 1;
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;

  }

  form {
    flex: 1;
    width: 100%;

    margin: 10px auto;
    padding: 10px 20px;
    background: #f4f7f8;
    border-radius: 8px;
  }

  h1 {
    margin: 0 0 30px 0;
    text-align: center;
  }

  input[type="text"],
  input[type="password"],
  input[type="date"],
  input[type="datetime"],
  input[type="email"],
  input[type="number"],
  input[type="search"],
  input[type="tel"],
  input[type="time"],
  input[type="url"],
  textarea,
  select {
    background: rgba(255,255,255,0.1);
    border: none;
    font-size: 16px;
    height: auto;
    outline: 0;
    padding: 15px;
    width: 100%;
    background-color: #e8eeef;
    color: #8a97a0;
    box-shadow: 0 1px 0 rgba(0,0,0,0.03) inset;
    margin: 0 0 30px;
  }

  textarea {
    resize: vertical;
  }

  input[type="radio"],
  input[type="checkbox"] {
    margin: 0 4px 8px 0;
  }

  select {
    padding: 6px;
    height: 32px;
    border-radius: 2px;
  }

  button {
    padding: 19px 39px 18px 39px;
    color: #FFF;
    font-size: 18px;
    text-align: center;
    font-style: normal;
    border-radius: 5px;
    width: 100%;
    border: 1px solid #3ac162;
    border-width: 1px 1px 3px;
    margin-bottom: 10px;
    cursor: pointer;
  }

  button.submit {
    background-color: #4bc970;
    box-shadow: 0 -1px 0 rgba(255, 255, 255, 0.1) inset;
  }

  button.delete {
    background-color: #ff0000;
    box-shadow: 0 -1px 0 rgba(230, 0, 0, 0.1) inset;
  }

  fieldset {
    margin-bottom: 30px;
    border: none;
  }

  legend {
    font-size: 1.4em;
    margin-bottom: 10px;
  }

  label {
    display: block;
    margin-bottom: 8px;
  }

  label.light {
    font-weight: 300;
    display: inline;
  }

  @media screen and (min-width: 480px) {
    form {
      max-width: 480px;
    }
  }

  .clear-fix {
    clear: both;
  }
</style>
