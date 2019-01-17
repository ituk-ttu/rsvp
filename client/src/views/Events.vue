<template>
  <form>
    <label for="selection">Select event:</label>
    <select id="selection" v-model="selected">
      <option :value="this.$data._emptyEvent">-Create new event-</option>
      <option v-for="e in events" :value="e">
        {{e.eventName}}
      </option>
    </select>

    <label for="name">Event name:</label>
    <input type="text" id="name" v-model="selected.eventName">

    <input type="checkbox" id="public" v-model="selected.isPublic"><label class="light" for="public">Public</label>
    <br><br>

    <label for="info">Info:</label>
    <textarea id="info" v-model="selected.info"></textarea>

    <label for="place">Event place:</label>
    <input type="text" id="place" v-model="selected.eventPlace">


    <label class="light" for="time">Event time: </label><input type="datetime-local" id="time" v-model="properTime"><br>
    <label class="light" for="expire">Invite expire: </label><input type="datetime-local" id="expire" v-model="properExpire"><br>
    <br>

    <button class="submit" @click.prevent="onSubmit">Save</button>
    <button class="delete" @click.prevent="onDelete" v-if="selected.id !== -1">Delete</button>
    <label>Form by <a target="_blank" href="https://codepen.io/matthu185/pen/myyvgr">Matthew Largent</a> </label>
  </form>
</template>

<script>
  import moment from 'moment'
  import axios from '@/client'

  export default {
    name: 'EventEdit',
    data() {
      return {
        timeFormat: 'DD.MM.YYYY HH:mm',
        selected: '',
        _emptyEvent: {
          id: -1,
          creatorId: 'The Man',
          isPublic: false,
          eventName: '',
          info: '',
          eventPlace: '',
          eventTime: '',
          inviteExpire: ''
        },
        events: []
      }
    },
    methods: {
      getEventRequestData() {
        return {
          id: 0,
          isPublic: this.selected.isPublic,
          creatorId: this.selected.creatorId,
          eventName: this.selected.eventName,
          eventTime: this.selected.eventTime,
          eventPlace: this.selected.eventPlace,
          info: this.selected.info,
          inviteExpire: this.selected.inviteExpire
        }
      },
      onDelete() {
        if (confirm("Are you sure you want to delete this event?")) {
          axios.delete("/events/" + this.selected.id)
            .then(res => {
              this.getEvents()
            })
            .catch(err => {
              console.log(err)
            })
        }
      },
      onSubmit() {
        if (this.selected.id === -1) {
          axios.post("/events/", this.getEventRequestData())
            .then(res => {

            }).catch(err => {
              console.log(err)
          })
        } else {
          axios.put("/events/" + this.selected.id, this.getEventRequestData())
            .then(res => {

            }).catch(err => {
            console.log(err)
          })
        }
        this.getEvents()
      },
      getEvents() {
        this.selected = this.$data._emptyEvent
        axios.get('/events/')
          .then(res => {
            if (res.status === 200) {
              this.events = res.data
            }
          })
      }
    },
    mounted() {
      if (this.selected === '') {
        this.selected = this.$data._emptyEvent
      }
      this.getEvents()
    },
    computed: {
      properTime: {
        get() {
          if (this.selected.eventTime === '') {
            return ''
          } else {
            let m = moment(this.selected.eventTime, 'X')
            return m.format("YYYY-MM-DD") + 'T' + m.format("HH:mm")
          }
        },
        set(newValue) {
          if (newValue !== '') {
            this.selected.eventTime = moment(newValue).unix()
          } else {
            this.selected.eventTime = ''
          }
        }
      },
      properExpire: {
        get() {
          if (this.selected.inviteExpire === '') {
            return ''
          } else {
            let m = moment(this.selected.inviteExpire, 'X')
            return m.format("YYYY-MM-DD") + 'T' + m.format("HH:mm")
          }
        },
        set(newValue) {
          if (newValue !== '') {
            this.selected.inviteExpire = moment(newValue).unix()
          } else {
            this.selected.inviteExpire = ''
          }
        }
      }
    }
  }
</script>

<style scoped>
  *, *:before, *:after {
    -moz-box-sizing: border-box;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
  }

  body {
    font-family: 'Nunito', sans-serif;
    color: #384047;
  }

  form {
    max-width: 300px;
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
    margin: 0;
    outline: 0;
    padding: 15px;
    width: 100%;
    background-color: #e8eeef;
    color: #8a97a0;
    box-shadow: 0 1px 0 rgba(0,0,0,0.03) inset;
    margin-bottom: 30px;
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

  .number {
    background-color: #5fcf80;
    color: #fff;
    height: 30px;
    width: 30px;
    display: inline-block;
    font-size: 0.8em;
    margin-right: 4px;
    line-height: 30px;
    text-align: center;
    text-shadow: 0 1px 0 rgba(255,255,255,0.2);
    border-radius: 100%;
  }

  @media screen and (min-width: 480px) {
    form {
      max-width: 480px;
    }
  }
</style>
